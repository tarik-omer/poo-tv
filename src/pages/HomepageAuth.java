package pages;

import java.util.ArrayList;

public final class HomepageAuth extends Page {
    private static final HomepageAuth INSTANCE = new HomepageAuth();

    private HomepageAuth() {
        this.setPageName("home");

        ArrayList<String> childrenPages = new ArrayList<>();
        ArrayList<String> pageCommands = new ArrayList<>();

        childrenPages.add("movies");
        childrenPages.add("upgrades");
        childrenPages.add("logout");

        this.setChildrenPages(childrenPages);
        this.setPageCommands(pageCommands);
    }

    public static HomepageAuth getInstance() {
        return INSTANCE;
    }

}
