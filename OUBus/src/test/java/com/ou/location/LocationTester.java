/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ou.location;

import com.ou.conf.JdbcUtils;
import com.ou.pojo.Location;
import com.ou.service.LocationService;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
public class LocationTester {

    private static Connection conn;
    public static LocationService ls;

    @BeforeAll
    public static void beforeAll() throws SQLException {
        conn = JdbcUtils.getConn();
        ls = new LocationService();
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
        ResultSet rs = stm.executeQuery("SELECT * FROM location");

        List<String> kq = new ArrayList<>();
        while (rs.next()) {
            String name = rs.getString("Name");
            kq.add(name);
        }

        Set<String> kq2 = new HashSet<>(kq);

        Assertions.assertEquals(kq.size(), kq2.size());
    }
    @Test
    void testGetLocations() throws SQLException {
        // -- KIỂM TRA KHI KEYWORD EMPTY --
        List<Location> locations = ls.getLocations("");

        //Kiểm tra kết quả trả về
        Assertions.assertNotNull(locations);

        //Kiểm tra số lượng trả về
        Assertions.assertEquals(5, locations.size());

        //Kiểm tra các thuộc tính của đối lượng location
        Assertions.assertEquals(1, locations.get(0).getId());
        Assertions.assertEquals("Thành phố Hồ Chí Minh", locations.get(0).getName());

        // -- KIỂM TRA KHI CÓ KEYWORD --
        List<Location> locationsHaveKw = ls.getLocations("cà");

        //Kiểm tra kết quả trả về
        Assertions.assertNotNull(locationsHaveKw);

        //Kiểm tra số lượng trả về
        Assertions.assertEquals(1, locationsHaveKw.size());

        //Kiểm tra các thuộc tính của đối lượng location
        Assertions.assertEquals(5, locationsHaveKw.get(0).getId());
        Assertions.assertEquals("Cà Mau", locationsHaveKw.get(0).getName());
    }

    @Test
    void testGetLocation() throws SQLException {

        // -- KIỂM TRA LOCATION CÓ NGOẠI TRỪ --
        Location location = ls.getLocation(1, "Thành phố Hồ Chí Minh");

        // Kiểm tra kết quả trả về
        Assertions.assertNull(location);

        // -- KIỂM TRA LOCATION KHÔNG NGOẠI TRỪ --
        Location location2 = ls.getLocation(-1, "Thành phố Hồ Chí Minh");
        Assertions.assertNotNull(location2);

        Assertions.assertEquals(1, location2.getId());
        Assertions.assertEquals("Thành phố Hồ Chí Minh", location2.getName());

        // -- KIỂM TRA LOCATION KHÔNG TỒN TẠI --
        Location location3 = ls.getLocation(-1, "Thái Lan");
        Assertions.assertNull(location3);
    }

    @Test
    void testAddLocation() throws SQLException {
        Location location = new Location(0, "Thái Nguyên");
        boolean result = ls.addLocation(location);

        Assertions.assertTrue(result);

        String sql = "SELECT * FROM location WHERE Name=?";
        PreparedStatement stm = conn.prepareCall(sql);
        stm.setString(1, location.getName());

        ResultSet rs = stm.executeQuery();
        Assertions.assertNotNull(rs.next());
    }
    
    @Test
    void testEditLocation() throws SQLException {
        Location location = new Location(17, "Thái Bình");
        boolean result = ls.editLocation(location);

        Assertions.assertTrue(result);

        String sql = "SELECT * FROM location WHERE ID=?";
        PreparedStatement stm = conn.prepareCall(sql);
        stm.setInt(1, location.getId());

        ResultSet rs = stm.executeQuery();
        Assertions.assertNotNull(rs.next());
        Assertions.assertEquals("Thái Bình", rs.getNString("Name"));
    }
    
    @Test
    void testDeleteLocation() throws SQLException {
        boolean result = ls.deleteLocation(17);
        Assertions.assertTrue(result);
        String sql = "SELECT * FROM location WHERE ID=?";
        PreparedStatement stm = conn.prepareCall(sql);
        stm.setInt(1, 17);
        ResultSet rs = stm.executeQuery();
        Assertions.assertFalse(rs.next());
    }
}
