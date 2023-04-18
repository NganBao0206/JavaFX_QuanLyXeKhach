/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ou.user;

import com.ou.conf.JdbcUtils;
import com.ou.pojo.User;
import com.ou.utils.AuthenticationResult;
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
    public static void afterAll() throws SQLException {
        if (conn != null) {
            conn.close();
        }
    }

    @Test
    public void testUnique() throws SQLException {
        Statement stm = conn.createStatement();
        ResultSet rs = stm.executeQuery("SELECT * FROM user");

        List<String> kq = new ArrayList<>();
        while (rs.next()) {
            String name = rs.getString("Username");
            kq.add(name);
        }

        Set<String> kq2 = new HashSet<>(kq);

        Assertions.assertEquals(kq.size(), kq2.size());
    }

    @Test
    public void testNotNull() throws SQLException {
        List<User> users = us.getUsers();
        long r = users.stream().filter(u -> u.getId() == null).count();
        Assertions.assertTrue(r == 0);
    }

    @Test
    public void testAddSuccessful() throws SQLException {
        User u = new User("abc", "123", "Vo Phu Phat", "staff");
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

    }

    @Test
    void testGetUsers() throws SQLException {
        List<User> users = us.getUsers();
        // Kiểm tra kết quả trả về
        Assertions.assertNotNull(users);
        // kiểm tra số lượng trả về
        Assertions.assertEquals(4, users.size());
        // Kiểm tra thông tin của người dùng đầu tiên
        User firstUser = users.get(0);
        Assertions.assertEquals("0f77b858-3b2a-41d3-9921-d174f8600433", firstUser.getId());
        Assertions.assertEquals("abc", firstUser.getUsername());
        Assertions.assertEquals("$2a$10$7FknVd8C1A9r1HEiu/IZt.cXDpNHaqG0SxwYHFb70Rtas5paNbLzK", firstUser.getPassword());
        Assertions.assertEquals("Vo Phu Phat", firstUser.getName());
        Assertions.assertEquals("staff", firstUser.getUserRole());
    }

    @Test
    void testGetUserByUsername() throws SQLException {
        User user = us.getUserByUsername("abc");
        // Kiểm tra kết quả trả về
        Assertions.assertNotNull(user);
        // Kiểm tra thông tin của người dùng
        Assertions.assertEquals("abc", user.getUsername());
        Assertions.assertEquals("$2a$10$7FknVd8C1A9r1HEiu/IZt.cXDpNHaqG0SxwYHFb70Rtas5paNbLzK", user.getPassword());
        Assertions.assertEquals("staff", user.getUserRole());
    }

    @Test
    void testGetUserByUsernameWithExceptID() throws SQLException {
        User user = us.getUserByUsername("0f77b858-3b2a-41d3-9921-d174f8600433", "abc");
        // Kiểm tra kết quả trả về
        Assertions.assertNull(user);
    }

    @Test
    void testAuthenticatedUser() throws SQLException {

        AuthenticationResult result = us.authenticatedUser("abc", "123");
        // Kiểm tra kết quả xác thực
        Assertions.assertTrue(result.isSuccess());
        // Kiểm tra vai trò người dùng
        Assertions.assertEquals("staff", result.getUserRole());
    }

    @Test
    void testEditUser() throws SQLException {
        User user = new User();
        user.setId("b7dbc023-3957-45eb-a10b-d6835543a394");
        user.setUsername("staff1");
        user.setPassword("123");
        user.setName("Ngân");
        user.setUserRole("staff");
        boolean result = us.editUser(user);
        // Kiểm tra kết quả cập nhật
        Assertions.assertTrue(result);
    }

    @Test
    void testEditUserWithoutPass() throws SQLException {
        User user = new User();
        user.setId("c8ecad89-d275-46f4-b852-b416aa018e52");
        user.setUsername("staff2");
        user.setName("Nguyễn Kim Bảo");
        user.setUserRole("staff");
        boolean result = us.editUserWithoutPass(user);
        // Kiểm tra kết quả cập nhật
        Assertions.assertTrue(result);
    }
}
