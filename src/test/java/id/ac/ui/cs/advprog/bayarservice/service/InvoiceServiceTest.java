package id.ac.ui.cs.advprog.bayarservice.service;

import id.ac.ui.cs.advprog.bayarservice.dto.Invoice.InvoiceRequest;
import id.ac.ui.cs.advprog.bayarservice.exception.InvoiceDoesNotExistException;
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

import java.util.Optional;
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
    Invoice newInvoice;
    InvoiceRequest createRequest;
    InvoiceRequest updateRequest;

    @BeforeEach
    void setUp() {
        createRequest = InvoiceRequest.builder()
                .paymentMethod(String.valueOf(PaymentMethod.CASH))
                .adminFee(5000)
                .totalAmount(100000)
                .discount(5000)
                .build();

        updateRequest = InvoiceRequest.builder()
                .paymentMethod(String.valueOf(PaymentMethod.CASH))
                .adminFee(2500)
                .totalAmount(200000)
                .discount(10000)
                .build();

        invoice = Invoice.builder()
                .id(1)
                .paymentMethod(PaymentMethod.CASH)
                .adminFee(5000)
                .totalAmount(100000)
                .discount(5000)
                .build();

        newInvoice = Invoice.builder()
                .id(1)
                .paymentMethod(PaymentMethod.CASH)
                .adminFee(2500)
                .totalAmount(200000)
                .discount(10000)
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

    @Test // Review
    void whenUpdateInvoiceShouldReturnTheUpdatedInvoice() {
        when(invoiceRepository.findById(any(Integer.class))).thenReturn(Optional.of(invoice));
        when(invoiceRepository.save(any(Invoice.class))).thenAnswer(invocation ->
                invocation.getArgument(0, Invoice.class));

        Invoice result = invoiceService.update(1, updateRequest);
        verify(invoiceRepository, atLeastOnce()).save(any(Invoice.class));
        Assertions.assertEquals(newInvoice, result);

    }

    @Test // Review
    void whenUpdateInvoiceAndNotFoundShouldThrowException() {
        when(invoiceRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
        Assertions.assertThrows(InvoiceDoesNotExistException.class, () -> invoiceService.findById(1));

    }

    @Test // Review
    void whenFindByIdAndFoundShouldReturnInvoice() {
        when(invoiceRepository.findById(any(Integer.class))).thenReturn(Optional.of(invoice));

        Invoice result = invoiceService.findById(1);

        verify(invoiceRepository, atLeastOnce()).findById(any(Integer.class));
        Assertions.assertEquals(invoice, result);
    }

    @Test // Review
    void whenFindByIdAndNotFoundShouldThrowException() {
        when(invoiceRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        Assertions.assertThrows(InvoiceDoesNotExistException.class, () -> invoiceService.findById(1));
    }

    @Test // Review
    void whenDeleteAndFoundByIdShouldDeleteInvoice() {
        when(invoiceRepository.findById(any(Integer.class))).thenReturn(Optional.of(invoice));

        invoiceService.delete(1);

        verify(invoiceRepository, atLeastOnce()).findById(any(Integer.class));
        verify(invoiceRepository, atLeastOnce()).deleteById(any(Integer.class));
    }

    @Test // Review
    void whenDeleteAndNotFoundByIdShouldThrowException() {
        when(invoiceRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        Assertions.assertThrows(InvoiceDoesNotExistException.class, () -> invoiceService.delete(1));
    }

}

