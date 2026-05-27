package project.boot.fideco.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.boot.fideco.member.entity.MemberEntity;
import project.boot.fideco.member.service.MemberService;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private MemberService memberService; // MemberService 의존성 주입

    // 회원가입 폼 페이지 반환
    @GetMapping("/signup")
    public String signupForm() {
        return "member/signup"; // 회원가입 페이지
    }

    // 마이페이지 (회원 정보 조회)
    @GetMapping("/myPage/{id}")
    public String memberMypage(@PathVariable("id") Long id, Model model) {
        Optional<MemberEntity> member = memberService.getMemberById(id); // ID로 회원 정보 조회
        member.ifPresent(value -> model.addAttribute("member", value)); // 회원 정보가 있으면 모델에 데이터를 담아서 뷰로전달
        return "member/myPage"; // 마이페이지로 이동
    }

    // 회원 수정 폼
    @GetMapping("/update/{id}")
    public String editForm(@PathVariable("id") Long id, Model model) {
        Optional<MemberEntity> member = memberService.getMemberById(id); // ID로 회원 정보 조회
        member.ifPresent(value -> model.addAttribute("member", value)); // 회원 정보가 있으면 모델에 데이터를 담아서 뷰로전달
        return "member/update"; // 회원 수정 페이지로 이동
    }

    // 회원 수정 처리 (회원 정보 업데이트)
    @PostMapping("/update/{id}")
    public ResponseEntity<?> updateMember(@PathVariable("id") Long id, @RequestBody MemberEntity memberEntity) {
        try {
            memberEntity.setId(id); // ID 설정
            memberService.updateMember(memberEntity); // 서비스로 회원 업데이트 요청
            return ResponseEntity.ok(Collections.singletonMap("code", "SU")); // 성공 시 코드 반환
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Collections.singletonMap("code", "FA")); // 실패 시 코드 반환
        }
    }

    // 회원 삭제 확인 페이지
    @GetMapping("/delete/{id}")
    public String deleteForm(@PathVariable("id") Long id, Model model) {
        Optional<MemberEntity> member = memberService.getMemberById(id); // ID로 회원 정보 조회
        member.ifPresent(value -> model.addAttribute("member", value)); // 회원 정보가 있으면 모델에 추가
        return "member/delete"; // 삭제 확인 페이지로 이동
    }

    // 회원 삭제 처리
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        memberService.deleteMember(id); // 서비스로 회원 삭제 요청
        return "redirect:/"; // 회원 삭제 후 메인 페이지로 리다이렉트
    }

    // 전체 회원 조회 (관리자 페이지)
    @GetMapping("/memberList")
    public String list(Model model) {
        List<MemberEntity> members = memberService.getAllMembers(); // 모든 회원 조회
        model.addAttribute("members", members); // 조회된 회원 리스트를 모델에 추가
        return "member/memberList"; // 회원 리스트 페이지로 이동
    }

    // 회원 상세 정보 조회 (관리자 페이지)
    @GetMapping("/memberDetail/{id}")
    public String memberDetail(@PathVariable("id") Long id, Model model) {
        Optional<MemberEntity> member = memberService.getMemberById(id); // ID로 회원 상세 정보 조회
        member.ifPresent(value -> model.addAttribute("member", value)); // 회원 정보가 있으면 모델에 추가
        return "member/memberDetail"; // 회원 상세 정보 페이지로 이동
    }

    // 아이디 찾기 폼 페이지 반환
    @GetMapping("/find-id")
    public String findIdForm() {
        return "member/find-id"; // 아이디 찾기 페이지로 이동
    }

    // 아이디 찾기 처리
    @PostMapping("/find-id")
    public ResponseEntity<?> findId(@RequestBody Map<String, String> requestBody) {
        String email = requestBody.get("email"); // 입력된 이메일 가져오기
        boolean success = memberService.sendIdToEmail(email); // 이메일로 아이디 발송
        return ResponseEntity.ok(Collections.singletonMap("success", success)); // 성공 여부 반환
    }

    // 비밀번호 찾기 폼 페이지 반환
    @GetMapping("/find-password")
    public String findPasswordForm() {
        return "member/find-password"; // 비밀번호 찾기 페이지로 이동
    }

    // 비밀번호 찾기 처리
    @PostMapping("/find-password")
    public ResponseEntity<?> findPassword(@RequestBody Map<String, String> requestBody) {
        String memberId = requestBody.get("memberId"); // 입력된 회원 ID 확인
        String email = requestBody.get("email"); // 입력된 이메일 확인
        boolean success = memberService.sendPasswordResetLink(memberId, email); // 비밀번호 재설정 링크 발송
        return ResponseEntity.ok(Collections.singletonMap("success", success)); // 성공 여부 반환
    }

    // 이메일 중복 확인 처리
    @PostMapping("/email-check")
    public ResponseEntity<Map<String, String>> checkEmail(@RequestBody Map<String, String> request) {
        String memberEmail = request.get("memberEmail"); // 입력된 이메일 가져오기
        boolean isDuplicate = memberService.isEmailDuplicate(memberEmail); // 이메일 중복 여부 확인
        if (isDuplicate) {
            return ResponseEntity.ok(Collections.singletonMap("code", "DI")); // 중복 시 코드 반환
        } else {
            return ResponseEntity.ok(Collections.singletonMap("code", "SU")); // 중복 아닐 시 코드 반환
        }
    }
}
