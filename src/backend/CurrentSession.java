package backend;

import com.fasterxml.jackson.annotation.JsonIgnore;
import database.Database;
import fileio.Movie;
import fileio.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pages.HomepageAuth;
import pages.HomepageNonAuth;
import pages.Page;

import java.util.ArrayList;

@Getter @Setter @ToString
public final class CurrentSession {
    private Page currentPage;
    private User currentUser;
    private ArrayList<Movie> currentMoviesList;
    @JsonIgnore
    private ArrayList<Movie> allAvailableMovies;

    public CurrentSession(final Page currentPage, final User currentUser) {
        this.currentPage = currentPage;
        this.currentUser = currentUser;
        this.currentMoviesList = new ArrayList<>();
    }

    /**
     * Creates a new login session, logging out current user
     * and returning to Unauthenticated Homepage
     */
    public void createNewSession() {
        this.currentPage = HomepageNonAuth.getInstance();
        this.currentUser = null;
        this.currentMoviesList = new ArrayList<>();
        this.allAvailableMovies = null;
    }

    /**
     * Login of user - sets user's available movies (not banned), user's information and
     * moves current page to Authenticated Homepage
     * @param user      user to be logged in
     * @param database  current database - contains movies and registered users
     */
    public void login(final User user, final Database database) {
        this.currentPage = HomepageAuth.getInstance();
        this.currentUser = user;
        this.currentMoviesList = new ArrayList<>();
        this.setAvailableMovies(database);
    }

    /**
     * Populates list of available movies with movies that are not banned
     * in the current user's country
     * @param database  current database - contains movies and registered users
     */
    public void setAvailableMovies(final Database database) {
        // user is not logged in
        if (currentUser == null) {
            return;
        }

        String currentUserCountry = currentUser.getCredentials().getCountry();
        this.allAvailableMovies = new ArrayList<>();

        // get movies not banned in user's country
        for (Movie movie : database.getAllMovies()) {
            if (!movie.getCountriesBanned().contains(currentUserCountry)) {
                this.allAvailableMovies.add(movie);
            }
        }
    }
}
