package com.epam.esm.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class GiftCertificate {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    private String name;
    private String description;
    private BigDecimal price;
    private Integer duration;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    @ManyToMany(mappedBy = "giftCertificates", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private Set<Tag> tags = new HashSet<>();

    public void addTag(Tag tag) {
        tags.add(tag);
        tag.getGiftCertificates().add(this);
    }

//    public List<Tag> getTags() {
//        return null;
//    }
//
//    public void setTags(List<Tag> tags) {
//
//    }
}
