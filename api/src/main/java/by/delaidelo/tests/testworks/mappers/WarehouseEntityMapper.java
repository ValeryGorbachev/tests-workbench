package by.delaidelo.tests.testworks.mappers;

import by.delaidelo.tests.testworks.dao.WarehouseRepositry;
import by.delaidelo.tests.testworks.domain.Warehouse;
import by.delaidelo.tests.testworks.dto.SelectListItemDto;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class WarehouseEntityMapper {

    private final WarehouseRepositry warehouseRepository;

    public WarehouseEntityMapper(WarehouseRepositry warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    public Warehouse toEntity(SelectListItemDto dto) {
        if (dto == null) return null;
        return Optional.ofNullable(dto.id())
                .flatMap(warehouseRepository::findById)
                .orElse(null);
    }

    public SelectListItemDto fromEntity(Warehouse entity) {
        if (entity == null) return null;
        return new SelectListItemDto(entity.getId(), entity.getTitle());
    }
}
