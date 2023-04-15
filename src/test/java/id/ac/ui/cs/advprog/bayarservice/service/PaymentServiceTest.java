package id.ac.ui.cs.advprog.bayarservice.service;

import id.ac.ui.cs.advprog.bayarservice.model.bank.Bank;
import id.ac.ui.cs.advprog.bayarservice.model.bill.Bill;
import id.ac.ui.cs.advprog.bayarservice.model.invoice.PaymentMethod;
import id.ac.ui.cs.advprog.bayarservice.service.payment.PaymentServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {
    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Mock
    private PaymentMethod paymentMethod;


    @Test
    void whenGetPaymentMethodShouldReturnList() {

        List<String> result = paymentService.getPaymentMethods();
        List<String> paymentMethods = Stream.of(paymentMethod.values())
                .map(PaymentMethod::name)
                .collect(Collectors.toList());

        Assertions.assertEquals(result, paymentMethods);
    }
}
