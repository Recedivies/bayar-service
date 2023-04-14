package id.ac.ui.cs.advprog.bayarservice.dto.Invoice;

import id.ac.ui.cs.advprog.bayarservice.model.bill.Bill;
import id.ac.ui.cs.advprog.bayarservice.model.invoice.Invoice;
import id.ac.ui.cs.advprog.bayarservice.model.invoice.PaymentMethod;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceRequest {


    @NotNull(message = "sessionId is mandatory")
    private UUID sessionId;

    @NotBlank(message = "paymentMethod is mandatory")
    private String paymentMethod;

    @Min(0)
    @NotNull(message = "adminFee is mandatory")
    private Integer adminFee;

    @Min(0)
    @NotNull(message = "totalAmount is mandatory")
    private Integer totalAmount;

    @Min(0)
    @NotNull(message = "discount is mandatory")
    private Integer discount;

    public Invoice toEntity() {
        Invoice entity = new Invoice();

        entity.setSessionId(this.sessionId);
        entity.setDiscount(this.discount);
        entity.setPaymentMethod(PaymentMethod.valueOf(this.paymentMethod));
        entity.setTotalAmount(this.totalAmount);
        entity.setAdminFee(this.adminFee);

        return entity;
    }
}
