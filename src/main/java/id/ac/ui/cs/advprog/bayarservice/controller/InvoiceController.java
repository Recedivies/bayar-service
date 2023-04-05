package id.ac.ui.cs.advprog.bayarservice.controller;

import id.ac.ui.cs.advprog.bayarservice.dto.Bill.BillRequest;
import id.ac.ui.cs.advprog.bayarservice.dto.Invoice.InvoiceRequest;
import id.ac.ui.cs.advprog.bayarservice.model.invoice.Invoice;
import id.ac.ui.cs.advprog.bayarservice.service.bill.BillService;
import id.ac.ui.cs.advprog.bayarservice.service.invoice.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import id.ac.ui.cs.advprog.bayarservice.model.bill.Bill;



@RestController
@RequestMapping("/api/v1/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    private final BillService billService;

    @GetMapping("/id/{invoiceId}")
    public ResponseEntity<Invoice> getInvoice(@PathVariable Integer invoiceId) {
        Invoice response = invoiceService.findById(invoiceId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    public ResponseEntity<Invoice> addInvoice(@RequestBody InvoiceRequest request) {
        Invoice response = invoiceService.create(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{invoiceId}")
    public ResponseEntity<Bill> addBillToInvoice(@RequestBody BillRequest request) {
        Bill response = billService.create(request);
        return ResponseEntity.ok(response);
    }


}
