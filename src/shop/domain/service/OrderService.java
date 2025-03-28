package shop.domain.service;

import shop.domain.model.Order;
import shop.domain.model.OrderStatus;
import shop.domain.model.Product;
import shop.domain.port.primary.OrderManagementUseCase;
import shop.domain.port.secondary.NotificationService;
import shop.domain.port.secondary.OrderRepository;
import shop.domain.port.secondary.ProductRepository;

import java.util.List;
import java.util.Optional;

public class OrderService implements OrderManagementUseCase {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final NotificationService notificationService;

    public OrderService(
            OrderRepository orderRepository,
            ProductRepository productRepository,
            NotificationService notificationService) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.notificationService = notificationService;
    }

    @Override
    public String createOrder() {
        Order newOrder = new Order();
        orderRepository.save(newOrder);
        return newOrder.getId();
    }

    @Override
    public void addProductToOrder(String orderId, String productId, int quantity) {
        Order order = getOrderOrThrow(orderId);
        Product product = getProductOrThrow(productId);

        order.addItem(product, quantity);
        orderRepository.save(order);
    }

    @Override
    public void removeProductFromOrder(String orderId, int itemIndex) {
        Order order = getOrderOrThrow(orderId);

        order.removeItem(itemIndex);
        orderRepository.save(order);
    }

    @Override
    public void applyDiscount(String orderId, double discountPercent) {
        Order order = getOrderOrThrow(orderId);

        order.setDiscount(discountPercent);
        orderRepository.save(order);
    }

    @Override
    public void changeOrderStatus(String orderId, OrderStatus newStatus) {
        Order order = getOrderOrThrow(orderId);

        order.changeStatus(newStatus);
        orderRepository.save(order);

        // Отправка уведомления о смене статуса
        notificationService.notifyOrderStatusChanged(order);
    }

    @Override
    public Optional<Order> getOrder(String orderId) {
        return orderRepository.findById(orderId);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public List<Product> getAvailableProducts() {
        return productRepository.findAll();
    }

    private Order getOrderOrThrow(String orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Заказ не найден: " + orderId));
    }

    private Product getProductOrThrow(String productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Товар не найден: " + productId));
    }
}
