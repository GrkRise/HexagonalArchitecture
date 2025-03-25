package shop.adapter.out.notification;

import shop.domain.model.Order;
import shop.domain.port.out.NotificationService;

public class ConsoleNotificationService implements NotificationService {
    @Override
    public void notifyOrderStatusChanged(Order order) {
        System.out.println("\n[УВЕДОМЛЕНИЕ] Статус заказа #" + order.getId() + " изменен на: " + order.getStatus());
    }
}
