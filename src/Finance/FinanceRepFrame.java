package Finance;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import shared.utils.FileUtils;
import static shared.utils.FileUtils.ensureFileExists;




public class FinanceRepFrame extends javax.swing.JFrame {

    public FinanceRepFrame() {
        initComponents();
        loadFRepToTable();
        initializeStatusComboBox();
    }

    public static final String FinanceRep_FILE = "src/database/financeReport.txt";
    
        private void initializeStatusComboBox() {
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
    
    // Add predefined values to the model
    model.addElement("Approved");
    model.addElement("Rejected");
    model.addElement("Completed");
    model.addElement("Pending");
    
    // Set the combo box model
    cmbStatus.setModel(model);
    }

        
    private void addNewFinanceRep() throws IOException {
         // 1. Ensure finance report file exists
        if (!ensureFileExists(FinanceRep_FILE)) {
        return;
        }
        
        String grossSales =txtGrossSales.getText().trim();
        String discount = txtDiscount.getText().trim();
        String netTotal = txtNetTotal.getText().trim();
        String date = txtDate.getText().trim();
        String status = (String) cmbStatus.getSelectedItem();
        
        // 3. Validate inputs
        if (grossSales.isEmpty() || discount.isEmpty() || netTotal.isEmpty() || 
            date.isEmpty() || status.isEmpty() ) {
            showErrorDialog("Input Error", "Please fill in all fields");
            return;
        }
        
        try {
        
            // 4. Generate Finance Report ID 
                
            int maxId = 0;
            File file = new File(FinanceRep_FILE);
            if (file.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (line.startsWith("F")) {
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

            String FRepID = "F" + String.format("%03d", maxId + 1);
            
            // 6. Save to file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
                writer.write(String.join(",",
                    FRepID,
                    date,
                    netTotal,
                    status
                ));
                writer.newLine();
            }
            
            // 7. Refresh and clear
            loadFRepToTable(); 
            clearFinanceRep();

            JOptionPane.showMessageDialog(this, 
                "Finance record added successfully!\nID: " + FRepID,
                "Success",
                JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException e) {
            showErrorDialog("File Error", "Failed to save finance : " + e.getMessage());
        }
    }

    private void clearFinanceRep() {
        txtGrossSales.setText("");
        txtDiscount.setText("");
        txtNetTotal.setText("");
        txtDate.setText("");
        cmbStatus.setSelectedIndex(0);
    }

    private static void showErrorDialog(String title, String message) {
        JOptionPane.showMessageDialog(null, 
            message, 
            title, 
            JOptionPane.ERROR_MESSAGE);
    }
    
    private void loadFRepToTable() {
    DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
    model.setRowCount(0);  // Clear any existing rows in the table
    
    try {
        File file = new File(FinanceRep_FILE);  // Use the financeReport file
        if (file.exists()) {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            
            // Read each line from the file
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");  // Split the line by commas
                
                // Ensure there are 4 elements (Report ID, Date, Net Total, Status)
                if (parts.length == 4) {
                    // Add data to the table model
                    model.addRow(new Object[]{
                        parts[0],  // Report ID
                        parts[1],  // Date
                        parts[2],  // Net Total
                        parts[3]   // Status
                    });
                }
            }
            reader.close();  // Close the BufferedReader
        } else {
            JOptionPane.showMessageDialog(this, "File not found: " + FinanceRep_FILE, "Error", JOptionPane.ERROR_MESSAGE);
        }
    } catch (IOException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error reading file: " + e.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
    }
}


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        cmbStatus = new javax.swing.JComboBox<>();
        txtDate = new javax.swing.JTextField();
        txtGrossSales = new javax.swing.JTextField();
        txtDiscount = new javax.swing.JTextField();
        txtNetTotal = new javax.swing.JTextField();
        btnGenerate = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 204));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("Finance Report");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(31, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(22, 22, 22))
        );

        jPanel2.setBackground(new java.awt.Color(255, 233, 217));

        jLabel2.setText("Gross Sales (RM):");

        jLabel3.setText("Discount (RM):");

        jLabel4.setText("Net Total (RM):");

        jLabel5.setText("Date:");

        jLabel6.setText("Status:");

        cmbStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Approved", "Rejected", "Completed", "Pending" }));
        cmbStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbStatusActionPerformed(evt);
            }
        });

        btnGenerate.setText("Generate");
        btnGenerate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerateActionPerformed(evt);
            }
        });

        btnDelete.setText("Delete");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Report ID", "Date", "Net Total (RM)", "Status"
            }
        ));
        jScrollPane2.setViewportView(jTable2);

        jButton1.setText("Edit");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(txtDiscount, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                    .addComponent(txtGrossSales)
                    .addComponent(txtNetTotal))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                            .addGap(32, 32, 32)
                            .addComponent(jLabel5)
                            .addGap(18, 18, 18)
                            .addComponent(txtDate, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                            .addGap(24, 24, 24)
                            .addComponent(jLabel6)
                            .addGap(18, 18, 18)
                            .addComponent(cmbStatus, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addComponent(btnGenerate)
                        .addGap(18, 18, 18)
                        .addComponent(btnDelete)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 653, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(15, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel5)
                    .addComponent(txtDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtGrossSales, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel6)
                    .addComponent(cmbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDiscount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtNetTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnGenerate)
                    .addComponent(btnDelete)
                    .addComponent(jButton1))
                .addGap(29, 29, 29)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnGenerateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerateActionPerformed
            try {
                addNewFinanceRep();
    } catch (IOException ex) {
        showErrorDialog("File Error", "Failed to add finance report: " + ex.getMessage());
    }
    }//GEN-LAST:event_btnGenerateActionPerformed

    private void cmbStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbStatusActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbStatusActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
    int selectedRow = jTable2.getSelectedRow();  // Get the selected row index

    // Check if a row is selected
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this,
            "Please select an item to delete",
            "No Selection",
            JOptionPane.WARNING_MESSAGE);
        return;
    }

    // Get the selected row's Report ID (FRepID) and Date
    String FRepID = (String) jTable2.getValueAt(selectedRow, 0);
    String date = (String) jTable2.getValueAt(selectedRow, 1);

    // Confirmation dialog
    int confirm = JOptionPane.showConfirmDialog(this,
        "Are you sure you want to delete:\n" + date + " (ID: " + FRepID + ")",
        "Confirm Deletion",
        JOptionPane.YES_NO_OPTION);

    // If the user confirms the deletion
    if (confirm == JOptionPane.YES_OPTION) {
        try {
            // Delete the row from the file based on Report ID (FRepID)
            FileUtils.deleteFromFileByField(FinanceRep_FILE, 0, FRepID);  // Field 0 is Report ID

            // Remove the row from the table (DefaultTableModel)
            DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
            model.removeRow(selectedRow);  // Remove the row from the table

            JOptionPane.showMessageDialog(this,
                "Item deleted successfully",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                "Error deleting item: " + ex.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
            int selectedRow = jTable2.getSelectedRow();  // Get the selected row index

    // Check if a row is selected
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this,
            "Please select an item to edit",
            "No Selection",
            JOptionPane.WARNING_MESSAGE);
        return;
    }

    // Get the selected row's Report ID (FRepID) and Date
    String FRepID = (String) jTable2.getValueAt(selectedRow, 0);
    String date = (String) jTable2.getValueAt(selectedRow, 1);
    String netTotal = (String) jTable2.getValueAt(selectedRow, 2);
    String status = (String) jTable2.getValueAt(selectedRow, 3);

    // Show confirmation dialog
    int confirm = JOptionPane.showConfirmDialog(this,
        "Are you sure you want to edit:\n" + date + " (ID: " + FRepID + ")",
        "Confirm Edit",
        JOptionPane.YES_NO_OPTION);

    // If the user confirms the edit
    if (confirm == JOptionPane.YES_OPTION) {
        // Allow user to edit the fields (e.g., Gross Sales, Date, Discount, etc.)
        txtGrossSales.setText("");  // You can pre-fill these with the current values
        txtDiscount.setText("");    // Add the current values if you wish
        txtNetTotal.setText(netTotal);
        txtDate.setText(date);
        cmbStatus.setSelectedItem(status);  // Set the current status
    }
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
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
            java.util.logging.Logger.getLogger(FinanceRepFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FinanceRepFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FinanceRepFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FinanceRepFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FinanceRepFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnGenerate;
    private javax.swing.JComboBox<String> cmbStatus;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField txtDate;
    private javax.swing.JTextField txtDiscount;
    private javax.swing.JTextField txtGrossSales;
    private javax.swing.JTextField txtNetTotal;
    // End of variables declaration//GEN-END:variables
}
