//
//package project.boot.fideco.order.entity;
//
//import java.util.List;
//import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.Setter;
//import lombok.ToString;
//import project.boot.fideco.cart.entity.CartEntity;
//import project.boot.fideco.entity.Payment;
//import project.boot.fideco.entity.PaymentStatus;
//import project.boot.fideco.member.entity.MemberEntity;
//
//@Getter
//@Setter
//@ToString
//@Entity
//@Table(name = "orders")
//public class OrderEntity {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    @Column(name = "order_id")
//    private Long orderId;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "cart_id")
//    private CartEntity cart;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "id", referencedColumnName = "id")
//    private MemberEntity member;
//
//
//    @Column(name = "order_amount")
//    private int orderAmount;
//    
//    @Enumerated(EnumType.STRING)
//    @Column(name = "paymentstatus")
//    private PaymentStatus paymentStatus;
//    
//    @OneToOne(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    private Payment payment;
//    
//    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<OrderItem> orderItems;
//}
package project.boot.fideco.order.entity;

import java.util.List;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import project.boot.fideco.cart.entity.CartEntity;
import project.boot.fideco.entity.Payment;
import project.boot.fideco.entity.PaymentStatus;
import project.boot.fideco.member.entity.MemberEntity;

@Getter
@Setter
@ToString
@Entity
@Table(name = "orders")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_id")
    private Long orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private CartEntity cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", referencedColumnName = "id")
    private MemberEntity member;

    @Column(name = "order_amount")
    private int orderAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "paymentstatus")
    private PaymentStatus paymentStatus;

    @OneToOne(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Payment payment;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems;
}
