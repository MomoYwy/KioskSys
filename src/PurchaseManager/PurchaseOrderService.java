package PurchaseManager;

import shared.models.PurchaseOrder;
import java.util.List;
import java.util.Optional;

public class PurchaseOrderService {
    private final PurchaseOrderRepository repository;
    private final PurchaseOrderValidator validator;

    public PurchaseOrderService() {
        this.repository = new PurchaseOrderRepository();
        this.validator = new PurchaseOrderValidator();
    }

    public PurchaseOrderService(PurchaseOrderRepository repository, PurchaseOrderValidator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    public String createPurchaseOrder(PurchaseOrder purchaseOrder) throws ServiceException {
        try {
            validator.validateForCreation(purchaseOrder);
            return repository.save(purchaseOrder);
        } catch (Exception e) {
            throw new ServiceException("Failed to create purchase order: " + e.getMessage(), e);
        }
    }

    public void updatePurchaseOrder(PurchaseOrder purchaseOrder) throws ServiceException {
        try {
            validator.validateForUpdate(purchaseOrder);
            if (!purchaseOrder.canBeEdited()) {
                throw new ServiceException("Purchase order cannot be edited in current status");
            }
            repository.update(purchaseOrder);
        } catch (Exception e) {
            throw new ServiceException("Failed to update purchase order: " + e.getMessage(), e);
        }
    }

    public boolean deletePurchaseOrder(String purchaseOrderId) throws ServiceException {
        try {
            Optional<PurchaseOrder> order = repository.findById(purchaseOrderId);
            if (order.isPresent()) {
                if (!order.get().canBeDeleted()) {
                    throw new ServiceException("Purchase order cannot be deleted in current status");
                }
                return repository.delete(purchaseOrderId);
            }
            return false;
        } catch (Exception e) {
            throw new ServiceException("Failed to delete purchase order: " + e.getMessage(), e);
        }
    }

    public List<PurchaseOrder> getAllPurchaseOrders() throws ServiceException {
        try {
            return repository.findAll();
        } catch (Exception e) {
            throw new ServiceException("Failed to retrieve purchase orders: " + e.getMessage(), e);
        }
    }

    public List<PurchaseOrder> getPurchaseOrdersByStatus(PurchaseOrder.PurchaseOrderStatus status) throws ServiceException {
        try {
            return repository.findByStatus(status);
        } catch (Exception e) {
            throw new ServiceException("Failed to retrieve purchase orders by status: " + e.getMessage(), e);
        }
    }

    public Optional<PurchaseOrder> getPurchaseOrderById(String id) throws ServiceException {
        try {
            return repository.findById(id);
        } catch (Exception e) {
            throw new ServiceException("Failed to retrieve purchase order: " + e.getMessage(), e);
        }
    }

    public void approvePurchaseOrder(String purchaseOrderId) throws ServiceException {
        try {
            Optional<PurchaseOrder> orderOpt = repository.findById(purchaseOrderId);
            if (orderOpt.isPresent()) {
                PurchaseOrder order = orderOpt.get();
                order.approve();
                repository.update(order);
            } else {
                throw new ServiceException("Purchase order not found");
            }
        } catch (Exception e) {
            throw new ServiceException("Failed to approve purchase order: " + e.getMessage(), e);
        }
    }

    public void rejectPurchaseOrder(String purchaseOrderId) throws ServiceException {
        try {
            Optional<PurchaseOrder> orderOpt = repository.findById(purchaseOrderId);
            if (orderOpt.isPresent()) {
                PurchaseOrder order = orderOpt.get();
                order.reject();
                repository.update(order);
            } else {
                throw new ServiceException("Purchase order not found");
            }
        } catch (Exception e) {
            throw new ServiceException("Failed to reject purchase order: " + e.getMessage(), e);
        }
    }

    public String generateNextPurchaseOrderId() throws ServiceException {
        try {
            return repository.generateNextId();
        } catch (Exception e) {
            throw new ServiceException("Failed to generate purchase order ID: " + e.getMessage(), e);
        }
    }
}