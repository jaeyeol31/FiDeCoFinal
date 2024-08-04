package project.boot.fideco.product.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import project.boot.fideco.product.entity.ProductEntity;
import project.boot.fideco.product.repository.ProductRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    // 업로드 디렉토리 경로
    private static final String UPLOAD_DIR = "src/main/resources/static/img/product";

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // 제품을 저장합니다.
    @Transactional(rollbackFor = Exception.class)
    public ProductEntity saveProduct(ProductEntity productEntity, MultipartFile imageFile) throws IOException {
        productEntity.generateProductId(); // UUID 생성 및 할당

        if (imageFile != null && !imageFile.isEmpty()) {
            String fileName = generateUniqueFileName("image", imageFile);
            Path filePath = Paths.get(UPLOAD_DIR, fileName);
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, imageFile.getBytes());

            productEntity.setProductImagePath(fileName); // 상대 경로 저장
        }

        return productRepository.save(productEntity);
    }

    // 제품 ID로 제품을 조회합니다.
    @Transactional(readOnly = true)
    public Optional<ProductEntity> getProductById(String id) {
        return productRepository.findById(id);
    }

    // 모든 제품을 조회합니다.
    @Transactional(readOnly = true)
    public List<ProductEntity> getAllProducts() {
        return productRepository.findAll();
    }

    // 제품 정보를 업데이트합니다.
    @Transactional(rollbackFor = { IOException.class, IllegalArgumentException.class })
    public ProductEntity updateProduct(String id, ProductEntity productEntity, MultipartFile imageFile) throws IOException {
        Optional<ProductEntity> optionalProduct = productRepository.findById(id);

        if (optionalProduct.isPresent()) {
            ProductEntity product = optionalProduct.get();
            product.setProductName(productEntity.getProductName());
            product.setProductPrice(productEntity.getProductPrice());
            product.setProductRegisdate(productEntity.getProductRegisdate());
            product.setProductIntro(productEntity.getProductIntro());

            if (imageFile != null && !imageFile.isEmpty()) {
                String fileName = generateUniqueFileName("image", imageFile);
                Path filePath = Paths.get(UPLOAD_DIR, fileName);
                Files.createDirectories(filePath.getParent());
                Files.write(filePath, imageFile.getBytes());

                product.setProductImagePath(fileName); // 상대 경로 저장
            }

            product.setProductCategory(productEntity.getProductCategory());

            return productRepository.save(product);
        } else {
            throw new IllegalArgumentException("Product not found");
        }
    }

    // 제품을 삭제합니다.
    @Transactional(rollbackFor = Exception.class)
    public void deleteProduct(String id) {
        productRepository.deleteById(id);
    }

    // 파일 이름을 생성합니다.
    private String generateUniqueFileName(String baseName, MultipartFile file) throws IOException {
        String originalFileName = file.getOriginalFilename();
        String fileName = baseName;

        // 중복 파일명 방지를 위해 숫자를 증가시켜 파일명 생성
        Path filePath = Paths.get(UPLOAD_DIR, fileName);
        int count = 1;
        while (Files.exists(filePath)) {
            fileName = baseName + "_" + count;
            filePath = Paths.get(UPLOAD_DIR, fileName);
            count++;
        }

        // 원본 파일의 확장자를 포함한 이름으로 저장
        fileName += "_" + UUID.randomUUID().toString() + getFileExtension(originalFileName);
        return fileName;
    }

    // 파일 확장자를 가져옵니다.
    private String getFileExtension(String fileName) {
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
            return fileName.substring(fileName.lastIndexOf("."));
        } else {
            return "";
        }
    }
}
