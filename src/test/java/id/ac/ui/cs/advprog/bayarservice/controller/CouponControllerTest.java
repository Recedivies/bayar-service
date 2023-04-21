package id.ac.ui.cs.advprog.bayarservice.controller;

import id.ac.ui.cs.advprog.bayarservice.Util;
import id.ac.ui.cs.advprog.bayarservice.dto.coupon.CouponRequest;
import id.ac.ui.cs.advprog.bayarservice.dto.coupon.UseCouponRequest;
import id.ac.ui.cs.advprog.bayarservice.model.coupon.Coupon;
import id.ac.ui.cs.advprog.bayarservice.service.coupon.CouponServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = CouponController.class)
@AutoConfigureMockMvc
public class CouponControllerTest {
    private static final String END_POINT_PATH = "/api/v1/";

    @Autowired
    private MockMvc mockMvc; // to mock http request

    @MockBean
    private CouponServiceImpl couponService;

    Coupon coupon;

    CouponRequest couponRequest;

    UseCouponRequest useCouponRequest;

    @BeforeEach
    void setUp() {
        coupon = Coupon.builder()
                .name("SEPTEMBERCERIA")
                .discount(50000L)
                .build();

        couponRequest = CouponRequest.builder()
                .name("SEPTEMBERCERIA")
                .discount(50000L)
                .build();

        useCouponRequest = UseCouponRequest.builder()
                .name("SEPTEMBERCERIA")
                .build();
    }

    @Test
    void testUpdateCoupon() throws Exception {
        int couponId = 123;
        String requestURI = END_POINT_PATH + "coupons/" + couponId;

        when(couponService.update(any(Integer.class), any(CouponRequest.class))).thenReturn(coupon);

        String requestBody = Util.mapToJson(couponRequest);

        mockMvc.perform(put(requestURI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("updateCoupon"))
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.content.name").value(coupon.getName()))
                .andDo(print());

        verify(couponService, atLeastOnce()).update(any(Integer.class), any(CouponRequest.class));
    }

    @Test
    void testUseCoupon() throws Exception {
        UUID sessionId = UUID.randomUUID();
        String requestURI = END_POINT_PATH + "sessions/" + sessionId + "/coupons/use";

        String requestBody = Util.mapToJson(useCouponRequest);

        mockMvc.perform(post(requestURI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("useCoupon"))
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andDo(print());
    }
}
