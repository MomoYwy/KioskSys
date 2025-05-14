package shared.models;

public class Supplier implements Recordable {
    private String supplierId;
    private String name;
    private String itemName;
    private double itemPrice;
    private String contact;
    private int deliveryTime;
    private Address address;

    public Supplier(String supplierId, String name, String itemName, double itemPrice, 
                   String contact, int deliveryTime, Address address) {
        this.supplierId = supplierId;
        this.name = name;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.contact = contact;
        this.deliveryTime = deliveryTime;
        this.address = address;
    }
    
    // Getters
    public String getSupplierId() { return supplierId; }
    public String getName() { return name; }
    public String getItemName() { return itemName; }
    public double getItemPrice() { return itemPrice; }
    public String getContact() { return contact; }
    public int getDeliveryTime() { return deliveryTime; }
    public Address getAddress() { return address; }

    // Method to convert to CSV string format
    @Override
    public String toCsvString() {
        return String.join(",",
            supplierId,
            name,
            itemName,
            String.valueOf(itemPrice),
            contact,
            String.valueOf(deliveryTime),
            address.getStreet() + "|" + address.getCity() + "|" + 
            address.getState() + "|" + address.getPostalCode()
        );
    }
    
     @Override
    public String getId() {
        return supplierId;
    }
    
}