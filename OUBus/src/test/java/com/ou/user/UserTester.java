/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ou.user;
import com.ou.conf.JdbcUtils;
import java.sql.Connection;
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
public class UserTester {
    private static Connection conn;
    @BeforeAll
    public static void beforeAll() throws SQLException {
        conn = JdbcUtils.getConn();
    }
    @AfterAll
    public static void afterAll() throws SQLException
    {
        if (conn != null)
            conn.close();
    }
    @Test
    public void testUnique() throws SQLException {
        Statement stm = conn.createStatement();
        ResultSet rs = stm.executeQuery("SELECT * FROM user");
        
        List<String> kq = new ArrayList<>();
        while(rs.next()) {
            String name = rs.getString("Username");
            kq.add(name);
        }
        
        Set<String> kq2 = new HashSet<>(kq);
        
        Assertions.assertEquals(kq.size(), kq2.size());
    }
}
