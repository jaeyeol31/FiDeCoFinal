package project.boot.fideco.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import project.boot.fideco.cart.entity.CartItemEntity;

public interface CartItemRepository extends JpaRepository<CartItemEntity, Long> {
    
    @Transactional
    void deleteByCartMemberId(String memberId);
}
