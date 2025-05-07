    package shared.models;

    import java.util.Date;


    public class PurchaseRequisition {
        private String purchaseRequisitionId;
        private String itemId;
        private String itemName; 
        private int stockAmount;
        private int quantity;
        private Date dateRequired;
        private String supplierId;
        private String userId;  
        private String status; // "Pending", "Approved", "Rejected"

        /**
        * Constructor for PurchaseRequisition.
 
        */
        public PurchaseRequisition(String purchaseRequisitionId, String itemId, String itemName, int stockAmount, int quantity, Date dateRequired, String supplierId, String userId) {
            this.purchaseRequisitionId = purchaseRequisitionId;
            this.itemId = itemId;
            this.itemName = itemName;
            this.stockAmount = stockAmount;
            this.quantity = quantity;
            this.dateRequired = dateRequired;
            this.supplierId = supplierId;
            this.userId = userId;
            this.status = "Pending";
        }

        // Getters

        public String getPurchaseRequisitionId() {
            return purchaseRequisitionId;
        }

        public String getItemId() {
            return itemId;
        }

        public String getItemName() {
            return itemName;
        }

        public int getStockAmount() {
            return stockAmount;
        }

        public int getQuantity() {
            return quantity;
        }

        public Date getDateRequired() {
            return dateRequired;
        }

        public String getSupplierId() {
            return supplierId;
        }

        public String getUserId() {
            return userId;
        }

        public String getStatus() {
            return status;
        }

        // Setters

        public void setPurchaseRequisitionId(String purchaseRequisitionId) {
            this.purchaseRequisitionId = purchaseRequisitionId;
        }

        public void setItemId(String itemId) {
            this.itemId = itemId;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }

        public void setStockAmount(int stockAmount) {
            this.stockAmount = stockAmount;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public void setDateRequired(Date dateRequired) {
            this.dateRequired = dateRequired;
        }

        public void setSupplierId(String supplierId) {
            this.supplierId = supplierId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public void approve() {
            this.status = "Approved";
        }

        /**
        * Rejects the purchase requisition.
        */
        public void reject() {
            this.status = "Rejected";
        }

        /**
        * Creates a string representation of the PurchaseRequisition object.
        * @return A string representation of the PurchaseRequisition.
        */
        @Override
        public String toString() {
            return "PurchaseRequisition{" +
                    "purchaseRequisitionId='" + purchaseRequisitionId + '\'' +
                    ", itemId='" + itemId + '\'' +
                    ", itemName='" + itemName + '\'' +
                    ", stockAmount=" + stockAmount +
                    ", quantity=" + quantity +
                    ", dateRequired=" + dateRequired +
                    ", supplierId='" + supplierId + '\'' +
                    ", userId='" + userId + '\'' +
                    ", status='" + status + '\'' +
                    '}';
        }
    }

