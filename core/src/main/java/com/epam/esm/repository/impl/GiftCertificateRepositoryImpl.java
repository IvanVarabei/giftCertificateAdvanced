package com.epam.esm.repository.impl;

import com.epam.esm.dto.SearchCertificateDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.repository.GenericRepository;
import com.epam.esm.repository.GiftCertificateRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

import static com.epam.esm.dto.search.SortOrder.DESC;

@Repository
public class GiftCertificateRepositoryImpl extends GenericRepository<GiftCertificate>
        implements GiftCertificateRepository {
    private static final String READ_CERTIFICATES_BASE =
            "select id, name, description, price, duration, created_date, updated_date from gift_certificate " +
                    "where true ";

    private static final String READ_CERTIFICATES_TAGS1 = "and id in (SELECT gift_certificate_id FROM " +
            "certificate_tag LEFT JOIN tag ON tag_id = tag.id WHERE tag.name IN (";

    private static final String READ_CERTIFICATES_TAGS2 = ") " +
            "GROUP BY gift_certificate_id HAVING COUNT(tag_id) = ?)";

    private static final String NAME_FILTER = "and name ilike '%";

    private static final String DESCRIPTION_FILTER = "and description ilike '%";

    private static final String SORT_FIELD = "order by ";

    private static final String COUNT_CERTIFICATES_BASE = "select count(id) from gift_certificate where true ";

    private static final String BLANK = " ";

    public GiftCertificateRepositoryImpl() {
        super(GiftCertificate.class);
    }

    /**
     * select id, name, description, price, duration, create_date, last_update_date
     * from gift_certificate
     * where true
     * and id in (
     * SELECT gift_certificate_id
     * FROM certificate_tag
     * LEFT JOIN tag ON tag_id = tag.id
     * WHERE tag.name IN ('tag_name_1', 'tag_name_2')
     * GROUP BY gift_certificate_id
     * HAVING COUNT(tag_id) = 2)
     * and name ilike '%name_fragment%'
     * and description ilike '%description_fragment%'
     * order by last_update_date
     * desc
     * offset %s limit %s
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<GiftCertificate> findPaginated(SearchCertificateDto searchDto) {
        List queryParams = new ArrayList<>();
        StringBuilder sb = generateSearchQuery(searchDto, READ_CERTIFICATES_BASE, queryParams);
        if (searchDto.getSortByField() != null) {
            sb.append(SORT_FIELD)
                    .append(searchDto.getSortByField())
                    .append(BLANK);
            if (DESC == searchDto.getSortOrder()) {
                sb.append(DESC);
            }
        }
        int size = searchDto.getPageRequest().getSize();
        int page = searchDto.getPageRequest().getPage();
        int offset = size * page;
        Query query = entityManager.createNativeQuery(sb.toString(), GiftCertificate.class);
        for (int i = 0; i < queryParams.size(); i++) {
            Object o = queryParams.get(i);
            query.setParameter(i + 1, o);
        }
        return query.setFirstResult(offset)
                .setMaxResults(size)
                .getResultList();
    }

    /**
     * select count(id) from gift_certificate where true
     * and id in (
     * SELECT gift_certificate_id
     * FROM certificate_tag
     * LEFT JOIN tag ON tag_id = tag.id
     * WHERE tag.name IN ('tag_name_1', 'tag_name_2')
     * GROUP BY gift_certificate_id
     * HAVING COUNT(tag_id) = 2)
     * and name ilike '%name_fragment%'
     * and description ilike '%description_fragment%'
     */
    @Override
    public Long countAll(SearchCertificateDto searchDto) {
        List queryParams = new ArrayList<>();
        StringBuilder sb = generateSearchQuery(searchDto, COUNT_CERTIFICATES_BASE, queryParams);
        Query query = entityManager.createNativeQuery(sb.toString());
        for (int i = 0; i < queryParams.size(); i++) {
            Object o = queryParams.get(i);
            query.setParameter(i + 1, o);
        }
        return ((Number) query.getSingleResult()).longValue();
    }

    @Override
    public void updatePrice(GiftCertificate giftCertificate) {
        entityManager.merge(giftCertificate);
    }

    /**
     * Generates a query regarding passed params (searchDto and and tableColumns).
     *
     * @param tableColumns + following query fragment:
     *                     and id in (
     *                     SELECT gift_certificate_id
     *                     FROM certificate_tag
     *                     LEFT JOIN tag ON tag_id = tag.id
     *                     WHERE tag.name IN ('tag_name_1', 'tag_name_2')
     *                     GROUP BY gift_certificate_id
     *                     HAVING COUNT(tag_id) = 2)
     *                     and name ilike '%name_fragment%'
     *                     and description ilike '%description_fragment%'
     */
    @SuppressWarnings("unchecked")
    private StringBuilder generateSearchQuery(SearchCertificateDto searchDto, String tableColumns, List queryParams) {
        StringBuilder sb = new StringBuilder(tableColumns);
        List<String> tags = searchDto.getTagNames();
        if (tags != null && !tags.isEmpty()) {
            sb.append(READ_CERTIFICATES_TAGS1)
                    .append("?, ".repeat(tags.size() - 1))
                    .append("?")
                    .append(READ_CERTIFICATES_TAGS2);
            queryParams.addAll(tags);
            queryParams.add(tags.size());
        }
        if (!StringUtils.isBlank(searchDto.getName())) {
            sb.append(NAME_FILTER)
                    .append(searchDto.getName())
                    .append("%' ");
        }
        if (!StringUtils.isBlank(searchDto.getDescription())) {
            sb.append(DESCRIPTION_FILTER)
                    .append(searchDto.getDescription())
                    .append("%' ");
        }
        return sb;
    }
}
