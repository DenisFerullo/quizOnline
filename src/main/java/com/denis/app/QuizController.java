package com.denis.app;

import com.denis.app.entity.Question;
import com.denis.app.repository.QuestionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class QuizController {

	private final QuestionRepository questionRepository;

	public QuizController(QuestionRepository questionRepository) {
		this.questionRepository = questionRepository;
	}

	@GetMapping("/quiz")
	public String showQuestion(@RequestParam(required = false) Integer index,
			@RequestParam(required = false, defaultValue = "false") boolean hint,
			@RequestParam(required = false, defaultValue = "false") boolean answer, Model model) {
		List<Question> questions = questionRepository.findAll();

		if (questions.isEmpty()) {
			model.addAttribute("error", "Nessuna domanda caricata");
			return "errorPage";
		}

		int currentIndex = (index == null) ? 0 : index;

		// Controllo limiti dell'indice
		if (currentIndex < 0)
			currentIndex = 0;
		if (currentIndex >= questions.size())
			currentIndex = questions.size() - 1;

		Question question = questions.get(currentIndex);

		model.addAttribute("question", question);
		model.addAttribute("index", currentIndex);
		model.addAttribute("total", questions.size());
		model.addAttribute("showHint", hint);
		model.addAttribute("showAnswer", answer);

		return "quiz";
	}

	@GetMapping("/quiz/hint")
	public String showHint(@RequestParam int index) {
		return "redirect:/quiz?index=" + index + "&hint=true";
	}

	@GetMapping("/quiz/answer")
	public String showAnswer(@RequestParam int index) {
		return "redirect:/quiz?index=" + index + "&hint=true&answer=true";
	}

	@GetMapping("/quiz/prev")
	public String previousQuestion(@RequestParam int index) {
		int prevIndex = Math.max(index - 1, 0);
		return "redirect:/quiz?index=" + prevIndex;
	}

	@GetMapping("/quiz/next")
	public String nextQuestion(@RequestParam int index) {
		int nextIndex = Math.min(index + 1, questionRepository.findAll().size() - 1);
		return "redirect:/quiz?index=" + nextIndex;
	}
}
