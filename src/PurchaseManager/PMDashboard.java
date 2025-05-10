
package PurchaseManager;

import java.awt.Point;
import shared.frames.ViewSalesEntry;
import shared.frames.ViewSupplierEntry;


public class PMDashboard extends javax.swing.JFrame {


    public PMDashboard() {
        initComponents();
    }
    private void loadSuppliersData(String filepath) {
        System.out.println("Loading supplier data from: " + filepath);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        BGFrame = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        btnViewSuppliers = new javax.swing.JButton();
        btnViewItems = new javax.swing.JButton();
        btnViewPurchaseRequisition = new javax.swing.JButton();
        btnLogout = new javax.swing.JButton();
        btnGeneratePurchaseOrders = new javax.swing.JButton();
        btnViewPurchaseOrders = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Purchase Manager Dashboard");
        setResizable(false);

        BGFrame.setBackground(new java.awt.Color(255, 255, 204));

        jPanel2.setBackground(new java.awt.Color(255, 204, 153));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(252, 159, 184), new java.awt.Color(242, 242, 242)));
        jPanel2.setPreferredSize(new java.awt.Dimension(700, 109));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        jLabel1.setText("PURCHASE MANAGER DASHBOARD");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(154, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 391, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(149, 149, 149))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel1)
                .addContainerGap(26, Short.MAX_VALUE))
        );

        btnViewSuppliers.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        btnViewSuppliers.setText("View Suppliers");
        btnViewSuppliers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewSuppliersActionPerformed(evt);
            }
        });

        btnViewItems.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        btnViewItems.setText("View Items");
        btnViewItems.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnViewItems.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewItemsActionPerformed(evt);
            }
        });

        btnViewPurchaseRequisition.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        btnViewPurchaseRequisition.setText("View Purchase Requisitions");
        btnViewPurchaseRequisition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewPurchaseRequisitionActionPerformed(evt);
            }
        });

        btnLogout.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        btnLogout.setText("Logout");
        btnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogoutActionPerformed(evt);
            }
        });

        btnGeneratePurchaseOrders.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        btnGeneratePurchaseOrders.setText("Generate Purchase Orders");
        btnGeneratePurchaseOrders.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGeneratePurchaseOrdersActionPerformed(evt);
            }
        });

        btnViewPurchaseOrders.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        btnViewPurchaseOrders.setText("View Purchase Orders");
        btnViewPurchaseOrders.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewPurchaseOrdersActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout BGFrameLayout = new javax.swing.GroupLayout(BGFrame);
        BGFrame.setLayout(BGFrameLayout);
        BGFrameLayout.setHorizontalGroup(
            BGFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BGFrameLayout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 698, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(BGFrameLayout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addGroup(BGFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnViewItems, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnViewPurchaseRequisition, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(BGFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnViewSuppliers, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnViewPurchaseOrders, javax.swing.GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE))
                .addGap(76, 76, 76))
            .addGroup(BGFrameLayout.createSequentialGroup()
                .addGroup(BGFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(BGFrameLayout.createSequentialGroup()
                        .addGap(229, 229, 229)
                        .addComponent(btnGeneratePurchaseOrders, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(BGFrameLayout.createSequentialGroup()
                        .addGap(295, 295, 295)
                        .addComponent(btnLogout)))
                .addGap(257, 257, 257))
        );
        BGFrameLayout.setVerticalGroup(
            BGFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BGFrameLayout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(65, 65, 65)
                .addGroup(BGFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnViewItems, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnViewSuppliers, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(41, 41, 41)
                .addGroup(BGFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnViewPurchaseRequisition, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnViewPurchaseOrders, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addComponent(btnGeneratePurchaseOrders, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
                .addGap(28, 28, 28)
                .addComponent(btnLogout, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                .addGap(21, 21, 21))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(BGFrame, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(BGFrame, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnViewSuppliersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewSuppliersActionPerformed
        ViewSupplierEntry ViewSupplierFrame = new ViewSupplierEntry();
    
        // Position it to the right of the StockList frame
        Point PMDashboardLocation = this.getLocation();
        int x = PMDashboardLocation.x + this.getWidth() + 10; // 10 pixels gap
        int y = PMDashboardLocation.y;
        ViewSupplierFrame.setLocation(x, y);

        // Make sure both frames stay on top when either is clicked
        ViewSupplierFrame.setAlwaysOnTop(this.isAlwaysOnTop());

        // Show the sales entry frame
        ViewSupplierFrame.setVisible(true);

        // Add listener to refresh stock list when sales entry is closed
        ViewSupplierFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                loadSuppliersData("src/database/suppliers.txt");
            }
        



 
        });
    }//GEN-LAST:event_btnViewSuppliersActionPerformed

    private void btnViewItemsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewItemsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnViewItemsActionPerformed

    private void btnViewPurchaseRequisitionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewPurchaseRequisitionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnViewPurchaseRequisitionActionPerformed

    private void btnGeneratePurchaseOrdersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGeneratePurchaseOrdersActionPerformed
        GeneratePurchaseOrderForm poForm = new GeneratePurchaseOrderForm(this);
        
        // Position it relative to the dashboard
        java.awt.Point dashboardLocation = this.getLocation();
        poForm.setLocation(dashboardLocation.x + 50, dashboardLocation.y + 50);
        
        // Make dashboard invisible while working with PO form
        this.setVisible(false);
        
        // Show the PO form
        poForm.setVisible(true);
    }//GEN-LAST:event_btnGeneratePurchaseOrdersActionPerformed

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnLogoutActionPerformed

    private void btnViewPurchaseOrdersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewPurchaseOrdersActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnViewPurchaseOrdersActionPerformed

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
            java.util.logging.Logger.getLogger(PMDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PMDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PMDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PMDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PMDashboard().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel BGFrame;
    private javax.swing.JButton btnGeneratePurchaseOrders;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnViewItems;
    private javax.swing.JButton btnViewPurchaseOrders;
    private javax.swing.JButton btnViewPurchaseRequisition;
    private javax.swing.JButton btnViewSuppliers;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables
}
