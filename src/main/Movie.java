package main;

import java.util.*;

public class Movie {
    private int id;
    private String title;
    private String description;
    private String date;
    // optional
    private int ratingAmount;
    private double rating;
    private String genre;

    private List<Actor> actors;
    private List<Director> directors;

    public Movie(int id, String title, String description, String genre, String date) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.genre = genre;
        this.date = date;
        this.actors = new ArrayList<>();
        this.directors = new ArrayList<>();
    }

    public Movie(int id, String title, String description, String genre, String date, int ratingAmount, double rating) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.genre = genre;
        this.date = date;
        this.ratingAmount = ratingAmount;
        this.rating = rating;
        this.actors = new ArrayList<>();
        this.directors = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getRatingAmount() {
        return ratingAmount;
    }

    public void setRatingAmount(int ratingAmount) {
        this.ratingAmount = ratingAmount;
    }

    public List<Actor> getActors() {
        return actors;
    }

    public void setActors(List<Actor> actors) {
        this.actors = actors;
    }

    public List<Director> getDirectors() {
        return directors;
    }

    public void setDirectors(List<Director> directors) {
        this.directors = directors;
    }

    public void addActor(Actor actor) {
        this.actors.add(actor);
    }

    public Actor getActor(int index) {
        return this.actors.get(index);
    }

    public void addDirector(Director director) {
        this.directors.add(director);
    }

    public Director getDirector(int index) {
        return this.directors.get(index);
    }
}
