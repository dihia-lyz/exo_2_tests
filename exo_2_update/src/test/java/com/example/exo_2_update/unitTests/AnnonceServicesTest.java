package com.example.exo_2_update.unitTests;

import com.example.exo_2_update.dto.Filter;
import com.example.exo_2_update.model.Annonce;
import com.example.exo_2_update.repository.AnnonceRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.Mockito.*;

public class AnnonceServicesTest {


    @Mock
    private AnnonceRepository annonceRepository;

    @InjectMocks
    private AnnonceService annonceService;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        annonceService = new AnnonceService(annonceRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void ItShouldGetAllAnnonces() {

        List<Annonce> expectedAnnonces = new ArrayList<>();
        expectedAnnonces.add(Annonce.builder()
                .id("id1")
                .titre("annonce 1")
                .prix(1000L)
                .description("description")
                .typeA(Annonce.TypeAnnonce.IMMOBILIER)
                .build());

        expectedAnnonces.add(Annonce.builder()
                .id("id2")
                .titre("annonce 2")
                .prix(1000L)
                .description("description")
                .typeA(Annonce.TypeAnnonce.VEHICULE)
                .build());

        when(annonceRepository.findAll()).thenReturn(expectedAnnonces);

        List<Annonce> actualAnnonces = annonceService.getAnnonces();

        verify(annonceRepository).findAll();

        assertEquals(expectedAnnonces.size(), actualAnnonces.size());
    }

    @Test
    void ItShoulAddNewAnnonce() {
        Annonce expectedAnnonce = new Annonce("id5", "annonce 5", "description 5", 1500L, Annonce.TypeAnnonce.EMPLOI);
        when(annonceRepository.save(expectedAnnonce)).thenReturn(expectedAnnonce);

        Annonce actualAnnonce = annonceService.add(expectedAnnonce);
        verify(annonceRepository).save(expectedAnnonce);

        assertEquals(expectedAnnonce, actualAnnonce);
    }

    @Test
    void ItShouldUpdateById() {
        Annonce annonceTopUpdate = Annonce.builder()
                .id("id1")
                .titre("annonce 1")
                .prix(1000L)
                .description("description")
                .typeA(Annonce.TypeAnnonce.IMMOBILIER)
                .build();
        Annonce newAnnonce = Annonce.builder()
                .id(annonceTopUpdate.getId())
                .titre("annonce updated")
                .prix(3000L)
                .description("description")
                .typeA(Annonce.TypeAnnonce.IMMOBILIER)
                .build();

//        when(annonceRepository.save(annonceTopUpdate)).thenReturn(annonceTopUpdate);
        when(annonceRepository.findById(annonceTopUpdate.getId())).thenReturn(Optional.of(annonceTopUpdate));
        when(annonceRepository.save(
                newAnnonce
        )).thenReturn(newAnnonce);

        Optional<Annonce> actualAnnonce = annonceService.update(annonceTopUpdate.getId(), newAnnonce);

        verify(annonceRepository, times(1)).findById(annonceTopUpdate.getId());
        verify(annonceRepository, times(1)).save(newAnnonce);

        assertEquals(newAnnonce, actualAnnonce.orElse(null));
        assertEquals(newAnnonce.getTitre(), annonceTopUpdate.getTitre());
        assertEquals(newAnnonce.getPrix(), annonceTopUpdate.getPrix());
        assertEquals(newAnnonce.getDescription(), annonceTopUpdate.getDescription());
        assertEquals(newAnnonce.getTypeA(), annonceTopUpdate.getTypeA());
    }

    @Test
    void ItShouldFilterByTitleAndPriceAndType() {
        Filter filter = Filter.builder()
                .titre("2")
                .prixMin(2000L)
                .prixMax(4000L)
                .type(Annonce.TypeAnnonce.IMMOBILIER)
                .build();

        Annonce annonce1 = Annonce.builder()
                .id("id1")
                .titre("annonce 1")
                .prix(8000L)
                .description("description 1")
                .typeA(Annonce.TypeAnnonce.IMMOBILIER)
                .build();

        Annonce annonce2 = Annonce.builder()
                .id("id2")
                .titre("annonce 2")
                .prix(3000L)
                .description("description 2")
                .typeA(Annonce.TypeAnnonce.IMMOBILIER)
                .build();

        List<Annonce> allAnnonces = new ArrayList<>();
        allAnnonces.add(annonce1);
        allAnnonces.add(annonce2);

        List <Annonce> expectedAnnonces = getFilteredAnnonces(allAnnonces, filter);

        when(annonceRepository.getByTitleAndPriceAndType(
                filter.getTitre(),
                Optional.ofNullable(filter.getPrixMin()),
                Optional.ofNullable(filter.getPrixMax()),
                Optional.ofNullable(filter.getType())
        )).thenReturn(getFilteredAnnonces(allAnnonces, filter));

        List<Annonce> actualAnnonces = annonceService.search(filter);
        verify(annonceRepository).getByTitleAndPriceAndType(
                        filter.getTitre(),
                        Optional.ofNullable(filter.getPrixMin()),
                        Optional.ofNullable(filter.getPrixMax()),
                        Optional.ofNullable(filter.getType())
                );

        assertEquals(expectedAnnonces, actualAnnonces);
    }

    private List<Annonce> getFilteredAnnonces(List<Annonce> annonces, Filter filter) {
        List<Annonce> filteredAnnonces = new ArrayList<>();

        for (Annonce annonce : annonces) {
            boolean titreMatches = filter.getTitre().trim().isEmpty() || annonce.getTitre().contains(filter.getTitre());
            boolean prixMinMatches = filter.getPrixMin() == null || annonce.getPrix() >= filter.getPrixMin();
            boolean prixMaxMatches = filter.getPrixMax() == null || annonce.getPrix() <= filter.getPrixMax();
            boolean typeMatches = filter.getType() == null || annonce.getTypeA() == filter.getType();

            if(titreMatches && prixMinMatches && prixMaxMatches && typeMatches)
                filteredAnnonces.add(annonce);
        }
        return filteredAnnonces;
    }
}