package by.delaidelo.tests.testworks.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import by.delaidelo.tests.testworks.domain.GoodsReceiptDocument;

public interface GoodsReceiptDocumentRepository extends JpaRepository<GoodsReceiptDocument, Long> {
    @Query("SELECT d FROM GoodsReceiptDocument d WHERE " +
           "LOWER(d.documentNumber) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(d.contractor.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(d.warehouse.title) LIKE LOWER(CONCAT('%', :query, '%')) order by d.id desc")
    Page<GoodsReceiptDocument> findByQuery(@Param("query") String query, Pageable pageable);

    Page<GoodsReceiptDocument> findAllByOrderByIdDesc(Pageable pageable);
}


