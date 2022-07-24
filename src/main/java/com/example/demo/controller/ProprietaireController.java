package com.example.demo.controller;

import com.example.demo.dto.ProprietaireDto;
import com.example.demo.service.ProprietaireService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping(value = "api/owner")
public class ProprietaireController {

  private final ProprietaireService proprietaireService;

  public ProprietaireController(ProprietaireService proprietaireService) {
    this.proprietaireService = proprietaireService;
  }

  @GetMapping
  public ResponseEntity<List<ProprietaireDto>> getTousLesProprietaires() throws Exception {
    return ResponseEntity.status(HttpStatus.OK).body(proprietaireService.getTousLesProprietaires());
  }

  @GetMapping("/{id}")
  public ResponseEntity<ProprietaireDto> getProprietaireParId(@PathVariable Long id)
      throws Exception {
    return ResponseEntity.status(HttpStatus.OK).body(proprietaireService.getProprietaireById(id));
  }

  @PostMapping
  public ResponseEntity creerNouveauProprio(
      UriComponentsBuilder uriComponentsBuilder, @RequestBody ProprietaireDto proprietaireDto)
      throws Exception {
    Long id = proprietaireService.insertNouveauProprietaire(proprietaireDto);
    UriComponents uriComponents = uriComponentsBuilder.path("/{id}").buildAndExpand(id);
    return ResponseEntity.status(HttpStatus.CREATED)
        .header(HttpHeaders.LOCATION, uriComponents.toUriString())
        .build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity supprimerProprietaire(@PathVariable Long id) throws Exception {
    proprietaireService.deleteProprietaireById(id);
    return ResponseEntity.status(HttpStatus.OK).build();
  }
}
