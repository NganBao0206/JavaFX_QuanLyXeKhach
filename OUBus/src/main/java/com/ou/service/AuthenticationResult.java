/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ou.service;

/**
 *
 * @author yuumm
 */
public class AuthenticationResult {
    private boolean success; 
    private String failureReason;
    private String userRole;

    public AuthenticationResult(boolean success, String failureReason, String userRole) {
        this.success = success;
        this.failureReason = failureReason;
        this.userRole = userRole;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public String getUserRole() {
        return userRole;
    }
}
