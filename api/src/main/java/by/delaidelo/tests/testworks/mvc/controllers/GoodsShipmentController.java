package by.delaidelo.tests.testworks.mvc.controllers;

import by.delaidelo.tests.testworks.dto.GoodsShipmentDocumentDto;
import by.delaidelo.tests.testworks.services.GoodsShipmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/goods-shipment")
@CrossOrigin("*")
@RequiredArgsConstructor
public class GoodsShipmentController {

    private final GoodsShipmentService goodsShipmentService;

    @GetMapping
    public Page<GoodsShipmentDocumentDto> find(@RequestParam(defaultValue = "") String query, Pageable pageable) {
        return goodsShipmentService.find(query, pageable);
    }

    @GetMapping("/{id:\\d+}")
    public GoodsShipmentDocumentDto findById(@PathVariable Long id) {
        return goodsShipmentService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Long> create(@Valid @RequestBody GoodsShipmentDocumentDto dto) {
        return ResponseEntity.ok(goodsShipmentService.create(dto));
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @Valid @RequestBody GoodsShipmentDocumentDto dto) {
        goodsShipmentService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        goodsShipmentService.delete(id);
    }

    @PostMapping("/{id:\\d+}/conduct")
    public void conduct(@PathVariable Long id) {
        goodsShipmentService.conduct(id);
    }

    @PostMapping("/{id:\\d+}/unconduct")
    public void unConduct(@PathVariable Long id) {
        goodsShipmentService.unConduct(id);
    }
}