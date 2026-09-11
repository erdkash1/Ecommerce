package com.iggy.ecommerce.dto;

public class PaymentRequest {
    private Long OrderId;
    private Long UserId;


    public Long getOrderId() {
        return OrderId;}

    public void setOrderId(Long orderId) {
        OrderId = orderId;}

    public Long getUserId() {
        return UserId;}

    public void setUserId(Long userId) {
        UserId = userId;}


}
