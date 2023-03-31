/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ou.pojo;

import java.sql.Time;
import java.util.UUID;

/**
 *
 * @author yuumm
 */
public class BusTrip {

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the routeID
     */
    public String getRouteID() {
        return routeID;
    }

    /**
     * @param routeID the routeID to set
     */
    public void setRouteID(String routeID) {
        this.routeID = routeID;
    }

    /**
     * @return the departureTime
     */
    public Time getDepartureTime() {
        return departureTime;
    }

    /**
     * @param departureTime the departureTime to set
     */
    public void setDepartureTime(Time departureTime) {
        this.departureTime = departureTime;
    }

    /**
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * @return the busId
     */
    public int getBusId() {
        return busId;
    }

    /**
     * @param busId the busId to set
     */
    public void setBusId(int busId) {
        this.busId = busId;
    }
    private String id;
    private String routeID;
    private Time departureTime;
    private double price;
    private int busId;
    
    {
        id = UUID.randomUUID().toString();
    }
    
    public BusTrip() {
    }
    
    public BusTrip(String routeID, Time departureTime, double price, int busId) {
        this.routeID = routeID;
        this.departureTime = departureTime;
        this.price = price;
        this.busId = busId;
    }
}
