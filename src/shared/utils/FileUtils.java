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


public class FileUtils {
    
    public static final String RECORD_TYPE_ITEM = "ITEM";
    public static final String RECORD_TYPE_SUPPLIER = "SUPPLIER";
    public static final String RECORD_TYPE_SALES = "SALES";
    
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
    
    public static String addToFile(String filePath, String recordType, 
                                                Map<String, String> fields,
                                                Function<Map<String, String>, String> idGenerator) {
        try {
            // Ensure file exists
            if (!ensureFileExists(filePath)) {
                showErrorDialog("File Error", "Failed to initialize database file");
                return null;
            }

            // Generate ID
            String recordId = idGenerator.apply(fields);
            if (recordId == null) {
                showErrorDialog("ID Generation Error", "Failed to generate ID");
                return null;
            }

            // Build the record line
            String recordLine;
            switch (recordType) {
                case RECORD_TYPE_ITEM:
                    recordLine = String.join(",",
                        recordId,
                        fields.get("name"),
                        fields.get("supplierId"),
                        fields.get("price"),
                        fields.get("category")
                    );
                    break;
                case RECORD_TYPE_SUPPLIER:
                    recordLine = String.join(",",
                        recordId,
                        fields.get("name"),
                        fields.get("itemName"),
                        fields.get("itemPrice"),
                        fields.get("contact"),
                        fields.get("deliveryTime"),
                        String.join("|", 
                            fields.get("street"),
                            fields.get("city"),
                            fields.get("state"),
                            fields.get("postalCode")
                        )
                    );
                    break;
                case RECORD_TYPE_SALES:
                    recordLine = String.join(",",
                        recordId,
                        fields.get("date"),
                        fields.get("customerName"),
                        fields.get("customerContact"),
                        fields.get("itemId"),
                        fields.get("itemName"),
                        fields.get("quantity"),
                        fields.get("dateRequired")
                    );
                    break;
                default:
                    showErrorDialog("Error", "Invalid record type");
                    return null;
            }

            // Write to file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
                writer.write(recordLine);
                writer.newLine();
            }

            return recordId;
        } catch (IOException e) {
            showErrorDialog("File Error", "Failed to save record: " + e.getMessage());
            return null;
        }
    }
    
    public static String generateItemId(String filePath, String category) throws IOException {
        String prefix = "";
        switch(category.toUpperCase()) {
            case "GROCERIES": prefix = "G"; break;
            case "FRESH PRODUCE": prefix = "F"; break;
            case "ESSENTIAL GOODS": prefix = "E"; break;
            default: prefix = "X";
        }

        int maxNum = 0;
        File file = new File(filePath);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith(prefix)) {
                        try {
                            int currentNum = Integer.parseInt(line.substring(1, 4));
                            maxNum = Math.max(maxNum, currentNum);
                        } catch (NumberFormatException e) {
                            // Skip if ID format is invalid
                        }
                    }
                }
            }
        }
        return prefix + String.format("%03d", maxNum + 1);
    }
    
    public static String generateSupplierId(String filePath) throws IOException {
        int maxId = 0;
        File file = new File(filePath);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("S")) {
                        try {
                            int currentId = Integer.parseInt(line.substring(1, 4));
                            maxId = Math.max(maxId, currentId);
                        } catch (NumberFormatException e) {
                            // Skip if ID format is invalid
                        }
                    }
                }
            }
        }
        return "S" + String.format("%03d", maxId + 1);
    }
    
    public static String generateSalesId(String filePath) throws IOException {
        int maxId = 0;
        File file = new File(filePath);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("SE")) {
                        try {
                            int currentId = Integer.parseInt(line.substring(2, 6));
                            maxId = Math.max(maxId, currentId);
                        } catch (NumberFormatException e) {
                            // Skip if ID format is invalid
                        }
                    }
                }
            }
        }
        return "SE" + String.format("%04d", maxId + 1);
    }
    
    
    // This will display the items in form of table
    // Add  <<private static final String ITEMS_FILE = "src/database/items.txt";>> in the frame to use it
    
    /*-- USAGE EXAMPLE-- (Can copy paste if want)
        private static final String ITEMS_FILE = "src/database/items.txt";

            private void loadItems() {
                try {
                    TableUtils.loadItemsToTable(ITEMS_FILE, (DefaultTableModel) itemsTable.getModel());
                } catch (RuntimeException e) {
                    JOptionPane.showMessageDialog(this, 
                        e.getMessage(),
                        "Database Error", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
    */
    public class TableUtils {
    
        public static void loadItemsToTable(String filePath, DefaultTableModel model) {
            model.setRowCount(0); 

            try {
                File file = new File(filePath);
                if (file.exists()) {
                    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            String[] parts = line.split(",");
                            if (parts.length == 5) {
                                model.addRow(new Object[]{
                                    parts[0], // ID
                                    parts[1], // Name
                                    parts[2], // Supplier ID
                                    Double.parseDouble(parts[3]), // Price
                                    parts[4]  // Category
                                });
                            }
                        }
                    }
                }
            } catch (IOException | NumberFormatException e) {
                throw new RuntimeException("Error loading items to table: " + e.getMessage(), e);
            }
        }
        
        public static void loadSuppliersToTable(String filePath, DefaultTableModel model) {
            model.setRowCount(0); 

            try {
                File file = new File(filePath);
                if (file.exists()) {
                    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            String[] parts = line.split(",");
                            if (parts.length == 7) {
                                // Format the items list for display
                                String formattedItems = parts[2].replace("|", ", ");

                                // Parse address components
                                String[] addressParts = parts[6].split("\\|");
                                String formattedAddress = String.join(", ", addressParts);

                                model.addRow(new Object[]{
                                    parts[0], // Supplier ID
                                    parts[1], // Name
                                    formattedItems, // Formatted Items
                                    parts[3], // Item Price
                                    parts[4], // Contact
                                    Integer.parseInt(parts[5]), // Delivery Time
                                    formattedAddress // Formatted Address
                                });
                            }
                        }
                    }
                }
            } catch (IOException | NumberFormatException e) {
                throw new RuntimeException("Error loading suppliers to table: " + e.getMessage(), e);
            }
        }
    }
    
    private static void showErrorDialog(String title, String message) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
    }
    
    
}



