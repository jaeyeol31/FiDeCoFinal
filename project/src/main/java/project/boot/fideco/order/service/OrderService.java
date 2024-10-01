//package project.boot.fideco.order.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import project.boot.fideco.order.entity.OrderEntity;
//import project.boot.fideco.order.entity.OrderItem;
//import project.boot.fideco.order.repository.OrderRepository;
//import project.boot.fideco.order.repository.OrderItemRepository;
//import project.boot.fideco.repository.PaymentRepository;
//import project.boot.fideco.cart.entity.CartEntity;
//import project.boot.fideco.cart.entity.CartItemEntity;
//import project.boot.fideco.cart.repository.CartRepository;
//import project.boot.fideco.member.repository.MemberRepository;
//import project.boot.fideco.entity.Payment;
//import project.boot.fideco.entity.PaymentStatus;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class OrderService {
//
//    private final OrderRepository orderRepository;
//    private final CartRepository cartRepository;
//    private final MemberRepository memberRepository;
//    private final PaymentRepository paymentRepository;
//    private final OrderItemRepository orderItemRepository;
//
//    @Autowired
//    public OrderService(OrderRepository orderRepository, CartRepository cartRepository, MemberRepository memberRepository, PaymentRepository paymentRepository, OrderItemRepository orderItemRepository) {
//        this.orderRepository = orderRepository;
//        this.cartRepository = cartRepository;
//        this.memberRepository = memberRepository;
//        this.paymentRepository = paymentRepository;
//        this.orderItemRepository = orderItemRepository;
//    }
//
//    @Transactional
//    public Long createOrder(Long cartId, Long memberId, int orderAmount) {
//        OrderEntity order = new OrderEntity();
//        CartEntity cart = cartRepository.findById(cartId).orElseThrow(() -> new IllegalArgumentException("Invalid cart ID"));
//        order.setCart(cart);
//        order.setMemberId(memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("Invalid member ID")));
//        order.setOrderAmount(orderAmount);
//        order.setPaymentStatus(PaymentStatus.PENDING); // Set initial payment status to PENDING
//
//        // Create OrderItems from CartItems
//        List<OrderItem> orderItems = new ArrayList<>();
//        for (CartItemEntity cartItem : cart.getCartItems()) {
//            OrderItem orderItem = new OrderItem();
//            orderItem.setOrder(order);
//            orderItem.setProduct(cartItem.getProduct());
//            orderItem.setQuantity(cartItem.getQuantity());
//            orderItems.add(orderItem);
//        }
//
//        order.setOrderItems(orderItems);
//        OrderEntity savedOrder = orderRepository.save(order);
//
//        // Save OrderItems
//        for (OrderItem orderItem : orderItems) {
//            orderItemRepository.save(orderItem);
//        }
//
//        return savedOrder.getOrderId();
//    }
//
//    @Transactional(readOnly = true)
//    public OrderEntity getOrderById(Long orderId) {
//        OrderEntity order = orderRepository.findById(orderId)
//            .orElseThrow(() -> new IllegalArgumentException("Invalid order ID"));
//        if (order.getPayment() != null) {
//            order.getPayment().getId(); // Lazy loading 초기화
//        }
//        return order;
//    }
//
//    @Transactional(readOnly = true)
//    public List<OrderEntity> getAllOrders() {
//        return orderRepository.findAll();
//    }
//
//    @Transactional(readOnly = true)
//    public List<OrderEntity> getOrdersByMemberId(String memberId) {
//        return orderRepository.findByMemberId(memberId);
//    }
//
//    @Transactional
//    public void saveOrder(OrderEntity order) {
//        orderRepository.save(order);
//    }
//
//    @Transactional
//    public void deleteOrder(Long orderId) {
//        OrderEntity order = getOrderById(orderId);
//        if (order.getPaymentStatus() == PaymentStatus.PENDING || order.getPaymentStatus() == PaymentStatus.CANCELLED) {
//            orderRepository.delete(order);
//        } else {
//            throw new IllegalStateException("Cannot delete a completed or failed order");
//        }
//    }
//
//    @Transactional
//    public void cancelOrder(Long orderId) {
//        OrderEntity order = getOrderById(orderId);
//        if (order.getPaymentStatus() == PaymentStatus.PENDING) {
//            order.setPaymentStatus(PaymentStatus.CANCELLED);
//            saveOrder(order);
//        } else {
//            throw new IllegalStateException("Cannot cancel a completed or failed order");
//        }
//    }
//
//    @Transactional
//    public Optional<CartEntity> getCartByMemberId(String memberId) {
//        return cartRepository.findByMemberId(memberId);
//    }
//    
//    @Transactional(readOnly = true)
//    public Payment getPaymentByOrderId(Long orderId) {
//        OrderEntity order = orderRepository.findById(orderId)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid order ID"));
//        return order.getPayment();
//    }
//    @Transactional(readOnly = true)
//    public LocalDateTime getPaymentDateByOrderId(Long orderId) {
//        Payment payment = paymentRepository.findByOrder_OrderId(orderId)
//                .orElseThrow(() -> new IllegalArgumentException("Payment information not found for the order"));
//        return payment.getPaymentDate();
//    }
//}
package project.boot.fideco.order.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.boot.fideco.order.entity.OrderEntity;
import project.boot.fideco.order.entity.OrderItem;
import project.boot.fideco.order.repository.OrderRepository;
import project.boot.fideco.order.repository.OrderItemRepository;
import project.boot.fideco.repository.PaymentRepository;
import project.boot.fideco.cart.entity.CartEntity;
import project.boot.fideco.cart.entity.CartItemEntity;
import project.boot.fideco.cart.repository.CartRepository;
import project.boot.fideco.member.entity.MemberEntity;
import project.boot.fideco.member.repository.MemberRepository;
import project.boot.fideco.entity.Payment;
import project.boot.fideco.entity.PaymentStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;
    private final PaymentRepository paymentRepository;
    private final OrderItemRepository orderItemRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, CartRepository cartRepository, MemberRepository memberRepository,
                        PaymentRepository paymentRepository, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.memberRepository = memberRepository;
        this.paymentRepository = paymentRepository;
        this.orderItemRepository = orderItemRepository;
    }
    
    //
    @Transactional
    public Long createOrder(Long cartId, Long memberId, int orderAmount) {
        OrderEntity order = new OrderEntity();
        CartEntity cart = cartRepository.findById(cartId).orElseThrow(() -> new IllegalArgumentException("Invalid cart ID"));
        MemberEntity member = memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));
        order.setCart(cart);
        order.setMember(member);  // member 필드를 설정합니다.
        order.setOrderAmount(orderAmount);
        order.setPaymentStatus(PaymentStatus.PENDING); // Set initial payment status to PENDING

        // Create OrderItems from CartItems
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItemEntity cartItem : cart.getCartItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItems.add(orderItem);
        }

        order.setOrderItems(orderItems);
        OrderEntity savedOrder = orderRepository.save(order);

        // Save OrderItems
        for (OrderItem orderItem : orderItems) {
            orderItemRepository.save(orderItem);
        }

        return savedOrder.getOrderId();
    }

    @Transactional(readOnly = true)
    public OrderEntity getOrderById(Long orderId) {
        OrderEntity order = orderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("잘못된 주문 ID"));
        if (order.getPayment() != null) {
            order.getPayment().getId(); // Lazy loading 초기화
        }
        return order;
    }

    @Transactional(readOnly = true)
    public List<OrderEntity> getAllOrders() {
        return orderRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<OrderEntity> getOrdersByMemberId(Long memberId) {
        return orderRepository.findByMember_Id(memberId);
    }

    @Transactional
    public void saveOrder(OrderEntity order) {
        orderRepository.save(order);
    }

    @Transactional
    public void deleteOrder(Long orderId) {
        OrderEntity order = getOrderById(orderId);
        if (order.getPaymentStatus() == PaymentStatus.PENDING || order.getPaymentStatus() == PaymentStatus.CANCELLED) {
            orderRepository.delete(order);
        } else {
            throw new IllegalStateException("완료되거나 실패한 주문은 삭제할 수 없습니다.");
        }
    }

    @Transactional
    public void cancelOrder(Long orderId) {
        OrderEntity order = orderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 주문 ID: " + orderId));
        
        // 주문 상태가 PENDING일 때만 취소 가능
        if (order.getPaymentStatus() == PaymentStatus.PENDING) {
            order.setPaymentStatus(PaymentStatus.CANCELLED);
            orderRepository.save(order);
        } else {
            throw new IllegalStateException("주문을 취소할 수 없습니다. 현재 상태: " + order.getPaymentStatus());
        }
    } 

    @Transactional
    public Optional<CartEntity> getCartByMemberId(String memberId) {
        return cartRepository.findByMemberId(memberId);
    }

    @Transactional(readOnly = true)
    public Payment getPaymentByOrderId(Long orderId) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 주문 ID"));
        return order.getPayment();
    }

    @Transactional(readOnly = true)
    public LocalDateTime getPaymentDateByOrderId(Long orderId) {
        Payment payment = paymentRepository.findByOrder_OrderId(orderId)
                .orElseThrow(() -> new IllegalArgumentException("해당 주문의 결제 정보를 찾을 수 없습니다."));
        return payment.getPaymentDate();
    }
    
}
