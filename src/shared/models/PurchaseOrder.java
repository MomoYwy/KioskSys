package shared.models;

import java.text.SimpleDateFormat;
import java.util.Date;
import shared.models.Recordable;

public class PurchaseOrder implements Recordable {
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
    private PurchaseOrderStatus status;

    // Enum for status
    public enum PurchaseOrderStatus {
        PENDING, APPROVED, REJECTED, PAID, RECEIVED_ITEMS
    }

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    // Private constructor for Builder
    private PurchaseOrder(Builder builder) {
        this.purchaseOrderId = builder.purchaseOrderId;
        this.purchaseRequisitionId = builder.purchaseRequisitionId;
        this.itemId = builder.itemId;
        this.itemName = builder.itemName;
        this.quantity = builder.quantity;
        this.itemPrice = builder.itemPrice;
        this.totalPrice = calculateTotalPrice();
        this.dateCreated = builder.dateCreated;
        this.dateRequired = builder.dateRequired;
        this.supplierId = builder.supplierId;
        this.salesManagerId = builder.salesManagerId;
        this.purchaseManagerId = builder.purchaseManagerId;
        this.status = builder.status != null ? builder.status : PurchaseOrderStatus.PENDING;
    }

    //PUBLIC CONSTRUCTOR FOR OrderManager COMPATIBILITY
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
        this.totalPrice = calculateTotalPrice(); // Recalculate to ensure consistency
        this.dateCreated = dateCreated;
        this.dateRequired = dateRequired;
        this.supplierId = supplierId;
        this.salesManagerId = salesManagerId;
        this.purchaseManagerId = purchaseManagerId;
        
        // Convert String status to enum
        try {
            this.status = PurchaseOrderStatus.valueOf(status.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            // Default to PENDING if status is invalid
            this.status = PurchaseOrderStatus.PENDING;
            System.err.println("Warning: Invalid status '" + status + "' for PO " + purchaseOrderId + 
                              ". Defaulting to PENDING.");
        }
    }

    // Builder Pattern (unchanged)
    public static class Builder {
        private String purchaseOrderId;
        private String purchaseRequisitionId;
        private String itemId;
        private String itemName;
        private int quantity;
        private double itemPrice;
        private Date dateCreated;
        private Date dateRequired;
        private String supplierId;
        private String salesManagerId;
        private String purchaseManagerId;
        private PurchaseOrderStatus status;

        public Builder setPurchaseOrderId(String purchaseOrderId) {
            this.purchaseOrderId = purchaseOrderId;
            return this;
        }

        public Builder setPurchaseRequisitionId(String purchaseRequisitionId) {
            this.purchaseRequisitionId = purchaseRequisitionId;
            return this;
        }

        public Builder setItemId(String itemId) {
            this.itemId = itemId;
            return this;
        }

        public Builder setItemName(String itemName) {
            this.itemName = itemName;
            return this;
        }

        public Builder setQuantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public Builder setItemPrice(double itemPrice) {
            this.itemPrice = itemPrice;
            return this;
        }

        public Builder setDateCreated(Date dateCreated) {
            this.dateCreated = dateCreated;
            return this;
        }

        public Builder setDateRequired(Date dateRequired) {
            this.dateRequired = dateRequired;
            return this;
        }

        public Builder setSupplierId(String supplierId) {
            this.supplierId = supplierId;
            return this;
        }

        public Builder setSalesManagerId(String salesManagerId) {
            this.salesManagerId = salesManagerId;
            return this;
        }

        public Builder setPurchaseManagerId(String purchaseManagerId) {
            this.purchaseManagerId = purchaseManagerId;
            return this;
        }

        public Builder setStatus(PurchaseOrderStatus status) {
            this.status = status;
            return this;
        }

        public PurchaseOrder build() {
            return new PurchaseOrder(this);
        }
    }

    // Factory method for creating from CSV string (unchanged)
    public static PurchaseOrder fromCsvString(String csvLine) {
        try {
            String[] parts = csvLine.split(",");
            if (parts.length < 13) {
                throw new IllegalArgumentException("Invalid CSV format for PurchaseOrder");
            }

            return new Builder()
                    .setPurchaseOrderId(parts[0].trim())
                    .setPurchaseRequisitionId(parts[1].trim())
                    .setItemId(parts[2].trim())
                    .setItemName(parts[3].trim())
                    .setQuantity(Integer.parseInt(parts[4].trim()))
                    .setItemPrice(Double.parseDouble(parts[5].trim()))
                    .setDateCreated(DATE_FORMAT.parse(parts[7].trim()))
                    .setDateRequired(DATE_FORMAT.parse(parts[8].trim()))
                    .setSupplierId(parts[9].trim())
                    .setSalesManagerId(parts[10].trim())
                    .setPurchaseManagerId(parts[11].trim())
                    .setStatus(PurchaseOrderStatus.valueOf(parts[12].trim()))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Error parsing PurchaseOrder from CSV: " + e.getMessage(), e);
        }
    }

    // Business Logic Methods (unchanged)
    public void approve() {
        if (status == PurchaseOrderStatus.PENDING) {
            this.status = PurchaseOrderStatus.APPROVED;
        } else {
            throw new IllegalStateException("Can only approve PENDING orders");
        }
    }

    public void reject() {
        if (status == PurchaseOrderStatus.PENDING) {
            this.status = PurchaseOrderStatus.REJECTED;
        } else {
            throw new IllegalStateException("Can only reject PENDING orders");
        }
    }

    public void markAsPaid() {
        if (status == PurchaseOrderStatus.APPROVED) {
            this.status = PurchaseOrderStatus.PAID;
        } else {
            throw new IllegalStateException("Can only mark APPROVED orders as paid");
        }
    }

    public void markAsReceived() {
        if (status == PurchaseOrderStatus.PAID) {
            this.status = PurchaseOrderStatus.RECEIVED_ITEMS;
        } else {
            throw new IllegalStateException("Can only mark PAID orders as received");
        }
    }

    public boolean canBeEdited() {
    return status == PurchaseOrderStatus.PENDING || 
           status == PurchaseOrderStatus.APPROVED ||
           status == PurchaseOrderStatus.REJECTED;
    }

    public boolean canBeDeleted() {
        return status == PurchaseOrderStatus.PENDING;
    }

    public void updateQuantity(int newQuantity) {
        if (!canBeEdited()) {
            throw new IllegalStateException("Cannot edit non-pending orders");
        }
        this.quantity = newQuantity;
        this.totalPrice = calculateTotalPrice();
    }

    public void updateSupplier(String newSupplierId, double newItemPrice) {
        if (!canBeEdited()) {
            throw new IllegalStateException("Cannot edit non-pending orders");
        }
        this.supplierId = newSupplierId;
        this.itemPrice = newItemPrice;
        this.totalPrice = calculateTotalPrice();
    }

    private double calculateTotalPrice() {
        return quantity * itemPrice;
    }

    // Implement Recordable interface
    @Override
    public String getId() {
        return purchaseOrderId;
    }

    @Override
    public String toCsvString() {
        return String.format("%s,%s,%s,%s,%d,%.2f,%.2f,%s,%s,%s,%s,%s,%s",
                purchaseOrderId, purchaseRequisitionId, itemId, itemName, quantity,
                itemPrice, totalPrice, DATE_FORMAT.format(dateCreated),
                DATE_FORMAT.format(dateRequired), supplierId, salesManagerId,
                purchaseManagerId, status.name());
    }

    // Getters (unchanged)
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
    public PurchaseOrderStatus getStatus() { return status; }

    // For backward compatibility with existing code
    public String getStatusString() {
        return status.name();
    }

    // Keep these methods for backward compatibility
    public void setStatus(String statusString) {
        this.status = PurchaseOrderStatus.valueOf(statusString);
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.totalPrice = calculateTotalPrice();
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
        this.totalPrice = calculateTotalPrice();
    }

    // Legacy methods for backward compatibility
    public static PurchaseOrder fromString(String line) {
        return fromCsvString(line);
    }

    public String toFileString() {
        return toCsvString();
    }

    public void markReceived() {
        markAsReceived();
    }

    public void updateTotalPrice() {
        this.totalPrice = calculateTotalPrice();
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
}