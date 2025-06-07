// package com.levanz.customer.config;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.jdbc.core.JdbcTemplate;
// import org.springframework.jdbc.core.RowMapper;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.stereotype.Component;
// import org.springframework.transaction.annotation.Transactional;

// import com.levanz.customer.entity.User;

// import javax.annotation.PostConstruct;
// import java.sql.ResultSet;
// import java.sql.SQLException;
// import java.util.List;

// @Component
// @Configuration
// public class DatabaseInitializer {

//     @Autowired
//     private JdbcTemplate jdbcTemplate;

//     @Autowired
//     private PasswordEncoder passwordEncoder;

//     private RowMapper<User> userRowMapper = new RowMapper<User>() {
//         @Override
//         public User mapRow(ResultSet rs, int rowNum) throws SQLException {
//             User user = new User();
//             user.setUserID(rs.getLong("userID"));
//             user.setName(rs.getString("name"));
//             user.setEmail(rs.getString("email"));
//             user.setPassword(rs.getString("password"));
//             user.setRole(rs.getString("role"));
//             return user;
//         }
//     };

//     @PostConstruct
//     @Transactional
//     public void hashPasswords() {
//         String sqlSelect = "SELECT * FROM user";
//         List<User> users = jdbcTemplate.query(sqlSelect, userRowMapper);
//         for (User user : users) {
//             String hashedPassword = passwordEncoder.encode(user.getPassword());
//             String sqlUpdate = "UPDATE user SET password = ? WHERE userID = ?";
//             jdbcTemplate.update(sqlUpdate, hashedPassword, user.getUserID());
//         }
//     }
// }
