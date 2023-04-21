package id.ac.ui.cs.advprog.bayarservice.service.coupon;

import id.ac.ui.cs.advprog.bayarservice.dto.coupon.CouponRequest;
import id.ac.ui.cs.advprog.bayarservice.dto.coupon.UseCouponRequest;
import id.ac.ui.cs.advprog.bayarservice.exception.CouponAlreadyUsedException;
import id.ac.ui.cs.advprog.bayarservice.exception.CouponDoesNotExistException;
import id.ac.ui.cs.advprog.bayarservice.exception.InvoiceDoesNotExistException;
import id.ac.ui.cs.advprog.bayarservice.model.coupon.Coupon;
import id.ac.ui.cs.advprog.bayarservice.model.invoice.Invoice;
import id.ac.ui.cs.advprog.bayarservice.repository.CouponRepository;
import id.ac.ui.cs.advprog.bayarservice.repository.InvoiceRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CouponServiceImpl implements  CouponService {

    private final CouponRepository couponRepository;

    private final InvoiceRepository invoiceRepository;

    public CouponServiceImpl(CouponRepository couponRepository, InvoiceRepository invoiceRepository) {
        this.couponRepository = couponRepository;
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    public Coupon findById(Integer id) {
        return this.couponRepository.findById(id)
                .orElseThrow(() -> new CouponDoesNotExistException(id));
    }

    @Override
    public Coupon update(Integer id, CouponRequest request) {
        Coupon coupon = this.findById(id);
        coupon.setName(request.getName());
        coupon.setDiscount(request.getDiscount());
        return this.couponRepository.save(coupon);
    }

    @Override
    public void useCoupon(UUID sessionId, UseCouponRequest request) {
        Invoice invoice = this.invoiceRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new InvoiceDoesNotExistException(sessionId));
        Coupon coupon = this.couponRepository.findByName(request.getName())
                .orElseThrow(() -> new CouponDoesNotExistException(request.getName()));

        if (coupon.isUsed()) {
            throw new CouponAlreadyUsedException(request.getName());
        }
        handleInvoiceDiscount(invoice, coupon.getDiscount());
        coupon.setUsed(true);
        this.couponRepository.save(coupon);
        this.invoiceRepository.save(invoice);
    }

    private void handleInvoiceDiscount(Invoice invoice, long discountCoupon) {
        invoice.setDiscount(invoice.getDiscount() + discountCoupon);
        if (invoice.getTotalAmount() - discountCoupon < 0) {
            invoice.setTotalAmount(0L);
        } else {
            invoice.setTotalAmount(invoice.getTotalAmount() - discountCoupon);
        }
    }
}
