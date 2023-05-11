package id.ac.ui.cs.advprog.bayarservice.service.discount;

import id.ac.ui.cs.advprog.bayarservice.dto.discount.DiscountRequest;
import id.ac.ui.cs.advprog.bayarservice.exception.invoice.InvoiceDoesNotExistException;
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

        if (request.getDiscountType().equals("Nominal")) {
            invoice.setDiscount(invoice.getDiscount() + request.getDiscount());
        }

        if (request.getDiscountType().equals("Percentage")) {
            long newDiscount = (invoice.getTotalAmount()/100) * request.getDiscount();
            invoice.setDiscount(invoice.getDiscount() + newDiscount);
        }

        this.invoiceRepository.save(invoice);
    }
}
