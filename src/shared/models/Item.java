
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
    private String category;
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
    public String getSupplierId() { return supplierId; }
    public int getStock() { return stock; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }
    
    // Business logic
    public void updateStock(int quantity) {
        this.stock += quantity;
    }
}
