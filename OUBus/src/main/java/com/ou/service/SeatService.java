/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ou.service;

import com.ou.conf.JdbcUtils;
import com.ou.pojo.Seat;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Hi
 */
public class SeatService {
    public List<Seat> getSeats() throws SQLException
    {
        List<Seat> seats = new ArrayList<>();
        try (Connection conn = JdbcUtils.getConn())
        {
            String sql = "SELECT * FROM seat";
            PreparedStatement stm = conn.prepareCall(sql);
            ResultSet rs = stm.executeQuery(); 
            while (rs.next()) {
                int id = rs.getInt("ID");
                String name = rs.getNString("Name");
                seats.add(new Seat(id, name));
            }
        }
        return seats;
    }
    
    public Seat getSeat(String name) throws SQLException
    {
        try (Connection conn = JdbcUtils.getConn())
        {
            String sql = "SELECT ID FROM seat WHERE name = ?";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setString(1, name);
            ResultSet rs = stm.executeQuery(); 
            if (rs.next()) {
                int id = rs.getInt("ID");
                return (new Seat(id, name));
            }
        }
        return null;
    }
    
    public Seat getSeat(int id) throws SQLException
    {
        try (Connection conn = JdbcUtils.getConn())
        {
            String sql = "SELECT name FROM seat WHERE id = ?";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery(); 
            if (rs.next()) {
                String name = rs.getString("name");
                return (new Seat(id, name));
            }
        }
        return null;
    }
}
