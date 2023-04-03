package id.ac.ui.cs.advprog.bayarservice.controller;

import id.ac.ui.cs.advprog.bayarservice.model.invoice.Invoice;
import id.ac.ui.cs.advprog.bayarservice.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/invoices/")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    @GetMapping("/health")
    public @ResponseBody String invoicesHealth() {
        return "Invoice Page Running";
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Invoice> getInvoice(@PathVariable UUID sessionId) {
        Invoice response = invoiceService.findById(sessionId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    public ResponseEntity<Invoice> postInvoices(@RequestBody Invoice invoices) {
        Invoice response = invoices;
        return ResponseEntity.ok(response);
    }

}
