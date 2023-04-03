package id.ac.ui.cs.advprog.bayarservice.controller;

import id.ac.ui.cs.advprog.bayarservice.model.bill.Bill;
import id.ac.ui.cs.advprog.bayarservice.service.bill.BillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
