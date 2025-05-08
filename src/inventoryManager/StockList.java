
package inventoryManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.Color;
import java.awt.Component;
import java.io.*;
import javax.swing.*;
import javax.swing.table.*;
import java.util.Map;
import java.util.HashMap;
import shared.frames.ViewSalesEntry;

public class StockList extends javax.swing.JFrame {
    private final java.util.Set<Integer> markedRows = new java.util.HashSet<>();
    
    public StockList() {
        initComponents();  
        loadStockListToTable("src/database/stocklist.txt"); 
        loadSalesData("src/database/sales_entry.txt");
    }
   
    private void loadSalesData(String filePath) {
    try {
        //Load existing status from stocklist.txt
        Map<String, String> oldStatusMap = new HashMap<>();
        try (BufferedReader oldStockReader = new BufferedReader(new FileReader("src/database/stocklist.txt"))) {
            String oldLine;
            while ((oldLine = oldStockReader.readLine()) != null) {
                String[] parts = oldLine.split(",");
                if (parts.length >= 5) {
                    String itemId = parts[0].trim();
                    String status = parts[4].trim();
                    oldStatusMap.put(itemId, status);
                }
            }
        }

        //Loadcategory from items.txt
        Map<String, String> categoryMap = new HashMap<>();
        try (BufferedReader itemReader = new BufferedReader(new FileReader("src/database/items.txt"))) {
            String line;
            while ((line = itemReader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String itemId = parts[0].trim();
                    String category = parts[parts.length - 1].trim();
                    categoryMap.put(itemId, category);
                }
            }
        }

        //Load item from sales_entry.txt
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        Map<String, Integer> stockMap = new HashMap<>();
        Map<String, String> itemNameMap = new HashMap<>();

        String line;
        while ((line = br.readLine()) != null) {
            String[] data = line.split(",");
            if (data.length >= 8) {
                String itemId = data[5].trim();
                String itemName = data[6].trim();
                int soldQty = Integer.parseInt(data[7].trim());

                stockMap.put(itemId, stockMap.getOrDefault(itemId, 0) - soldQty);
                itemNameMap.put(itemId, itemName);
            }
        }
        br.close();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/database/stocklist.txt"))) {
            for (Map.Entry<String, Integer> entry : stockMap.entrySet()) {
                String itemId = entry.getKey();
                int quantity = entry.getValue();
                String itemName = itemNameMap.get(itemId);
                String category = categoryMap.getOrDefault(itemId, "N/A");

                String status;
                if (oldStatusMap.containsKey(itemId)) {
                    status = oldStatusMap.get(itemId);
                } else {
                    status = quantity < 10 ? "low stock" : "normal";
                }

                writer.write(itemId + "," + itemName + "," + category + "," + quantity + "," + status);
                writer.newLine();
            }
        }

    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Failed: " + e.getMessage());
    }
}

        private void loadStockListToTable(String filePath) {
            DefaultTableModel model = (DefaultTableModel) StockTable.getModel();
            model.setRowCount(0); 

            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] data = line.split(",");

                    if (data.length >= 4) {
                        String itemId = data[0].trim();
                        String itemName = data[1].trim();
                        String category = data[2].trim();
                        String quantityStr = data[3].trim();

                        model.addRow(new Object[]{itemId, itemName, category, quantityStr});

                        if (data.length >= 5) {
                            String status = data[4].trim();
                            if ("low stock".equals(status)) {
                                markedRows.add(model.getRowCount() - 1);
                            }
                        } else {
                            int quantity = Integer.parseInt(quantityStr);
                            if (quantity < 10) {
                                markedRows.add(model.getRowCount() - 1);
                            }
                        }
                    }
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Failed loading stocklist.txt : " + e.getMessage());
            }
        
        //Automation highlight 
        StockTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                int quantity = Integer.parseInt(table.getValueAt(row, 3).toString());
                
                comp.setBackground(Color.WHITE);

                if (quantity < 10) {
                    comp.setBackground(Color.PINK);
                    
                    markedRows.add(row);
                }

                if (markedRows.contains(row)) {
                    comp.setBackground(Color.PINK);
                }

                if (isSelected) {
                    comp.setBackground(table.getSelectionBackground());
                }

                return comp;
            }
        });
    }
    
        // Saved highlight to file
        private void saveStockListToFile() {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/database/stocklist.txt"))) {
                DefaultTableModel model = (DefaultTableModel) StockTable.getModel();
                for (int i = 0; i < model.getRowCount(); i++) {
                    String itemId = model.getValueAt(i, 0).toString();
                    String itemName = model.getValueAt(i, 1).toString();
                    String category = model.getValueAt(i, 2).toString();
                    String quantity = model.getValueAt(i, 3).toString();

                    String status = markedRows.contains(i) ? "low stock" : "normal";

                    writer.write(itemId + "," + itemName + "," + category + "," + quantity + "," + status);
                    writer.newLine();
                }
                JOptionPane.showMessageDialog(this, "Low stock saved");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Failed: " + e.getMessage());
            }
        }
    
        //Highlight function
        private void toggleMarkSelectedRows() {
        int[] selectedRows = StockTable.getSelectedRows();
        DefaultTableModel model = (DefaultTableModel) StockTable.getModel();

        boolean hasValidChange = false; 
        boolean hasLowStock = false;    
        StringBuilder lowStockRows = new StringBuilder();

        for (int row : selectedRows) {
            int quantity = Integer.parseInt(model.getValueAt(row, 3).toString());

            if (quantity < 10) {
                hasLowStock = true;
                lowStockRows.append("Row ").append(row + 1).append(", ");
                continue;
            }

            if (markedRows.contains(row)) {
                markedRows.remove(row);
            } else {
                markedRows.add(row);
            }
            hasValidChange = true; 
        }

        if (hasLowStock) {
            String rowsText = lowStockRows.substring(0, lowStockRows.length() - 2);
            JOptionPane.showMessageDialog(this, "Already low stock (" + rowsText + ")");
        }

        StockTable.repaint();

        if (hasValidChange) {
            saveStockListToFile(); 
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
        jScrollPane1 = new javax.swing.JScrollPane();
        StockTable = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        btnViewSalesEntry = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(242, 242, 204));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("Stock List");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(492, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(21, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addContainerGap())
        );

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
        jScrollPane1.setViewportView(StockTable);

        jButton1.setText("Update stocks");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        btnViewSalesEntry.setText("View Sales Entry");
        btnViewSalesEntry.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewSalesEntryActionPerformed(evt);
            }
        });

        jButton3.setText("Low stock");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnViewSalesEntry)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(btnViewSalesEntry)
                    .addComponent(jButton3))
                .addGap(0, 68, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
       
    }//GEN-LAST:event_formWindowOpened

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        loadSalesData("src/database/sales_entry.txt");
        loadStockListToTable("src/database/stocklist.txt");
        JOptionPane.showMessageDialog(null, "Stocks updated");
    
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnViewSalesEntryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewSalesEntryActionPerformed
        ViewSalesEntry salesEntryFrame = new ViewSalesEntry();
    
        // Position it to the right of the StockList frame
        Point stockListLocation = this.getLocation();
        int x = stockListLocation.x + this.getWidth() + 10; // 10 pixels gap
        int y = stockListLocation.y;
        salesEntryFrame.setLocation(x, y);

        // Make sure both frames stay on top when either is clicked
        salesEntryFrame.setAlwaysOnTop(this.isAlwaysOnTop());

        // Show the sales entry frame
        salesEntryFrame.setVisible(true);

        // Add listener to refresh stock list when sales entry is closed
        salesEntryFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                loadSalesData("src/database/sales_entry.txt");
            }
        });
    }//GEN-LAST:event_btnViewSalesEntryActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
       toggleMarkSelectedRows();
    }//GEN-LAST:event_jButton3ActionPerformed

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
            java.util.logging.Logger.getLogger(StockList.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(StockList.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(StockList.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(StockList.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new StockList().setVisible(true);
            }
        });
    }
    
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable StockTable;
    private javax.swing.JButton btnViewSalesEntry;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
