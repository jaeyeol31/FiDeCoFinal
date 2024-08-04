//
//
//package project.boot.fideco.service;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import project.boot.fideco.dto.PaymentDTO;
//import project.boot.fideco.entity.Payment;
//import project.boot.fideco.member.entity.MemberEntity;
//import project.boot.fideco.member.repository.MemberRepository;
//import project.boot.fideco.order.repository.OrderRepository;
//import project.boot.fideco.repository.PaymentRepository;
//
//@Service
//public class PaymentService {
//
//    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);
//
//    @Autowired
//    private PaymentRepository paymentRepository;
//
//    @Autowired
//    private OrderRepository orderRepository;
//
//    @Autowired
//    private MemberRepository memberRepository;
//
//    public PaymentDTO getPaymentDTOById(Long paymentId) {
//        Payment payment = paymentRepository.findById(paymentId)
//                .orElseThrow(() -> new IllegalArgumentException("Payment not found for id: " + paymentId));
//
//        PaymentDTO paymentDTO = new PaymentDTO();
//        paymentDTO.setMerchantUid(payment.getMerchantUid());
//        paymentDTO.setOrder_id(payment.getOrder().getOrderId());
//        paymentDTO.setAmount(payment.getOrderAmount());
//        paymentDTO.setPaymentDate(payment.getPaymentDate());
//        paymentDTO.setMember_id(payment.getMember().getMemberId());
//
//        return paymentDTO;
//    }
//
//    @Transactional
//    public void updatePayment(Long id, PaymentDTO paymentDTO) {
//        Payment payment = paymentRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Payment not found for id: " + id));
//        // payment.setPaymentStatus(paymentDTO.getPaymentStatus());
//        payment.setPaymentDate(paymentDTO.getPaymentDate());
//
//        logger.info("Updating payment with id {}: status = {}, date = {}", id, paymentDTO.getPaymentStatus(), paymentDTO.getPaymentDate());
//
//        paymentRepository.save(payment);
//    }
//
//    @Transactional
//    public void deletePayment(Long id) {
//        Payment payment = paymentRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Payment not found for id: " + id));
//
//        logger.info("Deleting payment with id {}", id);
//
//        paymentRepository.delete(payment);
//    }
//
//    @Transactional
//    public void savePayment(PaymentDTO paymentDTO, String memberId) {
//        Payment payment = new Payment();
//        payment.setMerchantUid(paymentDTO.getMerchantUid());
//        payment.setOrder(orderRepository.findById(paymentDTO.getOrder_id()).orElse(null));
//        payment.setOrderAmount(paymentDTO.getAmount());
//
//        logger.info("Fetching member with id: {}", memberId); // 로그 추가
//        MemberEntity member = memberRepository.findByMemberId(memberId);
//        if (member == null) {
//            throw new IllegalArgumentException("Member not found for id: " + memberId);
//        }
//        payment.setMember(member);
//        payment.setPaymentDate(paymentDTO.getPaymentDate());
//
//        logger.info("Saving payment: merchantUid = {}, status = {}, date = {}, memberId = {}", paymentDTO.getMerchantUid(), paymentDTO.getPaymentStatus(), paymentDTO.getPaymentDate(), member.getId());
//
//        paymentRepository.save(payment);
//    }
//}
package project.boot.fideco.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.boot.fideco.dto.PaymentDTO;
import project.boot.fideco.entity.Payment;
import project.boot.fideco.member.entity.MemberEntity;
import project.boot.fideco.member.repository.MemberRepository;
import project.boot.fideco.order.repository.OrderRepository;
import project.boot.fideco.repository.PaymentRepository;

@Service
public class PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MemberRepository memberRepository;

    public PaymentDTO getPaymentDTOById(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found for id: " + paymentId));

        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setMerchantUid(payment.getMerchantUid());
        paymentDTO.setOrder_id(payment.getOrder().getOrderId());
        paymentDTO.setAmount(payment.getOrderAmount());
        paymentDTO.setPaymentDate(payment.getPaymentDate());
        paymentDTO.setMember_id(payment.getMember().getMemberId());

        return paymentDTO;
    }

    @Transactional
    public void updatePayment(Long id, PaymentDTO paymentDTO) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found for id: " + id));
        // payment.setPaymentStatus(paymentDTO.getPaymentStatus());
        payment.setPaymentDate(paymentDTO.getPaymentDate());

        logger.info("Updating payment with id {}: status = {}, date = {}", id, paymentDTO.getPaymentStatus(), paymentDTO.getPaymentDate());

        paymentRepository.save(payment);
    }

    @Transactional
    public void deletePayment(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found for id: " + id));

        logger.info("Deleting payment with id {}", id);

        paymentRepository.delete(payment);
    }

    @Transactional
    public void savePayment(PaymentDTO paymentDTO, String memberId) {
        Payment payment = new Payment();
        payment.setMerchantUid(paymentDTO.getMerchantUid());
        payment.setOrder(orderRepository.findById(paymentDTO.getOrder_id()).orElse(null));
        payment.setOrderAmount(paymentDTO.getAmount());

        logger.info("Fetching member with id: {}", memberId); // 로그 추가
        MemberEntity member = memberRepository.findByMemberId(memberId);
        if (member == null) {
            throw new IllegalArgumentException("Member not found for id: " + memberId);
        }
        payment.setMember(member);
        payment.setPaymentDate(paymentDTO.getPaymentDate());

        logger.info("Saving payment: merchantUid = {}, status = {}, date = {}, memberId = {}", paymentDTO.getMerchantUid(), paymentDTO.getPaymentStatus(), paymentDTO.getPaymentDate(), member.getId());

        paymentRepository.save(payment);
    }
}
