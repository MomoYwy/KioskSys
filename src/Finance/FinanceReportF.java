package Finance;

import javax.swing.*;
import java.io.*;
import java.util.*;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author User
 */
public class FinanceReportF extends javax.swing.JFrame {

    /**
     * Creates new form FinanceReportF
     */
    public FinanceReportF() {
        initComponents();
        // Reload the dummy data and display it
        loadDefaultData();
    }
    
      // Method to load dummy data into the JTable by default
    private void loadDefaultData() {
        // Read the dummy data from the file
        List<po> poList = readPODataFromFile("src/finance/dummy.txt");
        
        // Call displayDataInTable to populate the JTable
    displayDataInTable(poList);
        
        // Update the bottom fields
    updateStatusFields(poList);
    }
    
    // Method to read PO data from the text file (same as before)
    public static List<po> readPODataFromFile(String fileName) {
        List<po> poList = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;
            po po = null;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("PO ID")) {
                    if (po != null) {
                        poList.add(po);  // Add previous PO if available
                    }
                    po = new po();
                    po.setPoId(line.split(":")[1].trim());
                }
                if (line.startsWith("Supplier ID")) {
                    po.setSupplierId(line.split(":")[1].trim());
                }
                if (line.startsWith("Total Amount")) {
                    po.setTotalAmount(Double.parseDouble(line.split(":")[1].trim()));
                }
                if (line.startsWith("Payment Status")) {
                    po.setPaymentStatus(line.split(":")[1].trim());
                }
                if (line.startsWith("PO Status")) {
                    po.setPoStatus(line.split(":")[1].trim());
                }
                if (line.startsWith("Date")) {
                    po.setDate(line.split(":")[1].trim());
                }
            }
            if (po != null) {
                poList.add(po);  // Add the last PO
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return poList;
    }

    // Method to display data in the JTable
    public void displayDataInTable(List<po> poList) {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        
        // Clear previous data in the JTable
        model.setRowCount(0);
        
        // Populate JTable with data
        for (po po : poList) {
            Object[] row = {
                po.getPoId(),
                po.getSupplierId(),
                po.getTotalAmount(),
                po.getPaymentStatus()
            };
            model.addRow(row);  // Add the row to the JTable
        }
    }
    
    // Method to filter the data based on selected criteria
    public List<po> filterData(String selectedDay, String selectedPoStatus, String selectedPaymentStatus, String searchKeyword) {
    List<po> poList = readPODataFromFile("src/finance/dummy.txt");  // You can adjust the file path accordingly

    List<po> filteredPoList = new ArrayList<>();

    // Apply the filters based on user input
    for (po po : poList) {
        boolean matches = true;

        // Filter by PO Status
        if (!selectedPoStatus.equals("All") && !po.getPoStatus().equals(selectedPoStatus)) {
            matches = false;
        }

        // Filter by Payment Status
        if (!selectedPaymentStatus.equals("All") && !po.getPaymentStatus().equals(selectedPaymentStatus)) {
            matches = false;
        }

        // Search functionality based on PO ID or Supplier ID
        if (!searchKeyword.isEmpty()) {
        if (!(po.getPoId().toLowerCase().contains(searchKeyword.toLowerCase().trim()) || po.getSupplierId().toLowerCase().contains(searchKeyword.toLowerCase().trim()))) {
        matches = false;
        }
    }

        // If all conditions match, add the PO to the filtered list
        if (matches) {
            filteredPoList.add(po);
        }
    }

    return filteredPoList;
}
    
    private void updateStatusFields(List<po> poList) {
    // Count the total POs
    int totalPOs = poList.size();

    // Count Pending POs
    long pendingCount = poList.stream().filter(po -> po.getPoStatus().equalsIgnoreCase("Pending")).count();

    // Count Paid POs
    long paidCount = poList.stream().filter(po -> po.getPaymentStatus().equalsIgnoreCase("Paid")).count();

    // Update the UI fields
    txtTotalPo.setText(String.valueOf(totalPOs));
    txtPending.setText(String.valueOf(pendingCount));
    txtPaid.setText(String.valueOf(paidCount));
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
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        cmbDay = new javax.swing.JComboBox<>();
        btnOK = new javax.swing.JButton();
        btnRefresh = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        txtSearch = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        cmbPoStatus = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        cmbPaymentStatus = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtTotalPo = new javax.swing.JTextField();
        txtPending = new javax.swing.JTextField();
        txtPaid = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 204));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("Finance Report");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel1)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(255, 233, 217));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "PO ID", "Supplier ID", "Total Amount", "Payment Status"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jLabel2.setText("Search:");

        cmbDay.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Today", "This Week", "This Month", "This Year" }));

        btnOK.setText("OK");
        btnOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOKActionPerformed(evt);
            }
        });

        btnRefresh.setText("Refresh");
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });

        jLabel3.setText("Day:");

        jLabel4.setText("PO Status:");

        cmbPoStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pending", "Approved", "Dispatched", "Received" }));

        jLabel5.setText("Payment Status:");

        cmbPaymentStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Paid", "Pending", "Overdue", "Refunded" }));

        jLabel6.setText("Total POs:");

        jLabel7.setText("Pending:");

        jLabel8.setText("Paid:");

        txtPending.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPendingActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 555, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(18, 18, 18)
                                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 396, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(cmbDay, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(31, 31, 31)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(cmbPoStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(32, 32, 32)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5)
                                    .addComponent(cmbPaymentStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(141, 141, 141)
                        .addComponent(btnRefresh)
                        .addGap(82, 82, 82)
                        .addComponent(btnOK))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTotalPo, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPending, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPaid, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbDay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbPoStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbPaymentStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRefresh)
                    .addComponent(btnOK))
                .addGap(37, 37, 37)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8)
                    .addComponent(txtTotalPo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPending, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPaid, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(43, Short.MAX_VALUE))
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
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOKActionPerformed
        String selectedDay = (String) cmbDay.getSelectedItem();
        String selectedPoStatus = (String) cmbPoStatus.getSelectedItem();
        String selectedPaymentStatus = (String) cmbPaymentStatus.getSelectedItem();
        String searchKeyword = txtSearch.getText();

    // Filter the data based on selected criteria
        List<po> filteredPoList = filterData(selectedDay, selectedPoStatus, selectedPaymentStatus, searchKeyword);

    // Display the filtered data in the JTable
        displayDataInTable(filteredPoList);
        
    // Update the bottom fields based on the filtered data
    updateStatusFields(filteredPoList);
    }//GEN-LAST:event_btnOKActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
         loadDefaultData();
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void txtPendingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPendingActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPendingActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        new FinanceReportF();
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
            java.util.logging.Logger.getLogger(FinanceReportF.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FinanceReportF.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FinanceReportF.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FinanceReportF.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FinanceReportF().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnOK;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JComboBox<String> cmbDay;
    private javax.swing.JComboBox<String> cmbPaymentStatus;
    private javax.swing.JComboBox<String> cmbPoStatus;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField txtPaid;
    private javax.swing.JTextField txtPending;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtTotalPo;
    // End of variables declaration//GEN-END:variables
}
