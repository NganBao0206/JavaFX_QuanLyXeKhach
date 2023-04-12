/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ou.service;

import com.ou.conf.JdbcUtils;
import com.ou.pojo.BusTrip;
import com.ou.pojo.Route;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    public ValidResult isValidBusTripWithBeforeTrip(BusTrip busTrip) throws SQLException {
        try (Connection conn = JdbcUtils.getConn()) {
            String sql = "SELECT * FROM bustrip b, route r, location l1, location l2 WHERE b.ID != ? AND b.RouteID = r.ID AND r.DepartureID = l1.ID AND r.DestinationID = l2.ID AND BusID = ? AND DepartureTime < ? ORDER BY DepartureTime DESC LIMIT 1";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setString(1, busTrip.getId());
            stm.setInt(2, busTrip.getBusId());
            stm.setObject(3, busTrip.getDepartureTime());
            ResultSet rs = stm.executeQuery();
            BusTrip beforeTrip = null;
            if (rs.next()) {
                String id = rs.getNString("ID");
                String routeId = rs.getNString("RouteID");
                LocalDateTime departureTime = (LocalDateTime) rs.getObject("DepartureTime");
                int busId = rs.getInt("BusID");
                double surcharge = rs.getDouble("Surcharge");
                int departureId = rs.getInt("DepartureID");
                int destinationId = rs.getInt("DestinationID");
                String departureName = rs.getNString("l1.Name");
                String destinationName = rs.getNString("l2.Name");
                int totalTime = rs.getInt("TotalTime");
                beforeTrip = new BusTrip(id, routeId, departureTime, busId, surcharge, totalTime, departureId, destinationId, departureName, destinationName);
            }
            RouteService rsv = new RouteService();
            if (beforeTrip != null) {
                LocalDateTime arrivalTime = beforeTrip.getDepartureTime().plusMinutes(beforeTrip.getTotalTime());
                Route routeBusTrip = rsv.getRoute(busTrip.getRouteId());
                Route middleRoute;
                if (beforeTrip.getDestinationId() == routeBusTrip.getDepartureId()) {
                    middleRoute = new Route(-1, -1, 0, 0);
                } else {
                    middleRoute = rsv.getRoute("-1", beforeTrip.getDestinationId(), routeBusTrip.getDepartureId());
                    if (middleRoute == null) {
                        middleRoute = rsv.getRoute("-1", routeBusTrip.getDepartureId(), beforeTrip.getDestinationId());
                        if (middleRoute == null) {
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
                            LocalDateTime arrivalTimeOfBeforeTrip = beforeTrip.getDepartureTime().plusMinutes(beforeTrip.getTotalTime());
                            String date = arrivalTimeOfBeforeTrip.format(formatter);
                            return new ValidResult(0, "Theo dự tính xe đang ở " + beforeTrip.getDestinationName() + " lúc " + date + "Bạn chưa cung cấp thông tin về tuyến giữa " + routeBusTrip.getDepartureName() + " và " + beforeTrip.getDestinationName() + " việc tiếp tục thêm có thể tạo ra chuyến xe bất hợp lý bạn có muốn tiếp tục không?", null);
                        }
                    }
                }

                LocalDateTime minTime = arrivalTime.plusMinutes(middleRoute.getTotalTime());

                if (busTrip.getDepartureTime().isBefore(minTime)) {
                    System.out.println(beforeTrip.getId());
                    System.out.println(middleRoute.getDepartureId() + "  " + middleRoute.getDestinationId());
                    System.out.println(middleRoute.getTotalTime());
                    System.out.println(arrivalTime);
                    System.out.println(busTrip.getDepartureTime());
                    System.out.println(minTime);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
                    LocalDateTime arrivalTimeOfBeforeTrip = beforeTrip.getDepartureTime().plusMinutes(beforeTrip.getTotalTime());
                    String date = arrivalTimeOfBeforeTrip.format(formatter);
                    String tripDate = busTrip.getDepartureTime().format(formatter);
                    return new ValidResult(-1, "Không thể thêm\nTheo dự tính xe đang ở " + beforeTrip.getDestinationName() + " lúc " + date + " và không thể di chuyển đến nơi xuất phát của chuyến này lúc " + tripDate + "kịp thời gian.\nXin vui lòng điều chỉnh thời gian, xe hoặc tuyến đi, bạn nên chọn điểm xuất phát là " + beforeTrip.getDestinationName() + " để tận dụng xe tốt nhất", beforeTrip);
                }
            }
        }
        return new ValidResult(1, "", null);
    }

    public ValidResult isValidBusTripWithAfterTrip(BusTrip busTrip) throws SQLException {
        try (Connection conn = JdbcUtils.getConn()) {
            String sql = "SELECT * FROM bustrip b, route r, location l1, location l2 WHERE b.ID != ? AND b.RouteID = r.ID AND r.DepartureID = l1.ID AND r.DestinationID = l2.ID AND BusID = ? AND DepartureTime > ? ORDER BY DepartureTime LIMIT 1";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setString(1, busTrip.getId());
            stm.setInt(2, busTrip.getBusId());
            stm.setObject(3, busTrip.getDepartureTime());
            ResultSet rs = stm.executeQuery();
            BusTrip afterTrip = null;
            if (rs.next()) {
                String id = rs.getNString("ID");
                String routeId = rs.getNString("RouteID");
                LocalDateTime departureTime = (LocalDateTime) rs.getObject("DepartureTime");
                int busId = rs.getInt("BusID");
                double surcharge = rs.getDouble("Surcharge");
                int departureId = rs.getInt("DepartureID");
                int destinationId = rs.getInt("DestinationID");
                String departureName = rs.getNString("l1.Name");
                String destinationName = rs.getNString("l2.Name");
                int totalTime = rs.getInt("TotalTime");
                afterTrip = new BusTrip(id, routeId, departureTime, busId, surcharge, totalTime, departureId, destinationId, departureName, destinationName);
            }
            RouteService rsv = new RouteService();
            if (afterTrip != null) {
                LocalDateTime arrivalTime = busTrip.getDepartureTime().plusMinutes(busTrip.getTotalTime());

                Route routeBusTrip = rsv.getRoute(busTrip.getRouteId());
                Route middleRoute;
                if (routeBusTrip.getDestinationId() == afterTrip.getDepartureId()) {
                    middleRoute = new Route(-1, -1, 0, 0);
                } else {
                    middleRoute = rsv.getRoute("-1", routeBusTrip.getDestinationId(), afterTrip.getDepartureId());
                    if (middleRoute == null) {
                        middleRoute = rsv.getRoute("-1", afterTrip.getDepartureId(), routeBusTrip.getDestinationId());
                        if (middleRoute == null) {
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
                            LocalDateTime arrivalTimeOfBeforeTrip = afterTrip.getDepartureTime();
                            String date = arrivalTimeOfBeforeTrip.format(formatter);
                            return new ValidResult(0, "Theo dự tính xe phải có mặt ở " + afterTrip.getDepartureName() + " lúc " + date + "Bạn chưa cung cấp thông tin về tuyến giữa " + routeBusTrip.getDestinationName() + " và " + afterTrip.getDepartureName() + " việc tiếp tục thêm có thể tạo ra chuyến xe bất hợp lý bạn có muốn tiếp tục không?", null);
                        }
                    }
                }
                LocalDateTime minTime = arrivalTime.plusMinutes(middleRoute.getTotalTime());
                if (afterTrip.getDepartureTime().isBefore(minTime)) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
                    LocalDateTime arrivalTimeOfBeforeTrip = afterTrip.getDepartureTime();
                    String date = arrivalTimeOfBeforeTrip.format(formatter);
                    String min = minTime.format(formatter);
                    return new ValidResult(-1, "Không thể thêm\nTheo dự tính xe phải có mặt ở " + afterTrip.getDepartureName() + " lúc " + date + "\nNhưng chuyến yêu cầu thêm hiện tại hoàn thành lúc " + min + "Vui lòng điều chỉnh tại thời gian, xe hoặc tuyến đi, bạn chọn điểm đến là " + afterTrip.getDepartureName() + " để tận dụng xe tốt nhất", null);
                }
            }
        }
        return new ValidResult(1, "", null);
    }

    public BusTrip getBusTripById(String id) throws SQLException {
        try (Connection conn = JdbcUtils.getConn()) {
            String sql = "SELECT * FROM bustrip WHERE ID = ?";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setString(1, id);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                String routeId = rs.getString("RouteID");
                LocalDateTime departureTime = (LocalDateTime) rs.getObject("DepartureTime");
                int busId = rs.getInt("BusID");
                double surcharge = rs.getDouble("Surcharge");
                return new BusTrip(routeId, departureTime, busId, surcharge);
            }
        }
        return null;
    }

    public List<BusTrip> getBusTrips(LocalDate departureDate, int departureId, int destinationId) throws SQLException {
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

    public BusTrip getBusTrip(LocalDate departureDate, int departureId, int destinationId) throws SQLException {
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
            String id = rs.getNString("ID");
            String routeId = rs.getNString("RouteID");
            LocalDateTime departureTime = (LocalDateTime) rs.getObject("DepartureTime");
            int busId = rs.getInt("BusID");
            double price = rs.getDouble("Price");
            double surcharge = rs.getDouble("Surcharge");
            int totalTime = rs.getInt("TotalTime");
            String departureName = rs.getNString("l1.Name");
            String destinationName = rs.getNString("l2.Name");
            return new BusTrip(id, routeId, departureTime, busId, price, surcharge, totalTime, departureName, destinationName);
        }
    }

public List<BusTrip> getBusTripsEmployee(LocalDate departureDate, int departureId, int destinationId) throws SQLException {
        List<BusTrip> busTrips = new ArrayList<>();
        try (Connection conn = JdbcUtils.getConn()) {
            String sql = "SELECT * FROM bustrip b, route r, location l1, location l2 "
                    + "WHERE b.RouteID = r.ID AND r.DepartureID = l1.ID "
                    + "AND r.DestinationID = l2.ID "
                    + "AND b.DepartureTime > ?";
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
            stm.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now().plusMinutes(5)));
            System.out.println(LocalDateTime.now().plusMinutes(5));
            int index = 2;
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
