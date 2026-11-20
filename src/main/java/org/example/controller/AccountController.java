package org.example.controller;

import org.example.service.AccountService;
import org.example.repository.dto.AccountDTO;
import org.example.repository.AccountDAO;
import org.example.repository.UserDAO;
import java.util.Scanner;
import java.sql.SQLException;
import java.util.List;

public class AccountController {
    private final Scanner scanner;
    private final AccountService accountService = new AccountService(new AccountDAO(), new UserDAO());

    public AccountController(Scanner scanner){
        this.scanner = scanner;
    }

    public void handleDeposit(int userId){
        List<AccountDTO> accounts;
        try{
            accounts = accountService.findByUserId(userId);
        }catch(SQLException e){
            System.out.printf(e.getMessage());
            System.out.println("Error finding your accounts. Returning to main menu");
            return;
        }
        if(accounts.size() == 0){
            System.out.println("You do not have an account. Returning to main menu.");
        }
        boolean isDepositing = true;
        while(isDepositing) {
            System.out.println("Select the account that you would like to deposit into or enter 0 to exit:");
            for (int i = 0; i < accounts.size(); i++) {
                AccountDTO account = accounts.get(i);
                System.out.println(Integer.toString(i + 1) + ". ID = " + account.getAccountId() + "\tBalance: " + account.getBalance());
            }
            int selection;
            try{
                selection = scanner.nextInt();
                scanner.nextLine();
            }catch(Exception e){
                System.out.println("Error reading your selection. Please try again.");
                continue;
            }
            if(selection < 0 || selection > accounts.size()){
                System.out.println("Value must be between 0 and " + accounts.size() + ". Please try again.");
                continue;
            }
            if(selection == 0){
                System.out.println("Returning to main menu");
                return;
            }
            AccountDTO account = accounts.get(selection - 1);
            System.out.println("How much would you like to deposit? ");
            boolean isValidAmount = false;
            double amount = 0.0;
            while(!isValidAmount){
                try{
                    amount = scanner.nextDouble();
                    scanner.nextLine();
                    isValidAmount = true;
                }catch(Exception e){
                    System.out.println("Could not parse your input.");
                    System.out.println("Please enter a valid amount: ");
                }
            }
            boolean success;
            try {
                success = accountService.deposit(account, amount);
                if(success){
                    System.out.println("Deposit Successful");
                }else{
                    System.out.println("Deposit Unsuccessful");
                }
            }catch(SQLException e){
                System.out.printf(e.getMessage());
                System.out.println("Error depositing funds.");
            }finally{
                System.out.println("Returning to main menu");
            }
            return;
        }
    }
    public void handleWithdrawal(int userId){
        List<AccountDTO> accounts;
        try{
            accounts = accountService.findByUserId(userId);
        }catch(SQLException e){
            System.out.printf(e.getMessage());
            System.out.println("Error finding your accounts. Returning to main menu");
            return;
        }
        if(accounts.size() == 0){
            System.out.println("You do not have an account. Returning to main menu.");
        }
        boolean isWithdrawing = true;
        while(isWithdrawing) {
            System.out.println("Select the account that you would like to withdraw from or enter 0 to exit:");
            for (int i = 0; i < accounts.size(); i++) {
                AccountDTO account = accounts.get(i);
                System.out.println(Integer.toString(i + 1) + ". ID = " + account.getAccountId() + "\tBalance: " + account.getBalance());
            }
            int selection;
            try{
                selection = scanner.nextInt();
                scanner.nextLine();
            }catch(Exception e){
                System.out.println("Error reading your selection. Please try again.");
                continue;
            }
            if(selection < 0 || selection > accounts.size()){
                System.out.println("Value must be between 0 and " + accounts.size() + ". Please try again.");
                continue;
            }
            if(selection == 0){
                System.out.println("Returning to main menu");
                return;
            }
            AccountDTO account = accounts.get(selection - 1);
            System.out.println("How much would you like to withdraw? ");
            boolean isValidAmount = false;
            double amount = 0.0;
            while(!isValidAmount){
                try{
                    amount = scanner.nextDouble();
                    scanner.nextLine();
                    isValidAmount = true;
                }catch(Exception e){
                    System.out.println("Could not parse your input.");
                    System.out.println("Please enter a valid amount: ");
                }
            }
            boolean success;
            try {
                success = accountService.withdraw(account, amount);
                if(success){
                    System.out.println("Withdrawal Successful");
                }else{
                    System.out.println("Withdrawal Unsuccessful");
                }
            }catch(SQLException e){
                System.out.printf(e.getMessage());
                System.out.println("Error depositing funds.");
            }finally{
                System.out.println("Returning to main menu");
            }
            return;
        }
    }
    public void handleTransfer(int userId){
        List<AccountDTO> accounts;
        try{
            accounts = accountService.findByUserId(userId);
        }catch(SQLException e){
            System.out.printf(e.getMessage());
            System.out.println("Error finding your accounts. Returning to main menu");
            return;
        }
        if(accounts.size() == 0){
            System.out.println("You do not have an account. Returning to main menu.");
        }
        AccountDTO accountFrom = null;
        AccountDTO accountTo = null;
        boolean isSelectingFrom = true;
        int selectionFrom;
        while(isSelectingFrom) {
            System.out.println("Select the account that you would like to withdraw from or enter 0 to exit:");
            for (int i = 0; i < accounts.size(); i++) {
                AccountDTO account = accounts.get(i);
                System.out.println(Integer.toString(i + 1) + ". ID = " + account.getAccountId() + "\tBalance: " + account.getBalance());
            }
            try {
                selectionFrom = scanner.nextInt();
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println("Error reading your selection. Please try again.");
                continue;
            }
            if (selectionFrom < 0 || selectionFrom > accounts.size()) {
                System.out.println("Value must be between 0 and " + accounts.size() + ". Please try again.");
                continue;
            }
            if (selectionFrom == 0) {
                System.out.println("Returning to main menu");
                return;
            }
            accountFrom = accounts.get(selectionFrom - 1);
            isSelectingFrom = false;
        }
        boolean isSelectingTo = true;
        int selectionTo;
        while(isSelectingTo) {
            System.out.println("Select the account that you would like to deposit into or enter 0 to exit:");
            for (int i = 0; i < accounts.size(); i++) {
                AccountDTO account = accounts.get(i);
                System.out.println(Integer.toString(i + 1) + ". ID = " + account.getAccountId() + "\tBalance: " + account.getBalance());
            }
            try {
                selectionTo = scanner.nextInt();
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println("Error reading your selection. Please try again.");
                continue;
            }
            if (selectionTo < 0 || selectionTo > accounts.size()) {
                System.out.println("Value must be between 0 and " + accounts.size() + ". Please try again.");
                continue;
            }
            if (selectionTo == 0) {
                System.out.println("Returning to main menu");
                return;
            }
            accountTo = accounts.get(selectionTo - 1);
            isSelectingTo = false;
        }
            System.out.println("How much would you like to transfer? ");
            boolean isValidAmount = false;
            double amount = 0.0;
            while(!isValidAmount){
                try{
                    amount = scanner.nextDouble();
                    scanner.nextLine();
                    isValidAmount = true;
                }catch(Exception e){
                    System.out.println("Could not parse your input.");
                    System.out.println("Please enter a valid amount: ");
                }
            }
            boolean success;
            try {
                success = accountService.transfer(accountFrom, accountTo, amount);
                if(success){
                    System.out.println("Transfer Successful");
                }else{
                    System.out.println("Transfer Unsuccessful");
                }
            }catch(SQLException e){
                System.out.printf(e.getMessage());
                System.out.println("Error transferring funds.");
            }finally{
                System.out.println("Returning to main menu");
            }
            return;

    }
}
