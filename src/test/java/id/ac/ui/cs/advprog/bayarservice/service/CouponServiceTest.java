package id.ac.ui.cs.advprog.bayarservice.service;

import id.ac.ui.cs.advprog.bayarservice.dto.coupon.CouponRequest;
import id.ac.ui.cs.advprog.bayarservice.dto.coupon.UseCouponRequest;
import id.ac.ui.cs.advprog.bayarservice.exception.coupon.CouponAlreadyExistException;
import id.ac.ui.cs.advprog.bayarservice.exception.coupon.CouponAlreadyUsedException;
import id.ac.ui.cs.advprog.bayarservice.exception.coupon.CouponDoesNotExistException;
import id.ac.ui.cs.advprog.bayarservice.exception.InvoiceDoesNotExistException;
import id.ac.ui.cs.advprog.bayarservice.model.coupon.Coupon;
import id.ac.ui.cs.advprog.bayarservice.model.invoice.Invoice;
import id.ac.ui.cs.advprog.bayarservice.repository.CouponRepository;
import id.ac.ui.cs.advprog.bayarservice.repository.InvoiceRepository;
import id.ac.ui.cs.advprog.bayarservice.service.coupon.CouponServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CouponServiceTest {
    @InjectMocks
    private CouponServiceImpl couponService;

    @Mock
    private CouponRepository couponRepository;

    @Mock
    private InvoiceRepository invoiceRepository;

    Invoice invoice;
    Coupon coupon;
    Coupon newCoupon;
    CouponRequest updateRequest;
    UseCouponRequest useCouponRequest;

    @BeforeEach
    void setUp() {
        invoice = Invoice.builder()
                .totalAmount(100000L)
                .discount(5000L)
                .build();

        coupon = Coupon.builder()
                .name("SEPTEMBERCERIA")
                .discount(50000L)
                .build();

        newCoupon = Coupon.builder()
                .name("RAMADHANSALE")
                .discount(100000000L)
                .build();

        updateRequest = CouponRequest.builder()
                .name("RAMADHANSALE")
                .discount(100000000L)
                .build();

        useCouponRequest = UseCouponRequest.builder()
                .name("SEPTEMBERCERIA")
                .build();
    }

    @Test
    void whenFindByIdAndFoundShouldReturnCoupon() {
        when(couponRepository.findById(any(Integer.class))).thenReturn(Optional.of(coupon));

        Coupon result = couponService.findById(1);

        verify(couponRepository, atLeastOnce()).findById(any(Integer.class));
        Assertions.assertEquals(coupon, result);
    }

    @Test
    void whenFindByIdAndNotFoundShouldThrowException() {
        when(couponRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        Assertions.assertThrows(CouponDoesNotExistException.class, () -> couponService.findById(1));
    }

    @Test
    void whenUpdateCouponAndFoundShouldReturnTheUpdatedCoupon() {
        when(couponRepository.findById(any(Integer.class))).thenReturn(Optional.of(coupon));
        when(couponRepository.save(any(Coupon.class))).thenAnswer(invocation ->
                invocation.getArgument(0, Coupon.class));

        Coupon result = couponService.update(0, updateRequest);
        verify(couponRepository, atLeastOnce()).save(any(Coupon.class));
        Assertions.assertEquals(newCoupon, result);
    }

    @Test
    void whenUpdateCouponAndCouponNameAlreadyExistShouldThrowException() {
        when(couponRepository.findByName(any(String.class))).thenReturn(Optional.of(coupon));
        Assertions.assertThrows(CouponAlreadyExistException.class,
                () -> couponService.update(0, updateRequest));
    }

    @Test
    void whenUpdateCouponAndNotFoundShouldThrowException() {
        when(couponRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
        Assertions.assertThrows(CouponDoesNotExistException.class,
                () -> couponService.update(0, updateRequest));
    }

    @Test
    void whenUseCouponShouldUpdateInvoiceDiscount() {
        when(invoiceRepository.findBySessionId(any(UUID.class))).thenReturn(Optional.of(invoice));
        when(couponRepository.findByName(any(String.class))).thenReturn(Optional.of(coupon));

        couponService.useCoupon(UUID.randomUUID(), useCouponRequest);
        verify(couponRepository, atLeastOnce()).save(any(Coupon.class));
    }

    @Test
    void whenUseCouponAndCouponAlreadyUsedThrowException() {
        when(invoiceRepository.findBySessionId(any(UUID.class))).thenReturn(Optional.of(invoice));
        coupon.setUsed(true);
        when(couponRepository.findByName(any(String.class))).thenReturn(Optional.of(coupon));

        Assertions.assertThrows(CouponAlreadyUsedException.class,
                () -> couponService.useCoupon(UUID.randomUUID(), useCouponRequest));
    }

    @Test
    void whenUseCouponAndDiscountGreaterThanInvoiceTotalAmount() {
        when(invoiceRepository.findBySessionId(any(UUID.class))).thenReturn(Optional.of(invoice));
        coupon.setDiscount(10000000000L);
        when(couponRepository.findByName(any(String.class))).thenReturn(Optional.of(coupon));

        when(invoiceRepository.save(any(Invoice.class))).thenAnswer(invocation -> {
            var invoice = invocation.getArgument(0, Invoice.class);
            invoice.setTotalAmount(0L);
            return invoice;
        });

        couponService.useCoupon(UUID.randomUUID(), useCouponRequest);
        verify(couponRepository, atLeastOnce()).save(any(Coupon.class));
        Assertions.assertEquals(0L, invoice.getTotalAmount());
    }

    @Test
    void whenUseCouponAndInvoiceNotFoundThrowException() {
        when(invoiceRepository.findBySessionId(any(UUID.class))).thenReturn(Optional.empty());
        Assertions.assertThrows(InvoiceDoesNotExistException.class,
                () -> couponService.useCoupon(UUID.randomUUID(), useCouponRequest));
    }

    @Test
    void whenUseCouponAndCouponNotFoundThrowException() {
        when(invoiceRepository.findBySessionId(any(UUID.class))).thenReturn(Optional.of(invoice));
        when(couponRepository.findByName(any())).thenReturn(Optional.empty());
        Assertions.assertThrows(CouponDoesNotExistException.class,
                () -> couponService.useCoupon(UUID.randomUUID(), useCouponRequest));
    }
}
