package org.example.repository;

import org.example.util.DatabaseConnectionFactory;
import org.example.repository.dto.UserDTO;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;

public class UserDAO {

    public int create(UserDTO user) throws SQLException{
        String sql = "INSERT INTO bank_user (username, password, userType) VALUES (?, ?, ?) RETURNING userId";

        try(Connection conn = DatabaseConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getUserType());
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()) {
                    int userId = rs.getInt(1);
                    user.setUserId(userId);
                    return userId;
                }
                throw new SQLException("Error creating user: no user ID returned");
            }
        }
    }
    public UserDTO findById(int userId) throws SQLException{
        String sql = "SELECT userId, username, password, userType FROM bank_user WHERE userId = ?";
        try(Connection conn = DatabaseConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, userId);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    return new UserDTO(userId, rs.getString("username"), rs.getString("password"), rs.getString("userType"));
                }
                return null;
            }
        }
    }
    public UserDTO findByUsername(String username) throws SQLException{
        String sql = "SELECT userId, password, userType FROM bank_user WHERE username = ?";
        try(Connection conn = DatabaseConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, username);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    return new UserDTO(
                            rs.getInt("userId"),
                            username,
                            rs.getString("password"),
                            rs.getString("userType")
                    );
                }
            }
        }
        return null;
    }
    public UserDTO findByUsernameAndPassword(String username, String password) throws SQLException{
        String sql = "SELECT userId, password, userType FROM bank_user WHERE username = ? AND password = ?";
        try(Connection conn = DatabaseConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, username);
            ps.setString(2, password);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    return new UserDTO(
                            rs.getInt("userId"),
                            username,
                            password,
                            rs.getString("userType")
                    );
                }
            }
        }
        return null;
    }
    public List<UserDTO> findByUserType(String userType) throws SQLException{
        List<UserDTO> list = new ArrayList<UserDTO>();
        String sql = "SELECT userId, username, password FROM bank_user WHERE userType = ?";
        try(Connection conn = DatabaseConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, userType);
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    list.add(
                            new UserDTO(
                                rs.getInt("userId"),
                                rs.getString("username"),
                                rs.getString("password"),
                                userType
                            )
                    );
                }
            }
        }
        return list;
    }

    public boolean update(UserDTO user) throws SQLException{
        String sql = "UPDATE bank_user SET username = ?, password = ?, userType = ? WHERE userId = ?";
        try(Connection conn = DatabaseConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getUserType());
            ps.setInt(4, user.getUserId());
            return ps.executeUpdate() > 0;
        }
    }
    public boolean delete(int userId) throws SQLException{
        String sql = "DELETE FROM bank_user WHERE userId = ?";
        try(Connection conn = DatabaseConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;
        }
    }

    public List<UserDTO> findByApplicationId(int applicationId) throws SQLException{
        List<UserDTO> list = new ArrayList<UserDTO>();
        String sql = "SELECT bank_user.userId, username, password, userType FROM bank_user INNER JOIN applicant ON bank_user.userId = applicant.userId WHERE applicationId = ?";
        try(Connection conn = DatabaseConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, applicationId);
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    list.add(new UserDTO(rs.getInt("userId"), rs.getString("username"), rs.getString("password"), rs.getString("userType")));
                }
            }
        }
        return list;
    }
    public List<UserDTO> findByAccountId(int accountId) throws SQLException{
        List<UserDTO> list = new ArrayList<UserDTO>();
        String sql = "SELECT bank_user.userId, username, password, userType FROM bank_user INNER JOIN hasAccount ON bank_user.userId = hasAccount.userId WHERE accountId = ?";
        try(Connection conn = DatabaseConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, accountId);
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    list.add(new UserDTO(rs.getInt("userId"), rs.getString("username"), rs.getString("password"), rs.getString("userType")));
                }
            }
        }
        return list;
    }
}
