/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ou.seat;

import com.ou.conf.JdbcUtils;
import com.ou.pojo.Seat;
import com.ou.service.SeatService;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

/**
 *
 * @author Hi
 */
public class SeatTester {
    private static Connection conn;
    public static SeatService ss;

    @BeforeAll
    public static void beforeAll() throws SQLException {
        conn = JdbcUtils.getConn();
        ss = new SeatService();
    }

    @AfterAll
    public static void afterAll() throws SQLException {
        if (conn != null) {
            conn.close();
        }
    }
    
    @Test
    void testUnique() throws SQLException {
        Statement stm = conn.createStatement();
        ResultSet rs = stm.executeQuery("SELECT * FROM seat");

        List<String> kq = new ArrayList<>();
        while (rs.next()) {
            String name = rs.getString("Name");
            kq.add(name);
        }

        Set<String> kq2 = new HashSet<>(kq);

        Assertions.assertEquals(kq.size(), kq2.size());
    }
    
    @Test
    void testGetSeats() throws SQLException {
        List<Seat> listSeat = ss.getSeats();
        
        //Kiểm tra kết quả trả về
        Assertions.assertNotNull(listSeat);
        
        //Kiểm tra số lượng
        Assertions.assertEquals(32, listSeat.size());
        
        //Kiểm tra một thành phần
        Seat s = listSeat.get(0);
        Assertions.assertEquals(1, s.getId());
        Assertions.assertEquals("A01", s.getName());
    }
    
    @Test
    void testGetSeatByName() throws SQLException {
        Seat seat = ss.getSeat("A01");
        
        //Kiểm tra kết quả trả về
        Assertions.assertNotNull(seat);
        
        
        //Kiểm tra một thành phần
        Assertions.assertEquals(1, seat.getId());
        Assertions.assertEquals("A01", seat.getName());
    }
    
    @Test
    void testGetSeatById() throws SQLException {
        Seat seat = ss.getSeat(1);
        
        //Kiểm tra kết quả trả về
        Assertions.assertNotNull(seat);
        
        
        //Kiểm tra một thành phần
        Assertions.assertEquals(1, seat.getId());
        Assertions.assertEquals("A01", seat.getName());
    }
    
    @Test
    void testGetEmptySeatByBusTrip() throws SQLException {
        int results = ss.getEmptySeatByBusTrip("d4d6aae4-a6e5-4f0a-91da-ebb33f69e369");
        
        //Kiểm tra số lượng
        Assertions.assertEquals(21, results);
    }
}
