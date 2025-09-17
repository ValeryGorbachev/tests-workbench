package by.delaidelo.tests.testworks.mappers;

import by.delaidelo.tests.testworks.domain.GoodsShipmentItem;
import by.delaidelo.tests.testworks.dto.ProductDto;
import org.mapstruct.Mapper;

@Mapper(uses = {WarehouseItemTypeEntityMapper.class})
public interface GoodsShipmentItemMapper extends MappableEntity<GoodsShipmentItem, ProductDto> {
    @Override
    GoodsShipmentItem fromDto(ProductDto dto);

    @Override
    ProductDto toDto(GoodsShipmentItem entity);
}