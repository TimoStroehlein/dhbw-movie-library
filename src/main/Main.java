package main;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    private static String filename = "./src/movielibrary.db";
    private static String argument;
    private static String value;

    private static final String ACTOR_ID = "actor_id";
    private static final String ACTOR_NAME = "actor_name";

    private static final String DIRECTOR_ID = "director_id";
    private static final String DIRECTOR_NAME = "director_name";

    private static final String MOVIE_ID = "movie_id";
    private static final String MOVIE_TITLE = "movie_title";
    private static final String MOVIE_PLOT = "movie_plot";
    private static final String MOVIE_GENRE = "genre_name";
    private static final String MOVIE_RELEASED = "movie_released";
    private static final String MOVIE_IMDBVOTES = "movie_imdbVotes";
    private static final String MOVIE_IMDBRATING = "movie_imdbRating";

    public static final List<String> ACTOR_STRINGS = Arrays.asList(ACTOR_ID, ACTOR_NAME);

    public static final List<String> MOVIE_STRINGS = Arrays.asList(MOVIE_ID, MOVIE_TITLE, MOVIE_PLOT,
            MOVIE_GENRE, MOVIE_RELEASED, MOVIE_IMDBVOTES, MOVIE_IMDBRATING);

    public static final List<String> DIRECTOR_STRINGS = Arrays.asList(DIRECTOR_ID, DIRECTOR_NAME);

    public static final List<String> ACTOR_MOVIE_STRINGS = Arrays.asList(ACTOR_ID, MOVIE_ID);
    public static final List<String> DIRECTOR_MOVIE_STRINGS = Arrays.asList(DIRECTOR_ID, MOVIE_ID);

    public static void main(String[] args) {
        Library library = new Library(filename);
        library.loadData(ACTOR_STRINGS, Type.ACTOR);
        library.loadData(MOVIE_STRINGS, Type.MOVIE);
        library.loadData(DIRECTOR_STRINGS, Type.DIRECTOR);
        library.loadData(ACTOR_MOVIE_STRINGS, Type.ACTOR_MOVIE);
        library.loadData(DIRECTOR_MOVIE_STRINGS, Type.DIRECTOR_MOVIE);

        Pattern pattern = Pattern.compile("(?:--)(.*)(?:=)(.*)");
        Matcher matcher = pattern.matcher(args[0]);
        if (matcher.matches()) {
            argument = matcher.group(1);
            value = matcher.group(2);
        }
        switch (argument) {
            case "filmsuche":
                List<Movie> movies = library.searchMovies(value);
                library.printMovies(movies);
                break;
            case "schauspielersuche":
                List<Actor> actors = library.searchActors(value);
                library.printActors(actors);
                break;
            case "filmnetzwerk":
                Movie movieNetwork = library.searchMovieNetwork(Integer.parseInt(value));
                library.printMovieNetwork(movieNetwork);
                break;
            case "schauspielernetzwerk":
                Actor actorNetwork = library.searchActorNetwork(Integer.parseInt(value));
                library.printActorNetwork(actorNetwork);
                break;
        }
    }
}
