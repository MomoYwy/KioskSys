package Finance;

import admin.AdminDashboard;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableCellRenderer;
import java.util.HashSet;
import java.util.Set;



public class PaymentFrame extends javax.swing.JFrame {
    private FinanceDashboard previousFMForm;
    private AdminDashboard previousAdminForm;    
    private Set<String> paidPOs = new HashSet<>();
    
    private static int paymentCounter = 1;  


    public PaymentFrame() {
        initComponents();
        initializeTableListener();     
        readCounterFromFile();  

    }
    public PaymentFrame(FinanceDashboard previousForm) {
        this();
        this.previousFMForm = previousForm;
    }  
    public PaymentFrame(AdminDashboard previousForm) {
        this();
        this.previousAdminForm = previousForm;
    }      
    
    // Function to read the "APPROVED" PO details from the "purchase_orders.txt" file and display selected fields in the JTable
    private void loadApprovedPOs() {
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
    
    private void updatePOStatus(String poId, String newStatus) {
        try {
        // Read the current file
        File file = new File("src/database/purchase_orders.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuilder fileContents = new StringBuilder();
        String line;
        
        // Iterate through each line and update the status of the selected PO
        while ((line = reader.readLine()) != null) {
            String[] poDetails = line.split(",");
            String currentPoId = poDetails[0];

            if (currentPoId.equals(poId)) {
                // Update the status field (index 12 corresponds to status)
                poDetails[12] = newStatus;
                line = String.join(",", poDetails);  // Rebuild the line with updated status
            }

           fileContents.append(line).append("\n");
        }

        reader.close();

        // Write the modified contents back to the file
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write(fileContents.toString());
        writer.close();

        System.out.println("PO status updated to " + newStatus);

    } catch (IOException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error updating PO status.", "Error", JOptionPane.ERROR_MESSAGE);
    }
}
    
    
    private void initializeTableListener() {
    tblPO.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            // Check if a row is selected
            if (!e.getValueIsAdjusting()) {
                int selectedRow = tblPO.getSelectedRow();
                if (selectedRow >= 0) {
                    // Get the total price from the selected row (assuming it's in column 6, index 6)
                    String totalAmount = tblPO.getValueAt(selectedRow, 4).toString();
                  
                    // Set the total amount in the payment amount field (txtPAmount)
                    txtPAmount.setText(totalAmount);
                }
            }
        }
    });
}
 
    private String generatePaymentId() {
        // Format the Payment_ID as "PAY0001", "PAY0002", etc.
        String paymentId = String.format("PAY%04d", paymentCounter++);
        updateCounterInFile();  // Save the updated counter to the file
        return paymentId;
    }

    private void readCounterFromFile() {
        try {
            File file = new File("src/database/payment_counter.txt"); // Path to store the counter
            if (file.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line = reader.readLine();
                if (line != null) {
                    paymentCounter = Integer.parseInt(line.trim());
                }
                reader.close();
            }
        } catch (IOException e) {
            System.out.println("Error reading payment counter file: " + e.getMessage());
        }
    }

    private void updateCounterInFile() {
        try {
            File file = new File("src/database/payment_counter.txt");
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(String.valueOf(paymentCounter));
            writer.close();
        } catch (IOException e) {
            System.out.println("Error writing payment counter to file: " + e.getMessage());
        }
    }




    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPO = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        cmbPMethod = new javax.swing.JComboBox<>();
        txtPAmount = new javax.swing.JTextField();
        txtPDate = new javax.swing.JTextField();
        btnPayment = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();
        btnLoadPO = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 204));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("Payment Processing");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(26, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(23, 23, 23))
        );

        jPanel2.setBackground(new java.awt.Color(255, 233, 217));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("Purchase Order Pending for Payment...");

        tblPO.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "PO ID", "Item Name", "Quantity", "Item Price", "Total Price", "Supplier ID", "Status"
            }
        ));
        jScrollPane1.setViewportView(tblPO);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 882, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(84, 84, 84))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 204));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setText("Payment Details:");

        jLabel4.setText("Payment Method:");

        jLabel5.setText("Payment Amount (RM):");

        jLabel6.setText("Payment Date:");

        cmbPMethod.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Bank Transfer", "Cheque", "Cash", "Credit" }));

        btnPayment.setText("Process Payment");
        btnPayment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPaymentActionPerformed(evt);
            }
        });

        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        btnBack.setText("Back");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        btnLoadPO.setText("Load PO");
        btnLoadPO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoadPOActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel5))
                        .addGap(18, 18, 18))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addGap(32, 32, 32)))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbPMethod, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(txtPAmount, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                        .addComponent(txtPDate)))
                .addGap(146, 146, 146)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnLoadPO, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnPayment, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnBack, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jLabel3)
                        .addGap(34, 34, 34)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(cmbPMethod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnLoadPO)))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(txtPAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(33, 33, 33))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnPayment)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnCancel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(btnBack))
                .addContainerGap(35, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 277, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnPaymentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPaymentActionPerformed
        // Retrieve input data from the text fields and combo box
    String paymentAmount = txtPAmount.getText().trim(); // Get the payment amount
    String paymentDate = txtPDate.getText().trim(); // Get the payment date
    String paymentMethod = cmbPMethod.getSelectedItem() != null ? cmbPMethod.getSelectedItem().toString() : ""; // Get the payment method

    // Get the PO ID from the selected row
    int selectedRow = tblPO.getSelectedRow();
    if (selectedRow >= 0) {
        String poId = tblPO.getValueAt(selectedRow, 0).toString();  // Retrieve PO ID from the selected row

        // Check if this PO has already been paid
        if (paidPOs.contains(poId)) {
            JOptionPane.showMessageDialog(this, "This PO has already been paid.", "Payment Status", JOptionPane.WARNING_MESSAGE);
            return;  // Prevent further processing
        }

        // Check if any required field is empty
        if (paymentAmount.isEmpty() || paymentDate.isEmpty() || paymentMethod.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all the fields!", "Missing Information", JOptionPane.WARNING_MESSAGE);
        } else {
            // Proceed if all fields are filled
            JOptionPane.showMessageDialog(this, "Paid Successfully!", "Payment Status", JOptionPane.INFORMATION_MESSAGE);

            // Generate a unique Payment_ID for the payment
            String paymentId = generatePaymentId(); // Implement this method to generate a new unique ID

            // Store payment data into payment.txt file using BufferedWriter
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/database/payment.txt", true))) {
                // Prepare the payment details string to write to the file
                String recordLine = paymentId + "," + poId + "," + tblPO.getValueAt(selectedRow, 1).toString() + "," 
                                    + tblPO.getValueAt(selectedRow, 2).toString() + "," + tblPO.getValueAt(selectedRow, 3).toString() + "," 
                                    + tblPO.getValueAt(selectedRow, 4).toString() + "," + tblPO.getValueAt(selectedRow, 5).toString() + "," 
                                    + tblPO.getValueAt(selectedRow, 6).toString() + "," + paymentMethod + "," + paymentAmount + "," + paymentDate + "\n";
                writer.write(recordLine);
                writer.newLine();  // Blank line between entries for readability
                System.out.println("Payment details saved to file.");

                // Add the PO ID to the paidPOs set to prevent re-payment
                paidPOs.add(poId);

                // Update the PO status in the purchase_orders.txt file
                updatePOStatus(poId, "PAID");

                // Reload the table data to reflect the updated state
                loadApprovedPOs();

                // Optionally, show a confirmation dialog or log the success
                JOptionPane.showMessageDialog(this, "Payment details saved successfully.", "Payment Status", JOptionPane.INFORMATION_MESSAGE);

            } catch (IOException ex) {
                // Catch exceptions and display the error
                JOptionPane.showMessageDialog(this, "Error saving payment information.", "Error", JOptionPane.ERROR_MESSAGE);
                System.out.println("Error: " + ex.getMessage());
            }

            // Optional: Clear fields after success
            txtPAmount.setText("");
            txtPDate.setText("");
            cmbPMethod.setSelectedIndex(0); // Reset to first option
        }
    } else {
        JOptionPane.showMessageDialog(this, "Please select a PO to process payment.", "Error", JOptionPane.ERROR_MESSAGE);
    }

    }//GEN-LAST:event_btnPaymentActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        txtPAmount.setText("");
        txtPDate.setText("");
        cmbPMethod.setSelectedIndex(0);
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
    if (previousFMForm != null) {
        previousFMForm.setVisible(true);  // Show the previous Finance Manager form
    } else if (previousAdminForm != null) {
        previousAdminForm.setVisible(true);  // Show the previous Admin form
    }
    this.setVisible(false);  // Hide the current frame instead of disposing it

    }//GEN-LAST:event_btnBackActionPerformed

    private void btnLoadPOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoadPOActionPerformed
        loadApprovedPOs();
    }//GEN-LAST:event_btnLoadPOActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
         new PaymentFrame();
         //new PaymentFrame();
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PaymentFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PaymentFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PaymentFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PaymentFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PaymentFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnLoadPO;
    private javax.swing.JButton btnPayment;
    private javax.swing.JComboBox<String> cmbPMethod;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblPO;
    private javax.swing.JTextField txtPAmount;
    private javax.swing.JTextField txtPDate;
    // End of variables declaration//GEN-END:variables
}
