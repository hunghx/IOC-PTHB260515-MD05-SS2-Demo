package ra.edu.paymentservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.edu.paymentservice.dto.request.CreateOrderRequest;
import ra.edu.paymentservice.dto.request.PaymentWebhookRequest;
import ra.edu.paymentservice.dto.response.ApiResponse;
import ra.edu.paymentservice.dto.response.OrderDetailResponse;
import ra.edu.paymentservice.dto.response.OrderResponse;
import ra.edu.paymentservice.dto.response.WebhookResponse;
import ra.edu.paymentservice.service.PaymentService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/orders")
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(
            @Valid @RequestBody CreateOrderRequest request
    ) {
        OrderResponse response = paymentService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(response, "Khởi tạo đơn hàng thành công"));
    }

    @PostMapping("/webhook")
    public ResponseEntity<ApiResponse<WebhookResponse>> processWebhook(
            @Valid @RequestBody PaymentWebhookRequest request
    ) {
        WebhookResponse response = paymentService.processWebhook(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<ApiResponse<OrderDetailResponse>> getOrderDetail(
            @PathVariable("orderId") UUID orderId
    ) {
        OrderDetailResponse response = paymentService.getOrderDetail(orderId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
