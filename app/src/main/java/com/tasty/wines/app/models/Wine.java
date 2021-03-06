package com.tasty.wines.app.models;


import java.util.Calendar;
import com.google.firebase.database.Exclude;

public class Wine {

    public static final String RED = "red";
    public static final String WHITE = "white";
    public static final String ROSE = "rose";

    private String color;

    private String grape;

    private int year;

    private String region;

    private String winery;

    private String country;

    private String name;

    private float rating = 0;

    @Exclude
    private String key;

    private Calendar dateAdded;

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
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public Calendar getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Calendar dateAdded) {
        this.dateAdded = dateAdded;
    }
}
