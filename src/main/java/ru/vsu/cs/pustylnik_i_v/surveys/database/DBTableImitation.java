package ru.vsu.cs.pustylnik_i_v.surveys.database;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Function;

public class DBTableImitation<T, K> {
    private final T[] data;
    private int currentIndex = 0;
    private final Function<Object[], T> constructorFunction;
    private final Function<T, K> keyExtractor;

    @SuppressWarnings("unchecked")
    public DBTableImitation(int size, Function<Object[], T> constructorFunction, Function<T, K> keyExtractor) {
        data = (T[]) new Object[size];
        this.constructorFunction = constructorFunction;
        this.keyExtractor = keyExtractor;
    }

    public void add(Object... args) {
        if (currentIndex >= data.length) {
            throw new RuntimeException("Index out of bounds");
        } else {
            T newInstance = constructorFunction.apply(args);

            try {
                Method setIdMethod = newInstance.getClass().getMethod("setId", Integer.class);
                setIdMethod.invoke(newInstance, currentIndex);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {}

            data[currentIndex] = newInstance;
            currentIndex++;
        }
    }


    public void remove(K key) {
        for (int i = 0; i < currentIndex; i++) {
            T element = data[i];
            if (element != null && keyExtractor.apply(element).equals(key)) {
                data[i] = null;
                return;
            }
        }
        throw new RuntimeException("Element with key " + key + " not found");
    }


    public T get(K key) {
        for (int i = 0; i < currentIndex; i++) {
            T element = data[i];
            if (element != null && keyExtractor.apply(element).equals(key)) {
                return element;
            }
        }
        return null;
    }

    public boolean containsKey(K key) {
        for (int i = 0; i < currentIndex; i++) {
            T element = data[i];
            if (element != null && keyExtractor.apply(element).equals(key)) {
                return true;
            }
        }
        return false;
    }
}