package org.example.repository.dto;

public class ApplicationDTO {
    private int applicationId;
    private String applicationStatus;

    public ApplicationDTO(){}

    public ApplicationDTO(int applicationId, String applicationStatus){
        this.applicationId = applicationId;
        this.applicationStatus = applicationStatus;
    }

    public ApplicationDTO(String applicationStatus){
        this.applicationStatus = applicationStatus;
    }

    public int getApplicationId(){
        return this.applicationId;
    }
    public void setApplicationId(int applicationId){
        this.applicationId = applicationId;
    }
    public String getApplicationStatus(){
        return this.applicationStatus;
    }
    public void setApplicationStatus(String applicationStatus){
        this.applicationStatus = applicationStatus;
    }
    @Override
    public String toString(){
        return "Application ID: " + applicationId + "\nApplication Status: " + applicationStatus;
    }
}
