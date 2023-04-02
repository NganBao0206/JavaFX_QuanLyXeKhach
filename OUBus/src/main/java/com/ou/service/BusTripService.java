/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ou.service;

import com.ou.conf.JdbcUtils;
import com.ou.pojo.BusTrip;
import com.ou.pojo.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author yuumm
 */
public class BusTripService {

    public boolean addBusTrip(BusTrip busTrip) throws SQLException {
        try (Connection conn = JdbcUtils.getConn()) {
            conn.setAutoCommit(false);
            String sql = "INSERT INTO bustrip(id, routeId, departureTime, busId, surcharge) "
                    + "VALUES(?, ?, ?, ?, ?)";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setString(1, busTrip.getId());
            stm.setString(2, busTrip.getRouteId());
            stm.setObject(3, busTrip.getDepartureTime());
            stm.setInt(4, busTrip.getBusId());
            stm.setDouble(5, busTrip.getSurcharge() / 1000);
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

    public boolean editBusTrip(BusTrip busTrip) throws SQLException {
        try (Connection conn = JdbcUtils.getConn()) {
            conn.setAutoCommit(false);
            String sql = "UPDATE bustrip SET routeId = ?, departureTime = ?, busId = ?, surcharge = ? WHERE id = ?";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setString(1, busTrip.getRouteId());
            stm.setObject(2, busTrip.getDepartureTime());
            stm.setInt(3, busTrip.getBusId());
            stm.setDouble(4, busTrip.getSurcharge() / 1000);
            stm.setString(5, busTrip.getId());
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

    public boolean isValidBusTrip(BusTrip busTrip) throws SQLException {
        try (Connection conn = JdbcUtils.getConn()) {
            String sql = "SELECT COUNT(*) FROM bustrip b, route r WHERE b.ID != ? AND b.RouteId = r.ID AND busId = ? AND ((departureTime BETWEEN ? AND ?) OR ((DATE_ADD(departureTime, INTERVAL totalTime MINUTE) BETWEEN ? AND ?)))";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setString(1, busTrip.getId());
            stm.setInt(2, busTrip.getBusId());
            stm.setObject(3, busTrip.getDepartureTime());
            stm.setObject(4, busTrip.getDepartureTime().plusMinutes(busTrip.getTotalTime()));
            stm.setObject(5, busTrip.getDepartureTime());
            stm.setObject(6, busTrip.getDepartureTime().plusMinutes(busTrip.getTotalTime()));
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count == 0;
            }
        }
        return false;
    }

    public List<BusTrip> getBusTrips(LocalDate departureDate,int departureId,int destinationId) throws SQLException {
        List<BusTrip> busTrips = new ArrayList<>();
        try (Connection conn = JdbcUtils.getConn()) {
            String sql = "SELECT * FROM bustrip b, route r, location l1, location l2 WHERE b.RouteID = r.ID AND r.DepartureID = l1.ID AND r.DestinationID = l2.ID";
            if (departureDate != null) {
                sql += " AND DATE(b.DepartureTime) = ?";
            }
            if (departureId != -1) {
                sql += " AND r.DepartureID = ?";
            }
            if (destinationId != -1) {
                sql += " AND r.DestinationID = ?";
            }
            PreparedStatement stm = conn.prepareCall(sql);
            int index = 1;
            if (departureDate != null) {
                stm.setObject(index++, departureDate);
            }
            if (departureId != -1) {
                stm.setInt(index++, departureId);
            }
            if (destinationId != -1) {
                stm.setInt(index++, destinationId);
            }

            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                String id = rs.getNString("ID");
                String routeId = rs.getNString("RouteID");
                LocalDateTime departureTime = (LocalDateTime) rs.getObject("DepartureTime");
                int busId = rs.getInt("BusID");
                double price = rs.getDouble("Price");
                double surcharge = rs.getDouble("Surcharge");
                int totalTime = rs.getInt("TotalTime");
                String departureName = rs.getNString("l1.Name");
                String destinationName = rs.getNString("l2.Name");

                busTrips.add(new BusTrip(id, routeId, departureTime, busId, price, surcharge, totalTime, departureName, destinationName));
            }
        }
        return busTrips;
    }

    public boolean deleteBusTrip(String id) throws SQLException {
        try (Connection conn = JdbcUtils.getConn()) {
            String sql = "DELETE FROM bustrip WHERE id=?";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setString(1, id);

            return stm.executeUpdate() > 0;
        }
    }

}
