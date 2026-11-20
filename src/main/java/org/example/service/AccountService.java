package org.example.service;

import org.example.repository.AccountDAO;
import org.example.repository.dto.AccountDTO;
import org.example.repository.UserDAO;
import org.example.repository.dto.UserDTO;
import java.sql.SQLException;
import java.util.List;

public class AccountService {
    private final AccountDAO accountDAO;
    private final UserDAO userDAO;

    public AccountService(AccountDAO accountDAO, UserDAO userDAO){
        this.accountDAO = accountDAO;
        this.userDAO = userDAO;
    }

    public AccountDTO create(int applicationId) throws SQLException{
        //Get users applying for this account
        List<UserDTO> users = userDAO.findByApplicationId(applicationId);
        //Give them the account
        return accountDAO.create(users);
    }

    public List<AccountDTO> findByUserId(int userId) throws SQLException{
        return accountDAO.findByUserId(userId);
    }

    private boolean update(AccountDTO account) throws SQLException{
        if(account.getBalance() < 0.0)
            return false;
        return accountDAO.update(account);
    }

    public boolean deposit(AccountDTO account, double amount) throws SQLException{
        if(amount < 0.0)
            return false;
        AccountDTO updatedAccount = new AccountDTO(account.getAccountId(), account.getBalance() + amount);
        return update(updatedAccount);
    }

    public boolean withdraw(AccountDTO account, double amount) throws SQLException{
        if(amount < 0.0 || amount > account.getBalance())
            return false;
        AccountDTO updatedAccount = new AccountDTO(account.getAccountId(), account.getBalance() - amount);
        return update(updatedAccount);
    }

    public boolean transfer(AccountDTO accountFrom, AccountDTO accountTo, double amount) throws SQLException{
        if(amount > accountFrom.getBalance())
            return false;
        if(amount < 0.0)
            return false;
        AccountDTO updatedAccountFrom = new AccountDTO(accountFrom.getAccountId(), accountFrom.getBalance() - amount);
        AccountDTO updatedAccountTo = new AccountDTO(accountTo.getAccountId(), accountTo.getBalance() + amount);
        if(update(updatedAccountFrom) && update(updatedAccountTo))
            return true;
        else{
            update(accountFrom);
            update(accountTo);
            return false;
        }
    }

}
