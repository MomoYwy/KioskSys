
package salesManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.ListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import shared.models.PurchaseRequisition;
import shared.models.Recordable;
import shared.utils.FileUtils;



public class PurchaseRequisitionEntry extends javax.swing.JFrame {
    
    private String userId;
    private String username;

    private static final String STOCK_FILE = "src/database/stocklist.txt";
    private static final String SUPPLIERS_FILE = "src/database/suppliers.txt";
    private static final String ITEMS_FILE = "src/database/items.txt";
    private static final String PR_FILE = "src/database/purchase_requisition.txt";
    private static final String RECORD_TYPE_ITEM = "ITEM";
    private static final String RECORD_TYPE_SUPPLIER = "SUPPLIER";
    private static final String RECORD_TYPE_SALES = "SALES";
    public static final String RECORD_TYPE_PURCHASE_REQUISITION = "PURCHASE_REQUISITION";

    
    public PurchaseRequisitionEntry(String userId, String username) {
        initComponents();
        this.userId = userId;
        this.username = username;
        loadStockList();
        selectFromTable();
    }
    
    private void loadStockList() {
        List<String[]> data = FileUtils.getLowStockItems(STOCK_FILE);
        DefaultTableModel model = (DefaultTableModel) tblStockList.getModel();
        model.setRowCount(0); 
        for (String[] row : data) {
            model.addRow(row);
        }
    }
    
    private void selectFromTable(){
        tblStockList.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting()) { // Only handle the final selection event
                    int selectedRow = tblStockList.getSelectedRow();
                    if (selectedRow != -1) { // Check if a row is actually selected
                        displaySelectedItemDetails(selectedRow);
                    }
                }
            }
        });
    }
    
    private void displaySelectedItemDetails(int selectedRow) {
        DefaultTableModel model = (DefaultTableModel) tblStockList.getModel();
        String itemID = (String) model.getValueAt(selectedRow, 0);
        String itemName = (String) model.getValueAt(selectedRow, 1);
        String stockAmount = (String) model.getValueAt(selectedRow, 3);

        lblSHItemID.setText(itemID);
        lblSHItemNm.setText(itemName);
        lblSHStockAmt.setText(stockAmount);

        // Use findLineWithValue to get supplier info
        List<String> supplierLines = FileUtils.findLinesWithValue(SUPPLIERS_FILE, itemName);
        if (supplierLines != null && !supplierLines.isEmpty()) {
            //split the line with ","
            List<String> supplierNames = new ArrayList<>();
            for (String supplierLine : supplierLines) {
                String[] supplierData = supplierLine.split(",");
                if (supplierData.length > 1) {
                    supplierNames.add(supplierData[1].trim());
                }
            }
            listSupplier.setListData(supplierNames.toArray(new String[0]));
        } else {
            listSupplier.setListData(new String[]{"Supplier Not Found"});
        }
    }

    private String getSupplierIdByName(String supplierName) throws IOException {
        List<String> lines = FileUtils.findLinesWithValue(SUPPLIERS_FILE, supplierName);
        if (lines != null && !lines.isEmpty()) {
            String[] parts = lines.get(0).split(",");
            if (parts.length > 0) {
                return parts[0]; // First part is supplier ID
            }
        }
        throw new IOException("Supplier not found");
    }
    
    private void showSuccessMessage(String title, String message) {
        JOptionPane.showMessageDialog(this, 
            message, 
            title, 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void clearForm() {
        lblSHItemID.setText("");
        lblSHItemNm.setText("");
        lblSHStockAmt.setText("");
        spQuantity.setValue(0);
        txtDateRequired.setText("");
        listSupplier.clearSelection();
    }

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        lblPR = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblStockList = new javax.swing.JTable();
        lblFlaggedStock = new javax.swing.JLabel();
        lblItemID = new javax.swing.JLabel();
        lblItemNm = new javax.swing.JLabel();
        lblStockAmt = new javax.swing.JLabel();
        lblQuantity = new javax.swing.JLabel();
        lblDateRequired = new javax.swing.JLabel();
        lblSupplier = new javax.swing.JLabel();
        btnAdd = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnViewList = new javax.swing.JButton();
        lblSHItemID = new javax.swing.JLabel();
        lblSHItemNm = new javax.swing.JLabel();
        lblSHStockAmt = new javax.swing.JLabel();
        txtDateRequired = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        listSupplier = new javax.swing.JList<>();
        spQuantity = new javax.swing.JSpinner();
        btnBack = new javax.swing.JButton();

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 233, Short.MAX_VALUE)
        );

        jButton1.setText("jButton1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 204));

        lblPR.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblPR.setText("Purchase Requisition (PR)");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(lblPR, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(429, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(35, Short.MAX_VALUE)
                .addComponent(lblPR)
                .addGap(26, 26, 26))
        );

        tblStockList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Item_ID", "Item_Name", "Category", "Stock_Amount"
            }
        ));
        jScrollPane1.setViewportView(tblStockList);

        lblFlaggedStock.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblFlaggedStock.setText("Flagged Stock:");

        lblItemID.setText("Item_ID:");

        lblItemNm.setText("Item_Name:");

        lblStockAmt.setText("Stock_Amount:");

        lblQuantity.setText("Quantity:");

        lblDateRequired.setText("Date_Required:");

        lblSupplier.setText("Supplier:");

        btnAdd.setText("Add");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnEdit.setText("Edit");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        btnViewList.setText("View List");

        lblSHItemID.setText("Item ID");

        lblSHItemNm.setText("Item Name");

        lblSHStockAmt.setText("Stock Amt");

        txtDateRequired.setText("(DD/MM/YYYY)");

        jScrollPane2.setViewportView(listSupplier);

        btnBack.setText("Back");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(lblFlaggedStock))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblItemID)
                            .addComponent(lblItemNm)
                            .addComponent(lblStockAmt))
                        .addGap(36, 36, 36)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblSHItemID, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblSHItemNm, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblSHStockAmt, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE))
                        .addGap(107, 107, 107)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblDateRequired)
                            .addComponent(lblSupplier)
                            .addComponent(lblQuantity))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtDateRequired, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(spQuantity))))
                .addContainerGap(139, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnViewList, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBack)
                .addGap(171, 171, 171))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21)
                .addComponent(lblFlaggedStock)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblItemID)
                    .addComponent(lblQuantity)
                    .addComponent(lblSHItemID)
                    .addComponent(spQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblDateRequired)
                        .addComponent(txtDateRequired, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblItemNm)
                        .addComponent(lblSHItemNm)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblStockAmt)
                        .addComponent(lblSHStockAmt))
                    .addComponent(lblSupplier)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAdd)
                    .addComponent(btnEdit)
                    .addComponent(btnViewList)
                    .addComponent(btnBack))
                .addGap(39, 39, 39))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
       String itemId = lblSHItemID.getText().trim();
        String itemName = lblSHItemNm.getText().trim();
        String stockAmountStr = lblSHStockAmt.getText().trim();
        String dateRequired = txtDateRequired.getText().trim();
        int quantity = (Integer) spQuantity.getValue();

        // Validate inputs
        if (itemId.isEmpty() || itemName.isEmpty() || stockAmountStr.isEmpty() || 
            dateRequired.isEmpty() || quantity <= 0) {
            JOptionPane.showMessageDialog(this, 
                "Please fill in all required fields",
                "Input Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Check for duplicate PR
            if (isDuplicatePR(itemName)) {
                JOptionPane.showMessageDialog(this,
                    "pending purchase requisition already exists\n",
                    "Duplicate Requisition",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            int stockAmount = Integer.parseInt(stockAmountStr);

            // Generate PR ID
            String prId = FileUtils.generatePurchaseRequisitionId(PR_FILE);

            // Get all supplier IDs from the list
            ListModel<String> supplierModel = listSupplier.getModel();
            StringBuilder allSupplierIds = new StringBuilder();

            for (int i = 0; i < supplierModel.getSize(); i++) {
                String supplierName = supplierModel.getElementAt(i);
                try {
                    String supplierId = getSupplierIdByName(supplierName);
                    if (i > 0) {
                        allSupplierIds.append("|");
                    }
                    allSupplierIds.append(supplierId);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this,
                        "Error getting ID for supplier: " + supplierName,
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            // Create PurchaseRequisition object
            PurchaseRequisition pr = new PurchaseRequisition(
                prId,
                itemId,
                itemName,
                stockAmount,
                quantity,
                dateRequired,
                allSupplierIds.toString(), // Pass all supplier IDs as pipe-separated string
                userId
            );

            // Add to file
            String savedId = FileUtils.addToFile(PR_FILE, pr);

            if (savedId != null) {
                showSuccessMessage("Purchase Requisition Created", 
                    "<html><b>PR ID:</b> " + prId + "<br>" +
                    "<b>Item:</b> " + itemName + " (" + itemId + ")<br>" +
                    "<b>Stock Amount:</b> " + stockAmount + "<br>" +
                    "<b>Quantity:</b> " + quantity + "<br>" +
                    "<b>Date Required:</b> " + dateRequired + "<br>" +
                    "<b>Supplier IDs:</b> " + allSupplierIds.toString().replace("|", ", ") + "<br>" +
                    "<b>Created by:</b> " + username + "</html>");

                clearForm();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Invalid stock amount value",
                "Input Error",
                JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                "Error: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        
    }//GEN-LAST:event_btnBackActionPerformed

    
    private boolean isDuplicatePR(String itemName) throws IOException {
        File prFile = new File(PR_FILE);
        if (!prFile.exists()) {
            return false;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(prFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                // Check if we have enough fields and status is 'pending"
                if (parts.length >= 10 && parts[2].equals(itemName) && parts[9].equals("Pending")) {
                    return true;
                }
            }
        }
        return false;
    }
    
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
            java.util.logging.Logger.getLogger(PurchaseRequisition.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PurchaseRequisition.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PurchaseRequisition.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PurchaseRequisition.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnViewList;
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblDateRequired;
    private javax.swing.JLabel lblFlaggedStock;
    private javax.swing.JLabel lblItemID;
    private javax.swing.JLabel lblItemNm;
    private javax.swing.JLabel lblPR;
    private javax.swing.JLabel lblQuantity;
    private javax.swing.JLabel lblSHItemID;
    private javax.swing.JLabel lblSHItemNm;
    private javax.swing.JLabel lblSHStockAmt;
    private javax.swing.JLabel lblStockAmt;
    private javax.swing.JLabel lblSupplier;
    private javax.swing.JList<String> listSupplier;
    private javax.swing.JSpinner spQuantity;
    private javax.swing.JTable tblStockList;
    private javax.swing.JTextField txtDateRequired;
    // End of variables declaration//GEN-END:variables
}
