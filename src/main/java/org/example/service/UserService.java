package org.example.service;

import org.example.repository.dto.UserDTO;
import org.example.repository.UserDAO;
import java.sql.SQLException;
import java.util.List;

public class UserService {
    private final UserDAO userDAO;

    public UserService(UserDAO userDAO){
        this.userDAO = userDAO;
    }

    public UserDTO create(UserDTO user) throws SQLException{
            if(user.getUsername() == null)
                return null;
            if(user.getPassword() == null)
                return null;
            int userId =  userDAO.create(user);
            user.setUserId(userId);
            return user;
    }

    public UserDTO findByUsername(String username) throws SQLException{
        if(username == null)
            return null;
        return userDAO.findByUsername(username);
    }

    public UserDTO findByUsernameAndPassword(String username, String password) throws SQLException{
        if(username == null || password == null)
            return null;
        return userDAO.findByUsernameAndPassword(username, password);
    }

    public List<UserDTO> findByUserType(String userType) throws SQLException{
        if(userType == null)
            return null;
        return userDAO.findByUserType(userType);
    }

    public UserDTO findById(int id) throws SQLException{
        return userDAO.findById(id);
    }
}
