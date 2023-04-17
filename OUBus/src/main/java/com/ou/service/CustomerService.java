/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ou.service;

import com.ou.conf.JdbcUtils;
import com.ou.pojo.Customer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Hi
 */
public class CustomerService {
    public List<Customer> getCustomers() throws SQLException {

        List<Customer> customers = new ArrayList<>();
        try (Connection conn = JdbcUtils.getConn()) {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM customer");
            while (rs.next()) {
                String id = rs.getNString("ID");
                String name = rs.getNString("Name");
                String phone = rs.getNString("Phone");
                
                customers.add(new Customer(id, name, phone));
            }
        }

        return customers;
    }
    
    
    public Customer getCustomer(String name, String phone) throws SQLException {

        try (Connection conn = JdbcUtils.getConn()) {
            String sql = "SELECT * FROM customer WHERE Name=? AND Phone=?";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setString(1, name);
            stm.setString(2, phone);
            ResultSet rs = stm.executeQuery(); 
            if (rs.next()) {
                String id = rs.getNString("ID");
                String n = rs.getNString("Name");
                String p = rs.getNString("Phone");               
                return new Customer(id, n, p);
            }
        }
        return null;
    }
    
    public Customer getCustomer(String id) throws SQLException {

        try (Connection conn = JdbcUtils.getConn()) {
            String sql = "SELECT * FROM customer WHERE ID=?";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setString(1, id);
            ResultSet rs = stm.executeQuery(); 
            if (rs.next()) {
                String n = rs.getNString("Name");
                String p = rs.getNString("Phone");               
                return new Customer(id, n, p);
            }
        }
        return null;
    }
    
    public boolean addCustomer(Customer c) throws SQLException {

        try( Connection conn = JdbcUtils.getConn()) {
            conn.setAutoCommit(false);
            String sql = "INSERT INTO customer(id, name, phone) "
                    + "VALUES(?, ?, ?)";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setString(1, c.getId());
            stm.setString(2, c.getName());
            stm.setString(3, c.getPhone());
            stm.executeUpdate();
            try {
                conn.commit();
                return true;
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
                return false;
            }
        }
    }
    
    public boolean deleteCustomer(String customerId) throws SQLException {
        try (Connection conn = JdbcUtils.getConn()) {
            String sql = "DELETE FROM customer WHERE id=?";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setString(1, customerId);
            return stm.executeUpdate() > 0;
        }
    }
}
