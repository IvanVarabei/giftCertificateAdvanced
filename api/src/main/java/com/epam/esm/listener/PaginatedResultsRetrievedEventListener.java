package com.epam.esm.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;

@Component
public class PaginatedResultsRetrievedEventListener implements ApplicationListener<PaginatedResultsRetrievedEvent> {
    @Override
    public void onApplicationEvent(PaginatedResultsRetrievedEvent ev) {
        addLinkHeaderOnPagedResourceRetrieval(
                ev.getUriBuilder(), ev.getResponse(),
                ev.getRequestMapping(), ev.getPage(),
                ev.getLastPage(), ev.getPageSize());
    }

    private void addLinkHeaderOnPagedResourceRetrieval(UriComponentsBuilder uriBuilder,
                                                       HttpServletResponse response, String requestMapping,
                                                       int page, int lastPage, int pageSize) {
        uriBuilder.path(requestMapping);
        StringBuilder linkHeader = new StringBuilder();
        if (hasNextPage(page, lastPage)) {
            String uriForNextPage = constructNextPageUri(uriBuilder, page, pageSize);
            linkHeader.append(createLinkHeader(uriForNextPage, "next"));
        }
        if (hasPreviousPage(page)) {
            String uriForPrevPage = constructPrevPageUri(uriBuilder, page, pageSize);
            appendCommaIfNecessary(linkHeader);
            linkHeader.append(createLinkHeader(uriForPrevPage, "prev"));
        }
        if (hasFirstPage(page)) {
            String uriForFirstPage = constructFirstPageUri(uriBuilder, pageSize);
            appendCommaIfNecessary(linkHeader);
            linkHeader.append(createLinkHeader(uriForFirstPage, "first"));
        }
        if (hasLastPage(page, lastPage)) {
            String uriForLastPage = constructLastPageUri(uriBuilder, lastPage, pageSize);
            appendCommaIfNecessary(linkHeader);
            linkHeader.append(createLinkHeader(uriForLastPage, "last"));
        }
        response.addHeader("Link", linkHeader.toString());
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

    private String constructLastPageUri(UriComponentsBuilder uriBuilder, int lastPage, int size) {
        return uriBuilder
                .replaceQueryParam("page", lastPage)
                .replaceQueryParam("size", size)
                .build().encode().toUriString();
    }

    private boolean hasNextPage(int page, int lastPage) {
        return page < lastPage;
    }

    private boolean hasPreviousPage(int page) {
        return page > 0;
    }

    private boolean hasFirstPage(int page) {
        return hasPreviousPage(page);
    }

    private boolean hasLastPage(int page, int lastPage) {
        return lastPage > 0 && hasNextPage(page, lastPage);
    }

    private void appendCommaIfNecessary(StringBuilder linkHeader) {
        if (linkHeader.length() > 0) {
            linkHeader.append(", ");
        }
    }

    private String createLinkHeader(String uri, String rel) {
        return String.format("<%s>; rel=\"%s\"", uri, rel);
    }
}