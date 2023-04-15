package id.ac.ui.cs.advprog.bayarservice.service.payment;

import id.ac.ui.cs.advprog.bayarservice.model.invoice.PaymentMethod;

import java.util.List;

public interface Payment {
    List<String> getPaymentMethods();
}
