package org.example;

import java.util.Scanner;
import org.example.controller.UserController;
import org.example.controller.ApplicationController;
import org.example.controller.AccountController;
import org.example.repository.dto.UserDTO;


public class Main {
    private final Scanner scanner = new Scanner(System.in);
    private final UserController userController = new UserController(scanner);
    private final ApplicationController applicationController = new ApplicationController(scanner);
    private final AccountController accountController = new AccountController(scanner);
    private UserDTO user;

    static void main(){
        new Main().start();
    }

    public void start(){
        boolean isActiveSession = true;
        while(isActiveSession){
            System.out.println("Hello! Please make a selection:");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");

            switch(scanner.nextLine().trim()) {
                case ("1"):
                    user = userController.handleRegistration();
                    if(user != null){
                        System.out.println("Registration Successful. You may now log in. Returning to main menu.");
                    }else{
                        System.out.println("There was an error during registration. Returning to main menu.");
                    }
                    break;
                case ("2"):
                    user = userController.handleLogin();
                    if(user == null){
                        System.out.println("Login Unsuccessful. Returning to main menu.");
                    }else if(user.getUserType().equals("customer")){
                        customerStart(user);
                    }else if(user.getUserType().equals("employee")){
                        employeeStart(user);
                    }else{
                        throw new RuntimeException("Unexpected account type for user");
                    }
                    break;
                case ("3"):
                    System.out.println("Goodbye!");
                    isActiveSession = false;
                    break;
                default:
                    System.out.println("Invalid Input. Try again please.");
            }
        }
    }
    public void customerStart(UserDTO customer){
        boolean isCustomerSessionActive = true;
        while(isCustomerSessionActive) {
            System.out.println("Welcome " + customer.getUsername() + "! Please make a selection:");
            System.out.println("1. Exit");
            System.out.println("2. See your personal details");
            System.out.println("3. Apply for a new account");
            System.out.println("4. View your account applications");
            System.out.println("5. Make a deposit");
            System.out.println("6. Make a withdrawal");
            System.out.println("7. Initiate a transfer");
            switch (scanner.nextLine().trim()) {
                case "1":
                    isCustomerSessionActive = false;
                    break;
                case "2":
                    System.out.println("Here are your details:");
                    System.out.println(customer.toString());
                    break;
                case "3":
                    applicationController.handleSubmitApplication(customer.getUserId());
                    break;
                case "4":
                    applicationController.handleViewApplications(customer.getUserId());
                    break;
                case "5":
                    accountController.handleDeposit(customer.getUserId());
                    break;
                case "6":
                    accountController.handleWithdrawal(customer.getUserId());
                    break;
                case "7":
                    accountController.handleTransfer(customer.getUserId());
                    break;
                default:
                    System.out.println("Invalid Selection. Please try again.");
            }
        }
    }
    public void employeeStart(UserDTO employee){
        boolean isEmployeeSessionActive = true;
        while(isEmployeeSessionActive) {
            System.out.println("Welcome " + employee.getUsername() + "! Please make a selection:");
            System.out.println("1. Exit");
            System.out.println("2. Review pending applications");
            System.out.println("3. View customers");
            System.out.println("4. View information on a customer");
            switch(scanner.nextLine().trim()){
                case "1":
                    isEmployeeSessionActive = false;
                    break;
                case "2":
                    applicationController.handleReviewApplication();
                    break;
                case "3":
                    userController.handleViewCustomers();
                    break;
                case "4":
                    userController.handleViewCustomer();
                    break;
                default:
                    System.out.println("Invalid selection. Please Try again.");
            }
        }

    }
}
