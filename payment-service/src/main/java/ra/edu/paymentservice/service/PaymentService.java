package ra.edu.paymentservice.service;

import ra.edu.paymentservice.dto.request.CreateOrderRequest;
import ra.edu.paymentservice.dto.request.PaymentWebhookRequest;
import ra.edu.paymentservice.dto.response.OrderDetailResponse;
import ra.edu.paymentservice.dto.response.OrderResponse;
import ra.edu.paymentservice.dto.response.WebhookResponse;

import java.util.UUID;

public interface PaymentService {

    OrderResponse createOrder(CreateOrderRequest request);

    WebhookResponse processWebhook(PaymentWebhookRequest request);

    OrderDetailResponse getOrderDetail(UUID orderId);
}
