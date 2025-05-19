package shared.models;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Model class representing a Purchase Order in the OWSB system
 */
public class PurchaseOrder {
    private String purchaseOrderId;
    private String purchaseRequisitionId;
    private String itemId;
    private String itemName;
    private int quantity;
    private double itemPrice;
    private double totalPrice;
    private Date dateCreated;
    private Date dateRequired;
    private String supplierId;
    private String salesManagerId;
    private String purchaseManagerId;
    private String status; // "PENDING", "APPROVED", "REJECTED", "PAID", "RECEIVED_ITEMS"

    /**
     * Constructor for creating a new Purchase Order
     */
    public PurchaseOrder(String purchaseOrderId, String purchaseRequisitionId, String itemId, 
            String itemName, int quantity, double itemPrice, Date dateCreated, 
            Date dateRequired, String supplierId, String salesManagerId, String purchaseManagerId) {
        
        this.purchaseOrderId = purchaseOrderId;
        this.purchaseRequisitionId = purchaseRequisitionId;
        this.itemId = itemId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.itemPrice = itemPrice;
        this.totalPrice = quantity * itemPrice;
        this.dateCreated = dateCreated;
        this.dateRequired = dateRequired;
        this.supplierId = supplierId;
        this.salesManagerId = salesManagerId;
        this.purchaseManagerId = purchaseManagerId;
        this.status = "PENDING"; // Default status is PENDING
    }
    
    /**
     * Full constructor for PurchaseOrder with all fields
     */
    public PurchaseOrder(String purchaseOrderId, String purchaseRequisitionId, String itemId, 
            String itemName, int quantity, double itemPrice, double totalPrice, 
            Date dateCreated, Date dateRequired, String supplierId, 
            String salesManagerId, String purchaseManagerId, String status) {
        
        this.purchaseOrderId = purchaseOrderId;
        this.purchaseRequisitionId = purchaseRequisitionId;
        this.itemId = itemId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.itemPrice = itemPrice;
        this.totalPrice = totalPrice;
        this.dateCreated = dateCreated;
        this.dateRequired = dateRequired;
        this.supplierId = supplierId;
        this.salesManagerId = salesManagerId;
        this.purchaseManagerId = purchaseManagerId;
        this.status = status;
    }

    /**
     * Constructor for parsing from text file
     */
    public static PurchaseOrder fromString(String line) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String[] parts = line.split(",");
            
            return new PurchaseOrder(
                parts[0].trim(),  // PO ID
                parts[1].trim(),  // PR ID
                parts[2].trim(),  // Item ID
                parts[3].trim(),  // Item Name
                Integer.parseInt(parts[4].trim()),  // Quantity
                Double.parseDouble(parts[5].trim()),  // Item Price
                Double.parseDouble(parts[6].trim()),  // Total Price
                dateFormat.parse(parts[7].trim()),  // Date Created
                dateFormat.parse(parts[8].trim()),  // Date Required
                parts[9].trim(),  // Supplier ID
                parts[10].trim(), // Sales Manager ID
                parts[11].trim(), // Purchase Manager ID
                parts[12].trim()  // Status
            );
        } catch (Exception e) {
            throw new RuntimeException("Error parsing PurchaseOrder from string: " + e.getMessage());
        }
    }
    
    /**
     * Convert to string for saving to file
     */
    public String toFileString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return String.format("%s,%s,%s,%s,%d,%.2f,%.2f,%s,%s,%s,%s,%s,%s",
                purchaseOrderId,
                purchaseRequisitionId,
                itemId,
                itemName,
                quantity,
                itemPrice,
                totalPrice,
                dateFormat.format(dateCreated),
                dateFormat.format(dateRequired),
                supplierId,
                salesManagerId,
                purchaseManagerId,
                status);
    }

    // Getters
    public String getPurchaseOrderId() { return purchaseOrderId; }
    public String getPurchaseRequisitionId() { return purchaseRequisitionId; }
    public String getItemId() { return itemId; }
    public String getItemName() { return itemName; }
    public int getQuantity() { return quantity; }
    public double getItemPrice() { return itemPrice; }
    public double getTotalPrice() { return totalPrice; }
    public Date getDateCreated() { return dateCreated; }
    public Date getDateRequired() { return dateRequired; }
    public String getSupplierId() { return supplierId; }
    public String getSalesManagerId() { return salesManagerId; }
    public String getPurchaseManagerId() { return purchaseManagerId; }
    public String getStatus() { return status; }
    
    // Setters
    public void setPurchaseOrderId(String purchaseOrderId) { this.purchaseOrderId = purchaseOrderId; }
    public void setPurchaseRequisitionId(String purchaseRequisitionId) { this.purchaseRequisitionId = purchaseRequisitionId; }
    public void setItemId(String itemId) { this.itemId = itemId; }
    public void setItemName(String itemName) { this.itemName = itemName; }
    public void setQuantity(int quantity) { 
        this.quantity = quantity;
        this.totalPrice = this.quantity * this.itemPrice; // Update total price
    }
    public void setItemPrice(double itemPrice) { 
        this.itemPrice = itemPrice;
        this.totalPrice = this.quantity * this.itemPrice; // Update total price
    }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
    public void setDateCreated(Date dateCreated) { this.dateCreated = dateCreated; }
    public void setDateRequired(Date dateRequired) { this.dateRequired = dateRequired; }
    public void setSupplierId(String supplierId) { this.supplierId = supplierId; }
    public void setSalesManagerId(String salesManagerId) { this.salesManagerId = salesManagerId; }
    public void setPurchaseManagerId(String purchaseManagerId) { this.purchaseManagerId = purchaseManagerId; }
    public void setStatus(String status) { this.status = status; }
    
    // Business logic methods
    public void approve() {
        this.status = "APPROVED";
    }
    
    public void reject() {
        this.status = "REJECTED";
    }
    
    public void markReceived() {
        this.status = "RECEIVED_ITEMS";
    }
    
    /**
     * Calculates and updates the total price based on quantity and item price
     */
    public void updateTotalPrice() {
        this.totalPrice = this.quantity * this.itemPrice;
    }
    
    @Override
    public String toString() {
        return "PurchaseOrder{" +
                "purchaseOrderId='" + purchaseOrderId + '\'' +
                ", purchaseRequisitionId='" + purchaseRequisitionId + '\'' +
                ", itemId='" + itemId + '\'' +
                ", itemName='" + itemName + '\'' +
                ", quantity=" + quantity +
                ", itemPrice=" + itemPrice +
                ", totalPrice=" + totalPrice +
                ", dateCreated=" + dateCreated +
                ", dateRequired=" + dateRequired +
                ", supplierId='" + supplierId + '\'' +
                ", salesManagerId='" + salesManagerId + '\'' +
                ", purchaseManagerId='" + purchaseManagerId + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
    
    public String getOrderDetails() {
    return purchaseOrderId + "," + purchaseRequisitionId + "," + itemId + "," + itemName + ","
        + quantity + "," + itemPrice + "," + totalPrice + "," + dateCreated + "," + dateRequired + ","
        + supplierId + "," + salesManagerId + "," + purchaseManagerId + "," + status; 
}
}