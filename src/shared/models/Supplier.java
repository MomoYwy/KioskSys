/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shared.models;

/**
 *
 * @author User
 */
public class Supplier {
    private String supplierId;
    private String name;
    private String contact;
    private Address address;
    private String itemID;
    private double supplierItemPrice;
    private int delivery_time;
    

    public Supplier(String supplierId, String name, String contact, Address address, String itemID, double supplierItemPrice, int delivery_time) {
        this.supplierId = supplierId;
        this.name = name;
        this.contact = contact;
        this.address = address;
        this.itemID = itemID;
        this.supplierItemPrice = supplierItemPrice;
        this.delivery_time = delivery_time;
    }
    
    // Getters
    public String getSupplierId() { return supplierId; }
    public String getName() { return name; }
    public String getContact() { return contact; }
    public Address getAddress() { return address; }
    public String getItemID() { return itemID; }
    public double getSupplierItemPrice() { return supplierItemPrice; }
    public int getDelivery_time() { return delivery_time; }
    
}
