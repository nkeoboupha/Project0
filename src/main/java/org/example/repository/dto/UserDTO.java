package org.example.repository.dto;

public class UserDTO {
    private int userId;
    private String username;
    private String password;
    private String userType;

    public UserDTO(){
    }

    public UserDTO(int userId, String username, String password, String userType){
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.userType = userType;
    }

    public UserDTO(String username, String password, String userType){
        this.username = username;
        this.password = password;
        this.userType = userType;
    }

    public int getUserId(){
        return userId;
    }
    public void setUserId(int userId){
        this.userId = userId;
    }
    public String getUsername(){
        return username;
    }
    public void setUsername(String username){
        this.username = username;
    }
    public String getPassword(){
        return password;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public String getUserType(){
        return userType;
    }
    public void setUserType(String userType){
        this.userType = userType;
    }
    @Override
    public String toString(){
        return "User Id: " + this.userId + "\n" +
                "Username: " + this.username + "\n" +
                "Password: " + this.password + "\n" +
                "User Type: " + this.userType;
    }
}
