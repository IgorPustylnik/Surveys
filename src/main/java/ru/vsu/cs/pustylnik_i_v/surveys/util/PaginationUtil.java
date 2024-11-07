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

        int[] rangeArray = new int[endPage - startPage + 1];
        for (int i = 0; i < rangeArray.length; i++) {
            rangeArray[i] = startPage + i;
        }
        return rangeArray;
    }

}