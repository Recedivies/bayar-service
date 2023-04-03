package id.ac.ui.cs.advprog.bayarservice.controller;

import id.ac.ui.cs.advprog.bayarservice.dto.Invoice.InvoiceRequest;
import id.ac.ui.cs.advprog.bayarservice.model.invoice.Invoice;
import id.ac.ui.cs.advprog.bayarservice.service.invoice.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    @GetMapping("/id/{id}")
    public ResponseEntity<Invoice> getInvoice(@PathVariable UUID sessionId) {
        Invoice response = invoiceService.findById(sessionId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    public ResponseEntity<Invoice> addInvoice(@RequestBody InvoiceRequest request) {
        Invoice response = invoiceService.create(request);
        return ResponseEntity.ok(response);
    }
}
