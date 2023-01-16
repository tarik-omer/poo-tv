package commands;

import fileio.Movie;
import fileio.Sort;

import java.util.Comparator;

public final class MovieComparatorFactory {
    private MovieComparatorFactory() {

    }

    /**
     * A factory method that returns a new instance of a comparator, depending
     * on the desired sorting type (provided as parameter)
     * @param sort  desired sorting type
     * @return      comparator based on sorting type
     */
    public static Comparator<Movie> getComparator(final Sort sort) {
        if (sort.getDuration() == null && sort.getRating().equals("increasing")) {
            return new RatingInc();
        } else if (sort.getDuration() == null && sort.getRating().equals("decreasing")) {
            return new RatingDec();
        } else if (sort.getDuration().equals("decreasing")
            && sort.getRating().equals("decreasing")) {
            return new RatingDecDurationDec();
        } else if (sort.getDuration().equals("increasing")
                    && sort.getRating().equals("increasing")) {
            return new RatingIncDurationInc();
        } else if (sort.getDuration().equals("decreasing")
                    && sort.getRating().equals("increasing")) {
            return new RatingIncDurationDec();
        } else if (sort.getDuration().equals("increasing")
                    && sort.getRating().equals("decreasing")) {
            return new RatingDecDurationInc();
        }
        return null;
    }
}
class RatingIncDurationInc implements Comparator<Movie> {
    @Override
    public int compare(final Movie firstMovie, final Movie secondMovie) {
        if (firstMovie.getRating() == null) {
            firstMovie.setRating((double) 0);
        }

        if (secondMovie.getRating() == null) {
            secondMovie.setRating((double) 0);
        }

        int res = Integer.compare(firstMovie.getDuration(), secondMovie.getDuration());
        if (res == 0) {
            return Double.compare(firstMovie.getRating(), secondMovie.getRating());
        } else {
            return res;
        }
    }
}

class RatingDecDurationDec implements Comparator<Movie> {
    @Override
    public int compare(final Movie firstMovie, final Movie secondMovie) {
        if (firstMovie.getRating() == null) {
            firstMovie.setRating((double) 0);
        }

        if (secondMovie.getRating() == null) {
            secondMovie.setRating((double) 0);
        }

        int res = Integer.compare(firstMovie.getDuration(), secondMovie.getDuration());
        if (res == 0) {
            return -Double.compare(firstMovie.getRating(), secondMovie.getRating());
        } else {
            return -res;
        }
    }
}

class RatingDecDurationInc implements Comparator<Movie> {
    @Override
    public int compare(final Movie firstMovie, final Movie secondMovie) {
        if (firstMovie.getRating() == null) {
            firstMovie.setRating((double) 0);
        }

        if (secondMovie.getRating() == null) {
            secondMovie.setRating((double) 0);
        }

        int res = Integer.compare(firstMovie.getDuration(), secondMovie.getDuration());
        if (res == 0) {
            return -Double.compare(firstMovie.getRating(), secondMovie.getRating());
        } else {
            return res;
        }
    }
}

class RatingIncDurationDec implements Comparator<Movie> {
    @Override
    public int compare(final Movie firstMovie, final Movie secondMovie) {
        if (firstMovie.getRating() == null) {
            firstMovie.setRating((double) 0);
        }

        if (secondMovie.getRating() == null) {
            secondMovie.setRating((double) 0);
        }

        int res = Integer.compare(firstMovie.getDuration(), secondMovie.getDuration());
        if (res == 0) {
            return Double.compare(firstMovie.getRating(), secondMovie.getRating());
        } else {
            return -res;
        }
    }
}

class RatingInc implements Comparator<Movie> {
    @Override
    public int compare(final Movie firstMovie, final Movie secondMovie) {
        if (firstMovie.getRating() == null) {
            firstMovie.setRating((double) 0);
        }

        if (secondMovie.getRating() == null) {
            secondMovie.setRating((double) 0);
        }

        return Double.compare(firstMovie.getRating(), secondMovie.getRating());
    }
}

class RatingDec implements Comparator<Movie> {
    @Override
    public int compare(final Movie firstMovie, final Movie secondMovie) {
        if (firstMovie.getRating() == null) {
            firstMovie.setRating((double) 0);
        }

        if (secondMovie.getRating() == null) {
            secondMovie.setRating((double) 0);
        }

        return -Double.compare(firstMovie.getRating(), secondMovie.getRating());
    }
}