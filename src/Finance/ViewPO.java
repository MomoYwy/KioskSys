package Finance;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import shared.models.PurchaseOrder;
import javax.swing.JOptionPane;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import javax.swing.event.*;
import java.io.*;
import java.util.*;
import javax.swing.table.*;



public class ViewPO extends javax.swing.JFrame {
    

    public ViewPO() {
        initComponents(); 
        initializeTableListener();
        
    }
    
        private void initializeTableListener() {
        tblPO.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                // Check if a valid row is selected
                if (!e.getValueIsAdjusting() && tblPO.getSelectedRow() != -1) {
                    int selectedRow = tblPO.getSelectedRow();
                    // Retrieve data from the selected row
                    String poId = tblPO.getValueAt(selectedRow, 0).toString();
                    String prId = tblPO.getValueAt(selectedRow, 1).toString();
                    String itemId = tblPO.getValueAt(selectedRow, 2).toString();
                    String itemName = tblPO.getValueAt(selectedRow, 3).toString();
                    String quantity = tblPO.getValueAt(selectedRow, 4).toString();
                    String itemPrice = tblPO.getValueAt(selectedRow, 5).toString();
                    String totalPrice = tblPO.getValueAt(selectedRow, 6).toString();
                    String dateCreated = tblPO.getValueAt(selectedRow, 7).toString();
                    String dateRequired = tblPO.getValueAt(selectedRow, 8).toString();
                    String supplierId = tblPO.getValueAt(selectedRow, 9).toString();
                    String status = tblPO.getValueAt(selectedRow, 10).toString();
                    
                    // Populate the fields accordingly
                    jLabel3.setText(poId);
                    jLabel4.setText(prId);
                    jLabel5.setText(itemId);
                    jLabel6.setText(itemName);
                    jTextField1.setText(quantity);
                    jLabel13.setText(itemPrice);
                    jLabel14.setText(totalPrice);
                    jLabel7.setText(dateCreated);
                    jLabel15.setText(dateRequired);
                    jLabel19.setText(supplierId);
                    jLabel20.setText(status);
                }
            }
        });
    }
    
    // Function to update the status of the selected PO
    private void updatePOStatus(String poId, String status) {
        for (int i = 0; i < tblPO.getRowCount(); i++) {
            if (tblPO.getValueAt(i, 0).equals(poId)) {
                tblPO.setValueAt(status, i, 12); 
                break;
            }
        }}
          
    private void saveApprovedPOToFile(String poId) {
        try {
        // Read the existing file contents
            File file = new File("src\\database\\purchase_orders.txt");  
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder fileContent = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
            // Trim the line to avoid issues with trailing spaces
                line = line.trim();

            // Find and update the status from 'PENDING' to 'APPROVED' for the selected PO ID
                if (line.contains(poId) && line.contains("PENDING")) {
                    line = line.replace("PENDING", "APPROVED");
                }
                fileContent.append(line).append("\n");
            }
            reader.close();

        // Write the updated content back to the file
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(fileContent.toString());
            writer.close();

        // Optional: Log or show a message confirming the update
            System.out.println("File updated with PO ID " + poId + " status as APPROVED.");

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private void saveChangesAction() {
    // Retrieve the selected PO ID and the new values entered by the user
    String poId = jLabel3.getText(); // PO ID from the selected row
    String newStatus = (String) cmbStatus.getSelectedItem(); // New status from the combo box
    String newSupplierId = (String) cmbSupplierID.getSelectedItem(); // New Supplier ID from the combo box
    String newQuantity = jTextField1.getText(); // New quantity from the text field

    // Validate input: Ensure the user entered a valid quantity
    try {
        int quantity = Integer.parseInt(newQuantity); // Validate the quantity (integer)
        if (quantity <= 0) {
            JOptionPane.showMessageDialog(this, "Quantity must be greater than 0!");
            return;
        }
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Please enter a valid quantity!");
        return;
    }

    // File path to the purchase_orders.txt
    File file = new File("src\\database\\purchase_orders.txt");

    try {
        // Read the file
        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuilder fileContent = new StringBuilder();
        String line;

        // Iterate through the file lines and update the corresponding row
        while ((line = reader.readLine()) != null) {
            String[] fields = line.split(","); // Assuming CSV format

            if (fields[0].equals(poId)) { // Check if PO_ID matches
                fields[4] = newQuantity; // Update the quantity (index 4 is the quantity column)
                fields[12] = newStatus;  // Update the status (index 10 is the status column)
                fields[9] = newSupplierId; // Update the supplier ID (index 9 is the supplier ID column)
                
                
                // Debugging: Check if the status value is correctly retrieved
                System.out.println("Updated status: " + newStatus);

                line = String.join(",", fields); // Rebuild the line with updated values
            }
            fileContent.append(line).append("\n");
        }
        reader.close();

        // Write the updated content back to the file
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write(fileContent.toString());
        writer.close();

        // Update the table to reflect the new changes
        int selectedRow = tblPO.getSelectedRow();
        tblPO.getModel().setValueAt(newQuantity, selectedRow, 4); // Update quantity in table
        tblPO.getModel().setValueAt(newStatus, selectedRow, 10);  // Update status in table
        tblPO.getModel().setValueAt(newSupplierId, selectedRow, 9); // Update Supplier ID in table

        // Notify the user of the successful update
        JOptionPane.showMessageDialog(this, "Changes have been saved successfully!");

    } catch (IOException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error saving changes to the file.");
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
        lblPOID = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPO = new javax.swing.JTable();
        btnback = new javax.swing.JButton();
        btnLoadPO = new javax.swing.JButton();
        btnApprove = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        cmbSupplierID = new javax.swing.JComboBox<>();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        cmbStatus = new javax.swing.JComboBox<>();
        btnSaveChanges = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 204));
        jPanel1.setForeground(new java.awt.Color(255, 255, 204));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("View & Approve PO");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        lblPOID.setBackground(new java.awt.Color(255, 233, 217));

        tblPO.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "PO ID", "PR ID", "Item ID", "Item Name", "Quantity", "Item Price", "Total Price", "Date Created", "Date Required", "Supplier ID", "Sales Manager ID", "Purchase Manager ID", "Status"
            }
        ));
        jScrollPane1.setViewportView(tblPO);

        btnback.setText("Back");
        btnback.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnbackActionPerformed(evt);
            }
        });

        btnLoadPO.setText("Load PO");
        btnLoadPO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoadPOActionPerformed(evt);
            }
        });

        btnApprove.setText("Approve PO");
        btnApprove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnApproveActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("Please edit the quantity from the table:");

        jLabel26.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel26.setText("PO ID:");

        jLabel27.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel27.setText("PR ID:");

        jLabel28.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel28.setText("Item ID:");

        jLabel29.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel29.setText("Item Name:");

        jLabel30.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel30.setText("Quantity:");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel8.setText("Item Price:");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel9.setText("Total Price:");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel10.setText("Date Created:");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel11.setText("Date Required:");

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel12.setText("Supplier ID:");

        cmbSupplierID.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "S001", "S002", "S003", "S004", "S005", "S006" }));

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel16.setText("Sales Manager ID:");

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel17.setText("Purchase Manager ID:");

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel18.setText("Status:");

        cmbStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "PENDING", "APPROVED", "REJECTED" }));

        btnSaveChanges.setText("Save Changes");
        btnSaveChanges.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveChangesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout lblPOIDLayout = new javax.swing.GroupLayout(lblPOID);
        lblPOID.setLayout(lblPOIDLayout);
        lblPOIDLayout.setHorizontalGroup(
            lblPOIDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(lblPOIDLayout.createSequentialGroup()
                .addGroup(lblPOIDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(lblPOIDLayout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jLabel2))
                    .addGroup(lblPOIDLayout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addGroup(lblPOIDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel27)
                            .addComponent(jLabel26)
                            .addComponent(jLabel28)
                            .addComponent(jLabel29)
                            .addComponent(jLabel30))
                        .addGap(18, 18, 18)
                        .addGroup(lblPOIDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(86, 86, 86)
                        .addGroup(lblPOIDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, lblPOIDLayout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, lblPOIDLayout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, lblPOIDLayout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, lblPOIDLayout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, lblPOIDLayout.createSequentialGroup()
                                .addComponent(btnLoadPO)
                                .addGap(40, 40, 40)
                                .addComponent(btnApprove)
                                .addGap(43, 43, 43)
                                .addComponent(btnSaveChanges))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, lblPOIDLayout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addGap(18, 18, 18)
                                .addComponent(cmbSupplierID, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(lblPOIDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(lblPOIDLayout.createSequentialGroup()
                                .addGap(90, 90, 90)
                                .addGroup(lblPOIDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, lblPOIDLayout.createSequentialGroup()
                                        .addComponent(jLabel17)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, lblPOIDLayout.createSequentialGroup()
                                        .addComponent(jLabel18)
                                        .addGap(18, 18, 18)
                                        .addComponent(cmbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, lblPOIDLayout.createSequentialGroup()
                                        .addComponent(jLabel16)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(lblPOIDLayout.createSequentialGroup()
                                .addGap(40, 40, 40)
                                .addComponent(btnback)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        lblPOIDLayout.setVerticalGroup(
            lblPOIDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lblPOIDLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(11, 11, 11)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addGroup(lblPOIDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(jLabel3)
                    .addComponent(jLabel8)
                    .addComponent(jLabel13))
                .addGap(18, 18, 18)
                .addGroup(lblPOIDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel27)
                    .addGroup(lblPOIDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(jLabel9)
                        .addComponent(jLabel14)))
                .addGap(18, 18, 18)
                .addGroup(lblPOIDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addGroup(lblPOIDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel28)
                        .addComponent(jLabel5)
                        .addComponent(jLabel16)
                        .addComponent(jLabel19)
                        .addComponent(jLabel7)))
                .addGap(20, 20, 20)
                .addGroup(lblPOIDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29)
                    .addComponent(jLabel6)
                    .addComponent(jLabel11)
                    .addComponent(jLabel15)
                    .addComponent(jLabel17)
                    .addComponent(jLabel20))
                .addGap(18, 18, 18)
                .addGroup(lblPOIDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30)
                    .addComponent(jLabel12)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbSupplierID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18)
                    .addComponent(cmbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
                .addGroup(lblPOIDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLoadPO)
                    .addComponent(btnApprove)
                    .addComponent(btnback)
                    .addComponent(btnSaveChanges))
                .addGap(31, 31, 31))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lblPOID, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblPOID, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnApproveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnApproveActionPerformed
        btnApprove.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Get the selected row from the JTable
                int selectedRow = tblPO.getSelectedRow();

                // Check if a row is selected
                if (selectedRow >= 0) {
                    // Get the PO details from the selected row
                    String poId = tblPO.getValueAt(selectedRow, 0).toString(); // PO ID
                    String status = tblPO.getValueAt(selectedRow, 12).toString(); // Status column (index 12)

                    // Verify if the PO is still in "PENDING" status
                    if (status.equals("PENDING")) {
                        // Change the status of the PO to "APPROVED"
                        updatePOStatus(poId, "APPROVED");

                        // Update the file with the new status
                        saveApprovedPOToFile(poId);

                        // Show a confirmation message
                        JOptionPane.showMessageDialog(null, poId + " has been approved.");
                    } else {
                        // Show an error message if the PO is not in "PENDING"
                        JOptionPane.showMessageDialog(null, "PO " + poId + " is already " + status + ".");
                    }
                } else {
                    // If no PO is selected, show a message
                    JOptionPane.showMessageDialog(null, "Please select a PO to approve.");
                }
            }
        });
    }//GEN-LAST:event_btnApproveActionPerformed

    private void btnLoadPOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoadPOActionPerformed
        dataOperation LD1 = new dataOperation();
        List<PurchaseOrder> purchaseOrders = LD1.ReadFile("purchase_orders", PurchaseOrder.class); // Pass the PurchaseOrder class type

        // Get the DefaultTableModel for your JTable
        DefaultTableModel model = (DefaultTableModel) tblPO.getModel();
        model.setRowCount(0);  // Clear any existing rows

        // Loop through each PurchaseOrder and add it to the JTable
        for (PurchaseOrder po : purchaseOrders) {
            model.addRow(new Object[]{
                po.getPurchaseOrderId(),
                po.getPurchaseRequisitionId(),
                po.getItemId(),
                po.getItemName(),
                po.getQuantity(),
                po.getItemPrice(),
                po.getTotalPrice(),
                po.getDateCreated(),
                po.getDateRequired(),
                po.getSupplierId(),
                po.getSalesManagerId(),
                po.getPurchaseManagerId(),
                po.getStatus()
            });}

    }//GEN-LAST:event_btnLoadPOActionPerformed

    private void btnbackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnbackActionPerformed
        btnback.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                FinanceDashboard financeDashboard = new FinanceDashboard();
                financeDashboard.setVisible(true);
                dispose();
            }});
    }//GEN-LAST:event_btnbackActionPerformed

    private void btnSaveChangesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveChangesActionPerformed
        // TODO add your handling code here:
        btnSaveChanges.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        saveChangesAction();  // Call the method when the button is clicked
    }
});
    }//GEN-LAST:event_btnSaveChangesActionPerformed

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
            java.util.logging.Logger.getLogger(ViewPO.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ViewPO.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ViewPO.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ViewPO.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ViewPO().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnApprove;
    private javax.swing.JButton btnLoadPO;
    private javax.swing.JButton btnSaveChanges;
    private javax.swing.JButton btnback;
    private javax.swing.JComboBox<String> cmbStatus;
    private javax.swing.JComboBox<String> cmbSupplierID;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JPanel lblPOID;
    private javax.swing.JTable tblPO;
    // End of variables declaration//GEN-END:variables
}
