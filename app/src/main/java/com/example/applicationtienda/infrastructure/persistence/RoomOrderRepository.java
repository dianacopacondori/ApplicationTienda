package com.example.applicationtienda.infrastructure.persistence;


import com.example.applicationtienda.domain.model.CartItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class RoomOrderRepository {

    private final OrderDao orderDao;
    private final OrderItemDao orderItemDao;

    public RoomOrderRepository(AppDatabase db) {
        orderDao = db.orderDao();
        orderItemDao = db.orderItemDao();
    }

    public void guardarPedido(String userId,
                              String metodoPago,
                              List<CartItem> items,
                              double total) {

        String orderId = UUID.randomUUID().toString();

        String fecha = new SimpleDateFormat(
                "dd/MM/yyyy HH:mm",
                Locale.getDefault()
        ).format(new Date());

        OrderEntity order = new OrderEntity(
                orderId,
                userId,
                fecha,
                metodoPago,
                total,
                "COMPLETADO"
        );

        orderDao.insertOrder(order);

        List<OrderItemEntity> detalles = new ArrayList<>();

        for (CartItem item : items) {

            detalles.add(
                    new OrderItemEntity(
                            UUID.randomUUID().toString(),
                            orderId,
                            item.getProduct().getId(),
                            item.getProduct().getName(),
                            item.getQuantity(),
                            item.getProduct().getPrice()
                    )
            );
        }

        orderItemDao.insertOrderItems(detalles);
    }
}
