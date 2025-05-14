package shared.models;

public class SalesEntry implements Recordable {
    private String salesId;
    private String date;
    private String dateRequired;
    private Customer customer;
    private Item item;
    private int quantity;

    public SalesEntry(String salesId, String date, String dateRequired, 
                    Customer customer, Item item, int quantity) {
        this.salesId = salesId;
        this.date = date;
        this.dateRequired = dateRequired;
        this.customer = customer;
        this.item = item;
        this.quantity = quantity;
    }

    // Getters
    public String getSalesId() { return salesId; }
    public String getDate() { return date; }
    public String getDateRequired() { return dateRequired; }
    public Customer getCustomer() { return customer; }
    public Item getItem() { return item; }
    public int getQuantity() { return quantity; }

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
            String.valueOf(quantity)
        );
    }
}