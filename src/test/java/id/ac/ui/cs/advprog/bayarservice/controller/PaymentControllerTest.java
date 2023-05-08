package id.ac.ui.cs.advprog.bayarservice.controller;

import id.ac.ui.cs.advprog.bayarservice.Util;
import id.ac.ui.cs.advprog.bayarservice.dto.payment.PaymentRequest;
import id.ac.ui.cs.advprog.bayarservice.model.invoice.Invoice;
import id.ac.ui.cs.advprog.bayarservice.model.invoice.PaymentMethod;
import id.ac.ui.cs.advprog.bayarservice.model.payment.PaymentLog;
import id.ac.ui.cs.advprog.bayarservice.service.payment.PaymentServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
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
                .totalAmount(100000L)
                .discount(5000L)
                .sessionId(UUID.randomUUID())
                .build();

        PaymentRequest paymentRequest = PaymentRequest.builder()
                .totalAmount(100000L)
                .sessionId(UUID.randomUUID())
                .paymentMethod(String.valueOf(PaymentMethod.CASH))
                .build();

        PaymentLog paymentLog = PaymentLog.builder()
                .totalAmount(100000L)
                .sessionId(UUID.randomUUID())
                .invoice(invoice)
                .build();

        when(paymentService.create(any(Integer.class), any(PaymentRequest.class))).thenReturn(paymentLog);

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

        PaymentLog paymentLog = PaymentLog.builder().build();
        String requestBody = Util.mapToJson(paymentLog);

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

    @Test
    public void testGetLogPaymentShouldReturn200OK() throws Exception {
        int invoiceId = 123;
        String requestURI = END_POINT_PATH + "/log/paymentLog";

        Invoice invoice = Invoice.builder()
                .paymentMethod(PaymentMethod.CASH)
                .totalAmount(100000L)
                .discount(5000L)
                .sessionId(UUID.randomUUID())
                .build();

        PaymentRequest paymentRequest = PaymentRequest.builder()
                .totalAmount(100000L)
                .sessionId(UUID.randomUUID())
                .paymentMethod(String.valueOf(PaymentMethod.CASH))
                .build();

        PaymentLog paymentLog = PaymentLog.builder()
                .totalAmount(100000L)
                .sessionId(UUID.randomUUID())
                .createdAt(new Date(Instant.now().toEpochMilli()))
                .invoice(invoice)
                .build();

        when(paymentService.create(any(Integer.class), any(PaymentRequest.class))).thenReturn(paymentLog);

        String requestBody = Util.mapToJson(paymentRequest);

        mockMvc.perform(post(END_POINT_PATH + "/invoices/" + invoiceId + "/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(handler().methodName("createPayment"))
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andDo(print());

        verify(paymentService, atLeastOnce()).create(any(Integer.class), any(PaymentRequest.class));

        when(paymentService.getPaymentLog()).thenReturn(List.of(paymentLog));

        mockMvc.perform(get(requestURI))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("getPaymentLog"))
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andDo(print());
    }

    @Test
    public void testGetLogPaymentShouldReturn405MethodNotAllowed() throws Exception {
        String requestURI = END_POINT_PATH + "/log/paymentLog";

        mockMvc.perform(post(requestURI))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.status").value("FAILED"))
                .andDo(print());

        mockMvc.perform(put(requestURI))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.status").value("FAILED"))
                .andDo(print());

        mockMvc.perform(delete(requestURI))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.status").value("FAILED"))
                .andDo(print());
    }
    @Test
    public void testGetLogPaymentByYearAndMonthShouldReturn200OK() throws Exception {
        int invoiceId = 123;
        int year = LocalDate.now().getYear();
        int month = LocalDate.now().getMonthValue();
        String requestURI = END_POINT_PATH + "/log/paymentLog/monthly/" + year + "/" + month;

        Invoice invoice = Invoice.builder()
                .paymentMethod(PaymentMethod.CASH)
                .totalAmount(100000L)
                .discount(5000L)
                .sessionId(UUID.randomUUID())
                .build();

        PaymentRequest paymentRequest = PaymentRequest.builder()
                .totalAmount(100000L)
                .sessionId(UUID.randomUUID())
                .paymentMethod(String.valueOf(PaymentMethod.CASH))
                .build();

        PaymentLog paymentLog = PaymentLog.builder()
                .totalAmount(100000L)
                .sessionId(UUID.randomUUID())
                .createdAt(new Date(Instant.now().toEpochMilli()))
                .invoice(invoice)
                .createdAt(new Date(Instant.now().toEpochMilli()))
                .build();

        when(paymentService.create(any(Integer.class), any(PaymentRequest.class))).thenReturn(paymentLog);

        String requestBody = Util.mapToJson(paymentRequest);

        mockMvc.perform(post(END_POINT_PATH + "/invoices/" + invoiceId + "/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(handler().methodName("createPayment"))
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andDo(print());

        verify(paymentService, atLeastOnce()).create(any(Integer.class), any(PaymentRequest.class));

        when(paymentService.getPaymentLogByYearAndMonth(any(Integer.class), any(Integer.class))).thenReturn(List.of(paymentLog));

        mockMvc.perform(get(requestURI))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("getPaymentLogByYearAndMonth"))
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andDo(print());
    }

    @Test
    public void testGetLogPaymentByYearAndMonthShouldReturn405MethodNotAllowed() throws Exception {
        int year = LocalDate.now().getYear();
        int month = LocalDate.now().getMonthValue();
        String requestURI = END_POINT_PATH + "/log/paymentLog/monthly/" + year + "/" + month;

        mockMvc.perform(post(requestURI))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.status").value("FAILED"))
                .andDo(print());

        mockMvc.perform(put(requestURI))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.status").value("FAILED"))
                .andDo(print());

        mockMvc.perform(delete(requestURI))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.status").value("FAILED"))
                .andDo(print());
    }

    @Test
    public void testGetLogPaymentByYearShouldReturn200K() throws Exception {
        int invoiceId = 123;
        int year = LocalDate.now().getYear();
        String requestURI = END_POINT_PATH + "/log/paymentLog/" + year;

        Invoice invoice = Invoice.builder()
                .paymentMethod(PaymentMethod.CASH)
                .totalAmount(100000L)
                .discount(5000L)
                .sessionId(UUID.randomUUID())
                .build();

        PaymentRequest paymentRequest = PaymentRequest.builder()
                .totalAmount(100000L)
                .sessionId(UUID.randomUUID())
                .paymentMethod(String.valueOf(PaymentMethod.CASH))
                .build();

        PaymentLog paymentLog = PaymentLog.builder()
                .totalAmount(100000L)
                .sessionId(UUID.randomUUID())
                .createdAt(new Date(Instant.now().toEpochMilli()))
                .invoice(invoice)
                .build();

        when(paymentService.create(any(Integer.class), any(PaymentRequest.class))).thenReturn(paymentLog);

        String requestBody = Util.mapToJson(paymentRequest);

        mockMvc.perform(post(END_POINT_PATH + "/invoices/" + invoiceId + "/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(handler().methodName("createPayment"))
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andDo(print());

        verify(paymentService, atLeastOnce()).create(any(Integer.class), any(PaymentRequest.class));

        when(paymentService.getPaymentLogByYear(any(Integer.class))).thenReturn(List.of(paymentLog));

        mockMvc.perform(get(requestURI))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("getPaymentLogByYear"))
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andDo(print());
    }

    @Test
    public void testGetLogPaymentByYearShouldReturn405MethodNotAllowed() throws Exception {
        int year = LocalDate.now().getYear();
        String requestURI = END_POINT_PATH + "/log/paymentLog/" + year;

        mockMvc.perform(post(requestURI))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.status").value("FAILED"))
                .andDo(print());

        mockMvc.perform(put(requestURI))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.status").value("FAILED"))
                .andDo(print());

        mockMvc.perform(delete(requestURI))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.status").value("FAILED"))
                .andDo(print());
    }

    @Test
    public void testGetLogPaymentByWeekAndYearShouldReturn2000K () throws Exception {
        int invoiceId = 123;
        int year = LocalDate.now().getYear();
        int week = LocalDate.now().get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear());
        String requestURI = END_POINT_PATH + "/log/paymentLog/weekly/" + year + "/" + week;

        Invoice invoice = Invoice.builder()
                .paymentMethod(PaymentMethod.CASH)
                .totalAmount(100000L)
                .discount(5000L)
                .sessionId(UUID.randomUUID())
                .build();

        PaymentRequest paymentRequest = PaymentRequest.builder()
                .totalAmount(100000L)
                .sessionId(UUID.randomUUID())
                .paymentMethod(String.valueOf(PaymentMethod.CASH))
                .build();

        PaymentLog paymentLog = PaymentLog.builder()
                .totalAmount(100000L)
                .sessionId(UUID.randomUUID())
                .createdAt(new Date(Instant.now().toEpochMilli()))
                .invoice(invoice)
                .build();

        when(paymentService.create(any(Integer.class), any(PaymentRequest.class))).thenReturn(paymentLog);

        String requestBody = Util.mapToJson(paymentRequest);

        mockMvc.perform(post(END_POINT_PATH + "/invoices/" + invoiceId + "/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(handler().methodName("createPayment"))
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andDo(print());

        verify(paymentService, atLeastOnce()).create(any(Integer.class), any(PaymentRequest.class));

        when(paymentService.getPaymentLogByWeekAndYear(any(Integer.class), any(Integer.class))).thenReturn(List.of(paymentLog));

        mockMvc.perform(get(requestURI))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("getPaymentLogByWeekAndYear"))
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andDo(print());
    }

    @Test
    public void testGetLogPaymentByWeekAndYearShouldReturn405MethodNotAllowed() throws Exception {
        int year = LocalDate.now().getYear();
        int week = LocalDate.now().get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear());
        String requestURI = END_POINT_PATH + "/log/paymentLog/weekly/" + year + "/" + week;

        mockMvc.perform(post(requestURI))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.status").value("FAILED"))
                .andDo(print());

        mockMvc.perform(put(requestURI))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.status").value("FAILED"))
                .andDo(print());

        mockMvc.perform(delete(requestURI))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.status").value("FAILED"))
                .andDo(print());
    }

}
