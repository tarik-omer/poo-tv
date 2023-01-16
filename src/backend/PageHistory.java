package backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.Error;
import commands.Output;
import database.Database;
import fileio.Movie;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pages.Page;

import java.util.ArrayList;
import java.util.Stack;

/**
 * A class that contains information about the previous state of the current session
 * A memento-like class
 */
@Getter @Setter
class PrevPageDetails {
    private String pageName;
    private ArrayList<Movie> currentMoviesList;

    PrevPageDetails(final String pageName, final ArrayList<Movie> currentMoviesList) {
        this.setPageName(pageName);
        this.setCurrentMoviesList(new ArrayList<>(currentMoviesList));
    }
}

/**
 * Class that provides the abilities to store states, add them to the Stack and retrieve past
 * states from the Stack
 * A caretaker-like class
 */
@Getter @Setter @AllArgsConstructor
public class PageHistory {
    private Stack<PrevPageDetails> previousPages;
    private final ArrayNode output;
    private final Database database;
    private final CurrentSession currentSession;
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Sets the current session to its previous state
     */
    public void goBack() {
        // check for invalid cases
        // nowhere to go backwards
        if (previousPages.isEmpty()) {
            output.addPOJO(new Error());
            return;
        }

        // user not logged in
        if (currentSession.getCurrentUser() == null) {
            output.addPOJO(new Error());
            return;
        }

        // last page is login or register
        if (previousPages.peek().getPageName().equals("login")
            || previousPages.peek().getPageName().equals("register")
            || previousPages.peek().getPageName().equals("non home")) {
            output.addPOJO(new Error());
            return;
        }

        // get last page details
        PrevPageDetails prev = previousPages.pop();
        Page lastPage = Page.pseudoPageFactory(prev.getPageName());
        ArrayList<Movie> lastMovies = prev.getCurrentMoviesList();

        // set last page details
        currentSession.setCurrentPage(lastPage);
        currentSession.setCurrentMoviesList(new ArrayList<>(lastMovies));

        if (!currentSession.getCurrentPage().getPageName().equals("home")
            && !currentSession.getCurrentPage().getPageName().equals("upgrades")) {
            output.addPOJO(new Output(currentSession));
        }
    }

    /**
     * Stores the current session as a state
     */
    public void addPageToHistory() {
        PrevPageDetails prev = new PrevPageDetails(currentSession.getCurrentPage().getPageName(),
                                                    currentSession.getCurrentMoviesList());

        previousPages.push(prev);
    }

    /**
     * Clears the Stack which stores states
     */
    public void clearHistory() {
        this.setPreviousPages(new Stack<>());
    }

    /**
     * Removes the last entry in the state Stack, without modifying the current session
     */
    public void removeLastEntry() {
        this.getPreviousPages().pop();
    }
}
