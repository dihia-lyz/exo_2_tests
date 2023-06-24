package com.example.exo_2_update.repository;

import com.example.exo_2_update.model.Annonce;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface AnnonceRepository extends JpaRepository<Annonce, String> {
@Query("SELECT a FROM Annonce a WHERE"
        + "(:titre is null or :titre='' or a.titre LIKE %:titre% ) AND"
        + "(:prixMin is null or a.prix >= :prixMin) AND"
        + "(:prixMax is null or a.prix <= :prixMax) AND"
        + "(:type is null or a.typeA = :type)")
List<Annonce> getByTitleAndPriceAndType(@Param("titre") String titre, @Param("prixMin") Optional<Long> prixMin, @Param("prixMax") Optional<Long> prixMax, @Param("type") Optional<Annonce.TypeAnnonce> type);
}

