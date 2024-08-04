package project.boot.fideco.dto;

import lombok.Data;
import project.boot.fideco.entity.PaymentStatus;

import java.time.LocalDateTime;

@Data
public class PaymentDTO {
    private Long id;
    private String merchantUid;
    private Long order_id; // OrderEntity의 ID 값
    private int amount;
    private String member_id; // MemberEntity의 ID 값
    private PaymentStatus paymentStatus;
    private LocalDateTime paymentDate;
}
