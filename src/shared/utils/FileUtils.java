package shared.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;


public class FileUtils {
    
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
            model.setRowCount(0); // Clear existing data

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
    }
    
    private static void showErrorDialog(String title, String message) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
    }
}

