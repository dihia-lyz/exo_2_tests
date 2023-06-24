package com.example.exo_2_update.controller;

import com.example.exo_2_update.model.Annonce;
import com.example.exo_2_update.unitTests.AnnonceService;
import com.example.exo_2_update.dto.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("annonce")
@RestController
@Component
public class AnnonceController {
    private AnnonceService AnnonceService;
    private Filter filter;

    @Autowired
    public AnnonceController(AnnonceService AS) {
        this.AnnonceService = AS;
    }

    @GetMapping("all")
    public ResponseEntity<Object> getAnnonces() {
        return new ResponseEntity <>(AnnonceService.getAnnonces(), HttpStatus.OK);
    }

    @PostMapping("new")
    public ResponseEntity<Annonce> add(@RequestBody Annonce annonce) {
        return new ResponseEntity<>(AnnonceService.add(annonce), HttpStatus.OK);
    }

    @PutMapping("update")
    public ResponseEntity<Object> update(@RequestParam("id") String id, @RequestBody Annonce annonce) {
        return new ResponseEntity<>(AnnonceService.update(id, annonce), HttpStatus.OK);
    }

    @DeleteMapping("delete")
    public ResponseEntity<String> delete(@RequestParam("id") String id) {
        AnnonceService.delete(id);
        return  ResponseEntity.ok("Deleted successfully");
    }

    @PostMapping("search")
    public ResponseEntity<List<Annonce>> search(@RequestBody Filter filter) {
        return new ResponseEntity<>( AnnonceService.search(filter), HttpStatus.OK);
    }
}
