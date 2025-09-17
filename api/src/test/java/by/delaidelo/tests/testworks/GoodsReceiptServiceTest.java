package by.delaidelo.tests.testworks;

import by.delaidelo.tests.testworks.dao.GoodsReceiptDocumentRepository;
import by.delaidelo.tests.testworks.dao.WarehouseOperationRepository;
import by.delaidelo.tests.testworks.domain.GoodsReceiptDocument;
import by.delaidelo.tests.testworks.dto.GoodsReceiptDto;
import by.delaidelo.tests.testworks.mappers.GoodsReceiptDocumentMapper;
import by.delaidelo.tests.testworks.mappers.GoodsReceiptItemMapper;
import by.delaidelo.tests.testworks.services.GoodsReceiptService;
import by.delaidelo.tests.testworks.services.warehouse.WarehouseOperationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GoodsReceiptServiceTest {

    @Mock
    private GoodsReceiptDocumentRepository goodsReceiptDocumentRepository;
    @Mock
    private GoodsReceiptDocumentMapper goodsReceiptDocumentMapper;
    @Mock
    private GoodsReceiptItemMapper mapperItem;
    @Mock
    private WarehouseOperationService warehouseOperationService;
    @Mock
    private WarehouseOperationRepository warehouseOperationRepository;

    @InjectMocks
    private GoodsReceiptService service;

    private GoodsReceiptDocument document;
    private GoodsReceiptDto dto;

    @BeforeEach
    void setUp() {
        document = new GoodsReceiptDocument();
        document.setId(1L);
        document.setDocumentNumber("DOC-001");

        dto = new GoodsReceiptDto();
        dto.setId(1L);
        dto.setDocumentNumber("DOC-001");
    }

    @Test
    void find_shouldReturnPageOfDocuments() {
        Pageable pageable = PageRequest.of(0, 10);
        when(goodsReceiptDocumentRepository.findAllByOrderByIdDesc(pageable)).thenReturn(new PageImpl<>(Collections.singletonList(document)));
        when(goodsReceiptDocumentMapper.toDto(document)).thenReturn(dto);

        Page<GoodsReceiptDto> result = service.find(null, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(goodsReceiptDocumentRepository, times(1)).findAllByOrderByIdDesc(pageable);
    }

    @Test
    void findById_shouldReturnDocument() {
        when(goodsReceiptDocumentRepository.findById(1L)).thenReturn(Optional.of(document));
        when(goodsReceiptDocumentMapper.toDto(document)).thenReturn(dto);

        GoodsReceiptDto result = service.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(goodsReceiptDocumentRepository, times(1)).findById(1L);
    }

    @Test
    void create_shouldSaveDocument() {
        when(goodsReceiptDocumentMapper.fromDto(dto)).thenReturn(document);
        when(goodsReceiptDocumentRepository.save(document)).thenReturn(document);

        Long id = service.create(dto);

        assertNotNull(id);
        verify(goodsReceiptDocumentRepository, times(1)).save(document);
    }

    @Test
    void update_shouldUpdateDocument() {
        when(goodsReceiptDocumentRepository.findById(1L)).thenReturn(Optional.of(document));

        service.update(1L, dto);

        verify(goodsReceiptDocumentMapper, times(1)).update(document, dto);
        verify(goodsReceiptDocumentRepository, times(1)).save(document);
    }

    @Test
    void delete_shouldDeleteDocument() {
        when(goodsReceiptDocumentRepository.findById(1L)).thenReturn(Optional.of(document));

        service.delete(1L);

        verify(goodsReceiptDocumentRepository, times(1)).delete(document);
    }
}