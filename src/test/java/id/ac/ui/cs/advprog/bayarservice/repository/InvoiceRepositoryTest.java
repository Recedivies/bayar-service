package id.ac.ui.cs.advprog.bayarservice.repository;

import id.ac.ui.cs.advprog.bayarservice.model.bill.Bill;
import id.ac.ui.cs.advprog.bayarservice.model.invoice.Invoice;
import id.ac.ui.cs.advprog.bayarservice.model.invoice.PaymentMethod;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class InvoiceRepositoryTest {

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    UUID uuid = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        Invoice invoice = Invoice.builder()
                .id(1)
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
    @After("test")
    void tearDown() {
        billRepository.deleteAll();
        invoiceRepository.deleteAll();
    }

    @Test
    void testFindById() {
        Optional<Invoice> optionalInvoice = invoiceRepository.findById(1);

        Assertions.assertTrue(optionalInvoice.isPresent());
    }

    @Test
    void testFindByIdNotFound() {
        Optional<Invoice> optionalInvoice = invoiceRepository.findById(100);

        Assertions.assertFalse(optionalInvoice.isPresent());
    }

    @Test
    void testFindAll() { // Review
       List<Invoice> optionalInvoiceList = invoiceRepository.findAll();

        Assertions.assertNotNull(optionalInvoiceList);
    }
}
