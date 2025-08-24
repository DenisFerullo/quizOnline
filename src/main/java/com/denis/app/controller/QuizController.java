package com.denis.app.controller;

import com.denis.app.entity.Evaluation;
import com.denis.app.entity.Question;
import com.denis.app.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import java.util.ArrayList;
import java.util.Random;

@Controller
public class QuizController {

	@Autowired
	private QuestionRepository questionRepository;

	private final Random random = new Random();

	private final ArrayList<Question> history = new ArrayList();

	private final ArrayList<Evaluation> evaluationHistory = new ArrayList<Evaluation>();

	@GetMapping("/quiz")
	public String showQuestion(@RequestParam(value = "quizIndex", required = false, defaultValue = "0") int quizIndex,
			Model model) {

		// Configurazione iniziale per la pagina
		// ----------------------------------------------------- //
		Question currentQuestion;
		int questionIndex;
		ArrayList<Question> questions = questionRepository.findAll();

		// valutiamo se l'esito è andato a buon fine, altrimenti mandiamo un messaggio
		// di errore.
		if (questions.isEmpty()) {
			model.addAttribute("error", "Nessuna domanda caricata");
			return "errorPage";
		}
		// -------------------------------------------------------------------------------------------
		// //

		if (quizIndex >= 0 && quizIndex < history.size()) {
			// Torna a una domanda passata
			currentQuestion = history.get(quizIndex);
			questionIndex = quizIndex;
		} else {
			// Seleziono una domanda dall'archivio, scelta randomicamente
			int max = questions.size(); // numero di domande in archivio
			questionIndex = random.nextInt(max);
			currentQuestion = questions.get(questionIndex);

			// Controllo limiti dell'indice
			if (quizIndex < 0)
				quizIndex = 0;
			if (quizIndex > questions.size())
				quizIndex = questions.size();

			// Aggiungo la domanda alla cronologia delle domande
			history.add(currentQuestion);
		}

		model.addAttribute("question", currentQuestion);
		model.addAttribute("questionIndex", questionIndex);
		model.addAttribute("quizIndex", quizIndex);
		model.addAttribute("total", questions.size());

		return "quiz";
	}

	@GetMapping("/quiz/next")
	public String nextQuestion(@RequestParam("quizIndex") int quizIndex) {
		// Incremento dell'indice
		return "redirect:/quiz?quizIndex=" + (quizIndex + 1);
	}

	@GetMapping("/quiz/prev")
	public String previousQuestion(@RequestParam("quizIndex") int quizIndex) {

		if (quizIndex > 0) {
			quizIndex--;
		}
		return "redirect:/quiz?quizIndex=" + quizIndex;
	}

	@GetMapping("/quiz/reset")
	public String resetQuiz(SessionStatus status) {
		status.setComplete();
		return "redirect:/quiz";
	}

	@PostMapping("/quiz/evaluate")
	public String evaluateAnswer(@RequestParam int quizIndex, @RequestParam int rating, Model model) {

		// Qui salviamo la valutazione in una lista "history"
		// history può essere una struttura tipo: List<Evaluation>
		// dove Evaluation ha: quizIndex, domanda, voto
		evaluationHistory.add(new Evaluation(quizIndex, rating));

		// Torniamo alla stessa domanda o proseguiamo
		return "redirect:/quiz?quizIndex=" + (quizIndex + 1);
	}

	@GetMapping("/feedback")
	public String feedback(Model model) {
		model.addAttribute("evaluations", evaluationHistory);

		double avgOriginal = evaluationHistory.stream().mapToInt(Evaluation::getRating).average().orElse(0);
		
		double avgBase10 = (avgOriginal /5.0) * 10.0;

		model.addAttribute("average", avgBase10);

		return "feedback";
	}

	@GetMapping(path = "/quiz/nuovo")
	public String nuovoQuiz() {
		// Svuota le liste per ripartire da zero
		history.clear();
		evaluationHistory.clear();

		// Reindirizza alla prima domanda del quiz
		return "redirect:/quiz?quizIndex=0";
	}

	@GetMapping(path = "/test")
	public String showTest(Model model) {

		Question q = new Question(1, "Qual è la capitale d'Italia?", "Inizia con R...", "Roma", "La capitale è Roma");
		model.addAttribute("question", q);
		return "test";
	}

}
