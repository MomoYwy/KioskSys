/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shared.models;



import java.util.Objects;

public class SalesEntry {
    private String salesId;
    private String date;
    private String dateRequired;
    private String customerName;
    private String customerContact;
    private String itemId;
    private String itemName;
    private int quantity;

    // Constructor without salesId (for new entries)
    public SalesEntry(String date, String dateRequired, String customerName, 
                     String customerContact, String itemId, String itemName, int quantity) {
        this.date = date;
        this.dateRequired = dateRequired;
        this.customerName = customerName;
        this.customerContact = customerContact;
        this.itemId = itemId;
        this.itemName = itemName;
        this.quantity = quantity;
    }

    // Constructor with salesId (for existing entries)
    public SalesEntry(String salesId, String date, String dateRequired, String customerName, 
                     String customerContact, String itemId, String itemName, int quantity) {
        this(date, dateRequired, customerName, customerContact, itemId, itemName, quantity);
        this.salesId = salesId;
    }

    // Getters and Setters
    public String getSalesId() {
        return salesId;
    }

    public void setSalesId(String salesId) {
        this.salesId = salesId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDateRequired() {
        return dateRequired;
    }

    public void setDateRequired(String dateRequired) {
        this.dateRequired = dateRequired;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerContact() {
        return customerContact;
    }

    public void setCustomerContact(String customerContact) {
        this.customerContact = customerContact;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}