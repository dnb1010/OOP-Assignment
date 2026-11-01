/*Đồng Ngọc Bảo 202418849 */
package com.lab.devicemonitoring.event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Tuong duong AggregateException cua C#. */
public class HandlerFailureException extends RuntimeException {
    private final List<Throwable> errors;

    public HandlerFailureException(String message, List<Throwable> errors) {
        super(message);
        this.errors = Collections.unmodifiableList(new ArrayList<>(errors));
        errors.forEach(this::addSuppressed);
    }

    public List<Throwable> getErrors() {
        return errors;
    }
}
