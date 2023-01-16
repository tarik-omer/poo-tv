package pages;

import java.util.ArrayList;

public final class LoginPage extends Page {
    private static final LoginPage INSTANCE = new LoginPage();

    private LoginPage() {
        this.setPageName("login");


        ArrayList<String> childrenPages = new ArrayList<>();
        ArrayList<String> pageCommands = new ArrayList<>();

        childrenPages.add("homepage auth");
        childrenPages.add("login");
        pageCommands.add("login");

        this.setChildrenPages(childrenPages);
        this.setPageCommands(pageCommands);
    }

    public static LoginPage getInstance() {
        return INSTANCE;
    }
}
