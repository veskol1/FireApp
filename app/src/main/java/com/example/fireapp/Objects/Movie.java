package com.example.fireapp.Objects;

import java.io.Serializable;

public class Movie implements Serializable {
    private String movieId;
    private String movieName;
    private String genre;
    private String rating;
    private String summary;
    private String trailerLink;
    private String posterLink;
    //private String hallId;


    public Movie(){
    }

    public Movie(String movieId,String movieName, String genre, String rating, String summary, String trailerLink, String posterLink) {
        this.movieId = movieId;
        this.movieName = movieName;
        this.genre = genre;
        this.rating = rating;
        this.summary = summary;
        this.trailerLink = trailerLink;
        this.posterLink = posterLink;
        //this.hallId = hallId;

    }

    public String getMovieId() { return movieId; }

    public String getMovieName() {
        return movieName;
    }

    public String getGenre() {
        return genre;
    }

    public String getRating() {
        return rating;
    }

    public String getSummary() {
        return summary;
    }

    public String getTrailerLink() {
        return trailerLink;
    }

    public String getPosterLink() { return posterLink; }

    //public String getHallId(){ return hallId; }

}
