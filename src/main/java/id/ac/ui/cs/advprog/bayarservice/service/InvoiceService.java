package id.ac.ui.cs.advprog.bayarservice.service;

import java.util.List;
import java.util.UUID;

import id.ac.ui.cs.advprog.bayarservice.dto.InvoiceRequest;
import id.ac.ui.cs.advprog.bayarservice.model.invoice.Invoice;
import org.springframework.stereotype.Service;

@Service
public interface InvoiceService {
    List<Invoice> findAll();
    Invoice findById(UUID sessionId);
    Invoice create(InvoiceRequest request);
    Invoice update(UUID sessionId, InvoiceRequest request);
    void delete(UUID sessionId);
}
