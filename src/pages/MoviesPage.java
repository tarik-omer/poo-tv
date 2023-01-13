package pages;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
@Getter @Setter
public final class MoviesPage extends Page {
    private static final MoviesPage INSTANCE = new MoviesPage();

    private MoviesPage() {
        ArrayList<String> childrenPages = new ArrayList<>();
        ArrayList<String> pageCommands = new ArrayList<>();

        childrenPages.add("homepage Auth");
        childrenPages.add("see details");
        childrenPages.add("logout");
        childrenPages.add("movies");

        pageCommands.add("search");
        pageCommands.add("filter");

        this.setChildrenPages(childrenPages);
        this.setPageCommands(pageCommands);
    }

    public static MoviesPage getInstance() {
        return INSTANCE;
    }
}
