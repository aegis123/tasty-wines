package com.tasty.wines.app.models;


import java.util.List;

public class Wine {

    public static final String RED = "red";
    public static final String WHITE = "white";
    public static final String ROSE = "rose";

    private long id;

    private String color;

    private String grape;

    private int year;

    private String region;

    private String winery;

    private String country;

    private String name;

    private List<Review> reviews;

    private float rating = -1;

    public Wine() {
    }

    public Wine(String color, String grape, int year, String region, String winery, String country, String name) {
        this.color = color;
        this.grape = grape;
        this.year = year;
        this.region = region;
        this.winery = winery;
        this.country = country;
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getGrape() {
        return grape;
    }

    public void setGrape(String grape) {
        this.grape = grape;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getWinery() {
        return winery;
    }

    public void setWinery(String winery) {
        this.winery = winery;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getRating() {
        if (rating == -1) {
            double score = 0;
            for (int i = 0; i < reviews.size(); i++) {
                score += (double) reviews.get(i).getScore();
            }
            float avg = (float) (score / reviews.size());
            if (avg > -1) {
                rating = avg;
            } else {
                return 0;
            }
        }
        return rating;
    }
}
