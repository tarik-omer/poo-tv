package pages;

import java.util.ArrayList;

public final class SeeDetailsPage extends Page {
    private static final SeeDetailsPage INSTANCE = new SeeDetailsPage();

    private SeeDetailsPage() {
        ArrayList<String> childrenPages = new ArrayList<>();
        ArrayList<String> pageCommands = new ArrayList<>();

        childrenPages.add("homepage auth");
        childrenPages.add("movies");
        childrenPages.add("upgrades");
        childrenPages.add("logout");
        childrenPages.add("see details");

        pageCommands.add("purchase");
        pageCommands.add("watch");
        pageCommands.add("like");
        pageCommands.add("rate");

        this.setChildrenPages(childrenPages);
        this.setPageCommands(pageCommands);
    }

    public static SeeDetailsPage getInstance() {
        return INSTANCE;
    }
}
