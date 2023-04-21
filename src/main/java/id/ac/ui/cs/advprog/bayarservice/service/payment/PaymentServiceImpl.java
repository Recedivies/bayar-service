package id.ac.ui.cs.advprog.bayarservice.service.payment;

import id.ac.ui.cs.advprog.bayarservice.core.*;
import id.ac.ui.cs.advprog.bayarservice.dto.payment.PaymentRequest;
import id.ac.ui.cs.advprog.bayarservice.exception.InvoiceDoesNotExistException;
import id.ac.ui.cs.advprog.bayarservice.model.invoice.Invoice;
import id.ac.ui.cs.advprog.bayarservice.model.invoice.PaymentMethod;
import id.ac.ui.cs.advprog.bayarservice.model.invoice.PaymentStatus;
import id.ac.ui.cs.advprog.bayarservice.model.payment.PaymentHistory;
import id.ac.ui.cs.advprog.bayarservice.repository.InvoiceRepository;
import id.ac.ui.cs.advprog.bayarservice.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PaymentServiceImpl implements Payment {

    private final PaymentRepository paymentRepository;

    private final InvoiceRepository invoiceRepository;

    private final PaymentReceiver paymentReceiver;

    @Autowired
    public PaymentServiceImpl(
                              PaymentRepository paymentRepository,
                              InvoiceRepository invoiceRepository,
                              PaymentReceiver paymentReceiver
    ) {
        this.paymentRepository = paymentRepository;
        this.invoiceRepository = invoiceRepository;
        this.paymentReceiver = paymentReceiver;
    }

    public List<String> getPaymentMethods() {
        return Stream.of(PaymentMethod.values())
                                            .map(PaymentMethod::name)
                                            .collect(Collectors.toList());
    }

    @Override
    public PaymentHistory create(Integer invoiceId, PaymentRequest request) {
        Invoice invoice = this.invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new InvoiceDoesNotExistException(invoiceId));

        long totalAmount = paymentReceiver.receive(request);

        invoice.setPaymentStatus(PaymentStatus.PAID);
        invoice.setPaymentMethod(PaymentMethod.valueOf(request.getPaymentMethod()));

        PaymentHistory paymentHistory = PaymentHistory.builder()
                .sessionId(request.getSessionId())
                .totalAmount(totalAmount)
                .invoice(invoice)
                .build();
        return this.paymentRepository.save(paymentHistory);
    }
}
