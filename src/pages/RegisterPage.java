package pages;

import java.util.ArrayList;

public final class RegisterPage extends Page {
    private static final RegisterPage INSTANCE = new RegisterPage();

    private RegisterPage() {
        ArrayList<String> childrenPages = new ArrayList<>();
        ArrayList<String> pageCommands = new ArrayList<>();

        childrenPages.add("homepage auth");
        childrenPages.add("register");
        pageCommands.add("register");

        this.setChildrenPages(childrenPages);
        this.setPageCommands(pageCommands);
    }

    public static RegisterPage getInstance() {
        return INSTANCE;
    }
}
