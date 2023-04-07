/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ou.pojo;

import java.util.UUID;

/**
 *
 * @author yuumm
 */
public class Route {

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

    private String id;
    private int departureId;
    private int destinationId;
    private double price;
    //Extra
    private String departureName;
    private String destinationName;
    private int totalTime;
    {
        id = UUID.randomUUID().toString();
    }
    
    public Route() {
        
    }
    
    public Route(int departureId, int destinationId, double price, int totalTime) {
        this.departureId = departureId;
        this.destinationId = destinationId;
        this.price = price;
        this.totalTime = totalTime;
    }
    
    public Route(String id, int departureId, int destinationId, double price, int totalTime) {
        this.id = id;
        this.departureId = departureId;
        this.destinationId = destinationId;
        this.price = price;
        this.totalTime = totalTime;
    }
    
    public Route(String id, int departureId, int destinationId, double price,String departureName,String destinationName, int totalTime) {
        this.id = id;
        this.departureId = departureId;
        this.destinationId = destinationId;
        this.price = price;
        this.departureName = departureName;
        this.destinationName = destinationName;
        this.totalTime = totalTime;
    }
    
    @Override
    public String toString() {
        return "Tuyến từ " + departureName + " đến " + destinationName;
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
}
