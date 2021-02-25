package com.epam.esm.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "\"order\"")
@EqualsAndHashCode(callSuper = true)
@Audited
public class Order extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @NotAudited
    private User user;

    private LocalDateTime createdDate;

    @OneToMany(
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}
    )
    @JoinColumn(name = "order_id", nullable = false)
    @NotAudited
    private List<OrderItem> orderItems;

    private BigDecimal cost;
}
