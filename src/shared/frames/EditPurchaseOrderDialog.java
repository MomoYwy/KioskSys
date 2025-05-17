package shared.frames;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import shared.utils.PurchaseOrderUtils;
import shared.utils.FileUtils;
import shared.models.PurchaseOrder;

public class EditPurchaseOrderDialog extends javax.swing.JDialog {

    
    private static final String PO_FILE = "src/database/purchase_orders.txt";
    private static final String SUPPLIERS_FILE = "src/database/suppliers.txt";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    
    private String purchaseOrderId;
    private String purchaseRequisitionId;
    private String itemId;
    private String itemName;
    private int quantity;
    private double itemPrice;
    private double totalPrice;
    private Date dateCreated;
    private Date dateRequired;
    private String originalSupplierId;
    private String supplierId;
    private String salesManagerId;
    private String purchaseManagerId;
    private String status;
    
    // Map to store supplier ID to price mapping
    private Map<String, Double> supplierPriceMap = new HashMap<>();
    private boolean confirmed = false;
    
    public EditPurchaseOrderDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        // Add document listener to quantity field to update the table in real-time
        jTextFieldquantity.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                updateTableOnQuantityChange();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                updateTableOnQuantityChange();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                updateTableOnQuantityChange();
            }
        });
    }

    public void loadPurchaseOrderData(String poId) {
        try {
            File file = new File(PO_FILE);
            if (!file.exists()) {
                JOptionPane.showMessageDialog(this, 
                        "Purchase Orders file not found",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            boolean found = false;
            
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
                    if (parts.length >= 13 && parts[0].trim().equals(poId)) {
                        // Found the purchase order
                        purchaseOrderId = parts[0].trim();
                        purchaseRequisitionId = parts[1].trim();
                        itemId = parts[2].trim();
                        itemName = parts[3].trim();
                        quantity = Integer.parseInt(parts[4].trim());
                        itemPrice = Double.parseDouble(parts[5].trim());
                        totalPrice = Double.parseDouble(parts[6].trim());
                        dateCreated = DATE_FORMAT.parse(parts[7].trim());
                        dateRequired = DATE_FORMAT.parse(parts[8].trim());
                        supplierId = parts[9].trim();
                        salesManagerId = parts[10].trim();
                        purchaseManagerId = parts[11].trim();
                        status = parts[12].trim();
                        originalSupplierId = supplierId;
                        
                        // Set values in the form
                        jLabelPurchaseOrderId.setText(purchaseOrderId);
                        jLabelPurchaseRequisitionId.setText(purchaseRequisitionId);
                        jLabelDateCreated.setText(DATE_FORMAT.format(dateCreated));
                        jLabel.setText(DATE_FORMAT.format(dateRequired));
                        jLabelPurchaseManagerId.setText(purchaseManagerId);
                        jLabelsalesManagerId.setText(salesManagerId);
                        
                        jLabelItemID.setText(itemId);
                        jLabelItemName.setText(itemName);
                        jTextFieldquantity.setText(String.valueOf(quantity));
                        
                        // Set radio button based on status
                        switch (status) {
                            case "PENDING":
                                jRadioButtonPENDING.setSelected(true);
                                break;
                            case "APPROVED":
                                jRadioButtonAPPROVED.setSelected(true);
                                break;
                            case "REJECTED":
                                jRadioButtonREJECTED.setSelected(true);
                                break;
                            case "RECEIVED_ITEMS":
                                jRadioButtonRECEIVED_ITEMS.setSelected(true);
                                break;
                        }
                        
                        // Load suppliers for the item
                        loadSuppliersForItem();
                        
                        // Select supplier in combo box
                        selectSupplierInComboBox(supplierId);
                        
                        // Update price display
                        Price.setText(String.format("%.2f", itemPrice));
                        
                        // Update table
                        updateItemTable();
                        
                        found = true;
                        break;
                    }
                }
            }
            
            if (!found) {
                JOptionPane.showMessageDialog(this, 
                        "Purchase Order not found: " + poId,
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                this.dispose();
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                    "Error loading purchase order: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            this.dispose();
        }
    }
    
    private void loadSuppliersForItem() {
        jComboBoxSupplierID_Name.removeAllItems();
        supplierPriceMap.clear();
        
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
                
                while ((line = reader.readLine()) != null) {
                    // Skip empty lines
                    if (line.trim().isEmpty()) {
                        continue;
                    }
                    
                    String[] parts = line.split(",");
                    if (parts.length >= 4) {
                        String supplierId = parts[0].trim();
                        String supplierName = parts[1].trim();
                        String supplierItemName = parts[2].trim();
                        
                        // Check if this supplier supplies the current item
                        if (itemName != null && supplierItemName.equalsIgnoreCase(itemName)) {
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
            
        } catch (IOException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                    "Error loading suppliers: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Select the supplier in the combo box
     */
    private void selectSupplierInComboBox(String supplierId) {
        for (int i = 0; i < jComboBoxSupplierID_Name.getItemCount(); i++) {
            String item = jComboBoxSupplierID_Name.getItemAt(i);
            if (item.startsWith(supplierId + " ")) {
                jComboBoxSupplierID_Name.setSelectedIndex(i);
                return;
            }
        }
    }
    
    /**
     * Update the item table with current data
     */
    private void updateItemTable() {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);
 
        model.addRow(new Object[]{
            itemId,
            itemName,
            quantity,
            itemPrice,
            totalPrice
        });
    }
    
    /**
     * Handle supplier selection changed event
     */
    private void supplierSelectionChanged() {
        int selectedIndex = jComboBoxSupplierID_Name.getSelectedIndex();
        
        if (selectedIndex < 0) {
            return;
        }
        
        String selectedItem = jComboBoxSupplierID_Name.getSelectedItem().toString();
        String selectedSupplierId = selectedItem.substring(0, selectedItem.indexOf(" -"));
        
        // Update supplier ID
        supplierId = selectedSupplierId;
        
        // Get price for this supplier
        double newItemPrice = supplierPriceMap.getOrDefault(supplierId, 0.0);
        
        // Update price
        itemPrice = newItemPrice;
        totalPrice = quantity * itemPrice;
        
        // Update price label
        Price.setText(String.format("%.2f", itemPrice));
        
        // Update table
        updateItemTable();
    }
    
    private void updateTableOnQuantityChange() {
        try {
            // Try to parse the quantity from the text field
            int newQuantity = Integer.parseInt(jTextFieldquantity.getText());

            // Temporarily update quantity for table display
            int oldQuantity = quantity;
            quantity = newQuantity;

            // Update total price for display
            double tempTotalPrice = quantity * itemPrice;

            // Update the table
            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.setRowCount(0);
            model.addRow(new Object[]{
                itemId,
                itemName,
                quantity,
                itemPrice,
                tempTotalPrice
            });

            // Restore original quantity (it will be officially updated on save)
            quantity = oldQuantity;
        } catch (NumberFormatException e) {
            // If input is not a valid number, don't update table
        }
    }    
    
    /**
     * Save the updated purchase order
     */
    private void savePurchaseOrder() {
        try {
            // Get updated values
            int newQuantity = Integer.parseInt(jTextFieldquantity.getText());
            String newStatus;
            
            if (jRadioButtonPENDING.isSelected()) {
                newStatus = "PENDING";
            } else if (jRadioButtonAPPROVED.isSelected()) {
                newStatus = "APPROVED";
            } else if (jRadioButtonREJECTED.isSelected()) {
                newStatus = "REJECTED";
            } else if (jRadioButtonRECEIVED_ITEMS.isSelected()) {
                newStatus = "RECEIVED_ITEMS";
            } else {
                newStatus = status; // Default to current
            }
            
            // Check if anything has changed
            boolean supplierChanged = !supplierId.equals(originalSupplierId);
            boolean quantityChanged = newQuantity != quantity;
            boolean statusChanged = !newStatus.equals(status);
            
            if (!supplierChanged && !quantityChanged && !statusChanged) {
                JOptionPane.showMessageDialog(this, 
                        "No changes made to Purchase Order",
                        "Information",
                        JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
                return;
            }
            
            // Update values
            quantity = newQuantity;
            status = newStatus;
            totalPrice = quantity * itemPrice;
            
            // Create updated PO record
            String updatedRecord = String.format("%s,%s,%s,%s,%d,%.2f,%.2f,%s,%s,%s,%s,%s,%s",
                    purchaseOrderId, purchaseRequisitionId, itemId, itemName, quantity, 
                    itemPrice, totalPrice, DATE_FORMAT.format(dateCreated), 
                    DATE_FORMAT.format(dateRequired), supplierId, salesManagerId, 
                    purchaseManagerId, status);
            
            // Update the file
            updatePurchaseOrderInFile(purchaseOrderId, updatedRecord);
            
            JOptionPane.showMessageDialog(this, 
                    "Purchase Order updated successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            
            confirmed = true;
            this.dispose();
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                    "Please enter a valid quantity",
                    "Invalid Input",
                    JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                    "Error saving purchase order: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Update a purchase order record in the file
     */
    private void updatePurchaseOrderInFile(String poId, String updatedRecord) throws IOException {
        File originalFile = new File(PO_FILE);
        File tempFile = new File(PO_FILE + ".tmp");
        
        try (BufferedReader reader = new BufferedReader(new FileReader(originalFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            
            String line;
            boolean headerWritten = false;
            
            while ((line = reader.readLine()) != null) {
                // Write header as-is
                if (!headerWritten) {
                    writer.write(line);
                    writer.newLine();
                    headerWritten = true;
                    continue;
                }
                
                // Skip empty lines
                if (line.trim().isEmpty()) {
                    writer.write(line);
                    writer.newLine();
                    continue;
                }
                
                String[] parts = line.split(",");
                if (parts.length > 0 && parts[0].trim().equals(poId)) {
                    // Replace with updated record
                    writer.write(updatedRecord);
                } else {
                    // Keep original record
                    writer.write(line);
                }
                writer.newLine();
            }
        }
        
        // Replace original file with updated file
        if (!originalFile.delete()) {
            throw new IOException("Could not delete original file");
        }
        if (!tempFile.renameTo(originalFile)) {
            throw new IOException("Could not rename temp file");
        }
    }

    /**
     * Check if the operation was confirmed
     */
    public boolean isConfirmed() {
        return confirmed;
    }
    
    public void setPurchaseOrderId(String poId) {
        this.purchaseOrderId = poId;
        jLabelPurchaseOrderId.setText(poId);
    }
    
    public void setPurchaseRequisitionId(String prId) {
        this.purchaseRequisitionId = prId;
        jLabelPurchaseRequisitionId.setText(prId);
    }
    
    public void setItemID(String itemId) {
        this.itemId = itemId;
        jLabelItemID.setText(itemId);
    }
    
    public void setItemName(String itemName) {
        this.itemName = itemName;
        jLabelItemName.setText(itemName);
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
        jTextFieldquantity.setText(String.valueOf(quantity));
    }
    
    public void setDateCreated(String dateCreated) {
        try {
            this.dateCreated = DATE_FORMAT.parse(dateCreated);
            jLabelDateCreated.setText(dateCreated);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                    "Error parsing date: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void setDateRequired(String dateRequired) {
        try {
            this.dateRequired = DATE_FORMAT.parse(dateRequired);
            jLabel.setText(dateRequired);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                    "Error parsing date: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void setPurchaseManagerId(String purchaseManagerId) {
        this.purchaseManagerId = purchaseManagerId;
        jLabelPurchaseManagerId.setText(purchaseManagerId);
    }
    
    public void setSalesManagerId(String salesManagerId) {
        this.salesManagerId = salesManagerId;
        jLabelsalesManagerId.setText(salesManagerId);
    }
    
    public void loadSupplierForItem(String itemName, String supplierId) {
        this.itemName = itemName;
        this.supplierId = supplierId;
        
        // Load all suppliers for this item
        loadSuppliersForItem();
        
        // Select the specified supplier
        selectSupplierInComboBox(supplierId);
    }
    
    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
        this.totalPrice = quantity * itemPrice;
        Price.setText(String.format("%.2f", itemPrice));
    }
    
    public void setStatus(String status) {
        this.status = status;
        
        // Update radio buttons
        switch (status) {
            case "PENDING":
                jRadioButtonPENDING.setSelected(true);
                break;
            case "APPROVED":
                jRadioButtonAPPROVED.setSelected(true);
                break;
            case "REJECTED":
                jRadioButtonREJECTED.setSelected(true);
                break;
            case "RECEIVED_ITEMS":
                jRadioButtonRECEIVED_ITEMS.setSelected(true);
                break;
        }
    }
    
    public void updateOrderTable(String itemId, String itemName, int quantity, double itemPrice) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.itemPrice = itemPrice;
        this.totalPrice = quantity * itemPrice;
        
        updateItemTable();
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
        PurchaseRequisitionID = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabelsmid = new javax.swing.JLabel();
        PurchaseOrderID = new javax.swing.JLabel();
        jLabelPOid = new javax.swing.JLabel();
        jLabelDateCreatedPO = new javax.swing.JLabel();
        jLabelDateCreated = new javax.swing.JLabel();
        jLabelPurchaseOrderId = new javax.swing.JLabel();
        jLabelPurchaseManagerId = new javax.swing.JLabel();
        jLabelPurchaseRequisitionId = new javax.swing.JLabel();
        jLabelsalesManagerId = new javax.swing.JLabel();
        itemIformationPanel = new javax.swing.JPanel();
        ItemID1 = new javax.swing.JLabel();
        jTextFieldquantity = new javax.swing.JTextField();
        jLabelquantity = new javax.swing.JLabel();
        jLabelStatus = new javax.swing.JLabel();
        jRadioButtonPENDING = new javax.swing.JRadioButton();
        jRadioButtonAPPROVED = new javax.swing.JRadioButton();
        jRadioButtonREJECTED = new javax.swing.JRadioButton();
        jRadioButtonRECEIVED_ITEMS = new javax.swing.JRadioButton();
        WholesalePrice = new javax.swing.JLabel();
        Price = new javax.swing.JLabel();
        jComboBoxSupplierID_Name = new javax.swing.JComboBox<>();
        PleaseSelectSupplier = new javax.swing.JLabel();
        ItemName = new javax.swing.JLabel();
        jLabelItemID = new javax.swing.JLabel();
        jLabelItemName = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(255, 255, 204));
        jPanel2.setLayout(new java.awt.BorderLayout());

        GeneratePurchaseOrder.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        GeneratePurchaseOrder.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        GeneratePurchaseOrder.setText("Edit Purchase Order");
        jPanel2.add(GeneratePurchaseOrder, java.awt.BorderLayout.CENTER);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        PurchaseRequisitionID.setBackground(new java.awt.Color(255, 255, 255));
        PurchaseRequisitionID.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        PurchaseRequisitionID.setText("Purchase Requisition ID :");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("Date Required :");

        jLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
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

        PurchaseOrderID.setBackground(new java.awt.Color(255, 255, 255));
        PurchaseOrderID.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        PurchaseOrderID.setText("Purchase Order ID :");

        jLabelPOid.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabelPOid.setText("Purchase Manager ID :");

        jLabelDateCreatedPO.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabelDateCreatedPO.setText("Date Created PO  :");

        jLabelDateCreated.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelDateCreated.setText("jLabel");

        jLabelPurchaseOrderId.setText("PO ----");

        jLabelPurchaseManagerId.setText("PM ----");

        jLabelPurchaseRequisitionId.setText("PR ----");

        jLabelsalesManagerId.setText("SM ----");

        itemIformationPanel.setBackground(new java.awt.Color(255, 255, 255));
        itemIformationPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Item Information", javax.swing.border.TitledBorder.LEADING, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(102, 102, 102))); // NOI18N

        ItemID1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        ItemID1.setText("Item ID :");

        jTextFieldquantity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldquantityActionPerformed(evt);
            }
        });

        jLabelquantity.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabelquantity.setText("Quantity :");

        jLabelStatus.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabelStatus.setText("Status :");

        buttonGroup1.add(jRadioButtonPENDING);
        jRadioButtonPENDING.setSelected(true);
        jRadioButtonPENDING.setText("PENDING");

        buttonGroup1.add(jRadioButtonAPPROVED);
        jRadioButtonAPPROVED.setText("APPROVED");
        jRadioButtonAPPROVED.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonAPPROVEDActionPerformed(evt);
            }
        });

        buttonGroup1.add(jRadioButtonREJECTED);
        jRadioButtonREJECTED.setText("REJECTED");
        jRadioButtonREJECTED.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonREJECTEDActionPerformed(evt);
            }
        });

        buttonGroup1.add(jRadioButtonRECEIVED_ITEMS);
        jRadioButtonRECEIVED_ITEMS.setText("RECEIVED_ITEMS");
        jRadioButtonRECEIVED_ITEMS.setEnabled(false);

        WholesalePrice.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        WholesalePrice.setText("Wholesale Price (RM) :");

        Price.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        Price.setText("------");

        jComboBoxSupplierID_Name.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBoxSupplierID_Name.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxSupplierID_NameActionPerformed(evt);
            }
        });

        PleaseSelectSupplier.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        PleaseSelectSupplier.setText("Please Select Supplier :");

        ItemName.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        ItemName.setText("Item Name :");

        jLabelItemID.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabelItemID.setText("G ----");

        jLabelItemName.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabelItemName.setText("--------");

        javax.swing.GroupLayout itemIformationPanelLayout = new javax.swing.GroupLayout(itemIformationPanel);
        itemIformationPanel.setLayout(itemIformationPanelLayout);
        itemIformationPanelLayout.setHorizontalGroup(
            itemIformationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(itemIformationPanelLayout.createSequentialGroup()
                .addGap(109, 109, 109)
                .addGroup(itemIformationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jRadioButtonAPPROVED, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
                    .addComponent(jRadioButtonRECEIVED_ITEMS, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
                    .addComponent(jRadioButtonREJECTED, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jRadioButtonPENDING, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabelItemID, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 178, Short.MAX_VALUE)
                .addComponent(jLabelItemName, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(itemIformationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(itemIformationPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(itemIformationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(ItemID1, javax.swing.GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE)
                        .addComponent(jLabelquantity, javax.swing.GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE)
                        .addComponent(jLabelStatus, javax.swing.GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jTextFieldquantity, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addGroup(itemIformationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, itemIformationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(ItemName, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, itemIformationPanelLayout.createSequentialGroup()
                                .addComponent(PleaseSelectSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBoxSupplierID_Name, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, itemIformationPanelLayout.createSequentialGroup()
                            .addComponent(WholesalePrice, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(Price, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        itemIformationPanelLayout.setVerticalGroup(
            itemIformationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, itemIformationPanelLayout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addGroup(itemIformationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelItemID)
                    .addComponent(jLabelItemName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 70, Short.MAX_VALUE)
                .addComponent(jRadioButtonPENDING)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButtonAPPROVED)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButtonREJECTED)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButtonRECEIVED_ITEMS)
                .addGap(23, 23, 23))
            .addGroup(itemIformationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(itemIformationPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(itemIformationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(ItemID1)
                        .addComponent(ItemName))
                    .addGap(24, 24, 24)
                    .addGroup(itemIformationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabelquantity)
                        .addComponent(jTextFieldquantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(PleaseSelectSupplier)
                        .addComponent(jComboBoxSupplierID_Name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(18, 18, 18)
                    .addGroup(itemIformationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabelStatus)
                        .addComponent(WholesalePrice)
                        .addComponent(Price))
                    .addContainerGap(106, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(PurchaseOrderID, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(PurchaseRequisitionID, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabelDateCreatedPO, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabelDateCreated, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabelPurchaseOrderId, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelPurchaseRequisitionId, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabelPOid, javax.swing.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE)
                            .addComponent(jLabelsmid, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabelPurchaseManagerId, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabelsalesManagerId, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(itemIformationPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(182, 182, 182)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(75, 75, 75)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 585, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(PurchaseOrderID)
                    .addComponent(jLabelPOid)
                    .addComponent(jLabelPurchaseOrderId)
                    .addComponent(jLabelPurchaseManagerId))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(PurchaseRequisitionID)
                    .addComponent(jLabelsmid)
                    .addComponent(jLabelPurchaseRequisitionId)
                    .addComponent(jLabelsalesManagerId))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel)
                    .addComponent(jLabelDateCreatedPO)
                    .addComponent(jLabelDateCreated))
                .addGap(18, 18, 18)
                .addComponent(itemIformationPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(25, Short.MAX_VALUE))
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
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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

    private void jComboBoxSupplierID_NameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxSupplierID_NameActionPerformed
        supplierSelectionChanged();
    }//GEN-LAST:event_jComboBoxSupplierID_NameActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        confirmed = false;
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTextFieldquantityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldquantityActionPerformed

    }//GEN-LAST:event_jTextFieldquantityActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */

        //</editor-fold>
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(EditPurchaseOrderDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EditPurchaseOrderDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EditPurchaseOrderDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EditPurchaseOrderDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                EditPurchaseOrderDialog dialog = new EditPurchaseOrderDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                
                // For testing, load a sample PO
                try {
                    dialog.loadPurchaseOrderData("PO001");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
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
    private javax.swing.JLabel PurchaseRequisitionID;
    private javax.swing.JLabel WholesalePrice;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JPanel itemIformationPanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox<String> jComboBoxSupplierID_Name;
    private javax.swing.JLabel jLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabelDateCreated;
    private javax.swing.JLabel jLabelDateCreatedPO;
    private javax.swing.JLabel jLabelItemID;
    private javax.swing.JLabel jLabelItemName;
    private javax.swing.JLabel jLabelPOid;
    private javax.swing.JLabel jLabelPurchaseManagerId;
    private javax.swing.JLabel jLabelPurchaseOrderId;
    private javax.swing.JLabel jLabelPurchaseRequisitionId;
    private javax.swing.JLabel jLabelStatus;
    private javax.swing.JLabel jLabelquantity;
    private javax.swing.JLabel jLabelsalesManagerId;
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
    private javax.swing.JTextField jTextFieldquantity;
    // End of variables declaration//GEN-END:variables
}