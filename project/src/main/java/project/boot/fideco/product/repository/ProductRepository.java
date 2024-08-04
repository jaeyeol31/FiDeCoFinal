package project.boot.fideco.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import project.boot.fideco.product.entity.ProductEntity;

public interface ProductRepository extends JpaRepository<ProductEntity, String> {
}
