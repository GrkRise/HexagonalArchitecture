package shop;

import shop.adapter.primary.console.ConsoleOrderManager;
import shop.adapter.secondary.notification.ConsoleNotificationService;
import shop.adapter.secondary.persistence.InMemoryOrderRepository;
import shop.adapter.secondary.persistence.InMemoryProductRepository;
import shop.domain.port.primary.OrderManagementUseCase;
import shop.domain.port.secondary.NotificationService;
import shop.domain.port.secondary.OrderRepository;
import shop.domain.port.secondary.ProductRepository;
import shop.domain.service.OrderService;

public class ShopApplication {
    public static void main(String[] args) {
        // Инициализация исходящих адаптеров
        OrderRepository orderRepository = new InMemoryOrderRepository();
        ProductRepository productRepository = new InMemoryProductRepository();
        NotificationService notificationService = new ConsoleNotificationService();

        // Инициализация сервиса предметной области
        OrderManagementUseCase orderManagementService = new OrderService(
                orderRepository,
                productRepository,
                notificationService
        );

        // Инициализация входящего адаптера
        ConsoleOrderManager consoleUI = new ConsoleOrderManager(orderManagementService);

        // Запуск приложения
        System.out.println("Запуск системы управления заказами");
        consoleUI.start();
    }
}
