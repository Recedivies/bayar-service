package id.ac.ui.cs.advprog.bayarservice.service;

import id.ac.ui.cs.advprog.bayarservice.dto.discount.DiscountRequest;
import id.ac.ui.cs.advprog.bayarservice.exception.invoice.InvoiceDoesNotExistException;
import id.ac.ui.cs.advprog.bayarservice.model.discount.DiscountType;
import id.ac.ui.cs.advprog.bayarservice.model.invoice.Invoice;
import id.ac.ui.cs.advprog.bayarservice.repository.InvoiceRepository;
import id.ac.ui.cs.advprog.bayarservice.service.discount.DiscountServiceImpl;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DiscountServiceTest {
    @InjectMocks
    private DiscountServiceImpl discountService;

    @Mock
    private InvoiceRepository invoiceRepository;

    Invoice invoice;
    DiscountRequest nominalDiscountRequest;
    DiscountRequest percentageDiscountRequest;
    UUID uuid;

    @BeforeEach
    void setUp() {
        invoice = Invoice.builder()
                .totalAmount(100000L)
                .discount(5000L)
                .build();

        nominalDiscountRequest = DiscountRequest.builder()
                .discountType(String.valueOf(DiscountType.NOMINAL))
                .discount(50000L)
                .build();

        percentageDiscountRequest = DiscountRequest.builder()
            .discountType(String.valueOf(DiscountType.PERCENTAGE))
            .discount(50L)
            .build();

        uuid = UUID.randomUUID();
    }

    @Test
    void whenGiveNominalDiscountShouldUpdateInvoiceDiscount() {
        when(invoiceRepository.findBySessionId(any(UUID.class))).thenReturn(Optional.of(invoice));

        discountService.giveDiscount(uuid, nominalDiscountRequest);
        verify(invoiceRepository, atLeastOnce()).save(any(Invoice.class));
    }

    @Test
    void whenGiveNominalDiscountAndDiscountGreaterThanInvoiceTotalAmount() {
        when(invoiceRepository.findBySessionId(any(UUID.class))).thenReturn(Optional.of(invoice));

        when(invoiceRepository.save(any(Invoice.class))).thenAnswer(invocation -> {
            var invoice = invocation.getArgument(0, Invoice.class);
            invoice.setTotalAmount(0L);
            return invoice;
        });

        discountService.giveDiscount(uuid, nominalDiscountRequest);
        verify(invoiceRepository, atLeastOnce()).save(any(Invoice.class));
        Assertions.assertEquals(0L, invoice.getTotalAmount());
    }

    @Test
    void whenGiveNominalDiscountAndInvoiceNotFoundThrowException() {
        when(invoiceRepository.findBySessionId(any(UUID.class))).thenReturn(Optional.empty());
        Assertions.assertThrows(InvoiceDoesNotExistException.class,
                () -> discountService.giveDiscount(uuid, nominalDiscountRequest));
    }

    @Test
    void whenGivePercentageDiscountShouldUpdateInvoiceDiscount() {
        when(invoiceRepository.findBySessionId(any(UUID.class))).thenReturn(Optional.of(invoice));

        discountService.giveDiscount(uuid, percentageDiscountRequest);
        verify(invoiceRepository, atLeastOnce()).save(any(Invoice.class));
    }

    @Test
    void whenGivePercentageDiscountAndDiscountGreaterThanInvoiceTotalAmount() {
        when(invoiceRepository.findBySessionId(any(UUID.class))).thenReturn(Optional.of(invoice));

        when(invoiceRepository.save(any(Invoice.class))).thenAnswer(invocation -> {
            var invoice = invocation.getArgument(0, Invoice.class);
            invoice.setTotalAmount(0L);
            return invoice;
        });

        discountService.giveDiscount(uuid, percentageDiscountRequest);
        verify(invoiceRepository, atLeastOnce()).save(any(Invoice.class));
        Assertions.assertEquals(0L, invoice.getTotalAmount());
    }

    @Test
    void whenGivePercentageDiscountAndInvoiceNotFoundThrowException() {
        when(invoiceRepository.findBySessionId(any(UUID.class))).thenReturn(Optional.empty());
        Assertions.assertThrows(InvoiceDoesNotExistException.class,
                () -> discountService.giveDiscount(uuid, percentageDiscountRequest));
    }

}
