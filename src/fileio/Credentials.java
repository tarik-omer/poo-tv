package fileio;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter @Setter
public final class Credentials {
    private String name;
    private String password;
    private String accountType;
    private String country;
    private String balance;

    public Credentials() {

    }

    public Credentials(final Credentials credentials) {
        this.name = credentials.name;
        this.password = credentials.password;
        this.accountType = credentials.accountType;
        this.balance = credentials.balance;
        this.country = credentials.country;
    }

    /**
     * Reduces balance of user with given amount
     * @param cost  amount to be reduced with
     */
    public void reduceBalance(final int cost) {
        int integerBalance = Integer.parseInt(balance);
        integerBalance -= cost;
        balance = Integer.toString(integerBalance);
    }

    /**
     * increases amount of balance with given amount
     * @param gain  amount to be increased with
     */
    public void increaseBalance(final int gain) {
        int integerBalance = Integer.parseInt(balance);
        integerBalance += gain;
        balance = Integer.toString(integerBalance);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Credentials that = (Credentials) o;
        return Objects.equals(name, that.name) && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, password);
    }
}
