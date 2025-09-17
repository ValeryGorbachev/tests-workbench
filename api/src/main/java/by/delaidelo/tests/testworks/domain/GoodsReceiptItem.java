package by.delaidelo.tests.testworks.domain;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "goods_receipt_items")
public class GoodsReceiptItem extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "document_id", nullable = false)
    private GoodsReceiptDocument document;

    @ManyToOne
    @JoinColumn(name = "item_type_id", nullable = false)
    private WarehouseItemType itemType;

    @Column(name = "quantity", nullable = false)
    private BigDecimal quantity;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "total_sum", nullable = false)
    private BigDecimal totalSum;
}


