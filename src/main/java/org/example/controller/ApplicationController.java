package org.example.controller;

import java.util.Scanner;
import org.example.service.ApplicationService;
import java.util.List;
import org.example.repository.dto.ApplicationDTO;
import org.example.repository.ApplicationDAO;
import org.example.repository.AccountDAO;
import org.example.repository.UserDAO;
import java.sql.SQLException;
import java.lang.Integer;
import org.example.service.AccountService;

public class ApplicationController {
    private final Scanner scanner;
    private final ApplicationService applicationService = new ApplicationService(new ApplicationDAO());
    private final AccountService accountService = new AccountService(new AccountDAO(), new UserDAO());

    public ApplicationController(Scanner scanner){
        this.scanner = scanner;
    }

    public void handleSubmitApplication(int userId){
        try {
            ApplicationDTO application = applicationService.submitApplication(userId);
            System.out.println("Application submitted. Your application ID is " + application.getApplicationId());
        }catch(SQLException e){
            System.out.printf(e.getMessage());
            System.out.println("There was an error submitting your application");
        }finally{
            System.out.println("Returning to customer menu");
        }
    }

    public void handleViewApplications(int userId){
        System.out.println("Here are your applications:");
        try{
            for(ApplicationDTO application : applicationService.findByUserId(userId)){
                System.out.println("Application " + application.getApplicationId() + ": " + application.getApplicationStatus());
            }
        }catch(SQLException e){
            System.out.printf(e.getMessage());
            System.out.println("There was an error displaying all of your applications");
        }finally{
            System.out.println("Returning to main menu");
        }
    }

    public void handleReviewApplication(){
        List<ApplicationDTO> applicationList;
        try {
            applicationList = applicationService.getPendingApplications();
        }catch(SQLException e){
            System.out.printf(e.getMessage());
            System.out.println("Error getting pending applications. Returning to employee menu.");
            return;
        }
        boolean isSelectingApplication = true;
        int selection = -1;
        while(isSelectingApplication) {
            System.out.println("Select an application to review or enter 0 to exit");
            System.out.println("0. Exit");
            for (int i = 0; i < applicationList.size(); i++) {
                System.out.println(Integer.toString(i + 1) + ". " + applicationList.get(i).getApplicationId());
            }
            boolean validIntegerEntered = false;
            try {
                selection = scanner.nextInt();
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println("Invalid Input. Please try again.");
                continue;
            }
            if (selection < 0 || selection > applicationList.size()) {
                System.out.println("Invalid integer entered");
                System.out.println("Valid values are between 0 and " + applicationList.size());
                continue;
            }
            if (selection == 0) {
                System.out.println("Returning to employee menu");
                return;
            }
            isSelectingApplication = false;
        }
            ApplicationDTO application = applicationList.get(selection - 1);
            System.out.println("What is your decision on application " + application.getApplicationId() + "?");
            System.out.println("Enter your selection:");
            System.out.println("1. Exit");
            System.out.println("2. Approve");
            System.out.println("3. Deny");
            String decisionInput = scanner.nextLine().trim();
            System.out.println(decisionInput);
            switch(decisionInput){
                case "1":
                    System.out.println("Returning to application selection");
                    break;
                case "2":
                    try{
                        application.setApplicationStatus("approved");
                        applicationService.update(application);
                        accountService.create(application.getApplicationId());
                        System.out.println("Application Approved!");
                    }catch(SQLException e){
                        System.out.printf(e.getMessage());
                        System.out.println("There was an error in approving the application. Returning to main menu");
                        return;
                    }
                    break;
                case "3":
                    try{
                        application.setApplicationStatus("denied");
                        applicationService.update(application);
                        System.out.println("Application Denied!");
                    }catch(SQLException e){
                        System.out.printf(e.getMessage());
                        System.out.println("There was an error in denying the application. Returning to main menu");
                        return;
                    }
                    break;
            }

    }
}
