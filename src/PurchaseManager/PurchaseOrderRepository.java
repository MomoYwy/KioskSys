package PurchaseManager;

import shared.models.PurchaseOrder;
import shared.utils.FileUtils;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class PurchaseOrderRepository {
    private static final String PO_FILE = "src/database/purchase_orders.txt";
    private static final String HEADER = "PO_ID,PR_ID,Item_ID,Item_Name,Quantity,Item_Price,Total_Price,Date_Created,Date_Required,Supplier_ID,Sales_Manager_ID,Purchase_Manager_ID,Status";

    public String save(PurchaseOrder purchaseOrder) throws RepositoryException {
        try {
            FileUtils.ensureFileExists(PO_FILE);
            ensureHeaderExists();
            
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(PO_FILE, true))) {
                writer.write(purchaseOrder.toCsvString());
                writer.newLine();
            }
            return purchaseOrder.getId();
        } catch (IOException e) {
            throw new RepositoryException("Failed to save purchase order", e);
        }
    }

    public void update(PurchaseOrder purchaseOrder) throws RepositoryException {
        try {
            List<PurchaseOrder> orders = findAll();
            orders = orders.stream()
                    .map(order -> order.getPurchaseOrderId().equals(purchaseOrder.getPurchaseOrderId()) 
                            ? purchaseOrder : order)
                    .collect(Collectors.toList());
            
            saveAll(orders);
        } catch (Exception e) {
            throw new RepositoryException("Failed to update purchase order", e);
        }
    }

    public boolean delete(String purchaseOrderId) throws RepositoryException {
        try {
            List<PurchaseOrder> orders = findAll();
            boolean removed = orders.removeIf(order -> order.getPurchaseOrderId().equals(purchaseOrderId));
            
            if (removed) {
                saveAll(orders);
            }
            return removed;
        } catch (Exception e) {
            throw new RepositoryException("Failed to delete purchase order", e);
        }
    }

    public List<PurchaseOrder> findAll() throws RepositoryException {
        List<PurchaseOrder> orders = new ArrayList<>();
        
        try {
            File file = new File(PO_FILE);
            if (!file.exists()) {
                return orders;
            }

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                boolean headerSkipped = false;
                
                while ((line = reader.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    
                    if (!headerSkipped) {
                        headerSkipped = true;
                        continue;
                    }
                    
                    orders.add(PurchaseOrder.fromCsvString(line));
                }
            }
        } catch (Exception e) {
            throw new RepositoryException("Failed to load purchase orders", e);
        }
        
        return orders;
    }

    public Optional<PurchaseOrder> findById(String id) throws RepositoryException {
        return findAll().stream()
                .filter(order -> order.getPurchaseOrderId().equals(id))
                .findFirst();
    }

    public List<PurchaseOrder> findByStatus(PurchaseOrder.PurchaseOrderStatus status) throws RepositoryException {
        return findAll().stream()
                .filter(order -> order.getStatus() == status)
                .collect(Collectors.toList());
    }

    public String generateNextId() throws RepositoryException {
        try {
            List<PurchaseOrder> orders = findAll();
            int maxId = orders.stream()
                    .mapToInt(order -> {
                        String id = order.getPurchaseOrderId();
                        if (id.startsWith("PO")) {
                            try {
                                return Integer.parseInt(id.substring(2));
                            } catch (NumberFormatException e) {
                                return 0;
                            }
                        }
                        return 0;
                    })
                    .max()
                    .orElse(0);
            
            return "PO" + String.format("%03d", maxId + 1);
        } catch (Exception e) {
            throw new RepositoryException("Failed to generate next ID", e);
        }
    }

    private void saveAll(List<PurchaseOrder> orders) throws RepositoryException {
        try {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(PO_FILE))) {
                writer.write(HEADER);
                writer.newLine();
                
                for (PurchaseOrder order : orders) {
                    writer.write(order.toCsvString());
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            throw new RepositoryException("Failed to save all purchase orders", e);
        }
    }

    private void ensureHeaderExists() throws IOException {
        File file = new File(PO_FILE);
        if (file.length() == 0) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(HEADER);
                writer.newLine();
            }
        }
    }
}