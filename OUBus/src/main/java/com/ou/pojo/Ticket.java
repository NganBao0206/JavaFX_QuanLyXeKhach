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
public class Ticket {

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
     * @return the customerId
     */
    public String getCustomerId() {
        return customerId;
    }

    /**
     * @param customerId the customerId to set
     */
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    /**
     * @return the busTripId
     */
    public String getBusTripId() {
        return busTripId;
    }

    /**
     * @param busTripId the busTripId to set
     */
    public void setBusTripId(String busTripId) {
        this.busTripId = busTripId;
    }

    /**
     * @return the seatId
     */
    public int getSeatId() {
        return seatId;
    }

    /**
     * @param seatId the seatId to set
     */
    public void setSeatId(int seatId) {
        this.seatId = seatId;
    }

    /**
     * @return the staffId
     */
    public String getStaffId() {
        return staffId;
    }

    /**
     * @param staffId the staffId to set
     */
    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }
    private String id;
    private String customerId;
    private String busTripId;
    private int seatId;
    private String staffId;
    private String status;
    {
        setId(UUID.randomUUID().toString());
    }
    
    public Ticket() {
    }
    
    public Ticket(String customerId, String busTripId, int seatId, String staffId, String status)
    {
        this.customerId = customerId;
        this.busTripId = busTripId;
        this.seatId = seatId;
        this.staffId = staffId;
        this.status = status;
    }
}
