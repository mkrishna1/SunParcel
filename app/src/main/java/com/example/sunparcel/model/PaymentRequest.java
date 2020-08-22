package com.example.sunparcel.model;

import com.google.gson.annotations.SerializedName;

public class PaymentRequest {

    @SerializedName("detail")
    private String paymentDetail;
    @SerializedName("amount")
    private String paymentAmount;
    @SerializedName("order_id")
    private String paymentOrderId;
    @SerializedName("hash")
    private String paymentHash;
    @SerializedName("name")
    private String paymentUserName;
    @SerializedName("email")
    private String paymentUserEmail;
    @SerializedName("phone")
    private String paymentUserMobile;

    public PaymentRequest(String paymentDetail, String paymentAmount, String paymentOrderId, String paymentHash, String paymentUserName, String paymentUserEmail, String paymentUserMobile) {
        this.paymentDetail = paymentDetail;
        this.paymentAmount = paymentAmount;
        this.paymentOrderId = paymentOrderId;
        this.paymentHash = paymentHash;
        this.paymentUserName = paymentUserName;
        this.paymentUserEmail = paymentUserEmail;
        this.paymentUserMobile = paymentUserMobile;
    }

    public String getPaymentDetail() {
        return paymentDetail;
    }

    public String getPaymentAmount() {
        return paymentAmount;
    }

    public String getPaymentOrderId() {
        return paymentOrderId;
    }

    public String getPaymentHash() {
        return paymentHash;
    }

    public String getPaymentUserName() {
        return paymentUserName;
    }

    public String getPaymentUserEmail() {
        return paymentUserEmail;
    }

    public String getPaymentUserMobile() {
        return paymentUserMobile;
    }
}
