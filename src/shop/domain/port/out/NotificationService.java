package shop.domain.port.out;

import shop.domain.model.Order;

public interface NotificationService {
    void notifyOrderStatusChanged(Order order);
}
