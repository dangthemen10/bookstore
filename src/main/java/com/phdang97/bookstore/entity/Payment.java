package com.phdang97.bookstore.entity;

import com.phdang97.bookstore.enums.PaymentMethod;
import com.phdang97.bookstore.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "payments")
public class Payment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "payment_intent_id")
    private String paymentIntentId;

    @Column(name = "client_secret")
    private String clientSecret;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    private Double amount;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "payment")
    private Order order;
}
