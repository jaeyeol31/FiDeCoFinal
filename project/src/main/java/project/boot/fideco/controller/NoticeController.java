package project.boot.fideco.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import project.boot.fideco.dto.NoticeDTO;
import project.boot.fideco.entity.Notice;
import project.boot.fideco.member.entity.MemberEntity;
import project.boot.fideco.service.NoticeService;

@Controller
@RequiredArgsConstructor
public class NoticeController {
	private static final Logger logger = LogManager.getLogger(NoticeController.class);
	@Inject
	private final NoticeService noticeService;

	@GetMapping("/NoticeSelect")
	public String list(Model model) {
		model.addAttribute("list", noticeService.findAllNotices());
		logger.info("list - {}", model);
		return "./notice/notice_select_all_view";
	}

	@GetMapping("/NoticeSelectDetail")
	public String detail(Model model, @RequestParam("notice_num") Integer notice_num) {
		noticeService.incrementNoticeHit(notice_num); // 조회수 증가
		model.addAttribute("noticeDTO", noticeService.findNoticeById(notice_num));
		return "./notice/notice_select_view";
	}

	@GetMapping("/NoticeInsert")
	public String insert() {
		return "./notice/notice_insert";
	}

	@PostMapping("/NoticeInsert")
	public String insert(Model model, NoticeDTO noticeDTO, @RequestParam("file") MultipartFile file) throws Exception{
		Notice notice = new Notice();
		notice.setNotice_num(noticeDTO.getNotice_num());
		notice.setNotice_title(noticeDTO.getNotice_title());
		notice.setNotice_content(noticeDTO.getNotice_content());
		notice.setNotice_writer(noticeDTO.getNotice_writer());
		notice.setNotice_registday(LocalDate.now()); // 작성일 현재 날짜로 변경
		notice.setNotice_hit(noticeDTO.getNotice_hit());
		noticeService.saveNotice(notice, file);
		model.addAttribute("list", noticeService.findAllNotices());
		
		return "./notice/notice_insert_view";
	}

	@GetMapping("/NoticeUpdate")
	public String update(Model model, @RequestParam("notice_num") Integer notice_num) {
		Notice notice = noticeService.findNoticeById(notice_num);
		if (notice != null) {
			NoticeDTO noticeDTO = new NoticeDTO();
			noticeDTO.setNotice_num(notice.getNotice_num());
			noticeDTO.setNotice_title(notice.getNotice_title());
			noticeDTO.setNotice_content(notice.getNotice_content());
			noticeDTO.setNotice_writer(notice.getNotice_writer());
			noticeDTO.setNotice_registday(notice.getNotice_registday());
			noticeDTO.setNotice_hit(notice.getNotice_hit());
			noticeDTO.setFilename(notice.getFilename());
	        noticeDTO.setFilepath(notice.getFilepath());
			model.addAttribute("noticeDTO", noticeDTO);
		}
		return "./notice/notice_update";
	}

	@PostMapping("/NoticeUpdate")
	public String update(NoticeDTO noticeDTO, @RequestParam(value = "file", required = false) MultipartFile file) throws Exception {
	    Notice existingNotice = noticeService.findNoticeById(noticeDTO.getNotice_num());
	    if (existingNotice != null) {
	        existingNotice.setNotice_title(noticeDTO.getNotice_title());
	        existingNotice.setNotice_content(noticeDTO.getNotice_content());
	        existingNotice.setNotice_writer(noticeDTO.getNotice_writer());
	        noticeService.saveNotice(existingNotice, file);
	    }
	    return "./notice/notice_update_view";
	}
	
	@PostMapping("/NoticeDelete")
	@ResponseBody
	public ResponseEntity<Map<String, Boolean>> delete(@RequestParam("notice_num") Integer notice_num) {
	    Map<String, Boolean> response = new HashMap<>();
	    try {
	        noticeService.deleteNoticeById(notice_num);
	        response.put("success", true);
	    } catch (Exception e) {
	        response.put("success", false);
	    }
	    return ResponseEntity.ok(response);
	}
	
	// 검색 기능
	@GetMapping("/NoticeSearch")
	public String search(@RequestParam("keyword") String keyword, Model model) {
		List<Notice> noticeList = noticeService.search(keyword);
		model.addAttribute("list", noticeList);
		return "./notice/notice_select_all_view";  // 검색 결과를 보여줄 페이지
	}
	
	
}