package shared.models;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PurchaseRequisition implements Recordable {
    private String prId;
    private String itemId;
    private String itemName;
    private int stockAmount;
    private int quantity;
    private String dateRequired;
    private String supplierId;
    private String userId;
    private String dateCreated;
    private String status;
    private String allSuppliers; // New field for all suppliers

    public PurchaseRequisition(String prId, String itemId, String itemName, 
                             int stockAmount, int quantity, String dateRequired,
                             String supplierId, String userId, String allSuppliers) {
        this.prId = prId;
        this.itemId = itemId;
        this.itemName = itemName;
        this.stockAmount = stockAmount;
        this.quantity = quantity;
        this.dateRequired = dateRequired;
        this.supplierId = supplierId;
        this.userId = userId;
        this.allSuppliers = allSuppliers;
        this.dateCreated = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        this.status = "Pending";
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
    public String getDateCreated() { return dateCreated; }
    public String getStatus() { return status; }
    public String getAllSuppliers() { return allSuppliers; } // New getter

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
            dateCreated,
            status,
            allSuppliers // Added to CSV string
        );
    }
}