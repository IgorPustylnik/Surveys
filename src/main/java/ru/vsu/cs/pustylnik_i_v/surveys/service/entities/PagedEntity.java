package ru.vsu.cs.pustylnik_i_v.surveys.service.entities;

public class PagedEntity<T> {

    private final Integer currentIndex;
    private final Integer size;
    private final T page;

    public PagedEntity(Integer currentIndex, Integer size, T page) {
        this.currentIndex = currentIndex;
        this.size = size;
        this.page = page;
    }

    public Integer getCurrentIndex() {
        return currentIndex;
    }

    public Integer getSize() {
        return size;
    }

    public T getPage() {
        return page;
    }
}
