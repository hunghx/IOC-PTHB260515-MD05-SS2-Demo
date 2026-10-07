package ra.edu.paymentservice.dto.response;

import lombok.*;
import ra.edu.paymentservice.constant.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {

    private UUID orderId;
    private String orderCode;
    private BigDecimal totalAmount;
    private OrderStatus status;
    private String paymentUrl;
    private LocalDateTime createdAt;
}
