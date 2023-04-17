/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ou.utils;

import com.ou.pojo.BusTrip;
import com.ou.pojo.Route;

import java.time.LocalDateTime;

/**
 *
 * @author yuumm
 */
public class ValidResult {
    private final int result; //-1 fail //0 question //1 success
    private final String failureReason;
    private final BusTrip trip;
    private final Route midRoute;
    private final LocalDateTime minDate;

    public ValidResult(int result, String failureReason, BusTrip trip, Route midRoute, LocalDateTime minDate) {
        this.result = result;
        this.failureReason = failureReason;
        this.trip = trip;
        this.midRoute = midRoute;
        this.minDate = minDate;
    }

    public int getResult() {
        return result;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public BusTrip getTrip() {
        if (trip == null)
            return new BusTrip("-1", "-1", LocalDateTime.MIN, -1, 0, 0, -1, -1, "", "");
        return trip;
    }

    public Route getMidRoute() {
        return midRoute;
    }

    public LocalDateTime getMinDate() {
        return minDate;
    }

}