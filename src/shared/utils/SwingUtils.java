package shared.utils;

import javax.swing.JComboBox;
import java.util.Calendar;
import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class SwingUtils {
    
    public static void initializeDateComboBoxes(JComboBox<String> dayCombo, 
                                              JComboBox<String> monthCombo, 
                                              JComboBox<String> yearCombo) {
        // Set current date as default
        Calendar cal = Calendar.getInstance();
        int currentDay = cal.get(Calendar.DAY_OF_MONTH);
        int currentMonth = cal.get(Calendar.MONTH) + 1; // Months are 0-based
        int currentYear = cal.get(Calendar.YEAR);
        
        // Update day combo based on current month/year
        updateDayComboBox(dayCombo, monthCombo, yearCombo);
        
        // Set current selections
        dayCombo.setSelectedItem(String.format("%02d", currentDay));
        monthCombo.setSelectedIndex(currentMonth - 1);
        yearCombo.setSelectedItem(String.valueOf(currentYear));
        
        // Add listeners to update days when month/year changes
        monthCombo.addActionListener(e -> updateDayComboBox(dayCombo, monthCombo, yearCombo));
        yearCombo.addActionListener(e -> updateDayComboBox(dayCombo, monthCombo, yearCombo));
    }
    
    public static void updateDayComboBox(JComboBox<String> dayCombo, 
                                       JComboBox<String> monthCombo, 
                                       JComboBox<String> yearCombo) {
        String selectedDay = (String) dayCombo.getSelectedItem();
        dayCombo.removeAllItems();
        
        int month = monthCombo.getSelectedIndex() + 1;
        int year = Integer.parseInt((String) yearCombo.getSelectedItem());
        int daysInMonth = getDaysInMonth(month, year);
        
        for (int i = 1; i <= daysInMonth; i++) {
            dayCombo.addItem(String.format("%02d", i));
        }
        
        // Try to maintain selection if possible
        if (selectedDay != null) {
            try {
                int day = Integer.parseInt(selectedDay);
                if (day <= daysInMonth) {
                    dayCombo.setSelectedItem(selectedDay);
                    return;
                }
            } catch (NumberFormatException ignored) {}
        }
        
        // Default to first day if selection wasn't maintained
        dayCombo.setSelectedIndex(0);
    }
    
    private static int getDaysInMonth(int month, int year) {
        switch (month) {
            case 4: case 6: case 9: case 11:
                return 30;
            case 2:
                return ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) ? 29 : 28;
            default:
                return 31;
        }
    }
    
     public static List<String[]> loadItemsFromFile(String filePath) {
        List<String[]> items = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    items.add(parts);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "Error loading items: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
        return items;
    }
     
     public static String getDashboardClassByUserId(String userId) {
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }

        // Check the prefix pattern (first 1-2 characters)
        String prefix = userId.length() >= 2 ? userId.substring(0, 2).toUpperCase() 
                                           : userId.substring(0, 1).toUpperCase();

        switch (prefix) {
            case "A":
                return "admin.AdminDashboard";
            case "S":
            case "SM": // Sales Manager
                return "salesManager.SMdashboard";
            case "F":
            case "FM": // Finance Manager
                return "finance.FinanceDashboard";
            case "P":
            case "PM": // Purchase Manager
                return "PurchaseManager.PMDashboard";
            case "I":
            case "IM": // Inventory Manager
                return "inventoryManager.IMDashboard";
            default:
                throw new IllegalArgumentException("Unknown user role prefix: " + prefix);
        }
    }

    public static void handleBackButton(JFrame currentFrame, String userId, String username) {
        try {
            String dashboardClass = getDashboardClassByUserId(userId);
            
            Class<?> clazz = Class.forName(dashboardClass);
            JFrame dashboard = (JFrame) clazz.getConstructor(String.class, String.class)
                                           .newInstance(userId, username);
            
            dashboard.setVisible(true);
            currentFrame.dispose();
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(currentFrame, 
                e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(currentFrame, 
                "Failed to open dashboard: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
     
}