package project.boot.fideco.cart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import project.boot.fideco.cart.entity.CartEntity;
import project.boot.fideco.cart.entity.CartItemEntity;
import project.boot.fideco.cart.service.CartService;
import project.boot.fideco.dto.request.cart.AddToCartRequestDTO;
import project.boot.fideco.provider.JwtProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/cart")
public class CartController {

    private static final Logger logger = LoggerFactory.getLogger(CartController.class);

    private final CartService cartService;
    private final JwtProvider jwtProvider;

    @Autowired
    public CartController(CartService cartService, JwtProvider jwtProvider) {
        this.cartService = cartService;
        this.jwtProvider = jwtProvider;
    }

    // 사용자 장바구니
    @GetMapping("/{memberId}")
    public String getCartItemsByMemberId(@PathVariable("memberId") String memberId, Model model) {
        Optional<CartEntity> cartEntity = cartService.getCartByMemberId(memberId);
        if (cartEntity.isPresent()) {
            List<CartItemEntity> cartItems = cartEntity.get().getCartItems();
            model.addAttribute("cartItems", cartItems);
        } else {
            model.addAttribute("cartItems", Collections.emptyList());
        }
        return "cart/cart";
    }

    // 사용자 장바구니에 추가
    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addToCart(@RequestBody AddToCartRequestDTO addToCartRequest,
                                                         HttpServletRequest request) {
        String memberId = extractMemberIdFromRequest(request);
        logger.info("Add to cart request by memberId: {}", memberId);
        if (memberId == null) {
            logger.warn("Unauthorized access attempt to add to cart");
            return ResponseEntity.status(401).body(Collections.singletonMap("success", false));
        }

        cartService.addToCart(memberId, addToCartRequest.getProductId(), addToCartRequest.getQuantity());
        return ResponseEntity.ok(Collections.singletonMap("success", true));
    }

    // 장바구니 수정화면
    @PostMapping("/update")
    public ResponseEntity<?> updateCartItemQuantity(@RequestBody Map<String, Object> request) {
        Long cartItemId = Long.valueOf(request.get("cartItemId").toString());
        Integer quantity = Integer.valueOf(request.get("quantity").toString());
        cartService.updateCartItemQuantity(cartItemId, quantity);
        return ResponseEntity.ok().build();
    }

    // 장바구니 아이템 삭제
    @DeleteMapping("/delete/{cartItemId}")
    public ResponseEntity<?> deleteCartItem(@PathVariable("cartItemId") Long cartItemId) {
        cartService.deleteCartItem(cartItemId);
        return ResponseEntity.ok().build();
    }

    // 장바구니 전체 조회
    @GetMapping("/list")
    public String getAllCarts(Model model) {
        List<CartEntity> carts = cartService.getAllCarts();
        model.addAttribute("carts", carts);
        return "cart/cartList";
    }

    private String extractMemberIdFromRequest(HttpServletRequest request) {
        String jwt = getTokenFromCookies(request.getCookies());
        if (jwt == null) {
            logger.warn("JWT token missing in cookies");
            return null;
        }

        String memberId = jwtProvider.validate(jwt);
        logger.info("Extracted memberId from JWT: {}", memberId);
        return memberId;
    }

    private String getTokenFromCookies(Cookie[] cookies) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwtToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
