package ra.edu.paymentservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import ra.edu.paymentservice.constant.OrderStatus;
import ra.edu.paymentservice.constant.PaymentConstant;
import ra.edu.paymentservice.dto.request.CreateOrderRequest;
import ra.edu.paymentservice.dto.request.PaymentWebhookRequest;
import ra.edu.paymentservice.dto.response.OrderDetailResponse;
import ra.edu.paymentservice.dto.response.OrderResponse;
import ra.edu.paymentservice.dto.response.WebhookResponse;
import ra.edu.paymentservice.entity.Order;
import ra.edu.paymentservice.exception.PaymentException;
import ra.edu.paymentservice.exception.ResourceNotFoundException;
import ra.edu.paymentservice.repository.OrderRepository;
import ra.edu.paymentservice.service.PaymentService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;

    @Value("${services.enrollment.base-url:http://localhost:8083/api/v1/enrollments}")
    private String enrollmentServiceBaseUrl;

    @Override
    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        String orderCode = "ORD" + timestamp + uniqueSuffix;

        Order order = Order.builder()
                .userId(request.getUserId())
                .courseId(request.getCourseId())
                .orderCode(orderCode)
                .amount(request.getAmount())
                .paymentMethod(request.getPaymentMethod())
                .status(OrderStatus.PENDING)
                .build();

        Order savedOrder = orderRepository.save(order);

        String paymentUrl = PaymentConstant.VNPAY_SIMULATED_URL + orderCode + "&amount=" + request.getAmount();

        return OrderResponse.builder()
                .orderId(savedOrder.getId())
                .orderCode(savedOrder.getOrderCode())
                .totalAmount(savedOrder.getAmount())
                .status(savedOrder.getStatus())
                .paymentUrl(paymentUrl)
                .createdAt(savedOrder.getCreatedAt())
                .build();
    }

    @Override
    @Transactional
    public WebhookResponse processWebhook(PaymentWebhookRequest request) {
        Order order = orderRepository.findByOrderCode(request.getOrderCode())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng với mã: " + request.getOrderCode()));

        if (order.getStatus() == OrderStatus.PAID) {
            return WebhookResponse.builder()
                    .orderCode(order.getOrderCode())
                    .status(order.getStatus())
                    .enrollmentTriggered(false)
                    .build();
        }

        if (PaymentConstant.SUCCESS_RESPONSE_CODE.equals(request.getResponseCode())) {
            order.setStatus(OrderStatus.PAID);
            order.setTransactionRef(request.getTransactionNo());
            orderRepository.save(order);

            // Giao tiếp liên dịch vụ (Microservices Communication): Gọi sang Enrollment Service để kích hoạt khóa học
            boolean enrollmentSuccess = callEnrollmentService(order.getUserId(), order.getCourseId());

            return WebhookResponse.builder()
                    .orderCode(order.getOrderCode())
                    .status(OrderStatus.PAID)
                    .enrollmentTriggered(enrollmentSuccess)
                    .build();
        } else {
            order.setStatus(OrderStatus.FAILED);
            orderRepository.save(order);

            throw new PaymentException("Giao dịch thanh toán thất bại với mã lỗi: " + request.getResponseCode());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDetailResponse getOrderDetail(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng với ID: " + orderId));

        return OrderDetailResponse.builder()
                .orderId(order.getId())
                .userId(order.getUserId())
                .courseId(order.getCourseId())
                .orderCode(order.getOrderCode())
                .amount(order.getAmount())
                .paymentMethod(order.getPaymentMethod())
                .status(order.getStatus())
                .transactionRef(order.getTransactionRef())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }

    private boolean callEnrollmentService(UUID userId, UUID courseId) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("userId", userId);
            payload.put("courseId", courseId);

            ResponseEntity<String> response = restTemplate.postForEntity(enrollmentServiceBaseUrl, payload, String.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception ex) {
            log.error("Không thể kết nối đến Enrollment Service để kích hoạt khóa học: {}", ex.getMessage());
            return false;
        }
    }
}
