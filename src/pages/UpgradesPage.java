package pages;

import java.util.ArrayList;

public final class UpgradesPage extends Page {
    private static final UpgradesPage INSTANCE = new UpgradesPage();

    private UpgradesPage() {
        this.setPageName("upgrades");

        ArrayList<String> childrenPages = new ArrayList<>();
        ArrayList<String> pageCommands = new ArrayList<>();

        childrenPages.add("homepage Auth");
        childrenPages.add("movies");
        childrenPages.add("logout");
        childrenPages.add("upgrades");

        pageCommands.add("buy tokens");
        pageCommands.add("buy premium account");

        this.setChildrenPages(childrenPages);
        this.setPageCommands(pageCommands);
    }

    public static UpgradesPage getInstance() {
        return INSTANCE;
    }
}
