package salesManager;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import shared.models.Address;
import shared.models.Supplier;
import shared.utils.FileUtils;


public class EditSupplier extends javax.swing.JFrame {

    private String supplierId;
    private ArrayList<JTextField> itemFields = new ArrayList<>();
    
    public EditSupplier(String supplierId) {
        this.supplierId = supplierId;
        initComponents();
        loadSupplierData();
        itemFields.add(tfSuppliedItem);
        
        // Hide new item fields by default
        tfNewItemName.setVisible(false);
        spNewItemPrice.setVisible(false);
        jLabel12.setVisible(false);
        jLabel13.setVisible(false);
    }
    
    private void loadSupplierData() {
        try {
            File file = new File(SupplierEntry.SUPPLIERS_FILE);
            if (file.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split(",");
                        if (parts.length >= 7 && parts[0].equals(supplierId)) {
                            // Load basic supplier info
                            tfSupplierName.setText(parts[1]);
                            tfSuppliedItem.setText(parts[2]);
                            spItemPrice.setValue(Double.parseDouble(parts[3]));
                            tfContact.setText(parts[4]);
                            jSpinner1.setValue(Integer.parseInt(parts[5]));
                            
                            // Parse address components
                            String[] addressParts = parts[6].split("\\|");
                            if (addressParts.length >= 4) {
                                tfStreet.setText(addressParts[0]);
                                tfCity.setText(addressParts[1]);
                                tfState.setSelectedItem(addressParts[2]);
                                tfPostalCode.setText(addressParts[3]);
                            }
                            break;
                        }
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Error loading supplier data: " + e.getMessage(),
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
                                         

    private void saveSupplier() {
        // Validate common fields
        if (tfSupplierName.getText().trim().isEmpty() || 
            tfContact.getText().trim().isEmpty() ||
            tfStreet.getText().trim().isEmpty() ||
            tfCity.getText().trim().isEmpty() ||
            tfPostalCode.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(this,
                "Please fill in all required fields",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (rbAddNewItem.isSelected()) {
            saveNewItem();
        } else {
            saveOriginalItem();
        }
    }
    
    private void saveOriginalItem() {
        // Validate item fields
        if (tfSuppliedItem.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter at least one supplied item",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        double itemPrice = ((Number) spItemPrice.getValue()).doubleValue();
        if (itemPrice <= 0) {
            JOptionPane.showMessageDialog(this,
                "Item price must be greater than 0",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Create Address object
            Address address = new Address(
                tfStreet.getText().trim(),
                tfCity.getText().trim(),
                tfState.getSelectedItem().toString(),
                tfPostalCode.getText().trim()
            );

            // Create Supplier object with parameters in correct order
            Supplier supplier = new Supplier(
                supplierId,
                tfSupplierName.getText().trim(), 
                tfSuppliedItem.getText().trim(), 
                itemPrice,                           
                tfContact.getText().trim(),             
                Integer.parseInt(jSpinner1.getValue().toString()), 
                address                                 
            );
            // Update the record
            FileUtils.updateRecordInFile(SupplierEntry.SUPPLIERS_FILE, supplierId, supplier.toCsvString());

            JOptionPane.showMessageDialog(this,
                "Supplier updated successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);

            this.dispose();
            new SupplierEntry().setVisible(true);
        } catch (IOException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Error saving supplier: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveNewItem() {
        // Validate new item fields
        if (tfNewItemName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter a new item name",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        double newItemPrice = ((Number) spNewItemPrice.getValue()).doubleValue();
        if (newItemPrice <= 0) {
            JOptionPane.showMessageDialog(this,
                "New item price must be greater than 0",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        String newItemName = tfNewItemName.getText().trim();

        try {
            // Check if item exists in inventory
            if (!FileUtils.itemExistsByName("src/database/items.txt", newItemName)) {
                int choice = JOptionPane.showConfirmDialog(this,
                    "Item '" + newItemName + "' doesn't exist in inventory.\n" +
                    "Would you like to add it now?",
                    "Add New Item",
                    JOptionPane.YES_NO_OPTION);

                if (choice == JOptionPane.YES_OPTION) {
                    openItemEntryScreen(newItemName);
                    return; // Exit saving process
                }
            }

            // Create Address object
            Address address = new Address(
                tfStreet.getText().trim(),
                tfCity.getText().trim(),
                tfState.getSelectedItem().toString(),
                tfPostalCode.getText().trim()
            );

            // Create Supplier object with parameters in correct order
            Supplier supplier = new Supplier(
                supplierId,
                tfSupplierName.getText().trim(),     
                newItemName,                        
                newItemPrice,                   
                tfContact.getText().trim(),   
                Integer.parseInt(jSpinner1.getValue().toString()), 
                address                          
            );

            // Add as a new record
            String savedId = FileUtils.addToFile(SupplierEntry.SUPPLIERS_FILE, supplier);

            if (savedId != null) {
                JOptionPane.showMessageDialog(this,
                    "New supplier item added successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

                this.dispose();
                new SupplierEntry().setVisible(true);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error saving new supplier item: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

   private void openItemEntryScreen(String itemName) {
       ItemEntry itemEntry = new ItemEntry();
       itemEntry.setItemName(itemName);
       itemEntry.setVisible(true);
       this.dispose();
   }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        tfState = new javax.swing.JComboBox<>();
        tfStreet = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        tfPostalCode = new javax.swing.JTextField();
        tfCity = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        tfSupplierName = new javax.swing.JTextField();
        tfSuppliedItem = new javax.swing.JTextField();
        jSpinner1 = new javax.swing.JSpinner();
        lblDay = new javax.swing.JLabel();
        tfContact = new javax.swing.JFormattedTextField();
        btnSave = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        spItemPrice = new javax.swing.JSpinner();
        rbAddNewItem = new javax.swing.JRadioButton();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        spNewItemPrice = new javax.swing.JSpinner();
        tfNewItemName = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 204));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("Edit Supplier");

        jLabel2.setText("Supplier Name:");

        jLabel3.setText("Supplied Item:");

        jLabel4.setText("Contact:");

        jLabel5.setText("Delivery Time:");

        jLabel10.setText("Postal Code:");

        tfState.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Johor", "Kedah", "Kelantan", "Malacca", "Negeri Sembilan", "Pahang", "Penang", "Perak", "Perlis", "Sabah", "Sarawak", "Terengganu", "Kuala Lumpur", "Putrajaya" }));

        tfStreet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfStreetActionPerformed(evt);
            }
        });

        jLabel6.setText("Address:");

        jLabel7.setText("Street:");

        jLabel8.setText("City");

        jLabel9.setText("State:");

        tfSupplierName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfSupplierNameActionPerformed(evt);
            }
        });

        lblDay.setText("Day(s)");

        btnSave.setText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        jLabel11.setText("Item Price:");

        rbAddNewItem.setText("Add New Item");
        rbAddNewItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbAddNewItemActionPerformed(evt);
            }
        });

        jLabel12.setText("New Item Name:");

        jLabel13.setText("New Item Price:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1)
                            .addComponent(jLabel12))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(rbAddNewItem)
                            .addComponent(tfSupplierName, javax.swing.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE)
                            .addComponent(tfSuppliedItem)
                            .addComponent(tfNewItemName)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel11)
                            .addComponent(jLabel4)
                            .addComponent(jLabel13))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(tfContact)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(spItemPrice, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jSpinner1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblDay, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(spNewItemPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tfPostalCode, javax.swing.GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE)
                    .addComponent(tfState, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfCity)
                    .addComponent(tfStreet, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(btnSave)
                        .addGap(18, 18, 18)
                        .addComponent(btnCancel)))
                .addGap(90, 90, 90))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(tfSupplierName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(tfSuppliedItem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(rbAddNewItem)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tfNewItemName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(spNewItemPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(tfContact, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblDay)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(spItemPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tfStreet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tfCity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(tfState, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tfPostalCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 123, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnSave)
                            .addComponent(btnCancel))
                        .addGap(22, 22, 22))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tfStreetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfStreetActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfStreetActionPerformed

    private void tfSupplierNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfSupplierNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfSupplierNameActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        saveSupplier();
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        this.dispose();
        new SupplierEntry().setVisible(true);
    }//GEN-LAST:event_btnCancelActionPerformed

    private void rbAddNewItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbAddNewItemActionPerformed
        boolean selected = rbAddNewItem.isSelected();
        tfNewItemName.setVisible(selected);
        spNewItemPrice.setVisible(selected);
        jLabel12.setVisible(selected);
        jLabel13.setVisible(selected);
        
        if (selected) {
            tfNewItemName.setText("");
            spNewItemPrice.setValue(0.0);
        }
    }//GEN-LAST:event_rbAddNewItemActionPerformed

 



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnSave;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JLabel lblDay;
    private javax.swing.JRadioButton rbAddNewItem;
    private javax.swing.JSpinner spItemPrice;
    private javax.swing.JSpinner spNewItemPrice;
    private javax.swing.JTextField tfCity;
    private javax.swing.JFormattedTextField tfContact;
    private javax.swing.JTextField tfNewItemName;
    private javax.swing.JTextField tfPostalCode;
    private javax.swing.JComboBox<String> tfState;
    private javax.swing.JTextField tfStreet;
    private javax.swing.JTextField tfSuppliedItem;
    private javax.swing.JTextField tfSupplierName;
    // End of variables declaration//GEN-END:variables
}
