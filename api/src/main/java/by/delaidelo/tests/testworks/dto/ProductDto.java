package by.delaidelo.tests.testworks.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDto {
    private Long id;
    @NotNull
    @Valid
    private SelectListItemDto itemType;
    @NotNull
    private BigDecimal quantity;
    @NotNull
    private BigDecimal price;
    @NotNull
    private BigDecimal totalSum;
}