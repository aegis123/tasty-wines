package com.tasty.wines.app.models;


public class Review {

    private String body;
    private int score;
    private long date;

    private User user;

    private String wine;

    public Review() {
    }

    public Review(String body, int score, long date, User user, String wine) {
        this.body = body;
        this.score = score;
        this.date = date;
        this.user = user;
        this.wine = wine;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getWine() {
        return wine;
    }

    public void setWine(String wine) {
        this.wine = wine;
    }
}
