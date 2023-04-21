package id.ac.ui.cs.advprog.bayarservice.exception;

public class CouponAlreadyUsedException extends RuntimeException {
    public CouponAlreadyUsedException(String couponName) {
        super("Coupon with name " + couponName + " does not exist");
    }
}
