package com.example.demo.dao;

import com.example.demo.entities.Proprietaire;
import com.example.demo.utils.ConnectionManager;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProprietaireDao {

  public List<Proprietaire> getAllProprietaires(boolean getChild) throws Exception {
    Connection connection = ConnectionManager.getInstance();
    List<Proprietaire> collectionProprietaire = new ArrayList<>();
    connection.beginRequest();
    String sql = "SELECT * FROM proprietaire";
    Statement statement = connection.createStatement();
    try {
      statement.execute(sql);
      ResultSet resultSet = statement.getResultSet();
      while (resultSet.next()) {
        Proprietaire p =
            new Proprietaire(
                resultSet.getLong("id"), resultSet.getString("nom"), resultSet.getString("prenom"));
        collectionProprietaire.add(p);
      }
    } catch (Exception e) {
      throw new Exception("erreur lors de l'insertion de la donnée");
    } finally {
      statement.close();
      connection.close();
    }
    return collectionProprietaire;
  }

  public Proprietaire getProprietaireById(long id) throws Exception {
    Connection connection = ConnectionManager.getInstance();
    Proprietaire proprietaire = null;
    connection.beginRequest();
    String sql = "SELECT * FROM proprietaire WHERE id=" + id;
    Statement statement = connection.createStatement();
    try {
      statement.execute(sql);
      ResultSet resultSet = statement.getResultSet();
      if (resultSet.next()) {
        proprietaire =
            new Proprietaire(
                resultSet.getLong("id"), resultSet.getString("nom"), resultSet.getString("prenom"));
      }
    } catch (Exception e) {
      throw new Exception("erreur lors de l'insertion de la donnée");
    } finally {
      statement.close();
      connection.close();
    }

    return proprietaire;
  }

  public Long insertProprietaire(Proprietaire proprietaire) throws Exception {
    Connection connection = ConnectionManager.getInstance();
    connection.beginRequest();
    Long id = null;
    String sql =
        "INSERT INTO proprietaire(nom, prenom) VALUES ('"
            + proprietaire.getNom()
            + "','"
            + proprietaire.getPrenom()
            + "')";
    Statement statement = connection.createStatement();
    try {
      statement.execute(sql);
      connection.commit();
      ResultSet resultSet = statement.getGeneratedKeys();
      if (resultSet.next()) {
        id = resultSet.getLong(0);
      }
      connection.endRequest();
    } catch (Exception e) {
      connection.rollback();
      throw new Exception("erreur lors de l'insertion de la donnée");
    } finally {
      statement.close();
      connection.close();
    }

    return id;
  }
}
