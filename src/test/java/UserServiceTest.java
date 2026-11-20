import org.example.repository.dto.UserDTO;
import org.example.repository.UserDAO;
import org.example.service.UserService;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserDAO userDAO;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("createUser should successfully create user")
    void createUser_Success() throws SQLException{
        //Arrange
        UserDTO newUserDTO = new UserDTO("user", "pass", "customer");
        when(userDAO.create(newUserDTO)).thenReturn(1);
        //Act
        UserDTO returnedUser = userService.create(newUserDTO);
        //Assert
        assertNotNull(returnedUser);
        assertEquals(1, returnedUser.getUserId());
    }

    @Test
    @DisplayName("createUser should return null when username is null")
    void createUser_NullUsername_ReturnsNull() throws SQLException{
        //Arrange
        UserDTO nullUsernameUser = new UserDTO(null, "pass", "customer");
        //Act
        UserDTO returnedUser = userService.create(nullUsernameUser);
        //Assert
        assertNull(returnedUser);
    }

    @Test
    @DisplayName("createUser should return null when password is null")
    void createUser_NullPassword_ReturnsNull() throws SQLException{
        //Arrange
        UserDTO nullPasswordUser = new UserDTO("user", null, "customer");
        //Act
        UserDTO returnedUser = userService.create(nullPasswordUser);
        //Assert
        assertNull(returnedUser);
    }

    @Test
    @DisplayName("findByUsername should return user when valid username provided")
    void findByUsername_Success() throws SQLException{
        //Arrange
        UserDTO userToReturn = new UserDTO(1, "user", "pass", "customer");
        when(userDAO.findByUsername("user")).thenReturn(userToReturn);
        //Act
        UserDTO returnedUser = userService.findByUsername("user");
        //Assert
        assertNotNull(returnedUser);
        assertEquals(1, returnedUser.getUserId());
        assertEquals("user", returnedUser.getUsername());
        assertEquals("pass", returnedUser.getPassword());
        assertEquals("customer", returnedUser.getUserType());
    }

    @Test
    @DisplayName("findByUsername should return null when user not found")
    void findByUsername_NotFound_ReturnsNull() throws SQLException{
        //Arrange
        when(userDAO.findByUsername("invalidUser")).thenReturn(null);
        //Act
        UserDTO returnedUser = userService.findByUsername("invalidUser");
        //Assert
        assertNull(returnedUser);
    }
    @Test
    @DisplayName("findByUsername should return null when null username provided")
    void findByUsername_NullUsername_ReturnsNull() throws SQLException{
        //Arrange
        //Act
        UserDTO returnedUser = userService.findByUsername(null);
        //Assert
        assertNull(returnedUser);
    }
    @Test
    @DisplayName("findByUsernameAndPassword should return user when valid username and password provided")
    void findByUsernameAndPassword_Success() throws SQLException{
        //Arrange
        when(userDAO.findByUsernameAndPassword("user", "pass")).thenReturn(new UserDTO(1, "user", "pass", "customer"));
        //Act
        UserDTO returnedUser = userService.findByUsernameAndPassword("user", "pass");
        //Assert
        assertNotNull(returnedUser);
        assertEquals(1, returnedUser.getUserId());
        assertEquals("user", returnedUser.getUsername());
        assertEquals("pass", returnedUser.getPassword());
        assertEquals("customer", returnedUser.getUserType());
    }
    @Test
    @DisplayName("findByUsernameAndPassword should return null when user not found")
    void findByUsernameAndPassword_NotFound_ReturnsNULL() throws SQLException{
        //Arrange
        when(userDAO.findByUsernameAndPassword("invalidUser", "invalidPass")).thenReturn(null);
        //Act
        UserDTO returnedUser = userService.findByUsernameAndPassword("invalidUser", "invalidPass");
        //Assert
        assertNull(returnedUser);
    }
    @Test
    @DisplayName("findByUsernameAndPassword should return null when username is null")
    void findByUsernameAndPassword_NullUsername_ReturnsNULL() throws SQLException{
        //Arrange
        //Act
        UserDTO returnedUser = userService.findByUsernameAndPassword(null, "invalidPass");
        //Assert
        assertNull(returnedUser);
    }
    @Test
    @DisplayName("findByUsernameAndPassword should return null when password is null")
    void findByUsernameAndPassword_NullPassword_ReturnsNULL() throws SQLException{
        //Arrange
        //Act
        UserDTO returnedUser = userService.findByUsernameAndPassword(null, "invalidPass");
        //Assert
        assertNull(returnedUser);
    }
    @Test
    @DisplayName("findByUserType should always return a list of users")
    void findByUserType_Success() throws SQLException{
        //Arrange
        List<UserDTO> userList = new ArrayList<>();
        userList.add(new UserDTO(1, "user", "pass", "customer"));
        when(userDAO.findByUserType("customer")).thenReturn(userList);
        //Act
        List<UserDTO> returnedUsers = userService.findByUserType("customer");
        //Assert
        assertNotNull(returnedUsers);
        assertEquals(1, returnedUsers.size());
    }
    @Test
    @DisplayName("findById should return a user with the Id")
    void findById_Success() throws SQLException{
        //Arrange
        UserDTO userToReturn = new UserDTO(1, "user", "pass", "customer");
        when(userDAO.findById(1)).thenReturn(userToReturn);
        //Act
        UserDTO returnedUser = userService.findById(1);
        //Assert
        assertNotNull(returnedUser);
        assertEquals(1, returnedUser.getUserId());
        assertEquals("user", returnedUser.getUsername());
        assertEquals("pass", returnedUser.getPassword());
        assertEquals("customer", returnedUser.getUserType());
    }

}
