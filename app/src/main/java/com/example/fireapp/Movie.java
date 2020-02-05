package com.example.fireapp;

import java.util.ArrayList;

public class Movie {

    private String movieID;
    private String movieName;
    private String rating;
    private ArrayList<String> arr;

    public Movie(){
    }

    public Movie(String movieID, String movieName, String rating,ArrayList<String> arr) {
        this.movieID = movieID;
        this.movieName = movieName;
        this.rating = rating;
        this.arr=arr;
    }

    public String getMovieID() {
        return movieID;
    }

    public String getMovieName() {
        return movieName;
    }

    public String getRating() {
        return rating;
    }

    public ArrayList<String> getArr(){
        return arr;
    }
}
