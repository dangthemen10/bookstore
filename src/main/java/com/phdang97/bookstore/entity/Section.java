package com.phdang97.bookstore.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "sections")
public class Section extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "section_name", nullable = false, length = 200)
    private String name;

    @Column(name = "section_description", length = 500)
    private String description;

    @OneToMany(mappedBy = "section", cascade = {CascadeType.REMOVE})
    private Set<Category> categories;
}
