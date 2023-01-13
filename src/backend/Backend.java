package backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.Commands;
import commands.Error;
import commands.Output;
import database.Database;
import fileio.Action;
import fileio.Input;
import fileio.Movie;
import pages.Page;
import pages.HomepageNonAuth;
import pages.MoviesPage;
import pages.SeeDetailsPage;
import pages.LogoutPage;

import java.util.ArrayList;
import java.util.Stack;

public final class Backend {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ArrayNode output;
    private final Input input;
    public Backend(final ArrayNode output, final Input input) {
        this.output = output;
        this.input = input;
    }

    /**
     * Method to be called to start POO TV
     */

    public void startRunning() {
        // set current available movies
        Database database = new Database(input.getMovies(), input.getUsers());
        // get command list
        ArrayList<Action> commands = input.getActions();
        // we start logged out, on unauthenticated home page
        CurrentSession currentSession = new CurrentSession(HomepageNonAuth.getInstance(),
                                                            null);

        Commands commandController = new Commands(output, database, currentSession);

        PageHistory pageHistory = new PageHistory(new Stack<>(), output, database, currentSession);

        for (Action command : commands) {
            switch (command.getType()) {
                case "change page" -> {
                    // change page if possible
                    boolean successfulPageChange = false;

                    // user can change page
                    if (currentSession.getCurrentPage().canChangePage(command.getPage())) {
                        currentSession.setCurrentPage(Page.pseudoPageFactory(command.getPage()));
                        successfulPageChange = true;
                    }

                    // display error if page change failed
                    if (!successfulPageChange) {
                        output.addPOJO(new Error());
                        continue;
                    }

                    // user joined movies page
                    if (currentSession.getCurrentPage().equals(MoviesPage.getInstance())) {
                        // create output
                        currentSession.setCurrentMoviesList(currentSession.getAllAvailableMovies());
                        output.addPOJO(new Output(currentSession));
                        continue;
                    }

                    // user changed to 'see details' of a movie
                    if (currentSession.getCurrentPage().equals(SeeDetailsPage.getInstance())) {
                        // using modified hashCode and equals, we use contains method to check
                        // movie existence faster
                        Movie selectedMovie = new Movie();
                        selectedMovie.setName(command.getMovie());

                        if (currentSession.getCurrentMoviesList().contains(selectedMovie)) {
                            // selected movie becomes only one current
                            int idx = currentSession.getCurrentMoviesList().indexOf(selectedMovie);
                            selectedMovie = currentSession.getCurrentMoviesList().get(idx);
                            ArrayList<Movie> selectedMovieList = new ArrayList<>();
                            selectedMovieList.add(selectedMovie);
                            currentSession.setCurrentMoviesList(selectedMovieList);
                            output.addPOJO(new Output(currentSession));
                        } else {
                            // no such movie exists
                            output.addPOJO(new Error());
                            currentSession.setCurrentPage(MoviesPage.getInstance());
                        }
                        continue;
                    }

                    // add page to history
                    pageHistory.addPageToHistory();

                    // user logged out
                    if (currentSession.getCurrentPage().equals(LogoutPage.getInstance())) {
                        currentSession.createNewSession();
                        pageHistory.clearHistory();
                    }
                }
                case "on page" -> commandController.accessCommand(command);
                case "back" -> pageHistory.goBack();
                default ->
                        throw new IllegalStateException("Unexpected value: " + command.getType());
            }
        }
    }
}
