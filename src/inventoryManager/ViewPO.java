package inventoryManager;
import java.util.HashSet;
import java.util.Set;
import javax.swing.table.DefaultTableModel;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;



public class ViewPO extends javax.swing.JFrame {
    
     public ViewPO() {
        initComponents();
        loadPurchaseOrders();
    }
    
    private void updateFromPO(){
        int selectedRow = poTable.getSelectedRow();
    if (selectedRow == -1) {
        javax.swing.JOptionPane.showMessageDialog(this, "Please select a row first.");
        return;
    }

    String itemId = poTable.getValueAt(selectedRow, 2).toString(); 
    int quantityToAdd = Integer.parseInt(poTable.getValueAt(selectedRow, 4).toString()); 

    String stockFilePath = "src/database/stocklist.txt";
    java.util.List<String> updatedLines = new java.util.ArrayList<>();

    boolean itemFound = false;

    try (BufferedReader reader = new BufferedReader(new FileReader(stockFilePath))) {
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",", -1);
            if (parts.length >= 5 && parts[0].trim().equals(itemId)) {
    int currentStock = Integer.parseInt(parts[3].trim());
    int newStock = currentStock + quantityToAdd;
    parts[3] = String.valueOf(newStock);

    if (newStock >= 10) {
        parts[4] = "normal";
    } else {
        parts[4] = "low stock";
    }

    itemFound = true;
}
            updatedLines.add(String.join(",", parts));
        }
    } catch (IOException | NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Error reading stock file: " + e.getMessage());
        return;
    }

    if (!itemFound) {
        JOptionPane.showMessageDialog(this, "Item ID not found in stocklist.");
        return;
    }

    try (BufferedWriter writer = new BufferedWriter(new FileWriter(stockFilePath))) {
        for (String updatedLine : updatedLines) {
            writer.write(updatedLine);
            writer.newLine();
        }
        JOptionPane.showMessageDialog(this, "Stock updated successfully.");
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Error writing stock file: " + e.getMessage());
        return;
    }

    String poFilePath = "src/database/purchase_orders.txt";
    List<String> poLines = new ArrayList<>();
    boolean poUpdated = false;

    try (BufferedReader poReader = new BufferedReader(new FileReader(poFilePath))) {
        String line;
        while ((line = poReader.readLine()) != null) {
            String[] parts = line.split(",", -1);
            if (parts.length > 12 && parts[2].trim().equals(itemId)) {
                parts[12] = "RECEIVED_ITEMS";
                poUpdated = true;
            }
            poLines.add(String.join(",", parts));
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Error reading purchase_orders file: " + e.getMessage());
        return;
    }

    if (poUpdated) {
        try (BufferedWriter poWriter = new BufferedWriter(new FileWriter(poFilePath))) {
            for (String updatedLine : poLines) {
                poWriter.write(updatedLine);
                poWriter.newLine();
            }
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error writing purchase_orders file: " + e.getMessage());
        }

        ((DefaultTableModel) poTable.getModel()).removeRow(selectedRow);
    } else {
        JOptionPane.showMessageDialog(this, "No matching purchase order found for the selected Item ID.");
    }
    }
    
    private void loadPurchaseOrders() {
    String purchaseOrderPath = "src/database/purchase_orders.txt";

    DefaultTableModel model = (DefaultTableModel) poTable.getModel();
    model.setRowCount(0); 

    try (BufferedReader br = new BufferedReader(new FileReader(purchaseOrderPath))) {
        String line;
        br.readLine(); 
        while ((line = br.readLine()) != null) {
            String[] data = line.split(",", -1); 
            if (data.length == 13) {
                String status = data[12].trim(); 
                if ("PAID".equalsIgnoreCase(status)) {
                    Object[] row = new Object[] {
                        data[0], data[1], data[2], data[3],
                        Integer.parseInt(data[4]), Double.parseDouble(data[5]),
                        Double.parseDouble(data[6]), data[7], data[8],
                        data[9], data[10], data[11], data[12]
                    };
                    model.addRow(row);
                }
            }
        }
    } catch (IOException e) {
        System.out.println("Unable to purchase_orders.txt：" + e.getMessage());
    } catch (NumberFormatException e) {
        System.out.println("Format Wrong：" + e.getMessage());
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
        UpdateStock = new javax.swing.JButton();
        Back = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        poTable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(242, 242, 204));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("View Purchase Order");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        UpdateStock.setText("Update stocks");
        UpdateStock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UpdateStockActionPerformed(evt);
            }
        });

        Back.setText("Back");
        Back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BackActionPerformed(evt);
            }
        });

        poTable.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(poTable);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 824, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(UpdateStock, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Back, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 417, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(UpdateStock)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Back)
                .addGap(0, 11, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void UpdateStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UpdateStockActionPerformed
        updateFromPO();
    }//GEN-LAST:event_UpdateStockActionPerformed

    private void BackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BackActionPerformed
        this.dispose(); // TODO add your handling code here:
    }//GEN-LAST:event_BackActionPerformed

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
    private javax.swing.JButton Back;
    private javax.swing.JButton UpdateStock;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable poTable;
    // End of variables declaration//GEN-END:variables
}
