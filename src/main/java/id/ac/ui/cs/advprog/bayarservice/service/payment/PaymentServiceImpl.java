package id.ac.ui.cs.advprog.bayarservice.service.payment;

import id.ac.ui.cs.advprog.bayarservice.core.*;
import id.ac.ui.cs.advprog.bayarservice.dto.payment.PaymentRequest;
import id.ac.ui.cs.advprog.bayarservice.exception.invoice.InvoiceAlreadyPaidException;
import id.ac.ui.cs.advprog.bayarservice.exception.invoice.InvoiceDoesNotExistException;
import id.ac.ui.cs.advprog.bayarservice.model.invoice.Invoice;
import id.ac.ui.cs.advprog.bayarservice.model.invoice.PaymentMethod;
import id.ac.ui.cs.advprog.bayarservice.model.invoice.PaymentStatus;
import id.ac.ui.cs.advprog.bayarservice.model.payment.PaymentLog;
import id.ac.ui.cs.advprog.bayarservice.repository.InvoiceRepository;
import id.ac.ui.cs.advprog.bayarservice.repository.PaymentRepository;
import id.ac.ui.cs.advprog.bayarservice.rest.WarnetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;

    private final InvoiceRepository invoiceRepository;

    private final PaymentReceiver paymentReceiver;

    private final WarnetService warnetService;

    public List<String> getPaymentMethods() {
        return Stream.of(PaymentMethod.values())
                                            .map(PaymentMethod::name)
                                            .toList();
    }

    @Override
    public PaymentLog create(Integer invoiceId, PaymentRequest request) {
        Invoice invoice = this.invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new InvoiceDoesNotExistException(invoiceId));
        if (invoice.getPaymentStatus().equals(PaymentStatus.PAID)) {
            throw new InvoiceAlreadyPaidException(request.getSessionId());
        }

        var session = this.warnetService.getSessionViaAPI(request.getSessionId());
        var noPC = session.getPc().getNoPC();
        long totalAmount = paymentReceiver.receive(request);
        invoice.setPaymentStatus(PaymentStatus.PAID);
        invoice.setPaymentMethod(PaymentMethod.valueOf(request.getPaymentMethod()));

        PaymentLog paymentLog = PaymentLog.builder()
                .sessionId(request.getSessionId())
                .noPC(noPC)
                .totalAmount(totalAmount)
                .invoice(invoice)
                .build();
        return this.paymentRepository.save(paymentLog);
    }

    @Override
    public List<PaymentLog> getPaymentLog() {
        return this.paymentRepository.findAll();
    }

    @Override
    public List<PaymentLog> getPaymentLogByYearAndMonth(int year, int month) {
        List<PaymentLog> paymentHistories = this.paymentRepository.findAll();
        return paymentHistories.stream()
                .filter(paymentHistory -> paymentHistory.getCreatedAt().toLocalDate().getYear() == year &&
                        paymentHistory.getCreatedAt().toLocalDate().getMonthValue() == month)
                .toList();
    }

    @Override
    public List<PaymentLog> getPaymentLogByYear(int year) {
        List<PaymentLog> paymentHistories = this.paymentRepository.findAll();
        return paymentHistories.stream()
                .filter(paymentHistory -> paymentHistory.getCreatedAt().toLocalDate().getYear() == year)
                .toList();
    }

    @Override
    public List<PaymentLog> getPaymentLogByWeekAndYear(int year, int week) {
        List<PaymentLog> paymentHistories = this.paymentRepository.findAll();
        return paymentHistories.stream()
                .filter(paymentHistory -> paymentHistory.getCreatedAt().toLocalDate().getYear() == year &&
                        paymentHistory.getCreatedAt().toLocalDate().get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear()) == week)
                .toList();
    }
}
