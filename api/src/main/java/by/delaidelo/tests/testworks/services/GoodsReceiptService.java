package by.delaidelo.tests.testworks.services;

import by.delaidelo.tests.testworks.dao.GoodsReceiptDocumentRepository;
import by.delaidelo.tests.testworks.dao.WarehouseOperationRepository;
import by.delaidelo.tests.testworks.domain.GoodsReceiptDocument;
import by.delaidelo.tests.testworks.domain.GoodsReceiptItem;
import by.delaidelo.tests.testworks.dto.GoodsReceiptDto;
import by.delaidelo.tests.testworks.dto.ProductDto;
import by.delaidelo.tests.testworks.enums.OperationManagementDocumentType;
import by.delaidelo.tests.testworks.enums.WarehouseOperationType;
import by.delaidelo.tests.testworks.mappers.GoodsReceiptDocumentMapper;
import by.delaidelo.tests.testworks.mappers.GoodsReceiptItemMapper;
import by.delaidelo.tests.testworks.services.warehouse.WarehouseOperationService;
import by.delaidelo.tests.testworks.utils.WarehouseOperationBuilder;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GoodsReceiptService {
    private final GoodsReceiptDocumentRepository goodsReceiptDocumentRepository;
    private final GoodsReceiptDocumentMapper goodsReceiptDocumentMapper;
    private final GoodsReceiptItemMapper mapperItem;
    private final WarehouseOperationService warehouseOperationService;
    private final WarehouseOperationRepository warehouseOperationRepository;


    @Transactional(readOnly = true)
    public Page<GoodsReceiptDto> find(String query, Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            return goodsReceiptDocumentRepository.findAllByOrderByIdDesc(pageable).map(goodsReceiptDocumentMapper::toDto);
        }
        return goodsReceiptDocumentRepository.findByQuery(query.trim(), pageable)
                .map(goodsReceiptDocumentMapper::toDto);
    }

    @Transactional(readOnly = true)
    public GoodsReceiptDto findById(@NotNull Long id) {
        return goodsReceiptDocumentMapper.toDto(goodsReceiptDocumentRepository.findById(id).orElseThrow());
    }

    @Transactional
    public Long create(@NotNull GoodsReceiptDto dto) {
        GoodsReceiptDocument entity = goodsReceiptDocumentMapper.fromDto(dto);
        if (entity.getItems() != null) {
            for (GoodsReceiptItem item : entity.getItems()) {
                item.setDocument(entity);
            }
        }
        goodsReceiptDocumentRepository.save(entity);
        return entity.getId();
    }

    @Transactional
    public void update(Long id, GoodsReceiptDto dto) {
        GoodsReceiptDocument entity = goodsReceiptDocumentRepository.findById(id).orElseThrow();
        goodsReceiptDocumentMapper.update(entity, dto);
        entity.getItems().clear();
        if (dto.getItems() != null) {
            for (ProductDto itemDto : dto.getItems()) {
                GoodsReceiptItem item = mapperItem.fromDto(itemDto);
                item.setDocument(entity);
                entity.getItems().add(item);
            }
        }
        goodsReceiptDocumentRepository.save(entity);
    }


    @Transactional
    public void delete(@NotNull Long id) {
        final var entity = goodsReceiptDocumentRepository.findById(id).orElseThrow();
        goodsReceiptDocumentRepository.delete(entity);
    }

    @Transactional
    public void conduct(@NotNull Long documentId) {
        final var doc = goodsReceiptDocumentRepository.findById(documentId).orElseThrow();
        if (doc.isConducted()) return;
        for (GoodsReceiptItem item : doc.getItems()) {
            final var warehouseOperation = WarehouseOperationBuilder.builder()
                    .type(WarehouseOperationType.INCOMING)
                    .warehouse(doc.getWarehouse())
                    .itemType(item.getItemType())
                    .ownerDocumentType(OperationManagementDocumentType.WAREHOUSE_INCOMING_DOCUMENT)
                    .ownerDocumentId(doc.getId())
                    .ownerDocumentInfo(doc.getDocumentNumber())
                    .operationDate(doc.getDocumentDate())
                    .quantity(item.getQuantity())
                    .price(item.getPrice())
                    .totalSum(item.getTotalSum())
                    .build();
            final var opId = warehouseOperationService.create(warehouseOperation);
            warehouseOperationService.process(opId);
        }
        doc.setConducted(true);
    }

    @Transactional
    public void unConduct(@NotNull Long documentId) {
        final var doc = goodsReceiptDocumentRepository.findById(documentId).orElseThrow();
        if (!doc.isConducted()) return;
        warehouseOperationRepository.findAll().stream()
                .filter(o -> o.getOwnerDocType() == OperationManagementDocumentType.WAREHOUSE_INCOMING_DOCUMENT)
                .filter(o -> o.getOwnerDocId() == doc.getId())
                .forEach(o -> {
                    warehouseOperationService.rollback(o.getId());
                    warehouseOperationRepository.delete(o);
                });
        doc.setConducted(false);
    }
}


