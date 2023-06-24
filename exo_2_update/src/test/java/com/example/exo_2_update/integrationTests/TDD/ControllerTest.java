package com.example.exo_2_update.integrationTests.TDD;

import com.example.exo_2_update.Exo2UpdateApplication;
import com.example.exo_2_update.controller.AnnonceController;
import com.example.exo_2_update.dto.Filter;
import com.example.exo_2_update.model.Annonce;
import com.example.exo_2_update.unitTests.AnnonceService;
import io.cucumber.core.internal.com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.core.internal.com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

//classes= Exo2UpdateApplication.class,
@SpringBootTest(classes = Exo2UpdateApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Slf4j
public class ControllerTest {
    @LocalServerPort
    Integer port;
    @Autowired
    TestRestTemplate restTemplate;
    @Autowired
    AnnonceController controller;
    @MockBean
    AnnonceService annonceService;
    @Autowired
    Filter filter;
    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    public void getAnnoncesShouldReturnAll() throws JsonProcessingException {

        Annonce annonce1 = Annonce.builder()
                .id("id1")
                .titre("annonce 1")
                .prix(20000L)
                .description("annonce 1 description")
                .typeA(Annonce.TypeAnnonce.EMPLOI).build();

        Annonce annonce2 = Annonce.builder()
                .id("id2")
                .titre("annonce 2")
                .prix(20000L)
                .description("annonce 2 description")
                .typeA(Annonce.TypeAnnonce.EMPLOI).build();

        List<Annonce> expectedAnnonces = Arrays.asList(annonce1, annonce2);

        when(annonceService.getAnnonces()).thenReturn(expectedAnnonces);
        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/annonce/all", String.class);

        List<Annonce> actualAnnonces = getArrayListFromJson(response.getBody());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedAnnonces, actualAnnonces);

        verify(annonceService).getAnnonces();
    }

    @Test
    public void updateAnnonceShouldReturnUpdatedAnnonce() throws Exception {
        Annonce expectedAnnonce = Annonce.builder()
                .id("id1")
                .titre("annonce updated")
                .prix(15000L)
                .description("annonce updated description")
                .typeA(Annonce.TypeAnnonce.EMPLOI).build();

        String id = expectedAnnonce.getId();

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://localhost:" + port + "/annonce/update")
                .queryParam("id", id);
        String url = builder.toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Annonce> requestEntity = new HttpEntity<>(expectedAnnonce, headers);

        when(annonceService.update(eq(id), eq(expectedAnnonce))).thenReturn(Optional.of(expectedAnnonce));

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, String.class);
        Annonce actualAnnonce = getObjectFromJson(response.getBody());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedAnnonce, actualAnnonce);

        verify(annonceService).update(eq(id), eq(expectedAnnonce));
    }


    @Test
    public void deleteAnnonceShouldReturnStatus200() throws Exception {
        Annonce annonceToDelete = Annonce.builder()
                .id("id1")
                .titre("annonce updated")
                .prix(15000L)
                .description("annonce updated description")
                .typeA(Annonce.TypeAnnonce.EMPLOI).build();

        String id = annonceToDelete.getId();

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://localhost:" + port + "/annonce/delete")
                .queryParam("id", id);
        String url = builder.toUriString();

        doNothing().when(annonceService).delete(eq(id));

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, null, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(annonceService).delete(eq(id));
    }

    @Test
    public void searchShouldReturnSomeAnnonces() throws Exception {
        Annonce annonce1 = Annonce.builder()
                .id("id1")
                .titre("annonce updated")
                .prix(15000L)
                .description("annonce updated description")
                .typeA(Annonce.TypeAnnonce.IMMOBILIER).build();

        Annonce annonce2 = Annonce.builder()
                .id("id2")
                .titre("annonce 2")
                .prix(20000L)
                .description("annonce 2 description")
                .typeA(Annonce.TypeAnnonce.EMPLOI).build();

        List<Annonce> expectedAnnonces = Arrays.asList(annonce1);

        Filter filter = Filter.builder()
                .titre("")
                .prixMin(100L)
                .prixMax(1000000L)
                .type(Annonce.TypeAnnonce.IMMOBILIER)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Filter> requestEntity = new HttpEntity<>(filter, headers);

        when(annonceService.search(filter)).thenReturn(expectedAnnonces);

        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:" + port + "/annonce/search", requestEntity, String.class);
        List<Annonce> actualAnnonces = getArrayListFromJson(response.getBody());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedAnnonces, actualAnnonces);

        verify(annonceService).search(filter);
    }

    private Annonce getObjectFromJson(String jsonString) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Annonce object = objectMapper.readValue(jsonString, Annonce.class);
        return object;
    }
    private List<Annonce> getArrayListFromJson(String jsonList) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Annonce[] annoncesArray = objectMapper.readValue(jsonList, Annonce[].class);
        List<Annonce> result = Arrays.asList(annoncesArray);
        return result;
    }

}