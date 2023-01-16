package pages;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Objects;

@Setter @Getter
public abstract class Page {
    private String pageName;
    private ArrayList<String> childrenPages;
    private ArrayList<String> pageCommands;

    /**
     * Method that determines whether current page can switch to given nextPage
     * @param nextPage  page to switch to
     * @return          true - can change / false - can't change
     */
    public boolean canChangePage(final String nextPage) {
        return this.childrenPages.contains(nextPage);
    }

    /**
     * Method that determines whether the user can perform given action on current page
     * @param action    desired action
     * @return          true - can perform / false - can't perform action
     */
    public boolean canPerformAction(final String action) {
        return this.pageCommands.contains(action);
    }

    /**
     * A factory-like method that returns the instance of given page name
     * @param pageName      name of desired page
     * @return              instance of desired page
     */
    public static Page pseudoPageFactory(final String pageName) {
        switch (pageName) {
            case "login" -> {
                return LoginPage.getInstance();
            }
            case "register" -> {
                return RegisterPage.getInstance();
            }
            case "movies" -> {
                return MoviesPage.getInstance();
            }
            case "see details" -> {
                return SeeDetailsPage.getInstance();
            }
            case "upgrades" -> {
                return UpgradesPage.getInstance();
            }
            case "logout" -> {
                return LogoutPage.getInstance();
            }
            case "home" -> {
                return HomepageAuth.getInstance();
            }
            default -> throw new IllegalStateException("Unexpected value: " + pageName);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Page page = (Page) o;
        return Objects.equals(pageName, page.pageName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pageName);
    }
}
