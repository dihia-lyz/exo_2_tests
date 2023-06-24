package com.example.exo_2_update.dto;

import com.example.exo_2_update.model.Annonce;
import lombok.Builder;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@Builder
public class AnnonceDto {

    String id;
    String titre;
    String description;
    Long prix;
    @Enumerated(EnumType.STRING)
    Annonce.TypeAnnonce typeA;
}
