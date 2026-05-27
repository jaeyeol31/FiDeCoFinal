package project.boot.fideco.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionForm {
	//질문 등록 페이지에서 사용자로부터 입력받은 값을 검증하는데 필요한 폼 클래스
	//입력값 검증할 때뿐만 아니라 입력 항목을 바인딩할 때도 사용
	@NotEmpty(message = "제목은 필수항목입니다.")
	@Size(max = 200)
	private String subject;

	@NotEmpty(message = "내용은 필수항목입니다.")
	private String content;
}