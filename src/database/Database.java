package database;

import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.Error;
import fileio.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import observer_pattern.Observer;
import observer_pattern.Subject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;

@Getter @Setter
public final class Database implements Subject {
    private ArrayList<Movie> allMovies;
    private Hashtable<Credentials, User> registeredUsers;
    private final ArrayNode output;
    private Hashtable<String, ArrayList<User>> subscribedUsers;

    public Database(final ArrayList<Movie> availableMovies,
                    final ArrayList<User> registeredUsersList,
                    final ArrayNode output) {
        this.allMovies = availableMovies;
        // transform list to hashtable
        this.registeredUsers = new Hashtable<Credentials, User>();
        for (User user : registeredUsersList) {
            Credentials credentials = user.getCredentials();
            this.registeredUsers.put(credentials, user);
        }
        this.subscribedUsers = new Hashtable<>();
        this.output = output;
    }

    /**
     * Adds a user into the registered users HashTable
     * @param user  user to be registered
     */
    public void addUser(final User user) {
        this.registeredUsers.put(user.getCredentials(), user);
    }

    public void addMovie(final Movie addedMovie) {
        // cannot add same movie
        if (this.allMovies.contains(addedMovie)) {
            output.addPOJO(new Error());
            return;
        }

        // add movie
        this.allMovies.add(new Movie(addedMovie));

        // notify users
        notif(new Notification("ADD", addedMovie.getName()), addedMovie.getGenres());
    }

    public void deleteMovie(final String deletedMovie) {
        // cannot delete inexistent movie
        if (!this.allMovies.contains(new Movie(deletedMovie))) {
            output.addPOJO(new Error());
            return;
        }

        // get instance of deleted movie
        Movie movie = this.allMovies.get(this.allMovies.indexOf(new Movie(deletedMovie)));

        // get info about movie
        String movieName = movie.getName();
        ArrayList<String> movieGenres = movie.getGenres();


        // notify users
        notif(new Notification("DELETE", movieName), movieGenres);

        ArrayList<User> users = new ArrayList<>(this.registeredUsers.values());

        for (User user : users) {
            ArrayList<Movie> purchasedMovies = user.getPurchasedMovies();
            if (purchasedMovies.contains(movie)) {
                if (user.getCredentials().getAccountType().equals("premium")) {
                    user.refundFreemiumMovie();
                } else {
                    user.addTokens(Constants.MOVIE_COST);
                }
                user.removeMovie(movie);
            }
        }

        // delete movie
        this.allMovies.remove(new Movie(deletedMovie));
    }

    /**
     *  Notify users based on given properties with provided message
     * @param message       message to be provided to users
     * @param properties    details about the notification - genres
     */

    @Override
    public void notif(Notification message, ArrayList<String> properties) {
        if (this.subscribedUsers == null || this.subscribedUsers.isEmpty()) {
            return;
        }

        HashSet<User> notifiedUsers = new HashSet<>();

        for (String genre : properties) {
            // ignore unsubscribed genres
            if (!this.subscribedUsers.containsKey(genre) ||
                    this.subscribedUsers.get(genre).isEmpty()) {
                continue;
            }

            ArrayList<User> subscribedUsers = this.subscribedUsers.get(genre);
            for (User user : subscribedUsers) {
                if (!notifiedUsers.contains(user)) {
                    user.update(message);
                    notifiedUsers.add(user);
                }
            }
        }
    }

    @Override
    public void attach(Observer o, String property) {
        // add genre if needed
        if (!this.subscribedUsers.containsKey(property)) {
            this.subscribedUsers.put(property, new ArrayList<>());
        }

        // user is already subscribed
        if (this.subscribedUsers.get(property).contains((User)o)) {
            output.addPOJO(new Error());
            return;
        }
        this.subscribedUsers.get(property).add((User)o);
    }

    @Override
    public void detach(Observer o, String property) {
        if (this.subscribedUsers.isEmpty()) {
            return;
        }

        if (!this.subscribedUsers.containsKey(property)) {
            return;
        }

        if (!this.subscribedUsers.get(property).contains((User)o)) {
            output.addPOJO(new Error());
            return;
        }

        this.subscribedUsers.get(property).remove((User)o);
    }

    public void accessCommand(Action command) {
        switch (command.getFeature()) {
            case "add" -> this.addMovie(command.getAddedMovie());
            case "delete" -> this.deleteMovie(command.getDeletedMovie());
            default -> throw new IllegalStateException("Unexpected value: " + command.getFeature());
        }
    }
}
