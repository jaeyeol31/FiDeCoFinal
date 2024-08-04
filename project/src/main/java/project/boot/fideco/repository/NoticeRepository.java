package project.boot.fideco.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import project.boot.fideco.entity.Notice;

public interface NoticeRepository extends JpaRepository<Notice, Integer>{
	
	// 공지사항 전체 리스트 내림차순 @Query 추가해주지 않으면 NoticeNum 명칭(언더바)때문에 에러남.
	@Query("SELECT n FROM Notice n ORDER BY n.notice_num DESC")
	List<Notice> findAllByOrderByNoticeNumDesc();

	@Query("SELECT n FROM Notice n WHERE n.notice_title LIKE %:keyword%")
	List<Notice> findByNoticeTitleContaining(@Param("keyword") String keyword);
}
