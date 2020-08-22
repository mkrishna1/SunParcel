package com.example.sunparcel.model;

import com.google.gson.annotations.SerializedName;

public class ShipperDetailsRequest {

    @SerializedName("companyName")
    String shipperCompanyName;
    @SerializedName("address")
    String shipperAddress;
    @SerializedName("contactName")
    String shipperContactName;
    @SerializedName("contactNumber")
    String shipperContactNumber;
    @SerializedName("email")
    String shipperContactEmail;

    @SerializedName("name")
    String shipperServiceName;
    @SerializedName("item")
    String shipperItem;
    @SerializedName("weight")
    String shipperWeight;

    public ShipperDetailsRequest(String shipperCompanyName, String shipperAddress, String shipperContactName, String shipperContactNumber, String shipperContactEmail) {
        this.shipperCompanyName = shipperCompanyName;
        this.shipperAddress = shipperAddress;
        this.shipperContactName = shipperContactName;
        this.shipperContactNumber = shipperContactNumber;
        this.shipperContactEmail = shipperContactEmail;
    }

    public ShipperDetailsRequest(String shipperServiceName, String shipperItem, String shipperWeight) {
        this.shipperServiceName = shipperServiceName;
        this.shipperItem = shipperItem;
        this.shipperWeight = shipperWeight;
    }

    public String getShipperServiceName() {
        return shipperServiceName;
    }

    public String getShipperItem() {
        return shipperItem;
    }

    public String getShipperWeight() {
        return shipperWeight;
    }

    public String getShipperCompanyName() {
        return shipperCompanyName;
    }

    public String getShipperAddress() {
        return shipperAddress;
    }

    public String getShipperContactName() {
        return shipperContactName;
    }

    public String getShipperContactNumber() {
        return shipperContactNumber;
    }

    public String getShipperContactEmail() {
        return shipperContactEmail;
    }
}
