package fileio;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Objects;

@Getter @Setter @ToString
public final class Movie {
    private String name;
    private String year;
    private int duration;
    private ArrayList<String> genres;
    private ArrayList<String> actors;
    private ArrayList<String> countriesBanned;
    private int numLikes = 0;
    private int numRatings = 0;
    private Double rating = 0.00;

    @JsonIgnore
    private ArrayList<Rating> userRatings = new ArrayList<>();

    public Movie() {

    }

    public Movie(final Movie movie) {
        this.name = movie.name;
        this.year = movie.year;
        this.duration = movie.duration;
        this.genres = new ArrayList<>(movie.genres);
        this.actors = new ArrayList<>(movie.actors);
        this.countriesBanned = new ArrayList<>(movie.countriesBanned);
        this.numLikes = movie.numLikes;
        this.rating = movie.rating;
        this.numRatings = movie.numRatings;
        // copy all ratings
        this.userRatings = new ArrayList<>();
        for (Rating userRating : movie.userRatings) {
            this.userRatings.add(new Rating(userRating));
        }
    }

    public Movie(final String movieName) {
        this.name = movieName;
    }

    /**
     * Sets rating of movie with 2 decimals places
     * @param rating    given rating
     */
    public void setRating(final Double rating) {
        BigDecimal bd = new BigDecimal(rating).setScale(2, RoundingMode.FLOOR);
        this.rating = bd.doubleValue();
    }

    /**
     * Increments the number of likes the movie has
     */
    public void likeMovie() {
        this.numLikes++;
    }

    /**
     * Calculates new rating based on given rating
     * @param newRating     given rating
     */
    public void rateMovie(final int newRating, final User currentUser) {
        if (this.rating == null) {
            this.rating = (double) 0;
        }

        // create user ratings store system if it does not exist
        if (this.userRatings == null) {
            this.userRatings = new ArrayList<>();
        }

        // create user rating instance - for storage, so you can later modify it
        Rating userRating = new Rating(currentUser.getCredentials().getName(), newRating);

        if (!this.userRatings.isEmpty() && this.userRatings.contains(userRating)) {
            Rating oldRating = this.userRatings.get(this.userRatings.indexOf(userRating));

            this.setRating((this.rating * this.numRatings
                    - oldRating.getRating() + newRating) / this.numRatings);
            oldRating.setRating(newRating);
        } else {
            this.setRating((this.rating * this.numRatings + newRating) / (this.numRatings + 1));
            this.numRatings++;
            this.userRatings.add(userRating);
        }

    }
    @Override
    public boolean equals(final Object o) {
        if (o.getClass() != this.getClass()) {
            return false;
        }
        return this.name.equals(((Movie) o).getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    /**
     * Returns a deep-copied list of Movie objects
     * @param moviesList    list to be copied
     * @return              copy list
     */
    public static ArrayList<Movie> getMovieListCopy(final ArrayList<Movie> moviesList) {
        ArrayList<Movie> moviesListCopy = new ArrayList<>();

        for (Movie movie : moviesList) {
            moviesListCopy.add(new Movie(movie));
        }

        return moviesListCopy;
    }
}
