package shared.models;

public class Supplier {
    private String supplierId;
    private String name;
    private String contact;
    private Address address;
    private String itemID;
    private double itemPrice;
    private int delivery_time;

    public Supplier(String supplierId, String name, String contact, Address address, 
                   String itemID, double itemPrice, int delivery_time) {
        this.supplierId = supplierId;
        this.name = name;
        this.contact = contact;
        this.address = address;
        this.itemID = itemID;
        this.itemPrice = itemPrice;
        this.delivery_time = delivery_time;
    }
    
    // Getters
    public String getSupplierId() { return supplierId; }
    public String getName() { return name; }
    public String getContact() { return contact; }
    public Address getAddress() { return address; }
    public String getItemID() { return itemID; }
    public double getItemPrice() { return itemPrice; }
    public int getDelivery_time() { return delivery_time; }
}