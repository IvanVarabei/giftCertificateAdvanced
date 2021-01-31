package com.epam.esm.dto;

import com.epam.esm.dto.search.SortByField;
import com.epam.esm.dto.search.SortOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
public class SearchCertificateDto {
    private List<String> tagNames;
    private String name;
    private String description;
    private SortByField sortByField;
    private SortOrder sortOrder;
    private Pageable pageRequest;
}
