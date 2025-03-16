package shop;

import shop.adapter.in.console.ConsoleOrderManager;
import shop.adapter.out.notification.ConsoleNotificationService;
import shop.adapter.out.persistence.InMemoryOrderRepository;
import shop.adapter.out.persistence.InMemoryProductRepository;
import shop.domain.port.in.OrderManagementUseCase;
import shop.domain.port.out.NotificationService;
import shop.domain.port.out.OrderRepository;
import shop.domain.port.out.ProductRepository;
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
