package shared.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import javax.swing.*;
import java.util.*;
import java.util.function.Function;
import shared.models.Recordable;


public class FileUtils {
    
    public static final String RECORD_TYPE_ITEM = "ITEM";
    public static final String RECORD_TYPE_SUPPLIER = "SUPPLIER";
    public static final String RECORD_TYPE_SALES = "SALES";
    public static final String RECORD_TYPE_PURCHASE_REQUISITION = "PURCHASE_REQUISITION";
    public static final String DEFAULT_STATUS = "pending"; 
    public static final String SUPPLIERS_FILE = "src/database/suppliers.txt";
   
    
// This method is used to check if a txt file for the item is created or not
// if not, it will create a new file
    // Checks file and parent directories
    
    public static boolean ensureFileExists(String filePath) {
        File file = new File(filePath);
        try {
            // Create parent directories if they don't exist
            file.getParentFile().mkdirs();

            // Create file if it doesn't exist
            if (!file.exists()) {
                return file.createNewFile();
            }
            return true;
        } catch (IOException e) {
            showErrorDialog("File Error",
                    "Failed to create file: " + filePath + "\nError: " + e.getMessage());
            return false;
        }
    }
    
    //Check if username taken or not
    
    public static boolean isUsernameTaken(String filePath, String username) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) return false;
        
        try (java.io.BufferedReader reader = new java.io.BufferedReader(
             new java.io.FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2 && parts[1].equals(username)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public static void deleteFromFileByField(String filePath, int fieldIndex, String fieldValue) throws IOException {
        File originalFile = new File(filePath);
        File tempFile = new File(filePath + ".tmp");

        try (BufferedReader reader = new BufferedReader(new FileReader(originalFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                // Only write lines that DON'T match the field value to be deleted
                if (parts.length > fieldIndex && !parts[fieldIndex].equals(fieldValue)) {
                    writer.write(line);
                    writer.newLine();
                }
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
    
    public static boolean itemExistsByName(String filePath, String itemName) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) return false;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2 && parts[1].equalsIgnoreCase(itemName)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public static void updateRecordInFile(String filePath, String recordId, String updatedRecord) throws IOException {
        File originalFile = new File(filePath);
        File tempFile = new File(filePath + ".tmp");

        try (BufferedReader reader = new BufferedReader(new FileReader(originalFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 0 && parts[0].equals(recordId)) {
                    // Replace with updated record
                    writer.write(updatedRecord);
                } else {
                    // Keep original record
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
    
    // In FileUtils.java
    public static String addToFile(String filePath, Recordable record) {
        try {
            // Ensure file exists
            if (!ensureFileExists(filePath)) {
                showErrorDialog("File Error", "Failed to initialize database file");
                return null;
            }

            // Write to file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
                writer.write(record.toCsvString());
                writer.newLine();
            }

            return record.getId(); // Assuming Recordable interface has getId()
        } catch (IOException e) {
            showErrorDialog("File Error", "Failed to save record: " + e.getMessage());
            return null;
        }
    }

    
   public static String generateId(String filePath, String prefix, int idLength) {
    int maxId = 0;
    File file = new File(filePath);
    if (!file.exists()) {
        return prefix + String.format("%0" + idLength + "d", 1);
    }
    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith(prefix)) {
                try {
                    int currentId = Integer.parseInt(line.substring(prefix.length(), prefix.length() + idLength));
                    maxId = Math.max(maxId, currentId);
                } catch (NumberFormatException e) {
                    // Ignore non-numeric IDs
                }
            }
        }
    } catch (IOException e) {
        System.err.println("Error reading file: " + e.getMessage());
        return null; // Or handle the error as appropriate for your application
    }
    return prefix + String.format("%0" + idLength + "d", maxId + 1);
}

    public static String generateItemId(String filePath, String category) {
        String prefix = "";
        switch (category.toUpperCase()) {
            case "GROCERIES":
                prefix = "G";
                break;
            case "FRESH PRODUCE":
                prefix = "F";
                break;
            case "ESSENTIAL GOODS":
                prefix = "E";
                break;
            default:
                prefix = "X";
        }
        return generateId(filePath, prefix, 3);
    }

    public static String generateSupplierId(String filePath) {
        return generateId(filePath, "S", 3);
    }

    public static String generateSalesId(String filePath) {
        return generateId(filePath, "SE", 4);
    }

    public static String generatePurchaseRequisitionId(String filePath) {
        return generateId(filePath, "PR", 4);
    }

    public static String generatePurchaseRequistionId(String PR_FILE) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
    
    
    // This will display the items in form of table
    // Add  <<private static final String ITEMS_FILE = "src/database/items.txt";>> in the frame to use it

    public class TableUtils {

        private static void loadDataToTable(String filePath, DefaultTableModel model, Function<String[], Object[]> rowMapper, int expectedColumns) {
            model.setRowCount(0);

            try {
                File file = new File(filePath);
                if (file.exists()) {
                    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            String[] parts = line.split(",");
                            if (parts.length >= expectedColumns) {
                                model.addRow(rowMapper.apply(parts));
                            }
                        }
                    }
                }
            } catch (IOException | NumberFormatException e) {
                throw new RuntimeException("Error loading data to table: " + e.getMessage(), e);
            }
        }

        public static void loadItemsToTable(String filePath, DefaultTableModel model) {
            loadDataToTable(filePath, model, parts -> new Object[]{
                parts[0], // ID
                parts[1], // Name
                parts[2], // Supplier ID
                parts[3]  // Category
            }, 4);
        }

        public static void loadSuppliersToTable(String filePath, DefaultTableModel model) {
            loadDataToTable(filePath, model, parts -> {
                String formattedItems = parts[2].replace("|", ", ");
                String[] addressParts = parts[6].split("\\|");
                String formattedAddress = String.join(", ", addressParts);

                return new Object[]{
                    parts[0], // Supplier ID
                    parts[1], // Name
                    formattedItems, // Formatted Items
                    parts[3], // Item Price
                    parts[4], // Contact
                    Integer.parseInt(parts[5]), // Delivery Time
                    formattedAddress // Formatted Address
                };
            }, 7);
        }

        public static void loadSalesToTable(String filePath, DefaultTableModel model) {
            loadDataToTable(filePath, model, parts -> new Object[]{
                parts[0], // Sales ID
                parts[1], // Date
                parts[2], // Date Required
                parts[3], // Customer Name
                parts[4], // Customer Contact
                parts[5], // Item ID
                parts[6], // Item Name
                Integer.parseInt(parts[7]), // Quantity
                parts[8]  // Total
            }, 9);
        }

        public static void loadPurchaseRequisitionsToTable(String filePath, DefaultTableModel model) {
            loadDataToTable(filePath, model, parts -> new Object[]{
                parts[0], // PR_ID
                parts[1], // Item_ID
                parts[2], // Item_Name
                Integer.parseInt(parts[3]), // Stock_Amount
                Integer.parseInt(parts[4]), // Quantity
                parts[5], // Date_Required
                parts[6], // Supplier_ID(s)
                parts[7], // User_ID
                parts[8], // Date_Created
                parts[9]  // Status
            }, 10);
        }
        
        public static void loadUsersToTable(String filePath, DefaultTableModel model) {
            loadDataToTable(filePath, model, parts -> new Object[]{
                parts[0], // User ID
                parts[1], // Username
                parts[3]  // Role
            }, 4); 
        }

    }
    
    
    
    private static void showErrorDialog(String title, String message) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
    }
    
    public static List<String[]> getLowStockItems(String filePath) {
        List<String[]> lowStockItems = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5 && parts[4].trim().equalsIgnoreCase("low stock")) {
                    lowStockItems.add(new String[]{parts[0], parts[1], parts[2], parts[3]});
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                "Error reading stock list: " + e.getMessage(),
                "File Error",
                JOptionPane.ERROR_MESSAGE);
        }

        return lowStockItems;
    }

    public static List<String> findLinesWithValue(String filePath, String valueToFind) {
        List<String> matchingLines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains(valueToFind)) {
                    matchingLines.add(line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return matchingLines;
    }

    public void searchAndDisplayItems(String filePath, String query, DefaultTableModel tableModel) {
        List<String> results = findLinesWithValue(filePath, query);

        tableModel.setRowCount(0);

        for (String line : results) {
            String[] data = line.split(","); 

            tableModel.addRow(data);
        }
    }
    
}



