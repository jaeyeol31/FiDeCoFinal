package project.boot.fideco.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


import project.boot.fideco.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long>{

    Optional<Payment> findByOrder_OrderId(Long orderId);
}

