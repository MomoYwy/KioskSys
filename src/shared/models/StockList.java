/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shared.models;

/**
 *
 * @author user
 */
public class StockList {    
    private String itemId;
    private String itemName;
    private String category;
    private int stockAmount;

    public StockList(String itemId, String itemName, String category, int quantity) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.category = category;
        this.stockAmount = quantity;
    }

    public String getItemId() { return itemId; }
    public String getItemName() { return itemName; }
    public String getCategory() { return category; }
    public int getQuantity() { return stockAmount; }

    public void setQuantity(int quantity) { this.stockAmount = quantity; }
}


