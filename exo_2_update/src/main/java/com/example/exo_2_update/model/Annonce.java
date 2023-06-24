package com.example.exo_2_update.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "annonce")
public class Annonce {
    public enum TypeAnnonce {
        IMMOBILIER, VEHICULE, EMPLOI,
    }

    @Id
    private String id;
    private String titre;
    private String description;
    private Long prix;
    @Enumerated(EnumType.STRING)
    private TypeAnnonce typeA;
}
