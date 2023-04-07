package id.ac.ui.cs.advprog.bayarservice.service;

import id.ac.ui.cs.advprog.bayarservice.dto.Invoice.InvoiceRequest;
import id.ac.ui.cs.advprog.bayarservice.model.invoice.Invoice;
import id.ac.ui.cs.advprog.bayarservice.model.invoice.PaymentMethod;
import id.ac.ui.cs.advprog.bayarservice.repository.InvoiceRepository;
import id.ac.ui.cs.advprog.bayarservice.service.invoice.InvoiceServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InvoiceServiceTest {

    @InjectMocks
    private InvoiceServiceImpl invoiceService;

    @Mock
    private InvoiceRepository invoiceRepository;

    UUID uuid = UUID.randomUUID();
    Invoice invoice;
    InvoiceRequest createRequest;

    @BeforeEach
    void setUp() {
        createRequest = InvoiceRequest.builder()
                .paymentMethod(String.valueOf(PaymentMethod.CASH))
                .adminFee(5000)
                .totalAmount(100000)
                .discount(5000)
                .sessionId(uuid)
                .build();

        invoice = Invoice.builder()
                .id(1)
                .paymentMethod(PaymentMethod.CASH)
                .adminFee(5000)
                .totalAmount(100000)
                .discount(5000)
                .sessionId(uuid)
                .build();
    }

    @Test
    void whenCreateInvoiceShouldReturnTheCreatedInvoice() {
        when(invoiceRepository.save(any(Invoice.class))).thenAnswer(invocation -> {
            var invoice = invocation.getArgument(0, Invoice.class);
            invoice.setId(1);
            return invoice;
        });

        Invoice result = invoiceService.create(createRequest);
        verify(invoiceRepository, atLeastOnce()).save(any(Invoice.class));
        Assertions.assertEquals(invoice, result);
    }
}

