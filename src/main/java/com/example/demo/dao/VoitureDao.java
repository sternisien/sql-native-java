package com.example.demo.dao;

import com.example.demo.entities.Voiture;
import org.springframework.stereotype.Repository;

@Repository
public class VoitureDao extends AbstractDao<Voiture> {

  public VoitureDao() {
    super(connectionManager);
  }

  public void getVoitureById() {
    findById(2l, Voiture.class);
  }
}
