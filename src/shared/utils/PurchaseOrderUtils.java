package shared.utils;

import java.io.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import shared.frames.EditPurchaseOrderDialog;

public class PurchaseOrderUtils {
    
    private static final String PR_FILE = "src/database/purchase_requisition.txt";
    private static final String PO_FILE = "src/database/purchase_orders.txt";
    private static final String ITEMS_FILE = "src/database/items.txt";
    private static final String SUPPLIERS_FILE = "src/database/suppliers.txt";
    
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    
    /**
     * Loads pending purchase requisitions into the table
     */
    public static void loadPendingPRsToTable(DefaultTableModel model) {
        model.setRowCount(0);
        
        try {
            File file = new File(PR_FILE);
            if (!file.exists()) {
                return;
            }
            
            // Skip header lines
            boolean headerSkipped = false;
            
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    // Skip header and empty lines
                    if (line.trim().isEmpty() || !headerSkipped) {
                        headerSkipped = true;
                        continue;
                    }
                    
                    String[] parts = line.split(",");
                    if (parts.length >= 9) {
                        // Only show pending PRs
                        if (parts[8].trim().equalsIgnoreCase("Pending")) {
                            model.addRow(new Object[]{
                                parts[0].trim(), // PR ID
                                parts[1].trim(), // Item ID
                                Integer.parseInt(parts[4].trim()), // Quantity
                                parts[5].trim(), // Date Required
                                parts[8].trim(), // Status
                                parts[7].trim()  // User ID (Sales Manager ID)
                            });
                        }
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, 
                    "Error loading purchase requisitions: " + e.getMessage(),
                    "Database Error", 
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Loads all purchase orders into the table
     */
    public static void loadPurchaseOrdersToTable(DefaultTableModel model) {
        model.setRowCount(0);

        try {
            File file = new File(PO_FILE);
            if (!file.exists()) {
                FileUtils.ensureFileExists(PO_FILE);
                return;
            }

            // Skip header lines
            boolean headerSkipped = false;

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    // Skip header and empty lines
                    if (line.trim().isEmpty() || !headerSkipped) {
                        headerSkipped = true;
                        continue;
                    }

                    String[] parts = line.split(",");
                    if (parts.length >= 13) { // We need all 13 fields
                        model.addRow(new Object[]{
                            parts[0].trim(),  // PO ID
                            parts[1].trim(),  // PR ID
                            parts[2].trim(),  // Item ID
                            parts[3].trim(),  // Item Name
                            Integer.parseInt(parts[4].trim()),  // Quantity
                            Double.parseDouble(parts[5].trim()),  // Item Price
                            Double.parseDouble(parts[6].trim()),  // Total Price
                            parts[7].trim(),  // Date Created
                            parts[8].trim(),  // Date Required
                            parts[9].trim(),  // Supplier ID
                            parts[10].trim(), // Sales Manager ID
                            parts[11].trim(), // Purchase Manager ID
                            parts[12].trim()  // Status
                        });
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            JOptionPane.showMessageDialog(null, 
                    "Error loading purchase orders: " + e.getMessage(),
                    "Database Error", 
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Load purchase orders filtered by status
     */
    public static void loadFilteredPurchaseOrders(DefaultTableModel model, String status) {
        model.setRowCount(0);
        
        try {
            File file = new File(PO_FILE);
            if (!file.exists()) {
                return;
            }
            
            boolean headerSkipped = false;
            
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    // Skip header and empty lines
                    if (line.trim().isEmpty() || !headerSkipped) {
                        headerSkipped = true;
                        continue;
                    }
                    
                    String[] parts = line.split(",");
                    if (parts.length >= 13) {
                        // Filter by status if not "ALL"
                        if (status.equals("ALL") || parts[12].trim().equals(status)) {
                            model.addRow(new Object[]{
                                parts[0].trim(),  // PO ID
                                parts[1].trim(),  // PR ID
                                parts[2].trim(),  // Item ID
                                parts[3].trim(),  // Item Name
                                Integer.parseInt(parts[4].trim()),  // Quantity
                                Double.parseDouble(parts[5].trim()),  // Item Price
                                Double.parseDouble(parts[6].trim()),  // Total Price
                                parts[7].trim(),  // Date Created
                                parts[8].trim(),  // Date Required
                                parts[9].trim(),  // Supplier ID
                                parts[10].trim(), // Sales Manager ID
                                parts[11].trim(), // Purchase Manager ID
                                parts[12].trim()  // Status
                            });
                        }
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            JOptionPane.showMessageDialog(null, 
                    "Error loading purchase orders: " + e.getMessage(),
                    "Database Error", 
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Initialize EditPurchaseOrderDialog with purchase order data
     */
    public static void initializeEditDialog(EditPurchaseOrderDialog dialog, String poId) {
        dialog.loadPurchaseOrderData(poId);
    }
    
    /**
     * Update purchase order in file
     */
    public static void updatePurchaseOrder(String poId, String prId, String itemId, String itemName,
                                        int quantity, double itemPrice, String dateCreated, 
                                        String dateRequired, String supplierId, String salesManagerId,
                                        String purchaseManagerId, String status) throws IOException {
        
        double totalPrice = quantity * itemPrice;
        
        String updatedRecord = String.format("%s,%s,%s,%s,%d,%.2f,%.2f,%s,%s,%s,%s,%s,%s",
                poId, prId, itemId, itemName, quantity, itemPrice, totalPrice,
                dateCreated, dateRequired, supplierId, salesManagerId, 
                purchaseManagerId, status);
        
        updatePOInFile(poId, updatedRecord);
    }
    
    /**
     * Update purchase order record in file
     */
    public static void updatePOInFile(String poId, String updatedRecord) throws IOException {
        File originalFile = new File(PO_FILE);
        File tempFile = new File(PO_FILE + ".tmp");

        try (BufferedReader reader = new BufferedReader(new FileReader(originalFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            boolean headerWritten = false;

            while ((line = reader.readLine()) != null) {
                // Write header or empty lines as-is
                if (line.trim().isEmpty() || !headerWritten) {
                    writer.write(line);
                    writer.newLine();
                    headerWritten = true;
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length > 0 && parts[0].equals(poId)) {
                    // Replace with updated record
                    writer.write(updatedRecord);
                } else {
                    writer.write(line);
                }
                writer.newLine();
            }
        }

        // Replace original file with updated file
        if (!originalFile.delete()) {
            throw new IOException("Could not delete original file");
        }
        if (!tempFile.renameTo(originalFile)) {
            throw new IOException("Could not rename temp file");
        }
    }
    
    /**
     * Generates a new PO ID
     */
    public static String generatePurchaseOrderId() throws IOException {
        int maxId = 0;
        File file = new File(PO_FILE);
        
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                boolean headerSkipped = false;
                
                while ((line = reader.readLine()) != null) {
                    // Skip header line
                    if (!headerSkipped) {
                        headerSkipped = true;
                        continue;
                    }
                    
                    // Skip empty lines
                    if (line.trim().isEmpty()) {
                        continue;
                    }
                    
                    String[] parts = line.split(",");
                    if (parts.length > 0 && parts[0].startsWith("PO")) {
                        try {
                            int id = Integer.parseInt(parts[0].substring(2));
                            if (id > maxId) {
                                maxId = id;
                            }
                        } catch (NumberFormatException e) {
                            // Skip invalid ID format
                        }
                    }
                }
            }
        } else {
            // Create file with header if it doesn't exist
            FileUtils.ensureFileExists(PO_FILE);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write("PO_ID,PR_ID,Item_ID,Item_Name,Quantity,Item_Price,Total_Price,Date_Created,Date_Required,Supplier_ID,Sales_Manager_ID,Purchase_Manager_ID,Status");
                writer.newLine();
            }
        }
        
        return "PO" + String.format("%03d", maxId + 1);
    }
    
    /**
     * Update PR status in the PR file
     */
    public static boolean updatePRStatus(String prId, String newStatus) {
        try {
            File originalFile = new File(PR_FILE);
            File tempFile = new File(PR_FILE + ".tmp");
            
            boolean updated = false;
            
            try (BufferedReader reader = new BufferedReader(new FileReader(originalFile));
                 BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
                
                String line;
                boolean headerWritten = false;
                
                while ((line = reader.readLine()) != null) {
                    // Write header or empty lines as-is
                    if (line.trim().isEmpty() || !headerWritten) {
                        writer.write(line);
                        writer.newLine();
                        headerWritten = true;
                        continue;
                    }
                    
                    String[] parts = line.split(",");
                    if (parts.length >= 9 && parts[0].trim().equals(prId)) {
                        // Update the status (parts[8])
                        parts[8] = newStatus;
                        
                        // Reconstruct the line
                        writer.write(String.join(",", parts));
                        updated = true;
                    } else {
                        writer.write(line);
                    }
                    writer.newLine();
                }
            }
            
            // Replace original file with updated file
            if (!originalFile.delete()) {
                throw new IOException("Could not delete original file");
            }
            if (!tempFile.renameTo(originalFile)) {
                throw new IOException("Could not rename temp file");
            }
            
            return updated;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, 
                    "Error updating PR status: " + e.getMessage(),
                    "Database Error", 
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    /**
     * Parse a date string to a Date object
     */
    public static Date parseDate(String dateString) {
        try {
            return DATE_FORMAT.parse(dateString);
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(null, 
                    "Error parsing date: " + dateString,
                    "Date Error", 
                    JOptionPane.ERROR_MESSAGE);
            return new Date(); // Return current date as fallback
        }
    }
    
    /**
     * Format a Date object to a string
     */
    public static String formatDate(Date date) {
        return DATE_FORMAT.format(date);
    }
}