package project.boot.fideco.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import jakarta.inject.Inject;
import project.boot.fideco.config.ImageUtils;
import project.boot.fideco.entity.Notice;
import project.boot.fideco.repository.CommentRepository;
import project.boot.fideco.repository.NoticeRepository;

@Service

public class NoticeService {
	@Inject
	public NoticeRepository noticeRepository;
	@Inject
	public CommentRepository commentRepository;

	private static final Logger logger = LoggerFactory.getLogger(NoticeService.class);

	@Transactional
	public Notice saveNotice(Notice notice, MultipartFile file) throws Exception {
		Notice existingNotice = null;
		if (notice.getNotice_num() != null) {
			existingNotice = noticeRepository.findById(notice.getNotice_num()).orElse(null);
		}

		if (file != null && !file.isEmpty()) {
			// 새 파일 처리 로직 (기존 코드와 동일)
			BufferedImage originalImage = ImageIO.read(file.getInputStream());
			BufferedImage resizedImage = ImageUtils.resizeImage(originalImage, 500, 500);
			String projectPath = System.getProperty("user.dir") + "/src/main/resources/static/files";
			UUID uuid = UUID.randomUUID();
			String fileName = uuid + "_" + file.getOriginalFilename();
			File saveFile = new File(projectPath, fileName);
			ImageIO.write(resizedImage, "jpg", saveFile);

			notice.setFilename(fileName);
			notice.setFilepath("/files/" + fileName);
		} else if (existingNotice != null) {
			// 기존 파일 정보 유지
			notice.setFilename(existingNotice.getFilename());
			notice.setFilepath(existingNotice.getFilepath());
		}

		if (existingNotice != null) {
			// 기존 등록일과 조회수 유지
			notice.setNotice_registday(existingNotice.getNotice_registday());
			notice.setNotice_hit(existingNotice.getNotice_hit());
			notice.setNotice_writer(existingNotice.getNotice_writer());
		} else {
			notice.setNotice_registday(LocalDate.now());
			notice.setNotice_hit(0);
		}

		return noticeRepository.save(notice);
	}

	@Transactional(readOnly = true)
	public List<Notice> findAllNotices() {
		return noticeRepository.findAllByOrderByNoticeNumDesc();
	}

	@Transactional(readOnly = true)
	public Notice findNoticeById(Integer notice_num) {
		return noticeRepository.findById(notice_num).orElse(null);
	}

	@Transactional
	public void deleteNoticeById(Integer notice_num) {
		//무결성 제약조건때문에 해당 notice_num의 아이디를 삭제하려면 comment를 먼저 삭제해야함.
		commentRepository.deleteByNotice(notice_num);
		// 그 다음 공지사항을 삭제
		noticeRepository.deleteById(notice_num);
	}

	public boolean existsById(Integer notice_num) {
		return noticeRepository.existsById(notice_num);
	}

	// 조회수 증가.
	@Transactional
	public void incrementNoticeHit(Integer notice_num) {
		Notice notice = noticeRepository.findById(notice_num).orElse(null);
		if (notice != null) {
			notice.setNotice_hit(notice.getNotice_hit() + 1);
			noticeRepository.save(notice);
		}
	}

	// 검색 기능
	@Transactional
	public List<Notice> search(String keyword) {
		return noticeRepository.findByNoticeTitleContaining(keyword);
	}

	// 답변 기능
	public List<Notice> getList() {
		return this.noticeRepository.findAll();
	}

}
