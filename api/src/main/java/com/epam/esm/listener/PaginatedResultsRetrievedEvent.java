package com.epam.esm.listener;

import lombok.Builder;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;

@Getter
@Builder
public class PaginatedResultsRetrievedEvent extends ApplicationEvent {
    private final String requestMapping;
    private final UriComponentsBuilder uriBuilder;
    private final HttpServletResponse response;
    private final Integer page;
    private final Integer lastPage;
    private final Integer pageSize;

    public PaginatedResultsRetrievedEvent(String requestMapping, UriComponentsBuilder uriBuilder,
                                          HttpServletResponse response, Integer page,
                                          Integer lastPage, Integer pageSize) {
        super(requestMapping);
        this.requestMapping = requestMapping;
        this.uriBuilder = uriBuilder;
        this.response = response;
        this.page = page;
        this.lastPage = lastPage;
        this.pageSize = pageSize;
    }
}
