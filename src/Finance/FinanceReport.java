/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Finance;


import java.util.Date;
/**
 *
 * @author User
 */
public class FinanceReport {
    private int reportID;           // Unique identifier for the report
    private String date;            // Date of the report
    private double totalRevenue;    // Total revenue for the report
    private String status;          // Status of the report (e.g., Pending, Completed)

    public FinanceReport(int reportID, String date, double totalRevenue, String status) {
        this.reportID = reportID;
        this.date = date;
        this.totalRevenue = totalRevenue;
        this.status = status;
    }

    public int getReportID() {
        return reportID;
    }

    public void setReportID(int reportID) {
        this.reportID = reportID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    // Method to display report details (optional)
    public void displayFinanceReport() {
        System.out.println("Report ID: " + reportID);
        System.out.println("Date: " + date);
        System.out.println("Total Revenue: " + totalRevenue);
        System.out.println("Status: " + status);
    } 
}
