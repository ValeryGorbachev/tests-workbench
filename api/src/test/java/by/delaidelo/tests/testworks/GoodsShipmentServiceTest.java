package by.delaidelo.tests.testworks;

import by.delaidelo.tests.testworks.dao.GoodsShipmentDocumentRepository;
import by.delaidelo.tests.testworks.dao.WarehouseOperationRepository;
import by.delaidelo.tests.testworks.domain.GoodsShipmentDocument;
import by.delaidelo.tests.testworks.dto.GoodsShipmentDocumentDto;
import by.delaidelo.tests.testworks.mappers.GoodsShipmentDocumentMapper;
import by.delaidelo.tests.testworks.mappers.GoodsShipmentItemMapper;
import by.delaidelo.tests.testworks.services.GoodsShipmentService;
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
class GoodsShipmentServiceTest {

    @Mock
    private GoodsShipmentDocumentRepository repository;
    @Mock
    private GoodsShipmentDocumentMapper documentMapper;
    @Mock
    private GoodsShipmentItemMapper itemMapper;
    @Mock
    private WarehouseOperationService warehouseOperationService;
    @Mock
    private WarehouseOperationRepository warehouseOperationRepository;

    @InjectMocks
    private GoodsShipmentService service;

    private GoodsShipmentDocument document;
    private GoodsShipmentDocumentDto dto;

    @BeforeEach
    void setUp() {
        document = new GoodsShipmentDocument();
        document.setId(1L);
        document.setDocumentNumber("DOC-001");

        dto = new GoodsShipmentDocumentDto();
        dto.setId(1L);
        dto.setDocumentNumber("DOC-001");
    }

    @Test
    void find_shouldReturnPageOfDocuments() {
        Pageable pageable = PageRequest.of(0, 10);
        when(repository.findAllByOrderByIdDesc(pageable)).thenReturn(new PageImpl<>(Collections.singletonList(document)));
        when(documentMapper.toDto(document)).thenReturn(dto);

        Page<GoodsShipmentDocumentDto> result = service.find(null, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(repository, times(1)).findAllByOrderByIdDesc(pageable);
    }

    @Test
    void findById_shouldReturnDocument() {
        when(repository.findById(1L)).thenReturn(Optional.of(document));
        when(documentMapper.toDto(document)).thenReturn(dto);

        GoodsShipmentDocumentDto result = service.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void create_shouldSaveDocument() {
        when(documentMapper.fromDto(dto)).thenReturn(document);
        when(repository.save(document)).thenReturn(document);

        Long id = service.create(dto);

        assertNotNull(id);
        verify(repository, times(1)).save(document);
    }
}