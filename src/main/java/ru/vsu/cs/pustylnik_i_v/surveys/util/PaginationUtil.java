package ru.vsu.cs.pustylnik_i_v.surveys.util;

public class PaginationUtil {

    public static int[] getVisiblePagesNumbers(int current, int visibleAmount, int size) {
        int halfPagesToShow = visibleAmount / 2;

        current = Math.max(1, Math.min(current, size));

        int startPage = Math.max(1, current - halfPagesToShow);
        int endPage = Math.min(size, current + halfPagesToShow);

        if (endPage - startPage + 1 < visibleAmount) {
            if (startPage == 1) {
                endPage = Math.min(size, startPage + visibleAmount - 1);
            } else if (endPage == size) {
                startPage = Math.max(1, endPage - visibleAmount + 1);
            }
        }

        boolean includeFirstPage = startPage > 1;
        boolean includeLastPage = endPage < size;

        int extraPages = (includeFirstPage ? 1 : 0) + (includeLastPage ? 1 : 0);
        int[] rangeArray = new int[endPage - startPage + 1 + extraPages];
        int index = 0;

        if (includeFirstPage) {
            rangeArray[index++] = 1;
        }

        for (int i = startPage; i <= endPage; i++) {
            rangeArray[index++] = i;
        }

        if (includeLastPage) {
            rangeArray[index] = size;
        }

        return rangeArray;
    }


}