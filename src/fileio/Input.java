package fileio;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter @Setter
public class Input {
    private ArrayList<User> users;
    private ArrayList<Movie> movies;
    private ArrayList<Action> actions;
}
