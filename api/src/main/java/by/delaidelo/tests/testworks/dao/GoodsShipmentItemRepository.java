package by.delaidelo.tests.testworks.dao;

import by.delaidelo.tests.testworks.domain.GoodsShipmentItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodsShipmentItemRepository extends JpaRepository<GoodsShipmentItem, Long> {
}