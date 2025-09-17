package by.delaidelo.tests.testworks.mappers;

import by.delaidelo.tests.testworks.domain.GoodsReceiptItem;
import by.delaidelo.tests.testworks.dto.ProductDto;
import org.mapstruct.Mapper;

@Mapper(uses = {WarehouseItemTypeEntityMapper.class})
public interface GoodsReceiptItemMapper extends MappableEntity<GoodsReceiptItem, ProductDto> {

    @Override
    GoodsReceiptItem fromDto(ProductDto dto);

    @Override
    ProductDto toDto(GoodsReceiptItem entity);
}



