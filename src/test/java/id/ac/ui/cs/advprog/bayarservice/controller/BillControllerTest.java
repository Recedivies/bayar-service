package id.ac.ui.cs.advprog.bayarservice.controller;

import id.ac.ui.cs.advprog.bayarservice.model.bill.Bill;
import id.ac.ui.cs.advprog.bayarservice.service.bill.BillServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = BillController.class)
@AutoConfigureMockMvc
public class BillController {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BillServiceImpl service;

    Bill bill;

    @BeforeEach
    void setUp() {
        bill = Bill.builder()
                .name("Coffee")
                .quantity(5)
                .price(10000)
                .subTotal(50000L)
                .build();
    }

    @Test
    void testGetBillById() throws Exception {
        when(service.findById(any(Integer.class))).thenReturn(bill);

        mvc.perform(get("/api/v1/bills/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("getBillById"))
                .andExpect(jsonPath("$.name").value(bill.getName()));

        verify(service, atLeastOnce()).findById(any(Integer.class));
    }
}
