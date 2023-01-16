package fileio;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter @Setter @AllArgsConstructor
public class Rating {
    private String userName;
    private int rating;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rating rating = (Rating) o;
        return Objects.equals(this.userName, rating.userName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.userName);
    }

    public Rating(final Rating userRating) {
        this.rating = userRating.rating;
        this.userName = userRating.getUserName();
    }
}
