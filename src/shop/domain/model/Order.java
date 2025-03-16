package shop.domain.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Order {
    private String id;
    private List<OrderItem> items;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private double discount;

    public Order() {
        this.id = UUID.randomUUID().toString();
        this.items = new ArrayList<>();
        this.status = OrderStatus.CREATED;
        this.createdAt = LocalDateTime.now();
        this.discount = 0;
    }

    public String getId() {
        return id;
    }

    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public OrderStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        if (discount < 0 || discount > 100) {
            throw new IllegalArgumentException("Скидка должна быть от 0 до 100 процентов");
        }
        this.discount = discount;
    }

    public void addItem(Product product, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Количество товара должно быть положительным числом");
        }
        items.add(new OrderItem(product, quantity));
    }

    public void removeItem(int index) {
        if (index < 0 || index >= items.size()) {
            throw new IllegalArgumentException("Некорректный индекс товара");
        }
        items.remove(index);
    }

    public void changeStatus(OrderStatus newStatus) {
        // Проверка допустимости перехода статуса
        if (!isValidStatusTransition(this.status, newStatus)) {
            throw new IllegalStateException("Недопустимый переход статуса: " + this.status + " -> " + newStatus);
        }
        this.status = newStatus;
    }

    private boolean isValidStatusTransition(OrderStatus current, OrderStatus next) {
        switch (current) {
            case CREATED:
                return next == OrderStatus.PAID;
            case PAID:
                return next == OrderStatus.SHIPPED;
            case SHIPPED:
                return next == OrderStatus.DELIVERED;
            case DELIVERED:
                return false; // Финальный статус
            default:
                return false;
        }
    }

    public double calculateTotalPrice() {
        double total = items.stream().mapToDouble(OrderItem::getTotalPrice).sum();
        double discountAmount = total * (discount / 100);
        return total - discountAmount;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Заказ #").append(id).append("\n");
        sb.append("Статус: ").append(status).append("\n");
        sb.append("Создан: ").append(createdAt).append("\n");
        sb.append("Товары:\n");

        for (int i = 0; i < items.size(); i++) {
            sb.append("  ").append(i + 1).append(". ").append(items.get(i)).append("\n");
        }

        sb.append("Скидка: ").append(discount).append("%\n");
        sb.append("Итого: ").append(calculateTotalPrice()).append(" руб.");

        return sb.toString();
    }
}
