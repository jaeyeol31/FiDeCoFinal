//
//package project.boot.fideco.entity;
//import java.time.LocalDateTime;
//import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.Setter;
//import project.boot.fideco.member.entity.MemberEntity;
//import project.boot.fideco.order.entity.OrderEntity;
//
//@Getter
//@Setter
//@Entity
//@Table(name = "payment")
//public class Payment {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payment_seq_generator")
//    @SequenceGenerator(name = "payment_seq_generator", sequenceName = "payment_seq", allocationSize = 1)
//    @Column(name = "id")
//    private Long id;
//
//    @Column(name = "merchant_uid", nullable = false)
//    private String merchantUid;
//
////    @ManyToOne(fetch = FetchType.LAZY)
////    @JoinColumn(name = "order_id")
////    private OrderEntity order;
//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "order_id", referencedColumnName = "order_id", nullable = false)
//    private OrderEntity order;
//    
//    @Column(name = "order_amount")
//    private int orderAmount;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "member_entity_id")  // memberEntity테이블의 id 구분
//    private MemberEntity member;
//
//    @Column(name = "payment_date")
//    private LocalDateTime paymentDate;
//}
package project.boot.fideco.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import project.boot.fideco.member.entity.MemberEntity;
import project.boot.fideco.order.entity.OrderEntity;

@Getter
@Setter
@Entity
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payment_seq_generator")
    @SequenceGenerator(name = "payment_seq_generator", sequenceName = "payment_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "merchant_uid", nullable = false)
    private String merchantUid;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", referencedColumnName = "order_id", nullable = false)
    private OrderEntity order;

    @Column(name = "order_amount")
    private int orderAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_entity_id")
    private MemberEntity member;

    @Column(name = "payment_date")
    private LocalDateTime paymentDate;
}
