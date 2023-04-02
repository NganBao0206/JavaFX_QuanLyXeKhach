/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ou.pojo;

/**
 *
 * @author yuumm
 */
public class Bus {

    
    private int id;
    private String licensePlates;
    
    public Bus() {
    }
    
    public Bus(int id, String licensePlates)
    {
        this.id = id;
        this.licensePlates = licensePlates;
    }
    
    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the licensePlates
     */
    public String getLicensePlates() {
        return licensePlates;
    }

    /**
     * @param licensePlates the licensePlates to set
     */
    public void setLicensePlates(String licensePlates) {
        this.licensePlates = licensePlates;
    }

    @Override
    public String toString() {
        return "Xe số " + id + " (" + licensePlates + ")"; 
    }
}
