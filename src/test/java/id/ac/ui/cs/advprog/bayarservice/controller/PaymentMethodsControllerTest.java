package id.ac.ui.cs.advprog.bayarservice.controller;

import id.ac.ui.cs.advprog.bayarservice.service.payment.PaymentServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PaymentMethodController.class)
@AutoConfigureMockMvc
class PaymentMethodsControllerTest {
    private static final String END_POINT_PATH = "/api/v1";
    @MockBean
    private PaymentServiceImpl paymentMethodService;
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

        verify(paymentMethodService, atLeastOnce()).getPaymentMethods();
    }
}
