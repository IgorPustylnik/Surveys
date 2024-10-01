package ru.vsu.cs.pustylnik_i_v.surveys.database;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DBTableImitation<T> {
    private final T[] data;
    private int currentIndex = 0;
    private final Function<Object[], T> constructorFunction;

    public DBTableImitation(int size, Function<Object[], T> constructorFunction) {
        data = (T[]) new Object[size];
        this.constructorFunction = constructorFunction;
    }

    public void add(Object... args) {
        if (currentIndex >= data.length) {
            throw new RuntimeException("Index out of bounds");
        } else {
            T newInstance = constructorFunction.apply(args);
            try {
                Method setIdMethod = newInstance.getClass().getMethod("setId", Integer.class);
                setIdMethod.invoke(newInstance, currentIndex);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {
            }
            data[currentIndex] = newInstance;
            currentIndex++;
        }
    }

    public void remove(Function<T, ?> keyFunction, Object key) {
        for (int i = 0; i < currentIndex; i++) {
            T element = data[i];
            if (element != null && keyFunction.apply(element).equals(key)) {
                data[i] = null;
                return;
            }
        }
        throw new RuntimeException("Element with key " + key + " not found");
    }

    public List<T> get(Function<T, ?> keyFunction, Object key) {
        List<T> result = new ArrayList<>();
        for (int i = 0; i < currentIndex; i++) {
            T element = data[i];
            if (element != null && keyFunction.apply(element).equals(key)) {
                result.add(element);
            }
        }
        return result;
    }

    public List<T> getAll() {
        return Arrays.stream(data)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public boolean contains(Function<T, ?> keyFunction, Object key) {
        for (int i = 0; i < currentIndex; i++) {
            T element = data[i];
            if (element != null && keyFunction.apply(element).equals(key)) {
                return true;
            }
        }
        return false;
    }
}