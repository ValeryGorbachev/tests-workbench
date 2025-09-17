package by.delaidelo.tests.testworks.dao;

import by.delaidelo.tests.testworks.domain.GoodsShipmentDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodsShipmentDocumentRepository extends JpaRepository<GoodsShipmentDocument, Long> {
    @Query("SELECT g FROM GoodsShipmentDocument g WHERE " +
            "LOWER(g.documentNumber) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(g.contractor.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(g.warehouse.title) LIKE LOWER(CONCAT('%', :query, '%')) order by g.id desc")
    Page<GoodsShipmentDocument> findByQuery(@Param("query") String query, Pageable pageable);

    Page<GoodsShipmentDocument> findAllByOrderByIdDesc(Pageable pageable);
}