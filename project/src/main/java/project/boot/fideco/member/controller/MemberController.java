//package project.boot.fideco.member.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//import project.boot.fideco.member.entity.MemberEntity;
//import project.boot.fideco.member.service.MemberService;
//
//import java.util.Collections;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//
//@Controller
//@RequestMapping("/member")
//public class MemberController {
//
//    @Autowired
//    private MemberService memberService;
//
//    @GetMapping("/signup")
//    public String signupForm() {
//        return "member/signup";
//    }
//
//    // 마이페이지
//    @GetMapping("/myPage/{id}")
//    public String memberMypage(@PathVariable("id") Long id, Model model) {
//        Optional<MemberEntity> member = memberService.getMemberById(id);
//        member.ifPresent(value -> model.addAttribute("member", value));
//        return "member/myPage";
//    }
//
//    // 수정 폼
//    @GetMapping("/update/{id}")
//    public String editForm(@PathVariable("id") Long id, Model model) {
//        Optional<MemberEntity> member = memberService.getMemberById(id);
//        member.ifPresent(value -> model.addAttribute("member", value));
//        return "member/update";
//    }
//
//    // 수정 처리
//    @PostMapping("/update/{id}")
//    public String update(@PathVariable("id") Long id, @ModelAttribute MemberEntity memberEntity) {
//        memberEntity.setId(id);
//        memberService.updateMember(memberEntity);
//        return "redirect:/member/memberList";
//    }
//
//    // 삭제 확인 폼
//    @GetMapping("/delete/{id}")
//    public String deleteForm(@PathVariable("id") Long id, Model model) {
//        Optional<MemberEntity> member = memberService.getMemberById(id);
//        member.ifPresent(value -> model.addAttribute("member", value));
//        return "member/delete"; // 삭제 확인 페이지로 이동
//    }
//
//    // 삭제 처리
//    @PostMapping("/delete/{id}")
//    public String delete(@PathVariable("id") Long id) {
//        memberService.deleteMember(id);
//        return "redirect:/member/memberList";
//    }
//
//    // 관리자 접근용
//
//    // 전체 조회
//    @GetMapping("/memberList")
//    public String list(Model model) {
//        List<MemberEntity> members = memberService.getAllMembers();
//        model.addAttribute("members", members);
//        return "member/memberList";
//    }
//
//    // 회원 상세 정보
//    @GetMapping("/memberDetail/{id}")
//    public String memberDetail(@PathVariable("id") Long id, Model model) {
//        Optional<MemberEntity> member = memberService.getMemberById(id);
//        member.ifPresent(value -> model.addAttribute("member", value));
//        return "member/memberDetail";
//    }
//
//    // 아이디 찾기 폼
//    @GetMapping("/find-id")
//    public String findIdForm() {
//        return "member/find-id";
//    }
////
////    // 아이디 찾기 처리
////    @PostMapping("/find-id")
////    public ResponseEntity<?> findId(@RequestBody Map<String, String> requestBody) {
////        String email = requestBody.get("email");
////        boolean success = memberService.sendIdToEmail(email);
////        return ResponseEntity.ok(Collections.singletonMap("success", success));
////    }
//
//    // 비밀번호 찾기 폼
//    @GetMapping("/find-password")
//    public String findPasswordForm() {
//        return "member/find-password";
//    }
//
////    // 비밀번호 찾기 처리
////    @PostMapping("/find-password")
////    public ResponseEntity<?> findPassword(@RequestBody Map<String, String> requestBody) {
////        String memberId = requestBody.get("memberId");
////        String email = requestBody.get("email");
////        boolean success = memberService.sendPasswordResetLink(memberId, email);
////        return ResponseEntity.ok(Collections.singletonMap("success", success));
////    }
//}


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
    private MemberService memberService;

    @GetMapping("/signup")
    public String signupForm() {
        return "member/signup";
    }

    // 마이페이지
    @GetMapping("/myPage/{id}")
    public String memberMypage(@PathVariable("id") Long id, Model model) {
        Optional<MemberEntity> member = memberService.getMemberById(id);
        member.ifPresent(value -> model.addAttribute("member", value));
        return "member/myPage";
    }

    // 수정 폼
    @GetMapping("/update/{id}")
    public String editForm(@PathVariable("id") Long id, Model model) {
        Optional<MemberEntity> member = memberService.getMemberById(id);
        member.ifPresent(value -> model.addAttribute("member", value));
        return "member/update";
    }

    // 수정 처리
    @PostMapping("/update/{id}")
    public ResponseEntity<?> updateMember(@PathVariable("id") Long id, @RequestBody MemberEntity memberEntity) {
        try {
            memberEntity.setId(id);
            memberService.updateMember(memberEntity);
            return ResponseEntity.ok(Collections.singletonMap("code", "SU"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Collections.singletonMap("code", "FA"));
        }
    }

    // 삭제 확인 폼
    @GetMapping("/delete/{id}")
    public String deleteForm(@PathVariable("id") Long id, Model model) {
        Optional<MemberEntity> member = memberService.getMemberById(id);
        member.ifPresent(value -> model.addAttribute("member", value));
        return "member/delete"; // 삭제 확인 페이지로 이동
    }

    // 삭제 처리
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        memberService.deleteMember(id);
        return "redirect:/";
    }

    // 관리자 접근용

    // 전체 조회
    @GetMapping("/memberList")
    public String list(Model model) {
        List<MemberEntity> members = memberService.getAllMembers();
        model.addAttribute("members", members);
        return "member/memberList";
    }

    // 회원 상세 정보
    @GetMapping("/memberDetail/{id}")
    public String memberDetail(@PathVariable("id") Long id, Model model) {
        Optional<MemberEntity> member = memberService.getMemberById(id);
        member.ifPresent(value -> model.addAttribute("member", value));
        return "member/memberDetail";
    }

    // 아이디 찾기 폼
    @GetMapping("/find-id")
    public String findIdForm() {
        return "member/find-id";
    }

    // 아이디 찾기 처리
    @PostMapping("/find-id")
    public ResponseEntity<?> findId(@RequestBody Map<String, String> requestBody) {
        String email = requestBody.get("email");
        boolean success = memberService.sendIdToEmail(email);
        return ResponseEntity.ok(Collections.singletonMap("success", success));
    }

    // 비밀번호 찾기 폼
    @GetMapping("/find-password")
    public String findPasswordForm() {
        return "member/find-password";
    }

    // 비밀번호 찾기 처리
    @PostMapping("/find-password")
    public ResponseEntity<?> findPassword(@RequestBody Map<String, String> requestBody) {
        String memberId = requestBody.get("memberId");
        String email = requestBody.get("email");
        boolean success = memberService.sendPasswordResetLink(memberId, email);
        return ResponseEntity.ok(Collections.singletonMap("success", success));
    }
    //이메일 중복확인
    @PostMapping("/email-check")
    public ResponseEntity<Map<String, String>> checkEmail(@RequestBody Map<String, String> request) {
        String memberEmail = request.get("memberEmail");
        boolean isDuplicate = memberService.isEmailDuplicate(memberEmail);
        if (isDuplicate) {
            return ResponseEntity.ok(Collections.singletonMap("code", "DI"));
        } else {
            return ResponseEntity.ok(Collections.singletonMap("code", "SU"));
        }
    }
    
}
