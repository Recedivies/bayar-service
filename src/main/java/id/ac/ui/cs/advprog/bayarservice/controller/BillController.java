package id.ac.ui.cs.advprog.bayarservice.controller;

import id.ac.ui.cs.advprog.bayarservice.dto.bill.BillRequest;
import id.ac.ui.cs.advprog.bayarservice.model.bill.Bill;
import id.ac.ui.cs.advprog.bayarservice.service.bill.BillService;
import id.ac.ui.cs.advprog.bayarservice.util.Response;
import id.ac.ui.cs.advprog.bayarservice.util.ResponseHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class BillController {
    private final BillService billService;

    @GetMapping("/bills/{billId}")
    public ResponseEntity<Object> getBillById(@PathVariable Integer billId) {
        Bill bill = billService.findById(billId);
        return ResponseHandler.generateResponse(new Response(
                "Success retrieved data", HttpStatus.OK, "SUCCESS", bill)
        );
    }

    @PostMapping("/bills")
    public ResponseEntity<Bill> addBillToInvoice(@RequestBody BillRequest request) {
        Bill response = billService.create(request);
        if (response == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/bills/delete/{billId}")
    public ResponseEntity<String> deleteBillById(@PathVariable Integer billId) {
        if (billService.findById(billId) == null) {
            return ResponseEntity.notFound().build();
        }
        billService.delete(billId);
        return ResponseEntity.ok(String.format("Deleted Bill with id %d", billId));
    }

    @PutMapping("/bills/update/{billId}")
    public ResponseEntity<Bill> updateBillById(@PathVariable Integer billId, @RequestBody BillRequest request) {
        if (billService.findById(billId) == null) {
            return ResponseEntity.notFound().build();
        }
        Bill updatedBill = billService.update(billId, request);
        if (billService.findById(billId) == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedBill);
    }
}
