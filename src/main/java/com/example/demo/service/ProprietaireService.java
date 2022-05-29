package com.example.demo.service;

import com.example.demo.dao.ProprietaireDao;
import com.example.demo.dto.ProprietaireDto;
import com.example.demo.entities.Proprietaire;
import com.github.dozermapper.core.Mapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProprietaireService {

  private final ProprietaireDao proprietaireDao;
  private final Mapper mapper;

  public ProprietaireService(ProprietaireDao proprietaireDao, Mapper mapper) {
    this.proprietaireDao = proprietaireDao;
    this.mapper = mapper;
  }

  public Long insertNouveauProprietaire(ProprietaireDto proprietaireDto) throws Exception {
    Proprietaire newProprio = mapper.map(proprietaireDto, Proprietaire.class);
    return proprietaireDao.insertProprietaire(newProprio);
  }

  public List<ProprietaireDto> getTousLesProprietaires() throws Exception {
    return proprietaireDao.getAllProprietaires(false).stream()
        .map(proprietaire -> mapper.map(proprietaire, ProprietaireDto.class))
        .collect(Collectors.toList());
  }

  public ProprietaireDto getProprietaireById(Long id) throws Exception {
    return mapper.map(proprietaireDao.getProprietaireById(id), ProprietaireDto.class);
  }
}
