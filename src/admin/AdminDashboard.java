/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package admin;

import Finance.ManagementPO;
import Finance.PaymentFrame;
import Finance.FinanceReportF;
import PurchaseManager.GeneratePurchaseOrderForm;
import PurchaseManager.ViewPR;
import PurchaseManager.ViewPurchaseOrder;
import inventoryManager.StockList;
import inventoryManager.IMDashboard;
import salesManager.DailySalesEntry;
import salesManager.ItemEntry;
import salesManager.PurchaseRequisitionEntry;
import salesManager.SupplierEntry;
import shared.frames.LoginScreen;


/**
 *
 * @author User
 */
public class AdminDashboard extends javax.swing.JFrame {

    private String userId;
    private String username;

    public AdminDashboard(String userId, String username) {
        initComponents();
        this.userId = userId;
        this.username = username;
        setTitle("Admin Dashboard - " + username + " (" + userId + ")");
    }

        // Add this method to allow access to userId
    public String getUserId() {
        return userId;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        btnUserList = new javax.swing.JButton();
        btnUserRegistration = new javax.swing.JButton();
        btnItemList = new javax.swing.JButton();
        btnPR = new javax.swing.JButton();
        btnSupplierList = new javax.swing.JButton();
        btnDailySales = new javax.swing.JButton();
        btnStockList = new javax.swing.JButton();
        btnStockReport = new javax.swing.JButton();
        btnPO = new javax.swing.JButton();
        btnApprovePO = new javax.swing.JButton();
        btnProcessPayments = new javax.swing.JButton();
        btnLogout = new javax.swing.JButton();
        btnviewPO = new javax.swing.JButton();
        btnFinanceReport = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 204));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("OWSB Admin Dashboard");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(70, 70, 70)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(jLabel1)
                .addContainerGap(49, Short.MAX_VALUE))
        );

        btnUserList.setText("User List");
        btnUserList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUserListActionPerformed(evt);
            }
        });

        btnUserRegistration.setText("User Registration");
        btnUserRegistration.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUserRegistrationActionPerformed(evt);
            }
        });

        btnItemList.setText("Item List");
        btnItemList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnItemListActionPerformed(evt);
            }
        });

        btnPR.setText("PR");
        btnPR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPRActionPerformed(evt);
            }
        });

        btnSupplierList.setText("Supplier List");
        btnSupplierList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSupplierListActionPerformed(evt);
            }
        });

        btnDailySales.setText("Daily Sales");
        btnDailySales.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDailySalesActionPerformed(evt);
            }
        });

        btnStockList.setText("Stock List");
        btnStockList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStockListActionPerformed(evt);
            }
        });

        btnStockReport.setText("Stock Report");
        btnStockReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStockReportActionPerformed(evt);
            }
        });

        btnPO.setText("Generate PO");
        btnPO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPOActionPerformed(evt);
            }
        });

        btnApprovePO.setText("Approve PO");
        btnApprovePO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnApprovePOActionPerformed(evt);
            }
        });

        btnProcessPayments.setText("Process Payments");
        btnProcessPayments.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcessPaymentsActionPerformed(evt);
            }
        });

        btnLogout.setText("Logout");
        btnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogoutActionPerformed(evt);
            }
        });

        btnviewPO.setText("View PO(Edit & delete)");
        btnviewPO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnviewPOActionPerformed(evt);
            }
        });

        btnFinanceReport.setText("Finance Report");
        btnFinanceReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFinanceReportActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnLogout, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnFinanceReport, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnUserList, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnPO, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnSupplierList, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(31, 31, 31)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnDailySales, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnviewPO, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnUserRegistration, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(41, 41, 41)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btnStockList, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnApprovePO, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE)
                    .addComponent(btnItemList, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(48, 48, 48)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnProcessPayments, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnStockReport, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnPR, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(22, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnUserList)
                    .addComponent(btnUserRegistration)
                    .addComponent(btnItemList)
                    .addComponent(btnPR))
                .addGap(27, 27, 27)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSupplierList)
                    .addComponent(btnDailySales)
                    .addComponent(btnStockList)
                    .addComponent(btnStockReport))
                .addGap(27, 27, 27)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnPO)
                    .addComponent(btnApprovePO)
                    .addComponent(btnProcessPayments)
                    .addComponent(btnviewPO))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addComponent(btnFinanceReport)
                .addGap(7, 7, 7)
                .addComponent(btnLogout)
                .addGap(25, 25, 25))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnUserListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUserListActionPerformed
        Users user = new Users(userId, username);
        user.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnUserListActionPerformed

    private void btnPOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPOActionPerformed
        GeneratePurchaseOrderForm poForm = new GeneratePurchaseOrderForm(userId, username);
        
        java.awt.Point dashboardLocation = this.getLocation();
        poForm.setLocation(dashboardLocation.x + 50, dashboardLocation.y + 50);
        
        this.setVisible(false);
        
        poForm.setVisible(true);
    }//GEN-LAST:event_btnPOActionPerformed

    private void btnviewPOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnviewPOActionPerformed
        ViewPurchaseOrder viewPOForm = new ViewPurchaseOrder(userId, username);

        java.awt.Point dashboardLocation = this.getLocation();
        viewPOForm.setLocation(dashboardLocation.x + 50, dashboardLocation.y + 50);

        this.setVisible(false);

        viewPOForm.setVisible(true);
    }//GEN-LAST:event_btnviewPOActionPerformed

    private void btnStockListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStockListActionPerformed
        StockList stockList = new StockList(this); 
        stockList.setVisible(true);
        this.setVisible(false); 
        this.dispose();  
    }//GEN-LAST:event_btnStockListActionPerformed

    private void btnStockReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStockReportActionPerformed
        IMDashboard.ReportGenerator.generateReportFromStocklist(this);
    }//GEN-LAST:event_btnStockReportActionPerformed

    private void btnUserRegistrationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUserRegistrationActionPerformed
        RegisterScreen register = new RegisterScreen(userId, username);
        register.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnUserRegistrationActionPerformed

    private void btnItemListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnItemListActionPerformed
        ItemEntry itEntry = new ItemEntry(userId, username);
        itEntry.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnItemListActionPerformed

    private void btnPRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPRActionPerformed
        PurchaseRequisitionEntry prEntry = new PurchaseRequisitionEntry(userId, username);
        prEntry.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnPRActionPerformed

    private void btnSupplierListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSupplierListActionPerformed
        SupplierEntry spEntry = new SupplierEntry(userId, username);
        spEntry.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnSupplierListActionPerformed

    private void btnDailySalesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDailySalesActionPerformed
        DailySalesEntry salesEntry = new DailySalesEntry(userId, username);
        salesEntry.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnDailySalesActionPerformed

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
        LoginScreen login = new LoginScreen();
        login.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnLogoutActionPerformed

    private void btnApprovePOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnApprovePOActionPerformed
        ManagementPO MPOForm = new ManagementPO(this);
        
        java.awt.Point dashboardLocation = this.getLocation();
        MPOForm.setLocation(dashboardLocation.x + 50, dashboardLocation.y + 50);
        
        this.setVisible(false);
        
        MPOForm.setVisible(true);
    }//GEN-LAST:event_btnApprovePOActionPerformed

    private void btnProcessPaymentsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcessPaymentsActionPerformed
        PaymentFrame PFForm = new PaymentFrame(this);
        
        java.awt.Point dashboardLocation = this.getLocation();
        PFForm.setLocation(dashboardLocation.x + 50, dashboardLocation.y + 50);
        
        this.setVisible(false);
        
        PFForm.setVisible(true);
    }//GEN-LAST:event_btnProcessPaymentsActionPerformed

    private void btnFinanceReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFinanceReportActionPerformed
        FinanceReportF FRForm = new FinanceReportF(this);
        
        java.awt.Point dashboardLocation = this.getLocation();
        FRForm.setLocation(dashboardLocation.x + 50, dashboardLocation.y + 50);
        
        this.setVisible(false);
        
        FRForm.setVisible(true);
    }//GEN-LAST:event_btnFinanceReportActionPerformed

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
            java.util.logging.Logger.getLogger(AdminDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AdminDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AdminDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AdminDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnApprovePO;
    private javax.swing.JButton btnDailySales;
    private javax.swing.JButton btnFinanceReport;
    private javax.swing.JButton btnItemList;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnPO;
    private javax.swing.JButton btnPR;
    private javax.swing.JButton btnProcessPayments;
    private javax.swing.JButton btnStockList;
    private javax.swing.JButton btnStockReport;
    private javax.swing.JButton btnSupplierList;
    private javax.swing.JButton btnUserList;
    private javax.swing.JButton btnUserRegistration;
    private javax.swing.JButton btnviewPO;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables
}
