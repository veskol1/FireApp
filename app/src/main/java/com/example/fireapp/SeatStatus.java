package com.example.fireapp;

import android.util.Log;

import java.util.ArrayList;

public class SeatStatus {
    private String movieID;
    private String date;
    private String hour;
    private Hall hall;
//    private int[][] seatsHall;
    private ArrayList<Integer>[][] seatsHall;

    public SeatStatus(String movieID,String date, String hour, Hall hall){
        this.movieID=movieID;
        this.date=date;
        this.hour=hour;

        int row=hall.getRow();
        int column=hall.getColumn();
        Log.d("testoo",""+row);
        Log.d("testoo",""+column);

        seatsHall = new ArrayList[row][column];
        for (int i=0; i<row; i++)
            for(int j=0; j<column; j++)
                seatsHall[i][j].add(0);

    }

    public String getMovieID() {
        return movieID;
    }

    public String getDate() {
        return date;
    }

    public String getHour() {
        return hour;
    }

    public Hall getHall() {
        return hall;
    }

    public ArrayList[][] getSeatsHall() {
        return seatsHall;
    }
}
