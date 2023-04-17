package id.ac.ui.cs.advprog.bayarservice.controller;

import id.ac.ui.cs.advprog.bayarservice.dto.Invoice.InvoiceRequest;
import id.ac.ui.cs.advprog.bayarservice.model.invoice.Invoice;
import id.ac.ui.cs.advprog.bayarservice.service.invoice.InvoiceService;
import id.ac.ui.cs.advprog.bayarservice.util.Response;
import id.ac.ui.cs.advprog.bayarservice.util.ResponseHandler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    @GetMapping("/invoices/id/{invoiceId}")
    public ResponseEntity<Object> getInvoiceById(@PathVariable Integer invoiceId) {
        Invoice response = invoiceService.findById(invoiceId);
        return ResponseHandler.generateResponse(new Response(
                "Success retrieved data", HttpStatus.OK, "SUCCESS", response)
        );
    }

    @PostMapping(path = "/invoices", consumes = "application/json")
    public ResponseEntity<Object> addInvoice(@RequestBody @Valid InvoiceRequest request) {
        Invoice invoice = invoiceService.create(request);
        return ResponseHandler.generateResponse(new Response(
                "Success created invoice", HttpStatus.CREATED, "SUCCESS", invoice)
        );
    }
}
