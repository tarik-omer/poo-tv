package pages;

import java.util.ArrayList;

public final class LogoutPage extends Page {
    private static final LogoutPage INSTANCE = new LogoutPage();

    private LogoutPage() {
        this.setPageName("logout");

        ArrayList<String> childrenPages = new ArrayList<>();
        ArrayList<String> pageCommands = new ArrayList<>();

        childrenPages.add("homepage NonAuth");

        this.setChildrenPages(childrenPages);
        this.setPageCommands(pageCommands);
    }

    public static LogoutPage getInstance() {
        return INSTANCE;
    }

}
