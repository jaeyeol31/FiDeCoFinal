package project.boot.fideco.home.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import project.boot.fideco.product.entity.ProductEntity;
import project.boot.fideco.product.service.ProductService;

@Controller
public class HomeController {
	
	 private final ProductService productService;
	 
	 @Autowired
	    public HomeController(ProductService productService) {
	        this.productService = productService;
	    }

	@GetMapping("/")
    public String index(Model model) {
        List<ProductEntity> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "index";
    }
	
	@GetMapping("/admin")
    public String adminPage() {
        return "admin";
    }
	 @GetMapping("/access-denied")
	    public String accessDenied() {
	        return "access-denied";
	    }
}
