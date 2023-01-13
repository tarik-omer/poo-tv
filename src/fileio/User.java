package fileio;

import com.fasterxml.jackson.annotation.JsonInclude;
import database.Constants;
import lombok.Getter;
import lombok.Setter;

import java.awt.image.AreaAveragingScaleFilter;
import java.util.ArrayList;

@Getter @Setter @JsonInclude(JsonInclude.Include.NON_NULL)
public final class User {
    private Credentials credentials;

    private int tokensCount = 0;
    private int numFreePremiumMovies = Constants.INITIAL_FREE_MOVIES;

    private ArrayList<Movie> purchasedMovies = new ArrayList<>();
    private ArrayList<Movie> watchedMovies = new ArrayList<>();
    private ArrayList<Movie> likedMovies = new ArrayList<>();
    private ArrayList<Movie> ratedMovies = new ArrayList<>();

    private ArrayList<String> notifications = new ArrayList<>();

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

    public User(final Credentials credentials) {
        this.credentials = credentials;
    }
}
