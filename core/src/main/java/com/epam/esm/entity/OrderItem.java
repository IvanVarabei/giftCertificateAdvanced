package com.epam.esm.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class OrderItem extends BaseEntity {
    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gift_certificate_id")
    private GiftCertificate certificate;

    public OrderItem(GiftCertificate certificate, Integer quantity) {
        this.certificate = certificate;
        this.quantity = quantity;
    }
}