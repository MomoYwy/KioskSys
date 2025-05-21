/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package PurchaseManager;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import shared.utils.PurchaseOrderUtils;
import shared.utils.FileUtils;
import shared.models.PurchaseOrder;

public class AddPurchaseOrderDialog extends javax.swing.JDialog {
    private boolean confirmed = false;
    private String prId;
    private String itemId;
    private String itemName;
    private int quantity;
    private String dateRequired;
    private String salesManagerId;
    private String purchaseManagerId;   
    
    // File paths
    private static final String SUPPLIERS_FILE = "src/database/suppliers.txt";
    private static final String PO_FILE = "src/database/purchase_orders.txt";
    private static final String PR_FILE = "src/database/purchase_requisition.txt";
    
    // Map to store supplier ID to price mapping
    private Map<String, Double> supplierPriceMap = new HashMap<>();
    private String currentSupplierId = "";
    /**
     * Creates new form AddPurchaseOrderDialog
     */
    public AddPurchaseOrderDialog(java.awt.Frame parent, boolean modal,
            String prId, String itemId, String itemName, int quantity, 
            String dateRequired, String salesManagerId, String purchaseManagerId) {
        super(parent, modal);
        initComponents();
        
        this.prId = prId;
        this.itemId = itemId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.dateRequired = dateRequired;
        this.salesManagerId = salesManagerId;
        this.purchaseManagerId = purchaseManagerId; 
        
        // Initialize the dialog with PR data
        initializePRData();
    }
    /**
     * Initialize the dialog with PR data
     */
    private void initializePRData() {
        // Set the PR data in the text fields
        jTextFieldPurchaseOrderId.setText(prId);
        jTextFieldItemID.setText(itemId);
        jTextFieldItemName.setText(itemName);
        jTextFieldquantity.setText(String.valueOf(quantity));
        jLabel.setText(dateRequired);
        jTextFieldsalesManagerId.setText(salesManagerId);
        
        // Initialize the supplier combo box
        jComboBoxSupplierID_Name.removeAllItems();
        jComboBoxSupplierID_Name.addItem("-- Choose --");
        
        // Load suppliers for the selected item
        loadSuppliers();
        
        // Set up action listener for supplier combo box
        jComboBoxSupplierID_Name.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supplierSelectionChanged();
            }
        });
        
        // Initialize default values
        Price.setText("------");
        
        // Default status is PENDING
        jRadioButtonPENDING.setSelected(true);
        
        // Initialize table with empty rows
        updateItemTable();
        
        // Set Save button initially disabled
        jButton1.setEnabled(false);
    }
    
    /**
     * Load suppliers for the selected item
     */
    private void loadSuppliers() {
        try {
            File file = new File(SUPPLIERS_FILE);
            if (!file.exists()) {
                JOptionPane.showMessageDialog(this, 
                        "Suppliers file not found",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                boolean firstLine = true;
                
                while ((line = reader.readLine()) != null) {
                    // Skip header line
                    if (firstLine) {
                        firstLine = false;
                        continue;
                    }
                    
                    // Skip empty lines
                    if (line.trim().isEmpty()) {
                        continue;
                    }
                    
                    String[] parts = line.split(",");
                    if (parts.length >= 4) {
                        String supplierId = parts[0].trim();
                        String supplierName = parts[1].trim();
                        String supplierItemName = parts[2].trim();
                        
                        // Check if this supplier supplies the selected item
                        if (supplierItemName.equalsIgnoreCase(itemName)) {
                            double price = Double.parseDouble(parts[3].trim());
                            
                            // Store price for this supplier
                            supplierPriceMap.put(supplierId, price);
                            
                            // Add to combo box
                            String displayText = supplierId + " - " + supplierName;
                            jComboBoxSupplierID_Name.addItem(displayText);
                        }
                    }
                }
            }
            
            // If no suppliers found
            if (jComboBoxSupplierID_Name.getItemCount() <= 1) {
                JOptionPane.showMessageDialog(this, 
                        "No suppliers found for item: " + itemName,
                        "Warning",
                        JOptionPane.WARNING_MESSAGE);
            }
            
        } catch (IOException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                    "Error loading suppliers: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Handle supplier selection changed
     */
    private void supplierSelectionChanged() {
        int selectedIndex = jComboBoxSupplierID_Name.getSelectedIndex();
        
        if (selectedIndex <= 0) {
            // No supplier selected or "Choose" option selected
            Price.setText("------");
            jButton1.setEnabled(false);
            currentSupplierId = "";
            updateItemTable();
            return;
        }
        
        String selectedItem = jComboBoxSupplierID_Name.getSelectedItem().toString();
        currentSupplierId = selectedItem.substring(0, selectedItem.indexOf(" -"));
        
        // Get price for this supplier
        double itemPrice = supplierPriceMap.getOrDefault(currentSupplierId, 0.0);
        
        // Update price label
        Price.setText(String.format("%.2f", itemPrice));
        
        // Update table
        updateItemTable();
        
        // Enable save button
        jButton1.setEnabled(true);
    }
    
    /**
     * Update the item table with current selection
     */
    private void updateItemTable() {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);
        
        if (!currentSupplierId.isEmpty()) {
            double itemPrice = supplierPriceMap.getOrDefault(currentSupplierId, 0.0);
            double totalPrice = itemPrice * quantity;
            
            model.addRow(new Object[]{
                itemId,
                itemName,
                quantity,
                itemPrice,
                totalPrice
            });
        }
    }
    
    /**
     * Generate new PO ID
     */
    private String generatePOId() {
        try {
            return PurchaseOrderUtils.generatePurchaseOrderId();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, 
                    "Error generating PO ID: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return "PO000"; // Fallback
        }
    }
    
    /**
     * Save the purchase order
     */
    private void savePurchaseOrder() {
        if (currentSupplierId.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                    "Please select a supplier",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            // Generate PO ID
            String poId = generatePOId();
            
            // Get price
            double itemPrice = supplierPriceMap.getOrDefault(currentSupplierId, 0.0);
            double totalPrice = itemPrice * quantity;
            
            // Get status
            String status = "PENDING";
            
            // Get current date
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String currentDate = sdf.format(new Date());
            
            // Create PO record
            String poRecord = String.format("%s,%s,%s,%s,%d,%.2f,%.2f,%s,%s,%s,%s,%s,%s",
                    poId, prId, itemId, itemName, quantity, itemPrice, totalPrice,
                    currentDate, dateRequired, currentSupplierId, salesManagerId, 
                    purchaseManagerId,status);
            
            // Ensure file exists
            File file = new File(PO_FILE);
            boolean isNewFile = !file.exists();
            
            if (isNewFile) {
                FileUtils.ensureFileExists(PO_FILE);
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                    writer.write("PO_ID,PR_ID,Item_ID,Item_Name,Quantity,Item_Price,Total_Price,Date_Created,Date_Required,Supplier_ID,Sales_Manager_ID,Purchase_Manager_ID,Status");
                    writer.newLine();
                }
            }
            
            // Write to file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
                writer.write(poRecord);
                writer.newLine();
            }
            
            // Update PR status
            updatePRStatus(prId, "Approved");
            
            JOptionPane.showMessageDialog(this, 
                    "Purchase Order " + poId + " created successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            
            confirmed = true;
            this.dispose();
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, 
                    "Error saving purchase order: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Update PR status
     */
    private void updatePRStatus(String prId, String newStatus) {
        try {
            PurchaseOrderUtils.updatePRStatus(prId, newStatus);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                    "Error updating PR status: " + e.getMessage(),
                    "Warning",
                    JOptionPane.WARNING_MESSAGE);
        }
    }
    
    /**
     * Check if the operation was confirmed
     */
    public boolean isConfirmed() {
        return confirmed;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        GeneratePurchaseOrder = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        PurchaseOrderID = new javax.swing.JLabel();
        jTextFieldPurchaseOrderId = new javax.swing.JTextField();
        ItemID1 = new javax.swing.JLabel();
        jTextFieldItemID = new javax.swing.JTextField();
        ItemName = new javax.swing.JLabel();
        jTextFieldItemName = new javax.swing.JTextField();
        jLabelquantity = new javax.swing.JLabel();
        jTextFieldquantity = new javax.swing.JTextField();
        PleaseSelectSupplier = new javax.swing.JLabel();
        jComboBoxSupplierID_Name = new javax.swing.JComboBox<>();
        jRadioButtonPENDING = new javax.swing.JRadioButton();
        jRadioButtonAPPROVED = new javax.swing.JRadioButton();
        jRadioButtonREJECTED = new javax.swing.JRadioButton();
        jRadioButtonRECEIVED_ITEMS = new javax.swing.JRadioButton();
        jLabelStatus = new javax.swing.JLabel();
        WholesalePrice = new javax.swing.JLabel();
        Price = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabelsmid = new javax.swing.JLabel();
        jTextFieldsalesManagerId = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(255, 255, 204));
        jPanel2.setLayout(new java.awt.BorderLayout());

        GeneratePurchaseOrder.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        GeneratePurchaseOrder.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        GeneratePurchaseOrder.setText("Generate Purchase Order");
        jPanel2.add(GeneratePurchaseOrder, java.awt.BorderLayout.CENTER);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        PurchaseOrderID.setBackground(new java.awt.Color(255, 255, 255));
        PurchaseOrderID.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        PurchaseOrderID.setText("Purchase Requisition ID :");

        jTextFieldPurchaseOrderId.setEditable(false);
        jTextFieldPurchaseOrderId.setBackground(new java.awt.Color(255, 255, 255));
        jTextFieldPurchaseOrderId.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jTextFieldPurchaseOrderId.setRequestFocusEnabled(false);
        jTextFieldPurchaseOrderId.setSelectionColor(new java.awt.Color(0, 102, 204));

        ItemID1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        ItemID1.setText("Item ID :");

        jTextFieldItemID.setEditable(false);
        jTextFieldItemID.setBackground(new java.awt.Color(255, 255, 255));
        jTextFieldItemID.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jTextFieldItemID.setRequestFocusEnabled(false);
        jTextFieldItemID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldItemIDActionPerformed(evt);
            }
        });

        ItemName.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        ItemName.setText("Item Name :");

        jTextFieldItemName.setEditable(false);
        jTextFieldItemName.setBackground(new java.awt.Color(255, 255, 255));
        jTextFieldItemName.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jTextFieldItemName.setRequestFocusEnabled(false);

        jLabelquantity.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabelquantity.setText("Quantity :");

        jTextFieldquantity.setEditable(false);
        jTextFieldquantity.setBackground(new java.awt.Color(255, 255, 255));
        jTextFieldquantity.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jTextFieldquantity.setRequestFocusEnabled(false);

        PleaseSelectSupplier.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        PleaseSelectSupplier.setText("Please Select Supplier :");

        jComboBoxSupplierID_Name.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jComboBoxSupplierID_Name.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBoxSupplierID_Name.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxSupplierID_NameActionPerformed(evt);
            }
        });

        buttonGroup1.add(jRadioButtonPENDING);
        jRadioButtonPENDING.setSelected(true);
        jRadioButtonPENDING.setText("PENDING");

        buttonGroup1.add(jRadioButtonAPPROVED);
        jRadioButtonAPPROVED.setText("APPROVED");
        jRadioButtonAPPROVED.setEnabled(false);
        jRadioButtonAPPROVED.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonAPPROVEDActionPerformed(evt);
            }
        });

        buttonGroup1.add(jRadioButtonREJECTED);
        jRadioButtonREJECTED.setText("REJECTED");
        jRadioButtonREJECTED.setEnabled(false);
        jRadioButtonREJECTED.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonREJECTEDActionPerformed(evt);
            }
        });

        buttonGroup1.add(jRadioButtonRECEIVED_ITEMS);
        jRadioButtonRECEIVED_ITEMS.setText("RECEIVED_ITEMS");
        jRadioButtonRECEIVED_ITEMS.setEnabled(false);

        jLabelStatus.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabelStatus.setText("Status :");

        WholesalePrice.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        WholesalePrice.setText("Wholesale Price (RM) :");

        Price.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        Price.setText("------");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("Date Required :");

        jLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel.setText("jLabel");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Item ID", "Item Name", "Quantity", "Item Price (per unit)", "Total Price (RM)"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Double.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setEnabled(false);
        jTable1.setGridColor(new java.awt.Color(255, 153, 102));
        jScrollPane1.setViewportView(jTable1);

        jButton1.setBackground(new java.awt.Color(0, 204, 102));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Save");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(255, 102, 102));
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Cancel");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabelsmid.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabelsmid.setText("Sales Manger ID :");

        jTextFieldsalesManagerId.setEditable(false);
        jTextFieldsalesManagerId.setBackground(new java.awt.Color(255, 255, 255));
        jTextFieldsalesManagerId.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jTextFieldsalesManagerId.setRequestFocusEnabled(false);
        jTextFieldsalesManagerId.setSelectionColor(new java.awt.Color(0, 102, 204));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(169, 169, 169)
                .addComponent(jButton1)
                .addGap(139, 139, 139)
                .addComponent(jButton2)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addGap(20, 20, 20))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                        .addComponent(PurchaseOrderID, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextFieldPurchaseOrderId, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel3Layout.createSequentialGroup()
                                                .addComponent(jLabelsmid, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                    .addComponent(jLabelquantity, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addComponent(ItemID1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addGap(6, 6, 6)))
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jTextFieldItemID)
                                            .addComponent(jTextFieldquantity)
                                            .addComponent(jTextFieldsalesManagerId, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(18, 18, 18))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabelStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(6, 6, 6)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jRadioButtonPENDING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jRadioButtonREJECTED, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jRadioButtonRECEIVED_ITEMS, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jRadioButtonAPPROVED, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(38, 38, 38)))
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(WholesalePrice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(ItemName, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(PleaseSelectSupplier, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextFieldItemName, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jComboBoxSupplierID_Name, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(Price, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(PurchaseOrderID)
                    .addComponent(jTextFieldPurchaseOrderId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabelsmid)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextFieldsalesManagerId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1)
                        .addComponent(jLabel)))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ItemID1)
                    .addComponent(jTextFieldItemID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ItemName)
                    .addComponent(jTextFieldItemName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(PleaseSelectSupplier)
                        .addComponent(jComboBoxSupplierID_Name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabelquantity)
                        .addComponent(jTextFieldquantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelStatus)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jRadioButtonPENDING)
                            .addComponent(WholesalePrice)
                            .addComponent(Price))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jRadioButtonAPPROVED)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jRadioButtonREJECTED)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jRadioButtonRECEIVED_ITEMS)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(83, 83, 83)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(200, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 602, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        savePurchaseOrder();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jRadioButtonAPPROVEDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonAPPROVEDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButtonAPPROVEDActionPerformed

    private void jRadioButtonREJECTEDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonREJECTEDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButtonREJECTEDActionPerformed

    private void jTextFieldItemIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldItemIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldItemIDActionPerformed

    private void jComboBoxSupplierID_NameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxSupplierID_NameActionPerformed
        supplierSelectionChanged();
    }//GEN-LAST:event_jComboBoxSupplierID_NameActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        confirmed = false;
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

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
            java.util.logging.Logger.getLogger(AddPurchaseOrderDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AddPurchaseOrderDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AddPurchaseOrderDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AddPurchaseOrderDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                // Create a sample dialog with dummy data for testing purposes
                AddPurchaseOrderDialog dialog = new AddPurchaseOrderDialog(
                        new javax.swing.JFrame(), 
                        true,
                        "PR001",       // Sample PR ID
                        "G001",        // Sample Item ID
                        "Bread",       // Sample Item Name
                        10,            // Sample Quantity
                        "15/06/2025",  // Sample Date Required
                        "S001",        // Sample Sales Manager ID
                        "PM336"        // Sample Purchase Manager ID (use a real PM ID for testing)
                );

                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel GeneratePurchaseOrder;
    private javax.swing.JLabel ItemID1;
    private javax.swing.JLabel ItemName;
    private javax.swing.JLabel PleaseSelectSupplier;
    private javax.swing.JLabel Price;
    private javax.swing.JLabel PurchaseOrderID;
    private javax.swing.JLabel WholesalePrice;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox<String> jComboBoxSupplierID_Name;
    private javax.swing.JLabel jLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabelStatus;
    private javax.swing.JLabel jLabelquantity;
    private javax.swing.JLabel jLabelsmid;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JRadioButton jRadioButtonAPPROVED;
    private javax.swing.JRadioButton jRadioButtonPENDING;
    private javax.swing.JRadioButton jRadioButtonRECEIVED_ITEMS;
    private javax.swing.JRadioButton jRadioButtonREJECTED;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextFieldItemID;
    private javax.swing.JTextField jTextFieldItemName;
    private javax.swing.JTextField jTextFieldPurchaseOrderId;
    private javax.swing.JTextField jTextFieldquantity;
    private javax.swing.JTextField jTextFieldsalesManagerId;
    // End of variables declaration//GEN-END:variables
}
