package com.example.fireapp;


import java.util.Map;

public class Movie {

    private String movieID;
    private String movieName;
    private String rating;
    private Map<String,String> dates;
    private Hall hall;

    public Movie(){
    }

    public Movie(String movieID, String movieName, String rating,Map<String,String> dates,Hall hall) {
        this.movieID = movieID;
        this.movieName = movieName;
        this.rating = rating;
        this.dates=dates;
        this.hall=hall;
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

    public Map<String,String> getMap(){
        return dates;
    }

    public Hall getHall(){
        return hall;
    }
}
