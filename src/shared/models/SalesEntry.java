package shared.models;

public class SalesEntry {
    private String salesId;
    private String date;
    private String dateRequired;
    private Customer customer;
    private Item item;
    private int quantity;

    // Constructor without salesId (for new entries)
    public SalesEntry(String date, String dateRequired, Customer customer, Item item, int quantity) {
        this.date = date;
        this.dateRequired = dateRequired;
        this.customer = customer;
        this.item = item;
        this.quantity = quantity;
    }

    // Constructor with salesId (for existing entries)
    public SalesEntry(String salesId, String date, String dateRequired, Customer customer, Item item, int quantity) {
        this(date, dateRequired, customer, item, quantity);
        this.salesId = salesId;
    }

    // Getters and Setters
    public String getSalesId() { return salesId; }
    public void setSalesId(String salesId) { this.salesId = salesId; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getDateRequired() { return dateRequired; }
    public void setDateRequired(String dateRequired) { this.dateRequired = dateRequired; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public Item getItem() { return item; }
    public void setItem(Item item) { this.item = item; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
