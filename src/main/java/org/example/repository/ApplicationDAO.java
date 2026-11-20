package org.example.repository;

import java.util.List;
import java.util.ArrayList;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import org.example.repository.dto.ApplicationDTO;
import org.example.util.DatabaseConnectionFactory;

public class ApplicationDAO {
    public ApplicationDTO create(int userId) throws SQLException{
        String sql = "SELECT 1 FROM bank_user WHERE userId = ?";
        try(Connection conn = DatabaseConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, userId);
            try(ResultSet rs = ps.executeQuery()){
                if(!rs.next()) {
                    return null;
                }
            }
        }
        sql = "INSERT INTO application (applicationStatus) VALUES (?) RETURNING applicationId";
        ApplicationDTO application;
        try(Connection conn = DatabaseConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, "pending");
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()) {
                    application = new ApplicationDTO(rs.getInt("applicationId"), "pending");
                }else{
                    throw new SQLException("Unable to create new application");
                }
            }
        }
        sql = "INSERT INTO applicant (userId, applicationId) VALUES (?, ?) returning applicationId";
        try(Connection conn = DatabaseConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, userId);
            ps.setInt(2, application.getApplicationId());
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    return application;
                }else{
                    throw new SQLException("Unable to update applicant table");
                }
            }
        }
    }
    public ApplicationDTO findById(int applicationId) throws SQLException{
        String sql = "SELECT applicationStatus FROM application WHERE applicationId = ?";
        try(Connection conn = DatabaseConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, applicationId);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    return new ApplicationDTO(applicationId, rs.getString("applicationStatus"));
                }else{
                    throw new IllegalArgumentException("Invalid ID provided");
                }
            }
        }
    }
    public List<ApplicationDTO> findByUserId(int userId) throws SQLException{
        List<ApplicationDTO>  list = new ArrayList<ApplicationDTO>();
        String sql = "SELECT application.applicationId, applicationStatus FROM application INNER JOIN applicant ON application.applicationId = applicant.applicationId WHERE userId = ?";
        try(Connection conn = DatabaseConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, userId);
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    list.add(new ApplicationDTO(rs.getInt("applicationId"), rs.getString("applicationStatus")));
                }
            }
        }
        return list;
    }
    public List<ApplicationDTO> findByStatus(String status) throws SQLException{
        List<ApplicationDTO> list = new ArrayList<ApplicationDTO>();
        String sql = "SELECT applicationId FROM application WHERE applicationStatus = ?";
        try(Connection conn = DatabaseConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, status);
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    list.add(new ApplicationDTO(rs.getInt("applicationId"), status));
                }
            }
        }
        return list;
    }
    public boolean update(ApplicationDTO application) throws SQLException{
        String sql = "UPDATE application SET applicationStatus = ? WHERE applicationId = ?";
        try(Connection conn = DatabaseConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, application.getApplicationStatus());
            ps.setInt(2, application.getApplicationId());
            return ps.executeUpdate() > 0;
        }
    }
}
