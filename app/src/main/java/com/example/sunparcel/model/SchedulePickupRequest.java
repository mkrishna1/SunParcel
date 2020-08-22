package com.example.sunparcel.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SchedulePickupRequest {

    @SerializedName("userid")
    int userId;

    @SerializedName("shipper")
    List<ShipperDetailsRequest> shipperDetailsRequestList;

    @SerializedName("receiver")
    List<ShipperDetailsRequest> receiverDetailsRequestList;

    @SerializedName("service")
    List<ShipperDetailsRequest> serviceDetailsRequestList;

    @SerializedName("instruction")
    String instruction;


    public SchedulePickupRequest(int userId, List<ShipperDetailsRequest> shipperDetailsRequestList, List<ShipperDetailsRequest> receiverDetailsRequestList, List<ShipperDetailsRequest> serviceDetailsRequestList, String instruction) {
        this.userId = userId;
        this.shipperDetailsRequestList = shipperDetailsRequestList;
        this.receiverDetailsRequestList = receiverDetailsRequestList;
        this.serviceDetailsRequestList = serviceDetailsRequestList;
        this.instruction = instruction;
    }

    public int getUserId() {
        return userId;
    }

    public List<ShipperDetailsRequest> getShipperDetailsRequestList() {
        return shipperDetailsRequestList;
    }

    public List<ShipperDetailsRequest> getReceiverDetailsRequestList() {
        return receiverDetailsRequestList;
    }

    public List<ShipperDetailsRequest> getServiceDetailsRequestList() {
        return serviceDetailsRequestList;
    }

    public String getInstruction() {
        return instruction;
    }
}

