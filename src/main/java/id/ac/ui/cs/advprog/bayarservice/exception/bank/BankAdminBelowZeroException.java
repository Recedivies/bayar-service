package id.ac.ui.cs.advprog.bayarservice.exception.bank;

public class BankAdminBelowZeroException extends RuntimeException{

    public BankAdminBelowZeroException(Integer id) {
        super(String.format("Bank with id %d has admin fee below zero", id));
    }
}
