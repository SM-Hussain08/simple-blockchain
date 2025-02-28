//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        System.out.println("Hello and welcome!");
        Blockchain blockchain = new Blockchain();
        User u1 = new User("Ali");
        User u2 = new User("Ahmed");
        User u3 = new User("Hassan");

        simulateTransaction(u1, u2, 10, blockchain);
        simulateTransaction(u2, u3, 5, blockchain);
        simulateTransaction(u1, u3, 20, blockchain);

        blockchain.printList();
        System.out.println("Blockchain valid: " + blockchain.isChainValid());
    }

    public static void simulateTransaction(User sender, User receiver, int amount, Blockchain blockchain) {
        String transactionData = sender.getName() + " sends " + amount + " coins to " + receiver.getName();
        String signature = sender.signTransaction(transactionData);

        Transaction transaction = new Transaction(sender.getName(), receiver.getName(), amount, signature);

        // VERIFY the transaction before adding it to the block
        if (!sender.verifyTransaction(transactionData, signature, sender.getPublicKey())) {
            System.out.println("Transaction failed: Invalid signature!");
            return;
        }

        System.out.println("Transaction verified and added to blockchain!");
        Block newBlock = new Block(blockchain.getChain().size(), transaction, blockchain.getLatestBlock().getHash());
        blockchain.addBlock(newBlock);
    }
}