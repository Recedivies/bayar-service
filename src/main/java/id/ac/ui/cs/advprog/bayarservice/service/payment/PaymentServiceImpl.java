package id.ac.ui.cs.advprog.bayarservice.service.payment;

import id.ac.ui.cs.advprog.bayarservice.model.invoice.PaymentMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements Payment {

    public List<String> getPaymentMethods() {
        List<String> paymentMethods = Stream.of(PaymentMethod.values())
                                            .map(PaymentMethod::name)
                                            .collect(Collectors.toList());
        return paymentMethods;
    }

}
