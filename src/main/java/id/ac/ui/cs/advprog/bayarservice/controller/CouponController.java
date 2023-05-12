package id.ac.ui.cs.advprog.bayarservice.controller;

import id.ac.ui.cs.advprog.bayarservice.dto.coupon.CouponRequest;
import id.ac.ui.cs.advprog.bayarservice.dto.coupon.UseCouponRequest;
import id.ac.ui.cs.advprog.bayarservice.model.bank.Bank;
import id.ac.ui.cs.advprog.bayarservice.model.coupon.Coupon;
import id.ac.ui.cs.advprog.bayarservice.service.coupon.CouponService;
import id.ac.ui.cs.advprog.bayarservice.util.Response;
import id.ac.ui.cs.advprog.bayarservice.util.ResponseHandler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CouponController {
    private final CouponService couponService;
    private static final String SUCCESS = "SUCCESS";

    @GetMapping("/coupons")
    public ResponseEntity<Object> getAllCoupons() {
        List<Coupon> coupons = couponService.getAll();
        return ResponseHandler.generateResponse(new Response(
                "Success retrieved data", HttpStatus.OK, SUCCESS, coupons)
        );
    }

    @GetMapping("/coupons/available")
    public ResponseEntity<Object> getAllAvailableCoupons() {
        List<Coupon> coupons = couponService.getAllAvailableCoupon();
        return ResponseHandler.generateResponse(new Response(
                "Success retrieved data", HttpStatus.OK, SUCCESS, coupons)
        );
    }

    @PutMapping("/coupons/{couponId}")
    public ResponseEntity<Object> updateCoupon(@PathVariable Integer couponId, @RequestBody @Valid CouponRequest request) {
        Coupon coupon = couponService.update(couponId, request);
        return ResponseHandler.generateResponse(new Response(
                "Success created invoice", HttpStatus.OK, SUCCESS, coupon)
        );
    }

    @PostMapping("/sessions/{sessionId}/coupons/use")
    public ResponseEntity<Object> useCoupon(@PathVariable UUID sessionId, @RequestBody @Valid UseCouponRequest request) {
        couponService.useCoupon(sessionId, request);
        return ResponseHandler.generateResponse(new Response(
                "Success Used Coupon", HttpStatus.OK, SUCCESS, null)
        );
    }

    @PostMapping("/coupons/createCoupon")
    public ResponseEntity<Object> createCoupon(@RequestBody @Valid CouponRequest request) {
        Coupon coupon = couponService.createCoupon(request);
        return ResponseHandler.generateResponse(new Response(
                "Success created coupon", HttpStatus.CREATED, SUCCESS, coupon)
        );
    }

    @DeleteMapping("/coupons/delete/{couponId}")
    public ResponseEntity<Object> deleteCoupon(@PathVariable Integer couponId) {
        couponService.deleteCoupon(couponId);
        return ResponseHandler.generateResponse(new Response(
                "Success deleted coupon", HttpStatus.OK, SUCCESS, null)
        );
    }
}
