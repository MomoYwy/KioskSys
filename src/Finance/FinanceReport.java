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
    private String grossSales;
    private String discount;
    private String netTotal;
    private String date;
    private String FRepID;
    private String status;
    
        public FinanceReport(String grossSales, String discount, String netTotal, String date, String day, String status) {
        this.grossSales = grossSales;
        this.discount = discount;
        this.netTotal = netTotal;
        this.date = date;
        this.day = day;
        this.status = status;
    }

    public String getGrossSales() {
        return grossSales;
    }

    public void setGrossSales(String grossSales) {
        this.grossSales = grossSales;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getNetTotal() {
        return netTotal;
    }

    public void setNetTotal(String netTotal) {
        this.netTotal = netTotal;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
        

    }

