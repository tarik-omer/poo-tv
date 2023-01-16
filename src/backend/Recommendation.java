package backend;

import fileio.Movie;
import fileio.Notification;
import fileio.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter @Setter @AllArgsConstructor
class Genre implements Comparable<Genre> {
    private String name;
    private int likes;

    @Override
    public int compareTo(Genre o) {
        if (this.likes == o.likes) {
            return this.name.compareTo(o.name);
        }
        return o.likes - this.likes;
    }

    public void addLike() {
        this.likes++;
    }
}

class LikesInc implements Comparator<Movie> {
    @Override
    public int compare(Movie o1, Movie o2) {
        return o2.getNumLikes() - o1.getNumLikes();
    }
}

public class Recommendation {
    public static void getRecommendation(CurrentSession currentSession) {
        User currentUser = currentSession.getCurrentUser();

        ArrayList<Movie> movies = currentSession.getAllAvailableMovies();
        ArrayList<Movie> userLikedMovies = currentUser.getLikedMovies();

        if (userLikedMovies.isEmpty() || movies.isEmpty()) {
            // no recommendations could be found
            Notification notification = new Notification("Recommendation",
                                                "No recommendation");
            currentUser.getNotifications().add(notification);
            return;
        }

        Hashtable<String, Genre> genreLikes = new Hashtable<>();


        // store genres in a hashmap by name
        for (Movie movie : userLikedMovies) {
            for (String genre : movie.getGenres()) {
                // init with 0 if needed
                if (!genreLikes.containsKey(genre)) {
                    genreLikes.put(genre, new Genre(genre, 0));
                }
                // add likes to the genre collection
                genreLikes.get(genre).addLike();
            }
        }
        // transfer genres to list
        ArrayList<Genre> genreLikesList = new ArrayList<>(genreLikes.values());

        // sort genres based on likes
        Collections.sort(genreLikesList);

        // sort available movies based on likes
        movies.sort(new LikesInc());

        boolean foundRecommendation = false;
        Movie foundMovie = null;

        // select final movie - intersection between the two sortings
        for (Genre genre : genreLikesList) {
            for (Movie movie : movies) {
                // found candidate
                if (movie.getGenres().contains(genre.getName()) &&
                    !currentUser.getWatchedMovies().contains(movie)) {
                    foundRecommendation = true;
                    foundMovie = movie;
                    break;
                }
            }
        }
        if (foundRecommendation) {
            Notification notification = new Notification("Recommendation",
                                                        foundMovie.getName());
            currentUser.getNotifications().add(notification);
        } else {
            Notification notification = new Notification("Recommendation",
                                                "No recommendation");
            currentUser.getNotifications().add(notification);

        }
    }
}
