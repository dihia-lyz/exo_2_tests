package com.example.exo_2_update.unitTests;

import com.example.exo_2_update.model.Annonce;
import com.example.exo_2_update.repository.AnnonceRepository;
import com.example.exo_2_update.dto.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Component
public class AnnonceService {
    //    @Autowired
    private AnnonceRepository AnnonceRepo;

    @Autowired
    public AnnonceService(AnnonceRepository ar) {
        this.AnnonceRepo = ar;
    }

    public List<Annonce> getAnnonces() {
        List<Annonce> result = new ArrayList<>();
        List<Annonce> Ann = AnnonceRepo.findAll();
        for (Annonce a : Ann) {
            result.add(a);
        }
        return result;
    }

    public Annonce add(Annonce annonce) {
        return AnnonceRepo.save(annonce);
    }

    public Optional<Annonce> update(String id, Annonce annonce) {
        Optional<Annonce> existedAnnonce = AnnonceRepo.findById(id).map(
                existAnnonce -> {
                    existAnnonce.setTitre(annonce.getTitre());
                    existAnnonce.setPrix(annonce.getPrix());
                    existAnnonce.setDescription(annonce.getDescription());
                    existAnnonce.setTypeA(annonce.getTypeA());
                    AnnonceRepo.save(existAnnonce);
                    return existAnnonce;
                }
        );
        return existedAnnonce;
    }

    public void delete(String id) {
        AnnonceRepo.deleteById(id);
    }

    public List<Annonce> search(Filter filter) {

        String titre = (filter.getTitre().trim().equals("")) ? "" : filter.getTitre();
        Optional<Long> prixMin = Optional.ofNullable(filter.getPrixMin());
        Optional<Long> prixMax = Optional.ofNullable(filter.getPrixMax());
        Optional<Annonce.TypeAnnonce> type = Optional.ofNullable(filter.getType());

        return AnnonceRepo.getByTitleAndPriceAndType(titre, prixMin, prixMax, type);
    }
}
