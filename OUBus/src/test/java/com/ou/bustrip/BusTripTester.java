/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ou.bustrip;

import com.ou.conf.JdbcUtils;
import com.ou.pojo.BusTrip;
import com.ou.service.BusTripService;
import com.ou.utils.ValidResult;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

/**
 * @author yuumm
 */
public class BusTripTester {
    private static Connection conn;
    public static BusTripService bts;

    @BeforeAll
    public static void beforeAll() throws SQLException {
        conn = JdbcUtils.getConn();
        bts = new BusTripService();
    }

    @AfterAll
    public static void afterAll() throws SQLException {
        if (conn != null) {
            conn.close();
        }
    }

    @Test
    void testAddBusTrip() throws SQLException {
        BusTrip bt = new BusTrip();
        bt.setBusId(20);
        bt.setRouteId("a5b661cc-f9b2-4a68-a2ad-262f6e09fd81");
        bt.setDepartureTime(LocalDateTime.of(2023, Month.APRIL, 23, 14, 0, 0));
        bt.setSurcharge(0);

        boolean actual = bts.addBusTrip(bt);
        Assertions.assertTrue(actual);

        String sql = "SELECT * FROM BusTrip WHERE id=?";
        PreparedStatement stm = conn.prepareCall(sql);
        stm.setString(1, bt.getId());

        ResultSet rs = stm.executeQuery();
        Assertions.assertNotNull(rs.next());
        Assertions.assertEquals(bt.getId(), rs.getString("ID"));
        Assertions.assertEquals("a5b661cc-f9b2-4a68-a2ad-262f6e09fd81", rs.getString("RouteID"));
        Assertions.assertEquals(20, rs.getInt("BusID"));
        Assertions.assertEquals(0, rs.getDouble("Surcharge"));
        Assertions.assertEquals(LocalDateTime.of(2023, Month.APRIL, 23, 14, 0, 0), (LocalDateTime) rs.getObject("DepartureTime"));
    }

    @Test
    void testEditBusTrip() throws SQLException {
        BusTrip bt = new BusTrip();
        bt.setId("970a5796-0234-4a65-9a02-7c2a12b73b63");
        bt.setBusId(23);
        bt.setRouteId("a5b661cc-f9b2-4a68-a2ad-262f6e09fd81");
        bt.setDepartureTime(LocalDateTime.of(2023, Month.APRIL, 23, 14, 0, 0));
        bt.setSurcharge(0);

        boolean actual = bts.editBusTrip(bt);
        Assertions.assertTrue(actual);

        String sql = "SELECT * FROM BusTrip WHERE id=?";
        PreparedStatement stm = conn.prepareCall(sql);
        stm.setString(1, bt.getId());

        ResultSet rs = stm.executeQuery();
        Assertions.assertNotNull(rs.next());
        Assertions.assertEquals(bt.getId(), rs.getString("ID"));
        Assertions.assertEquals("a5b661cc-f9b2-4a68-a2ad-262f6e09fd81", rs.getString("RouteID"));
        Assertions.assertEquals(23, rs.getInt("BusID"));
        Assertions.assertEquals(0, rs.getDouble("Surcharge"));
        Assertions.assertEquals(LocalDateTime.of(2023, Month.APRIL, 23, 14, 0, 0), (LocalDateTime) rs.getObject("DepartureTime"));
    }

    @Test
    void testIsValidBusTrip() throws SQLException {
        BusTrip bt = new BusTrip();
        bt.setBusId(27);
        bt.setRouteId("a5b661cc-f9b2-4a68-a2ad-262f6e09fd81");
        bt.setDepartureTime(LocalDateTime.of(2023, Month.APRIL, 19, 14, 1, 0));
        bt.setSurcharge(0);
        bt.setTotalTime(540);
        bt.setDepartureId(5);
        bt.setDestinationId(1);

        boolean actual = bts.isValidBusTrip(bt);
        Assertions.assertTrue(actual);

        bt.setDepartureTime(LocalDateTime.of(2023, Month.APRIL, 19, 12, 0, 0));
        actual = bts.isValidBusTrip(bt);
        Assertions.assertFalse(actual);
    }

    @Test
    void testIsValidBusTripWithBeforeTrip() throws SQLException {
        BusTrip bt = new BusTrip();
        bt.setBusId(27);
        bt.setRouteId("a5b661cc-f9b2-4a68-a2ad-262f6e09fd81");
        bt.setDepartureTime(LocalDateTime.of(2023, Month.APRIL, 19, 14,  1, 0));
        bt.setSurcharge(0);
        bt.setTotalTime(540);
        bt.setDepartureId(5);
        bt.setDestinationId(1);

        ValidResult actual = bts.isValidBusTripWithBeforeTrip(bt);
        Assertions.assertNotNull(actual.getTrip());
        Assertions.assertEquals(1, actual.getResult());
        Assertions.assertEquals(LocalDateTime.of(2023, Month.APRIL, 19, 14, 0, 0), actual.getMinDate());
        Assertions.assertEquals(-1, actual.getMidRoute().getDepartureId());
    }

    @Test
    void testIsValidBusTripWithAfterTrip() throws SQLException {
        BusTrip bt = new BusTrip();
        bt.setBusId(20);
        bt.setRouteId("a5b661cc-f9b2-4a68-a2ad-262f6e09fd81");
        bt.setDepartureTime(LocalDateTime.of(2023, Month.APRIL, 23, 14, 1, 0));
        bt.setSurcharge(0);
        bt.setTotalTime(540);
        bt.setDepartureId(5);
        bt.setDestinationId(1);

        ValidResult actual = bts.isValidBusTripWithAfterTrip(bt);
        Assertions.assertNotNull(actual.getTrip());
        Assertions.assertEquals(1, actual.getResult());
        Assertions.assertEquals(LocalDateTime.of(2023, Month.APRIL, 23, 23, 1, 0), actual.getMinDate());
        Assertions.assertEquals(-1, actual.getMidRoute().getDepartureId());
    }

    @Test
    void testGetBusTripById() throws SQLException {
        BusTrip bt = bts.getBusTripById("2ae58bad-4c0d-4729-ac9e-08de635d4bf2");

        //Kiểm tra kết quả trả về
        Assertions.assertNotNull(bt);

        //Kiểm tra đối tượng trả về
        Assertions.assertEquals("2ae58bad-4c0d-4729-ac9e-08de635d4bf2", bt.getId());
        Assertions.assertEquals("ae97bacd-5536-4b26-bd67-51b82b08e9c0", bt.getRouteId());
        Assertions.assertEquals(LocalDateTime.of(2023, Month.APRIL, 23, 23, 0, 0), bt.getDepartureTime());
        Assertions.assertEquals(20, bt.getBusId());
        Assertions.assertEquals(0, bt.getSurcharge());
    }

    @Test
    void testGetBusTrips() throws SQLException {
        List<BusTrip> busTrips = bts.getBusTrips(LocalDate.of(2023, Month.APRIL, 23), 1, 5);

        //Kiểm tra kết quả trả về
        Assertions.assertNotNull(busTrips);

        //Kiểm tra số lượng trả về
        Assertions.assertEquals(2, busTrips.size());

        //Kiểm tra đối tượng trả về
        BusTrip firstBusTrip = busTrips.get(0);
        Assertions.assertEquals("2ae58bad-4c0d-4729-ac9e-08de635d4bf2", firstBusTrip.getId());
        Assertions.assertEquals("ae97bacd-5536-4b26-bd67-51b82b08e9c0", firstBusTrip.getRouteId());
        Assertions.assertEquals(LocalDateTime.of(2023, Month.APRIL, 23, 23, 0, 0), firstBusTrip.getDepartureTime());
        Assertions.assertEquals(20, firstBusTrip.getBusId());
        Assertions.assertEquals(0, firstBusTrip.getSurcharge());
        Assertions.assertEquals(310.00, firstBusTrip.getPrice());
        Assertions.assertEquals(0, firstBusTrip.getSurcharge());
        Assertions.assertEquals(540, firstBusTrip.getTotalTime());
        Assertions.assertEquals("Thành phố Hồ Chí Minh", firstBusTrip.getDepartureName());
        Assertions.assertEquals("Cà Mau", firstBusTrip.getDestinationName());
    }
    
    @Test
    void testGetBusTripsEmployee() throws SQLException {
        List<BusTrip> busTrips = bts.getBusTripsEmployee(LocalDate.of(2023, Month.APRIL, 29), 1, 4);

        //Kiểm tra kết quả trả về
        Assertions.assertNotNull(busTrips);

        //Kiểm tra số lượng trả về
        Assertions.assertEquals(1, busTrips.size());

        //Kiểm tra đối tượng trả về
        BusTrip firstBusTrip = busTrips.get(0);
        Assertions.assertEquals("d4d6aae4-a6e5-4f0a-91da-ebb33f69e369", firstBusTrip.getId());
        Assertions.assertEquals("6daf7025-39f3-4a5f-90c0-4061b0f90f35", firstBusTrip.getRouteId());
        Assertions.assertEquals(LocalDateTime.of(2023, Month.APRIL, 29, 6, 0, 0), firstBusTrip.getDepartureTime());
        Assertions.assertEquals(22, firstBusTrip.getBusId());
        Assertions.assertEquals(150.00, firstBusTrip.getPrice());
        Assertions.assertEquals(0, firstBusTrip.getSurcharge());
        Assertions.assertEquals(150, firstBusTrip.getTotalTime());
        Assertions.assertEquals("Hồ Chí Minh", firstBusTrip.getDepartureName());
        Assertions.assertEquals("Long An", firstBusTrip.getDestinationName());
        Assertions.assertEquals(21, firstBusTrip.getEmptySeats());     
    }
    
    @Test
    void testDeleteBusTrip() throws SQLException {
        boolean result = bts.deleteBusTrip("970a5796-0234-4a65-9a02-7c2a12b73b63");

        // Kiểm tra kết quả trả về
        Assertions.assertTrue(result);

        // Kiểm tra xem chuyến xe đã bị xóa khỏi cơ sở dữ liệu chưa
        String sql = "SELECT * FROM bustrip WHERE id=?";
        PreparedStatement stm = conn.prepareCall(sql);
        stm.setString(1, "970a5796-0234-4a65-9a02-7c2a12b73b63");

        ResultSet rs = stm.executeQuery();
        Assertions.assertFalse(rs.next());
    }
}
