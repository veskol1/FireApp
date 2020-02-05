package com.example.fireapp;


import java.util.HashMap;
import java.util.Map;

public class Movie {

    private String movieID;
    private String movieName;
    private String rating;
    private Hall hall;
    private Map<String,String> showTimeMap ;

    public Movie(){
    }

    public Movie(String movieID, String movieName, String rating,Hall hall,String day,String hour) {
        this.movieID = movieID;
        this.movieName = movieName;
        this.rating = rating;
        this.hall=hall;
        showTimeMap = new HashMap<>();
        showTimeMap.put(day,hour);
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

    public Map<String,String> getShowTimeMap(){
        return showTimeMap;
    }

    public Hall getHall(){
        return hall;
    }
}
