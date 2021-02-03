package com.epam.esm.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = "tags")
@ToString(exclude = "tags")
public class GiftCertificate {
    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true)
    private String name;
    private String description;
    private BigDecimal price;
    private Integer duration;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    //    @ManyToMany(mappedBy = "giftCertificates",fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "certificate_tag",
            joinColumns = {@JoinColumn(name = "gift_certificate_id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id")}
    )
    private Set<Tag> tags = new HashSet<>();

    public void addTag(Tag tag) {
        tags.add(tag);
        //tag.getGiftCertificates().add(this);
    }
}
