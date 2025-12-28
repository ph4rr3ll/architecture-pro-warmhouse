package com.smarthome.temperature.dto;

public class TemperatureResponse {
    
    private double value;
    private String location;
    private String sensorId;
    private String unit;

    public TemperatureResponse() {
    }

    public TemperatureResponse(double value, String location, String sensorId, String unit) {
        this.value = value;
        this.location = location;
        this.sensorId = sensorId;
        this.unit = unit;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSensorId() {
        return sensorId;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}

