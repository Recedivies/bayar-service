package id.ac.ui.cs.advprog.bayarservice.controller;

import id.ac.ui.cs.advprog.bayarservice.dto.Bill.BillRequest;
import id.ac.ui.cs.advprog.bayarservice.model.bill.Bill;
import id.ac.ui.cs.advprog.bayarservice.service.bill.BillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bills")
@RequiredArgsConstructor
public class BillController {
    private final BillService billService;

    @GetMapping("/{bill_id}")
    public ResponseEntity<Bill> getBillById(@PathVariable Integer bill_id) {
        Bill bill = billService.findById(bill_id);
        return ResponseEntity.ok(bill);
    }

    @DeleteMapping("/bills/delete/{bill_id}")
    public ResponseEntity<String> deleteBillById(@PathVariable Integer bill_id) {
        billService.delete(bill_id);
        return ResponseEntity.ok(String.format("Deleted Bill with id %d", bill_id));
    }

    @PutMapping("/bills/update/{bill_id}")
    public ResponseEntity<Bill> updateBillById(@PathVariable Integer bill_id, @RequestBody BillRequest request) {
        Bill updatedBill = billService.update(bill_id, request);
        return ResponseEntity.ok(updatedBill);
    }
}
