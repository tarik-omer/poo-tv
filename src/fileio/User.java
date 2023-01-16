package fileio;

import com.fasterxml.jackson.annotation.JsonInclude;
import database.Constants;
import lombok.Getter;
import lombok.Setter;
import observerpattern.Observer;

import java.util.ArrayList;
import java.util.Objects;

@Getter @Setter @JsonInclude(JsonInclude.Include.NON_NULL)
public final class User implements Observer {
    private Credentials credentials;

    private int tokensCount = 0;
    private int numFreePremiumMovies = Constants.INITIAL_FREE_MOVIES;

    private ArrayList<Movie> purchasedMovies = new ArrayList<>();
    private ArrayList<Movie> watchedMovies = new ArrayList<>();
    private ArrayList<Movie> likedMovies = new ArrayList<>();
    private ArrayList<Movie> ratedMovies = new ArrayList<>();

    private ArrayList<Notification> notifications = new ArrayList<>();

    public User() {

    }
    public User(final User user) {
        this.credentials = new Credentials(user.credentials);
        this.tokensCount = user.tokensCount;
        this.numFreePremiumMovies = user.numFreePremiumMovies;
        this.ratedMovies = Movie.getMovieListCopy(user.ratedMovies);
        this.purchasedMovies = Movie.getMovieListCopy(user.purchasedMovies);
        this.watchedMovies = Movie.getMovieListCopy(user.watchedMovies);
        this.likedMovies = Movie.getMovieListCopy(user.likedMovies);
        this.notifications = new ArrayList<>(user.notifications);
    }

    /**
     * Increases number of tokens user has by given amount
     * @param gain  tokens gained
     */
    public void addTokens(final int gain) {
        this.tokensCount += gain;
    }

    /**
     * Uses an amount of token, reducing them by a given amount
     * @param cost  tokens lost
     */
    public void useTokens(final int cost) {
        this.tokensCount -= cost;
    }

    /**
     * Uses one free premium movie received along a premium account
     */
    public void useFreemiumMovie() {
        this.numFreePremiumMovies--;
    }

    /**
     * Refund a free premium movie. Increments Freemium movies by 1
     */
    public void refundFreemiumMovie() {
        this.numFreePremiumMovies++;
    }

    public User(final Credentials credentials) {
        this.credentials = credentials;
    }

    /**
     * Method that updates notifications with given message
     * @param message   message for the user
     */
    @Override
    public void update(final Notification message) {
        this.notifications.add(message);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        User user = (User) o;
        return Objects.equals(credentials, user.credentials);
    }

    @Override
    public int hashCode() {
        return Objects.hash(credentials);
    }

    /**
     * Removes movie from User's lists - purchased, watched, liked and rated
     * To be used when removing movie from database
     * @param removedMovie  Movie instance to be removed
     */
    public void removeMovie(final Movie removedMovie) {
        this.getPurchasedMovies().remove(removedMovie);
        this.getWatchedMovies().remove(removedMovie);
        this.getRatedMovies().remove(removedMovie);
        this.getLikedMovies().remove(removedMovie);
    }
}
