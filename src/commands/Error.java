package commands;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter @Setter
public final class Error {
    private String error = "Error";
    private String currentUser = null;
    private ArrayList<String> currentMoviesList = new ArrayList<>();
    public Error() {

    }
}
