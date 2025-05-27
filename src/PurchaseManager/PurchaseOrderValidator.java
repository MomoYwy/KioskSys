package PurchaseManager;

import shared.models.PurchaseOrder;

public class PurchaseOrderValidator {
    
    public void validateForCreation(PurchaseOrder purchaseOrder) throws ValidationException {
        validateCommon(purchaseOrder);
        
        if (purchaseOrder.getPurchaseOrderId() == null || purchaseOrder.getPurchaseOrderId().trim().isEmpty()) {
            throw new ValidationException("Purchase Order ID is required");
        }
    }
    
    public void validateForUpdate(PurchaseOrder purchaseOrder) throws ValidationException {
        validateCommon(purchaseOrder);

        if (purchaseOrder.getPurchaseOrderId() == null || purchaseOrder.getPurchaseOrderId().trim().isEmpty()) {
            throw new ValidationException("Purchase Order ID is required for update");
        }

    }
    
    private void validateCommon(PurchaseOrder purchaseOrder) throws ValidationException {
        if (purchaseOrder == null) {
            throw new ValidationException("Purchase Order cannot be null");
        }
        
        if (purchaseOrder.getPurchaseRequisitionId() == null || purchaseOrder.getPurchaseRequisitionId().trim().isEmpty()) {
            throw new ValidationException("Purchase Requisition ID is required");
        }
        
        if (purchaseOrder.getItemId() == null || purchaseOrder.getItemId().trim().isEmpty()) {
            throw new ValidationException("Item ID is required");
        }
        
        if (purchaseOrder.getItemName() == null || purchaseOrder.getItemName().trim().isEmpty()) {
            throw new ValidationException("Item Name is required");
        }
        
        if (purchaseOrder.getQuantity() <= 0) {
            throw new ValidationException("Quantity must be greater than 0");
        }
        
        if (purchaseOrder.getItemPrice() < 0) {
            throw new ValidationException("Item price cannot be negative");
        }
        
        if (purchaseOrder.getDateRequired() == null) {
            throw new ValidationException("Date required is required");
        }
        
        if (purchaseOrder.getSupplierId() == null || purchaseOrder.getSupplierId().trim().isEmpty()) {
            throw new ValidationException("Supplier ID is required");
        }
        
        if (purchaseOrder.getSalesManagerId() == null || purchaseOrder.getSalesManagerId().trim().isEmpty()) {
            throw new ValidationException("Sales Manager ID is required");
        }
        
        if (purchaseOrder.getPurchaseManagerId() == null || purchaseOrder.getPurchaseManagerId().trim().isEmpty()) {
            throw new ValidationException("Purchase Manager ID is required");
        }
    }
}