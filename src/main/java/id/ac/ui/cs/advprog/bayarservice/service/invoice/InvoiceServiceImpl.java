package id.ac.ui.cs.advprog.bayarservice.service.invoice;

import id.ac.ui.cs.advprog.bayarservice.dto.Invoice.InvoiceRequest;
import id.ac.ui.cs.advprog.bayarservice.exception.InvalidPaymentMethodException;
import id.ac.ui.cs.advprog.bayarservice.exception.InvoiceDoesNotExistException;
import id.ac.ui.cs.advprog.bayarservice.model.invoice.Invoice;
import id.ac.ui.cs.advprog.bayarservice.model.invoice.PaymentMethod;
import id.ac.ui.cs.advprog.bayarservice.repository.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final java.util.Date utilDate = new java.util.Date();
    @Override
    public List<Invoice> findAll() {
        return invoiceRepository.findAll()
                .stream()
                .map(invoices -> invoices)
                .toList();
    }

    @Override
    public Invoice findById(Integer id) {
        Optional<Invoice> invoice = invoiceRepository.findById(id);
        if (invoice.isEmpty()) {
            throw new InvoiceDoesNotExistException(id);
        }
        return invoice.get();
    }

    @Override
    public Invoice create(InvoiceRequest request) {
        if (!isValidPaymentMethod(request.getPaymentMethod())) {
            throw new InvalidPaymentMethodException(request.getPaymentMethod());
        }
        return invoiceRepository.save(request.toEntity());
    }

    @Override
    public Invoice update(Integer invoiceId, InvoiceRequest request){
        if (isInvoiceDoesNotExist(invoiceId)) {
            throw new InvoiceDoesNotExistException(invoiceId);
        }
        Invoice invoice = this.invoiceRepository.findById(invoiceId).orElseThrow();
        invoice.setAdminFee(request.getAdminFee());
        invoice.setSessionId(request.getSessionId());
        invoice.setPaymentMethod(PaymentMethod.valueOf(request.getPaymentMethod()));
        invoice.setAdminFee(request.getAdminFee());
        invoice.setTotalAmount(request.getTotalAmount());
        invoice.setDiscount(request.getDiscount());
        return this.invoiceRepository.save(invoice);
    }

    @Override
    public void delete(Integer invoiceId) {
        if (isInvoiceDoesNotExist(invoiceId)) {
            throw new InvoiceDoesNotExistException(invoiceId);
        }
        invoiceRepository.deleteById(invoiceId);
    }

    private boolean isInvoiceDoesNotExist(Integer invoiceId) {
        return invoiceRepository.findById(invoiceId).isEmpty();
    }

    private boolean isValidPaymentMethod(String paymentMethod) {
        for (PaymentMethod pm : PaymentMethod.values()) {
            if (pm.name().equals(paymentMethod)) {
                return true;
            }
        }
        return false;
    }
}