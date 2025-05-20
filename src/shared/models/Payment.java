package shared.models;


import java.util.Date;


public class Payment {
    private String poId;
    private double paymentAmount;
    private Date paymentDate;
    private String paymentMethod;

    // Constructor
    public Payment(String poId, double paymentAmount, Date paymentDate, String paymentMethod) {
        this.poId = poId;
        this.paymentAmount = paymentAmount;
        this.paymentDate = paymentDate;
        this.paymentMethod = paymentMethod;
    }

    // Getters and setters
    public String getPoId() {
        return poId;
    }

    public void setPoId(String poId) {
        this.poId = poId;
    }

    public double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    
    private poManagement poManage = new poManagement();
    
    

}

