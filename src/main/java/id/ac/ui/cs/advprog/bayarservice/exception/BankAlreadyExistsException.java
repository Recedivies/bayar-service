package id.ac.ui.cs.advprog.bayarservice.exception;

public class BankAlreadyExistsException extends RuntimeException{
    public BankAlreadyExistsException(String name) {
        super("Bank with name " + name + " already exists");
    }
}
