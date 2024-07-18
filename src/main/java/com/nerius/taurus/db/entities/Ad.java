package com.nerius.taurus.db.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Ad {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    private String goodName;

    private String description;

    private double price;

    @Lob
    @Column(length = 1000000) // указание максимального размера BLOB
    private byte[] image;
}
