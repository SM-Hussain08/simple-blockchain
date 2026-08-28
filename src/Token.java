import java.security.PublicKey;

public class Token {
    int amount;
    PublicKey owner;

    public Token(int amount, PublicKey owner) {
        this.amount = amount;
        this.owner = owner;
    }

    @Override
    public String toString() {
        return amount + " tokens -> " + owner.hashCode();
    }
}
