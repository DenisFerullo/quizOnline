package com.denis.app.entity;

public class Question {
	private int id;
	private String question;
	private String shortAnswer;
	private String fullAnswer;
	private String hint;

	public Question(int id, String question, String shortAnswer, String fullAnswer, String hint) {
		this.id = id;
		this.question = question;
		this.shortAnswer = shortAnswer;
		this.fullAnswer = fullAnswer;
		this.hint = hint;
	}

	// getter e setter
	public int getId() {
		return id;
	}

	public String getQuestion() {
		return question;
	}

	public String getShortAnswer() {
		return shortAnswer;
	}

	public String getFullAnswer() {
		return fullAnswer;
	}

	public String getHint() {
		return hint;
	}
}
