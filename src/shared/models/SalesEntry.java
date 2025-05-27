package shared.models;

public class SalesEntry implements Recordable {
    private String salesId;
    private String date;
    private String dateRequired;
    private Customer customer;
    private Item item;
    private int quantity;
    private String status;  // New field

    public SalesEntry(String salesId, String date, String dateRequired, 
                    Customer customer, Item item, int quantity) {
        this(salesId, date, dateRequired, customer, item, quantity, "unprocessed");
    }

    // New constructor with status
    public SalesEntry(String salesId, String date, String dateRequired, 
                    Customer customer, Item item, int quantity, String status) {
        this.salesId = salesId;
        this.date = date;
        this.dateRequired = dateRequired;
        this.customer = customer;
        this.item = item;
        this.quantity = quantity;
        this.status = status;
    }

    // Getters
    public String getSalesId() { return salesId; }
    public String getDate() { return date; }
    public String getDateRequired() { return dateRequired; }
    public Customer getCustomer() { return customer; }
    public Item getItem() { return item; }
    public int getQuantity() { return quantity; }
    public String getStatus() { return status; }  // New getter

    // Setter for status
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String getId() {
        return salesId;
    }

    @Override
    public String toCsvString() {
        return String.join(",",
            salesId,
            date,
            dateRequired,
            customer.getName(),
            customer.getContact(),
            item.getItemId(),
            item.getName(),
            String.valueOf(quantity),
            status  // Added status to CSV output
        );
    }
}