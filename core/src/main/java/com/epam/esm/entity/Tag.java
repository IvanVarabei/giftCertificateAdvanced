package com.epam.esm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
//@EqualsAndHashCode(exclude = "giftCertificates")
//@ToString(exclude = "giftCertificates")
public class Tag {
    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true)
    private String name;
    //    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
//    @JoinTable(
//            name = "certificate_tag",
//            joinColumns = {@JoinColumn(name = "tag_id")},
//            inverseJoinColumns = {@JoinColumn(name = "gift_certificate_id")}
//    )
//    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
//    private Set<GiftCertificate> giftCertificates = new HashSet<>();
}
