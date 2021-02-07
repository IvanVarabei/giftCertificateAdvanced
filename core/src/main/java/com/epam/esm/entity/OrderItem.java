package com.epam.esm.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gift_certificate_id")
    private GiftCertificate certificate;
}