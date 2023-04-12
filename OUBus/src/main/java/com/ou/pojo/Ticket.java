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
public class Ticket {

    /**
     * @return the Time
     */
    public LocalDateTime getTime() {
        return time;
    }

    /**
     * @param time
     */
    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    /**
     * @return the DepartureTime
     */
    public LocalDateTime getDepartureTime() {
        return DepartureTime;
    }

    /**
     * @param DepartureTime the DepartureTime to set
     */
    public void setDepartureTime(LocalDateTime DepartureTime) {
        this.DepartureTime = DepartureTime;
    }

    /**
     * @return the ticketPrice
     */
    public double getTicketPrice() {
        return ticketPrice;
    }

    /**
     * @param ticketPrice the ticketPrice to set
     */
    public void setTicketPrice(double ticketPrice) {
        this.ticketPrice = ticketPrice;
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
     * @return the cusPhone
     */
    public String getCusPhone() {
        return cusPhone;
    }

    /**
     * @param cusPhone the cusPhone to set
     */
    public void setCusPhone(String cusPhone) {
        this.cusPhone = cusPhone;
    }

    /**
     * @return the LicensePlates
     */
    public String getLicensePlates() {
        return LicensePlates;
    }

    /**
     * @param LicensePlates the LicensePlates to set
     */
    public void setLicensePlates(String LicensePlates) {
        this.LicensePlates = LicensePlates;
    }

    /**
     * @return the DepartureName
     */
    public String getDepartureName() {
        return DepartureName;
    }

    /**
     * @param DepartureName the DepartureName to set
     */
    public void setDepartureName(String DepartureName) {
        this.DepartureName = DepartureName;
    }

    /**
     * @return the DestinationName
     */
    public String getDestinationName() {
        return DestinationName;
    }

    /**
     * @param DestinationName the DestinationName to set
     */
    public void setDestinationName(String DestinationName) {
        this.DestinationName = DestinationName;
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
     * @return the cusName
     */
    public String getCusName() {
        return cusName;
    }

    /**
     * @param cusName the cusName to set
     */
    public void setCusName(String cusName) {
        this.cusName = cusName;
    }

    /**
     * @return the staffName
     */
    public String getStaffName() {
        return staffName;
    }

    /**
     * @param staffName the staffName to set
     */
    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    /**
     * @return the seatName
     */
    public String getSeatName() {
        return seatName;
    }

    /**
     * @param seatName the seatName to set
     */
    public void setSeatName(String seatName) {
        this.seatName = seatName;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
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
    private double ticketPrice;
    private LocalDateTime time;
    
    //EXTRA
    private String cusName;
    private String staffName;
    private String seatName;
    private String cusPhone;
    private String LicensePlates;
    private String DepartureName;
    private String DestinationName;
    private double price;
    private double surcharge;
    private LocalDateTime DepartureTime;

    {
        setId(UUID.randomUUID().toString());
    }

    public Ticket() {
    }

    public Ticket(String id, String customerId, String busTripId, int seatId, String staffId, String status, double ticketPrice) {
        this.id = id;
        this.customerId = customerId;
        this.busTripId = busTripId;
        this.seatId = seatId;
        this.staffId = staffId;
        this.status = status;
        this.ticketPrice = ticketPrice;
    }

    public Ticket(String customerId, String busTripId, int seatId, String staffId, String status, double ticketPrice ) {
        this.customerId = customerId;
        this.busTripId = busTripId;
        this.seatId = seatId;
        this.staffId = staffId;
        this.status = status;
        this.ticketPrice = ticketPrice;
    }

    public Ticket(String id, String customerId, String busTripId, int seatId, String staffId, String status, String staffName, String seatName, String cusName, String cusPhone,String LicensePlates, String DepartureName, String DestinationName, double ticketPrice, LocalDateTime departureTime) {
        this.id = id;
        this.customerId = customerId;
        this.busTripId = busTripId;
        this.seatId = seatId;
        this.staffId = staffId;
        this.status = status;
        this.cusName = cusName;
        this.staffName = staffName;
        this.seatName = seatName;
        this.cusPhone = cusPhone;
        this.LicensePlates = LicensePlates;
        this.DepartureName = DepartureName;
        this.DestinationName = DestinationName;
        this.ticketPrice = ticketPrice;
        this.DepartureTime = departureTime;
    }
}
