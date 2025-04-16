/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Finance;

import java.util.ArrayList;
/**
 *
 * @author User
 */
public class FinanceReportManager {
     // List to hold finance reports
    private ArrayList<FinanceReport> reports;

    public FinanceReportManager() {
        this.reports = new ArrayList<>();
    }
    
    // Method to generate a new finance report and add it to the list
    public void generateReport(int reportID, String date, double totalRevenue, String status) {
        // Create a new FinanceReport object
        FinanceReport newReport = new FinanceReport(reportID, date, totalRevenue, status);
        
        // Add the new report to the list of reports
        reports.add(newReport);
    }

    // Method to get all finance reports
    public ArrayList<FinanceReport> getAllReports() {
        return reports;
    }
   // Method to find a report by its ID
    public FinanceReport getReportByID(int reportID) {
        for (FinanceReport report : reports) {
            if (report.getReportID() == reportID) {
                return report; // Return the report if ID matches
            }
        }
        return null; // Return null if no report is found with the given ID
    }

    // Method to display all reports
    public void displayAllReports() {
        for (FinanceReport report : reports) {
            report.displayFinanceReport(); // Calls the method in FinanceReport class
        }
    }
    
    }
    
    

