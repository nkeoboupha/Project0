package org.example.controller;

import java.util.Scanner;
import org.example.service.UserService;
import org.example.repository.dto.UserDTO;
import org.example.repository.dto.AccountDTO;
import org.example.repository.dto.ApplicationDTO;
import org.example.repository.UserDAO;
import org.example.repository.AccountDAO;
import org.example.repository.ApplicationDAO;
import java.util.List;
import java.sql.SQLException;
import org.example.service.AccountService;
import org.example.service.ApplicationService;

public class UserController {

    private Scanner scanner;
    private final UserService userService = new UserService(new UserDAO());
    private final AccountService accountService = new AccountService(new AccountDAO(), new UserDAO());
    private final ApplicationService applicationService = new ApplicationService(new ApplicationDAO());

    public UserController(Scanner scanner){
        this.scanner = scanner;
    }

    public UserDTO handleLogin(){
        System.out.println("Enter your username: ");
        String username = scanner.nextLine();
        System.out.println("Enter your password: ");
        String password = scanner.nextLine();
        try {
            return userService.findByUsernameAndPassword(username, password);
        }catch(SQLException e){
            System.out.printf(e.getMessage());
            return null;
        }
    }

    public UserDTO handleRegistration(){
        try {
            System.out.println("Enter a username: ");
            String username = scanner.nextLine();
            while (userService.findByUsername(username) != null) {
                System.out.println("That user already exists.");
                System.out.println("Enter a different username: ");
                username = scanner.nextLine();
            }
            System.out.println("Enter a password: ");
            String password = scanner.nextLine();
            return userService.create(new UserDTO(username, password, "customer"));
        }catch(SQLException e){
            System.out.printf(e.getMessage());
            return null;
        }
    }

    public void handleViewCustomers(){
        System.out.println("Here are your customers");
        try{
            for(UserDTO user : userService.findByUserType("customer")){
                System.out.println("User " + user.getUsername() + ": ID = " +user.getUserId());
            }
        }catch(SQLException e){
            System.out.printf(e.getMessage());
            System.out.println("There was an error displaying all users");
        }finally{
            System.out.println("Returning to main menu");
        }
    }

    public void handleViewCustomer(){
        System.out.println("Enter the ID of the customer you would like to view: ");
        int userId;
        try{
            userId = scanner.nextInt();
            scanner.nextLine();
        }catch(Exception e){
            System.out.println("There was an error parsing the ID that you entered. Returning to main menu");
            return;
        }
        UserDTO user;
        try {
            user = userService.findById(userId);
        }catch(SQLException e){
            System.out.printf(e.getMessage());
            System.out.println("There was an error finding that user. Returning to main menu");
            return;
        }
        if(user == null){
            System.out.println("The User ID that you entered is invalid. Returning to main menu");
            return;
        }
        List<AccountDTO> accounts;
        try{
            accounts = accountService.findByUserId(user.getUserId());
        }catch(SQLException e){
            System.out.printf(e.getMessage());
            System.out.println("There was an error getting accounts for user " + user.getUserId());
            System.out.println("Returning to main menu");
            return;
        }
        List<ApplicationDTO> applications;
        try{
            applications = applicationService.findByUserId(user.getUserId());
        }catch(SQLException e){
            System.out.println(e.getMessage());
            System.out.println("There was an error getting applications for user " + user.getUserId());
            System.out.println("Returning to main menu");
            return;
        }
        System.out.println("\nHere are the details for that user:");
        System.out.println(user.toString());
        System.out.println("Accounts:");
        for(AccountDTO account : accounts){
            System.out.println("Account ID: " + account.getAccountId() + "\tBalance: " + account.getBalance());
        }
        System.out.println("Applications:");
        for(ApplicationDTO application : applications){
            System.out.println("Application Id: " + application.getApplicationId() + "\tStatus: " + application.getApplicationStatus());
        }
    }
}
