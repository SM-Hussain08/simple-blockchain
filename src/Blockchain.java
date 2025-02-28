import java.util.ArrayList;
import java.util.List;

public class Blockchain {
    private List<Block> chain;
    private int difficulty = 4;

    public Blockchain() {
        chain = new ArrayList<>();
        chain.add(createGenesisBlock());
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

    private Block createGenesisBlock() {
        return new Block(0, new Transaction("Genesis", "None", 0, ""), "0");
    }

    public Block getLatestBlock() {
        return chain.get(chain.size() - 1);
    }

    public void addBlock(Block newBlock) {
        newBlock.mineBlock(difficulty);
        chain.add(newBlock);
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

    public void printList() {
        System.out.println("""

                *****************************************************
                ***CHAIN***
                --------------------------------------------------""");

        for (Block block : chain) {
            System.out.println(block.toString());
        }

        System.out.println("*****************************************************" + "\n");
    }

}
