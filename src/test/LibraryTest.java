package test;

import main.*;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;

public class LibraryTest {
    private static Library library;

    @BeforeClass
    public static void create() {
        // always load data before tests
        library = new Library("./src/movielibrary.db");
        library.loadData(Main.ACTOR_STRINGS, Type.ACTOR);
        library.loadData(Main.MOVIE_STRINGS, Type.MOVIE);
        library.loadData(Main.DIRECTOR_STRINGS, Type.DIRECTOR);
        library.loadData(Main.ACTOR_MOVIE_STRINGS, Type.ACTOR_MOVIE);
        library.loadData(Main.DIRECTOR_MOVIE_STRINGS, Type.DIRECTOR_MOVIE);
        System.out.println("Test started.");
    }

    @Test
    public void searchMovies() {
        // expected movies
        List<Movie> expected = Arrays.asList(
                new Movie(4899, "Matrix Revolutions, The", "The human city of Zion defends itself against the massive invasion of the machines as Neo fights to end the war at another front while also opposing the rogue Agent Smith.", "Adventure", "2003-11-05", 354082, 6.7),
                new Movie(4622, "Matrix Reloaded, The", "Neo and the rebel leaders estimate that they have 72 hours until 250,000 probes discover Zion and destroy it and its inhabitants. During this, Neo must decide how he can save Trinity from a dark fate in his dreams.", "Adventure", "2003-05-15", 409410, 7.2),
                new Movie(5941, "Animatrix, The", "The Animatrix is a collection of several animated short films, detailing the backstory of the Matrix universe, and the original war between man and machines which led to the creation of the Matrix.", "Animation", "2003-06-03", 59863, 7.4),
                new Movie(2081, "Matrix, The", "A computer hacker learns from mysterious rebels about the true nature of his reality and his role in the war against its controllers.", "Action", "1999-03-31", 1163136, 8.7)
        );
        // actual movies
        List<Movie> actual = library.searchMovies("Matrix");
        // check if the expected and actual movies are the same
        for (Movie a: actual) {
            for (Movie b: expected) {
                if (a.getId() == b.getId()) {
                    Assert.assertThat(a.getTitle(), is(b.getTitle()));
                    Assert.assertThat(a.getDescription(), is(b.getDescription()));
                    Assert.assertThat(a.getGenre(), is(b.getGenre()));
                    Assert.assertThat(a.getDate(), is(b.getDate()));
                    Assert.assertThat(a.getRatingAmount(), is(b.getRatingAmount()));
                    Assert.assertThat(a.getRating(), is(b.getRating()));
                    break;
                }
            }
        }
    }

    @Test
    public void searchActors() {
        // expected actors
        List<Actor> expected = Arrays.asList(
                new Actor(10637, "Vernon Greeves"),
                new Actor(11064, "George Reeves"),
                new Actor(15578, "Keanu Reeves"),
                new Actor(16074, "Dale Reeves"),
                new Actor(16315, "Keanu Reeves"),
                new Actor(17134, "Perrey Reeves"),
                new Actor(18302, "Saskia Reeves")
        );
        // actual actors
        List<Actor> actual = library.searchActors("Reeves");
        // check if the expected and actual actors are the same
        for (Actor a: actual) {
            for (Actor b: expected) {
                if (a.getId() == b.getId()) {
                    Assert.assertThat(a.getName(), is(b.getName()));
                    break;
                }
            }
        }
    }

    @Test
    public void searchMovieNetwork() {
        // actual movies
        Movie actual = library.searchMovieNetwork(2081);
        // assert that the actual amount of actors equals 4
        Assert.assertThat(actual.getActors().size(), is(4));

        List<Movie> movies = new ArrayList<>();
        List<Movie> actualMovies = new ArrayList<>();

        // get all movies from the actors
        for (Actor a: actual.getActors()) {
            movies.addAll(a.getMovies());
        }
        // filter that every movie only occurs once and put it in the actualMovies list
        for (Movie m: movies) {
            if (actualMovies.stream().filter(mo -> mo.getId() == m.getId()).findFirst().orElse(null) == null) {
                actualMovies.add(m);
            }
        }
        // assert that the actual amount of movies equals 40
        Assert.assertThat(actualMovies.size(), is(40));
    }

    @Test
    public void searchActorNetwork() {
        // actual actors
        Actor actual = library.searchActorNetwork(19786);
        // assert that the actual amount of movies equals 11
        Assert.assertThat(actual.getMovies().size(), is(11));

        List<Actor> actors = new ArrayList<>();
        List<Actor> actualActors = new ArrayList<>();

        // get all actors from the movies
        for (Movie m: actual.getMovies()) {
            actors.addAll(m.getActors());
        }
        // filter that every actor only occurs once and put it in the actualActors list
        for (Actor a: actors) {
            if (actualActors.stream().filter(ac -> ac.getId() == a.getId()).findFirst().orElse(null) == null) {
                actualActors.add(a);
            }
        }
        // assert that the actual amount of actors equals 29
        Assert.assertThat(actualActors.size(), is(29));
    }

    @AfterClass
    public static void delete() {
        System.out.println("Test finished.");
    }
}