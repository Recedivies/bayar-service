package id.ac.ui.cs.advprog.bayarservice.exception;

import java.util.UUID;

public class InvoiceDoesNotExistException extends RuntimeException {
    public InvoiceDoesNotExistException(Integer invoiceId) {
        super("Invoice with id " + invoiceId + " does not exist");
    }

    public InvoiceDoesNotExistException(UUID sessionId) {
        super("Invoice with id " + sessionId + " does not exist");
    }
}

