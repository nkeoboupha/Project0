import org.example.repository.AccountDAO;
import org.example.repository.dto.AccountDTO;
import org.example.repository.UserDAO;
import org.example.repository.dto.UserDTO;
import org.example.service.AccountService;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
    @Mock
    private AccountDAO accountDAO;

    @Mock
    private UserDAO userDAO;

    @InjectMocks
    private AccountService accountService;




    @Test
    @DisplayName("create should return an unfunded account for all applicants")
    void create_Success() throws SQLException{
        //Arrange
        List<UserDTO> applicants = new ArrayList<>();
        applicants.add(new UserDTO(1, "user1", "pass1", "customer"));
        applicants.add(new UserDTO(2, "user2", "pass2", "customer"));
        when(userDAO.findByApplicationId(1)).thenReturn(applicants);
        when(accountDAO.create(anyList())).thenReturn(new AccountDTO(1, 0.0));
        //Act
        AccountDTO newAccount = accountService.create(1);
        //Assert
        assertNotNull(newAccount);
        assertInstanceOf(AccountDTO.class, newAccount);
        assertEquals(0.0, newAccount.getBalance());
    }

    @Test
    @DisplayName("create should return null if not every applicant is valid")
    void create_InvalidUsers_ReturnsNull() throws SQLException{
        //Arrange
        List<UserDTO> applicants = new ArrayList<>();
        applicants.add(new UserDTO(1, "user1", "pass1", "customer"));
        applicants.add(new UserDTO(2, "user2", "pass2", "employee"));
        when(userDAO.findByApplicationId(1)).thenReturn(applicants);
        when(accountDAO.create(anyList())).thenReturn(null);
        //Act
        AccountDTO newAccount = accountService.create(1);
        //Assert
        assertNull(newAccount);
    }

    @Test
    @DisplayName("findById should always return a list of accounts")
    void findById_Success() throws SQLException{
        //Arrange
        when(accountDAO.findByUserId(1)).thenReturn(new ArrayList<>());
        //Act
        List<AccountDTO> accounts = accountService.findByUserId(1);
        //Assert
        assertNotNull(accounts);
        assertInstanceOf(List.class, accounts);
    }

    @Test
    @DisplayName("deposit should return true if a positive number and valid account are provided")
    void deposit_Success() throws SQLException{
        //Arrange
        when(accountDAO.update(any(AccountDTO.class))).thenReturn(true);
        AccountDTO account = new AccountDTO(1, 0.0);
        //Act
        //Assert
        assertTrue(accountService.deposit(account, 50.0));
    }
    @Test
    @DisplayName("deposit should return false if an invalid account is given")
    void deposit_InvalidAccount_ReturnsFalse() throws SQLException{
        //Arrange
        when(accountDAO.update(any(AccountDTO.class))).thenReturn(false);
        AccountDTO account = new AccountDTO(-1, 0.0);
        //Act
        //Assert
        assertFalse(accountService.deposit(account, 50.0));
    }
    @Test
    @DisplayName("deposit should return false if a negative amount is deposited")
    void deposit_NegativeAmount_ReturnsFalse() throws SQLException{
        //Arrange
        AccountDTO account = new AccountDTO(1, 0.0);
        //Act
        //Assert
        assertFalse(accountService.deposit(account, -50.0));
    }
    @Test
    @DisplayName("withdraw should return true if an amount up to the account's balance is withdrawn")
    void withdraw_Success() throws SQLException{
        //Arrange
        when(accountDAO.update(any(AccountDTO.class))).thenReturn(true);
        AccountDTO account = new AccountDTO(1, 100.0);
        //Act
        //Assert
        assertTrue(accountService.withdraw(account, 50.0));
    }
    @Test
    @DisplayName("withdraw should return false if an invalid account is given")
    void withdraw_InvalidAccount_ReturnsFalse() throws SQLException{
        //Arrange
        when(accountDAO.update(any(AccountDTO.class))).thenReturn(false);
        AccountDTO account = new AccountDTO(-1, 100.0);
        //Act
        //Assert
        assertFalse(accountService.deposit(account, 50.0));
    }
    @Test
    @DisplayName("withdraw should return false if a negative amount is withdrawn")
    void withdraw_NegativeAmount_ReturnsFalse() throws SQLException{
        //Arrange
        AccountDTO account = new AccountDTO(1, 100.0);
        //Act
        //Assert
        assertFalse(accountService.deposit(account, -50.0));
    }
    @Test
    @DisplayName("withdraw should return false if a an amount larger than the balance is withdrawn")
    void withdraw_AmountExceedsBalance_ReturnsFalse() throws SQLException{
        //Arrange
        AccountDTO account = new AccountDTO(1, 100.0);
        //Act
        //Assert
        assertFalse(accountService.deposit(account, -500.0));
    }
    @Test
    @DisplayName("transfer should return true if a valid amount is transferred between valid accounts")
    void transfer_Success() throws SQLException{
        //Arrange
        AccountDTO accountFrom = new AccountDTO(1, 50.0);
        AccountDTO accountTo = new AccountDTO(2, 0.0);
        when(accountDAO.update(any(AccountDTO.class))).thenReturn(true);
        //Act
        boolean success = accountService.transfer(accountFrom, accountTo, 5.0);
        //Assert
        assertTrue(success);
    }
    @Test
    @DisplayName("transfer should return false if a negative amount is transferred")
    void transfer_NegativeAmount_ReturnsFalse() throws SQLException{
        //Arrange
        AccountDTO accountFrom = new AccountDTO(1, 50.0);
        AccountDTO accountTo = new AccountDTO(2, 0.0);
        //Act
        boolean success = accountService.transfer(accountFrom, accountTo, -5.0);
        //Assert
        assertFalse(success);
    }
    @Test
    @DisplayName("transfer should return false if DAO fails update")
    void transfer_FailedUpdate_ReturnsFalse() throws SQLException{
        //Arrange
        AccountDTO accountFrom = new AccountDTO(1, 50.0);
        AccountDTO accountTo = new AccountDTO(2, 0.0);
        when(accountDAO.update(any(AccountDTO.class))).thenReturn(false);
        //Act
        boolean success = accountService.transfer(accountFrom, accountTo, 5.0);
        //Assert
        assertFalse(success);
    }
    @Test
    @DisplayName("transfer should return false if amount exceeds balance of account withdrawing from")
    void transfer_AmountExceedsBalance() throws SQLException{
        //Arrange
        AccountDTO accountFrom = new AccountDTO(1, 50.0);
        AccountDTO accountTo = new AccountDTO(2, 0.0);
        //Act
        boolean success = accountService.transfer(accountFrom, accountTo, 500.0);
        //Assert
        assertFalse(success);
    }
}
