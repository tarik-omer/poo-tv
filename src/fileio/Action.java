package fileio;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Action {
    private String type;
    private String page;
    private String feature;
    private Credentials credentials;
    private Filters filters;
    private int count;
    private String movie;
    private String objectType;
    private String startsWith;
    private int rate;
    private String subscribedGenre;
    private Movie addedMovie;
    private String deletedMovie;
}
