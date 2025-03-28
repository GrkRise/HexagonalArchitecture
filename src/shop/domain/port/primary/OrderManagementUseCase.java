package shop.domain.port.primary;

import shop.domain.model.Order;
import shop.domain.model.OrderStatus;
import shop.domain.model.Product;

import java.util.List;
import java.util.Optional;

public interface OrderManagementUseCase {
    String createOrder();
    void addProductToOrder(String orderId, String productId, int quantity);
    void removeProductFromOrder(String orderId, int itemIndex);
    void applyDiscount(String orderId, double discountPercent);
    void changeOrderStatus(String orderId, OrderStatus newStatus);
    Optional<Order> getOrder(String orderId);
    List<Order> getAllOrders();
    List<Product> getAvailableProducts();
}