package shop.domain.port.secondary;

import shop.domain.model.Order;

public interface NotificationService {
    void notifyOrderStatusChanged(Order order);
}
