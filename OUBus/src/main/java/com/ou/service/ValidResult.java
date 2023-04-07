/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ou.service;

import com.ou.pojo.BusTrip;

/**
 *
 * @author yuumm
 */
public class ValidResult {
    private int result; //-1 fail //0 question //1 success
    private String failureReason;
    private BusTrip trip;

    public ValidResult(int result, String failureReason, BusTrip trip) {
        this.result = result;
        this.failureReason = failureReason;
        this.trip = trip;
    }

    public int getResult() {
        return result;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public BusTrip getTrip() {
        return trip;
    }
}
