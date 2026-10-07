package ra.edu.paymentservice.dto.response;

import lombok.*;
import ra.edu.paymentservice.constant.OrderStatus;
import ra.edu.paymentservice.constant.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetailResponse {

    private UUID orderId;
    private UUID userId;
    private UUID courseId;
    private String orderCode;
    private BigDecimal amount;
    private PaymentMethod paymentMethod;
    private OrderStatus status;
    private String transactionRef;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
