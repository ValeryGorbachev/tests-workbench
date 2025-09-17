package by.delaidelo.tests.testworks.mappers;

import by.delaidelo.tests.testworks.dao.WarehouseItemTypeRepository;
import by.delaidelo.tests.testworks.domain.WarehouseItemType;
import by.delaidelo.tests.testworks.dto.SelectListItemDto;
import org.springframework.stereotype.Component;

@Component
public class WarehouseItemTypeEntityMapper {
    private final WarehouseItemTypeRepository repository;

    public WarehouseItemTypeEntityMapper(WarehouseItemTypeRepository repository) {
        this.repository = repository;
    }

    public WarehouseItemType toEntity(SelectListItemDto dto) {
        if (dto == null) return null;
        return repository.findById(dto.id()).orElse(null);
    }

    public SelectListItemDto fromEntity(WarehouseItemType entity) {
        if (entity == null) return null;
        return new SelectListItemDto(entity.getId(), entity.getTitle());
    }
}
