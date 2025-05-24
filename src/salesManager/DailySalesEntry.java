
    package salesManager;

    import java.io.IOException;
    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;
    import javax.swing.DefaultComboBoxModel;
    import javax.swing.JOptionPane;
    import javax.swing.table.DefaultTableModel;
    import shared.models.Customer;
    import shared.models.Item;
import shared.models.Recordable;
    import shared.models.SalesEntry;
    import shared.utils.FileUtils;
    import shared.utils.SwingUtils;

    public class DailySalesEntry extends javax.swing.JFrame {
        private List<String[]> itemsList;
        private static final String SALES_FILE = "src/database/sales_entry.txt";
        private static final String ITEMS_FILE = "src/database/items.txt";
        
        private String userId;
        private String username;

        public DailySalesEntry(String userId, String username) {
            initComponents();
            initializeDateComponents();
            loadItems();
            loadSalesToTable(); // Load existing sales entries when form opens
            ensureSalesFileExists();
            this.userId = userId;
            this.username = username;
        }

        private void initializeDateComponents() {
            SwingUtils.initializeDateComboBoxes(cbDay, cbMonth, cbYear);
            SwingUtils.initializeDateComboBoxes(cbDayRequired, cbMonthRequired, cbYearRequired);
        }

        private void loadItems() {
            itemsList = SwingUtils.loadItemsFromFile(ITEMS_FILE);

            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
            for (String[] item : itemsList) {
                model.addElement(item[0]); // Add item ID
            }
            cbItemID.setModel(model);

            if (!itemsList.isEmpty()) {
                updateItemName();
            }
        }

        private void updateItemName() {
            int selectedIndex = cbItemID.getSelectedIndex();
            if (selectedIndex >= 0 && selectedIndex < itemsList.size()) {
                String[] selectedItem = itemsList.get(selectedIndex);
                lbItemName.setText(selectedItem[1]); // Set item name
            }
        }

        private void loadSalesToTable() {
            DefaultTableModel model = (DefaultTableModel) jTable3.getModel();
            model.setRowCount(0); // Clear existing data

            try {
                FileUtils.TableUtils.loadSalesToTable(SALES_FILE, model);
            } catch (RuntimeException e) {
                JOptionPane.showMessageDialog(this, 
                    "Error loading sales data: " + e.getMessage(),
                    "Database Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
        
        // Add new Sales Entry
        private void addSalesEntry() {
            // Get input values
            String customerName = tfCustomerName.getText().trim();
            String customerContact = tfCustomerContact.getText().trim();
            String itemId = (String) cbItemID.getSelectedItem();
            String itemName = lbItemName.getText();

            // Safely get quantity
            int quantity;
            try {
                quantity = (Integer) spQuantity.getValue();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Invalid quantity value",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Check stock availability before proceeding
            if (!isStockAvailable(itemId, quantity)) {
                JOptionPane.showMessageDialog(this,
                    "Insufficient stock! Available quantity is less than requested.",
                    "Stock Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Rest of your existing addSalesEntry() code...
            String date = String.format("%s/%s/%s", 
                cbDay.getSelectedItem(), cbMonth.getSelectedItem(), cbYear.getSelectedItem());
            String dateRequired = String.format("%s/%s/%s", 
                cbDayRequired.getSelectedItem(), cbMonthRequired.getSelectedItem(), cbYearRequired.getSelectedItem());

            // Validate inputs
            if (customerName.isEmpty() || customerContact.isEmpty() || 
                itemId == null || itemId.trim().isEmpty() || 
                itemName == null || itemName.trim().isEmpty()) {

                JOptionPane.showMessageDialog(this,
                    "Please fill in all required fields",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                // Get item details from database
                String[] itemDetails = getItemDetails(itemId);
                if (itemDetails == null) {
                    JOptionPane.showMessageDialog(this,
                        "Item not found in database",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Create objects with proper Item constructor
                Customer customer = new Customer(customerName, customerContact);
                Item item = new Item(
                    itemId,
                    itemName,
                    itemDetails[2], // supplierId
                    Double.parseDouble(itemDetails[3]), // price
                    itemDetails[4] // category
                );

                // Generate sales ID
                String salesId = FileUtils.generateSalesId(SALES_FILE);

                // Create sales entry
                SalesEntry entry = new SalesEntry(
                    salesId,
                    date,
                    dateRequired,
                    customer,
                    item,
                    quantity
                );

                // Add to file
                String savedId = FileUtils.addToFile(SALES_FILE, entry);

                if (savedId != null) {
                    // Show success message with HTML formatting
                    showSuccessMessage("Sales Entry Added", 
                        "<html><b>Sales ID:</b> " + salesId + "<br>" +
                        "<b>Customer:</b> " + customerName + "<br>" +
                        "<b>Item:</b> " + itemName + " (" + itemId + ")<br>" +
                        "<b>Quantity:</b> " + quantity + "<br>" +
                        "<b>Date:</b> " + date + "<br>" +
                        "<b>Required By:</b> " + dateRequired + "</html>");

                    clearSalesForm();
                    loadSalesToTable();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Error creating sales entry: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }    
        
        private String[] getItemDetails(String itemId) throws IOException {
            List<String> stockLines = FileUtils.findLinesWithValue("src/database/stocklist.txt", itemId);
            if (stockLines != null && !stockLines.isEmpty()) {
                return stockLines.get(0).split(",");
            }

            List<String> itemLines = FileUtils.findLinesWithValue(ITEMS_FILE, itemId);
            if (itemLines != null && !itemLines.isEmpty()) {
                return itemLines.get(0).split(",");
            }

            return null;
        }
        
        private void showSuccessMessage(String title, String message) {
            JOptionPane.showMessageDialog(this, 
                message, 
                title, 
                JOptionPane.INFORMATION_MESSAGE);
        }
        
        private void editSelectedEntry() {
            int selectedRow = jTable3.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this,
                    "Please select a sales entry to edit",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                // Get the complete item details from database using the item ID
                String itemId = (String) jTable3.getValueAt(selectedRow, 5);
                String[] itemDetails = getItemDetails(itemId);

                if (itemDetails == null || itemDetails.length < 5) {
                    JOptionPane.showMessageDialog(this,
                        "Could not find complete item details in database",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Recreate Customer with complete information
                Customer customer = new Customer(
                    (String) jTable3.getValueAt(selectedRow, 3), // Customer Name
                    (String) jTable3.getValueAt(selectedRow, 4)  // Customer Contact
                );

                // Recreate Item with complete information from database
                Item item = new Item(
                    itemId,
                    itemDetails[1], // Item Name from database
                    itemDetails[2], // Supplier ID from database
                    Double.parseDouble(itemDetails[3]), // Price from database
                    itemDetails[4]  // Category from database
                );

                // Recreate SalesEntry with complete information
                SalesEntry entry = new SalesEntry(
                    (String) jTable3.getValueAt(selectedRow, 0), // Sales ID
                    (String) jTable3.getValueAt(selectedRow, 1), // Date
                    (String) jTable3.getValueAt(selectedRow, 2), // Date Required
                    customer,
                    item,
                    Integer.parseInt(jTable3.getValueAt(selectedRow, 7).toString()) // Quantity
                );

                // Open EditSales frame
                EditSales editFrame = new EditSales(entry);
                editFrame.setVisible(true);
                editFrame.setLocationRelativeTo(this);

                // Don't refresh immediately - wait for edit frame to close
                // loadSalesToTable() should be called when edit frame closes
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Error preparing edit: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }

        private void deleteSelectedEntry() {
            int selectedRow = jTable3.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this,
                    "Please select a sales entry to delete",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                String salesId = (String) jTable3.getValueAt(selectedRow, 0);

                int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this sales entry?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    FileUtils.deleteFromFileByField(SALES_FILE, 0, salesId);
                    JOptionPane.showMessageDialog(this,
                        "Sales entry deleted successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                    loadSalesToTable(); // Refresh table
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Error deleting sales entry: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }


        private void clearSalesForm() {
            tfCustomerName.setText("");
            tfCustomerContact.setText("");
            spQuantity.setValue(1);
            // Reset dates to current date?
        }
        
        private void searchAndDisplayItems() {
            String query = txtSearch.getText().trim();
            if (query.isEmpty()) {
                loadSalesToTable(); // Reload all data if search is empty
                return;
            }

            DefaultTableModel model = (DefaultTableModel) jTable3.getModel();
            model.setRowCount(0); // Clear existing data

            try {
                List<String> results = FileUtils.findLinesWithValue(SALES_FILE, query);
                for (String line : results) {
                    String[] parts = line.split(",");
                    if (parts.length >= 9) { // Ensure we have all required columns
                        model.addRow(new Object[]{
                            parts[0], // Sales ID
                            parts[1], // Date
                            parts[2], // Date Required
                            parts[3], // Customer Name
                            parts[4], // Customer Contact
                            parts[5], // Item ID
                            parts[6], // Item Name
                            Integer.parseInt(parts[7]), // Quantity
                            parts[8]  // Total
                        });
                    }
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Error searching sales data: " + e.getMessage(),
                    "Search Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }

        private boolean isStockAvailable(String itemId, int requiredQuantity) {
            try {
                // Read from stocklist.txt instead of items.txt
                List<String> lines = FileUtils.findLinesWithValue("src/database/stocklist.txt", itemId);
                if (lines == null || lines.isEmpty()) {
                    JOptionPane.showMessageDialog(this, 
                        "Item not found in stock database",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    return false;
                }

                // Assuming format: ItemID,ItemName,Category,Stock,Status
                String[] stockDetails = lines.get(0).split(",");
                if (stockDetails.length < 4) {
                    JOptionPane.showMessageDialog(this, 
                        "Invalid stock data format",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    return false;
                }

                // Stock is stored as negative number (4th field, index 3)
                int availableStock = Math.abs(Integer.parseInt(stockDetails[3].trim()));

                if (availableStock < requiredQuantity) {
                    JOptionPane.showMessageDialog(this,
                        String.format("Insufficient stock! Available: %d, Requested: %d", 
                            availableStock, requiredQuantity),
                        "Stock Error",
                        JOptionPane.WARNING_MESSAGE);
                    return false;
                }

                return true;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                    "Error parsing stock quantity: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return false;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Error checking stock: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        
        private void ensureSalesFileExists() {
            if (!FileUtils.ensureFileExists(SALES_FILE)) {
                JOptionPane.showMessageDialog(this,
                    "Failed to initialize sales database file",
                    "File Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }

    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        cbDay = new javax.swing.JComboBox<>();
        cbMonth = new javax.swing.JComboBox<>();
        cbYear = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        cbDayRequired = new javax.swing.JComboBox<>();
        cbMonthRequired = new javax.swing.JComboBox<>();
        cbYearRequired = new javax.swing.JComboBox<>();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        tfCustomerName = new javax.swing.JTextField();
        tfCustomerContact = new javax.swing.JTextField();
        cbItemID = new javax.swing.JComboBox<>();
        lbItemName = new javax.swing.JLabel();
        spQuantity = new javax.swing.JSpinner();
        btnAdd = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        btnSearch = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtSearch = new javax.swing.JTextPane();
        btnBack = new javax.swing.JButton();

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

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(jTable2);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 204));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("Daily Item-wise Sales Entry");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(423, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(35, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(24, 24, 24))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setText("Date:");

        jLabel3.setText("Customer_Name:");

        jLabel4.setText("ItemID:");

        jLabel7.setText("Item_Name:");

        jLabel8.setText("Date_Required:");

        jLabel9.setText("Quantity:");

        jLabel5.setText("Customer_ContactNo:");

        cbDay.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cbMonth.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" }));

        cbYear.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "2025", "2026", "2027", "2028", "2029" }));

        jLabel6.setText("Day");

        jLabel10.setText("Month");

        jLabel11.setText("Year");

        jLabel12.setText("Year");

        cbDayRequired.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cbMonthRequired.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" }));

        cbYearRequired.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "2025", "2026", "2027", "2028", "2029" }));

        jLabel13.setText("Day");

        jLabel14.setText("Month");

        cbItemID.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbItemID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbItemIDActionPerformed(evt);
            }
        });

        lbItemName.setText("Item Name");

        btnAdd.setText("Add");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnDelete.setText("Delete");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnEdit.setText("Edit");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "SalesID", "Date", "Date_Required", "Customer_Name", "Customer_Contact", "ItemID", "Item_Name", "Quantity", "Status"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane3.setViewportView(jTable3);

        btnSearch.setText("Search");
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        jScrollPane4.setViewportView(txtSearch);

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
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(80, 80, 80)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(spQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cbItemID, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING)))
                                        .addGap(33, 33, 33))
                                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addComponent(lbItemName))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(tfCustomerName, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(tfCustomerContact, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(cbDayRequired, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel13)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(cbMonthRequired, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel14)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(cbYearRequired, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel12))
                                            .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(cbDay, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel6)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(cbMonth, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel10)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(cbYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel11)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 146, Short.MAX_VALUE)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(btnAdd)
                                            .addComponent(btnEdit)
                                            .addComponent(btnDelete)
                                            .addComponent(btnBack))
                                        .addGap(34, 34, 34)))))
                        .addGap(29, 29, 29))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 570, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnSearch))
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 648, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel10)
                                .addComponent(cbYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel11))
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(cbDay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel2))
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel6)
                                .addComponent(cbMonth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbDayRequired, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbMonthRequired, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbYearRequired, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13)
                            .addComponent(jLabel14)
                            .addComponent(jLabel12)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(tfCustomerName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tfCustomerContact, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btnAdd)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDelete)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEdit)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBack)
                        .addGap(19, 19, 19)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(cbItemID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(lbItemName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(spQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearch))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(56, Short.MAX_VALUE))
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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cbItemIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbItemIDActionPerformed
        updateItemName();
    }//GEN-LAST:event_cbItemIDActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        addSalesEntry();
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        deleteSelectedEntry();
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        editSelectedEntry();
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        searchAndDisplayItems();
    }//GEN-LAST:event_btnSearchActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        SwingUtils.handleBackButton(this, userId, username);
    }//GEN-LAST:event_btnBackActionPerformed

    
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
            java.util.logging.Logger.getLogger(DailySalesEntry.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DailySalesEntry.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DailySalesEntry.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DailySalesEntry.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnSearch;
    private javax.swing.JComboBox<String> cbDay;
    private javax.swing.JComboBox<String> cbDayRequired;
    private javax.swing.JComboBox<String> cbItemID;
    private javax.swing.JComboBox<String> cbMonth;
    private javax.swing.JComboBox<String> cbMonthRequired;
    private javax.swing.JComboBox<String> cbYear;
    private javax.swing.JComboBox<String> cbYearRequired;
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
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JLabel lbItemName;
    private javax.swing.JSpinner spQuantity;
    private javax.swing.JTextField tfCustomerContact;
    private javax.swing.JTextField tfCustomerName;
    private javax.swing.JTextPane txtSearch;
    // End of variables declaration//GEN-END:variables
}
