package id.ac.ui.cs.advprog.bayarservice.dto.Discount;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiscountRequest {

    @NotNull(message = "discount type is mandatory")
    private String discountType;

    @Min(0)
    @NotNull(message = "discount is mandatory")
    private Long discount;
}
