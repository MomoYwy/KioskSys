package shared.models;

import java.io.*;
import java.util.*;
import java.text.*;

public class OrderManager {
    private List<PurchaseOrder> orders;

    public OrderManager() {
        orders = new ArrayList<>();
    }

public void loadOrdersFromFile(String filePath) {
    // Change the date format to match the "dd/MM/yyyy" format in your data
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
        String line;
        // Skip the header line
        br.readLine();

        while ((line = br.readLine()) != null) {
            String[] columns = line.split(",");

            // Parse the dateCreated and dateRequired columns
            Date dateCreated = dateFormat.parse(columns[7]);
            Date dateRequired = dateFormat.parse(columns[8]);

            // Create a new PurchaseOrder object using the parsed Date objects
            PurchaseOrder order = new PurchaseOrder(
                columns[0], columns[1], columns[2], columns[3], Integer.parseInt(columns[4]),
                Double.parseDouble(columns[5]), Double.parseDouble(columns[6]), dateCreated, dateRequired,
                columns[9], columns[10], columns[11], columns[12]
            );

            // Add the order to the orders list
            orders.add(order);
        }
    } catch (IOException | ParseException e) {
        e.printStackTrace();
    }
}
    
    
       public Set<String> getUniquePurchaseManagerIds() {
        Set<String> uniqueManagerIds = new HashSet<>();
        for (PurchaseOrder order : orders) {
            uniqueManagerIds.add(order.getPurchaseManagerId());
        }
        return uniqueManagerIds;
    }

    public List<PurchaseOrder> filterOrdersByPurchaseManager(String purchaseManagerId) {
        List<PurchaseOrder> filteredOrders = new ArrayList<>();
        for (PurchaseOrder order : orders) {
            if (order.getPurchaseManagerId().equals(purchaseManagerId)) {
                filteredOrders.add(order);
            }
        }
        return filteredOrders;
    }

    public List<PurchaseOrder> getAllOrders() {
        return orders;
    }

}

