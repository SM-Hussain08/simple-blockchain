public class Transaction {
    private String sender;
    private String receiver;
    private int amount;
    private String signature;

    public Transaction(String sender, String receiver, int amount, String signature) {
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
        this.signature = signature;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public int getAmount() {
        return amount;
    }

    public String getSignature() {
        return signature;
    }

    @Override
    public String toString() {
        return sender + " sends " + amount + " coins to " + receiver + " | Signature: " + signature;
    }
}