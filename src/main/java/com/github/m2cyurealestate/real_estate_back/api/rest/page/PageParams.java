package com.github.m2cyurealestate.real_estate_back.api.rest.page;

import org.springdoc.core.annotations.ParameterObject;

import java.util.Optional;

/**
 * Query params used for route methods returning pages
 *
 * @author Aldric Vitali Silvestre
 */
@ParameterObject
public class PageParams {

    private int page = 0;

    private Optional<Integer> pageSize = Optional.empty();

    public PageParams() {
    }

    public PageParams(int page, Optional<Integer> pageSize) {
        this.page = page;
        this.pageSize = pageSize;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public Optional<Integer> getPageSize() {
        return pageSize;
    }

    public void setPageSize(Optional<Integer> pageSize) {
        this.pageSize = pageSize;
    }
}
