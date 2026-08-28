import java.util.ArrayList;
import java.util.List;

public class Transaction {
    List<Token> inputs;
    List<Token> outputs;
    String signature;

    public Transaction(List<Token> inputs, List<Token> outputs, String signature) {
        this.inputs = inputs;
        this.outputs = outputs;
        this.signature = signature;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Transaction{\n");
        sb.append("  Inputs:\n");
        for (Token input : inputs) {
            sb.append("    - ").append(input).append("\n");
        }
        sb.append("  Outputs:\n");
        for (Token output : outputs) {
            sb.append("    - ").append(output).append("\n");
        }
        sb.append("  Signature: ").append(signature).append("\n");
        sb.append("}");
        return sb.toString();
    }
}