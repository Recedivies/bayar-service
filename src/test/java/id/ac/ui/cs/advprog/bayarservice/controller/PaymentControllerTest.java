package id.ac.ui.cs.advprog.bayarservice.controller;

import id.ac.ui.cs.advprog.bayarservice.Util;
import id.ac.ui.cs.advprog.bayarservice.dto.payment.PaymentRequest;
import id.ac.ui.cs.advprog.bayarservice.model.invoice.Invoice;
import id.ac.ui.cs.advprog.bayarservice.model.invoice.PaymentMethod;
import id.ac.ui.cs.advprog.bayarservice.model.payment.PaymentHistory;
import id.ac.ui.cs.advprog.bayarservice.service.payment.PaymentServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PaymentController.class)
@AutoConfigureMockMvc
class PaymentControllerTest {
    private static final String END_POINT_PATH = "/api/v1";
    @MockBean
    private PaymentServiceImpl paymentService;
    @Autowired
    private MockMvc mockMvc;  // to mock http request

    @Test
    void testGetPaymentMethodsShouldReturn200OK() throws Exception {
        String getPaymentMethod = "/methods";
        String requestURI = END_POINT_PATH + getPaymentMethod;

        mockMvc.perform(get(requestURI))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("getPaymentMethods"))
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andDo(print());

        verify(paymentService, atLeastOnce()).getPaymentMethods();
    }

    @Test
    void testCreatePaymentShouldReturn201CREATED() throws Exception {
        int invoiceId = 123;
        String requestURI = END_POINT_PATH + "/invoices/" + invoiceId + "/payments";

        Invoice invoice = Invoice.builder()
                .paymentMethod(PaymentMethod.CASH)
                .adminFee(5000)
                .totalAmount(100000)
                .discount(5000)
                .sessionId(UUID.randomUUID())
                .build();

        PaymentRequest paymentRequest = PaymentRequest.builder()
                .totalAmount(100000L)
                .sessionId(UUID.randomUUID())
                .paymentMethod(String.valueOf(PaymentMethod.CASH))
                .build();

        PaymentHistory paymentHistory = PaymentHistory.builder()
                .totalAmount(100000L)
                .sessionId(UUID.randomUUID())
                .invoice(invoice)
                .build();

        when(paymentService.create(any(Integer.class), any(PaymentRequest.class))).thenReturn(paymentHistory);

        String requestBody = Util.mapToJson(paymentRequest);

        mockMvc.perform(post(requestURI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(handler().methodName("createPayment"))
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andDo(print());

        verify(paymentService, atLeastOnce()).create(any(Integer.class), any(PaymentRequest.class));
    }

    @Test
    void testCreatePaymentShouldReturn400BadRequest() throws Exception {
        int invoiceId = 123;
        String requestURI = END_POINT_PATH + "/invoices/" + invoiceId + "/payments";

        PaymentHistory paymentHistory = PaymentHistory.builder().build();
        String requestBody = Util.mapToJson(paymentHistory);

        mockMvc.perform(post(requestURI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(handler().methodName("createPayment"))
                .andExpect(jsonPath("$.status").value("FAILED"))
                .andDo(print());
    }

    @Test
    public void testAddInvoiceShouldReturn405MethodNotAllowed() throws Exception {
        int invoiceId = 123;
        String requestURI = END_POINT_PATH + "/invoices/" + invoiceId + "/payments";

        mockMvc.perform(get(requestURI))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.status").value("FAILED"))
                .andDo(print());
    }
}
