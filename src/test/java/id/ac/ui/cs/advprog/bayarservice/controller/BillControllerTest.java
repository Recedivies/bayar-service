package id.ac.ui.cs.advprog.bayarservice.controller;

import id.ac.ui.cs.advprog.bayarservice.exception.BillDoesNotExistException;
import id.ac.ui.cs.advprog.bayarservice.model.bill.Bill;
import id.ac.ui.cs.advprog.bayarservice.service.bill.BillServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

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

    @BeforeEach
    void setUp() {

    }

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
}
