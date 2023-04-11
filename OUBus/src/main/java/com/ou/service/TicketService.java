/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ou.service;

import com.ou.conf.JdbcUtils;
import com.ou.pojo.Ticket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Hi
 */
public class TicketService {
    public boolean addTicket(Ticket t) throws SQLException {
        try (Connection conn = JdbcUtils.getConn()) {
            conn.setAutoCommit(false);
            String sql = "INSERT INTO ticket(id, customerId, busTripId, seatId, staffId, status, ticketPrice) "
                    + "VALUES(?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setString(1, t.getId());
            stm.setString(2, t.getCustomerId());
            stm.setString(3, t.getBusTripId());
            stm.setInt(4, t.getSeatId());
            stm.setString(5, t.getStaffId());
            stm.setString(6, t.getStatus());
            stm.setDouble(7, t.getTicketPrice());
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
    
    public List<Ticket> getTicketsByBusTrip(String bustripID) throws SQLException
    {
        List<Ticket> tickets = new ArrayList<>();
        try (Connection conn = JdbcUtils.getConn())
        {
            String sql = "SELECT * FROM ticket WHERE BusTripID = ?";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setString(1, bustripID);
            ResultSet rs = stm.executeQuery(); 
            while (rs.next()) {
                String id = rs.getString("ID");
                String cusId = rs.getString("CustomerID");
                int seatId = rs.getInt("SeatID");
                String staffId = rs.getString("StaffID");
                String status = rs.getString("Status");
                double ticketPrice = rs.getDouble("TicketPrice");
                tickets.add(new Ticket(id,cusId,bustripID,seatId,staffId,status, ticketPrice));
            }
        }
        return tickets;
    }
    
    
    public List<Ticket> getTickets(String keyword) throws SQLException
    {
        List<Ticket> tickets = new ArrayList<>();
        try (Connection conn = JdbcUtils.getConn())
        {
            String sql = "SELECT * "
                    + "FROM ticket t, seat s, customer c, user u ,bustrip bt, bus b, route r, location l1, location l2 "
                    + "WHERE t.StaffID = u.ID AND s.ID = t.SeatID "
                    + "AND t.CustomerID = c.ID AND t.BusTripID = bt.ID "
                    + "AND bt.BusID = b.ID AND bt.routeID = r.ID "
                    + "AND r.DepartureID = l1.ID AND r.DestinationID = l2.ID "
                    + "AND bt.DepartureTime < ?";
            if (keyword != null && !keyword.isEmpty()) 
                sql += " and c.Name like concat('%', ?, '%')";
            PreparedStatement stm = conn.prepareCall(sql);
            if (keyword != null && !keyword.isEmpty()) 
                stm.setString(2, keyword);       
            stm.setTimestamp(1, new Timestamp(System.currentTimeMillis() + 5 * 1000));
            ResultSet rs = stm.executeQuery(); 
            while (rs.next()) {
                String id = rs.getString("ID");
                String cusID = rs.getString("CustomerID");
                String busTripID = rs.getNString("BusTripID");
                int seatID = rs.getInt("SeatID");
                String staffID = rs.getNString("StaffID");
                String status = rs.getString("Status");
                String staffName = rs.getString("u.Name");
                String seatName = rs.getString("s.Name");
                String cusName = rs.getString("c.Name");
                String cusPhone = rs.getString("c.Phone");
                String licensePlates = rs.getString("b.LicensePlates");
                String departureName = rs.getNString("l1.Name");
                String destinationName = rs.getNString("l2.Name");
                double ticketPrice = rs.getDouble("TicketPrice");
                LocalDateTime departureTime = (LocalDateTime) rs.getObject("DepartureTime");
                tickets.add(new Ticket(id,cusID, busTripID, seatID, staffID, status, staffName, seatName, cusName, cusPhone, licensePlates, departureName,destinationName,ticketPrice, departureTime));
            }
        }
        return tickets;
    }
    
     public boolean deleteTicket(String id) throws SQLException {
        try (Connection conn = JdbcUtils.getConn()) {
            String sql = "DELETE FROM ticket WHERE id=?";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setString(1, id);
            return stm.executeUpdate() > 0;
        }
    }
    
     
    public boolean changeStatusToBuy(Ticket t) throws SQLException
    {
        try(Connection conn = JdbcUtils.getConn())
        {
            conn.setAutoCommit(false);
            String sql = "UPDATE ticket set status = ? WHERE id = ? AND status = 'booked'";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setNString(1,  "purchased");
            stm.setNString(2,  t.getId());
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
    
    
}
