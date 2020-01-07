package com.skyracle.QuickParcels.Model;

public class NewOrder {

    String date;
    String time;
    String pickup;
    String drop;
    String distance;
    String orderid;
    String weight;

    public NewOrder(String date, String time, String pickup, String drop, String distance, String orderid, String weight) {
        this.date = date;
        this.time = time;
        this.pickup = pickup;
        this.drop = drop;
        this.distance = distance;
        this.orderid = orderid;
        this.weight = weight;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPickup() {
        return pickup;
    }

    public void setPickup(String pickup) {
        this.pickup = pickup;
    }

    public String getDrop() {
        return drop;
    }

    public void setDrop(String drop) {
        this.drop = drop;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
}

