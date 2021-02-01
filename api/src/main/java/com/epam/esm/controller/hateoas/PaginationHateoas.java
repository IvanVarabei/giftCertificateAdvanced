package com.epam.esm.controller.hateoas;

import com.epam.esm.dto.CustomPage;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class PaginationHateoas<T> {
    public void addPaginationLinks(UriComponentsBuilder uriBuilder, CustomPage<T> customPage) {
        int page = customPage.getNumber();
        long lastPage = customPage.getTotalPages() - 1;
        int pageSize = customPage.getSize();
        String uriCurrentPage = constructCurrentPageUri(uriBuilder, page, pageSize);
        customPage.add(Link.of(uriCurrentPage).withSelfRel());
        if (hasNextPage(page, lastPage)) {
            String uriForNextPage = constructNextPageUri(uriBuilder, page, pageSize);
            customPage.add(Link.of(uriForNextPage).withRel("next"));
        }
        if (hasPreviousPage(page)) {
            String uriForPrevPage = constructPrevPageUri(uriBuilder, page, pageSize);
            customPage.add(Link.of(uriForPrevPage).withRel("prev"));
        }
        if (hasFirstPage(page)) {
            String uriForFirstPage = constructFirstPageUri(uriBuilder, pageSize);
            customPage.add(Link.of(uriForFirstPage).withRel("first"));
        }
        if (hasLastPage(page, lastPage)) {
            String uriForLastPage = constructLastPageUri(uriBuilder, lastPage, pageSize);
            customPage.add(Link.of(uriForLastPage).withRel("last"));
        }
    }

    private String constructCurrentPageUri(UriComponentsBuilder uriBuilder, int page, int size) {
        return uriBuilder
                .replaceQueryParam("page", page)
                .replaceQueryParam("size", size)
                .build().encode().toUriString();
    }

    private String constructNextPageUri(UriComponentsBuilder uriBuilder, int page, int size) {
        return uriBuilder
                .replaceQueryParam("page", page + 1)
                .replaceQueryParam("size", size)
                .build().encode().toUriString();
    }

    private String constructPrevPageUri(UriComponentsBuilder uriBuilder, int page, int size) {
        return uriBuilder
                .replaceQueryParam("page", page - 1)
                .replaceQueryParam("size", size)
                .build().encode().toUriString();
    }

    private String constructFirstPageUri(UriComponentsBuilder uriBuilder, int size) {
        return uriBuilder
                .replaceQueryParam("page", 0)
                .replaceQueryParam("size", size)
                .build().encode().toUriString();
    }

    private String constructLastPageUri(UriComponentsBuilder uriBuilder, long lastPage, int size) {
        return uriBuilder
                .replaceQueryParam("page", lastPage)
                .replaceQueryParam("size", size)
                .build().encode().toUriString();
    }

    private boolean hasNextPage(int page, long lastPage) {
        return page < lastPage;
    }

    private boolean hasPreviousPage(int page) {
        return page > 0;
    }

    private boolean hasFirstPage(int page) {
        return hasPreviousPage(page);
    }

    private boolean hasLastPage(int page, long lastPage) {
        return lastPage > 0 && hasNextPage(page, lastPage);
    }
}