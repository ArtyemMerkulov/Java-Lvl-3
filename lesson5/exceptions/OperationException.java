package homework5.exceptions;

public class OperationException extends Exception {

    public OperationException(int i) {
        super("Unknown operation " + i);
    }
}
