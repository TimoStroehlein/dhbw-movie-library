package main;

import java.util.ArrayList;
import java.util.List;

public class Actor extends Cast {
    private List<Movie> movies;

    public Actor(int id, String name) {
        super(id, name);
        this.movies = new ArrayList<>();
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    public void addMovie(Movie movie) {
        this.movies.add(movie);
    }
}
