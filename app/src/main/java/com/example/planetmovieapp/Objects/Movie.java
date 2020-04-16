package com.example.planetmovieapp.Objects;

import java.io.Serializable;

/*Movie class represents the Movie details in our App and in our DB as table name*/
public class Movie implements Serializable {
    private String movieId;
    private String movieName;
    private String genre;
    private Integer rating;
    private String summary;
    private String trailerLink;
    private String posterLink;
    //private String hallId;


    public Movie(){
    }

    public Movie(String movieId,String movieName, String genre, Integer rating, String summary, String trailerLink, String posterLink) {
        this.movieId = movieId;
        this.movieName = movieName;
        this.genre = genre;
        this.rating = rating;
        this.summary = summary;
        this.trailerLink = trailerLink;
        this.posterLink = posterLink;

    }

    public String getMovieId() { return movieId; }

    public String getMovieName() {
        return movieName;
    }

    public String getGenre() {
        return genre;
    }

    public Integer getRating() {
        return rating;
    }

    public String getSummary() {
        return summary;
    }

    public String getTrailerLink() {
        return trailerLink;
    }

    public String getPosterLink() { return posterLink; }

}
