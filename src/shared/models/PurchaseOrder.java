/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shared.models;

/**
 *
 * @author User
 */
public class PurchaseOrder {
    private String poId;
    private String prId;
    private String supplierId;
    private double totalAmount;
    private String approvedBy;
    
    public PurchaseOrder(String poId, String prId, String supplierId, double totalAmount) {
        this.poId = poId;
        this.prId = prId;
        this.supplierId = supplierId;
        this.totalAmount = totalAmount;
    }
    
    public void approve(String financeManagerId) {
        this.approvedBy = financeManagerId;
    }
}
