package com.epam.esm.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import java.util.Collection;

@EqualsAndHashCode(callSuper = true)
@Data
public class CustomPage<T> extends RepresentationModel<CustomPage<?>> {
    private Collection<T> content;
    private Integer size;
    private Long totalElements;
    private Long totalPages;
    private Integer number;

    public CustomPage(Collection<T> content, CustomPageable pageRequest, Long total) {
        this.content = content;
        size = pageRequest.getSize();
        totalElements = total;
        totalPages = (total + size - 1) / size;
        number = pageRequest.getPage();
    }
}
