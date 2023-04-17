package id.ac.ui.cs.advprog.bayarservice.controller;

import id.ac.ui.cs.advprog.bayarservice.dto.payment.PaymentRequest;
import id.ac.ui.cs.advprog.bayarservice.model.payment.PaymentHistory;
import id.ac.ui.cs.advprog.bayarservice.service.payment.PaymentServiceImpl;
import id.ac.ui.cs.advprog.bayarservice.util.Response;
import id.ac.ui.cs.advprog.bayarservice.util.ResponseHandler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentServiceImpl paymentService;

    @GetMapping("/methods")
    public ResponseEntity<Object> getPaymentMethods() {
        List<String> response = paymentService.getPaymentMethods();
        return ResponseHandler.generateResponse(new Response(
                "Success retrieved data", HttpStatus.OK, "SUCCESS", response)
        );
    }

    @PostMapping("/invoices/{invoiceId}/payments")
    public ResponseEntity<Object> createPayment(@PathVariable Integer invoiceId, @RequestBody @Valid PaymentRequest request) {
        PaymentHistory paymentHistory = paymentService.create(invoiceId, request);
        return ResponseHandler.generateResponse(new Response(
                "Payment processed successfully!", HttpStatus.CREATED, "SUCCESS", paymentHistory)
        );
    }
}
