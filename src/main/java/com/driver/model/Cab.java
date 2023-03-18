package com.driver.model;

import javax.persistence.*;

@Entity
public class Cab {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;
    private int perKmRate;
    private boolean available;

    public Cab(){

    }
    public Cab(int id, int perKmRate, boolean available) {
        Id = id;
        this.perKmRate = perKmRate;
        this.available = available;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getPerKmRate() {
        return perKmRate;
    }

    public void setPerKmRate(int perKmRate) {
        this.perKmRate = perKmRate;
    }

    public boolean getAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
    @OneToOne(mappedBy = "cab", cascade = CascadeType.ALL)
    Driver driver;

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }
}