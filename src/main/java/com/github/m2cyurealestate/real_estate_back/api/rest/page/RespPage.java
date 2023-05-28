package com.github.m2cyurealestate.real_estate_back.api.rest.page;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;

/**
 * Create a page response containing the wanted data
 *
 * @param <T> the type of elements in the page
 */
public class RespPage<T> {

    /**
     * The total number of elements that can be returned.
     */
    private final long totalCount;

    /**
     * The number of elements in this page
     */
    private final long size;

    /**
     * The index of the page, 0 based
     */
    private final long pageNumber;

    /**
     * The total number of pages
     */
    private final long pageCount;

    private final List<T> content;

    /**
     * Create the page by converting the page return using the mapping function
     *
     * @param page   the page to convert
     * @param mapper the function to apply to all elements to
     * @param <U>    The base type of the page
     */
    public <U> RespPage(Page<U> page, Function<U, T> mapper) {
        this.content = page.getContent()
                .stream()
                .map(mapper)
                .toList();
        this.pageCount = page.getTotalPages();
        this.pageNumber = page.getNumber();
        this.size = page.getSize();
        this.totalCount = page.getTotalElements();
    }

    /**
     * Create the page by converting the page return using the mapping function.
     * <p>
     * This is an alternative version of {@link #RespPage(Page, Function)}.
     *
     * @param page   the page to convert
     * @param mapper the function to apply to all elements to
     * @param <U>    The base type of the page
     */
    public static <U, T> RespPage<T> of(Page<U> page, Function<U, T> mapper) {
        return new RespPage<>(page, mapper);
    }

    public long getTotalCount() {
        return totalCount;
    }

    public long getSize() {
        return size;
    }

    public long getPageNumber() {
        return pageNumber;
    }

    public long getPageCount() {
        return pageCount;
    }

    public List<T> getContent() {
        return content;
    }
}
