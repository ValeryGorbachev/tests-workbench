package by.delaidelo.tests.testworks.mvc.controllers;

import by.delaidelo.tests.testworks.dto.GoodsReceiptDto;
import by.delaidelo.tests.testworks.services.GoodsReceiptService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/goods-receipts")
@CrossOrigin("*")
public class GoodsReceiptController {
    private final GoodsReceiptService service;

    public GoodsReceiptController(GoodsReceiptService service) {
        this.service = service;
    }

    @GetMapping
    public Page<GoodsReceiptDto> find(@RequestParam(defaultValue = "") String query, Pageable pageable) {
        return service.find(query, pageable);
    }

    @GetMapping("/{id:\\d+}")
    public GoodsReceiptDto findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Long> create(@RequestBody @Valid GoodsReceiptDto dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @RequestBody @Valid GoodsReceiptDto dto) {
        service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @PostMapping("/{id:\\d+}/conduct")
    public void conduct(@PathVariable Long id) {
        service.conduct(id);
    }

    @PostMapping("/{id:\\d+}/unconduct")
    public void unConduct(@PathVariable Long id) {
        service.unConduct(id);
    }

}


