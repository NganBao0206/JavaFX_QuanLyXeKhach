/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ou.route;

import com.ou.conf.JdbcUtils;
import com.ou.pojo.Route;
import com.ou.service.RouteService;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

/**
 *
 * @author yuumm
 */
public class RouteTester {
    private static Connection conn;
    public static RouteService rs;

    @BeforeAll
    public static void beforeAll() throws SQLException {
        conn = JdbcUtils.getConn();
        rs = new RouteService();
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
        ResultSet rs = stm.executeQuery("SELECT * FROM Route");

        List<String> kq = new ArrayList<>();
        while (rs.next()) {
            String temp = rs.getInt("DepartureID") + "  " + rs.getInt("DestinationID");
            kq.add(temp);
        }

        Set<String> kq2 = new HashSet<>(kq);

        Assertions.assertEquals(kq.size(), kq2.size());
    }
    
    @Test
    void testGetRoutes() throws SQLException {
        List<Route> routes = rs.getRoutes("Đà Nẵng", "");

        // Kiểm tra kết quả trả về
        Assertions.assertNotNull(routes);

        // Kiểm tra các thuộc tính của đối tượng Route
        Assertions.assertEquals("0a7ebf40-0ee8-48a4-8b14-572068fe01b2", routes.get(0).getId());
        Assertions.assertEquals(2, routes.get(0).getDepartureId());
        Assertions.assertEquals(3, routes.get(0).getDestinationId());
        Assertions.assertEquals(300.00, routes.get(0).getPrice());
        Assertions.assertEquals(1200, routes.get(0).getTotalTime());
        Assertions.assertEquals("Đà Nẵng", routes.get(0).getDepartureName());
        Assertions.assertEquals("Hà Nội", routes.get(0).getDestinationName());

        // Kiểm tra số lượng
        Assertions.assertEquals(3, routes.size());
    }

    @Test
    void testGetRoute() throws SQLException {
        Route route = rs.getRoute("Đà Nẵng", "Long An");

        // Kiểm tra kết quả trả về
        Assertions.assertNotNull(route);

        // Kiểm tra các thuộc tính của đối tượng Route
        Assertions.assertEquals("227fdaeb-425a-43dc-a2c8-35736a2edcf9", route.getId());
        Assertions.assertEquals(2, route.getDepartureId());
        Assertions.assertEquals(4, route.getDestinationId());
        Assertions.assertEquals(450.00, route.getPrice());
        Assertions.assertEquals(980, route.getTotalTime());
        Assertions.assertEquals("Đà Nẵng", route.getDepartureName());
        Assertions.assertEquals("Long An", route.getDestinationName());
    }

    @Test
    void testGetRouteWithExceptId() throws SQLException {
        Route route = rs.getRoute("0a7ebf40-0ee8-48a4-8b14-572068fe01b2", 2, 3);

        // Kiểm tra kết quả trả về
        Assertions.assertNull(route);

        Route route2 = rs.getRoute("-1", 2, 3);
        Assertions.assertEquals("0a7ebf40-0ee8-48a4-8b14-572068fe01b2", route2.getId());
        Assertions.assertEquals(2, route2.getDepartureId());
        Assertions.assertEquals(3, route2.getDestinationId());
        Assertions.assertEquals(300.00, route2.getPrice());
        Assertions.assertEquals(1200, route2.getTotalTime());
    }

    @Test
    void testGetRoutesByLocationId() throws SQLException {
        List<Route> routes = rs.getRoutes(2);

        // Kiểm tra kết quả trả về
        Assertions.assertNotNull(routes);

        // Kiểm tra các thuộc tính của đối tượng Route
        Assertions.assertEquals("0a7ebf40-0ee8-48a4-8b14-572068fe01b2", routes.get(0).getId());
        Assertions.assertEquals(2, routes.get(0).getDepartureId());
        Assertions.assertEquals(3, routes.get(0).getDestinationId());
        Assertions.assertEquals(300.00, routes.get(0).getPrice());
        Assertions.assertEquals(1200, routes.get(0).getTotalTime());

        //Kiểm tra số lượng trả về
        Assertions.assertEquals(4, routes.size());
    }

    @Test
    void testGetRouteById() throws SQLException {
        Route route = rs.getRoute("4509ecc9-59d1-4c42-b0bb-de9252816090");

        // Kiểm tra kết quả trả về
        Assertions.assertNotNull(route);

        // Kiểm tra các thuộc tính
        // Kiểm tra các thuộc tính của đối tượng Route
        Assertions.assertEquals("4509ecc9-59d1-4c42-b0bb-de9252816090", route.getId());
        Assertions.assertEquals(1, route.getDepartureId());
        Assertions.assertEquals(2, route.getDestinationId());
        Assertions.assertEquals(500.00, route.getPrice());
        Assertions.assertEquals(1050, route.getTotalTime());
        Assertions.assertEquals("Thành phố Hồ Chí Minh", route.getDepartureName());
        Assertions.assertEquals("Đà Nẵng", route.getDestinationName());
    }

    @Test
    void testAddRoute() {
        Route r = new Route(3, 2, 300, 1200);
        try {
            boolean actual = rs.addRoute(r);
            Assertions.assertTrue(actual);

            String sql = "SELECT * FROM Route WHERE id=?";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setString(1, r.getId());

            ResultSet rs = stm.executeQuery();
            Assertions.assertNotNull(rs.next());
            Assertions.assertEquals(r.getId(), rs.getString("ID"));
            Assertions.assertEquals(3, rs.getInt("DepartureID"));
            Assertions.assertEquals(2, rs.getInt("DestinationID"));
            Assertions.assertEquals(300, rs.getDouble("Price"));
            Assertions.assertEquals(1200, rs.getInt("TotalTime"));
        } catch (SQLException ex) {
            Logger.getLogger(RouteTester.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testDeleteRoute() throws SQLException {
        boolean result = rs.deleteRoute("c2534e51-7727-4da1-9a13-28682cc968cc");

        // Kiểm tra kết quả trả về
        Assertions.assertTrue(result);

        // Kiểm tra xem tuyến đường đã bị xóa khỏi cơ sở dữ liệu chưa
        String sql = "SELECT * FROM Route WHERE id=?";
        PreparedStatement stm = conn.prepareCall(sql);
        stm.setString(1, "c2534e51-7727-4da1-9a13-28682cc968cc");

        ResultSet rs = stm.executeQuery();
        Assertions.assertFalse(rs.next());
    }
    
    @Test
    void testEditRoute() throws SQLException {
        Route route = new Route();
        route.setId("6daf7025-39f3-4a5f-90c0-4061b0f90f35");
        route.setDepartureId(1);
        route.setDestinationId(4);
        route.setPrice(160);
        route.setTotalTime(160);
        boolean result = rs.editRoute(route);
        // Kiểm tra kết quả cập nhật
        Assertions.assertTrue(result);
    }
}
