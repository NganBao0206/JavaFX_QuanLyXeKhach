/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ou.ticket;

import com.ou.conf.JdbcUtils;
import com.ou.pojo.Ticket;
import com.ou.service.TicketService;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

/**
 *
 * @author Hi
 */
public class TicketTester {

    private static Connection conn;
    public static TicketService ts;

    @BeforeAll
    public static void beforeAll() throws SQLException {
        conn = JdbcUtils.getConn();
        ts = new TicketService();
    }

    @AfterAll
    public static void afterAll() throws SQLException {
        if (conn != null) {
            conn.close();
        }
    }

    
    @Test
    void testAddTicket() throws SQLException {
        Ticket t = new Ticket();
        t.setCustomerId("5404ee15-4d96-471d-9b7a-d3f8a90c3d11");
        t.setBusTripId("d4d6aae4-a6e5-4f0a-91da-ebb33f69e369");
        t.setSeatId(30);
        t.setStaffId("9e6dfb03-d9d2-46c9-aeae-2b0236c2099c");
        t.setStatus("purchased");
        t.setTicketPrice(150.00);

        LocalDateTime dateNow = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        t.setTime(dateNow);
        try {
            boolean actual = ts.addTicket(t);
            Assertions.assertTrue(actual);

            String sql = "SELECT * FROM Ticket WHERE id=?";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setString(1, t.getId());

            ResultSet rs = stm.executeQuery();
            Assertions.assertNotNull(rs.next());
            Assertions.assertEquals(t.getId(), rs.getString("ID"));
            Assertions.assertEquals("5404ee15-4d96-471d-9b7a-d3f8a90c3d11", rs.getString("CustomerID"));
            Assertions.assertEquals("d4d6aae4-a6e5-4f0a-91da-ebb33f69e369", rs.getString("BusTripID"));
            Assertions.assertEquals(30, rs.getInt("SeatID"));
            Assertions.assertEquals("9e6dfb03-d9d2-46c9-aeae-2b0236c2099c", rs.getString("StaffID"));
            Assertions.assertEquals("purchased", rs.getString("Status"));
            Assertions.assertEquals(150.00, rs.getDouble("TicketPrice"));
            Assertions.assertEquals(dateNow, (LocalDateTime) rs.getObject("Time"));

        } catch (SQLException ex) {
            Logger.getLogger(TicketTester.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    @Test
    void testGetTicketsByBusTrip() throws SQLException {
        String bustripID = "d4d6aae4-a6e5-4f0a-91da-ebb33f69e369";
        List<Ticket> tickets = ts.getTicketsByBusTrip(bustripID);

        // Kiem tra ket qua tra ve khong null
        Assertions.assertNotNull(tickets);

        // Kiem so luong tra ve
        Assertions.assertEquals(2, tickets.size());

        // Kiem tra mot phan dai dien
        Ticket ticket = tickets.get(0);
        Assertions.assertEquals("611bfd32-6811-4709-9c9b-09582bbc8433", ticket.getId());
        Assertions.assertEquals("5404ee15-4d96-471d-9b7a-d3f8a90c3d11", ticket.getCustomerId());
        Assertions.assertEquals("d4d6aae4-a6e5-4f0a-91da-ebb33f69e369", ticket.getBusTripId());
        Assertions.assertEquals(24, ticket.getSeatId());
        Assertions.assertEquals("9e6dfb03-d9d2-46c9-aeae-2b0236c2099c", ticket.getStaffId());
        Assertions.assertEquals("purchased", ticket.getStatus());
        Assertions.assertEquals(150.00, ticket.getTicketPrice());
    }

    
    @Test
    void testGetTickets() throws SQLException{
        String keyword = "Ngân";
        List<Ticket> tickets = ts.getTickets(keyword);
        
        //Kiem tra tickets not null
        Assertions.assertNotNull(tickets);
        
        //Kiem tra so luong ve
        Assertions.assertEquals(3,tickets.size());
        
        //Kiem tra thanh phan dai dien
        Ticket ticket = tickets.get(0);
        Assertions.assertEquals("05d8e3c2-0ed3-46cb-86a6-57c8fb1596a6", ticket.getId());
        Assertions.assertEquals("94a57bec-8cc4-4306-bec5-e0fbb7e4706b", ticket.getCustomerId());
        Assertions.assertEquals("d4d6aae4-a6e5-4f0a-91da-ebb33f69e369", ticket.getBusTripId());
        Assertions.assertEquals(24, ticket.getSeatId());
        Assertions.assertEquals("9e6dfb03-d9d2-46c9-aeae-2b0236c2099c", ticket.getStaffId());
        Assertions.assertEquals("purchased", ticket.getStatus());
        Assertions.assertEquals(150.00, ticket.getTicketPrice());
        
    }
    
    
    @Test
    void testDeleteTicket() throws SQLException
    {
        Ticket t = new Ticket();
        t.setId("2e364b01-82c0-4cb6-b3ef-87abc5240b14");
        
        try
        {
            boolean actual = ts.deleteTicket(t.getId());
            
            Assertions.assertTrue(actual);
            
            String sql = "DELETE FROM Ticket WHERE id=?";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setString(1, t.getId());
            
            List<Ticket> tickets = ts.getTickets(null);
            
            Assertions.assertEquals(1,tickets.size());
        }
        catch (SQLException ex) {
            Logger.getLogger(TicketTester.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    void testEditTicket() throws SQLException{
        Ticket t = new Ticket();
        t.setId("05d8e3c2-0ed3-46cb-86a6-57c8fb1596a6");
        t.setCustomerId("94a57bec-8cc4-4306-bec5-e0fbb7e4706b");
        t.setBusTripId("d4d6aae4-a6e5-4f0a-91da-ebb33f69e369");
        t.setSeatId(25);
        t.setStaffId("9e6dfb03-d9d2-46c9-aeae-2b0236c2099c");
        
        try 
        {
            boolean actual = ts.editTicket(t); 
            Assertions.assertTrue(actual);
            
            String sql = "UPDATE ticket SET Customer = ?, BusTripID = ?, SeatID = ?, StaffID = ? WHERE id = ?";
            PreparedStatement stm = conn.prepareCall(sql);
            
            ResultSet rs = stm.executeQuery();
            Assertions.assertNotNull(rs.next());
            
            Assertions.assertEquals(25,t.getSeatId());
            
            
        }
        catch (SQLException ex) {
            Logger.getLogger(TicketTester.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
            
    @Test
    void testChangeStatusToBuy() throws SQLException{
        Ticket t = new Ticket();
        t.setId("05d8e3c2-0ed3-46cb-86a6-57c8fb1596a6");
        t.setStatus("booked");
        
        try 
        {
            boolean actual = ts.editTicket(t); 
            Assertions.assertTrue(actual);
            
            String sql = "UPDATE ticket set status = ? WHERE id = ? AND status = 'booked'";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setString(1, "purchased");
            stm.setString(2,t.getId());
            
            ResultSet rs = stm.executeQuery();
            Assertions.assertNotNull(rs.next());
            
            Assertions.assertEquals("purchased",t.getStatus());
            
            
        }
        catch (SQLException ex) {
            Logger.getLogger(TicketTester.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    @Test
    void testGetInvalidTickets() throws SQLException{
        List<Ticket> tickets = ts.getInvalidTickets();

        // Kiem tra ket qua tra ve khong null
        Assertions.assertNotNull(tickets);

        // Kiem so luong tra ve
        Assertions.assertEquals(2, tickets.size());

        // Kiem tra mot phan dai dien
        Ticket ticket = tickets.get(0);
        Assertions.assertEquals("557df3cb-2ad9-4d7c-a396-5fd0703d04c4", ticket.getId());
    }
}
