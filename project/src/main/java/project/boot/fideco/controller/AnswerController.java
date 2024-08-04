
package project.boot.fideco.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import project.boot.fideco.dto.AnswerForm;
import project.boot.fideco.entity.Answer;
import project.boot.fideco.entity.Question;
import project.boot.fideco.service.AnswerService;
import project.boot.fideco.service.QuestionService;

@RequestMapping("/answer")
@RequiredArgsConstructor
@Controller
public class AnswerController {

	private final QuestionService questionService;
	private final AnswerService answerService;

	@PostMapping("/create/{id}")
	public String createAnswer(Model model, @PathVariable("id") Integer id, @Valid AnswerForm answerForm,
			BindingResult bindingResult) {
		Question question = this.questionService.getQuestion(id);
		if (bindingResult.hasErrors()) {
			model.addAttribute("question", question);
			return "qna/question_detail";
		}
		this.answerService.create(question, answerForm.getContent());
		return String.format("redirect:/question/detail/%s", id);
	}

	@GetMapping("/modify/{id}")
	public String answerModify(AnswerForm answerForm, @PathVariable("id") Integer id) {
		Answer answer = this.answerService.getAnswer(id);
		answerForm.setContent(answer.getContent());
		return "qna/answer_form";
	}

	@PostMapping("/modify/{id}")
	public String answerModify(@Valid AnswerForm answerForm, BindingResult bindingResult,
			@PathVariable("id") Integer id) {
		if (bindingResult.hasErrors()) {
			return "qna/answer_form";
		}
		Answer answer = this.answerService.getAnswer(id);
		this.answerService.modify(answer, answerForm.getContent());
		return String.format("redirect:/question/detail/%s", answer.getQuestion().getId());
	}
	
	@GetMapping("/delete/{id}")
    public String answerDelete(@PathVariable("id") Integer id) {
        Answer answer = this.answerService.getAnswer(id);
        this.answerService.delete(answer);
        return String.format("redirect:/question/detail/%s", answer.getQuestion().getId());
    }
}