package com.epam.esm.repository.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.repository.GenericRepository;
import com.epam.esm.repository.TagRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class TagRepositoryImpl extends GenericRepository<Tag> implements TagRepository {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Tag> tagMapper;
    @PersistenceContext
    private final EntityManager entityManager;

    private static final String READ_TAGS_BY_CERTIFICATE_ID =
            "SELECT id, name FROM tag JOIN certificate_tag ON tag.id = tag_id WHERE gift_certificate_id = ?";

    private static final String READ_MOST_COMMON_TAG_OF_USER_WITH_THE_HIGHEST_COST_OF_ALL_ORDERS =
            "select t.id, t.name " +
                    "from \"order\" " +
                    "         join order_item oi on \"order\".id = oi.order_id " +
                    "    and user_id = (select user_id  " +
                    "                   from \"order\"  " +
                    "                   group by user_id  " +
                    "                   order by sum(cost) desc  " +
                    "                   limit 1) " +
                    "         join gift_certificate gc on oi.gift_certificate_id = gc.id " +
                    "         join certificate_tag ct on gc.id = ct.gift_certificate_id " +
                    "         join tag t on t.id = ct.tag_id " +
                    "group by t.id, t.name " +
                    "order by sum(quantity) desc " +
                    "limit 1";

    private static final String DELETE_TAG = "delete from tag where id = ?";

    private static final String COUNT_TAGS = "select count(id) from tag";

    public TagRepositoryImpl(EntityManager entityManager, JdbcTemplate jdbcTemplate, RowMapper<Tag> tagMapper, EntityManager entityManager1) {
        super(entityManager);
        this.jdbcTemplate = jdbcTemplate;
        this.tagMapper = tagMapper;
        this.entityManager = entityManager1;
    }

    @Override
    public Tag save(Tag tag) {
        tag.setId(null);
        entityManager.persist(tag);
        return tag;
    }

    @Override
    public List<Tag> findPaginated(Integer offset, Integer limit) {
        return entityManager
                .createQuery("from Tag", Tag.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public Long countAll() {
        return entityManager.createQuery("select count(id) from Tag ", Long.class).getSingleResult();
    }

    @Override
    public Optional<Tag> findById(Long tagId) {
        return Optional.ofNullable(entityManager.find(Tag.class, tagId));
    }

    @Override
    public Optional<Tag> findByName(String name) {
        return entityManager.createQuery("from Tag t where t.name=:name", Tag.class)
                .setParameter("name", name)
                .getResultStream().findAny();
    }

    @Override
    public List<Tag> getTagsByCertificateId(Long certificateId) {
        return jdbcTemplate.query(READ_TAGS_BY_CERTIFICATE_ID, tagMapper, certificateId);
    }

    @Override
    public Optional<Tag> getPrevalentTagOfMostProfitableUser() {
        return Optional.ofNullable((Tag) entityManager.createNativeQuery(
                READ_MOST_COMMON_TAG_OF_USER_WITH_THE_HIGHEST_COST_OF_ALL_ORDERS, Tag.class)
                .getSingleResult());
    }
}
