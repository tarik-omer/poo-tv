package commands;

import backend.CurrentSession;
import backend.PageHistory;
import com.fasterxml.jackson.databind.node.ArrayNode;
import database.Constants;
import database.Database;
import fileio.Action;
import fileio.Contains;
import fileio.Credentials;
import fileio.Filters;
import fileio.Movie;
import fileio.Sort;
import fileio.User;
import pages.HomepageNonAuth;

import java.util.ArrayList;

public final class Commands {
    private final ArrayNode output;
    private final Database database;
    private final CurrentSession currentSession;
    private final PageHistory pageHistory;

    public Commands(final ArrayNode output,
                    final Database database,
                    final CurrentSession currentSession,
                    final PageHistory pageHistory) {
        this.pageHistory = pageHistory;
        this.output = output;
        this.database = database;
        this.currentSession = currentSession;
    }

    /**
     *  A factory-like method that calls the given command
     * @param command   contains command information
     */
    public void accessCommand(final Action command) {
        switch (command.getFeature()) {
            case "login" -> this.login(command);
            case "register" -> this.register(command);
            case "search" -> this.search(command);
            case "filter" -> this.filter(command);
            case "buy tokens" -> this.buyTokens(command);
            case "buy premium account" -> this.buyPremiumAccount(command);
            case "purchase" -> this.purchase(command);
            case "watch" -> this.watch(command);
            case "like" -> this.like(command);
            case "rate" -> this.rate(command);
            case "subscribe" -> this.subscribe(command);
            default -> throw new IllegalStateException("Unexpected value: "
                                                        + command.getFeature());
        }
    }

    /**
     *  Method that logs in a user into the current session if possible
     * @param command   contains command information
     */
    public void login(final Action command) {
        // verify current page
        if (!currentSession.getCurrentPage().canPerformAction("login")) {
            output.addPOJO(new Error());
            return;
        }

        // verify credentials
        boolean successfulLogin = false;
        Credentials providedCredentials = command.getCredentials();

        if (database.getRegisteredUsers().containsKey(providedCredentials)) {
            // successful login
            User user  = database.getRegisteredUsers().get(providedCredentials);
            this.pageHistory.addPageToHistory();
            currentSession.login(user, database);
            successfulLogin = true;
        }

        // display error
        if (!successfulLogin) {
            output.addPOJO(new Error());
            currentSession.setCurrentPage(HomepageNonAuth.getInstance());
            return;
        }
        // create output
        output.addPOJO(new Output(currentSession));
    }

    /**
     * Method that registers user into the current session and database if possible
     * @param command   contains information about the command
     */
    public void register(final Action command) {
        // verify current page
        if (!currentSession.getCurrentPage().canPerformAction("register")) {
            output.addPOJO(new Error());
            return;
        }

        // verify user existence
        boolean successfulRegister = false;
        Credentials providedCredentials = command.getCredentials();

        if (!database.getRegisteredUsers().containsKey(providedCredentials)) {
            // new user - successful register
            successfulRegister = true;
            User newUser =  new User(providedCredentials);
            database.addUser(newUser);
            this.pageHistory.addPageToHistory();
            currentSession.login(newUser, database);
        }

        // display error
        if (!successfulRegister) {
            output.addPOJO(new Error());
            currentSession.setCurrentPage(HomepageNonAuth.getInstance());
            return;
        }

        // create output
        output.addPOJO(new Output(currentSession));
    }

    /**
     *  Sets current available movies to a list of movies whose name start with given prefix
     * @param command   contains information about the command
     */
    public void search(final Action command) {
        // verify current page
        if (!currentSession.getCurrentPage().canPerformAction("search")) {
            output.addPOJO(new Error());
            return;
        }

        // search for given string inside movie name
        ArrayList<Movie> searchedMovies = new ArrayList<>();

        // prefix
        String movieNamePrefix = command.getStartsWith();

        for (Movie movie : currentSession.getAllAvailableMovies()) {
            // contains given string
            if (movie.getName().startsWith(movieNamePrefix)) {
                searchedMovies.add(movie);
            }
        }

        // set current movies to result
        currentSession.setCurrentMoviesList(searchedMovies);

        // create output
        output.addPOJO(new Output(currentSession));
    }

    /**
     * Sets current available movies to a list of movies filtered and sorted through
     * provided criteria
     * @param command   contains information about command
     */
    public void filter(final Action command) {
        // verify current page
        if (!currentSession.getCurrentPage().canPerformAction("search")) {
            output.addPOJO(new Error());
            return;
        }

        // filter available movies
        Filters filters = command.getFilters();
        Contains contains = filters.getContains();
        Sort sort = filters.getSort();

        ArrayList<Movie> filteredList = new ArrayList<>();
        ArrayList<Movie> availableMovies = currentSession.getAllAvailableMovies();
        if (contains != null) {
            ArrayList<String> actors;
            ArrayList<String> genres;
            // actors - either given or not important
            if (contains.getActors() != null && !contains.getActors().isEmpty()) {
                actors = contains.getActors();
            } else {
                actors = new ArrayList<>();
            }
            // genres - either given or not important
            if (contains.getGenre() != null && !contains.getGenre().isEmpty()) {
                genres = contains.getGenre();
            } else {
                genres = new ArrayList<>();
            }
            // iterate through movies
            for (Movie movie : availableMovies) {
                // check that each movie contains filters
                if (movie.getActors().containsAll(actors)
                    && movie.getGenres().containsAll(genres)) {
                    filteredList.add(movie);
                }
            }
        } else {
            filteredList = availableMovies;
        }

        if (sort != null) {
            filteredList.sort(MovieComparatorFactory.getComparator(sort));
        }

        currentSession.setCurrentMoviesList(filteredList);

        // create output
        output.addPOJO(new Output(currentSession));
    }

    /**
     * Charges current user with an amount from personal balance, giving him
     * the same amount of tokens in his POO TV account
     * @param command   contains information about the command
     */
    public void buyTokens(final Action command) {
        // command on invalid page
        if (!currentSession.getCurrentPage().canPerformAction(command.getFeature())) {
            // generate error
            output.addPOJO(new Error());
            return;
        }

        // add tokens to account - decrease balance
        User currentUser = currentSession.getCurrentUser();
        currentUser.addTokens(command.getCount());
        currentUser.getCredentials().reduceBalance(command.getCount());
    }

    /**
     * Charges current user with an amount from owned tokens, giving him access to a premium
     * account, which receives 15 Free Premium movies
     * @param command   contains information about the command
     */
    public void buyPremiumAccount(final Action command) {
        // command on invalid page
        if (!currentSession.getCurrentPage().canPerformAction(command.getFeature())) {
            // generate error
            output.addPOJO(new Error());
            return;
        }

        User currentUser = currentSession.getCurrentUser();

        // buy account - use tokens
        if (currentUser.getTokensCount() >= Constants.PREMIUM_ACCOUNT_COST) {
            currentUser.useTokens(Constants.PREMIUM_ACCOUNT_COST);
            currentUser.getCredentials().setAccountType("premium");
        } else {
            output.addPOJO(new Error());
        }
    }

    /**
     * Consumes tokens (standard account) / free premium movies (premium account)
     * and offers access to the current user to the current movie
     * @param command   contains information about the command
     */
    public void purchase(final Action command) {
        // command invalid on current page
        if (!currentSession.getCurrentPage().canPerformAction("purchase")) {
            output.addPOJO(new Error());
            return;
        }

        if (currentSession.getCurrentMoviesList().size() != 1) {
            System.out.println("Something went wrong - See Details has more than 1 movie");
            return;
        }


        // on see details we must be able to see only 1 movie
        Movie currentMovie = currentSession.getCurrentMoviesList().get(0);
        User currentUser = currentSession.getCurrentUser();

        if (currentSession.getCurrentUser().getPurchasedMovies().contains(currentMovie)) {
            output.addPOJO(new Error());
            return;
        }

            // check premium or not account - subtract tokens or decrement freemium movies number
        if (currentUser.getCredentials().getAccountType().equals("premium")
            && currentUser.getNumFreePremiumMovies() >= 1) {
            currentUser.useFreemiumMovie();
        } else if (currentUser.getTokensCount() >= Constants.MOVIE_COST) {
            currentUser.useTokens(Constants.MOVIE_COST);
        } else {
            // not enough tokens
            output.addPOJO(new Error());
            return;
        }

        // mark movie as bought
        currentUser.getPurchasedMovies().add(currentMovie);

        // generate output
        output.addPOJO(new Output(currentSession));
    }

    /**
     * Marks the movie as watched for the current user - can be possible only if purchased
     * @param command   contains information about the command
     */
    public void watch(final Action command) {
        // command invalid on current page
        if (!currentSession.getCurrentPage().canPerformAction("watch")) {
            output.addPOJO(new Error());
            return;
        }

        if (currentSession.getCurrentMoviesList().size() != 1) {
            System.out.println("Something went wrong - See Details has more than 1 movie");
            return;
        }

        // on see details we must be able to see only 1 movie
        Movie currentMovie = currentSession.getCurrentMoviesList().get(0);
        User currentUser = currentSession.getCurrentUser();

        // if you watched the movie already, nothing happens - only output
        if (currentUser.getWatchedMovies().contains(currentMovie)) {
            output.addPOJO(new Output(currentSession));
            return;
        }

        // to watch a movie, it needs to be purchased
        if (currentUser.getPurchasedMovies().contains(currentMovie)) {
            currentUser.getWatchedMovies().add(currentMovie);
            output.addPOJO(new Output(currentSession));
        } else {
            output.addPOJO(new Error());
        }
    }

    /**
     * Changes movie's rating based on given rating by the current user - must be watched first
     * @param command   contains information about the command
     */
    public void rate(final Action command) {
        // command invalid on current page
        if (!currentSession.getCurrentPage().canPerformAction("rate")) {
            output.addPOJO(new Error());
            return;
        }

        if (currentSession.getCurrentMoviesList().size() != 1) {
            System.out.println("Something went wrong - See Details has more than 1 movie");
            return;
        }

        // on see details we must be able to see only 1 movie
        Movie currentMovie = currentSession.getCurrentMoviesList().get(0);
        User currentUser = currentSession.getCurrentUser();

        // to rate a movie, it needs to be watched first and the rating must be <= 5
        if (currentUser.getWatchedMovies().contains(currentMovie)
                && command.getRate() <= Constants.MAXIMUM_RATING
                && command.getRate() >= 0) {
            // rate movie - or replace previous rating
            currentMovie.rateMovie(command.getRate(), currentUser);

            // add the movie to rated movies only if it was not rated before
            if (!currentUser.getRatedMovies().contains(currentMovie)) {
                currentUser.getRatedMovies().add(currentMovie);
            }
            output.addPOJO(new Output(currentSession));
        } else {
            output.addPOJO(new Error());
        }
    }

    /**
     *  Increments the number of likes the movie received - must be watched first
     * @param command   contains information about the command
     */
    public void like(final Action command) {
        // command invalid on current page
        if (!currentSession.getCurrentPage().canPerformAction("like")) {
            output.addPOJO(new Error());
            return;
        }

        if (currentSession.getCurrentMoviesList().size() != 1) {
            System.out.println("Something went wrong - See Details has more than 1 movie");
            return;
        }

        // on see details we must be able to see only 1 movie
        Movie currentMovie = currentSession.getCurrentMoviesList().get(0);
        User currentUser = currentSession.getCurrentUser();

        // if you liked the movie already, nothing happens - only output
        if (currentUser.getLikedMovies().contains(currentMovie)) {
            output.addPOJO(new Output(currentSession));
            return;
        }

        // to rate a like, it needs to be watched first
        if (currentUser.getWatchedMovies().contains(currentMovie)) {
            currentMovie.likeMovie();
            currentUser.getLikedMovies().add(currentMovie);
            output.addPOJO(new Output(currentSession));
        } else {
            output.addPOJO(new Error());
        }
    }

    /**
     * Subscribes the current user to the specified genre; subscribed users are notified
     * when a movie withing the given genre is added / removed
     * @param command   information about the command - subscribed genre
     */
    public void subscribe(final Action command) {
        if (!currentSession.getCurrentPage().canPerformAction("subscribe")) {
            output.addPOJO(new Error());
            return;
        }

        if (currentSession.getCurrentMoviesList().size() != 1) {
            System.out.println("Something went wrong - See Details has more than 1 movie");
            return;
        }

        User currentUser = currentSession.getCurrentUser();

        // attach user to notified users list
        database.attach(currentUser, command.getSubscribedGenre());
    }
}
