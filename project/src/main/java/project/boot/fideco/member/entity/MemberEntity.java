package project.boot.fideco.member.entity;

import jakarta.persistence.*;
import lombok.*;
import project.boot.fideco.cart.entity.CartEntity;
import project.boot.fideco.order.entity.OrderEntity;
import project.boot.fideco.entity.Payment;
import project.boot.fideco.dto.request.auth.SignUpRequestDTO;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "member")
@Table(name = "member")
public class MemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id; // 자동 생성되는 회원 ID

    @Column(name = "member_id", unique = true, nullable = false)
    private String memberId; // 회원 ID (고유값, 필수 입력)

    @Column(name = "member_name")
    private String memberName; // 회원 이름

    @Column(name = "member_pw")
    private String memberPw; // 회원 비밀번호

    @Column(name = "member_address")
    private String memberAddress; // 회원 주소

    @Column(name = "member_phone")
    private String memberPhone; // 회원 전화번호

    @Column(name = "member_email")
    private String memberEmail; // 회원 이메일

    @Column(name = "member_auth")
    private String memberAuth; // 권한 (기본값: ROLE_USER)

    @Column(name = "member_type")
    private String memberType; // 로그인 방식 -> 아이디(app), 카카오, 네이버

    // CartEntity와의 1:N 관계 설정
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartEntity> carts = new ArrayList<>(); // 회원과 연관된 장바구니 리스트

    // OrderEntity와의 1:N 관계 설정
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderEntity> orders = new ArrayList<>(); // 회원과 연관된 주문 리스트

    // Payment와의 1:N 관계 설정
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payment> payments = new ArrayList<>(); // 회원과 연관된 결제 리스트

    // 회원가입 요청(SignUpRequestDTO)으로부터 MemberEntity를 생성하는 생성자
    public MemberEntity(SignUpRequestDTO dto) {
        this.memberId = dto.getMemberId();
        this.memberName = dto.getMemberName();
        this.memberPw = dto.getMemberPw();
        this.memberAddress = dto.getMemberAddress();
        this.memberPhone = dto.getMemberPhone();
        this.memberEmail = dto.getMemberEmail();
        this.memberAuth = "ROLE_USER"; // 기본 권한은 USER
        this.memberType = "app"; // 기본 로그인 방식은 app
    }
}
