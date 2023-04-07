package id.ac.ui.cs.advprog.bayarservice.exception;

import java.util.UUID;

public class InvoiceDoesNotExistException extends RuntimeException {

    public InvoiceDoesNotExistException(UUID sessionId) {
        super("Invoice with session id " + sessionId + " does not exist");
    }
}

