package com.glim.pay.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PayService {
    private static final Logger logger = LoggerFactory.getLogger(PayService.class);
//    private final PayConfig portOneConfig;
//    private final OrderService orderService;
//    private final RestTemplate restTemplate = new RestTemplate();
//
//    public Map<String, Object> verifyPayment(String impUid, String merchantUid, int expectedAmount) {
//        Order order = orderService.createPendingOrder(merchantUid, impUid, expectedAmount);
//
//        try {
//            // 1. 포트원 토큰 발급
//            String token = getPortOneToken();
//
//            // 2. 결제 정보 조회
//            String apiUrl = "https://api.iamport.kr/payments/" + impUid;
//            HttpHeaders headers = new HttpHeaders();
//            headers.set("Authorization", "Bearer " + token);
//            HttpEntity<String> entity = new HttpEntity<>(headers);
//
//            ResponseEntity<Map> response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, Map.class);
//            Map<String, Object> responseBody = response.getBody();
//
//            // 응답 본문 검증
//            if (responseBody == null || !responseBody.containsKey("response")) {
//                logger.error("결제 조회 응답이 비어있습니다. imp_uid: {}", impUid);
//                orderService.updateOrderStatus(order, "FAILED", "Empty response from PortOne API");
//                throw new PaymentException("결제 정보를 확인할 수 없습니다.", "Empty response from PortOne API");
//            }
//
//            Map<String, Object> responseData = (Map<String, Object>) responseBody.get("response");
//            if (responseData == null) {
//                logger.error("결제 데이터가 존재하지 않습니다. imp_uid: {}", impUid);
//                orderService.updateOrderStatus(order, "FAILED", "Response data is null");
//                throw new PaymentException("결제 데이터가 유효하지 않습니다.", "Response data is null");
//            }
//
//            // 3. 결제 검증
//            String status = (String) responseData.get("status");
//            if (status != null && "paid".equals(status)) {
//                Object amountObj = responseData.get("amount");
//                if (amountObj == null) {
//                    logger.error("결제 금액 정보가 누락되었습니다. imp_uid: {}", impUid);
//                    orderService.updateOrderStatus(order, "FAILED", "Amount is missing in response");
//                    throw new PaymentException("결제 금액 정보를 찾을 수 없습니다.", "Amount is missing in response");
//                }
//
//                int paidAmount = ((Number) amountObj).intValue();
//                String orderMerchantUid = (String) responseData.get("merchant_uid");
//
//                if (orderMerchantUid == null || !orderMerchantUid.equals(merchantUid)) {
//                    logger.warn("주문 번호 불일치. 기대: {}, 실제: {}", merchantUid, orderMerchantUid);
//                    orderService.updateOrderStatus(order, "FAILED", "Merchant UID mismatch");
//                    throw new PaymentException("주문 번호가 일치하지 않습니다.", "Merchant UID mismatch");
//                }
//
//                if (paidAmount != expectedAmount) {
//                    logger.warn("결제 금액 불일치. 기대: {}, 실제: {}", expectedAmount, paidAmount);
//                    orderService.updateOrderStatus(order, "FAILED", "Amount mismatch");
//                    throw new PaymentException("결제 금액이 일치하지 않습니다.", "Amount mismatch");
//                }
//
//                orderService.updateOrderStatus(order, "SUCCESS", null);
//                logger.info("결제 성공. imp_uid: {}, merchant_uid: {}", impUid, merchantUid);
//                return Map.of("message", "결제 성공", "data", responseData);
//            } else {
//                logger.error("결제 상태 비정상. 상태: {}, imp_uid: {}", status, impUid);
//                orderService.updateOrderStatus(order, "FAILED", "Invalid payment status: " + status);
//                throw new PaymentException("결제 처리에 실패했습니다. 다시 시도해주세요.", "Invalid payment status: " + status);
//            }
//        } catch (HttpClientErrorException e) {
//            logger.error("포트원 API 호출 실패. imp_uid: {}, 오류: {}", impUid, e.getMessage());
//            orderService.updateOrderStatus(order, "FAILED", "HTTP error: " + e.getMessage());
//            throw new PaymentException("결제 서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.", "HTTP error: " + e.getMessage());
//        } catch (Exception e) {
//            logger.error("결제 검증 중 예기치 않은 오류. imp_uid: {}, 오류: {}", impUid, e.getMessage(), e);
//            orderService.updateOrderStatus(order, "FAILED", "Unexpected error: " + e.getMessage());
//            throw new PaymentException("결제 처리 중 오류가 발생했습니다. 고객 지원팀에 문의해주세요.", "Unexpected error: " + e.getMessage());
//        }
//    }
//
//    private String getPortOneToken() {
//        try {
//            String apiUrl = "https://api.iamport.kr/users/getToken";
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_JSON);
//
//            Map<String, String> body = Map.of(
//                    "imp_key", portOneConfig.getApiKey(),
//                    "imp_secret", portOneConfig.getApiSecret()
//            );
//
//            HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);
//            ResponseEntity<Map> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, Map.class);
//            Map<String, Object> responseBody = response.getBody();
//
//            if (responseBody == null || !responseBody.containsKey("response")) {
//                logger.error("토큰 발급 응답이 비어있습니다.");
//                throw new PaymentException("결제 서버 연결에 실패했습니다.", "Empty token response");
//            }
//
//            Map<String, String> responseData = (Map<String, String>) responseBody.get("response");
//            return responseData.get("access_token");
//        } catch (Exception e) {
//            logger.error("토큰 발급 실패: {}", e.getMessage(), e);
//            throw new PaymentException("결제 서버 연결에 실패했습니다.", "Token retrieval error: " + e.getMessage());
//        }
//    }
}