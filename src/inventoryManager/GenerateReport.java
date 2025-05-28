package inventoryManager;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.*;
import java.util.*;
import java.util.stream.Collectors;

public class GenerateReport extends javax.swing.JFrame {
    
    private DefaultTableModel originalPOTableModel;
    private DefaultTableModel originalSETableModel;
    private JFrame parentWindow;


    public GenerateReport() {
        initComponents();
        loadPurchaseOrderToTable("src/database/purchase_orders.txt");
        loadComboBoxFromPOTable();
        loadProcessedSalesToTable("src/database/sales_entry.txt");
        loadComboBoxFromSETable();
        loadStockListToTable("src/database/stocklist.txt");
        
    }
    
    public GenerateReport(JFrame parentWindow) {
    this(); 
    this.parentWindow = parentWindow;
}
    
    private void loadPurchaseOrderToTable(String filePath) {
    String[] columnNames = {
        "PO_ID", "PR_ID", "Item_ID", "Item_Name", "Quantity",
        "Item_Price", "Total_Price", "Date_Created", "Date_Required",
        "Supplier_ID", "Sales_Manager_ID", "Purchase_Manager_ID", "Status"
    };

    DefaultTableModel model = new DefaultTableModel(columnNames, 0);

    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
        String line;

        while ((line = br.readLine()) != null) {
            String[] data = line.split(",");

            if (data.length < columnNames.length) continue;

            String status = data[12].trim(); 
            if (!status.equalsIgnoreCase("RECEIVED_ITEMS")) continue;

            Vector<String> row = new Vector<>();
            for (String field : data) {
                row.add(field.trim());
            }

            model.addRow(row);
        }

        POTable.setModel(model);

    } catch (IOException e) {
        e.printStackTrace();
    }
    
    POTable.setModel(model);
    originalPOTableModel = (DefaultTableModel) model; 
    
}
    
    private void loadProcessedSalesToTable(String filePath) {
    String[] columnNames = {
        "Sales_ID", "Date", "Date_Required", "Customer_Name",
        "Customer_Contact", "Item_Id", "Item_Name", "Quantity", "Status"
    };

    DefaultTableModel model = new DefaultTableModel(columnNames, 0);

    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
        String line;

        while ((line = br.readLine()) != null) {
            String[] data = line.split(",");

            if (data.length < columnNames.length || data[0].equalsIgnoreCase("Sales_ID")) continue;

            String status = data[8].trim(); 
            if (!status.equalsIgnoreCase("PROCESSED")) continue;

            Vector<String> row = new Vector<>();
            for (String field : data) {
                row.add(field.trim());
            }

            model.addRow(row);
        }

        SETable.setModel(model);

    } catch (IOException e) {
        e.printStackTrace();
    }
    originalSETableModel = (DefaultTableModel) SETable.getModel();
}
    
    private void loadStockListToTable(String filePath) {
    String[] columnNames = {"Item ID", "Item Name", "Category", "Quantity", "Status"};
    DefaultTableModel model = new DefaultTableModel(columnNames, 0);

    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
        String line;
        boolean firstLine = true;
        while ((line = br.readLine()) != null) {
            if (firstLine) { 
                firstLine = false;
                continue;
            }
            String[] data = line.split(",");
            if (data.length < columnNames.length) continue;

            Vector<String> row = new Vector<>();
            for (String field : data) {
                row.add(field.trim());
            }
            model.addRow(row);
        }
        StockTable.setModel(model);  
    } catch (IOException e) {
        e.printStackTrace();
    }
}
    
    private void loadComboBoxFromPOTable() {
    if (originalPOTableModel == null) return;

    Set<String> itemNames = new HashSet<>();
    int itemNameColIndex = 3;

    for (int i = 0; i < originalPOTableModel.getRowCount(); i++) {
        Object val = originalPOTableModel.getValueAt(i, itemNameColIndex);
        if (val != null) itemNames.add(val.toString());
    }

    jComboBox1.removeAllItems();
    jComboBox1.addItem("ALL");

    List<String> sortedNames = itemNames.stream().sorted().collect(Collectors.toList());
    for (String name : sortedNames) {
        jComboBox1.addItem(name);
    }
}

    private void loadComboBoxFromSETable() {
        if (originalSETableModel == null) return;

        Set<String> itemNames = new HashSet<>();
        int itemNameColIndex = 6; 

        for (int i = 0; i < originalSETableModel.getRowCount(); i++) {
            Object val = originalSETableModel.getValueAt(i, itemNameColIndex);
            if (val != null) itemNames.add(val.toString());
        }

        jComboBox2.removeAllItems();
        jComboBox2.addItem("ALL");

        List<String> sortedNames = itemNames.stream().sorted().collect(Collectors.toList());
        for (String name : sortedNames) {
            jComboBox2.addItem(name);
        }
    }

    private void filterPOTableByItemName(String itemName) {
    if (originalPOTableModel == null) return;

    if (itemName.equals("ALL")) {
        POTable.setModel(originalPOTableModel);
        return;
    }

    DefaultTableModel filteredModel = new DefaultTableModel();
    int colCount = originalPOTableModel.getColumnCount();
    for (int c = 0; c < colCount; c++) {
        filteredModel.addColumn(originalPOTableModel.getColumnName(c));
    }

    int itemNameColIndex = 3;

    for (int i = 0; i < originalPOTableModel.getRowCount(); i++) {
        Object val = originalPOTableModel.getValueAt(i, itemNameColIndex);
        if (val != null && val.toString().equals(itemName)) {
            Vector<Object> row = new Vector<>();
            for (int c = 0; c < colCount; c++) {
                row.add(originalPOTableModel.getValueAt(i, c));
            }
            filteredModel.addRow(row);
        }
    }

    POTable.setModel(filteredModel);
}

    private void filterSETableByItemName(String itemName) {
        if (originalSETableModel == null) return;

        if (itemName.equals("ALL")) {
            SETable.setModel(originalSETableModel);
            return;
        }

        DefaultTableModel filteredModel = new DefaultTableModel();
        int colCount = originalSETableModel.getColumnCount();
        for (int c = 0; c < colCount; c++) {
            filteredModel.addColumn(originalSETableModel.getColumnName(c));
        }

        int itemNameColIndex = 6;

        for (int i = 0; i < originalSETableModel.getRowCount(); i++) {
            Object val = originalSETableModel.getValueAt(i, itemNameColIndex);
            if (val != null && val.toString().equals(itemName)) {
                Vector<Object> row = new Vector<>();
                for (int c = 0; c < colCount; c++) {
                    row.add(originalSETableModel.getValueAt(i, c));
                }
                filteredModel.addRow(row);
            }
        }

        SETable.setModel(filteredModel);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        POTable = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        SETable = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        StockTable = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jButton2 = new javax.swing.JButton();

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

        jPanel1.setBackground(new java.awt.Color(242, 242, 204));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("Generate Stock Report");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(26, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(15, 15, 15))
        );

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("Purchase Order Records");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("Sales Entry Records");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jButton1.setText("Generate Report");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        POTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "PO ID", "PR ID", "Item ID", "Item Name", "Quantity", "Item Price", "Total Price", "Date Created PO", "Date Required", "Supplier ID", "SM ID", "PM ID", "Status"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Double.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(POTable);

        SETable.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane3.setViewportView(SETable);

        StockTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Item ID", "Item Name", "Category", "Stock Amount"
            }
        ));
        jScrollPane4.setViewportView(StockTable);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setText("Current Stock List");

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });

        jButton2.setText("Back");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 615, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGap(18, 18, 18)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 353, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 986, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(21, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 324, Short.MAX_VALUE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        String selected = (String) jComboBox1.getSelectedItem();
        if (selected != null) {
            filterPOTableByItemName(selected);
        }// TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        String selectedItem = (String) jComboBox2.getSelectedItem();
        if (selectedItem != null) {
            filterSETableByItemName(selectedItem);
        }// TODO add your handling code here:
    }//GEN-LAST:event_jComboBox2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Report saved");
    fileChooser.setSelectedFile(new File("report.txt"));

    int userSelection = fileChooser.showSaveDialog(this);
    if (userSelection == JFileChooser.APPROVE_OPTION) {
        File fileToSave = fileChooser.getSelectedFile();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave))) {
            writeTableDataToFile(writer, POTable, "=== Purchase Order Records ===");
            writeTableDataToFile(writer, SETable, "=== Sales Entry Records ===");
            writeTableDataToFile(writer, StockTable, "=== Current Stock List ===");
            writer.flush();
            JOptionPane.showMessageDialog(this, "Saved:\n" + fileToSave.getAbsolutePath());
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    } else {

        System.out.println("User cancelled");
    }
}

private void writeTableDataToFile(BufferedWriter writer, JTable table, String header) throws IOException {
    writer.write(header);
    writer.newLine();

    DefaultTableModel model = (DefaultTableModel) table.getModel();

    int colCount = model.getColumnCount();
    for (int c = 0; c < colCount; c++) {
        writer.write(model.getColumnName(c));
        if (c < colCount - 1) writer.write("\t"); 
    }
    writer.newLine();

    int rowCount = model.getRowCount();
    for (int r = 0; r < rowCount; r++) {
        for (int c = 0; c < colCount; c++) {
            Object val = model.getValueAt(r, c);
            writer.write(val == null ? "" : val.toString());
            if (c < colCount - 1) writer.write("\t");
        }
        writer.newLine();
    }
    writer.newLine();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        this.dispose(); 
        if (parentWindow != null) {
            parentWindow.setVisible(true); 
        }
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
            java.util.logging.Logger.getLogger(GenerateReport.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GenerateReport.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GenerateReport.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GenerateReport.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GenerateReport().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable POTable;
    private javax.swing.JTable SETable;
    private javax.swing.JTable StockTable;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
