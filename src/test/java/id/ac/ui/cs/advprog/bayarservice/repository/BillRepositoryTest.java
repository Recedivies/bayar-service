package id.ac.ui.cs.advprog.bayarservice.repository;

import id.ac.ui.cs.advprog.bayarservice.model.bill.Bill;
import id.ac.ui.cs.advprog.bayarservice.model.invoice.Invoice;
import id.ac.ui.cs.advprog.bayarservice.model.invoice.PaymentMethod;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import java.util.UUID;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BillRepositoryTest {

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    UUID uuid = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        Invoice invoice = Invoice.builder()
                .paymentMethod(PaymentMethod.CASH)
                .adminFee(5000)
                .totalAmount(100000)
                .discount(5000)
                .sessionId(uuid)
                .build();
        invoiceRepository.save(invoice);

        Bill bill = Bill.builder()
                .id(1)
                .name("Coffee")
                .quantity(5)
                .price(10000)
                .subTotal(50000L)
                .invoice(invoice)
                .build();
        billRepository.save(bill);
    }
    @AfterEach
    void tearDown() {
        billRepository.deleteAll();
        invoiceRepository.deleteAll();
    }

    @Test
    void testFindById() {
        Optional<Bill> optionalBill = billRepository.findById(1);

        Assertions.assertTrue(optionalBill.isPresent());
    }

    @Test
    void testFindByIdNotFound() {
        Optional<Bill> optionalBill = billRepository.findById(100);

        Assertions.assertFalse(optionalBill.isPresent());
    }
}
