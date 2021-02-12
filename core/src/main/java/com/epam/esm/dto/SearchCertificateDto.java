package com.epam.esm.dto;

import com.epam.esm.dto.search.SortByField;
import com.epam.esm.dto.search.SortOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchCertificateDto {
    private List<String> tagNames;
    private String name;
    private String description;
    private SortByField sortByField;
    private SortOrder sortOrder;
    private CustomPageable pageRequest;
}
