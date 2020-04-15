package com.example.planetmovieapp.Objects;

import java.util.ArrayList;

public class ShowTimes {
    private String showId;
    private String movieId;
    private String date;
    private String hour;
    private String hallName;
    private Hall hall;
    private ArrayList<String> seatsHall;
    //private List<List<Integer>> seatsHall;


    public ShowTimes(){}

    public ShowTimes(String showId, String movieId, String date, String hour, Hall hall){
        this.showId = showId;
        this.movieId = movieId;
        this.date = date;
        this.hour = hour;
        this.hallName = hall.getHallName();
        this.hall=hall;
        int row = hall.getRow();
        int column = hall.getColumn();

        seatsHall = new ArrayList<>();
        for(int i = 0 ; i < (row*column); i++)
            seatsHall.add(i,"0");
    }

    public String getShowId() { return showId; }

    public String getMovieId() {
        return movieId;
    }

    public String getDate() {
        return date;
    }

    public String getHour() {
        return hour;
    }

    public String getHallName(){ return hallName;}

    public Hall getHall() {
        return hall;
    }

    public ArrayList<String> getSeatsHall() {
        return seatsHall;
    }



}
