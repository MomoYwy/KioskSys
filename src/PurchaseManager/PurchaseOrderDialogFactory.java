/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PurchaseManager;

import shared.frames.EditPurchaseOrderDialog;
import javax.swing.JFrame;

public class PurchaseOrderDialogFactory {
    
    public static EditPurchaseOrderDialog createEditDialog(JFrame parent, boolean modal) {
        return new EditPurchaseOrderDialog(parent, modal);
    }
    
    public static AddPurchaseOrderDialog createAddDialog(JFrame parent, boolean modal, 
            String prId, String itemId, String itemName, int quantity, 
            String dateRequired, String salesManagerId, String purchaseManagerId) {
        return new AddPurchaseOrderDialog(parent, modal, prId, itemId, itemName, 
                quantity, dateRequired, salesManagerId, purchaseManagerId);
    }
}
