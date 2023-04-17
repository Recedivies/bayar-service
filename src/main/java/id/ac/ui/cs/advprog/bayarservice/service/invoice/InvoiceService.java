package id.ac.ui.cs.advprog.bayarservice.service.invoice;

import id.ac.ui.cs.advprog.bayarservice.dto.Invoice.InvoiceRequest;
import id.ac.ui.cs.advprog.bayarservice.model.invoice.Invoice;
import org.springframework.stereotype.Service;

@Service
public interface InvoiceService {
    Invoice findById(Integer invoiceId);
    Invoice create(InvoiceRequest request);
}
