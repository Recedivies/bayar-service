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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.annotation.Order;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class InvoiceRepositoryTest {

    @MockBean
    private BillRepository billRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    private final java.util.Date utilDate = new java.util.Date();
    UUID uuid = UUID.randomUUID();

    @BeforeEach
    void setUp() {

        Invoice invoice = Invoice.builder()
                .paymentMethod(PaymentMethod.CASH)
                .createdAt(new Date(utilDate.getTime()))
                .totalAmount(100000)
                .adminFee(5000)
                .discount(5000)
                .sessionId(uuid)
                .build();
        invoiceRepository.save(invoice);
        System.out.println(invoice);
        System.out.println(invoiceRepository.findAll());

        Bill bill = Bill.builder()
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
        invoiceRepository.deleteAll();
        billRepository.deleteAll();
    }

//    @Test
//    @Order(1)
//    void testFindById() {
//        System.out.println("This is the invoice repo " + invoiceRepository.findAll());
//        Optional<Invoice> optionalInvoice = invoiceRepository.findById(1);
//        System.out.println("Actual value: " + optionalInvoice);
//        Assertions.assertTrue(optionalInvoice.isPresent());
//    }

    @Test
    @Order(2)
    void testFindByIdNotFound() {
        Optional<Invoice> optionalInvoice = invoiceRepository.findById(100);

        Assertions.assertFalse(optionalInvoice.isPresent());
    }

    @Test
    @Order(3)
    void testFindAll() { // Review
        List<Invoice> optionalInvoiceList = invoiceRepository.findAll();

        Assertions.assertNotNull(optionalInvoiceList);
    }
}
