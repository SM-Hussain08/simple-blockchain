import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Block {
    private int index;
    private long timestamp;
    private Transaction transactions;
    private String previousHash;
    private String hash;
    private int nonce;

    public Block(int index, Transaction transactions, String previousHash) {
        this.index = index;
        this.timestamp = new Date().getTime();
        this.transactions = transactions;
        this.previousHash = previousHash;
        this.hash = calculateHash();
    }

    public int getIndex() {
        return index;
    }

    public String getTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // Define format
        return sdf.format(new Date(timestamp));
    }

    public Transaction getTransactions() {
        return transactions;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public String getHash() {
        return hash;
    }

    public int getNonce() {
        return nonce;
    }

    public String calculateHash() {
        String dataToHash = index + timestamp + transactions.toString() + previousHash + nonce;
        return applySHA256(dataToHash);
    }

    public void mineBlock(int difficulty) {
        String target = new String(new char[difficulty]).replace('\0', '0');
        while (!hash.substring(0, difficulty).equals(target)) {
            nonce++;
            hash = calculateHash();
        }
    }

    public static String applySHA256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return "Block #" + index + "\n" +
                "Timestamp: " + getTime() + "\n" +
                "Previous Hash: " + previousHash + "\n" +
                "Hash: " + hash + "\n" +
                "Transaction: " + transactions.toString() + "\n" +
                "--------------------------------------------------";
    }

}