package shared.models;

public class PurchaseRequisition implements Recordable {
    private String prId;
    private String itemId;
    private String itemName;
    private int stockAmount;
    private int quantity;
    private String dateRequired;
    private String supplierId;
    private String userId;
    private String status; // "Pending", "Approved", "Rejected"

    public PurchaseRequisition(String prId, String itemId, String itemName, 
                             int stockAmount, int quantity, String dateRequired,
                             String supplierId, String userId) {
        this.prId = prId;
        this.itemId = itemId;
        this.itemName = itemName;
        this.stockAmount = stockAmount;
        this.quantity = quantity;
        this.dateRequired = dateRequired;
        this.supplierId = supplierId;
        this.userId = userId;
        this.status = "Pending"; // Default status
    }

    // Getters
    public String getPrId() { return prId; }
    public String getItemId() { return itemId; }
    public String getItemName() { return itemName; }
    public int getStockAmount() { return stockAmount; }
    public int getQuantity() { return quantity; }
    public String getDateRequired() { return dateRequired; }
    public String getSupplierId() { return supplierId; }
    public String getUserId() { return userId; }
    public String getStatus() { return status; }

    // Business methods
    public void approve() {
        this.status = "Approved";
    }

    public void reject() {
        this.status = "Rejected";
    }

    @Override
    public String getId() {
        return prId;
    }

    @Override
    public String toCsvString() {
        return String.join(",",
            prId,
            itemId,
            itemName,
            String.valueOf(stockAmount),
            String.valueOf(quantity),
            dateRequired,
            supplierId,
            userId,
            status
        );
    }
}