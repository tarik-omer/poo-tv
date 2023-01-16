package fileio;

import com.fasterxml.jackson.annotation.JsonInclude;
import database.Constants;
import lombok.Getter;
import lombok.Setter;
import observer_pattern.Observer;

import java.awt.image.AreaAveragingScaleFilter;
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

    public void refundFreemiumMovie() {
        this.numFreePremiumMovies++;
    }

    public User(final Credentials credentials) {
        this.credentials = credentials;
    }

    @Override
    public void update(Notification message) {
        this.notifications.add(message);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(credentials, user.credentials);
    }

    @Override
    public int hashCode() {
        return Objects.hash(credentials);
    }

    public void removeMovie(Movie removedMovie) {
        this.getPurchasedMovies().remove(removedMovie);
        this.getWatchedMovies().remove(removedMovie);
        this.getRatedMovies().remove(removedMovie);
        this.getLikedMovies().remove(removedMovie);
    }
}
