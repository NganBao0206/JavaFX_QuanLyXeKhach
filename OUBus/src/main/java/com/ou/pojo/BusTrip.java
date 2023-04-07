/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ou.pojo;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 *
 * @author yuumm
 */
public class BusTrip {

    /**
     * @return the departureId
     */
    public int getDepartureId() {
        return departureId;
    }

    /**
     * @param departureId the departureId to set
     */
    public void setDepartureId(int departureId) {
        this.departureId = departureId;
    }

    /**
     * @return the destinationId
     */
    public int getDestinationId() {
        return destinationId;
    }

    /**
     * @param destinationId the destinationId to set
     */
    public void setDestinationId(int destinationId) {
        this.destinationId = destinationId;
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
     * @return the departureName
     */
    public String getDepartureName() {
        return departureName;
    }

    /**
     * @param departureName the departureName to set
     */
    public void setDepartureName(String departureName) {
        this.departureName = departureName;
    }

    /**
     * @return the destinationName
     */
    public String getDestinationName() {
        return destinationName;
    }

    /**
     * @param destinationName the destinationName to set
     */
    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    /**
     * @return the totalTime
     */
    public int getTotalTime() {
        return totalTime;
    }

    /**
     * @param totalTime the totalTime to set
     */
    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    /**
     * @return the departureTime
     */
    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    /**
     * @param departureTime the departureTime to set
     */
    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }


    /**
     * @return the surcharge
     */
    public double getSurcharge() {
        return surcharge;
    }

    /**
     * @param surcharge the surcharge to set
     */
    public void setSurcharge(double surcharge) {
        this.surcharge = surcharge;
    }

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
     * @return the routeId
     */
    public String getRouteId() {
        return routeId;
    }

    /**
     * @param routeId the routeId to set
     */
    public void setRouteId(String routeId) {
        this.routeId = routeId;
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
    private String routeId;
    private LocalDateTime departureTime;
    private int busId;
    private double surcharge;
    //Extra
    private int totalTime;
    private String departureName;
    private String destinationName;
    private int departureId;
    private int destinationId;
    private double price;
    {
        id = UUID.randomUUID().toString();
    }
    
    public BusTrip() {
    }
    
    public BusTrip(String routeId, LocalDateTime departureTime, int busId, double surcharge) {
        this.routeId = routeId;
        this.departureTime = departureTime;
        this.busId = busId;
        this.surcharge = surcharge;
    }
    
    public BusTrip(String id, String routeId, LocalDateTime departureTime, int busId, double surcharge, int totalTime) {
        this.id = id;
        this.routeId = routeId;
        this.departureTime = departureTime;
        this.busId = busId;
        this.surcharge = surcharge;
        this.totalTime = totalTime;
    }
    
    public BusTrip(String routeId, LocalDateTime departureTime, int busId, double surcharge, int totalTime) {
        this.routeId = routeId;
        this.departureTime = departureTime;
        this.busId = busId;
        this.surcharge = surcharge;
        this.totalTime = totalTime;
    }
    
    public BusTrip(String id, String routeId, LocalDateTime departureTime, int busId, double price, double surcharge, int totalTime, String departureName, String destinationName) {
        this.id = id;
        this.routeId = routeId;
        this.departureTime = departureTime;
        this.busId = busId;
        this.price = price;
        this.surcharge = surcharge;
        this.totalTime = totalTime;
        this.departureName = departureName;
        this.destinationName = destinationName;
    }
    
    public BusTrip(String id, String routeId, LocalDateTime departureTime, int busId, double surcharge, int totalTime, int departureId, int destinationId, String departureName, String destinationName) {
        this.id = id;
        this.routeId = routeId;
        this.departureTime = departureTime;
        this.busId = busId;
        this.surcharge = surcharge;
        this.totalTime = totalTime;
        this.departureId = departureId;
        this.destinationId = destinationId;
        this.departureName = departureName;
        this.destinationName = destinationName;
    }
}
