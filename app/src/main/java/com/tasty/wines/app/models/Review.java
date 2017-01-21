package com.tasty.wines.app.models;


import java.util.Calendar;

public class Review {

    public long id;
    public String body;
    public int score;
    public Calendar date;

    public User user;

    public Review() {
    }

    public Review(long id, String body, int score, Calendar date, User user) {
        this.id = id;
        this.body = body;
        this.score = score;
        this.date = date;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
