package shared.models;

public class Item implements Recordable {
    private String itemId;
    private String name;
    private String supplierId;
    private double price;
    private String category;
    
    public Item(String itemId, String name, String supplierId, double price, String category) {
        this.itemId = itemId;
        this.name = name;
        this.supplierId = supplierId;
        this.price = price;
        this.category = category;
    }

    // Getters
    public String getItemId() { return itemId; }
    public String getName() { return name; }
    public String getSupplierId() { return supplierId; }
    public double getPrice() { return price; }
    public String getCategory() { return category; }

    @Override
    public String getId() {
        return itemId;
    }

    @Override
    public String toCsvString() {
        return String.join(",",
            itemId,
            name,
            supplierId,
            String.valueOf(price),
            category
        );
    }
}