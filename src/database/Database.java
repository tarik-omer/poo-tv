package database;

import fileio.Credentials;
import fileio.Movie;
import fileio.User;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Hashtable;

@Getter @Setter
public final class Database {
    private ArrayList<Movie> allMovies;
    private Hashtable<Credentials, User> registeredUsers;

    public Database(final ArrayList<Movie> availableMovies,
                    final ArrayList<User> registeredUsersList) {
        this.allMovies = availableMovies;
        // transform list to hashtable
        this.registeredUsers = new Hashtable<Credentials, User>();
        for (User user : registeredUsersList) {
            Credentials credentials = user.getCredentials();
            this.registeredUsers.put(credentials, user);
        }
    }

    /**
     * Adds a user into the registered users HashTable
     * @param user  user to be registered
     */
    public void addUser(final User user) {
        this.registeredUsers.put(user.getCredentials(), user);
    }
}
