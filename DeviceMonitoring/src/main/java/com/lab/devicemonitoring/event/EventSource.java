/*Đồng Ngọc Bảo 202418849 */
package com.lab.devicemonitoring.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Phan "event" ma publisher giu: co the raise.
 * Publisher phai khai bao field kieu nay la private va chi lo ra EventPublisher.
 */
public final class EventSource<T> implements EventPublisher<T> {
    private final List<EventHandler<T>> handlers = new CopyOnWriteArrayList<>();

    @Override
    public void subscribe(EventHandler<T> handler) {
        handlers.add(Objects.requireNonNull(handler));
    }

    @Override
    public void unsubscribe(EventHandler<T> handler) {
        handlers.remove(handler); // so sanh equals => phai dung CUNG instance da dang ky
    }

    /**
     * Goi tung handler rieng le: mot subscriber loi khong lam cac subscriber khac mat su kien.
     * Sau khi chay het, neu co loi thi nem HandlerFailureException (khong nuot loi am tham).
     */
    public void raise(Object sender, T args, String ownerId) {
        List<Throwable> errors = null;

        for (EventHandler<T> handler : handlers) {
            try {
                handler.handle(sender, args);
            } catch (RuntimeException ex) {
                if (errors == null) {
                    errors = new ArrayList<>();
                }
                errors.add(ex);
            }
        }

        if (errors != null) {
            throw new HandlerFailureException(
                    errors.size() + " subscriber(s) failed while handling an event of device " + ownerId + ".",
                    errors);
        }
    }
}
