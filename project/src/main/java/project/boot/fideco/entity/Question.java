package project.boot.fideco.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "QUESTION")
public class Question {

	@Id // 기본키
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 일일이 값을 넣지 않아도 1씩 증가
	private Integer id;

	@Column(length = 200)
	private String subject;

	@Column(columnDefinition = "TEXT")
	private String content;

	private LocalDateTime createDate;

	@OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE) //질문을 삭제하면 답변들도 모두 삭제
	private List<Answer> answerList;
	
	private LocalDateTime modifyDate;
}
