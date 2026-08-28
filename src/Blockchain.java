import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

public class Blockchain {
    private List<Block> chain;
    private List<Token> utxoList = new ArrayList<>();
    private List<Transaction> memPool;
    private int difficulty = 4;

    public Blockchain(PublicKey owner) {
        chain = new ArrayList<>();
        utxoList = new ArrayList<>();
        memPool = new ArrayList<>();
        addBlock(createGenesisBlock(owner));
    }

    public List<Block> getChain() {
        return chain;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    private Block createGenesisBlock(PublicKey owner) {
        Token genesisToken = new Token(100, owner);
        utxoList.add(genesisToken);
        return new Block(0, List.of(new Transaction(List.of(), List.of(genesisToken), "Genesis Block")), "0");
    }

    public Block getLatestBlock() {
        return chain.getLast();
    }

    public void processTransaction(User sender, User receiver, int amount) {
        List<Token> senderTokens = new ArrayList<>();
        int total = 0;

        // Gather enough UTXOs from sender that are NOT in memPool
        for (Token token : utxoList) {
            if (token.owner.equals(sender.getPublicKey()) && !isTokenInMemPool(token)) {
                senderTokens.add(token);
                total += token.amount;
                if (total >= amount) break;
            }
        }

        System.out.print("Transaction of " + sender.getName() + " to " + receiver.getName() + " for amount " + amount + " ");

        if (total < amount) {
            System.out.println("failed: Insufficient funds\n");
            return;
        }

        // Ensure all tokens actually belong to the sender before removing them
        for (Token token : senderTokens) {
            if (!token.owner.equals(sender.getPublicKey())) {
                System.out.println("failed: Attempting to spend someone else's tokens!\n");
                return;
            }
        }

        // Create new output tokens
        List<Token> outputTokens = new ArrayList<>();
        outputTokens.add(new Token(amount, receiver.getPublicKey()));
        if (total > amount) {
            outputTokens.add(new Token(total - amount, sender.getPublicKey())); // Change returned to sender
        }

        // Transaction signature
        String transactionDetails = amount + " tokens sent from " + sender.getPublicKey().hashCode() + " to " + receiver.getPublicKey().hashCode();
        String signature = sender.signTransaction(transactionDetails);
        Transaction transaction = new Transaction(senderTokens, outputTokens, signature);

        // Verify signature before adding to memPool
        if (!sender.verifyTransaction(transactionDetails, signature, sender.getPublicKey())) {
            System.out.println("failed: Invalid signature!\n");
            return;
        }

        // Add to memPool (but do not modify UTXOs yet)
        memPool.add(transaction);
        System.out.println("verified and added in the memPool\n");
    }

    // Helper method to check if a token is already in a pending transaction (memPool)
    private boolean isTokenInMemPool(Token token) {
        for (Transaction tx : memPool) {
            if (tx.inputs.contains(token)) {
                return true; // The token is already being used in a pending transaction
            }
        }
        return false;
    }

    public void addBlock(Block newBlock) {
        newBlock.mineBlock(difficulty);
        for (Transaction tx : newBlock.getTransactions()) {
            utxoList.removeAll(tx.inputs);  // Remove spent tokens
            utxoList.addAll(tx.outputs);    // Add new tokens
        }
        chain.add(newBlock);
    }

    public void minePendingTransactions() {
        if (memPool.isEmpty()) {
            System.out.println("No transactions to mine\n");
            return;
        }

        Block newBlock = new Block(chain.size(), new ArrayList<>(memPool), getLatestBlock().getHash());
        newBlock.mineBlock(difficulty);
        chain.add(newBlock);

        // Update the UTXO list after successfully mining the block
        for (Transaction tx : memPool) {
            utxoList.removeAll(tx.inputs);  // Remove spent UTXOs
            utxoList.addAll(tx.outputs);    // Add new UTXOs
        }

        System.out.println("Transactions successfully mined for the memPool:  " + memPool + "\n");
        memPool.clear(); // Clear memPool after mining
    }

    public boolean isChainValid() {
        for (int i = 1; i < chain.size(); i++) {
            Block currentBlock = chain.get(i);
            Block previousBlock = chain.get(i - 1);

            if (!currentBlock.getHash().equals(currentBlock.calculateHash())) return false;
            if (!currentBlock.getPreviousHash().equals(previousBlock.getHash())) return false;
        }
        return true;
    }

    public void printUTXOList() {
        System.out.println("Current UTXO List:");
        for (Token token : utxoList) {
            System.out.println(token);
        }
        System.out.println("--------------------------------");
    }

    public void printList() {
        System.out.println("""

                *****************************************************
                ***CHAIN***
                --------------------------------------------------""");

        for (Block block : chain) {
            System.out.println(block.toString());
        }

        System.out.println("*****************************************************\n");
    }

}
