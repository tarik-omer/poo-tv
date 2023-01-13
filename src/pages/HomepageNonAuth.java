package pages;

import java.util.ArrayList;

public final class HomepageNonAuth extends Page {
    private static final HomepageNonAuth INSTANCE = new HomepageNonAuth();

    private HomepageNonAuth() {
        ArrayList<String> childrenPages = new ArrayList<>();
        ArrayList<String> pageCommands = new ArrayList<>();

        childrenPages.add("login");
        childrenPages.add("register");

        this.setChildrenPages(childrenPages);
        this.setPageCommands(pageCommands);
    }

    public static HomepageNonAuth getInstance() {
        return INSTANCE;
    }
}
