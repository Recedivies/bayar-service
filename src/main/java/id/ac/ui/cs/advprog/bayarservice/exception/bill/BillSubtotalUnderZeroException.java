package id.ac.ui.cs.advprog.bayarservice.exception.bill;

public class BillSubtotalUnderZeroException extends RuntimeException{

        public BillSubtotalUnderZeroException(Long id) {
            super(String.format("Bill with id %d has subtotal under zero", id));
        }
}
