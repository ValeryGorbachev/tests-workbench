package by.delaidelo.tests.testworks.mappers;

import by.delaidelo.tests.testworks.domain.GoodsShipmentDocument;
import by.delaidelo.tests.testworks.dto.GoodsShipmentDocumentDto;
import org.mapstruct.Mapper;

@Mapper(uses = {GoodsShipmentItemMapper.class, ContractorEntityMapper.class, ContractEntityMapper.class, WarehouseEntityMapper.class})
public interface GoodsShipmentDocumentMapper extends MappableEntity<GoodsShipmentDocument, GoodsShipmentDocumentDto> {
    @Override
    GoodsShipmentDocument fromDto(GoodsShipmentDocumentDto dto);

    @Override
    GoodsShipmentDocumentDto toDto(GoodsShipmentDocument entity);
}