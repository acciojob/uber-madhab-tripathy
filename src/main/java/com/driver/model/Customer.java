package com.driver.model;

import java.util.List;
import java.util.ArrayList;

import javax.persistence.*;

@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int customerId;
    private String mobile;
    private String password;
    private Customer(){

    }
    public Customer(int customerId, String mobile, String password) {
        this.customerId = customerId;
        this.mobile = mobile;
        this.password = password;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    List<TripBooking> tripBookingList = new ArrayList<>();

    public List<TripBooking> getTripBookingList() {
        return tripBookingList;
    }

    public void setTripBookingList(List<TripBooking> tripBookingList) {
        this.tripBookingList = tripBookingList;
    }
}