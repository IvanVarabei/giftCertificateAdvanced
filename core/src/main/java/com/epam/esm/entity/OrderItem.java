package com.epam.esm.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity(name = "OrderItem")
@Table(name = "\"order_item\"")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "default_generator")
    private Long id;

    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gift_certificate_id")
    private GiftCertificate certificate;
}