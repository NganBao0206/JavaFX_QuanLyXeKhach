/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ou.service;

import com.ou.conf.JdbcUtils;
import com.ou.pojo.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.crypto.bcrypt.BCrypt;

/**
 *
 * @author yuumm
 */
public class UserService {

    public List<User> getUsers() throws SQLException {
        
        List<User> users = new ArrayList<>();
        try (Connection conn = JdbcUtils.getConn()) {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM user");
            while (rs.next()) {
                String username = rs.getNString("Username");
                String password = rs.getNString("Password");
                String name = rs.getNString("Name");
                String userRole = rs.getNString("UserRole");
                users.add(new User(username, password, name, userRole));
            }
        }

        return users;
    }

    public User getUserByUsername(String username) throws SQLException {
        try (Connection conn = JdbcUtils.getConn()) {
            String sql = "SELECT * FROM user WHERE Username = ?";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setString(1, username);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                User u = new User();
                u.setUsername(rs.getNString("Username"));
                u.setPassword(rs.getNString("Password"));
                u.setUserRole(rs.getNString("UserRole"));
                return u;
            } else {
                return null;
            }
        }
        catch (Exception ex)
        {
            return null;
        }
    }

    public AuthenticationResult authenticatedUser(String username, String password) throws SQLException {
        User u = getUserByUsername(username);
        if (u == null) {
            return new AuthenticationResult(false, "Wrong username", "");
        }
        if (BCrypt.checkpw(password, u.getPassword())) {
            return new AuthenticationResult(true, null, u.getUserRole());
        }
        return new AuthenticationResult(false, "Wrong password", "");
    }
    
    public boolean addUser(User user) throws SQLException {
        try( Connection conn = JdbcUtils.getConn()) {
            conn.setAutoCommit(false);
            String sql = "INSERT INTO user(id, username, password, name, userRole) "
                    + "VALUES(?, ?, ?, ?, ?)";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setString(1, user.getId());
            stm.setString(2, user.getUsername());
            stm.setString(3, BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
            stm.setString(4, user.getName());
            stm.setString(5, user.getUserRole());
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
}