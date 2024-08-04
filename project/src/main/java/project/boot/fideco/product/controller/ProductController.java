package project.boot.fideco.product.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.boot.fideco.product.entity.ProductEntity;
import project.boot.fideco.product.service.ProductService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // 제품 생성 폼을 보여줍니다.
    @GetMapping("/save")
    public String showCreateForm(Model model) {
        model.addAttribute("productEntity", new ProductEntity());
        return "product/save";
    }

    // 새로운 제품을 생성합니다.
    @PostMapping
    public String createProduct(@ModelAttribute ProductEntity productEntity,
                                @RequestParam("imageFile") MultipartFile imageFile) {
        try {
            productService.saveProduct(productEntity, imageFile);
            return "redirect:/products/productList";
        } catch (IOException e) {
            return "error";
        }
    }

    // 관리자용 제품 상세 페이지를 보여줍니다.
    @GetMapping("/productDetail/{id}")
    public String getProductDetail(@PathVariable("id") String id, Model model) {
        Optional<ProductEntity> productEntity = productService.getProductById(id);
        if (productEntity.isPresent()) {
            ProductEntity product = productEntity.get();
            model.addAttribute("product", product);
            model.addAttribute("productThumbnail", product.getProductImagePath());
            return "product/productDetail";
        } else {
            return "error";
        }
    }

    // 회원용 제품 주문 페이지를 보여줍니다.
    @GetMapping("/productOrder/{id}")
    public String getProductOrder(@PathVariable("id") String id, Model model) {
        Optional<ProductEntity> productEntity = productService.getProductById(id);
        if (productEntity.isPresent()) {
            ProductEntity product = productEntity.get();
            model.addAttribute("product", product);
            model.addAttribute("productThumbnail", product.getProductImagePath());
            return "product/productOrder";
        } else {
            return "error";
        }
    }

    // 모든 제품 목록을 보여줍니다.
    @GetMapping("/productList")
    public String getAllProducts(Model model) {
        List<ProductEntity> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "product/productList";
    }

    // 제품 뷰 페이지를 보여줍니다.
    @GetMapping("/productView")
    public String getProductView(Model model) {
        List<ProductEntity> products = productService.getAllProducts();
        model.addAttribute("products", products);
        model.addAttribute("showFilter", true); // 필터를 표시하도록 설정
        return "product/productView";
    }

    // 제품 수정 폼을 보여줍니다.
    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") String id, Model model) {
        Optional<ProductEntity> productEntity = productService.getProductById(id);
        if (productEntity.isPresent()) {
            model.addAttribute("productEntity", productEntity.get());
            return "product/update";
        } else {
            return "error";
        }
    }

    // 제품 정보를 수정합니다.
    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable("id") String id, @ModelAttribute ProductEntity productEntity,
                                @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {
        try {
            productService.updateProduct(id, productEntity, imageFile);
            return "redirect:/products/productList";
        } catch (IOException | IllegalArgumentException e) {
            return "error";
        }
    }

    // 제품 삭제 폼을 보여줍니다.
    @GetMapping("/delete/{id}")
    public String showDeleteForm(@PathVariable("id") String id, Model model) {
        Optional<ProductEntity> productEntity = productService.getProductById(id);
        if (productEntity.isPresent()) {
            model.addAttribute("product", productEntity.get());
            return "product/delete";
        } else {
            return "error";
        }
    }

    // 제품을 삭제합니다.
    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") String id) {
        productService.deleteProduct(id);
        return "redirect:/products/productList";
    }
    
 // 제품 비교
    @GetMapping("/compareProducts")
    public String compareProducts(@RequestParam("ids") List<String> ids, Model model) {
        List<ProductEntity> products = ids.stream()
            .map(id -> productService.getProductById(id).orElse(null))
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
        model.addAttribute("products", products);
        return "product/compareProducts";
    }



}
