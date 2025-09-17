package by.delaidelo.tests.testworks.services;

import by.delaidelo.tests.testworks.dao.GoodsShipmentDocumentRepository;
import by.delaidelo.tests.testworks.dao.WarehouseOperationRepository;
import by.delaidelo.tests.testworks.domain.GoodsShipmentDocument;
import by.delaidelo.tests.testworks.domain.GoodsShipmentItem;
import by.delaidelo.tests.testworks.dto.GoodsShipmentDocumentDto;
import by.delaidelo.tests.testworks.dto.ProductDto;
import by.delaidelo.tests.testworks.enums.OperationManagementDocumentType;
import by.delaidelo.tests.testworks.enums.WarehouseOperationType;
import by.delaidelo.tests.testworks.mappers.GoodsShipmentDocumentMapper;
import by.delaidelo.tests.testworks.mappers.GoodsShipmentItemMapper;
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
public class GoodsShipmentService {

    private final GoodsShipmentDocumentRepository goodsShipmentDocumentRepository;
    private final GoodsShipmentDocumentMapper goodsShipmentDocumentMapper;
    private final GoodsShipmentItemMapper goodsShipmentItemMapper;
    private final WarehouseOperationService warehouseOperationService;
    private final WarehouseOperationRepository warehouseOperationRepository;


    @Transactional(readOnly = true)
    public Page<GoodsShipmentDocumentDto> find(String query, Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            return goodsShipmentDocumentRepository.findAllByOrderByIdDesc(pageable).map(goodsShipmentDocumentMapper::toDto);
        }
        return goodsShipmentDocumentRepository.findByQuery(query.trim(), pageable)
                .map(goodsShipmentDocumentMapper::toDto);
    }

    @Transactional(readOnly = true)
    public GoodsShipmentDocumentDto findById(@NotNull Long id) {
        return goodsShipmentDocumentMapper.toDto(goodsShipmentDocumentRepository.findById(id).orElseThrow());
    }

    @Transactional
    public Long create(@NotNull GoodsShipmentDocumentDto dto) {
        GoodsShipmentDocument entity = goodsShipmentDocumentMapper.fromDto(dto);
        if (entity.getItems() != null) {
            for (GoodsShipmentItem item : entity.getItems()) {
                item.setDocument(entity);
            }
        }
        goodsShipmentDocumentRepository.save(entity);
        return entity.getId();
    }

    @Transactional
    public void update(Long id, GoodsShipmentDocumentDto dto) {
        GoodsShipmentDocument entity = goodsShipmentDocumentRepository.findById(id).orElseThrow();
        goodsShipmentDocumentMapper.update(entity, dto);
        entity.getItems().clear();
        if (dto.getItems() != null) {
            for (ProductDto itemDto : dto.getItems()) {
                GoodsShipmentItem item = goodsShipmentItemMapper.fromDto(itemDto);
                item.setDocument(entity);
                entity.getItems().add(item);
            }
        }
        goodsShipmentDocumentRepository.save(entity);
    }


    @Transactional
    public void delete(@NotNull Long id) {
        final var entity = goodsShipmentDocumentRepository.findById(id).orElseThrow();
        goodsShipmentDocumentRepository.delete(entity);
    }

    @Transactional
    public void conduct(@NotNull Long documentId) {
        final var doc = goodsShipmentDocumentRepository.findById(documentId).orElseThrow();
        if (doc.isConducted()) return;
        for (GoodsShipmentItem item : doc.getItems()) {
            final var op = WarehouseOperationBuilder.builder()
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
            final var opId = warehouseOperationService.create(op);
            warehouseOperationService.process(opId);
        }
        doc.setConducted(true);
    }

    @Transactional
    public void unConduct(@NotNull Long documentId) {
        final var doc = goodsShipmentDocumentRepository.findById(documentId).orElseThrow();
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