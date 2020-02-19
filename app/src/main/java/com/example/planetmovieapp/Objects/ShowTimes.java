package com.example.planetmovieapp.Objects;

import java.util.ArrayList;

public class ShowTimes {
    private String statusId;
    private String movieId;
    private String date;
    private String hour;
    private String hallName;
    private Hall hall;
    private ArrayList<String> seatsHall;
    //private List<List<Integer>> seatsHall;


    public ShowTimes(){}

    public ShowTimes(String statusId, String movieId, String date, String hour, Hall hall){
        this.statusId=statusId;
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

//        seatsHall = new ArrayList<>(row);
//        for (int i=0; i<row; i++)
//            seatsHall.add(new ArrayList<>(column));
//
//        for (int i=0; i<row; i++)
//          for (int j=0; j<column; j++)
//            seatsHall.get(i).add(j,0);
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

    public ArrayList<String> getSeatsHall() {
        return seatsHall;
    }

//    public List<List<Integer>> getSeatsHall() {
//        return seatsHall;
//    }

}
