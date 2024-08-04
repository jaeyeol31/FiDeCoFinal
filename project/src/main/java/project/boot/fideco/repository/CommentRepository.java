package project.boot.fideco.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import project.boot.fideco.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

	@Query("SELECT c FROM Comment c WHERE c.notice.notice_num = :notice_num")
	List<Comment> findByNoticeNum(@Param("notice_num") Integer notice_num);

	@Modifying
	@Transactional
	@Query("DELETE FROM Comment c WHERE c.notice.notice_num = :notice_num")
	void deleteByNotice(@Param("notice_num") Integer notice_num);
}
