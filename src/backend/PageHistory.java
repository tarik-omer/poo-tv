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

@Getter @Setter
class PrevPageDetails {
    private String pageName;
    private ArrayList<Movie> currentMoviesList;

    public PrevPageDetails(String pageName, ArrayList<Movie> currentMoviesList) {
        this.setPageName(pageName);
        this.setCurrentMoviesList(new ArrayList<>(currentMoviesList));
    }
}

@Getter @Setter @AllArgsConstructor
public class PageHistory {
    private Stack<PrevPageDetails> previousPages;
    private final ArrayNode output;
    private final Database database;
    private final CurrentSession currentSession;
    private final ObjectMapper objectMapper = new ObjectMapper();

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
        currentSession.setCurrentMoviesList(lastMovies);

        if (!currentSession.getCurrentPage().getPageName().equals("home")) {
            output.addPOJO(new Output(currentSession));
        }
    }

    public void addPageToHistory() {
        PrevPageDetails prev = new PrevPageDetails(currentSession.getCurrentPage().getPageName(),
                                                    currentSession.getCurrentMoviesList());

        previousPages.push(prev);
    }

    public void clearHistory() {
        this.setPreviousPages(new Stack<>());
    }

    public void removeLastEntry() {
        this.getPreviousPages().pop();
    }
}
