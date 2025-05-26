package shared.models;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import shared.utils.FileUtils;


public class Payment {
    private String poId;
    private double paymentAmount;
    private Date paymentDate;
    private String paymentMethod;
    private static final String Payment_FILE = "src/database/payment.txt";


    // Constructor
    public Payment(String poId, double paymentAmount, Date paymentDate, String paymentMethod) {
        this.poId = poId;
        this.paymentAmount = paymentAmount;
        this.paymentDate = paymentDate;
        this.paymentMethod = paymentMethod;
    }

    // Getters and setters
    public String getPoId() {
        return poId;
    }

    public void setPoId(String poId) {
        this.poId = poId;
    }

    public double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    
    private poManagement poManage = new poManagement();
    
    
       public static String generatePaymentId() throws IOException {
        int maxId = 0;
        File file = new File(Payment_FILE);
        
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
            FileUtils.ensureFileExists(Payment_FILE);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write("Payment_ID,PO_ID,Item_Name,Quantity,Item_Price,Total_Price,Supplier_ID,Status,Payment_Method,Amount,Payment_Date");
                writer.newLine();
            }
        }
        
        return "PO" + String.format("%03d", maxId + 1);
    }

}

