package shared.models;

public class Item implements Recordable {
    private String itemId;
    private String name;
    private String supplierId;
    private String category;
    
    public Item(String itemId, String name, String supplierId, String category) {
        this.itemId = itemId;
        this.name = name;
        this.supplierId = supplierId;
        this.category = category;
    }

    // Getters
    public String getItemId() { return itemId; }
    public String getName() { return name; }
    public String getSupplierId() { return supplierId; }
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
            category
        );
    }
}