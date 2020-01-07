package com.skyracle.QuickParcels.Model;

public class CouponModelClass {

    String id;
    String price ;
    String lastDate;
    String minimum;
    String coupontype;
    String couponname;
    String time;

    public CouponModelClass(String id, String price, String lastDate, String minimum, String coupontype, String couponname, String time) {
        this.id = id;
        this.price = price;
        this.lastDate = lastDate;
        this.minimum = minimum;
        this.coupontype = coupontype;
        this.couponname = couponname;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getLastDate() {
        return lastDate;
    }

    public void setLastDate(String lastDate) {
        this.lastDate = lastDate;
    }

    public String getMinimum() {
        return minimum;
    }

    public void setMinimum(String minimum) {
        this.minimum = minimum;
    }

    public String getCoupontype() {
        return coupontype;
    }

    public void setCoupontype(String coupontype) {
        this.coupontype = coupontype;
    }

    public String getCouponname() {
        return couponname;
    }

    public void setCouponname(String couponname) {
        this.couponname = couponname;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
