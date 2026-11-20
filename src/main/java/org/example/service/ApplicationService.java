package org.example.service;

import org.example.repository.ApplicationDAO;
import java.sql.SQLException;
import java.util.List;
import org.example.repository.dto.ApplicationDTO;

public class ApplicationService {
    private final ApplicationDAO applicationDAO;

    public ApplicationService(ApplicationDAO applicationDAO){
        this.applicationDAO = applicationDAO;
    }

    public ApplicationDTO submitApplication(int userId) throws SQLException{
        return applicationDAO.create(userId);
    }

    public List<ApplicationDTO> getPendingApplications() throws SQLException{
        return applicationDAO.findByStatus("pending");
    }

    public boolean update(ApplicationDTO application) throws SQLException{
        return applicationDAO.update(application);
    }

    public List<ApplicationDTO> findByUserId(int userId) throws SQLException{
        return applicationDAO.findByUserId(userId);
    }
}
