package shop.adapter.primary.console;

import shop.domain.model.Order;
import shop.domain.model.OrderStatus;
import shop.domain.model.Product;
import shop.domain.port.primary.OrderManagementUseCase;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ConsoleOrderManager {
    private final OrderManagementUseCase orderManagement;
    private final Scanner scanner;
    private String currentOrderId;

    public ConsoleOrderManager(OrderManagementUseCase orderManagement) {
        this.orderManagement = orderManagement;
        this.scanner = new Scanner(System.in);
        this.currentOrderId = null;
    }

    public void start() {
        int choice;
        do {
            showMainMenu();
            choice = readIntInput();
            scanner.nextLine(); // очистка буфера
            handleMainMenuChoice(choice);
        } while (choice != 0);
    }

    private void showMainMenu() {
        System.out.println("\n===== Система управления заказами =====");
        if (currentOrderId != null) {
            System.out.println("Текущий заказ: #" + currentOrderId);
        }
        System.out.println("1. Создать новый заказ");
        System.out.println("2. Выбрать существующий заказ");
        System.out.println("3. Показать все заказы");

        if (currentOrderId != null) {
            System.out.println("4. Добавить товар в текущий заказ");
            System.out.println("5. Удалить товар из текущего заказа");
            System.out.println("6. Применить скидку к текущему заказу");
            System.out.println("7. Изменить статус текущего заказа");
            System.out.println("8. Показать детали текущего заказа");
        }

        System.out.println("0. Выход");
        System.out.print("Выберите действие: ");
    }

    private void handleMainMenuChoice(int choice) {
        switch (choice) {
            case 0:
                System.out.println("Выход из программы...");
                break;
            case 1:
                createNewOrder();
                break;
            case 2:
                selectExistingOrder();
                break;
            case 3:
                showAllOrders();
                break;
            case 4:
                if (currentOrderId != null) addProductToOrder();
                else showNoOrderSelectedMessage();
                break;
            case 5:
                if (currentOrderId != null) removeProductFromOrder();
                else showNoOrderSelectedMessage();
                break;
            case 6:
                if (currentOrderId != null) applyDiscount();
                else showNoOrderSelectedMessage();
                break;
            case 7:
                if (currentOrderId != null) changeOrderStatus();
                else showNoOrderSelectedMessage();
                break;
            case 8:
                if (currentOrderId != null) showOrderDetails();
                else showNoOrderSelectedMessage();
                break;
            default:
                System.out.println("Неверный выбор. Попробуйте снова.");
        }
    }

    private void createNewOrder() {
        currentOrderId = orderManagement.createOrder();
        System.out.println("Создан новый заказ #" + currentOrderId);
    }

    private void selectExistingOrder() {
        List<Order> orders = orderManagement.getAllOrders();

        if (orders.isEmpty()) {
            System.out.println("Нет доступных заказов.");
            return;
        }

        System.out.println("\n=== Доступные заказы ===");
        for (int i = 0; i < orders.size(); i++) {
            Order order = orders.get(i);
            System.out.println((i + 1) + ". Заказ #" + order.getId() + " (" + order.getStatus() + ")");
        }

        System.out.print("Выберите номер заказа (1-" + orders.size() + "): ");
        int orderIndex = readIntInput() - 1;
        scanner.nextLine(); // очистка буфера

        if (orderIndex >= 0 && orderIndex < orders.size()) {
            currentOrderId = orders.get(orderIndex).getId();
            System.out.println("Выбран заказ #" + currentOrderId);
        } else {
            System.out.println("Некорректный выбор заказа.");
        }
    }

    private void showAllOrders() {
        List<Order> orders = orderManagement.getAllOrders();

        if (orders.isEmpty()) {
            System.out.println("Нет доступных заказов.");
            return;
        }

        System.out.println("\n=== Все заказы ===");
        for (Order order : orders) {
            System.out.println("Заказ #" + order.getId());
            System.out.println("Статус: " + order.getStatus());
            System.out.println("Сумма: " + order.calculateTotalPrice() + " руб.");
            System.out.println("Товары: " + order.getItems().size());
            System.out.println("-----------------------");
        }
    }

    private void addProductToOrder() {
        List<Product> products = orderManagement.getAvailableProducts();

        if (products.isEmpty()) {
            System.out.println("Нет доступных товаров.");
            return;
        }

        System.out.println("\n=== Доступные товары ===");
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            System.out.println((i + 1) + ". " + product);
        }

        System.out.print("Выберите номер товара (1-" + products.size() + "): ");
        int productIndex = readIntInput() - 1;
        scanner.nextLine(); // очистка буфера

        if (productIndex < 0 || productIndex >= products.size()) {
            System.out.println("Некорректный выбор товара.");
            return;
        }

        System.out.print("Введите количество: ");
        int quantity = readIntInput();
        scanner.nextLine(); // очистка буфера

        try {
            String productId = products.get(productIndex).getId();
            orderManagement.addProductToOrder(currentOrderId, productId, quantity);
            System.out.println("Товар успешно добавлен в заказ.");
        } catch (Exception e) {
            System.out.println("Ошибка при добавлении товара: " + e.getMessage());
        }
    }

    private void removeProductFromOrder() {
        Optional<Order> orderOpt = orderManagement.getOrder(currentOrderId);

        if (!orderOpt.isPresent()) {
            System.out.println("Заказ не найден.");
            return;
        }

        Order order = orderOpt.get();

        if (order.getItems().isEmpty()) {
            System.out.println("В заказе нет товаров.");
            return;
        }

        System.out.println("\n=== Товары в заказе ===");
        for (int i = 0; i < order.getItems().size(); i++) {
            System.out.println((i + 1) + ". " + order.getItems().get(i));
        }

        System.out.print("Выберите номер товара для удаления (1-" + order.getItems().size() + "): ");
        int itemIndex = readIntInput() - 1;
        scanner.nextLine(); // очистка буфера

        try {
            orderManagement.removeProductFromOrder(currentOrderId, itemIndex);
            System.out.println("Товар успешно удален из заказа.");
        } catch (Exception e) {
            System.out.println("Ошибка при удалении товара: " + e.getMessage());
        }
    }

    private void applyDiscount() {
        System.out.print("Введите процент скидки (0-100): ");
        double discount = readDoubleInput();
        scanner.nextLine(); // очистка буфера

        try {
            orderManagement.applyDiscount(currentOrderId, discount);
            System.out.println("Скидка успешно применена.");
        } catch (Exception e) {
            System.out.println("Ошибка при применении скидки: " + e.getMessage());
        }
    }

    private void changeOrderStatus() {
        Optional<Order> orderOpt = orderManagement.getOrder(currentOrderId);

        if (!orderOpt.isPresent()) {
            System.out.println("Заказ не найден.");
            return;
        }

        Order order = orderOpt.get();
        OrderStatus currentStatus = order.getStatus();

        System.out.println("\nТекущий статус заказа: " + currentStatus);

        OrderStatus[] availableStatuses = OrderStatus.values();
        System.out.println("Доступные статусы:");
        for (int i = 0; i < availableStatuses.length; i++) {
            System.out.println((i + 1) + ". " + availableStatuses[i]);
        }

        System.out.print("Выберите новый статус (1-" + availableStatuses.length + "): ");
        int statusIndex = readIntInput() - 1;
        scanner.nextLine(); // очистка буфера

        if (statusIndex < 0 || statusIndex >= availableStatuses.length) {
            System.out.println("Некорректный выбор статуса.");
            return;
        }

        OrderStatus newStatus = availableStatuses[statusIndex];

        try {
            orderManagement.changeOrderStatus(currentOrderId, newStatus);
            System.out.println("Статус заказа успешно изменен на: " + newStatus);
        } catch (Exception e) {
            System.out.println("Ошибка при изменении статуса: " + e.getMessage());
        }
    }

    private void showOrderDetails() {
        Optional<Order> orderOpt = orderManagement.getOrder(currentOrderId);

        if (!orderOpt.isPresent()) {
            System.out.println("Заказ не найден.");
            return;
        }

        Order order = orderOpt.get();
        System.out.println("\n" + order);
    }

    private void showNoOrderSelectedMessage() {
        System.out.println("Сначала необходимо создать или выбрать заказ.");
    }

    private int readIntInput() {
        try {
            return scanner.nextInt();
        } catch (Exception e) {
            return -1;
        }
    }

    private double readDoubleInput() {
        try {
            return scanner.nextDouble();
        } catch (Exception e) {
            return -1;
        }
    }
}