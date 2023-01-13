package backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Error;
import database.Database;
import fileio.Movie;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pages.LoginPage;
import pages.MoviesPage;
import pages.Page;
import pages.RegisterPage;

import java.util.ArrayList;
import java.util.Stack;

@Getter @Setter
class PrevPageDetails {
    private Page currentPage;
    private ArrayList<Movie> currentMoviesList;

    public PrevPageDetails(Page page, ArrayList<Movie> currentMoviesList) {
        this.setCurrentPage(page);
        this.setCurrentMoviesList(Movie.getMovieListCopy(currentMoviesList));
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
        if (previousPages.isEmpty()) {
            output.addPOJO(new Error());
            return;
        }

        if (currentSession.getCurrentUser() == null) {
            output.addPOJO(new Error());
            return;
        }

        if (previousPages.peek().getCurrentPage() == LoginPage.getInstance()
            || previousPages.peek().getCurrentPage() == RegisterPage.getInstance()) {
            output.addPOJO(new Error());
            return;
        }

        // get last page details
        PrevPageDetails prev = previousPages.pop();
        Page lastPage = prev.getCurrentPage();
        ArrayList<Movie> lastMovies = Movie.getMovieListCopy(prev.getCurrentMoviesList());

        // set last page details
        currentSession.setCurrentPage(lastPage);
        currentSession.setCurrentMoviesList(lastMovies);
    }

    public void addPageToHistory() {
        PrevPageDetails prev = new PrevPageDetails(currentSession.getCurrentPage(),
                                                    currentSession.getCurrentMoviesList());

        previousPages.push(prev);
    }

    public void clearHistory() {
        this.setPreviousPages(new Stack<>());
    }

}
