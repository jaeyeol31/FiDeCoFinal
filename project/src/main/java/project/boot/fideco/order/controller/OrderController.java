package project.boot.fideco.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import project.boot.fideco.order.entity.OrderEntity;
import project.boot.fideco.order.service.OrderService;
import project.boot.fideco.cart.service.CartService;
import project.boot.fideco.provider.JwtProvider;
import project.boot.fideco.cart.entity.CartEntity;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final CartService cartService;
    private final JwtProvider jwtProvider;

    @Value("${secret-key}")
    private String secretKey;

    @Autowired
    public OrderController(OrderService orderService, CartService cartService, JwtProvider jwtProvider) {
        this.orderService = orderService;
        this.cartService = cartService;
        this.jwtProvider = jwtProvider;
    }

    // 주문 생성 메서드
    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<Map<String, Long>> createOrder(HttpServletRequest request, @RequestParam("orderAmount") int orderAmount) {
        // 쿠키에서 JWT 토큰을 추출
        String token = getTokenFromCookies(request.getCookies());
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 토큰이 없으면 인증 실패 응답
        }

        // 토큰에서 회원 ID 추출
        String memberId = jwtProvider.validate(token);
        if (memberId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 토큰이 유효하지 않으면 인증 실패 응답
        }

        // JWT에서 사용자 정보 추출
        Claims claims = Jwts.parserBuilder()
                            .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                            .build()
                            .parseClaimsJws(token)
                            .getBody();
        Long id = claims.get("id", Long.class);

        // 회원의 장바구니 정보 가져오기
        CartEntity cart = cartService.getCartByMemberId(memberId).orElseThrow(() -> new IllegalArgumentException("Cart not found"));
        Long cartId = cart.getCartId();

        // 주문 생성
        Long orderId = orderService.createOrder(cartId, id, orderAmount);

        // 생성된 주문 ID 반환
        Map<String, Long> response = new HashMap<>();
        response.put("orderId", orderId);
        return ResponseEntity.ok(response);
    }

    // 특정 주문 조회 메서드
    @GetMapping("/{orderId}")
    public String getOrderById(@PathVariable("orderId") Long orderId, Model model) {
        OrderEntity order = orderService.getOrderById(orderId);
        model.addAttribute("order", order); // 주문 정보를 모델에 추가
        return "order/orderDetail"; // 주문 상세 페이지 반환
    }

    // 모든 주문 조회 메서드
    @GetMapping
    public String getAllOrders(Model model) {
        List<OrderEntity> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders); // 모든 주문 정보를 모델에 추가
        return "order/orderList"; // 주문 목록 페이지 반환
    }

    // 특정 회원의 주문 조회 메서드
    @GetMapping("/member/{memberId}")
    public String getOrdersByMemberId(@PathVariable("memberId") Long memberId, Model model) {
        List<OrderEntity> orders = orderService.getOrdersByMemberId(memberId);
        model.addAttribute("orders", orders); // 특정 회원의 주문 정보를 모델에 추가
        return "order/memberOrders"; // 회원별 주문 목록 페이지 반환
    }

    // 주문 수정 폼 메서드
    @GetMapping("/{orderId}/edit")
    public String showEditOrderForm(@PathVariable Long orderId, Model model) {
        OrderEntity order = orderService.getOrderById(orderId);
        model.addAttribute("order", order); // 수정할 주문 정보를 모델에 추가
        return "order/editOrder"; // 주문 수정 페이지 반환
    }

    // 주문 수정 처리 메서드
    @PostMapping("/{orderId}/edit")
    public String updateOrder(@PathVariable Long orderId, @ModelAttribute OrderEntity order) {
        order.setOrderId(orderId);
        orderService.saveOrder(order); // 수정된 주문 정보를 저장
        return "redirect:/orders"; // 모든 주문 목록 페이지로 리다이렉트
    }

    // 주문 삭제 메서드
    @PostMapping("/{orderId}/delete")
    public String deleteOrder(@PathVariable Long orderId) {
        try {
            orderService.deleteOrder(orderId); // 주문 삭제 요청
            return "redirect:/orders"; // 모든 주문 목록 페이지로 리다이렉트
        } catch (IllegalStateException e) {
            return "redirect:/orders?error=" + e.getMessage(); // 오류 발생 시 오류 메시지를 포함하여 리다이렉트
        }
    }

    // 주문 취소 메서드 (DELETE 요청)
    @DeleteMapping("/cancel/{orderId}")
    public ResponseEntity<?> cancelOrder(@PathVariable("orderId") Long orderId) {
        orderService.cancelOrder(orderId); // 주문 취소 요청
        return ResponseEntity.ok().build(); // 취소 성공 응답 반환
    }

    // 결제 내역 조회 메서드
    @GetMapping("/{orderId}/payment")
    public String getOrderPaymentDetail(@PathVariable("orderId") Long orderId, Model model) {
        OrderEntity order = orderService.getOrderById(orderId);
        model.addAttribute("order", order); // 주문 결제 정보를 모델에 추가
        return "order/orderPaymentDetail"; // 결제 내역 페이지 반환
    }

    // 주문 상세 조회 메서드
    @GetMapping("/{orderId}/detail")
    public String getOrderDetail(@PathVariable("orderId") Long orderId, Model model) {
        OrderEntity order = orderService.getOrderById(orderId);
        model.addAttribute("order", order); // 주문 정보를 모델에 추가
        return "order/orderDetail"; // 주문 상세 페이지 반환
    }

    // 요청에서 JWT 토큰을 추출하여 회원 ID를 반환하는 메서드
    private String extractMemberIdFromRequest(HttpServletRequest request) {
        String jwt = getTokenFromCookies(request.getCookies());
        if (jwt == null) {
            return null; // 토큰이 없으면 null 반환
        }
        return jwtProvider.validate(jwt); // 토큰 유효성 검증 후 회원 ID 반환
    }

    // 쿠키에서 JWT 토큰을 추출하는 메서드
    private String getTokenFromCookies(Cookie[] cookies) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwtToken".equals(cookie.getName())) {
                    return cookie.getValue(); // 쿠키에서 JWT 토큰 반환
                }
            }
        }
        return null; // 쿠키에 JWT가 없으면 null 반환
    }
}
