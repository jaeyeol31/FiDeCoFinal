package project.boot.fideco.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import project.boot.fideco.entity.Answer;
import project.boot.fideco.entity.Question;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {
	
//	  Page<Answer> findAllByQuestion(Question question, Pageable pageable);

}
