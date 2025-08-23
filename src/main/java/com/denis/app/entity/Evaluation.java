package com.denis.app.entity;

public class Evaluation {
	private int quizIndex;
    private int rating;

    public Evaluation(int quizIndex, int rating) {
        this.quizIndex = quizIndex;
        this.rating = rating;
    }

    public int getQuizIndex() { return quizIndex; }
    public int getRating() { return rating; }
}
