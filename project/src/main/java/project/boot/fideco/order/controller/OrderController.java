//
//package project.boot.fideco.order.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//import project.boot.fideco.order.entity.OrderEntity;
//import project.boot.fideco.order.service.OrderService;
//import project.boot.fideco.cart.service.CartService;
//import project.boot.fideco.entity.Payment;
//import project.boot.fideco.provider.JwtProvider;
//import project.boot.fideco.cart.entity.CartEntity;
//
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.security.Keys;
//
//import java.nio.charset.StandardCharsets;
//import java.time.LocalDateTime;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Controller
//@RequestMapping("/orders")
//public class OrderController {
//
//    private final OrderService orderService;
//    private final CartService cartService;
//    private final JwtProvider jwtProvider;
//
//    @Value("${secret-key}")
//    private String secretKey;
//
//    @Autowired
//    public OrderController(OrderService orderService, CartService cartService, JwtProvider jwtProvider) {
//        this.orderService = orderService;
//        this.cartService = cartService;
//        this.jwtProvider = jwtProvider;
//    }
//
//    // 주문 생성
//    @PostMapping("/create")
//    @ResponseBody
//    public ResponseEntity<Map<String, Long>> createOrder(HttpServletRequest request, @RequestParam("orderAmount") int orderAmount) {
//        String token = getTokenFromCookies(request.getCookies());
//        if (token == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//
//        String memberId = jwtProvider.validate(token);
//        if (memberId == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//
//        Claims claims = Jwts.parserBuilder()
//                            .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
//                            .build()
//                            .parseClaimsJws(token)
//                            .getBody();
//        Long id = claims.get("id", Long.class);
//
//        CartEntity cart = cartService.getCartByMemberId(memberId).orElseThrow(() -> new IllegalArgumentException("Cart not found"));
//        Long cartId = cart.getCartId();
//
//        Long orderId = orderService.createOrder(cartId, id, orderAmount);
//
//        // orderId 반환
//        Map<String, Long> response = new HashMap<>();
//        response.put("orderId", orderId);
//        return ResponseEntity.ok(response);
//    }
//
//    // 특정 주문 조회
//    @GetMapping("/{orderId}")
//    public String getOrderById(@PathVariable("orderId") Long orderId, Model model) {
//        OrderEntity order = orderService.getOrderById(orderId);
//        model.addAttribute("order", order);
//        return "order/orderDetail";
//    }
//
//    // 모든 주문 조회
//    @GetMapping
//    public String getAllOrders(Model model) {
//        List<OrderEntity> orders = orderService.getAllOrders();
//        model.addAttribute("orders", orders);
//        return "order/orderList";
//    }
//
//    // 특정 회원의 주문 조회
//    @GetMapping("/member/{memberId}")
//    public String getOrdersByMemberId(@PathVariable("memberId") String memberId, Model model) {
//        List<OrderEntity> orders = orderService.getOrdersByMemberId(memberId);
//        model.addAttribute("orders", orders);
//        return "order/memberOrders";
//    }
//
//    // 주문 수정 폼
//    @GetMapping("/{orderId}/edit")
//    public String showEditOrderForm(@PathVariable Long orderId, Model model) {
//        OrderEntity order = orderService.getOrderById(orderId);
//        model.addAttribute("order", order);
//        return "order/editOrder";
//    }
//
//    // 주문 수정 처리
//    @PostMapping("/{orderId}/edit")
//    public String updateOrder(@PathVariable Long orderId, @ModelAttribute OrderEntity order) {
//        order.setOrderId(orderId);
//        orderService.saveOrder(order);
//        return "redirect:/orders";
//    }
//
//    // 주문 삭제
//    @PostMapping("/{orderId}/delete")
//    public String deleteOrder(@PathVariable Long orderId) {
//        try {
//            orderService.deleteOrder(orderId);
//            return "redirect:/orders";
//        } catch (IllegalStateException e) {
//            return "redirect:/orders?error=" + e.getMessage();
//        }
//    }
//
//    // 주문 취소 (DELETE 요청)
//    @DeleteMapping("/cancel/{orderId}")
//    @ResponseBody
//    public ResponseEntity<Void> cancelOrder(@PathVariable("orderId") Long orderId) {
//        try {
//            orderService.cancelOrder(orderId);
//            return ResponseEntity.ok().build();
//        } catch (IllegalStateException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//        }
//    }
//
//    // 결제 내역 조회
//    @GetMapping("/{orderId}/payment")
//    public String getOrderPaymentDetail(@PathVariable("orderId") Long orderId, Model model) {
//        OrderEntity order = orderService.getOrderById(orderId);
//        model.addAttribute("order", order);
//        return "order/orderPaymentDetail";
//    }
//
//    // 주문 상세 조회
//    @GetMapping("/{orderId}/detail")
//    public String getOrderDetail(@PathVariable("orderId") Long orderId, Model model) {
//        OrderEntity order = orderService.getOrderById(orderId);
//        model.addAttribute("order", order);
//        return "order/orderDetail";
//    }
//
//    // 요청에서 JWT 토큰을 추출하여 회원 ID를 반환하는 메소드
//    private String extractMemberIdFromRequest(HttpServletRequest request) {
//        String jwt = getTokenFromCookies(request.getCookies());
//        if (jwt == null) {
//            return null;
//        }
//        return jwtProvider.validate(jwt);
//    }
//
//    // 쿠키에서 JWT 토큰을 추출하는 메소드
//    private String getTokenFromCookies(Cookie[] cookies) {
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//                if ("jwtToken".equals(cookie.getName())) {
//                    return cookie.getValue();
//                }
//            }
//        }
//        return null;
//    }
//}
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

    // 주문 생성
    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<Map<String, Long>> createOrder(HttpServletRequest request, @RequestParam("orderAmount") int orderAmount) {
        String token = getTokenFromCookies(request.getCookies());
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String memberId = jwtProvider.validate(token);
        if (memberId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Claims claims = Jwts.parserBuilder()
                            .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                            .build()
                            .parseClaimsJws(token)
                            .getBody();
        Long id = claims.get("id", Long.class);

        CartEntity cart = cartService.getCartByMemberId(memberId).orElseThrow(() -> new IllegalArgumentException("Cart not found"));
        Long cartId = cart.getCartId();

        Long orderId = orderService.createOrder(cartId, id, orderAmount);

        // orderId 반환
        Map<String, Long> response = new HashMap<>();
        response.put("orderId", orderId);
        return ResponseEntity.ok(response);
    }

    // 특정 주문 조회
    @GetMapping("/{orderId}")
    public String getOrderById(@PathVariable("orderId") Long orderId, Model model) {
        OrderEntity order = orderService.getOrderById(orderId);
        model.addAttribute("order", order);
        return "order/orderDetail";
    }

    // 모든 주문 조회
    @GetMapping
    public String getAllOrders(Model model) {
        List<OrderEntity> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        return "order/orderList";
    }

    // 특정 회원의 주문 조회
    @GetMapping("/member/{memberId}")
    public String getOrdersByMemberId(@PathVariable("memberId") Long memberId, Model model) {
        List<OrderEntity> orders = orderService.getOrdersByMemberId(memberId);
        model.addAttribute("orders", orders);
        return "order/memberOrders";
    }

    // 주문 수정 폼
    @GetMapping("/{orderId}/edit")
    public String showEditOrderForm(@PathVariable Long orderId, Model model) {
        OrderEntity order = orderService.getOrderById(orderId);
        model.addAttribute("order", order);
        return "order/editOrder";
    }

    // 주문 수정 처리
    @PostMapping("/{orderId}/edit")
    public String updateOrder(@PathVariable Long orderId, @ModelAttribute OrderEntity order) {
        order.setOrderId(orderId);
        orderService.saveOrder(order);
        return "redirect:/orders";
    }

    // 주문 삭제
    @PostMapping("/{orderId}/delete")
    public String deleteOrder(@PathVariable Long orderId) {
        try {
            orderService.deleteOrder(orderId);
            return "redirect:/orders";
        } catch (IllegalStateException e) {
            return "redirect:/orders?error=" + e.getMessage();
        }
    }

    // 주문 취소 (DELETE 요청)
    @DeleteMapping("/cancel/{orderId}")
    public ResponseEntity<?> cancelOrder(@PathVariable("orderId") Long orderId) {
        orderService.cancelOrder(orderId);
        return ResponseEntity.ok().build();
    }
    
    // 결제 내역 조회
    @GetMapping("/{orderId}/payment")
    public String getOrderPaymentDetail(@PathVariable("orderId") Long orderId, Model model) {
        OrderEntity order = orderService.getOrderById(orderId);
        model.addAttribute("order", order);
        return "order/orderPaymentDetail";
    }

    // 주문 상세 조회
    @GetMapping("/{orderId}/detail")
    public String getOrderDetail(@PathVariable("orderId") Long orderId, Model model) {
        OrderEntity order = orderService.getOrderById(orderId);
        model.addAttribute("order", order);
        return "order/orderDetail";
    }

    // 요청에서 JWT 토큰을 추출하여 회원 ID를 반환하는 메소드
    private String extractMemberIdFromRequest(HttpServletRequest request) {
        String jwt = getTokenFromCookies(request.getCookies());
        if (jwt == null) {
            return null;
        }
        return jwtProvider.validate(jwt);
    }

    // 쿠키에서 JWT 토큰을 추출하는 메소드
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
