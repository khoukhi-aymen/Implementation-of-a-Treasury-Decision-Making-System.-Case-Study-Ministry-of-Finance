import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class relationnel {

  public String insertIntoAxeProgramatique(
    String codePortef,
    String libPortef,
    String programme,
    String sousProgramme,
    String libelle
  ) {
    Connection connection = null;
    PreparedStatement insertStatement = null;

    try {
      // Connexion à la base de données
      String url = "jdbc:oracle:thin:@localhost:1521/orcl";
      String username = "projetMaster";
      String passwordDB = "psw";
      connection = DriverManager.getConnection(url, username, passwordDB);

      // Vérifier si une ligne avec les mêmes valeurs existe déjà dans la table
      String selectQuery =
        "SELECT COUNT(*) FROM axeprogramatique WHERE CODE_PORTEF = ? AND PROGRAMME = ? AND SOUS_PROGRAMME = ?";
      PreparedStatement selectStatement = connection.prepareStatement(
        selectQuery
      );
      selectStatement.setString(1, codePortef);
      selectStatement.setString(2, programme);
      selectStatement.setString(3, sousProgramme);
      ResultSet resultSet = selectStatement.executeQuery();

      // Vérifier le résultat de la requête SELECT
      resultSet.next();
      int rowCount = resultSet.getInt(1);
      if (rowCount > 0) {
        return "La ligne existe déjà dans la table axeprogramatique. Insertion annulée.";
      }

      // Préparation de la requête d'insertion
      String insertQuery =
        "INSERT INTO axeprogramatique (CODE_PORTEF, LIB_PORTEF, PROGRAMME, SOUS_PROGRAMME, LIBELLE) VALUES (?, ?, ?, ?, ?)";
      insertStatement = connection.prepareStatement(insertQuery);
      insertStatement.setString(1, codePortef);
      insertStatement.setString(2, libPortef);
      insertStatement.setString(3, programme);
      insertStatement.setString(4, sousProgramme);
      insertStatement.setString(5, libelle);
      int rowsAffected = insertStatement.executeUpdate();

      if (rowsAffected > 0) {
        return "Élément inséré avec succès dans la table axeprogramatique";
      } else {
        return "Aucun élément inséré dans la table axeprogramatique.";
      }
    } catch (SQLException e) {
      // Gestion des exceptions SQL
      e.printStackTrace();
      return (
        "Erreur lors de l'insertion de l'élément dans la table axeprogramatique : " +
        e.getMessage()
      );
    } finally {
      // Fermeture des ressources
      try {
        if (insertStatement != null) {
          insertStatement.close();
        }
        if (connection != null) {
          connection.close();
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

  public String insertIntoAxeEconomique(String axeEco, String libelle) {
    Connection connection = null;
    PreparedStatement insertStatement = null;

    try {
      // Connexion à la base de données
      String url = "jdbc:oracle:thin:@localhost:1521/orcl";
      String username = "projetMaster";
      String passwordDB = "psw";
      connection = DriverManager.getConnection(url, username, passwordDB);

      // Vérifier si une ligne avec les mêmes valeurs existe déjà dans la table
      String selectQuery =
        "SELECT COUNT(*) FROM axeeconomique WHERE AXE_ECO = ?";
      PreparedStatement selectStatement = connection.prepareStatement(
        selectQuery
      );
      selectStatement.setString(1, axeEco);
      ResultSet resultSet = selectStatement.executeQuery();

      // Vérifier le résultat de la requête SELECT
      resultSet.next();
      int rowCount = resultSet.getInt(1);
      if (rowCount > 0) {
        return "La ligne existe déjà dans la table axeeconomique. Insertion annulée.";
      }

      // Préparation de la requête d'insertion
      String insertQuery =
        "INSERT INTO axeeconomique (AXE_ECO, LIBELLE) VALUES (?, ?)";
      insertStatement = connection.prepareStatement(insertQuery);
      insertStatement.setString(1, axeEco);
      insertStatement.setString(2, libelle);
      int rowsAffected = insertStatement.executeUpdate();

      if (rowsAffected > 0) {
        return "Élément inséré avec succès dans la table axeeconomique";
      } else {
        return "Aucun élément inséré dans la table axeeconomique.";
      }
    } catch (SQLException e) {
      // Gestion des exceptions SQL
      e.printStackTrace();
      return (
        "Erreur lors de l'insertion de l'élément dans la table axeeconomique : " +
        e.getMessage()
      );
    } finally {
      // Fermeture des ressources
      try {
        if (insertStatement != null) {
          insertStatement.close();
        }
        if (connection != null) {
          connection.close();
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

  public String insertIntoAction(
    String codePortef,
    String programme,
    String sousProgramme,
    String action,
    String sousAction,
    String libelleAction
  ) {
    Connection connection = null;
    PreparedStatement insertStatement = null;

    try {
      // Connexion à la base de données
      String url = "jdbc:oracle:thin:@localhost:1521/orcl";
      String username = "projetMaster";
      String passwordDB = "psw";
      connection = DriverManager.getConnection(url, username, passwordDB);

      // Vérifier si une ligne avec les mêmes valeurs existe déjà dans la table
      String selectQuery =
        "SELECT COUNT(*) FROM Action WHERE CODE_PORTEF = ? AND PROGRAMME = ? AND SOUS_PROGRAMME = ? AND ACTION = ? AND SOUS_ACTION = ?";
      PreparedStatement selectStatement = connection.prepareStatement(
        selectQuery
      );
      selectStatement.setString(1, codePortef);
      selectStatement.setString(2, programme);
      selectStatement.setString(3, sousProgramme);
      selectStatement.setString(4, action);
      selectStatement.setString(5, sousAction);
      ResultSet resultSet = selectStatement.executeQuery();

      // Vérifier le résultat de la requête SELECT
      resultSet.next();
      int rowCount = resultSet.getInt(1);
      if (rowCount > 0) {
        return "La ligne existe déjà dans la table Action. Insertion annulée.";
      }

      // Préparation de la requête d'insertion
      String insertQuery =
        "INSERT INTO Action (CODE_PORTEF, PROGRAMME, SOUS_PROGRAMME, ACTION, SOUS_ACTION, LIBELLE_ACTION) VALUES (?, ?, ?, ?, ?, ?)";
      insertStatement = connection.prepareStatement(insertQuery);
      insertStatement.setString(1, codePortef);
      insertStatement.setString(2, programme);
      insertStatement.setString(3, sousProgramme);
      insertStatement.setString(4, action);
      insertStatement.setString(5, sousAction);
      insertStatement.setString(6, libelleAction);
      int rowsAffected = insertStatement.executeUpdate();

      if (rowsAffected > 0) {
        return "Élément inséré avec succès dans la table Action";
      } else {
        return "Aucun élément inséré dans la table Action.";
      }
    } catch (SQLException e) {
      // Gestion des exceptions SQL
      e.printStackTrace();
      return (
        "Erreur lors de l'insertion de l'élément dans la table Action : " +
        e.getMessage()
      );
    } finally {
      // Fermeture des ressources
      try {
        if (insertStatement != null) {
          insertStatement.close();
        }
        if (connection != null) {
          connection.close();
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

  public String insertIntoOrdonnateur(String CodeOrd, String libelleOrd,String CodeWilaya) {
    Connection connection = null;
    PreparedStatement insertStatement = null;

    try {
      // Connexion à la base de données
      String url = "jdbc:oracle:thin:@localhost:1521/orcl";
      String username = "projetMaster";
      String passwordDB = "psw";
      connection = DriverManager.getConnection(url, username, passwordDB);

      // Vérifier si une ligne avec les mêmes valeurs existe déjà dans la table
      String selectQuery =
        "SELECT COUNT(*) FROM Ordonnateur WHERE CODE_ORD = ?";
      PreparedStatement selectStatement = connection.prepareStatement(
        selectQuery
      );
      selectStatement.setString(1, CodeOrd);
      ResultSet resultSet = selectStatement.executeQuery();

      // Vérifier le résultat de la requête SELECT
      resultSet.next();
      int rowCount = resultSet.getInt(1);
      if (rowCount > 0) {
        return "La ligne existe déjà dans la table Ordonnateur. Insertion annulée.";
      }

      // Préparation de la requête d'insertion
      String insertQuery =
        "INSERT INTO Ordonnateur (CODE_ORD, LIBELLE_ORD, CODE_WILAYA) VALUES (?, ?, ?)";
      insertStatement = connection.prepareStatement(insertQuery);
      insertStatement.setString(1, CodeOrd);
      insertStatement.setString(2, libelleOrd);
      insertStatement.setString(3, CodeWilaya);
      int rowsAffected = insertStatement.executeUpdate();

      if (rowsAffected > 0) {
        return "Élément inséré avec succès dans la table Ordonnateur";
      } else {
        return "Aucun élément inséré dans la table Ordonnateur.";
      }
    } catch (SQLException e) {
      // Gestion des exceptions SQL
      e.printStackTrace();
      return (
        "Erreur lors de l'insertion de l'élément dans la table Ordonnateur : " +
        e.getMessage()
      );
    } finally {
      // Fermeture des ressources
      try {
        if (insertStatement != null) {
          insertStatement.close();
        }
        if (connection != null) {
          connection.close();
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

  public String insertIntoCredit(
    String gestion,
    String mois,
    String codeOrd,
    String codePortef,
    String programme,
    String sousProgramme,
    String action,
    String sousAction,
    String AxeEconomique,
    String dispos,
    String totCredit,
    String totDeb,
    String solde
  ) {
    Connection connection = null;
    PreparedStatement insertStatement = null;

    try {
      // Connexion à la base de données
      String url = "jdbc:oracle:thin:@localhost:1521/orcl";
      String username = "projetMaster";
      String passwordDB = "psw";
      connection = DriverManager.getConnection(url, username, passwordDB);

      // Préparation de la requête d'insertion
      String insertQuery =
        "INSERT INTO CREDIT (GESTION, MOIS, CODE_ORD, CODE_PORTEF, PROGRAMME, SOUS_PROGRAMME, ACTION, SOUS_ACTION, AXE_ECO , DISPOS, TOT_CREDIT, TOT_DEB, SOLDE) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
      insertStatement = connection.prepareStatement(insertQuery);
      insertStatement.setString(1, gestion);
      insertStatement.setString(2, mois);
      insertStatement.setString(3, codeOrd);
      insertStatement.setString(4, codePortef);
      insertStatement.setString(5, programme);
      insertStatement.setString(6, sousProgramme);
      insertStatement.setString(7, action);
      insertStatement.setString(8, sousAction);
      insertStatement.setString(9, AxeEconomique);
      insertStatement.setString(10, dispos);
      insertStatement.setString(11, totCredit);
      insertStatement.setString(12, totDeb);
      insertStatement.setString(13, solde);
      int rowsAffected = insertStatement.executeUpdate();

      if (rowsAffected > 0) {
        return "Élément inséré avec succès dans la table CREDIT";
      } else {
        return "Aucun élément inséré dans la table CREDIT.";
      }
    } catch (SQLException e) {
      // Gestion des exceptions SQL
      e.printStackTrace();
      return (
        "Erreur lors de l'insertion de l'élément dans la table CREDIT : " +
        e.getMessage()
      );
    } finally {
      // Fermeture des ressources
      try {
        if (insertStatement != null) {
          insertStatement.close();
        }
        if (connection != null) {
          connection.close();
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }




  public String insertIntoMndat(
    String CodePortef,
    String codeOrdonnateur,
    String gestion,
    String CodeMandat,
    String DateEmission,
    String Statut,
    String Programme,
    String sousProgramme,
    String Action,
    String SousAction,
    String AxeEconomique,
    String Dispos,
    String Mantant
  ) {
    Connection connection = null;
    PreparedStatement insertStatement = null;

    try {
      // Connexion à la base de données
      String url = "jdbc:oracle:thin:@localhost:1521/orcl";
      String username = "projetMaster";
      String passwordDB = "psw";
      connection = DriverManager.getConnection(url, username, passwordDB);

      // Préparation de la requête d'insertion
      String insertQuery =
        "INSERT INTO Mandat (CODE_PORTEF, CODE_ORD, GESTION, CODE_MANDAT, DT_EMISSION, STATUT, PROGRAMME, SOUS_PROGRAMME, ACTION ,SOUS_ACTION ,AXE_ECO, DISPOS, MONTANT) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

      insertStatement = connection.prepareStatement(insertQuery);
      insertStatement.setString(1, CodePortef);
      insertStatement.setString(2, codeOrdonnateur);
      insertStatement.setString(3, gestion);
      insertStatement.setString(4, CodeMandat);
      insertStatement.setString(5, DateEmission);
      insertStatement.setString(6, Statut);
      insertStatement.setString(7, Programme);
      insertStatement.setString(8, sousProgramme);
      insertStatement.setString(9, Action);
      insertStatement.setString(10, SousAction);
      insertStatement.setString(11, AxeEconomique);
      insertStatement.setString(12, Dispos);
      insertStatement.setString(13, Mantant);
      int rowsAffected = insertStatement.executeUpdate();

      if (rowsAffected > 0) {
        return "Élément inséré avec succès dans la table Mandat";
      } else {
        return "Aucun élément inséré dans la table Mandat.";
      }
    } catch (SQLException e) {
      // Gestion des exceptions SQL
      e.printStackTrace();
      return (
        "Erreur lors de l'insertion de l'élément dans la table Mandat " +
        e.getMessage()
      );
    } finally {
      // Fermeture des ressources
      try {
        if (insertStatement != null) {
          insertStatement.close();
        }
        if (connection != null) {
          connection.close();
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }



  public String insertIntoWilaya(String CodeWilaya, String libelleWilaya) {
    Connection connection = null;
    PreparedStatement insertStatement = null;

    try {
      // Connexion à la base de données
      String url = "jdbc:oracle:thin:@localhost:1521/orcl";
      String username = "projetMaster";
      String passwordDB = "psw";
      connection = DriverManager.getConnection(url, username, passwordDB);

      // Vérifier si une ligne avec les mêmes valeurs existe déjà dans la table
      String selectQuery =
        "SELECT COUNT(*) FROM Wilayas WHERE CODE_WILAYA = ?";
      PreparedStatement selectStatement = connection.prepareStatement(
        selectQuery
      );
      selectStatement.setString(1, CodeWilaya);
      ResultSet resultSet = selectStatement.executeQuery();

      // Vérifier le résultat de la requête SELECT
      resultSet.next();
      int rowCount = resultSet.getInt(1);
      if (rowCount > 0) {
        return "La ligne existe déjà dans la table Wilaya. Insertion annulée.";
      }

      // Préparation de la requête d'insertion
      String insertQuery =
        "INSERT INTO Wilayas (CODE_WILAYA, LIBELLE_WILAYA) VALUES (?, ?)";
      insertStatement = connection.prepareStatement(insertQuery);
      insertStatement.setString(1, CodeWilaya);
      insertStatement.setString(2, libelleWilaya);
      int rowsAffected = insertStatement.executeUpdate();

      if (rowsAffected > 0) {
        return "Élément inséré avec succès dans la table Wilayas";
      } else {
        return "Aucun élément inséré dans la table Wilayas.";
      }
    } catch (SQLException e) {
      // Gestion des exceptions SQL
      e.printStackTrace();
      return (
        "Erreur lors de l'insertion de l'élément dans la table Wilayas : " +
        e.getMessage()
      );
    } finally {
      // Fermeture des ressources
      try {
        if (insertStatement != null) {
          insertStatement.close();
        }
        if (connection != null) {
          connection.close();
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }




  public String insertIntoRecette(String Gestion,String Mois,String CodeCompte, String libelleCompte,String PosteComptable,String Montant) {
    Connection connection = null;
    PreparedStatement insertStatement = null;

    try {
      // Connexion à la base de données
      String url = "jdbc:oracle:thin:@localhost:1521/orcl";
      String username = "projetMaster";
      String passwordDB = "psw";
      connection = DriverManager.getConnection(url, username, passwordDB);

      // Préparation de la requête d'insertion
      String insertQuery =
        "INSERT INTO Recette (GESTION, MOIS, CODE_CPT, LIB_CPT_G , POSTE_C, MT_MOIS) VALUES (?, ?, ?, ?, ?, ?)";
      insertStatement = connection.prepareStatement(insertQuery);
      insertStatement.setString(1, Gestion);
      insertStatement.setString(2, Mois);
      insertStatement.setString(3, CodeCompte);
      insertStatement.setString(4, libelleCompte);
      insertStatement.setString(5, PosteComptable);
      insertStatement.setString(6, Montant);
      int rowsAffected = insertStatement.executeUpdate();

      if (rowsAffected > 0) {
        return "Élément inséré avec succès dans la table Recette";
      } else {
        return "Aucun élément inséré dans la table Recette.";
      }
    } catch (SQLException e) {
      // Gestion des exceptions SQL
      e.printStackTrace();
      return (
        "Erreur lors de l'insertion de l'élément dans la table Recette : " +
        e.getMessage()
      );
    } finally {
      // Fermeture des ressources
      try {
        if (insertStatement != null) {
          insertStatement.close();
        }
        if (connection != null) {
          connection.close();
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }



  public String insertIntoSoumissionaire(String CODE_Soumissionaire, String Type_Soumissionaire,String libelle_Soumissionaire) {
    Connection connection = null;
    PreparedStatement insertStatement = null;

    try {
      // Connexion à la base de données
      String url = "jdbc:oracle:thin:@localhost:1521/orcl";
      String username = "projetMaster";
      String passwordDB = "psw";
      connection = DriverManager.getConnection(url, username, passwordDB);

      // Vérifier si une ligne avec les mêmes valeurs existe déjà dans la table
      String selectQuery =
        "SELECT COUNT(*) FROM Soumissionaire WHERE CODE_Soumissionaire = ?";
      PreparedStatement selectStatement = connection.prepareStatement(
        selectQuery
      );
      selectStatement.setString(1, CODE_Soumissionaire);
      ResultSet resultSet = selectStatement.executeQuery();

      // Vérifier le résultat de la requête SELECT
      resultSet.next();
      int rowCount = resultSet.getInt(1);
      if (rowCount > 0) {
        return "La ligne existe déjà dans la table Soumissionaire. Insertion annulée.";
      }

      // Préparation de la requête d'insertion
      String insertQuery =
        "INSERT INTO Soumissionaire (CODE_Soumissionaire, Type_Soumissionaire,libelle_Soumissionaire) VALUES (?, ?,?)";
      insertStatement = connection.prepareStatement(insertQuery);
      insertStatement.setString(1, CODE_Soumissionaire);
      insertStatement.setString(2, Type_Soumissionaire);
      insertStatement.setString(3, libelle_Soumissionaire);
      int rowsAffected = insertStatement.executeUpdate();

      if (rowsAffected > 0) {
        return "Élément inséré avec succès dans la table Soumissionaire";
      } else {
        return "Aucun élément inséré dans la table Soumissionaire.";
      }
    } catch (SQLException e) {
      // Gestion des exceptions SQL
      e.printStackTrace();
      return (
        "Erreur lors de l'insertion de l'élément dans la table Soumissionaire : " +
        e.getMessage()
      );
    } finally {
      // Fermeture des ressources
      try {
        if (insertStatement != null) {
          insertStatement.close();
        }
        if (connection != null) {
          connection.close();
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }



  public String insertIntoDette(
    String code_ISIN,
    String DATE_DEB,
    String DUREE_bon,
    String DATE_ECHEANCE,
    String Code_Soumissionaire,
    String MONTANT_Propose_Par_etat,
    String Montant_Propose_Par_Soumissionaire,
    String Montant_Adjuge,
    String Montant_Coupoun
  ) {
    Connection connection = null;
    PreparedStatement insertStatement = null;

    try {
      // Connexion à la base de données
      String url = "jdbc:oracle:thin:@localhost:1521/orcl";
      String username = "projetMaster";
      String passwordDB = "psw";
      connection = DriverManager.getConnection(url, username, passwordDB);

      // Préparation de la requête d'insertion
      String insertQuery =
        "INSERT INTO Dette (code_ISIN,DATE_DEB ,DUREE_bon ,DATE_ECHEANCE ,Code_Soumissionaire ,MONTANT_Propose_Par_etat ,Montant_Propose_Par_Soumissionaire ,Montant_Adjuge ,Montant_Coupoun) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

      insertStatement = connection.prepareStatement(insertQuery);
      insertStatement.setString(1, code_ISIN);
      insertStatement.setString(2, DATE_DEB);
      insertStatement.setString(3, DUREE_bon);
      insertStatement.setString(4, DATE_ECHEANCE);
      insertStatement.setString(5, Code_Soumissionaire);
      insertStatement.setString(6, MONTANT_Propose_Par_etat);
      insertStatement.setString(7, Montant_Propose_Par_Soumissionaire);
      insertStatement.setString(8, Montant_Adjuge);
      insertStatement.setString(9, Montant_Coupoun);
      int rowsAffected = insertStatement.executeUpdate();

      if (rowsAffected > 0) {
        return "Élément inséré avec succès dans la table Dette";
      } else {
        return "Aucun élément inséré dans la table Dette.";
      }
    } catch (SQLException e) {
      // Gestion des exceptions SQL
      e.printStackTrace();
      return (
        "Erreur lors de l'insertion de l'élément dans la table Dette " +
        e.getMessage()
      );
    } finally {
      // Fermeture des ressources
      try {
        if (insertStatement != null) {
          insertStatement.close();
        }
        if (connection != null) {
          connection.close();
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }



  public List<String> getAllActions() {
    List<String> Actions = new ArrayList<>();
    Connection connection = null;
    PreparedStatement selectStatement = null;

    try {
        // Connexion à la base de données
        String url = "jdbc:oracle:thin:@localhost:1521/orcl";
        String username = "projetMaster";
        String passwordDB = "psw";
        connection = DriverManager.getConnection(url, username, passwordDB);

        // Sélectionner tous les utilisateurs de la base de données
        String selectQuery = "SELECT * FROM Action";
        selectStatement = connection.prepareStatement(selectQuery);
        ResultSet resultSet = selectStatement.executeQuery();

        // Parcourir les résultats et ajouter chaque utilisateur à la liste
        while (resultSet.next()) {
            int CODE_PORTEF = resultSet.getInt("CODE_PORTEF");
            String Programme = resultSet.getString("PROGRAMME");
            String SousProgramme = resultSet.getString("SOUS_PROGRAMME");
            String Action = resultSet.getString("ACTION");
            String SousAction = resultSet.getString("SOUS_ACTION");
            String LibelleAction = resultSet.getString("LIBELLE_ACTION");

            // Construire une chaîne représentant l'utilisateur
            String ActionInfo = "CodePortef: " + CODE_PORTEF + ", Programme: " + Programme + ", SousProgramme: " + SousProgramme + ", Action: " + Action + ", SousAction: " + SousAction + ", LibelleAction: " + LibelleAction ;        

            Actions.add(ActionInfo);
        }

    } catch (SQLException e) {
        // Gestion des exceptions SQL
        e.printStackTrace();
    } finally {
        // Fermeture des ressources
        try {
            if (selectStatement != null) {
                selectStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    return Actions;
}



public List<String> getAllWilaya() {
  List<String> Wilaya = new ArrayList<>();
  Connection connection = null;
  PreparedStatement selectStatement = null;

  try {
      // Connexion à la base de données
      String url = "jdbc:oracle:thin:@localhost:1521/orcl";
      String username = "projetMaster";
      String passwordDB = "psw";
      connection = DriverManager.getConnection(url, username, passwordDB);

      // Sélectionner tous les utilisateurs de la base de données
      String selectQuery = "SELECT * FROM Wilayas";
      selectStatement = connection.prepareStatement(selectQuery);
      ResultSet resultSet = selectStatement.executeQuery();

      // Parcourir les résultats et ajouter chaque utilisateur à la liste
      while (resultSet.next()) {
          int CodeWilaya = resultSet.getInt("CODE_WILAYA");
          String libelleWilaya= resultSet.getString("LIBELLE_WILAYA");

          // Construire une chaîne représentant l'utilisateur
          String WilayaInfo = "CodeWilaya: " + CodeWilaya + ", libelleWilaya: " + libelleWilaya;        

          Wilaya.add(WilayaInfo);
      }

  } catch (SQLException e) {
      // Gestion des exceptions SQL
      e.printStackTrace();
  } finally {
      // Fermeture des ressources
      try {
          if (selectStatement != null) {
              selectStatement.close();
          }
          if (connection != null) {
              connection.close();
          }
      } catch (SQLException e) {
          e.printStackTrace();
      }
  }

  return Wilaya;
}




public List<String> getAllDette() {
  List<String> Dettes = new ArrayList<>();
  Connection connection = null;
  PreparedStatement selectStatement = null;

  try {
      // Connexion à la base de données
      String url = "jdbc:oracle:thin:@localhost:1521/orcl";
      String username = "projetMaster";
      String passwordDB = "psw";
      connection = DriverManager.getConnection(url, username, passwordDB);

      // Sélectionner tous les utilisateurs de la base de données
      String selectQuery = "SELECT * FROM Dette";
      selectStatement = connection.prepareStatement(selectQuery);
      ResultSet resultSet = selectStatement.executeQuery();
      // Parcourir les résultats et ajouter chaque utilisateur à la liste
      while (resultSet.next()) {
          int codeISIN = resultSet.getInt("code_ISIN");
          String DateDeb = resultSet.getString("DATE_DEB");
          String DureeBon = resultSet.getString("DUREE_bon");
          String DateEcheance = resultSet.getString("DATE_ECHEANCE");
          String CodeSoumissionaire = resultSet.getString("Code_Soumissionaire");
          String MontantProposeParEtat = resultSet.getString("MONTANT_Propose_Par_etat");
          String MontantProposeParSoumissionaire = resultSet.getString("Montant_Propose_Par_Soumissionaire");
          String MontantAdjuge = resultSet.getString("Montant_Adjuge");
          String MontantCoupoun = resultSet.getString("Montant_Coupoun");

          // Construire une chaîne représentant l'utilisateur
          String ActionInfo = "codeISIN: " + codeISIN + ", DateDeb: " + DateDeb + ", DureeBon: " + DureeBon + ", DateEcheance: " + DateEcheance + ", CodeSoumissionaire: " + CodeSoumissionaire + ", MontantProposeParEtat: " + MontantProposeParEtat  + ", MontantProposeParSoumissionaire: " + MontantProposeParSoumissionaire + ", MontantAdjuge: " + MontantAdjuge + ", MontantCoupoun: " + MontantCoupoun;    
          Dettes.add(ActionInfo);
      }

  } catch (SQLException e) {
      // Gestion des exceptions SQL
      e.printStackTrace();
  } finally {
      // Fermeture des ressources
      try {
          if (selectStatement != null) {
              selectStatement.close();
          }
          if (connection != null) {
              connection.close();
          }
      } catch (SQLException e) {
          e.printStackTrace();
      }
  }

  return Dettes;
}



public List<String> getAllRecettes() {
  List<String> Recettes = new ArrayList<>();
  Connection connection = null;
  PreparedStatement selectStatement = null;

  try {
      // Connexion à la base de données
      String url = "jdbc:oracle:thin:@localhost:1521/orcl";
      String username = "projetMaster";
      String passwordDB = "psw";
      connection = DriverManager.getConnection(url, username, passwordDB);

      // Sélectionner tous les utilisateurs de la base de données
      String selectQuery = "SELECT * FROM Recette";
      selectStatement = connection.prepareStatement(selectQuery);
      ResultSet resultSet = selectStatement.executeQuery();
      // Parcourir les résultats et ajouter chaque utilisateur à la liste
      while (resultSet.next()) {
          int Gestion = resultSet.getInt("GESTION");
          String Mois = resultSet.getString("MOIS");
          String CodeCompte = resultSet.getString("CODE_CPT");
          String libelleCompte = resultSet.getString("LIB_CPT_G");
          String PosteComptable = resultSet.getString("POSTE_C");
          String Montant = resultSet.getString("MT_MOIS");

          // Construire une chaîne représentant l'utilisateur
          String RecetteInfo = "Gestion: " + Gestion + ", Mois: " + Mois + ", CodeCompte: " + CodeCompte + ", libelleCompte: " + libelleCompte + ", PosteComptable: " + PosteComptable + ", Montant: " + Montant;    
          Recettes.add(RecetteInfo);
      }

  } catch (SQLException e) {
      // Gestion des exceptions SQL
      e.printStackTrace();
  } finally {
      // Fermeture des ressources
      try {
          if (selectStatement != null) {
              selectStatement.close();
          }
          if (connection != null) {
              connection.close();
          }
      } catch (SQLException e) {
          e.printStackTrace();
      }
  }

  return Recettes;
}






public List<String> getAllCredit() {
  List<String> Credits = new ArrayList<>();
  Connection connection = null;
  PreparedStatement selectStatement = null;

  try {
      // Connexion à la base de données
      String url = "jdbc:oracle:thin:@localhost:1521/orcl";
      String username = "projetMaster";
      String passwordDB = "psw";
      connection = DriverManager.getConnection(url, username, passwordDB);

      // Sélectionner tous les utilisateurs de la base de données
      String selectQuery = "SELECT * FROM Credit";
      selectStatement = connection.prepareStatement(selectQuery);
      ResultSet resultSet = selectStatement.executeQuery();
      // Parcourir les résultats et ajouter chaque utilisateur à la liste
      while (resultSet.next()) {
          String codeOrdonnateur = resultSet.getString("CODE_ORD");
          String CodePortef = resultSet.getString("CODE_PORTEF");
          String Programme = resultSet.getString("PROGRAMME");
          String SousProgramme = resultSet.getString("SOUS_PROGRAMME");
          String Action = resultSet.getString("ACTION");
          String SousAction = resultSet.getString("SOUS_ACTION");
          String AxeEconomique = resultSet.getString("AXE_ECO");
          String TotalCredits = resultSet.getString("TOT_CREDIT");
          String TotalCreditDEB = resultSet.getString("TOT_DEB");
          String Solde = resultSet.getString("SOLDE");

          // Construire une chaîne représentant l'utilisateur
          String CreditInfo = "codeOrdonnateur: " + codeOrdonnateur + ", CodePortef: " + CodePortef + ", Programme: " + Programme + ", SousProgramme: " + SousProgramme  + ", Action: " + Action + ", SousAction: " + SousAction + " , AxeEconomique: " + AxeEconomique + ", TotalCredits: " + TotalCredits + ", TotalCreditDEB: " + TotalCreditDEB + ", Solde: " + Solde;   
          Credits.add(CreditInfo);
      }

  } catch (SQLException e) {
      // Gestion des exceptions SQL
      e.printStackTrace();
  } finally {
      // Fermeture des ressources
      try {
          if (selectStatement != null) {
              selectStatement.close();
          }
          if (connection != null) {
              connection.close();
          }
      } catch (SQLException e) {
          e.printStackTrace();
      }
  }

  return Credits;
}



}
