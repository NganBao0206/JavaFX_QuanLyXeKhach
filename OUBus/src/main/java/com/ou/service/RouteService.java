/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ou.service;

import com.ou.conf.JdbcUtils;
import com.ou.pojo.Location;
import com.ou.pojo.Route;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author yuumm
 */
public class RouteService {

    
    public List<Route> getRoutes(String Departure, String Destination) throws SQLException {
        List<Route> routes = new ArrayList<>();
        try (Connection conn = JdbcUtils.getConn()) {
            String sql = "SELECT * FROM route r, location l1, location l2 WHERE r.DepartureID = l1.ID AND r.DestinationID = l2.ID";
            if (Departure != null && !Departure.isEmpty()) {
                sql += " AND l1.Name like concat('%', ?, '%')";
            }
            if (Destination != null && !Destination.isEmpty()) {
                sql += " AND l2.Name like concat('%', ?, '%')";
            }
            PreparedStatement stm = conn.prepareCall(sql);
            if (Destination != null && !Destination.isEmpty() && Departure != null && !Departure.isEmpty()) {
                stm.setString(2, Destination);
            }
            if (Departure != null && !Departure.isEmpty()) {
                stm.setString(1, Departure);
            } else if (Destination != null && !Destination.isEmpty()) {
                stm.setString(1, Destination);
            }
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                String id = rs.getString("ID");
                int departureId = rs.getInt("DepartureID");
                int destinationId = rs.getInt("DestinationID");
                double price = rs.getDouble("Price");
                String departureName = rs.getNString("l1.Name");
                String destinationName = rs.getNString("l2.Name");
                int totalTime = rs.getInt("TotalTime");
                routes.add(new Route(id, departureId, destinationId, price, departureName, destinationName, totalTime));
            }
        }
        return routes;
    }

    public Route getRoute(String Departure, String Destination) throws SQLException {
        try (Connection conn = JdbcUtils.getConn()) {
            String sql = "SELECT * FROM route r, location l1, location l2 WHERE r.DepartureID = l1.ID AND r.DestinationID = l2.ID";
            sql += " AND l1.Name like concat('%', ?, '%')";
            sql += " AND l2.Name like concat('%', ?, '%')";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setString(1, Departure);
            stm.setString(2, Destination);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                
                String id = rs.getString("ID");
                int departureId = rs.getInt("DepartureID");
                int destinationId = rs.getInt("DestinationID");
                double price = rs.getDouble("Price");
                String departureName = rs.getNString("l1.Name");
                String destinationName = rs.getNString("l2.Name");
                int totalTime = rs.getInt("TotalTime");
                return new Route(id, departureId, destinationId, price, departureName, destinationName, totalTime);
            }
        }
        return null;
    }
}
