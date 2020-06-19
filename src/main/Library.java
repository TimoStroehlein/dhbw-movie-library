package main;

import java.io.BufferedReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Library {
    // check for new entity
    private final String ENTITY = "New_Entity:\\s(.*)";
    // get string between quotes and remove first whitespace
    private final String QUOTES = "((?!\\s|,)[^\"]*[^\"]|\"\")";
    // check if the string is a number
    private final String NUMBER = "-?\\d+(\\.\\d+)?";
    // two different types of lists to improve search time in those lists
    // list for search functions to use the stream method
    private List<Actor> actors;
    private List<Movie> movies;
    private List<Director> directors;
    // hashMap for network functions to search for ids faster
    private HashMap<Integer, Actor> actorsHash;
    private HashMap<Integer, Movie> moviesHash;
    private HashMap<Integer, Director> directorsHash;

    private String filename;

    public Library(String filename) {
        this.filename = filename;
        this.actors = new ArrayList<>();
        this.actorsHash = new HashMap<>();
        this.movies = new ArrayList<>();
        this.moviesHash = new HashMap<>();
        this.directors = new ArrayList<>();
        this.directorsHash = new HashMap<>();
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    // method to load the data from the file specified by the type
    public void loadData(List<String> strings, Type type) {
        String line;
        boolean matched = false;
        try (BufferedReader bufferedReader = new BufferedReader(new java.io.FileReader(filename))) {
            // pattern to check if the line contains New_Entity
            Pattern entityPattern = Pattern.compile(ENTITY);
            // pattern to remove the quotes and the first whitespace
            Pattern quotesPattern = Pattern.compile(QUOTES);
            while ((line = bufferedReader.readLine()) != null) {
                Matcher entityMatcher = entityPattern.matcher(line);
                // check if the line matches with a New_Entity line
                if (entityMatcher.matches()) {
                    Matcher matcher = quotesPattern.matcher(entityMatcher.group());
                    int matchCounter = 0;
                    // compare the attributes with the strings check if it is the right line
                    while (matcher.find()) {
                        for (String s: strings) {
                            if (s.equals(matcher.group())) {
                                matchCounter++;
                                break;
                            }
                        }
                    }
                    matched = matchCounter == strings.size();
                    // if the line is not a New_Entity line and matched is true read the data
                } else if (matched) {
                    List<String> groups = new ArrayList<>();
                    Matcher quotesMatcher = quotesPattern.matcher(line);
                    // add values specified by the groups to the groups list
                    while (quotesMatcher.find()) {
                        groups.add(quotesMatcher.group());
                    }
                    // switch on the type to get which object should be created
                    switch (type) {
                        case ACTOR:
                            addActor(new Actor(Integer.parseInt(groups.get(0)), groups.get(1)));
                            break;
                        case MOVIE:
                            // check with regex if the values for votes and rating are actual numbers (required because regex takes "" if the value is empty)
                            if (groups.get(5).matches(NUMBER) && groups.get(6).matches(NUMBER)) {
                                addMovie(new Movie(Integer.parseInt(groups.get(0)), groups.get(1), groups.get(2), groups.get(3), groups.get(4), Integer.parseInt(groups.get(5)), Double.parseDouble(groups.get(6))));
                            } else {
                                addMovie(new Movie(Integer.parseInt(groups.get(0)), groups.get(1), groups.get(2), groups.get(3), groups.get(4)));
                            }
                            break;
                        case DIRECTOR:
                            addDirector(new Director(Integer.parseInt(groups.get(0)), groups.get(1)));
                            break;
                        case ACTOR_MOVIE:
                            moviesHash.get(Integer.parseInt(groups.get(1))).addActor(actorsHash.get(Integer.parseInt(groups.get(0))));
                            actorsHash.get(Integer.parseInt(groups.get(0))).addMovie(moviesHash.get(Integer.parseInt(groups.get(1))));
                            break;
                        case DIRECTOR_MOVIE:
                            moviesHash.get(Integer.parseInt(groups.get(1))).addDirector(directorsHash.get(Integer.parseInt(groups.get(0))));
                            break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(strings.get(0) + ", " + strings.get(1));
        }
    }

    public List<Movie> searchMovies(String searchValue) {
        return movies.stream().filter(movie -> movie.getTitle().toLowerCase().contains(searchValue.toLowerCase())).collect(Collectors.toList());
    }

    public List<Actor> searchActors(String searchValue) {
        return actors.stream().filter(actor -> actor.getName().toLowerCase().contains(searchValue.toLowerCase())).collect(Collectors.toList());
    }

    public Movie searchMovieNetwork(int searchValue) {
        Optional<Movie> movie = movies.stream().filter(m -> m.getId() == searchValue).findFirst();
        return movie.orElse(null);
    }

    public Actor searchActorNetwork(int searchValue) {
        Optional<Actor> actor = actors.stream().filter(a -> a.getId() == searchValue).findFirst();
        return actor.orElse(null);
    }

    public void printMovies(List<Movie> movies) {
        System.out.println("movies: ");
        for (Movie m: movies) {
            System.out.print("id: " + m.getId() + ", title: " + m.getTitle() + ", description: " + m.getDescription() + ", date: " + m.getDate());
            if (m.getRatingAmount() >= 0) System.out.print(", rating amount: " + m.getRatingAmount());
            if (m.getRating() >= 0.0) System.out.print(", rating: " + m.getRating());
            if (!m.getGenre().equals("")) System.out.print(", genre: " + m.getGenre());
            System.out.println();
        }
    }

    public void printActors(List<Actor> actors) {
        System.out.println("actors: ");
        for (Actor a: actors) {
            System.out.println("id: " + a.getId() + ", name: " + a.getName());
        }
    }

    public void printMovieNetwork(Movie movie) {
        System.out.print("Schauspieler: ");
        List<Actor> actors = new ArrayList<>();
        List<Movie> movies = new ArrayList<>();
        // string to remove the last comma
        String comma = "";
        // get all actors from the movie and make sure no actor is printed out twice
        for (Actor a: movie.getActors()) {
            if (actors.stream().filter(ac -> ac.getName().equals(a.getName())).findFirst().orElse(null) == null) {
                actors.add(a);
                System.out.print(comma + a.getName());
                comma = ", ";
            }
        }
        System.out.println();
        System.out.print("Filme: ");
        comma = "";
        // get all movies from the actors, make sure no movie is displayed twice and check if the movie is not the movie searched for
        for (Actor a: actors) {
            for (Movie m: a.getMovies()) {
                if (movies.stream().filter(mo -> mo.getTitle().equals(m.getTitle())).findFirst().orElse(null) == null) {
                    if (!movie.getTitle().equals(m.getTitle())) {
                        movies.add(m);
                        System.out.print(comma + m.getTitle());
                        comma = ", ";
                    }
                }
            }
        }
    }

    public void printActorNetwork(Actor actor) {
        System.out.print("Filme: ");
        List<Movie> movies = new ArrayList<>();
        List<Actor> actors = new ArrayList<>();
        // string to remove last comma
        String comma = "";
        // get all movies from the actor and make sure no movie is printed out twice
        for (Movie m: actor.getMovies()) {
            if (movies.stream().filter(mo -> mo.getTitle().equals(m.getTitle())).findFirst().orElse(null) == null) {
                movies.add(m);
                System.out.print(comma + m.getTitle());
                comma = ", ";
            }
        }
        System.out.println();
        System.out.print("Schauspieler: ");
        comma = "";
        // get all actors from the movies, make sure no actor is displayed twice and check if the actor is not the actor searched for
        for (Movie m: movies) {
            for (Actor a: m.getActors()) {
                if (actors.stream().filter(ac -> ac.getName().equals(a.getName())).findFirst().orElse(null) == null) {
                    if (!actor.getName().equals(a.getName())) {
                        actors.add(a);
                        System.out.print(comma + a.getName());
                        comma = ", ";
                    }
                }
            }
        }
    }

    private void addActor(Actor actor) {
        // check if the actor already exists by id and add it to the list/ hashMap
        if (actorsHash.get(actor.getId()) == null) {
            actors.add(actor);
            actorsHash.put(actor.getId(), actor);
        }
    }

    private void addMovie(Movie movie) {
        // check if the movie already exists by id and add it to the list/ hashMap
        if (moviesHash.get(movie.getId()) == null) {
            movies.add(movie);
            moviesHash.put(movie.getId(), movie);
        }
    }

    private void addDirector(Director director) {
        // check if the director already exists by id and add it to the list/ hashMap
        if (directorsHash.get(director.getId()) == null) {
            directors.add(director);
            directorsHash.put(director.getId(), director);
        }
    }
}
