
package salesManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;


public class EditItem extends javax.swing.JFrame {


    public EditItem() {
        initComponents();
        initializeSupplierList();
    }
    
    private String originalItemId;

    public EditItem(String itemId, String itemName, String supplierId, double price, String category) {
        initComponents();
        this.originalItemId = itemId;

        // Display original values
        tfItemName.setText(itemName);
        tfPrice.setText(String.valueOf(price));
        labelSupplierID.setText(supplierId);

        // Set category in combobox
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(
            new String[]{"GROCERIES", "FRESH PRODUCE", "ESSENTIAL GOODS"});
        cbCategory.setModel(model);
        cbCategory.setSelectedItem(category);

        // Setup button actions
        setupSaveButton();
        setupBackButton();
         initializeSupplierList(); 
    }
    
    private void initializeSupplierList() {
        DefaultListModel<String> model = new DefaultListModel<>();
        Set<String> uniqueEntries = new HashSet<>();

        // Extract existing supplier IDs from label (e.g., "S001 | S002")
        Set<String> existingSupplierIds = new HashSet<>();
        String supplierLabel = labelSupplierID.getText().trim();
        if (!supplierLabel.isEmpty()) {
            for (String id : supplierLabel.split("\\|")) {
                existingSupplierIds.add(id.trim());
            }
        }

        File supplierFile = new File("src/database/suppliers.txt");
        if (!supplierFile.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(supplierFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String supplierId = parts[0].trim();
                    String supplierName = parts[1].trim();

                    // Skip if this supplier is already linked to the item
                    if (existingSupplierIds.contains(supplierId)) continue;

                    String entry = supplierId + " - " + supplierName;

                    if (uniqueEntries.add(entry)) {
                        model.addElement(entry);
                    }
                }
            }
            listSupplier.setModel(model);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                "Error loading suppliers: " + e.getMessage(),
                "File Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void setupAddSupplierButton() {
        btnAddSupplier.addActionListener(e -> {
            String selected = listSupplier.getSelectedValue();
            if (selected == null) {
                JOptionPane.showMessageDialog(this,
                    "Please select a supplier from the list.",
                    "No Supplier Selected",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Extract the supplier ID before " - "
            String newSupplierId = selected.split("-")[0].trim();

            // Get current label and append new supplier if not already included
            String current = labelSupplierID.getText().trim();
            Set<String> supplierSet = new HashSet<>();
            for (String id : current.split("\\|")) {
                supplierSet.add(id.trim());
            }

            if (supplierSet.contains(newSupplierId)) {
                JOptionPane.showMessageDialog(this,
                    "This supplier is already linked to the item.",
                    "Duplicate Supplier",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            supplierSet.add(newSupplierId);
            labelSupplierID.setText(String.join(" | ", supplierSet));

            // Remove added supplier from the list
            ((DefaultListModel<String>) listSupplier.getModel()).removeElement(selected);
        });
    }


    
    private void setupSaveButton() {
        jButton1.addActionListener(e -> {
            try {
                String newName = tfItemName.getText().trim();
                String newPriceText = tfPrice.getText().trim();
                String newCategory = (String) cbCategory.getSelectedItem();
                String supplierId = labelSupplierID.getText();

                // Validate inputs
                if (newName.isEmpty() || newPriceText.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                        "Please fill in all fields",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }

                double newPrice;
                try {
                    newPrice = Double.parseDouble(newPriceText);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this,
                        "Please enter a valid price",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Update the item in the file
                updateItemInFile(newName, supplierId, newPrice, newCategory);

                // Return to ItemEntry
                returnToItemEntry();

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                    "Error updating item: " + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void updateItemInFile(String newName, String supplierId, double newPrice, String newCategory) 
            throws IOException {
        File itemsFile = new File("src/database/items.txt");
        File tempFile = new File("src/database/items_temp.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(itemsFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5 && parts[0].equals(originalItemId)) {
                    // Write the updated item
                    writer.write(String.join(",",
                        originalItemId, // Keep same ID
                        newName,
                        supplierId,
                        String.valueOf(newPrice),
                        newCategory
                    ));
                } else {
                    // Write other items unchanged
                    writer.write(line);
                }
                writer.newLine();
            }
        }

        // Replace original file with updated file
        itemsFile.delete();
        tempFile.renameTo(itemsFile);
    }

    private void setupBackButton() {
        jButton2.addActionListener(e -> returnToItemEntry());
    }

    private void returnToItemEntry() {
        new ItemEntry().setVisible(true);
        this.dispose();
    }
    
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        tfItemName = new javax.swing.JTextField();
        tfPrice = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        cbCategory = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        labelSupplierID = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        listSupplier = new javax.swing.JList<>();
        btnAddSupplier = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 204));

        jLabel2.setText("Item Name:");

        jLabel3.setText("Supplier ID:");

        jLabel4.setText("Price");

        jLabel5.setText("Category");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("Edit Item");

        cbCategory.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jButton1.setText("Save");

        jButton2.setText("Cancel");

        labelSupplierID.setText("SupplierID");

        jLabel6.setText("New Supplier:");

        listSupplier.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(listSupplier);

        btnAddSupplier.setText("Add Supplier");
        btnAddSupplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddSupplierActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel1)
                .addContainerGap(294, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(5, 5, 5)
                            .addComponent(jButton1)
                            .addGap(50, 50, 50)
                            .addComponent(jButton2)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel5)
                                .addComponent(jLabel4))
                            .addGap(18, 18, 18)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(tfPrice)
                                .addComponent(cbCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2)
                            .addComponent(jLabel6))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(tfItemName, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
                            .addComponent(labelSupplierID)
                            .addComponent(jScrollPane1)
                            .addComponent(btnAddSupplier, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(68, 68, 68))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel1)
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(tfItemName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(labelSupplierID))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                .addComponent(btnAddSupplier)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(tfPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(cbCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(38, 38, 38)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddSupplierActionPerformed
        setupAddSupplierButton();
    }//GEN-LAST:event_btnAddSupplierActionPerformed

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
            java.util.logging.Logger.getLogger(EditItem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EditItem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EditItem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EditItem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new EditItem().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddSupplier;
    private javax.swing.JComboBox<String> cbCategory;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel labelSupplierID;
    private javax.swing.JList<String> listSupplier;
    private javax.swing.JTextField tfItemName;
    private javax.swing.JTextField tfPrice;
    // End of variables declaration//GEN-END:variables
}
