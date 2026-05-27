package project.boot.fideco.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.boot.fideco.cart.entity.CartEntity;

import java.util.Optional;

public interface CartRepository extends JpaRepository<CartEntity, Long> {
    Optional<CartEntity> findByMemberId(String memberId);
}
