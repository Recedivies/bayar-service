package id.ac.ui.cs.advprog.bayarservice.controller;

import id.ac.ui.cs.advprog.bayarservice.dto.coupon.CouponRequest;
import id.ac.ui.cs.advprog.bayarservice.dto.coupon.UseCouponRequest;
import id.ac.ui.cs.advprog.bayarservice.model.coupon.Coupon;
import id.ac.ui.cs.advprog.bayarservice.service.coupon.CouponService;
import id.ac.ui.cs.advprog.bayarservice.util.Response;
import id.ac.ui.cs.advprog.bayarservice.util.ResponseHandler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CouponController {
    private final CouponService couponService;
//    create constant for success message
    private static final String SUCCESS_MESSAGE = "SUCCESS";

    @PutMapping("/coupons/{couponId}")
    public ResponseEntity<Object> updateCoupon(@PathVariable Integer couponId, @RequestBody @Valid CouponRequest request) {
        Coupon coupon = couponService.update(couponId, request);
        return ResponseHandler.generateResponse(new Response(
        "Success updated coupon", HttpStatus.OK, SUCCESS_MESSAGE, coupon)
        );
    }

    @PostMapping("/sessions/{sessionId}/coupons/use")
    public ResponseEntity<Object> useCoupon(@PathVariable UUID sessionId, @RequestBody @Valid UseCouponRequest request) {
        couponService.useCoupon(sessionId, request);
        return ResponseHandler.generateResponse(new Response(
                "Success used coupon", HttpStatus.OK, SUCCESS_MESSAGE, null)
        );
    }

    @PostMapping("/coupons/createCoupon")
    public ResponseEntity<Object> createCoupon(@RequestBody @Valid CouponRequest request) {
        Coupon coupon = couponService.createCoupon(request);
        return ResponseHandler.generateResponse(new Response(
                "Success created coupon", HttpStatus.OK, SUCCESS_MESSAGE, coupon)
        );
    }

    @DeleteMapping("/coupons/delete/{couponId}")
    public ResponseEntity<Object> deleteCoupon(@PathVariable Integer couponId) {
        couponService.deleteCoupon(couponId);
        return ResponseHandler.generateResponse(new Response(
                "Success deleted coupon", HttpStatus.OK, SUCCESS_MESSAGE, null)
        );
    }
}
