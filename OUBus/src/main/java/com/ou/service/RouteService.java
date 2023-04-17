/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ou.service;

import com.ou.conf.JdbcUtils;
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
    
    public Route getRoute(String exceptId, int departureId, int destinationId) throws SQLException {
        try (Connection conn = JdbcUtils.getConn()) {
            String sql = "SELECT * FROM route WHERE ID != ? AND DepartureID = ? AND DestinationID = ?";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setString(1, exceptId);
            stm.setInt(2, departureId);
            stm.setInt(3, destinationId);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                
                String id = rs.getString("ID");
                int dprId = rs.getInt("DepartureID");
                int dstId = rs.getInt("DestinationID");
                double price = rs.getDouble("Price");
                int totalTime = rs.getInt("TotalTime");
                return new Route(id, dprId, dstId, price, totalTime);
            }
            else
            {
                return null;
            }
                
        }
    }
    
    public List<Route> getRoutes(int locationId) throws SQLException {
        List<Route> routes = new ArrayList<>();
        try (Connection conn = JdbcUtils.getConn()) {
            String sql = "SELECT * FROM route WHERE DepartureID = ? OR DestinationID = ?";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setInt(1, locationId);
            stm.setInt(2, locationId);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                
                String id = rs.getString("ID");
                int dprId = rs.getInt("DepartureID");
                int dstId = rs.getInt("DestinationID");
                double price = rs.getDouble("Price");
                int totalTime = rs.getInt("TotalTime");
                routes.add(new Route(id, dprId, dstId, price, totalTime));
            }
        }
        return routes;
    }
    
    public Route getRoute(String routeId) throws SQLException {
        try (Connection conn = JdbcUtils.getConn()) {
            String sql = "SELECT * FROM route r, location l1, location l2 WHERE r.ID = ? AND r.DepartureID = l1.ID AND r.DestinationID = l2.ID";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setString( 1, routeId);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                String id = rs.getString("ID");
                int dprId = rs.getInt("DepartureID");
                int dstId = rs.getInt("DestinationID");
                String departureName = rs.getNString("l1.Name");
                String destinationName = rs.getNString("l2.Name");
                double price = rs.getDouble("Price");
                int totalTime = rs.getInt("TotalTime");
                return new Route(id, dprId, dstId, price, departureName, destinationName, totalTime);
            }
        }
        return null;
    }
    
    public boolean addRoute(Route route) throws SQLException
    {
        try( Connection conn = JdbcUtils.getConn()) {
            conn.setAutoCommit(false);
            String sql = "INSERT INTO route(id, departureId, destinationId, price, totalTime) "
                    + "VALUES(?, ?, ? , ?, ?)";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setString(1, route.getId());
            stm.setInt(2, route.getDepartureId());
            stm.setInt(3, route.getDestinationId());
            stm.setDouble(4, route.getPrice());
            stm.setInt(5, route.getTotalTime());
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
    
    public boolean editRoute(Route route) throws SQLException {
        try( Connection conn = JdbcUtils.getConn()) {
           String sql = "UPDATE route SET DepartureID = ?, DestinationID = ?, Price = ?, TotalTime = ? WHERE ID = ?";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setInt(1, route.getDepartureId());
            stm.setInt(2, route.getDestinationId());
            stm.setDouble(3, route.getPrice());
            stm.setInt(4, route.getTotalTime());
            stm.setString(5, route.getId());
            int rowsUpdated = stm.executeUpdate(); 
            return rowsUpdated > 0;
        }
    }
    
    public boolean deleteRoute(String routeId) throws SQLException {
        try (Connection conn = JdbcUtils.getConn()) {
            String sql = "DELETE FROM route WHERE id=?";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setString(1, routeId);

            return stm.executeUpdate() > 0;
        }
    
    }
}
