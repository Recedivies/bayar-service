package id.ac.ui.cs.advprog.bayarservice.controller;

import id.ac.ui.cs.advprog.bayarservice.Util;
import id.ac.ui.cs.advprog.bayarservice.dto.Bill.BillRequest;
import id.ac.ui.cs.advprog.bayarservice.dto.Invoice.InvoiceRequest;
import id.ac.ui.cs.advprog.bayarservice.exception.BillDoesNotExistException;
import id.ac.ui.cs.advprog.bayarservice.model.bill.Bill;
import id.ac.ui.cs.advprog.bayarservice.model.invoice.PaymentMethod;
import id.ac.ui.cs.advprog.bayarservice.service.bill.BillServiceImpl;
import id.ac.ui.cs.advprog.bayarservice.model.invoice.Invoice;
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

@WebMvcTest(controllers = BillController.class)
@AutoConfigureMockMvc
class BillControllerTest {
    private static final String END_POINT_PATH = "/api/v1/bills";

    @Autowired
    private MockMvc mockMvc; // to mock http request

    @MockBean
    private BillServiceImpl billService;

    @Test
    void testGetBillByIdShouldReturn200OK() throws Exception {
        int billId = 123;
        String requestURI = END_POINT_PATH + "/" + billId;

        Bill bill = Bill.builder()
                .id(billId)
                .name("Coffee")
                .quantity(5)
                .price(10000)
                .subTotal(50000L)
                .build();

        when(billService.findById(billId)).thenReturn(bill);

        mockMvc.perform(get(requestURI))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("getBillById"))
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.content.id").value(bill.getId()))
                .andDo(print());

        verify(billService, atLeastOnce()).findById(billId);
    }

    @Test
    void testGetBillByIdShouldReturn404NotFound() throws Exception {
        int billId = 123;
        String requestURI = END_POINT_PATH + "/" + billId;

        when(billService.findById(billId)).thenThrow(BillDoesNotExistException.class);

        mockMvc.perform(get(requestURI))
                .andExpect(status().isNotFound())
                .andExpect(handler().methodName("getBillById"))
                .andExpect(jsonPath("$.status").value("FAILED"))
                .andDo(print());

        verify(billService, atMostOnce()).findById(billId);
    }

    @Test
    void testGetBillByIdShouldReturn405MethodNotAllowed() throws Exception {
        int billId = 123;
        String requestURI = END_POINT_PATH + "/" + billId;

        mockMvc.perform(post(requestURI)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.status").value("FAILED"))
                .andDo(print());
    }

    @Test
    void testDeleteBillByIdShouldReturn200OK () throws Exception {
        int billId = 123;
        String requestURI = END_POINT_PATH + "/delete/" + billId;

        Bill bill = Bill.builder()
                .id(billId)
                .name("Coffee")
                .quantity(5)
                .price(10000)
                .subTotal(50000L)
                .build();

        when(billService.findById(billId)).thenReturn(bill);

        mockMvc.perform(delete(requestURI))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("deleteBillById"))
                .andDo(print());

        verify(billService, atLeastOnce()).delete(billId);
    }

    @Test
    void testDeleteBillByIdShouldReturn405MethodNotAllowed() throws Exception {
        int billId = 123;
        String requestURI = END_POINT_PATH + "/delete/" + billId;

        mockMvc.perform(post(requestURI)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.status").value("FAILED"))
                .andDo(print());

        mockMvc.perform(get(requestURI))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.status").value("FAILED"))
                .andDo(print());

        mockMvc.perform(put(requestURI)
                        .contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testDeleteBillByIdShouldReturn404NotFound() throws Exception {
        int billId = 123;
        String requestURI = END_POINT_PATH + "/delete/" + billId;

        when(billService.findById(billId)).thenThrow(BillDoesNotExistException.class);

        mockMvc.perform(delete(requestURI))
                .andExpect(status().isNotFound())
                .andExpect(handler().methodName("deleteBillById"))
                .andExpect(jsonPath("$.status").value("FAILED"))
                .andDo(print());

        verify(billService, atMostOnce()).delete(billId);
    }

    @Test
    void testAddBillShouldReturn200OK() throws Exception {
        int billId = 123;
        String requestURI = "/api/v1/invoices/{invoiceId}/bills";

        BillRequest billRequest = BillRequest.builder()
                .name("Coffee")
                .quantity(5)
                .price(10000)
                .subTotal(50000L)
                .invoiceId(1)
                .build();

        InvoiceRequest invoiceRequest = InvoiceRequest.builder()
                .id(1)
                .paymentMethod(String.valueOf(PaymentMethod.CASH))
                .adminFee(2500)
                .totalAmount(200000)
                .discount(10000)
                .sessionId(UUID.randomUUID())
                .build();

        mockMvc.perform(post("/api/v1/invoices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Util.mapToJson(invoiceRequest)))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("addInvoice"))
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andDo(print());

        mockMvc.perform(post(requestURI, billRequest.getInvoiceId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Util.mapToJson(billRequest)))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("addBill"))
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andDo(print());

        verify(billService, atLeastOnce()).create(billRequest);
    }
}
