package com.d210.moneymoa.controller;

import com.d210.moneymoa.dto.Wiki;
import com.d210.moneymoa.service.WikiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/wiki")
public class WikiController {
    @Autowired
    private WikiService wikiService;

    @PostMapping("/create")
    public ResponseEntity<List<Wiki>> createWiki(
            @RequestBody List<Wiki> wikiList) {
        return new ResponseEntity<>(wikiService.createWikiList(wikiList), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Wiki> getWiki(@PathVariable Long id) {
        Optional<Wiki> wiki = wikiService.getWiki(id);
        return wiki.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/list")
    public ResponseEntity<List<Wiki>> getAllWikis() {
        return new ResponseEntity<>(wikiService.getAllWikis(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWiki(@PathVariable Long id) {
        wikiService.deleteWiki(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
