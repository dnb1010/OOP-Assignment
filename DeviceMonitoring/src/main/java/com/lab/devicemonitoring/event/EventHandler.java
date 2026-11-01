/*Đồng Ngọc Bảo 202418849 */
package com.lab.devicemonitoring.event;

/** Tuong duong delegate EventHandler<TEventArgs> cua C#. */
@FunctionalInterface
public interface EventHandler<T> {
    void handle(Object sender, T args);
}
