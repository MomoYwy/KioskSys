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
    
    public Supplier(String supplierId, String name, String contact) {
        this.supplierId = supplierId;
        this.name = name;
        this.contact = contact;
    }
    
    // Getters
    public String getSupplierId() { return supplierId; }
    public String getName() { return name; }
}
