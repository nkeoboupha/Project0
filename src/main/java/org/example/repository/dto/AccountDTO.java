package org.example.repository.dto;

public class AccountDTO {
    private int accountId;
    private double balance;

    public AccountDTO(){}

    public AccountDTO(int accountId, double balance){
        this.accountId = accountId;
        this.balance = balance;
    }

    public AccountDTO(int balance){
        this.balance = balance;
    }

    public int getAccountId(){
        return accountId;
    }
    public double getBalance(){
        return balance;
    }
    public void setAccountId(int accountId){
        this.accountId = accountId;
    }
    public void setBalance(double balance){
        this.balance = balance;
    }
}
