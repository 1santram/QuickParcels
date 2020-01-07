package com.skyracle.QuickParcels.Model;

public class DropAddress {

    String username;
    String usermobile;
    String flat;
    String street;
    String nearby;
    String city;
    String state;
    String addresstype;
    String pincode;
    String addressid;
    String pickup;

    public DropAddress(String username, String usermobile, String flat, String street, String nearby, String city, String state, String addresstype, String pincode, String addressid, String pickup) {
        this.username = username;
        this.usermobile = usermobile;
        this.flat = flat;
        this.street = street;
        this.nearby = nearby;
        this.city = city;
        this.state = state;
        this.addresstype = addresstype;
        this.pincode = pincode;
        this.addressid = addressid;
        this.pickup = pickup;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsermobile() {
        return usermobile;
    }

    public void setUsermobile(String usermobile) {
        this.usermobile = usermobile;
    }

    public String getFlat() {
        return flat;
    }

    public void setFlat(String flat) {
        this.flat = flat;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNearby() {
        return nearby;
    }

    public void setNearby(String nearby) {
        this.nearby = nearby;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAddresstype() {
        return addresstype;
    }

    public void setAddresstype(String addresstype) {
        this.addresstype = addresstype;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getAddressid() {
        return addressid;
    }

    public void setAddressid(String addressid) {
        this.addressid = addressid;
    }

    public String getPickup() {
        return pickup;
    }

    public void setPickup(String pickup) {
        this.pickup = pickup;
    }
}

