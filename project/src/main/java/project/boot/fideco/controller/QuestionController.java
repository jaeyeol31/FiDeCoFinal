package project.boot.fideco.controller;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import project.boot.fideco.dto.AnswerForm;
import project.boot.fideco.dto.QuestionForm;
import project.boot.fideco.entity.Question;
import project.boot.fideco.service.QuestionService;

@RequestMapping("/question")
@RequiredArgsConstructor // 생성자. final이 붙은 속성을 포함하는 생성자를 자동으로 만들어 주는 역할
@Controller
public class QuestionController {

	private final QuestionService questionService;

	@GetMapping("/list")
	public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {
		Page<Question> paging = this.questionService.getList(page);
		model.addAttribute("paging", paging);
		return "qna/question_list";
	}

	@GetMapping(value = "/detail/{id}")
	public String detail(Model model, @PathVariable("id") Integer id, AnswerForm answerForm) { // 요청한 URL인 http://localhost:8080/question/detail/2의 숫자 2처럼 변하는 id값을 얻을 때에는 @PathVariable애너테이션을 사용,
																								// @GetMapping(value ="/question/detail/{id}")에서 사용한 id와 @PathVariable("id")의 매개변수 이름 동일해야함
		Question question = this.questionService.getQuestion(id);
		model.addAttribute("question", question);
		return "qna/question_detail";
	}

	@GetMapping("/create")
	public String questionCreate(QuestionForm questionForm) {
		return "qna/question_form";
	}

	@PostMapping("/create")
	public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "qna/question_form";
		}
		this.questionService.create(questionForm.getSubject(), questionForm.getContent());
		return "redirect:/question/list";
	}

	@GetMapping("/modify/{id}")
	public String questionModify(QuestionForm questionForm, @PathVariable("id") Integer id) {
		Question question = this.questionService.getQuestion(id);
		questionForm.setSubject(question.getSubject());
		questionForm.setContent(question.getContent());
		return "qna/question_form";
	}

	@PostMapping("/modify/{id}")
	public String questionModify(@Valid QuestionForm questionForm, BindingResult bindingResult, @PathVariable("id") Integer id) {
		if (bindingResult.hasErrors()) {
			return "qna/question_form";
		}
		Question question = this.questionService.getQuestion(id);
		this.questionService.modify(question, questionForm.getSubject(), questionForm.getContent());
		return String.format("redirect:/question/detail/%s", id);
	}
	
	@GetMapping("/delete/{id}")
    public String questionDelete(@PathVariable("id") Integer id) {
        Question question = this.questionService.getQuestion(id);
        this.questionService.delete(question);
        return "redirect:/question/list";
    }
	
	
}
