/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ou.service;

import com.ou.conf.JdbcUtils;
import com.ou.pojo.Ticket;
import com.ou.utils.StatisticalValue;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
            String sql = "INSERT INTO ticket(id, customerId, busTripId, seatId, staffId, status, ticketPrice, time) "
                    + "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setString(1, t.getId());
            stm.setString(2, t.getCustomerId());
            stm.setString(3, t.getBusTripId());
            stm.setInt(4, t.getSeatId());
            stm.setString(5, t.getStaffId());
            stm.setString(6, t.getStatus());
            stm.setDouble(7, t.getTicketPrice());
            stm.setObject(8, LocalDateTime.now());
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

    public List<Ticket> getTicketsByBusTrip(String bustripID) throws SQLException {
        List<Ticket> tickets = new ArrayList<>();
        try (Connection conn = JdbcUtils.getConn()) {
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
                Ticket t = new Ticket(id, cusId, bustripID, seatId, staffId, status, ticketPrice);
                tickets.add(t);
            }
        }
        return tickets;
    }

    public Ticket getTicket(String id) throws SQLException
    {
        Ticket t = null;
        try (Connection conn = JdbcUtils.getConn()) {
            String sql = "SELECT * "
                    + "FROM ticket t, seat s, customer c, user u ,bustrip bt, bus b, route r, location l1, location l2 "
                    + "WHERE t.ID = ? AND t.StaffID = u.ID AND s.ID = t.SeatID "
                    + "AND t.CustomerID = c.ID AND t.BusTripID = bt.ID "
                    + "AND bt.BusID = b.ID AND bt.routeID = r.ID "
                    + "AND r.DepartureID = l1.ID AND r.DestinationID = l2.ID ";
            
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setString(1, id);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
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
                int busId = rs.getInt("b.ID");
                String departureName = rs.getNString("l1.Name");
                String destinationName = rs.getNString("l2.Name");
                double ticketPrice = rs.getDouble("TicketPrice");
                LocalDateTime departureTime = (LocalDateTime) rs.getObject("DepartureTime");
                LocalDateTime time = (LocalDateTime) rs.getObject("Time");
                t = new Ticket(id, cusID, busTripID, seatID, staffID, status, staffName, seatName, cusName, cusPhone, licensePlates, departureName, destinationName, ticketPrice, departureTime);
                t.setTime(time);
                t.setBusId(busId);
            }
        }
        return t;

    }
    
    public List<Ticket> getTickets(String keyword) throws SQLException {
        List<Ticket> tickets = new ArrayList<>();
        try (Connection conn = JdbcUtils.getConn()) {
            String sql = "SELECT * "
                    + "FROM ticket t, seat s, customer c, user u ,bustrip bt, bus b, route r, location l1, location l2 "
                    + "WHERE t.StaffID = u.ID AND s.ID = t.SeatID "
                    + "AND t.CustomerID = c.ID AND t.BusTripID = bt.ID "
                    + "AND bt.BusID = b.ID AND bt.routeID = r.ID "
                    + "AND r.DepartureID = l1.ID AND r.DestinationID = l2.ID ";
            if (keyword != null && !keyword.isEmpty()) {
                sql += " and c.Name like concat('%', ?, '%')";
            }
            PreparedStatement stm = conn.prepareCall(sql);
            if (keyword != null && !keyword.isEmpty()) {
                stm.setString(1, keyword);
            }
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
                int busId = rs.getInt("b.ID");
                String departureName = rs.getNString("l1.Name");
                String destinationName = rs.getNString("l2.Name");
                double ticketPrice = rs.getDouble("TicketPrice");
                LocalDateTime departureTime = (LocalDateTime) rs.getObject("DepartureTime");
                LocalDateTime time = (LocalDateTime) rs.getObject("Time");
                Ticket t = new Ticket(id, cusID, busTripID, seatID, staffID, status, staffName, seatName, cusName, cusPhone, licensePlates, departureName, destinationName, ticketPrice, departureTime);
                t.setTime(time);
                t.setBusId(busId);
                tickets.add(t);
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

    public boolean changeStatusToBuy(Ticket t) throws SQLException {
        try (Connection conn = JdbcUtils.getConn()) {
            conn.setAutoCommit(false);
            String sql = "UPDATE ticket set status = ? WHERE id = ? AND status = 'booked'";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setNString(1, "purchased");
            stm.setNString(2, t.getId());
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

    public List<Ticket> getInvalidTickets() throws SQLException {
        List<Ticket> tickets = new ArrayList<>();
        try (Connection conn = JdbcUtils.getConn()) {
            String sql = "SELECT * FROM ticket WHERE DATE_ADD(time, INTERVAL 30 MINUTE) < NOW() AND status = 'booked' ";
            PreparedStatement stm = conn.prepareCall(sql);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                String id = rs.getString("ID");
                Ticket t = new Ticket();
                t.setId(id);
                tickets.add(t);
            }
            return tickets;
        }
    }

    public boolean editTicket(Ticket ticket) throws SQLException {
        try (Connection conn = JdbcUtils.getConn()) {
            conn.setAutoCommit(false);
            String sql = "UPDATE ticket "
                    + "SET CustomerID = ? ,BusTripID = ? ,SeatID = ? ,StaffID = ?"
                    + "WHERE id = ?";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setString(1, ticket.getCustomerId());
            stm.setString(2, ticket.getBusTripId());
            stm.setInt(3, ticket.getSeatId());
            stm.setString(4, ticket.getStaffId());
            stm.setString(5, ticket.getId());
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
    
    public int getAmountTicketOfCustomer(String customerId) throws SQLException {
        try (Connection conn = JdbcUtils.getConn()) {
            conn.setAutoCommit(false);
            String sql = "SELECT COUNT(*) FROM ticket WHERE CustomerId = ?";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setString(1, customerId);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count;
            }
            return 0;
        }
    }
    

    public List<StatisticalValue> getMonthlyRevenue(int year, int month) throws SQLException {
        try (Connection conn = JdbcUtils.getConn()) {
            conn.setAutoCommit(false);
            List<StatisticalValue> results = new ArrayList<>();
            String sql = "SELECT DATE(Time), SUM(TicketPrice) FROM ticket WHERE Status = 'purchased' AND YEAR(ticket.Time) = ? AND MONTH(ticket.Time) = ? GROUP BY DATE(Time)";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setInt(1, year);
            stm.setInt(2, month);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                String label = rs.getString(1);
                double value = rs.getDouble(2);
                results.add(new StatisticalValue(label, value*1000));           
            }
            return results;
        }
    }
    
    public StatisticalValue getAvergePercentageSeat(int year, int month) throws SQLException {
        try (Connection conn = JdbcUtils.getConn()) {
            conn.setAutoCommit(false);
            StatisticalValue results = null;
            String sql = "SELECT AVG(percentage) ";
            sql += "FROM ( ";
            sql += "SELECT bt.ID, (COUNT(DISTINCT t.ID)), COUNT(DISTINCT s.ID), (COUNT(DISTINCT t.ID) / COUNT(DISTINCT s.ID) ) * 100 AS percentage ";
            sql += "FROM bustrip bt ";
            sql += "CROSS JOIN seat s LEFT JOIN ticket t ON t.BusTripID = bt.ID WHERE YEAR(bt.DepartureTime) = ? AND MONTH(bt.DepartureTime) = ? ";
            sql += "GROUP BY bt.ID ) AS subquery";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setInt(1,year);
            stm.setInt(2, month);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                results = new StatisticalValue("Tỷ lệ ghế đã bán", rs.getDouble(1));
            }
            return results;
        }
    }    
    
    public List<Number> getTotalTicket(int year, int month) throws SQLException {
        try (Connection conn = JdbcUtils.getConn()) {
            conn.setAutoCommit(false);
            List<Number> results = new ArrayList<>();
            String sql = "SELECT Count(t.id), SUM(t.TicketPrice) FROM Ticket t, busTrip bt WHERE YEAR(bt.DepartureTime) = ? AND MONTH(bt.DepartureTime) = ? AND t.BusTripID = bt.ID AND t.Status = 'purchased'";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setInt(1,year);
            stm.setInt(2, month);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                int total = rs.getInt(1);
                double totalSales = rs.getDouble(2) * 1000.0;
                results.add(totalSales);
                results.add(total);
            }
            return results;
        }
    }

}
