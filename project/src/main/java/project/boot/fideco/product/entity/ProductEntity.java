package project.boot.fideco.product.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@ToString
@Entity
@Table(name = "product")
public class ProductEntity {

    @Id
    @Column(name = "product_id")
    private String productId; // UUID 타입의 식별자

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_price")
    private int productPrice;

    @Column(name = "product_regisdate")
    private String productRegisdate;

    @Column(name = "product_intro")
    private String productIntro;

    @Column(name = "product_image_path")
    private String productImagePath; // 이미지 경로

    @Column(name = "product_category")
    private String productCategory;

    @PrePersist
    public void generateProductId() {
        this.productId = UUID.randomUUID().toString(); // UUID 생성 후 String으로 변환하여 할당
    }
}
