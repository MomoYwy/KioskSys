/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shared.models;

/**
 *
 * @author User
 */
public class PurchaseRequisition {
    private String prId;
    private String itemCode;
    private int quantity;
    private String requestedBy; // User ID of Sales Manager
    private String status; // "PENDING", "APPROVED", "REJECTED"
    
    public PurchaseRequisition(String prId, String itemCode, int quantity, String requestedBy) {
        this.prId = prId;
        this.itemCode = itemCode;
        this.quantity = quantity;
        this.requestedBy = requestedBy;
        this.status = "PENDING";
    }
    
    // Approve/Reject methods
    public void approve() { this.status = "APPROVED"; }
    public void reject() { this.status = "REJECTED"; }
}
