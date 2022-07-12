package com.example.universe.simulator.entityservice.controllers.graphql;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.List;

public abstract class AbstractGraphQLController {

    public static final int DEFAULT_PAGE_NUMBER = 0;
    public static final int DEFAULT_PAGE_SIZE = 20;

    public static PageRequest assemblePageRequest(PageInput pageInput) {
        return pageInput != null ? PageRequest.of(pageInput.page(), pageInput.size(), Sort.by(assembleSortOrders(pageInput)))
                : PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE, Sort.unsorted());
    }

    public static List<Sort.Order> assembleSortOrders(PageInput pageInput) {
        return pageInput.sortOrders() != null ?
                pageInput.sortOrders().stream()
                        .map(s -> new Sort.Order(s.direction(), s.property()))
                        .toList()
                : Collections.emptyList();
    }

    record SortOrder(String property, Sort.Direction direction) {
    }

    record PageInput(Integer page, Integer size, List<SortOrder> sortOrders) {
    }

}
