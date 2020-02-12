package com.example.fireapp;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ShowTimes {
    private String statusId;
    private String movieId;
    private String date;
    private String hour;
    private String hallName;
    private Hall hall;
    private List<List<Integer>> seatsHall;


    public ShowTimes(){}

    public ShowTimes(String statusId, String movieId, String date, String hour, Hall hall){
        this.statusId=statusId;
        this.movieId = movieId;
        this.date = date;
        this.hour = hour;
        this.hallName = hall.getHallName();
        int row = hall.getRow();
        int column = hall.getColumn();


        // seatsHall = new ArrayList[10][10];
        seatsHall = new ArrayList<>(row);
        for (int i=0; i<row; i++)
            seatsHall.add(new ArrayList<>(column));

        for (int i=0; i<row; i++)
          for (int j=0; j<column; j++)
            seatsHall.get(i).add(j,0);
    }

    public String getStatusId() { return statusId; }

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

    public List<List<Integer>> getSeatsHall() {
        return seatsHall;
    }

}
