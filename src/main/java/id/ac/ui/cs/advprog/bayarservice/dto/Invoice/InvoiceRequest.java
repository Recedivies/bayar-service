package id.ac.ui.cs.advprog.bayarservice.dto.Invoice;

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
    private UUID sessionId;
    private String paymentMethod;
    private Integer adminFee;
    private Integer totalAmount;
    private Integer discount;
}
