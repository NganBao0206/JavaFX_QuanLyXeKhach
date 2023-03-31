/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ou.service;

import com.ou.conf.JdbcUtils;
import com.ou.pojo.Location;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author yuumm
 */
public class LocationService {
    public List<Location> getLocations(String keyword) throws SQLException {
        List<Location> locations = new ArrayList<>();
        try (Connection conn = JdbcUtils.getConn()) {
            String sql = "SELECT * FROM location";
             if (keyword != null && !keyword.isEmpty()) 
                sql += " WHERE Name like concat('%', ?, '%')";
            PreparedStatement stm = conn.prepareCall(sql);
            if (keyword != null && !keyword.isEmpty()) 
                stm.setString(1, keyword);
            System.out.println(sql);
            ResultSet rs = stm.executeQuery(); 
            while (rs.next()) {
                int id = rs.getInt("ID");
                String name = rs.getNString("Name");
                locations.add(new Location(id, name));
            }
        }
        return locations;
    }
    
    
    public boolean addLocation(Location location) throws SQLException {
        try( Connection conn = JdbcUtils.getConn()) {
            conn.setAutoCommit(false);
            String sql = "INSERT INTO location(id, name) "
                    + "VALUES(?, ?)";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setInt(1, location.getId());
            stm.setString(2, location.getName());
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
    
    public boolean editLocation(Location location) throws SQLException {
        try( Connection conn = JdbcUtils.getConn()) {
           String sql = "UPDATE location SET Name = ? WHERE ID = ?";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setString(1, location.getName());
            stm.setInt(2, location.getId());
            int rowsUpdated = stm.executeUpdate(); 
            return rowsUpdated > 0;
        }
    }
}

