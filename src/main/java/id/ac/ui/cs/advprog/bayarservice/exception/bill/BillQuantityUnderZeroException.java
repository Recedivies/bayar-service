package id.ac.ui.cs.advprog.bayarservice.exception.bill;

public class BillQuantityUnderZeroException extends RuntimeException{

    public BillQuantityUnderZeroException(Integer id) {
        super(String.format("Bill with id %d has quantity under zero", id));
    }
}
