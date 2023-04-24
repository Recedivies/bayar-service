package id.ac.ui.cs.advprog.bayarservice.service;

import id.ac.ui.cs.advprog.bayarservice.core.PaymentReceiver;
import id.ac.ui.cs.advprog.bayarservice.dto.payment.PaymentRequest;
import id.ac.ui.cs.advprog.bayarservice.exception.InvoiceDoesNotExistException;
import id.ac.ui.cs.advprog.bayarservice.model.invoice.Invoice;
import id.ac.ui.cs.advprog.bayarservice.model.invoice.PaymentMethod;
import id.ac.ui.cs.advprog.bayarservice.model.payment.PaymentHistory;
import id.ac.ui.cs.advprog.bayarservice.repository.InvoiceRepository;
import id.ac.ui.cs.advprog.bayarservice.repository.PaymentRepository;
import id.ac.ui.cs.advprog.bayarservice.service.payment.PaymentServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {
    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Mock
    private InvoiceRepository invoiceRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PaymentReceiver paymentReceiver;

    PaymentRequest createRequest;
    PaymentHistory paymentHistory;
    Invoice invoice;
    UUID uuid = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        invoice = Invoice.builder()
                .paymentMethod(PaymentMethod.CASH)
                .totalAmount(100000L)
                .adminFee(5000)
                .discount(5000L)
                .build();

        createRequest = PaymentRequest.builder()
                .totalAmount(100000L)
                .sessionId(uuid)
                .paymentMethod(String.valueOf(PaymentMethod.CASH))
                .build();

        paymentHistory = PaymentHistory.builder()
                .id(1)
                .totalAmount(100000L)
                .createdAt(Date.valueOf(LocalDate.now()))
                .sessionId(uuid)
                .invoice(invoice)
                .build();
    }

    @Test
    void whenGetPaymentMethodShouldReturnList() {
        List<String> result = paymentService.getPaymentMethods();
        List<String> paymentMethods = Stream.of(PaymentMethod.values())
                .map(PaymentMethod::name)
                .collect(Collectors.toList());

        Assertions.assertEquals(result, paymentMethods);
    }

    @Test
    void whenCreatePaymentShouldReturnTheCreatedPaymentHistory() {
        when(invoiceRepository.findById(any())).thenReturn(Optional.of(invoice));
        when(paymentRepository.save(any(PaymentHistory.class))).thenAnswer(invocation -> {
            var pH = invocation.getArgument(0, PaymentHistory.class);
            pH.setId(1);
            pH.setTotalAmount(paymentHistory.getTotalAmount());
            pH.setCreatedAt(paymentHistory.getCreatedAt());
            return pH;
        });

        PaymentHistory result = paymentService.create(invoice.getId(), createRequest);

        verify(paymentRepository, atLeastOnce()).save(any(PaymentHistory.class));
        Assertions.assertEquals(paymentHistory, result);
    }

    @Test
    void whenCreatePaymentShouldReturn404InvoiceNotFound() {
        when(invoiceRepository.findById(any())).thenReturn(Optional.empty());

        Assertions.assertThrows(InvoiceDoesNotExistException.class,
                () -> paymentService.create(0, createRequest));
    }

    @Test
    void whenGetPaymentLogShouldReturnListOfAllPaymentLog() {
        when(paymentRepository.findAll()).thenReturn(List.of(paymentHistory));

        List<PaymentHistory> result = paymentService.getPaymentLog();

        Assertions.assertEquals(List.of(paymentHistory), result);
    }

    @Test
    void whenGetPaymentLogByYearShouldReturnListOfPaymentLogByYear() {
        when(paymentRepository.findAll()).thenReturn(List.of(paymentHistory));

        int year = LocalDate.now().getYear();

        List<PaymentHistory> result = paymentService.getPaymentLogByYear(year);

        Assertions.assertEquals(List.of(paymentHistory), result);
    }

    @Test
    void whenGetPaymentLogByYearNotFoundShouldReturnEmptyList() {
        when(paymentRepository.findAll()).thenReturn(List.of(paymentHistory));

        List<PaymentHistory> result = paymentService.getPaymentLogByYear(2021);

        Assertions.assertEquals(List.of(), result);
    }

    @Test
    void whenGetPaymentLogByMonthShouldReturnListOfPaymentLogByMonth() {
        when(paymentRepository.findAll()).thenReturn(List.of(paymentHistory));

        int year = LocalDate.now().getYear();
        int month = LocalDate.now().getMonthValue();

        List<PaymentHistory> result = paymentService.getPaymentLogByYearAndMonth(year, month);

        Assertions.assertEquals(List.of(paymentHistory), result);
    }

    @Test
    void whenGetPaymentLogByMonthNotFoundShouldReturnEmptyList() {
        when(paymentRepository.findAll()).thenReturn(List.of(paymentHistory));

        List<PaymentHistory> result = paymentService.getPaymentLogByYearAndMonth(2020, 2);

        Assertions.assertEquals(List.of(), result);
    }

    @Test
    void whenGetPaymentLogByWeekAndYearShouldReturnListOfPaymentLogByWeekAndYear() {
        when(paymentRepository.findAll()).thenReturn(List.of(paymentHistory));

        int year = LocalDate.now().getYear();
        int week = LocalDate.now().get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear());

        List<PaymentHistory> result = paymentService.getPaymentLogByWeekAndYear(year, week);

        Assertions.assertEquals(List.of(paymentHistory), result);
    }

    @Test
    void whenGetPaymentLogByWeekAndYearNotFoundShouldReturnEmptyList() {
        when(paymentRepository.findAll()).thenReturn(List.of(paymentHistory));

        List<PaymentHistory> result = paymentService.getPaymentLogByWeekAndYear(2020, 2);

        Assertions.assertEquals(List.of(), result);
    }
}
