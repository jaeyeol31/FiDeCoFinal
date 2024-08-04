package project.boot.fideco.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.boot.fideco.order.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
