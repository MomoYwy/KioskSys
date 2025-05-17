package Finance;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import shared.models.PurchaseOrder;
import shared.models.dataOperation;

/**
 *
 * @author User
 */
public class ManagementPO extends javax.swing.JFrame {

    /**
     * Creates new form ManagementPO
     */
    public ManagementPO() {
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
                    String SalesManagerId = tblPO.getValueAt(selectedRow, 10).toString();
                    String PurchaseManagerId = tblPO.getValueAt(selectedRow, 11).toString();
                    String status = tblPO.getValueAt(selectedRow, 12).toString();
                    
                    // Populate the fields accordingly
                    lblPOid.setText(poId);
                    lblPRid.setText(prId);
                    lblitemID.setText(itemId);
                    lblitemName.setText(itemName);
                    lblQuantity.setText(quantity);
                    lblitemPrice.setText(itemPrice);
                    lblTotalPrice.setText(totalPrice);
                    lblDateCreated.setText(dateCreated);
                    lblDateRequired.setText(dateRequired);
                    lblSupplierID.setText(supplierId);
                    lblSalesManagerID.setText(SalesManagerId);
                    lblPurchaseManagerID.setText(PurchaseManagerId);
                    lblStatus.setText(status);
                    

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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPO = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
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
        lblPOid = new javax.swing.JLabel();
        lblQuantity = new javax.swing.JLabel();
        lblDateRequired = new javax.swing.JLabel();
        lblPRid = new javax.swing.JLabel();
        lblitemID = new javax.swing.JLabel();
        lblitemName = new javax.swing.JLabel();
        lblitemPrice = new javax.swing.JLabel();
        lblTotalPrice = new javax.swing.JLabel();
        lblDateCreated = new javax.swing.JLabel();
        lblSupplierID = new javax.swing.JLabel();
        lblSalesManagerID = new javax.swing.JLabel();
        lblPurchaseManagerID = new javax.swing.JLabel();
        lblStatus = new javax.swing.JLabel();
        btnLoadPO = new javax.swing.JButton();
        btnApprovePO = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 204));

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel14.setText("PO Management");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel14)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(21, Short.MAX_VALUE)
                .addComponent(jLabel14)
                .addGap(20, 20, 20))
        );

        jPanel2.setBackground(new java.awt.Color(255, 233, 217));

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

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setText("PO ID:");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("PR ID:");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setText("Item ID:");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setText("Item Name:");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setText("Quantity:");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setText("Item Price:");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setText("Total Price:");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel8.setText("Date Created:");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel9.setText("Date Required:");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel10.setText("Supplier ID:");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel11.setText("Sales Manager ID:");

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel12.setText("Purchase Manager ID:");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel13.setText("Status:");

        lblPOid.setText("lblPOid");

        lblQuantity.setText("lblQuantity");

        lblDateRequired.setText("lblDateRequired");

        lblPRid.setText("lblPRid");

        lblitemID.setText("lblitemID");

        lblitemName.setText("lblitemName");

        lblitemPrice.setText("lblitemPrice");

        lblTotalPrice.setText("lblTotalPrice");

        lblDateCreated.setText("lblDateCreated");

        lblSupplierID.setText("lblSupplierID");

        lblSalesManagerID.setText("lblSalesManagerID");

        lblPurchaseManagerID.setText("lblPurchaseManagerID");

        lblStatus.setText("lblStatus");

        btnLoadPO.setText("Load PO");
        btnLoadPO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoadPOActionPerformed(evt);
            }
        });

        btnApprovePO.setText("Approve PO");
        btnApprovePO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnApprovePOActionPerformed(evt);
            }
        });

        btnEdit.setText("Edit");

        btnBack.setText("Back");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblitemName, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblitemID, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblPRid, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblPOid, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap(239, Short.MAX_VALUE)
                        .addComponent(btnLoadPO)
                        .addGap(40, 40, 40)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btnApprovePO)
                        .addGap(43, 43, 43)
                        .addComponent(btnEdit)
                        .addGap(43, 43, 43)
                        .addComponent(btnBack))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblDateCreated, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblDateRequired, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblTotalPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblitemPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(36, 36, 36)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addGap(18, 18, 18)
                                .addComponent(lblStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addGap(18, 18, 18)
                                .addComponent(lblSalesManagerID, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addGap(18, 18, 18)
                                .addComponent(lblSupplierID, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addGap(18, 18, 18)
                                .addComponent(lblPurchaseManagerID, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(lblPOid))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(lblitemPrice)
                    .addComponent(jLabel10)
                    .addComponent(lblSupplierID)
                    .addComponent(jLabel2)
                    .addComponent(lblPRid))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel11)
                        .addComponent(lblSalesManagerID))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblTotalPrice)
                        .addComponent(jLabel7)
                        .addComponent(jLabel3)
                        .addComponent(lblitemID)))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPurchaseManagerID)
                    .addComponent(jLabel12)
                    .addComponent(lblDateCreated)
                    .addComponent(jLabel8)
                    .addComponent(lblitemName)
                    .addComponent(jLabel4))
                .addGap(15, 15, 15)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(lblDateRequired)
                    .addComponent(jLabel13)
                    .addComponent(lblStatus)
                    .addComponent(jLabel5)
                    .addComponent(lblQuantity))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLoadPO)
                    .addComponent(btnApprovePO)
                    .addComponent(btnEdit)
                    .addComponent(btnBack))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 16, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnApprovePOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnApprovePOActionPerformed
                btnApprovePO.addActionListener(new ActionListener() {
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
    }//GEN-LAST:event_btnApprovePOActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
                btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                FinanceDashboard financeDashboard = new FinanceDashboard();
                financeDashboard.setVisible(true);
                dispose();
            }});
    }//GEN-LAST:event_btnBackActionPerformed

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
            java.util.logging.Logger.getLogger(ManagementPO.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ManagementPO.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ManagementPO.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ManagementPO.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ManagementPO().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnApprovePO;
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnLoadPO;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblDateCreated;
    private javax.swing.JLabel lblDateRequired;
    private javax.swing.JLabel lblPOid;
    private javax.swing.JLabel lblPRid;
    private javax.swing.JLabel lblPurchaseManagerID;
    private javax.swing.JLabel lblQuantity;
    private javax.swing.JLabel lblSalesManagerID;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblSupplierID;
    private javax.swing.JLabel lblTotalPrice;
    private javax.swing.JLabel lblitemID;
    private javax.swing.JLabel lblitemName;
    private javax.swing.JLabel lblitemPrice;
    private javax.swing.JTable tblPO;
    // End of variables declaration//GEN-END:variables
}
