package shared.models;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.io.*;

public class poManagement {
    private List<PurchaseOrder> purchaseOrders = new ArrayList<>();
    private String filePath = "src/database/purchase_orders.txt"; // Path to the purchase orders file

    // Load Purchase Orders from a file
    public void loadPurchaseOrders() {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                PurchaseOrder po = PurchaseOrder.fromString(line);
                purchaseOrders.add(po);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading purchase orders from file.");
        }
    }

    // Save all purchase orders back to the file
    public void savePurchaseOrders() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (PurchaseOrder po : purchaseOrders) {
                writer.write(po.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error saving purchase orders to file.");
        }
    }

    // Find a purchase order by its ID
    public PurchaseOrder getPurchaseOrderById(String poId) {
        for (PurchaseOrder po : purchaseOrders) {
            if (po.getPurchaseOrderId().equals(poId)) {
                return po;
            }
        }
        return null; // Return null if not found
    }

    // Add a new purchase order
    public void addPurchaseOrder(PurchaseOrder po) {
        purchaseOrders.add(po);
        savePurchaseOrders();  // Save changes
    }

    // Update a purchase order status (approved, rejected, paid, etc.)
    public void updatePurchaseOrderStatus(String poId, String newStatus) {
        PurchaseOrder po = getPurchaseOrderById(poId);
        if (po != null) {
            po.setStatus(newStatus);
            savePurchaseOrders();  // Save changes after status update
        } else {
            System.out.println("Purchase Order not found for status update.");
        }
    }

    // Add a payment to the relevant purchase order
    public void processPayment(Payment payment) {
        PurchaseOrder po = getPurchaseOrderById(payment.getPoId());
        if (po != null) {
            // Ensure the PO is approved before accepting a payment
            if ("APPROVED".equals(po.getStatus())) {
                // Update the PO status to 'PAID' and update the total amount
                po.updateTotalPrice();
                updatePurchaseOrderStatus(po.getPurchaseOrderId(), "PAID");
                
                // You could also store the payment in a payments file (implement if needed)
                storePayment(payment);
            } else {
                System.out.println("Purchase Order is not approved for payment.");
            }
        } else {
            System.out.println("Purchase Order not found for payment.");
        }
        
        
    }

    // Store the payment details in a separate file (optional)
    private void storePayment(Payment payment) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/database/payment.txt", true))) {
            writer.write(payment.getPoId() + "," + payment.getPaymentAmount() + "," + payment.getPaymentDate() + "," + payment.getPaymentMethod());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error saving payment information.");
        }
    }
    
        // Function to read the "APPROVED" PO details from the "purchase_orders.txt" file and display selected fields in the JTable
    private void loadApprovedPOs(JTable tblPO) {
        DefaultTableModel model = (DefaultTableModel) tblPO.getModel();
        model.setRowCount(0);  // Clear the table first
        
        try {
            File file = new File("src/database/purchase_orders.txt"); // Ensure file path is correct
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
        
            while ((line = reader.readLine()) != null) {
            // Check if the line contains "APPROVED"
                if (line.contains("APPROVED")) {
                // Split the line by commas
                    String[] poDetails = line.split(",");
                
                // Extract only the relevant fields (PO ID, Item Name, Quantity, Item Price, Total Price, Supplier ID, Status)
                    String poId = poDetails[0];  // PO ID
                    String itemName = poDetails[3];  // Item Name
                    String quantity = poDetails[4];  // Quantity
                    String itemPrice = poDetails[5];  // Item Price
                    String totalPrice = poDetails[6];  // Total Price
                    String supplierId = poDetails[9];  // Supplier ID
                    String status = poDetails[12];  // Status
                
                // Add only the relevant fields to the table
                    model.addRow(new Object[] {poId, itemName, quantity, itemPrice, totalPrice, supplierId, status});
                                       
                }
            }        
            reader.close();
                    // Make the Quantity column non-editable (Column 2 corresponds to "Quantity")
        tblPO.getColumnModel().getColumn(2).setCellEditor(null);  // Disable editing for Quantity column
        
        // Alternatively, make the whole column non-editable
        tblPO.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
            } catch (IOException ex) {
        ex.printStackTrace();
    }
}

    // Get all purchase orders
    public List<PurchaseOrder> getAllPurchaseOrders() {
        return purchaseOrders;
    }

    // Method for approving a purchase order
    public void approvePurchaseOrder(String poId) {
        updatePurchaseOrderStatus(poId, "APPROVED");
    }

    // Method for rejecting a purchase order
    public void rejectPurchaseOrder(String poId) {
        updatePurchaseOrderStatus(poId, "REJECTED");
    }

    // Method for marking a purchase order as 'Received Items'
    public void markReceivedItems(String poId) {
        updatePurchaseOrderStatus(poId, "RECEIVED_ITEMS");
    }
    
    public List<PurchaseOrder> getPurchaseOrders() {
    return purchaseOrders;
}

   
}
