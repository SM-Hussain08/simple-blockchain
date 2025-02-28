import java.security.*;
import java.util.Base64;

public class User {
    private String name;
    private PublicKey publicKey;
    private PrivateKey privateKey;

    public User(String name) {
        this.name = name;
        generateKeyPair();
    }

    public String getName() {
        return name;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    private void generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            KeyPair pair = keyGen.generateKeyPair();
            this.privateKey = pair.getPrivate();
            this.publicKey = pair.getPublic();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String signTransaction(String transactionData) {
        try {
            Signature sign = Signature.getInstance("SHA256withRSA");
            sign.initSign(privateKey);
            sign.update(transactionData.getBytes());
            return Base64.getEncoder().encodeToString(sign.sign());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean verifyTransaction(String transactionData, String signature, PublicKey senderPublicKey) {
        try {
            Signature verify = Signature.getInstance("SHA256withRSA");
            verify.initVerify(senderPublicKey); // Use the sender's public key
            verify.update(transactionData.getBytes());
            return verify.verify(Base64.getDecoder().decode(signature)); // Compare with stored signature
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
