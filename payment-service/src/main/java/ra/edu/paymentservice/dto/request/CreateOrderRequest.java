package ra.edu.paymentservice.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ra.edu.paymentservice.constant.PaymentMethod;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrderRequest {

    @NotNull(message = "userId không được để trống")
    private UUID userId;

    @NotNull(message = "courseId không được để trống")
    private UUID courseId;

    @NotNull(message = "Số tiền thanh toán không được để trống")
    @DecimalMin(value = "0.0", inclusive = false, message = "Số tiền phải lớn hơn 0")
    private BigDecimal amount;

    @NotNull(message = "Phương thức thanh toán không được để trống")
    private PaymentMethod paymentMethod;
}
