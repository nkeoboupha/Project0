package org.example.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Connection;
import org.example.util.DatabaseConnectionFactory;
import java.util.List;
import java.util.ArrayList;
import org.example.repository.dto.UserDTO;
import org.example.repository.dto.AccountDTO;

public class AccountDAO {

    public AccountDTO create(List<UserDTO> users) throws SQLException{
        String sql;
        for(UserDTO user : users) {
            sql = "SELECT 1 FROM bank_user WHERE userId = ? AND userType = ?";
            try (Connection conn = DatabaseConnectionFactory.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, user.getUserId());
                ps.setString(2, "customer");
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next())
                        return null;
                }
            }
        }
        sql = "INSERT INTO account (balance) VALUES (0.0) RETURNING accountId";
        AccountDTO account;
        try(Connection conn = DatabaseConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next())
                    account = new AccountDTO(rs.getInt("accountId"), 0.0);
                else
                    throw new SQLException("Unable to create new account");
            }
        }
        for(UserDTO user : users) {
            sql = "INSERT INTO hasAccount (userId, accountId) VALUES (?, ?) RETURNING userId";
            try (Connection conn = DatabaseConnectionFactory.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, user.getUserId());
                ps.setInt(2, account.getAccountId());
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next())
                        throw new SQLException("Unable to assign user to account");
                }
            }
        }
        return account;
    }
    public AccountDTO findByID(int accountId) throws SQLException{
        String sql = "SELECT balance FROM account WHERE accountId = ?";
        try(Connection conn = DatabaseConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, accountId);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    return new AccountDTO(accountId, rs.getDouble("balance"));
                }
                return null;
            }
        }
    }
    public List<AccountDTO> findByUserId(int userId) throws SQLException{
        List<AccountDTO> list = new ArrayList<AccountDTO>();
        String sql = "SELECT account.accountId, balance FROM account INNER JOIN hasAccount on account.accountId = hasAccount.accountId WHERE userId = ?";
        try(Connection conn = DatabaseConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, userId);
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    list.add(new AccountDTO(rs.getInt("accountId"), rs.getDouble("balance")));
                }
            }
        }
        return list;
    }

    public boolean update(AccountDTO account) throws SQLException{
        String sql = "UPDATE account SET balance = ? WHERE accountId = ?";
        try(Connection conn = DatabaseConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setDouble(1, account.getBalance());
            ps.setInt(2, account.getAccountId());
            return ps.executeUpdate() > 0;
        }
    }
}
