package com.denis.app.controller;

import com.denis.app.entity.Question;
import com.denis.app.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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

	@GetMapping("/quiz")
	public String showQuestion(@RequestParam(value="quizIndex", required=false, defaultValue="0") int quizIndex,
								@RequestParam(value="showHint", required=false, defaultValue="false") boolean hint,
								@RequestParam(value="showAnswer", required=false, defaultValue="false") boolean answer,
								Model model) {

		// Configurazione iniziale per la pagina ----------------------------------------------------- //
		Question currentQuestion;
		int questionIndex;
		ArrayList<Question> questions = questionRepository.findAll();
		
		// valutiamo se l'esito è andato a buon fine, altrimenti mandiamo un messaggio di errore.
		if (questions.isEmpty()) {
	        model.addAttribute("error", "Nessuna domanda caricata");
	        return "errorPage";
	    }
		//------------------------------------------------------------------------------------------- //
		
		if (quizIndex >= 0 && quizIndex < history.size()) {
				// Torna a una domanda passata
				currentQuestion = history.get(quizIndex);
				questionIndex = quizIndex;
		} else {
				// Seleziono una domanda dall'archivio, scelta randomicamente				
				    int max = questions.size();											// numero di domande in archivio 
				  questionIndex = random.nextInt(max);
				currentQuestion = questions.get(questionIndex);
				
				// Controllo limiti dell'indice
			    if (quizIndex < 0) quizIndex = 0;
			    if (quizIndex > questions.size()) quizIndex = questions.size();
			    
			    
				// Aggiungo la domanda alla cronologia delle domande 
				history.add(currentQuestion);
		}
		
		model.addAttribute("question", currentQuestion);
		model.addAttribute("questionIndex", questionIndex);
		model.addAttribute("quizIndex", quizIndex);
		model.addAttribute("total" , questions.size());
		
		// mostra suggerimento se richiesto
		model.addAttribute("showHint", hint);
		
		// mostra la risposta se richiesto
		model.addAttribute("showAnswer", answer);
		return "quiz";
	}
	
	
	
	
	

	@GetMapping("/quiz/next")
	public String nextQuestion( @RequestParam("quizIndex") int quizIndex ) {
		// Incremento dell'indice
		return "redirect:/quiz?quizIndex=" + (quizIndex + 1);
	}

	
	
	@GetMapping("/quiz/prev")
	public String previousQuestion (@RequestParam("quizIndex") int quizIndex ) {
		
		if (quizIndex > 0) {
			quizIndex--;
		}
		return "redirect:/quiz?quizIndex=" + quizIndex;
	}
	
	

	@GetMapping("/quiz/hint")
	public String showHint(@RequestParam("quizIndex") int quizIndex) {
		return "redirect:/quiz?quizIndex=" + quizIndex + "&showHint=true";
	}

	
	
	@GetMapping("/quiz/answer")
	public String showAnswer(@RequestParam("quizIndex") int quizIndex) {
		return "redirect:/quiz?quizIndex=" + quizIndex + "&showHint=true&showAnswer=true";
	}

	@GetMapping("/quiz/reset")
	public String resetQuiz(SessionStatus status) {
		status.setComplete();
		return "redirect:/quiz";
	}
}
