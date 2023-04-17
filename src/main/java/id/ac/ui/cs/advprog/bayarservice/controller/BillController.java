package id.ac.ui.cs.advprog.bayarservice.controller;

import id.ac.ui.cs.advprog.bayarservice.dto.Bill.BillRequest;
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

    @GetMapping("/bills/{bill_id}")
    public ResponseEntity<Object> getBillById(@PathVariable Integer bill_id) {
        Bill bill = billService.findById(bill_id);
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

    @DeleteMapping("/bills/delete/{bill_id}")
    public ResponseEntity<String> deleteBillById(@PathVariable Integer bill_id) {
        if (billService.findById(bill_id) == null) {
            return ResponseEntity.notFound().build();
        }
        billService.delete(bill_id);
        return ResponseEntity.ok(String.format("Deleted Bill with id %d", bill_id));
    }

    @PutMapping("/bills/update/{bill_id}")
    public ResponseEntity<Bill> updateBillById(@PathVariable Integer bill_id, @RequestBody BillRequest request) {
        if (billService.findById(bill_id) == null) {
            return ResponseEntity.notFound().build();
        }
        Bill updatedBill = billService.update(bill_id, request);
        if (billService.findById(bill_id) == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedBill);
    }
}
