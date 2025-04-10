/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shared.models;

/**
 *
 * @author User
 */
public class Item {
    private String itemCode;
    private String name;
    private String supplierId;
    private int stock;
    private double price;
    
    public Item(String itemCode, String name, String supplierId, int stock, double price) {
        this.itemCode = itemCode;
        this.name = name;
        this.supplierId = supplierId;
        this.stock = stock;
        this.price = price;
    }
    
    // Getters
    public String getItemCode() { return itemCode; }
    public String getName() { return name; }
    public int getStock() { return stock; }
    
    // Business logic
    public void updateStock(int quantity) {
        this.stock += quantity;
    }
}
