package id.ac.ui.cs.advprog.bayarservice.service.discount;

import id.ac.ui.cs.advprog.bayarservice.dto.Discount.DiscountRequest;
import id.ac.ui.cs.advprog.bayarservice.exception.InvoiceDoesNotExistException;
import id.ac.ui.cs.advprog.bayarservice.model.invoice.Invoice;
import id.ac.ui.cs.advprog.bayarservice.repository.InvoiceRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DiscountServiceImpl implements  DiscountService {

    private final InvoiceRepository invoiceRepository;

    public DiscountServiceImpl(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    public void giveDiscount(UUID sessionId, DiscountRequest request) {
        Invoice invoice = this.invoiceRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new InvoiceDoesNotExistException(sessionId));

        handleInvoiceDiscount(invoice, request.getDiscount());

        this.invoiceRepository.save(invoice);
    }

    private void handleInvoiceDiscount(Invoice invoice, long discount) {
        invoice.setDiscount(invoice.getDiscount() + discount);
        if (invoice.getTotalAmount() - discount < 0) {
            invoice.setTotalAmount(0L);
        } else {
            invoice.setTotalAmount(invoice.getTotalAmount() - discount);
        }
    }
}
