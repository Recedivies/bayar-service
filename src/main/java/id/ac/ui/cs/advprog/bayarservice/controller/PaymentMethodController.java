package id.ac.ui.cs.advprog.bayarservice.controller;

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
public class PaymentMethodController {

    private final PaymentServiceImpl paymentService;

    @GetMapping("/methods")
    public ResponseEntity<Object> getPaymentMethods() {
        List<String> response = paymentService.getPaymentMethods();
        return ResponseHandler.generateResponse(new Response(
                "Success retrieved data", HttpStatus.OK, "SUCCESS", response)
        );
    }
}
