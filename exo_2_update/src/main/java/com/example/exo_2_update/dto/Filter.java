package com.example.exo_2_update.dto;

import com.example.exo_2_update.model.Annonce;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Component
public class Filter {
    private String titre;
    private Long prixMin;
    private Long prixMax;
    private Annonce.TypeAnnonce type;
}
