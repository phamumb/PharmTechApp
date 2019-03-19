package com.bhcc.app.pharmtech.data.model;

import java.util.Date;

/**
 * Created by Luat on 10/31/2017.
 */

public class Score {
    private int id;
    private double score;
    private int incorrect;
    private int correct;
    private Date date;
    private int totalQuestion;

    public Score(double score,int totalQuestion, int correct, int incorrect,Date date) {
        this.totalQuestion = totalQuestion;
        this.incorrect = incorrect;
        this.correct = correct;
        this.date = date;
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIncorrect() {
        return incorrect;
    }

    public int getCorrect() {
        return correct;
    }


    public void setIncorrect(int incorrect) {
        this.incorrect = incorrect;
    }

    public void setCorrect(int correct) {
        this.correct = correct;
    }

    public Date getDate() {
        return date;
    }

    public int getTotalQuestion() {
        return totalQuestion;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setTotalQuestion(int totalQuestion) {
        this.totalQuestion = totalQuestion;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
