package com.example.barber.model;

public class HairdresserStatistics {
    private String frizer;
    private int totalAppointments;
    private int completedAppointments;
    private int canceledAppointments;

    public String getFrizer()  { return frizer; }
    public void setFrizer(String frizer) { this.frizer = frizer; }
    public int getTotalAppointments() { return totalAppointments; }
    public void setTotalAppointments(int totalAppointments) { this.totalAppointments = totalAppointments; }
    public int getCompletedAppointments() { return completedAppointments; }
    public void setCompletedAppointments(int completedAppointments) { this.completedAppointments = completedAppointments; }
    public int getCanceledAppointments() { return canceledAppointments; }
    public void setCanceledAppointments(int canceledAppointments) { this.canceledAppointments = canceledAppointments; }
}
