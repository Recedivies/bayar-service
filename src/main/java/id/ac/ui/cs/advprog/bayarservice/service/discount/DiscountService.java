package id.ac.ui.cs.advprog.bayarservice.service.discount;

import id.ac.ui.cs.advprog.bayarservice.dto.Discount.DiscountRequest;
import id.ac.ui.cs.advprog.bayarservice.model.coupon.Coupon;

import java.util.UUID;

public interface DiscountService {
    void giveDiscount(UUID uuid, DiscountRequest request);
}
