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
    private String id;
    private int departureId;
    private int destinationId;
    private double price;
    
    {
        id = UUID.randomUUID().toString();
    }
    
    public Route() {
        
    }
    
    public Route(int departureId, int destinationId, double price) {
        this.departureId = departureId;
        this.destinationId = destinationId;
        this.price = price;
    }
}
