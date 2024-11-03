package ru.vsu.cs.pustylnik_i_v.surveys.database.simulation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DBTableSimulation<T> {
    private static final int DEFAULT_SIZE = 1000;
    private final T[] data;
    private int currentIndex = 1;
    private final Function<Object[], T> constructorFunction;

    public DBTableSimulation(Function<Object[], T> constructorFunction) {
        data = (T[]) new Object[DEFAULT_SIZE];
        this.constructorFunction = constructorFunction;
    }

    public int add(Object... args) {
        if (currentIndex >= data.length) {
            throw new RuntimeException("Index out of bounds");
        } else {
            T newInstance = constructorFunction.apply(args);
            try {
                Method setIdMethod = newInstance.getClass().getMethod("setId", int.class);
                setIdMethod.invoke(newInstance, currentIndex);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {
            }
            data[currentIndex] = newInstance;
            currentIndex++;
        }
        return currentIndex - 1;
    }

    public void remove(List<DBTableSimulationFilter<T>> filters) {
        for (int i = 0; i < currentIndex; i++) {
            T element = data[i];
            if (element != null && matchesFilters(element, filters)) {
                data[i] = null;
                return;
            }
        }
        throw new RuntimeException("Element with specified keys not found");
    }

    public List<T> get(List<DBTableSimulationFilter<T>> filters) {
        List<T> result = new ArrayList<>();
        for (int i = 0; i < currentIndex; i++) {
            T element = data[i];
            if (element != null && matchesFilters(element, filters)) {
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

    public boolean contains(List<DBTableSimulationFilter<T>> filters) {
        for (int i = 0; i < currentIndex; i++) {
            T element = data[i];
            if (element != null && matchesFilters(element, filters)) {
                return true;
            }
        }
        return false;
    }

    public int size() {
        return currentIndex;
    }

    private boolean matchesFilters(T element, List<DBTableSimulationFilter<T>> filters) {
        for (DBTableSimulationFilter<T> filter : filters) {
            if (!filter.keyFunction().apply(element).equals(filter.key())) {
                return false;
            }
        }
        return true;
    }
}
