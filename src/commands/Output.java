package commands;

import backend.CurrentSession;
import fileio.Movie;
import fileio.User;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter @Setter
public class Output {
    private String error = null;
    private User currentUser;
    private ArrayList<Movie> currentMoviesList;

    public Output(final CurrentSession currentSession) {
        this.currentUser = new User(currentSession.getCurrentUser());
        this.currentMoviesList = Movie.getMovieListCopy(currentSession.getCurrentMoviesList());
    }
}
