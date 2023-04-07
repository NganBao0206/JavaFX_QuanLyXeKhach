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

public class User {
    private String id;
    private String username;
    private String password;
    private String name;
    private String userRole;
    
    {
        id = UUID.randomUUID().toString();
    }
    public User(String username, String password, String name, String userRole) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.userRole = userRole;
    }
    
    public User(String id, String username, String password, String name, String userRole) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.userRole = userRole;
    }
    
    public User () {
    }
    
    @Override
    public String toString() {
        return this.getUsername();
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
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the userRole
     */
    public String getUserRole() {
        return userRole;
    }

    /**
     * @param userRole the userRole to set
     */
        public void setUserRole(String userRole) {
        this.userRole = userRole;
    }
    
}
