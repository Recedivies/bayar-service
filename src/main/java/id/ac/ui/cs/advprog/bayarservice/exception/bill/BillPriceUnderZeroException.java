package id.ac.ui.cs.advprog.bayarservice.exception.bill;

public class BillPriceUnderZeroException extends RuntimeException{

        public BillPriceUnderZeroException(Integer id) {
            super(String.format("Bill with id %d has price under zero", id));
        }
}
