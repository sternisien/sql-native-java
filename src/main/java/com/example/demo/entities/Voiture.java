package com.example.demo.entities;

import javax.persistence.*;

@Table(name = "voiture")
public class Voiture {

  @Id
  @Column(name = "id")
  private Long id;

  @Column(name = "marque")
  private String marque;

  @Column(name = "immatriculation")
  private String immatriculation;

  @ManyToOne
  @JoinColumn(name = "proprio_id")
  private Proprietaire proprietaire;
}
