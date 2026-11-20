import org.example.repository.ApplicationDAO;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import org.example.service.ApplicationService;
import org.example.repository.dto.ApplicationDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ApplicationServiceTest {
    @Mock
    private ApplicationDAO applicationDAO;

    @InjectMocks
    private ApplicationService applicationService;

    @Test
    @DisplayName("submitApplication should return application when given valid userId")
    void submitApplication_Success() throws SQLException{
        //Arrange
        when(applicationDAO.create(1)).thenReturn(new ApplicationDTO(1, "pending"));
        //Act
        ApplicationDTO application = applicationService.submitApplication(1);
        //Assert
        assertNotNull(application);
        assertEquals(1, application.getApplicationId());
        assertEquals("pending", application.getApplicationStatus());
    }

    @Test
    @DisplayName("submitApplication should return null when given an invalid userId")
    void submitApplication_InvalidId_ReturnsNull() throws SQLException{
        //Arrange
        when(applicationDAO.create(-1)).thenReturn(null);
        //Act
        ApplicationDTO application = applicationService.submitApplication(-1);
        //Assert
        assertNull(application);
    }

    @Test
    @DisplayName("getPendingApplications should always return a list of applications")
    void getPendingApplications_Success() throws SQLException{
        //Arrange
        when(applicationDAO.findByStatus("pending")).thenReturn(new ArrayList<>());
        //Act
        List<ApplicationDTO> applicationList = applicationService.getPendingApplications();
        //Assert
        assertNotNull(applicationList);
    }

    @Test
    @DisplayName("update should return true if update successful in dao")
    void update_Success() throws SQLException{
        //Arrange
        when(applicationDAO.update(any(ApplicationDTO.class))).thenReturn(true);
        //Act
        boolean hasUpdated = applicationService.update(new ApplicationDTO(1, "pending"));
        //Assert
        assertTrue(hasUpdated);
    }
    @Test
    @DisplayName("update should return false if update unsuccessful in dao")
    void update_Unsuccessful() throws SQLException{
        //Arrange
        when(applicationDAO.update(any(ApplicationDTO.class))).thenReturn(false);
        //Act
        boolean hasUpdated = applicationService.update(new ApplicationDTO(1, "pending"));
        //Assert
        assertFalse(hasUpdated);
    }
    @Test
    @DisplayName("findByUserId should always return a list of applications")
    void findByUserId_Success() throws SQLException{
        //Arrange
        when(applicationDAO.findByUserId(-1)).thenReturn(new ArrayList<>());
        //Act
        List<ApplicationDTO> applications = applicationService.findByUserId(-1);
        //Assert
        assertNotNull(applications);
        assertInstanceOf(List.class, applications);
    }
}
