package id.ac.ui.cs.advprog.bayarservice.exception;

public class InvoiceDoesNotExistException extends RuntimeException {

    public InvoiceDoesNotExistException(Integer invoiceId) {
        super("Invoice with id " + invoiceId + " does not exist");
    }
}

