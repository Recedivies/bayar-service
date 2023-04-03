package id.ac.ui.cs.advprog.bayarservice.exceptions;

import java.util.UUID;

public class InvoiceDoesNotExistException extends RuntimeException {

    public InvoiceDoesNotExistException(UUID sessionId) {
        super("Invoice with session id " + sessionId + " does not exist");
    }


}

