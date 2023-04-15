package id.ac.ui.cs.advprog.bayarservice.service.invoice;

import java.util.List;

import id.ac.ui.cs.advprog.bayarservice.dto.Invoice.InvoiceRequest;
import id.ac.ui.cs.advprog.bayarservice.model.invoice.Invoice;
import org.springframework.stereotype.Service;

@Service
public interface InvoiceService {
    List<Invoice> findAll();
    Invoice findById(Integer invoiceId);
    Invoice create(InvoiceRequest request);
    Invoice update(Integer invoiceId, InvoiceRequest request);
    void delete(Integer invoiceId);
}
