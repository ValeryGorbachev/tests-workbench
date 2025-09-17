package by.delaidelo.tests.testworks.mappers;

import by.delaidelo.tests.testworks.domain.GoodsReceiptDocument;
import by.delaidelo.tests.testworks.dto.GoodsReceiptDto;
import org.mapstruct.Mapper;

@Mapper(uses = {GoodsReceiptItemMapper.class, ContractorEntityMapper.class, ContractEntityMapper.class, WarehouseEntityMapper.class})
public interface GoodsReceiptDocumentMapper extends MappableEntity<GoodsReceiptDocument, GoodsReceiptDto> {

    @Override
    GoodsReceiptDocument fromDto(GoodsReceiptDto dto);

    @Override
    GoodsReceiptDto toDto(GoodsReceiptDocument entity);
}


