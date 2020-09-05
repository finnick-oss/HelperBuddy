package com.example.helperbuddy.HelperClasses;

import java.util.HashMap;
import java.util.List;

public class Request {
    private String workerName,workerPhone,workerOccupation,workerAddress;


    public Request() {

    }

    public Request(String workerName, String workerPhone, String workerOccupation, String workerAddress) {
        this.workerName = workerName;
        this.workerPhone = workerPhone;
        this.workerOccupation = workerOccupation;
        this.workerAddress = workerAddress;
    }

    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    public String getWorkerPhone() {
        return workerPhone;
    }

    public void setWorkerPhone(String workerPhone) {
        this.workerPhone = workerPhone;
    }

    public String getWorkerOccupation() {
        return workerOccupation;
    }

    public void setWorkerOccupation(String workerOccupation) {
        this.workerOccupation = workerOccupation;
    }

    public String getWorkerAddress() {
        return workerAddress;
    }

    public void setWorkerAddress(String workerAddress) {
        this.workerAddress = workerAddress;
    }
}
