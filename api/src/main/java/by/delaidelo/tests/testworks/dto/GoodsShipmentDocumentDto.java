package by.delaidelo.tests.testworks.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class GoodsShipmentDocumentDto {
    private Long id;
    @NotNull
    private LocalDate documentDate;
    @NotNull
    @NotBlank
    private String documentNumber;
    @NotNull
    @Valid
    private SelectListItemDto contractor;
    @Valid
    private SelectListItemDto contract;
    @NotNull
    @Valid
    private SelectListItemDto warehouse;
    @NotNull
    @Valid
    private List<ProductDto> items;
    private boolean conducted;
}