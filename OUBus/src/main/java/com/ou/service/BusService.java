/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ou.service;

import com.ou.conf.JdbcUtils;
import com.ou.pojo.Bus;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author yuumm
 */
public class BusService {
     public List<Bus> getBuses() throws SQLException {
        
        List<Bus> buses = new ArrayList<>();
        try (Connection conn = JdbcUtils.getConn()) {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM bus");
            while (rs.next()) {
                int id = rs.getInt("ID");
                String licensePlates = rs.getNString("LicensePlates");
                buses.add(new Bus(id, licensePlates));
            }
        }

        return buses;
    }
}
