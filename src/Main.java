//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    static Blockchain blockchain;

    public static void main(String[] args) {
        User u1 = new User("Hussain");
        initializeBlockchain(u1);

        User u2 = new User("Ahmed");
        User u3 = new User("Hassan");

        doTransaction(u1, u2, 20);
        mineBlock();

        doTransaction(u1, u3, 10);
        doTransaction(u2, u3, 10);
        mineBlock();

        blockchain.printList();
    }
    
    public static void initializeBlockchain(User owner){
        blockchain = new Blockchain(owner.getPublicKey());
    }

    public static void doTransaction(User sender, User receiver, int amount){
        System.out.println("""
                ***************
                TRANSACTION:
                ------------""");

        System.out.print("Before tx, ");
        blockchain.printUTXOList();
        System.out.println();

        blockchain.processTransaction(sender, receiver, amount);

        System.out.print("After tx, ");
        blockchain.printUTXOList();

        System.out.println("***************");
        System.out.println();
    }

    public static void mineBlock(){
        System.out.println("""
                ***************
                MINING BLOCK:
                ------------""");

        blockchain.minePendingTransactions();

        System.out.print("After mining, ");
        blockchain.printUTXOList();

        System.out.println("***************");
        System.out.println();
    }
}