package ru.practicum.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public final class Pages {
    public static Pageable getPage(int page, int size) {
        return PageRequest.of(page / size, size);
    }

    public static Pageable getSortedPage(int page, int size, Sort sort) {
        return PageRequest.of(page / size, size, sort);
    }
}
