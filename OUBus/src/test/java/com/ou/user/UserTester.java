/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ou.user;
import com.ou.conf.JdbcUtils;
import com.ou.pojo.User;
import com.ou.service.UserService;
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
public class UserTester {
    private static Connection conn;
    public static UserService us;
    
    @BeforeAll
    public static void beforeAll() throws SQLException {
        conn = JdbcUtils.getConn();
        us = new UserService();
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
    
    @Test
    public void testNotNull() throws SQLException{
        List<User> users = us.getUsers();
        long r = users.stream().filter(u -> u.getId() == null).count();
        Assertions.assertTrue(r == 0);
    }
    
    @Test
    public void testAddSuccessful(){
        User u = new User("abc","123","Vo Phu Phat","staff");
        try {
            boolean actual = us.addUser(u);
            Assertions.assertTrue(actual);
            
            String sql = "SELECT * FROM user WHERE id=?";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setString(1, u.getId());
            
            ResultSet rs = stm.executeQuery();
            Assertions.assertNotNull(rs.next());
            Assertions.assertEquals("abc", rs.getString("username"));
            Assertions.assertEquals("Vo Phu Phat", rs.getString("name"));
            Assertions.assertEquals("staff", rs.getString("staff"));
        } catch (SQLException ex) {
            Logger.getLogger(UserTester.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
