/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ou.bus;

import com.ou.conf.JdbcUtils;
import com.ou.pojo.Bus;
import com.ou.pojo.BusTrip;
import com.ou.service.BusService;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

/**
 *
 * @author yuumm
 */
public class BusTester {

    private static Connection conn;
    public static BusService bs;

    @BeforeAll
    public static void beforeAll() throws SQLException {
        conn = JdbcUtils.getConn();
        bs = new BusService();
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
        ResultSet rs = stm.executeQuery("SELECT * FROM bus");

        List<String> kq = new ArrayList<>();
        while (rs.next()) {
            String licensePlates = rs.getString("LicensePlates");
            kq.add(licensePlates);
        }

        Set<String> kq2 = new HashSet<>(kq);

        Assertions.assertEquals(kq.size(), kq2.size());
    }

    @Test
    void testGetBuses() throws SQLException {
        List<Bus> buses = bs.getBuses();
        // Kiểm tra kết quả trả về
        Assertions.assertNotNull(buses);
        // kiểm tra số lượng trả về
        Assertions.assertEquals(12, buses.size());
        // Kiểm tra thông tin của người dùng đầu tiên
        Bus firstBus = buses.get(0);
        Assertions.assertEquals(19, firstBus.getId());
        Assertions.assertEquals("51B-123.43", firstBus.getLicensePlates());
    }

    @Test
    void testGetAvailableBuses() throws SQLException {
        BusTrip busTrip = new BusTrip();
        busTrip.setRouteId("a5b661cc-f9b2-4a68-a2ad-262f6e09fd81");
        busTrip.setDepartureId(5);
        busTrip.setDestinationId(1);
        busTrip.setTotalTime(540);
        busTrip.setDepartureTime(LocalDateTime.of(2023, Month.APRIL, 23, 14, 0));
        List<Bus> availableBuses = bs.getAvailableBuses(busTrip);

        // Kiểm tra số lượng trả về
        Assertions.assertEquals(12, availableBuses.size());

        // Kiểm tra kết quả hàng đầu
        Bus firstBus = availableBuses.get(0);
        Assertions.assertEquals(20, firstBus.getId());
    }


}
