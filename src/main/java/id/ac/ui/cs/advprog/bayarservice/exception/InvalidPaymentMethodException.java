package id.ac.ui.cs.advprog.bayarservice.exception;

public class InvalidPaymentMethodException extends RuntimeException {
    public InvalidPaymentMethodException(String paymentMethod) {
        super("Invalid paymentMethod: " + paymentMethod);
    }
}