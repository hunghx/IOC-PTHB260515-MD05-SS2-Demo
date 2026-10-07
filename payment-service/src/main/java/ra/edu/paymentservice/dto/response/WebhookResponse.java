package ra.edu.paymentservice.dto.response;

import lombok.*;
import ra.edu.paymentservice.constant.OrderStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebhookResponse {

    private String orderCode;
    private OrderStatus status;
    private boolean enrollmentTriggered;
}
