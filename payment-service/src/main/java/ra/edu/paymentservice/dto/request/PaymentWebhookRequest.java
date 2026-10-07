package ra.edu.paymentservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentWebhookRequest {

    @NotBlank(message = "orderCode không được để trống")
    private String orderCode;

    private String transactionNo;

    @NotBlank(message = "responseCode không được để trống")
    private String responseCode;

    private String secureHash;
}
