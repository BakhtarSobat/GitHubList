package com.bsobat.github.dto;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bsobat.github.exception.AppException;

import static com.bsobat.github.dto.Resource.Status.ERROR;
import static com.bsobat.github.dto.Resource.Status.LOADING;
import static com.bsobat.github.dto.Resource.Status.SUCCESS;

public class Resource<T> {
    public enum Status {
        SUCCESS, ERROR, LOADING
    }

    private final Status status;
    private final T data;
    private final AppException exception;

    private Resource(@NonNull Status status, @Nullable T data, @Nullable AppException exception) {
        this.status = status;
        this.data = data;
        this.exception = exception;
    }

    public Status getStatus() {
        return status;
    }

    public T getData() {
        return data;
    }

    public AppException getException() {
        return exception;
    }

    public static <T> Resource<T> success(@NonNull T data) {
        return new Resource<>(SUCCESS, data, null);
    }

    public static <T> Resource<T> error(AppException exception, @Nullable T data) {
        return new Resource<>(ERROR, data, exception);
    }

    public static <T> Resource<T> loading(@Nullable T data) {
        return new Resource<>(LOADING, data, null);
    }
}