/*Đồng Ngọc Bảo 202418849 */
package com.lab.devicemonitoring.event;

/**
 * Phan "event" ma ben ngoai nhin thay: chi duoc dang ky / huy dang ky,
 * KHONG co cach raise. Tuong duong += va -= tren event cua C#.
 */
public interface EventPublisher<T> {
    void subscribe(EventHandler<T> handler);

    void unsubscribe(EventHandler<T> handler);
}
