package com.denis.app.repository;

import com.denis.app.entity.Question;
import org.springframework.stereotype.Repository;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

@Repository
public class QuestionRepository {

	private final ArrayList<Question> questions = new ArrayList<>();

	
	
	public QuestionRepository() {
		loadQuestionsFromFile("questions.txt");
	}
	
	
	
	private void loadQuestionsFromFile(String fileName) {
		try (InputStream is = getClass().getClassLoader().getResourceAsStream(fileName)) {
			if (is == null) {
				throw new RuntimeException("File not found: " + fileName);
			}

			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			String line;
			int id = 1;

			String domanda = null;
			String rispostaBreve = null;
			String rispostaCompleta = null;
			String suggerimento = null;

			while ((line = reader.readLine()) != null) {
				line = line.trim();
				if (line.isEmpty())
					continue;

				if (line.startsWith("Domanda:")) {
					domanda = line.replace("Domanda:", "").trim();
				} else if (line.startsWith("Risposta Breve:")) {
					rispostaBreve = line.replace("Risposta Breve:", "").trim();
				} else if (line.startsWith("Risposta Completa:")) {
					rispostaCompleta = line.replace("Risposta Completa:", "").trim();
				} else if (line.startsWith("Suggerimento:")) {
					suggerimento = line.replace("Suggerimento:", "").trim();

					// Quando ho completato un blocco creo la Question
					if (domanda != null && rispostaBreve != null && rispostaCompleta != null && suggerimento != null) {
						questions.add(new Question(id++, domanda, rispostaBreve, rispostaCompleta, suggerimento));
						domanda = rispostaBreve = rispostaCompleta = suggerimento = null;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	
	public ArrayList<Question> findAll() {
		return questions;
	}

	
	
	public Question findById(int id) {
		return questions.stream().filter(q -> q.getId() == id).findFirst().orElse(null);
	}
}
