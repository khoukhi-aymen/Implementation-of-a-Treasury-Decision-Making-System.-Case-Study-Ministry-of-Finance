import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Entrepot {



    /**********************************crédits réel****************************************** */

    public List<String> getTotalCreditsAllouePourChaquePortefeuilleReel(String birthday) {
        List<String> getTotalCreditsAllouePourChaquePortefeuilleReel = new ArrayList<>();
        Connection connection = null;
        PreparedStatement selectStatement = null;
        ResultSet resultSet = null;
    
        try {
            // Connexion à la base de données
            String url = "jdbc:oracle:thin:@localhost:1521:orcl";
            String username = "Master2IL";
            String passwordDB = "psw";
            connection = DriverManager.getConnection(url, username, passwordDB);
    
            // Formatage de la date
            String formattedBirthday = "";
            try {
                LocalDate localDate = LocalDate.parse(birthday);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                formattedBirthday = localDate.format(formatter);
            } catch (DateTimeParseException e) {
                e.printStackTrace();
            }
    
            // Sélectionner tous les utilisateurs de la base de données
            String selectQuery = "SELECT DA.code_portef AS code_portef, " +
                "SUM(TO_NUMBER(FC.CREDIT_Total, '9999999999.999')) AS TotalCreditDEB " +
                "FROM FaitCredit FC, DimensionActivite DA, Dimensiontemps DT " +
                "WHERE FC.ID_ACTIVITE = DA.ID_ACTIVITE AND FC.Id_Temps = DT.Id_Temps " +
                "AND ( " +
                "    (DT.Jour = TO_DATE(?, 'DD/MM/YYYY') AND TO_CHAR(TO_DATE(?, 'DD/MM/YYYY'), 'DD') <> '01') OR " +
                "    (TO_CHAR(TO_DATE(?, 'DD/MM/YYYY'), 'DD') = '01' AND EXTRACT(MONTH FROM DT.Jour) = EXTRACT(MONTH FROM TO_DATE(?, 'DD/MM/YYYY')) AND EXTRACT(YEAR FROM DT.Jour) = EXTRACT(YEAR FROM TO_DATE(?, 'DD/MM/YYYY'))) OR " +
                "    (TO_CHAR(TO_DATE(?, 'DD/MM/YYYY'), 'DD/MM') = '01/01' AND EXTRACT(YEAR FROM DT.Jour) = EXTRACT(YEAR FROM TO_DATE(?, 'DD/MM/YYYY'))) " +
                ") " +
                "GROUP BY DA.code_portef ";
    
            selectStatement = connection.prepareStatement(selectQuery);
            // Ajout des paramètres 'birthday' à la requête préparée
            for (int i = 1; i <= 7; i++) {
                selectStatement.setString(i, formattedBirthday);
            }
            
            resultSet = selectStatement.executeQuery();
    
            // Parcourir les résultats et ajouter chaque utilisateur à la liste
            while (resultSet.next()) {
                String code_portef = resultSet.getString("code_portef");
                BigDecimal totalCreditDEB = resultSet.getBigDecimal("TotalCreditDEB");
                // Construire une chaîne représentant tout le résultat
                String resultat = "Portefeuille: " + code_portef +", totalCreditDEB: "+ totalCreditDEB+", DATE: "+birthday;
                getTotalCreditsAllouePourChaquePortefeuilleReel.add(resultat);
            }
    
        } catch (SQLException e) {
            // Gestion des exceptions SQL
            e.printStackTrace();
        } finally {
            // Fermeture des ressources
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
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
    
        return getTotalCreditsAllouePourChaquePortefeuilleReel;
    }
    





    public List<String> getTotalCreditsAllouePourChaqueProgrammeReel(String birthday) {
        List<String> getTotalCreditsAllouePourChaqueProgrammeReel = new ArrayList<>();
        Connection connection = null;
        PreparedStatement selectStatement = null;
        ResultSet resultSet = null;

        try {
            // Connexion à la base de données
            String url = "jdbc:oracle:thin:@localhost:1521:orcl";
            String username = "Master2IL";
            String passwordDB = "psw";
            connection = DriverManager.getConnection(url, username, passwordDB);

            // Formatage de la date
            String formattedBirthday = "";
            try {
                LocalDate localDate = LocalDate.parse(birthday);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                formattedBirthday = localDate.format(formatter);
            } catch (DateTimeParseException e) {
                e.printStackTrace();
            }

            // Sélectionner tous les utilisateurs de la base de données
            String selectQuery = "SELECT DA.code_portef AS code_portef, da.programme AS programme, " +
                    "SUM(TO_NUMBER(FC.CREDIT_Total, '9999999999.999')) AS TotalCreditDEB " +
                    "FROM FaitCredit FC, DimensionActivite DA, Dimensiontemps DT " +
                    "WHERE FC.ID_ACTIVITE = DA.ID_ACTIVITE AND FC.Id_Temps = DT.Id_Temps " +
                    "AND ( " +
                    "    (DT.Jour = TO_DATE(?, 'DD/MM/YYYY') AND TO_CHAR(TO_DATE(?, 'DD/MM/YYYY'), 'DD') <> '01') OR " +
                    "    (TO_CHAR(TO_DATE(?, 'DD/MM/YYYY'), 'DD') = '01' AND EXTRACT(MONTH FROM DT.Jour) = EXTRACT(MONTH FROM TO_DATE(?, 'DD/MM/YYYY')) AND EXTRACT(YEAR FROM DT.Jour) = EXTRACT(YEAR FROM TO_DATE(?, 'DD/MM/YYYY'))) OR " +
                    "    (TO_CHAR(TO_DATE(?, 'DD/MM/YYYY'), 'DD/MM') = '01/01' AND EXTRACT(YEAR FROM DT.Jour) = EXTRACT(YEAR FROM TO_DATE(?, 'DD/MM/YYYY'))) " +
                    ") " +
                    "GROUP BY DA.code_portef, da.programme";

            selectStatement = connection.prepareStatement(selectQuery);

            // Ajout des paramètres 'birthday' à la requête préparée
            for (int i = 1; i <= 7; i++) {
                selectStatement.setString(i, formattedBirthday);
            }

            resultSet = selectStatement.executeQuery();

            // Parcourir les résultats et ajouter chaque utilisateur à la liste
            while (resultSet.next()) {
                String code_portef = resultSet.getString("code_portef");
                String programme = resultSet.getString("programme");
                BigDecimal totalCreditDEB = resultSet.getBigDecimal("TotalCreditDEB");
                // Construire une chaîne représentant tout le résultat
                String resultat = "Programme: " + code_portef + programme + ", totalCreditDEB: " + totalCreditDEB+ ", DATE: " + birthday;
                getTotalCreditsAllouePourChaqueProgrammeReel.add(resultat);
            }

        } catch (SQLException e) {
            // Gestion des exceptions SQL
            e.printStackTrace();
        } finally {
            // Fermeture des ressources
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
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

        return getTotalCreditsAllouePourChaqueProgrammeReel;
    }
    



    public List<String> getTotalCreditsAllouePourChaqueSousProgrammeReel(String birthday) {
        List<String> getTotalCreditsAllouePourChaqueSousProgrammeReel = new ArrayList<>();
        Connection connection = null;
        PreparedStatement selectStatement = null;
        ResultSet resultSet = null;

        try {
            // Connexion à la base de données
            String url = "jdbc:oracle:thin:@localhost:1521:orcl";
            String username = "Master2IL";
            String passwordDB = "psw";
            connection = DriverManager.getConnection(url, username, passwordDB);

            // Formatage de la date
            String formattedBirthday = "";
            try {
                LocalDate localDate = LocalDate.parse(birthday);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                formattedBirthday = localDate.format(formatter);
            } catch (DateTimeParseException e) {
                e.printStackTrace();
            }

            // Sélectionner tous les utilisateurs de la base de données
            String selectQuery = "SELECT DA.code_portef AS code_portef, DA.programme AS programme, DA.sous_programme AS SousProgramme, "
                    +
                    "SUM(TO_NUMBER(FC.CREDIT_Total, '9999999999.999')) AS TotalCreditDEB " +
                    "FROM FaitCredit FC, DimensionActivite DA, Dimensiontemps DT " +
                    "WHERE FC.ID_ACTIVITE = DA.ID_ACTIVITE AND FC.Id_Temps = DT.Id_Temps " +
                    "AND ( " +
                    "    (DT.Jour = TO_DATE(?, 'DD/MM/YYYY') AND TO_CHAR(TO_DATE(?, 'DD/MM/YYYY'), 'DD') <> '01') OR " +
                    "    (TO_CHAR(TO_DATE(?, 'DD/MM/YYYY'), 'DD') = '01' AND EXTRACT(MONTH FROM DT.Jour) = EXTRACT(MONTH FROM TO_DATE(?, 'DD/MM/YYYY')) AND EXTRACT(YEAR FROM DT.Jour) = EXTRACT(YEAR FROM TO_DATE(?, 'DD/MM/YYYY'))) OR " +
                    "    (TO_CHAR(TO_DATE(?, 'DD/MM/YYYY'), 'DD/MM') = '01/01' AND EXTRACT(YEAR FROM DT.Jour) = EXTRACT(YEAR FROM TO_DATE(?, 'DD/MM/YYYY'))) " +
                    ") " +
                    "GROUP BY DA.code_portef, DA.programme, DA.sous_programme";

            selectStatement = connection.prepareStatement(selectQuery);

            // Ajout des paramètres 'birthday' à la requête préparée
            for (int i = 1; i <= 7; i++) {
                selectStatement.setString(i, formattedBirthday);
            }

            resultSet = selectStatement.executeQuery();

            // Parcourir les résultats et ajouter chaque utilisateur à la liste
            while (resultSet.next()) {
                String code_portef = resultSet.getString("code_portef");
                String programme = resultSet.getString("programme");
                String sousProgramme = resultSet.getString("SousProgramme");
                BigDecimal totalCreditDEB = resultSet.getBigDecimal("TotalCreditDEB");
                // Construire une chaîne représentant tout le résultat
                String resultat = "SousProgramme: " + code_portef + programme + sousProgramme
                        + ", totalCreditDEB: " + totalCreditDEB + ", DATE: " + birthday;
                getTotalCreditsAllouePourChaqueSousProgrammeReel.add(resultat);
            }

        } catch (SQLException e) {
            // Gestion des exceptions SQL
            e.printStackTrace();
        } finally {
            // Fermeture des ressources
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
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

        return getTotalCreditsAllouePourChaqueSousProgrammeReel;
    }
    




    public List<String> getTotalCreditsAllouePourChaqueTitreReel(String birthday) {
        List<String> getTotalCreditsAllouePourChaqueTitreReel = new ArrayList<>();
        Connection connection = null;
        PreparedStatement selectStatement = null;
        ResultSet resultSet = null;

        try {
            // Connexion à la base de données
            String url = "jdbc:oracle:thin:@localhost:1521:orcl";
            String username = "Master2IL";
            String passwordDB = "psw";
            connection = DriverManager.getConnection(url, username, passwordDB);

            // Formatage de la date
            String formattedBirthday = "";
            try {
                LocalDate localDate = LocalDate.parse(birthday);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                formattedBirthday = localDate.format(formatter);
            } catch (DateTimeParseException e) {
                e.printStackTrace();
            }

            // Sélectionner tous les utilisateurs de la base de données
            String selectQuery = "SELECT SUBSTR(DN.AXE_ECO, 1, 1) AS Titre, " +
                    "SUM(TO_NUMBER(FC.CREDIT_Total, '9999999999.999')) AS TotalCreditDEB " +
                    "FROM FaitCredit FC, DimensionNatureEconomic DN, Dimensiontemps DT " +
                    "WHERE FC.Id_N_economic = DN.Id_N_economic AND FC.Id_Temps = DT.Id_Temps " +
                    "AND ( " +
                    "    (DT.Jour = TO_DATE(?, 'DD/MM/YYYY') AND TO_CHAR(TO_DATE(?, 'DD/MM/YYYY'), 'DD') <> '01') OR " +
                    "    (TO_CHAR(TO_DATE(?, 'DD/MM/YYYY'), 'DD') = '01' AND EXTRACT(MONTH FROM DT.Jour) = EXTRACT(MONTH FROM TO_DATE(?, 'DD/MM/YYYY')) AND EXTRACT(YEAR FROM DT.Jour) = EXTRACT(YEAR FROM TO_DATE(?, 'DD/MM/YYYY'))) OR " +
                    "    (TO_CHAR(TO_DATE(?, 'DD/MM/YYYY'), 'DD/MM') = '01/01' AND EXTRACT(YEAR FROM DT.Jour) = EXTRACT(YEAR FROM TO_DATE(?, 'DD/MM/YYYY'))) " +
                    ") " +
                    "GROUP BY SUBSTR(DN.AXE_ECO, 1, 1)";

            selectStatement = connection.prepareStatement(selectQuery);

            // Ajout des paramètres 'birthday' à la requête préparée
            for (int i = 1; i <= 7; i++) {
                selectStatement.setString(i, formattedBirthday);
            }

            resultSet = selectStatement.executeQuery();

            // Parcourir les résultats et ajouter chaque utilisateur à la liste
            while (resultSet.next()) {
                String Titre = resultSet.getString("Titre");
                BigDecimal totalCreditDEB = resultSet.getBigDecimal("TotalCreditDEB");
                // Construire une chaîne représentant tout le résultat
                String resultat = "Titre: T" + Titre + ", totalCreditDEB: " + totalCreditDEB + ", DATE: " + birthday;
                getTotalCreditsAllouePourChaqueTitreReel.add(resultat);
            }

        } catch (SQLException e) {
            // Gestion des exceptions SQL
            e.printStackTrace();
        } finally {
            // Fermeture des ressources
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
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

        return getTotalCreditsAllouePourChaqueTitreReel;
    }
    




    public List<String> getTotalCreditsAllouePourChaqueCategorieReel(String birthday) {
        List<String> getTotalCreditsAllouePourChaqueCategorieReel = new ArrayList<>();
        Connection connection = null;
        PreparedStatement selectStatement = null;
        ResultSet resultSet = null;

        try {
            // Connexion à la base de données
            String url = "jdbc:oracle:thin:@localhost:1521:orcl";
            String username = "Master2IL";
            String passwordDB = "psw";
            connection = DriverManager.getConnection(url, username, passwordDB);

            // Formatage de la date
            String formattedBirthday = "";
            try {
                LocalDate localDate = LocalDate.parse(birthday);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                formattedBirthday = localDate.format(formatter);
            } catch (DateTimeParseException e) {
                e.printStackTrace();
            }

            // Sélectionner tous les utilisateurs de la base de données
            String selectQuery = "SELECT SUBSTR(DN.AXE_ECO, 1, 2) AS Categorie, " +
                    "SUM(TO_NUMBER(FC.CREDIT_Total, '9999999999.999')) AS TotalCreditDEB " +
                    "FROM FaitCredit FC, DimensionNatureEconomic DN, Dimensiontemps DT " +
                    "WHERE FC.Id_N_economic = DN.Id_N_economic AND FC.Id_Temps = DT.Id_Temps " +
                    "AND ( " +
                    "    (DT.Jour = TO_DATE(?, 'DD/MM/YYYY') AND TO_CHAR(TO_DATE(?, 'DD/MM/YYYY'), 'DD') <> '01') OR " +
                    "    (TO_CHAR(TO_DATE(?, 'DD/MM/YYYY'), 'DD') = '01' AND EXTRACT(MONTH FROM DT.Jour) = EXTRACT(MONTH FROM TO_DATE(?, 'DD/MM/YYYY')) AND EXTRACT(YEAR FROM DT.Jour) = EXTRACT(YEAR FROM TO_DATE(?, 'DD/MM/YYYY'))) OR " +
                    "    (TO_CHAR(TO_DATE(?, 'DD/MM/YYYY'), 'DD/MM') = '01/01' AND EXTRACT(YEAR FROM DT.Jour) = EXTRACT(YEAR FROM TO_DATE(?, 'DD/MM/YYYY'))) " +
                    ") " +
                    "GROUP BY SUBSTR(DN.AXE_ECO, 1, 2)";

            selectStatement = connection.prepareStatement(selectQuery);

            // Ajout des paramètres 'birthday' à la requête préparée
            for (int i = 1; i <= 7; i++) {
                selectStatement.setString(i, formattedBirthday);
            }

            resultSet = selectStatement.executeQuery();

            // Parcourir les résultats et ajouter chaque utilisateur à la liste
            while (resultSet.next()) {
                String Categorie = resultSet.getString("Categorie");
                BigDecimal totalCreditDEB = resultSet.getBigDecimal("TotalCreditDEB");
                // Construire une chaîne représentant tout le résultat
                String resultat = "Categorie: " + Categorie + ", totalCreditDEB: " + totalCreditDEB + ", DATE: "
                        + birthday;
                getTotalCreditsAllouePourChaqueCategorieReel.add(resultat);
            }

        } catch (SQLException e) {
            // Gestion des exceptions SQL
            e.printStackTrace();
        } finally {
            // Fermeture des ressources
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
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

        return getTotalCreditsAllouePourChaqueCategorieReel;
    }
    



    /**********************************crédits réel****************************************** */


    /**********************************crédits prévu****************************************** */


    


    public List<String> getTotalCreditsAllouePourChaquePortefeuillePrevu(String scriptPath, List<String> inputData, String thirdParameter) {
        List<String> getTotalCreditsAllouePourChaquePortefeuillePrevu = new ArrayList<>();
        try {
            // Créer le processus pour exécuter le script Python
            ProcessBuilder pb = new ProcessBuilder("python", scriptPath);
    
            // Convertir la liste en une seule chaîne séparée par des barres verticales (pipes) pour passer en tant qu'argument au script Python
            String inputString = String.join("|", inputData);
    
            // Passer les chaînes de données comme arguments au script Python
            pb.command().add(inputString);
            pb.command().add(thirdParameter);
    
            // Rediriger la sortie standard du processus vers un flux
            pb.redirectErrorStream(true);
    
            // Démarrer le processus
            Process process = pb.start();
    
            // Lire la sortie standard du processus
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                // Ajouter chaque ligne de sortie à la liste de prédictions
                getTotalCreditsAllouePourChaquePortefeuillePrevu.add(line);
            }
    
            // Attendre la fin du processus
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.err.println("Erreur lors de l'exécution du script Python.");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    
        return getTotalCreditsAllouePourChaquePortefeuillePrevu;
    }
    





    public  List<String> getTotalCreditsAllouePourChaqueProgrammePrevu(String scriptPath, List<String> inputData, String thirdParameter) {
        List<String> getTotalCreditsAllouePourChaqueProgrammePrevu = new ArrayList<>();
        try {
            // Créer le processus pour exécuter le script Python
            ProcessBuilder pb = new ProcessBuilder("python", scriptPath);
    
            // Convertir la liste en une seule chaîne séparée par des barres verticales (pipes) pour passer en tant qu'argument au script Python
            String inputString = String.join("|", inputData);
    
            // Passer les chaînes de données comme arguments au script Python
            pb.command().add(inputString);
            pb.command().add(thirdParameter);
    
            // Rediriger la sortie standard du processus vers un flux
            pb.redirectErrorStream(true);
    
            // Démarrer le processus
            Process process = pb.start();
    
            // Lire la sortie standard du processus
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                
                // Ajouter chaque ligne de sortie à la liste de prédictions
                getTotalCreditsAllouePourChaqueProgrammePrevu.add(line);
            }

            // Attendre la fin du processus
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.err.println("Erreur lors de l'exécution du script Python.");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return getTotalCreditsAllouePourChaqueProgrammePrevu;
    }



    public  List<String> getTotalCreditsAllouePourChaqueSousProgrammePrevu(String scriptPath, List<String> inputData,String thirdParameter) {
        List<String> getTotalCreditsAllouePourChaqueSousProgrammePrevu = new ArrayList<>();
        try {
            // Créer le processus pour exécuter le script Python
            ProcessBuilder pb = new ProcessBuilder("python", scriptPath);
    
            // Convertir la liste en une seule chaîne séparée par des barres verticales (pipes) pour passer en tant qu'argument au script Python
            String inputString = String.join("|", inputData);
    
            // Passer les chaînes de données comme arguments au script Python
            pb.command().add(inputString);
            pb.command().add(thirdParameter);
    
            // Rediriger la sortie standard du processus vers un flux
            pb.redirectErrorStream(true);
    
            // Démarrer le processus
            Process process = pb.start();
    
            // Lire la sortie standard du processus
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                
                // Ajouter chaque ligne de sortie à la liste de prédictions
                getTotalCreditsAllouePourChaqueSousProgrammePrevu.add(line);
            }

            // Attendre la fin du processus
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.err.println("Erreur lors de l'exécution du script Python.");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return getTotalCreditsAllouePourChaqueSousProgrammePrevu;
    }





    public  List<String> getTotalCreditsAllouePourChaqueTitrePrevu(String scriptPath, List<String> inputData,String thirdParameter) {
        List<String> getTotalCreditsAllouePourChaqueTitrePrevu = new ArrayList<>();
        try {
            // Créer le processus pour exécuter le script Python
            ProcessBuilder pb = new ProcessBuilder("python", scriptPath);
    
            // Convertir la liste en une seule chaîne séparée par des barres verticales (pipes) pour passer en tant qu'argument au script Python
            String inputString = String.join("|", inputData);
    
            // Passer les chaînes de données comme arguments au script Python
            pb.command().add(inputString);
            pb.command().add(thirdParameter);
    
            // Rediriger la sortie standard du processus vers un flux
            pb.redirectErrorStream(true);
    
            // Démarrer le processus
            Process process = pb.start();
    
            // Lire la sortie standard du processus
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                
                // Ajouter chaque ligne de sortie à la liste de prédictions
                getTotalCreditsAllouePourChaqueTitrePrevu.add(line);
            }

            // Attendre la fin du processus
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.err.println("Erreur lors de l'exécution du script Python.");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return getTotalCreditsAllouePourChaqueTitrePrevu;
    }






    public  List<String> getTotalCreditsAllouePourChaqueCategoriePrevu(String scriptPath, List<String> inputData,String thirdParameter) {
        List<String> getTotalCreditsAllouePourChaqueCategoriePrevu = new ArrayList<>();
        try {
             // Créer le processus pour exécuter le script Python
             ProcessBuilder pb = new ProcessBuilder("python", scriptPath);
    
             // Convertir la liste en une seule chaîne séparée par des barres verticales (pipes) pour passer en tant qu'argument au script Python
             String inputString = String.join("|", inputData);
     
             // Passer les chaînes de données comme arguments au script Python
             pb.command().add(inputString);
             pb.command().add(thirdParameter);
     
             // Rediriger la sortie standard du processus vers un flux
             pb.redirectErrorStream(true);
     
             // Démarrer le processus
             Process process = pb.start();
     
             // Lire la sortie standard du processus
             BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
             String line;
            while ((line = reader.readLine()) != null) {
                
                // Ajouter chaque ligne de sortie à la liste de prédictions
                getTotalCreditsAllouePourChaqueCategoriePrevu.add(line);
            }

            // Attendre la fin du processus
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.err.println("Erreur lors de l'exécution du script Python.");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return getTotalCreditsAllouePourChaqueCategoriePrevu;
    }


    /**********************************crédits prévu****************************************** */

    /**********************************Dépenses réel****************************************** */


    public List<String> getTotalMontantdepensePourChaquePortefeuilleReel(String birthday) {
        List<String> getTotalMontantdepensePourChaquePortefeuilleReel = new ArrayList<>();
        Connection connection = null;
        PreparedStatement selectStatement = null;
        ResultSet resultSet = null;

        try {
            // Connexion à la base de données
            String url = "jdbc:oracle:thin:@localhost:1521:orcl";
            String username = "Master2IL";
            String passwordDB = "psw";
            connection = DriverManager.getConnection(url, username, passwordDB);


            // Formatage de la date
            String formattedBirthday = "";
            try {
                LocalDate localDate = LocalDate.parse(birthday);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                formattedBirthday = localDate.format(formatter);
            } catch (DateTimeParseException e) {
                e.printStackTrace();
            }

            // Sélectionner tous les utilisateurs de la base de données
            String selectQuery = " SELECT DA.code_portef As code_portef, " +
                    "SUM(TO_NUMBER(FD.MONTANT, '9999999999.999')) AS TotalMontantDepense " +
                    "FROM FaitDepenses FD, DimensionActivite DA,Dimensionmandat DM , Dimensiontemps DT " +
                    "WHERE FD.ID_ACTIVITE = DA.ID_ACTIVITE AND FD.ID_MANDAT = DM.ID_MANDAT AND DM.status_mandat ='REGLE' AND FD.Id_Temps = DT.Id_Temps "+
                    "AND ( " +
                    "    (DT.Jour = TO_DATE(?, 'DD/MM/YYYY') AND TO_CHAR(TO_DATE(?, 'DD/MM/YYYY'), 'DD') <> '01') OR " +
                    "    (TO_CHAR(TO_DATE(?, 'DD/MM/YYYY'), 'DD') = '01' AND EXTRACT(MONTH FROM DT.Jour) = EXTRACT(MONTH FROM TO_DATE(?, 'DD/MM/YYYY')) AND EXTRACT(YEAR FROM DT.Jour) = EXTRACT(YEAR FROM TO_DATE(?, 'DD/MM/YYYY'))) OR " +
                    "    (TO_CHAR(TO_DATE(?, 'DD/MM/YYYY'), 'DD/MM') = '01/01' AND EXTRACT(YEAR FROM DT.Jour) = EXTRACT(YEAR FROM TO_DATE(?, 'DD/MM/YYYY'))) " +
                    ") " +
                    "GROUP BY DA.code_portef";


            selectStatement = connection.prepareStatement(selectQuery);

            // Ajout des paramètres 'birthday' à la requête préparée
            for (int i = 1; i <= 7; i++) {
                selectStatement.setString(i, formattedBirthday);
            }

            resultSet = selectStatement.executeQuery();

            // Parcourir les résultats et ajouter chaque utilisateur à la liste
            while (resultSet.next()) {
                String code_portef = resultSet.getString("code_portef");
                BigDecimal TotalMontantDepense = resultSet.getBigDecimal("TotalMontantDepense");
                // Construire une chaîne représentant tout le résultat
                String resultat = "Portefeuille: " + code_portef +", TotalMontantDepense: "+ TotalMontantDepense+", DATE: "+birthday;
                getTotalMontantdepensePourChaquePortefeuilleReel.add(resultat);
            }

        } catch (SQLException e) {
            // Gestion des exceptions SQL
            e.printStackTrace();
        } finally {
            // Fermeture des ressources
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
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

        return getTotalMontantdepensePourChaquePortefeuilleReel;
    }





    public List<String> getTotalMontantdepensePourChaqueProgrammeReel(String birthday) {
        List<String> getTotalMontantdepensePourChaqueProgrammeReel = new ArrayList<>();
        Connection connection = null;
        PreparedStatement selectStatement = null;
        ResultSet resultSet = null;

        try {
            // Connexion à la base de données
            String url = "jdbc:oracle:thin:@localhost:1521:orcl";
            String username = "Master2IL";
            String passwordDB = "psw";
            connection = DriverManager.getConnection(url, username, passwordDB);


            // Formatage de la date
            String formattedBirthday = "";
            try {
                LocalDate localDate = LocalDate.parse(birthday);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                formattedBirthday = localDate.format(formatter);
            } catch (DateTimeParseException e) {
                e.printStackTrace();
            }

            // Sélectionner tous les utilisateurs de la base de données
            String selectQuery = " SELECT DA.code_portef As code_portef, da.programme AS programme, " +
                    "SUM(TO_NUMBER(FD.MONTANT, '9999999999.999')) AS TotalMontantDepense " +
                    "FROM FaitDepenses FD, DimensionActivite DA,Dimensionmandat DM , Dimensiontemps DT " +
                    "WHERE FD.ID_ACTIVITE = DA.ID_ACTIVITE AND FD.ID_MANDAT = DM.ID_MANDAT AND DM.status_mandat ='REGLE' AND FD.Id_Temps = DT.Id_Temps "+
                    "AND ( " +
                    "    (DT.Jour = TO_DATE(?, 'DD/MM/YYYY') AND TO_CHAR(TO_DATE(?, 'DD/MM/YYYY'), 'DD') <> '01') OR " +
                    "    (TO_CHAR(TO_DATE(?, 'DD/MM/YYYY'), 'DD') = '01' AND EXTRACT(MONTH FROM DT.Jour) = EXTRACT(MONTH FROM TO_DATE(?, 'DD/MM/YYYY')) AND EXTRACT(YEAR FROM DT.Jour) = EXTRACT(YEAR FROM TO_DATE(?, 'DD/MM/YYYY'))) OR " +
                    "    (TO_CHAR(TO_DATE(?, 'DD/MM/YYYY'), 'DD/MM') = '01/01' AND EXTRACT(YEAR FROM DT.Jour) = EXTRACT(YEAR FROM TO_DATE(?, 'DD/MM/YYYY'))) " +
                    ") " +
                    "GROUP BY DA.code_portef, da.programme";


            selectStatement = connection.prepareStatement(selectQuery);

            // Ajout des paramètres 'birthday' à la requête préparée
            for (int i = 1; i <= 7; i++) {
                selectStatement.setString(i, formattedBirthday);
            }

            resultSet = selectStatement.executeQuery();

            // Parcourir les résultats et ajouter chaque utilisateur à la liste
            while (resultSet.next()) {
                String code_portef = resultSet.getString("code_portef");
                String Programme = resultSet.getString("programme");
                BigDecimal TotalMontantDepense = resultSet.getBigDecimal("TotalMontantDepense");
                // Construire une chaîne représentant tout le résultat
                String resultat = "Programme: " + code_portef + Programme +", TotalMontantDepense: "+ TotalMontantDepense+", DATE: "+birthday;
                getTotalMontantdepensePourChaqueProgrammeReel.add(resultat);
            }

        } catch (SQLException e) {
            // Gestion des exceptions SQL
            e.printStackTrace();
        } finally {
            // Fermeture des ressources
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
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

        return getTotalMontantdepensePourChaqueProgrammeReel;
    }




    public List<String> getTotalMontantdepensePourChaqueSousProgrammeReel(String birthday) {
        List<String> getTotalMontantdepensePourChaqueSousProgrammeReel = new ArrayList<>();
        Connection connection = null;
        PreparedStatement selectStatement = null;
        ResultSet resultSet = null;

        try {
            // Connexion à la base de données
            String url = "jdbc:oracle:thin:@localhost:1521:orcl";
            String username = "Master2IL";
            String passwordDB = "psw";
            connection = DriverManager.getConnection(url, username, passwordDB);


            // Formatage de la date
            String formattedBirthday = "";
            try {
                LocalDate localDate = LocalDate.parse(birthday);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                formattedBirthday = localDate.format(formatter);
            } catch (DateTimeParseException e) {
                e.printStackTrace();
            }

            // Sélectionner tous les utilisateurs de la base de données
            String selectQuery = " SELECT DA.code_portef As code_portef, da.programme AS programme, da.sous_programme AS SousProgramme,  " +
                    "SUM(TO_NUMBER(FD.MONTANT, '9999999999.999')) AS TotalMontantDepense " +
                    "FROM FaitDepenses FD, DimensionActivite DA,Dimensionmandat DM , Dimensiontemps DT " +
                    "WHERE FD.ID_ACTIVITE = DA.ID_ACTIVITE AND FD.ID_MANDAT = DM.ID_MANDAT AND DM.status_mandat ='REGLE' AND FD.Id_Temps = DT.Id_Temps "+
                    "AND ( " +
                    "    (DT.Jour = TO_DATE(?, 'DD/MM/YYYY') AND TO_CHAR(TO_DATE(?, 'DD/MM/YYYY'), 'DD') <> '01') OR " +
                    "    (TO_CHAR(TO_DATE(?, 'DD/MM/YYYY'), 'DD') = '01' AND EXTRACT(MONTH FROM DT.Jour) = EXTRACT(MONTH FROM TO_DATE(?, 'DD/MM/YYYY')) AND EXTRACT(YEAR FROM DT.Jour) = EXTRACT(YEAR FROM TO_DATE(?, 'DD/MM/YYYY'))) OR " +
                    "    (TO_CHAR(TO_DATE(?, 'DD/MM/YYYY'), 'DD/MM') = '01/01' AND EXTRACT(YEAR FROM DT.Jour) = EXTRACT(YEAR FROM TO_DATE(?, 'DD/MM/YYYY'))) " +
                    ") " +
                    "GROUP BY DA.code_portef,da.programme,da.sous_programme";


            selectStatement = connection.prepareStatement(selectQuery);

            // Ajout des paramètres 'birthday' à la requête préparée
            for (int i = 1; i <= 7; i++) {
                selectStatement.setString(i, formattedBirthday);
            }

            resultSet = selectStatement.executeQuery();

            // Parcourir les résultats et ajouter chaque utilisateur à la liste
            while (resultSet.next()) {
                String code_portef = resultSet.getString("code_portef");
                String Programme = resultSet.getString("programme");
                String SousProgramme = resultSet.getString("SousProgramme");
                BigDecimal TotalMontantDepense = resultSet.getBigDecimal("TotalMontantDepense");
                // Construire une chaîne représentant tout le résultat
                String resultat = "SousProgramme: " + code_portef + Programme + SousProgramme + ", TotalMontantDepense: "+ TotalMontantDepense+", DATE: "+birthday;
                getTotalMontantdepensePourChaqueSousProgrammeReel.add(resultat);
            }

        } catch (SQLException e) {
            // Gestion des exceptions SQL
            e.printStackTrace();
        } finally {
            // Fermeture des ressources
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
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

        return getTotalMontantdepensePourChaqueSousProgrammeReel;
    }




    public List<String> getTotalMontantdepensePourChaqueTitreReel(String birthday) {
        List<String> getTotalMontantdepensePourChaqueTitreReel = new ArrayList<>();
        Connection connection = null;
        PreparedStatement selectStatement = null;
        ResultSet resultSet = null;

        try {
            // Connexion à la base de données
            String url = "jdbc:oracle:thin:@localhost:1521:orcl";
            String username = "Master2IL";
            String passwordDB = "psw";
            connection = DriverManager.getConnection(url, username, passwordDB);


            // Formatage de la date
            String formattedBirthday = "";
            try {
                LocalDate localDate = LocalDate.parse(birthday);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                formattedBirthday = localDate.format(formatter);
            } catch (DateTimeParseException e) {
                e.printStackTrace();
            }

            // Sélectionner tous les utilisateurs de la base de données
            String selectQuery = " SELECT  SUBSTR(DN.AXE_ECO, 1, 1) As Titre,  " +
                    "SUM(TO_NUMBER(FD.MONTANT, '9999999999.999')) AS TotalMontantDepense " +
                    "FROM FaitDepenses FD, DimensionNatureEconomic DN, Dimensionmandat DM , Dimensiontemps DT " +
                    "WHERE FD.Id_N_economic = DN.Id_N_economic AND FD.ID_MANDAT = DM.ID_MANDAT AND DM.status_mandat ='REGLE' AND FD.Id_Temps = DT.Id_Temps "+
                    "AND ( " +
                    "    (DT.Jour = TO_DATE(?, 'DD/MM/YYYY') AND TO_CHAR(TO_DATE(?, 'DD/MM/YYYY'), 'DD') <> '01') OR " +
                    "    (TO_CHAR(TO_DATE(?, 'DD/MM/YYYY'), 'DD') = '01' AND EXTRACT(MONTH FROM DT.Jour) = EXTRACT(MONTH FROM TO_DATE(?, 'DD/MM/YYYY')) AND EXTRACT(YEAR FROM DT.Jour) = EXTRACT(YEAR FROM TO_DATE(?, 'DD/MM/YYYY'))) OR " +
                    "    (TO_CHAR(TO_DATE(?, 'DD/MM/YYYY'), 'DD/MM') = '01/01' AND EXTRACT(YEAR FROM DT.Jour) = EXTRACT(YEAR FROM TO_DATE(?, 'DD/MM/YYYY'))) " +
                    ") " +
                    "GROUP BY SUBSTR(DN.AXE_ECO, 1, 1)";



            selectStatement = connection.prepareStatement(selectQuery);

            // Ajout des paramètres 'birthday' à la requête préparée
            for (int i = 1; i <= 7; i++) {
                selectStatement.setString(i, formattedBirthday);
            }

            resultSet = selectStatement.executeQuery();

            // Parcourir les résultats et ajouter chaque utilisateur à la liste
            while (resultSet.next()) {
                String Titre = resultSet.getString("Titre");
                BigDecimal TotalMontantDepense = resultSet.getBigDecimal("TotalMontantDepense");
                // Construire une chaîne représentant tout le résultat
                String resultat = "Titre: T" + Titre + ", TotalMontantDepense: "+ TotalMontantDepense+", DATE: "+birthday;
                getTotalMontantdepensePourChaqueTitreReel.add(resultat);
            }

        } catch (SQLException e) {
            // Gestion des exceptions SQL
            e.printStackTrace();
        } finally {
            // Fermeture des ressources
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
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

        return getTotalMontantdepensePourChaqueTitreReel;
    }




    public List<String> getTotalMontantdepensePourChaqueCategorieReel(String birthday) {
        List<String> getTotalMontantdepensePourChaqueCategorieReel = new ArrayList<>();
        Connection connection = null;
        PreparedStatement selectStatement = null;
        ResultSet resultSet = null;

        try {
            // Connexion à la base de données
            String url = "jdbc:oracle:thin:@localhost:1521:orcl";
            String username = "Master2IL";
            String passwordDB = "psw";
            connection = DriverManager.getConnection(url, username, passwordDB);


            // Formatage de la date
            String formattedBirthday = "";
            try {
                LocalDate localDate = LocalDate.parse(birthday);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                formattedBirthday = localDate.format(formatter);
            } catch (DateTimeParseException e) {
                e.printStackTrace();
            }

            // Sélectionner tous les utilisateurs de la base de données
            String selectQuery = " SELECT  SUBSTR(DN.AXE_ECO, 1, 2) As Categorie,  " +
                    "SUM(TO_NUMBER(FD.MONTANT, '9999999999.999')) AS TotalMontantDepense " +
                    "FROM FaitDepenses FD, DimensionNatureEconomic DN, Dimensionmandat DM , Dimensiontemps DT " +
                    "WHERE FD.Id_N_economic = DN.Id_N_economic AND FD.ID_MANDAT = DM.ID_MANDAT AND DM.status_mandat ='REGLE' AND FD.Id_Temps = DT.Id_Temps "+
                    "AND ( " +
                    "    (DT.Jour = TO_DATE(?, 'DD/MM/YYYY') AND TO_CHAR(TO_DATE(?, 'DD/MM/YYYY'), 'DD') <> '01') OR " +
                    "    (TO_CHAR(TO_DATE(?, 'DD/MM/YYYY'), 'DD') = '01' AND EXTRACT(MONTH FROM DT.Jour) = EXTRACT(MONTH FROM TO_DATE(?, 'DD/MM/YYYY')) AND EXTRACT(YEAR FROM DT.Jour) = EXTRACT(YEAR FROM TO_DATE(?, 'DD/MM/YYYY'))) OR " +
                    "    (TO_CHAR(TO_DATE(?, 'DD/MM/YYYY'), 'DD/MM') = '01/01' AND EXTRACT(YEAR FROM DT.Jour) = EXTRACT(YEAR FROM TO_DATE(?, 'DD/MM/YYYY'))) " +
                    ") " +
                    "GROUP BY SUBSTR(DN.AXE_ECO, 1, 2)";



            selectStatement = connection.prepareStatement(selectQuery);

            // Ajout des paramètres 'birthday' à la requête préparée
            for (int i = 1; i <= 7; i++) {
                selectStatement.setString(i, formattedBirthday);
            }

            resultSet = selectStatement.executeQuery();

            // Parcourir les résultats et ajouter chaque utilisateur à la liste
            while (resultSet.next()) {
                String Categorie = resultSet.getString("Categorie");
                BigDecimal TotalMontantDepense = resultSet.getBigDecimal("TotalMontantDepense");
                // Construire une chaîne représentant tout le résultat
                String resultat = "Categorie: " + Categorie + ", TotalMontantDepense: "+ TotalMontantDepense+", DATE: "+birthday;
                getTotalMontantdepensePourChaqueCategorieReel.add(resultat);
            }

        } catch (SQLException e) {
            // Gestion des exceptions SQL
            e.printStackTrace();
        } finally {
            // Fermeture des ressources
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
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

        return getTotalMontantdepensePourChaqueCategorieReel;
    }



    


    /**********************************Dépenses réel****************************************** */



    /**********************************Dépenses Prévu****************************************** */


    public List<String> getAllTotalMontantdepensePourChaquePortefeuilleReel(String birthday) {
        List<String> getAllTotalMontantdepensePourChaquePortefeuilleReel = new ArrayList<>();
        Connection connection = null;
        PreparedStatement selectStatement = null;
        ResultSet resultSet = null;

        try {
            // Connexion à la base de données
            String url = "jdbc:oracle:thin:@localhost:1521:orcl";
            String username = "Master2IL";
            String passwordDB = "psw";
            connection = DriverManager.getConnection(url, username, passwordDB);


            // Formatage de la date
            String formattedBirthday = "";
            try {
                LocalDate localDate = LocalDate.parse(birthday);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                formattedBirthday = localDate.format(formatter);
            } catch (DateTimeParseException e) {
                e.printStackTrace();
            }

            // Sélectionner tous les utilisateurs de la base de données
            String selectQuery = " SELECT DA.code_portef As code_portef, " +
                    "SUM(TO_NUMBER(FD.MONTANT, '9999999999.999')) AS TotalMontantDepense " +
                    "FROM FaitDepenses FD, DimensionActivite DA,Dimensionmandat DM , Dimensiontemps DT " +
                    "WHERE FD.ID_ACTIVITE = DA.ID_ACTIVITE AND FD.ID_MANDAT = DM.ID_MANDAT AND FD.Id_Temps = DT.Id_Temps "+
                    "AND ( " +
                    "    (DT.Jour = TO_DATE(?, 'DD/MM/YYYY') AND TO_CHAR(TO_DATE(?, 'DD/MM/YYYY'), 'DD') <> '01') OR " +
                    "    (TO_CHAR(TO_DATE(?, 'DD/MM/YYYY'), 'DD') = '01' AND EXTRACT(MONTH FROM DT.Jour) = EXTRACT(MONTH FROM TO_DATE(?, 'DD/MM/YYYY')) AND EXTRACT(YEAR FROM DT.Jour) = EXTRACT(YEAR FROM TO_DATE(?, 'DD/MM/YYYY'))) OR " +
                    "    (TO_CHAR(TO_DATE(?, 'DD/MM/YYYY'), 'DD/MM') = '01/01' AND EXTRACT(YEAR FROM DT.Jour) = EXTRACT(YEAR FROM TO_DATE(?, 'DD/MM/YYYY'))) " +
                    ") " +
                    "GROUP BY DA.code_portef";


            selectStatement = connection.prepareStatement(selectQuery);

            // Ajout des paramètres 'birthday' à la requête préparée
            for (int i = 1; i <= 7; i++) {
                selectStatement.setString(i, formattedBirthday);
            }

            resultSet = selectStatement.executeQuery();

            // Parcourir les résultats et ajouter chaque utilisateur à la liste
            while (resultSet.next()) {
                String code_portef = resultSet.getString("code_portef");
                BigDecimal TotalMontantDepense = resultSet.getBigDecimal("TotalMontantDepense");
                // Construire une chaîne représentant tout le résultat
                String resultat = "Portefeuille: " + code_portef +", TotalMontantDepense: "+ TotalMontantDepense+", DATE: "+birthday;
                getAllTotalMontantdepensePourChaquePortefeuilleReel.add(resultat);
            }

        } catch (SQLException e) {
            // Gestion des exceptions SQL
            e.printStackTrace();
        } finally {
            // Fermeture des ressources
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
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

        return getAllTotalMontantdepensePourChaquePortefeuilleReel;
    }



    public  List<String> getAllTotalMontantdepensePourChaquePortefeuillePrevu(String scriptPath, List<String> inputData,String thirdParameter) {
        List<String> getAllTotalMontantdepensePourChaquePortefeuillePrevu = new ArrayList<>();
        try {
            // Créer le processus pour exécuter le script Python
            ProcessBuilder pb = new ProcessBuilder("python", scriptPath);
    
            // Convertir la liste en une seule chaîne séparée par des barres verticales (pipes) pour passer en tant qu'argument au script Python
            String inputString = String.join("|", inputData);
    
            // Passer les chaînes de données comme arguments au script Python
            pb.command().add(inputString);
            pb.command().add(thirdParameter);
    
            // Rediriger la sortie standard du processus vers un flux
            pb.redirectErrorStream(true);
    
            // Démarrer le processus
            Process process = pb.start();
    
            // Lire la sortie standard du processus
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                
                // Ajouter chaque ligne de sortie à la liste de prédictions
                getAllTotalMontantdepensePourChaquePortefeuillePrevu.add(line);
            }

            // Attendre la fin du processus
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.err.println("Erreur lors de l'exécution du script Python.");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return getAllTotalMontantdepensePourChaquePortefeuillePrevu;
    }





    public List<String> getAllTotalMontantdepensePourChaqueProgrammeReel(String birthday) {
        List<String> getAllTotalMontantdepensePourChaqueProgrammeReel = new ArrayList<>();
        Connection connection = null;
        PreparedStatement selectStatement = null;
        ResultSet resultSet = null;

        try {
            // Connexion à la base de données
            String url = "jdbc:oracle:thin:@localhost:1521:orcl";
            String username = "Master2IL";
            String passwordDB = "psw";
            connection = DriverManager.getConnection(url, username, passwordDB);


            // Formatage de la date
            String formattedBirthday = "";
            try {
                LocalDate localDate = LocalDate.parse(birthday);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                formattedBirthday = localDate.format(formatter);
            } catch (DateTimeParseException e) {
                e.printStackTrace();
            }

            // Sélectionner tous les utilisateurs de la base de données
            String selectQuery = " SELECT DA.code_portef As code_portef, da.programme AS programme, " +
                    "SUM(TO_NUMBER(FD.MONTANT, '9999999999.999')) AS TotalMontantDepense " +
                    "FROM FaitDepenses FD, DimensionActivite DA,Dimensionmandat DM , Dimensiontemps DT " +
                    "WHERE FD.ID_ACTIVITE = DA.ID_ACTIVITE AND FD.ID_MANDAT = DM.ID_MANDAT AND  FD.Id_Temps = DT.Id_Temps "+
                    "AND ( " +
                    "    (DT.Jour = TO_DATE(?, 'DD/MM/YYYY') AND TO_CHAR(TO_DATE(?, 'DD/MM/YYYY'), 'DD') <> '01') OR " +
                    "    (TO_CHAR(TO_DATE(?, 'DD/MM/YYYY'), 'DD') = '01' AND EXTRACT(MONTH FROM DT.Jour) = EXTRACT(MONTH FROM TO_DATE(?, 'DD/MM/YYYY')) AND EXTRACT(YEAR FROM DT.Jour) = EXTRACT(YEAR FROM TO_DATE(?, 'DD/MM/YYYY'))) OR " +
                    "    (TO_CHAR(TO_DATE(?, 'DD/MM/YYYY'), 'DD/MM') = '01/01' AND EXTRACT(YEAR FROM DT.Jour) = EXTRACT(YEAR FROM TO_DATE(?, 'DD/MM/YYYY'))) " +
                    ") " +
                    "GROUP BY DA.code_portef, da.programme";


            selectStatement = connection.prepareStatement(selectQuery);

            // Ajout des paramètres 'birthday' à la requête préparée
            for (int i = 1; i <= 7; i++) {
                selectStatement.setString(i, formattedBirthday);
            }

            resultSet = selectStatement.executeQuery();

            // Parcourir les résultats et ajouter chaque utilisateur à la liste
            while (resultSet.next()) {
                String code_portef = resultSet.getString("code_portef");
                String Programme = resultSet.getString("programme");
                BigDecimal TotalMontantDepense = resultSet.getBigDecimal("TotalMontantDepense");
                // Construire une chaîne représentant tout le résultat
                String resultat = "Programme: " + code_portef + Programme +", TotalMontantDepense: "+ TotalMontantDepense+", DATE: "+birthday;
                getAllTotalMontantdepensePourChaqueProgrammeReel.add(resultat);
            }

        } catch (SQLException e) {
            // Gestion des exceptions SQL
            e.printStackTrace();
        } finally {
            // Fermeture des ressources
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
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

        return getAllTotalMontantdepensePourChaqueProgrammeReel;
    }



    public  List<String> getAllTotalMontantdepensePourChaqueProgrammePrevu(String scriptPath, List<String> inputData,String thirdParameter) {
        List<String> getAllTotalMontantdepensePourChaqueProgrammePrevu = new ArrayList<>();
        try {
            // Créer le processus pour exécuter le script Python
            ProcessBuilder pb = new ProcessBuilder("python", scriptPath);
    
            // Convertir la liste en une seule chaîne séparée par des barres verticales (pipes) pour passer en tant qu'argument au script Python
            String inputString = String.join("|", inputData);
    
            // Passer les chaînes de données comme arguments au script Python
            pb.command().add(inputString);
            pb.command().add(thirdParameter);
    
            // Rediriger la sortie standard du processus vers un flux
            pb.redirectErrorStream(true);
    
            // Démarrer le processus
            Process process = pb.start();
    
            // Lire la sortie standard du processus
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                
                // Ajouter chaque ligne de sortie à la liste de prédictions
                getAllTotalMontantdepensePourChaqueProgrammePrevu.add(line);
            }

            // Attendre la fin du processus
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.err.println("Erreur lors de l'exécution du script Python.");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return getAllTotalMontantdepensePourChaqueProgrammePrevu;
    }





    public List<String> getAllTotalMontantdepensePourChaqueSousProgrammeReel(String birthday) {
        List<String> getAllTotalMontantdepensePourChaqueSousProgrammeReel = new ArrayList<>();
        Connection connection = null;
        PreparedStatement selectStatement = null;
        ResultSet resultSet = null;

        try {
            // Connexion à la base de données
            String url = "jdbc:oracle:thin:@localhost:1521:orcl";
            String username = "Master2IL";
            String passwordDB = "psw";
            connection = DriverManager.getConnection(url, username, passwordDB);


            // Formatage de la date
            String formattedBirthday = "";
            try {
                LocalDate localDate = LocalDate.parse(birthday);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                formattedBirthday = localDate.format(formatter);
            } catch (DateTimeParseException e) {
                e.printStackTrace();
            }

            // Sélectionner tous les utilisateurs de la base de données
            String selectQuery = " SELECT DA.code_portef As code_portef, da.programme AS programme, da.sous_programme AS SousProgramme,  " +
                    "SUM(TO_NUMBER(FD.MONTANT, '9999999999.999')) AS TotalMontantDepense " +
                    "FROM FaitDepenses FD, DimensionActivite DA,Dimensionmandat DM , Dimensiontemps DT " +
                    "WHERE FD.ID_ACTIVITE = DA.ID_ACTIVITE AND FD.ID_MANDAT = DM.ID_MANDAT AND FD.Id_Temps = DT.Id_Temps "+
                    "AND ( " +
                    "    (DT.Jour = TO_DATE(?, 'DD/MM/YYYY') AND TO_CHAR(TO_DATE(?, 'DD/MM/YYYY'), 'DD') <> '01') OR " +
                    "    (TO_CHAR(TO_DATE(?, 'DD/MM/YYYY'), 'DD') = '01' AND EXTRACT(MONTH FROM DT.Jour) = EXTRACT(MONTH FROM TO_DATE(?, 'DD/MM/YYYY')) AND EXTRACT(YEAR FROM DT.Jour) = EXTRACT(YEAR FROM TO_DATE(?, 'DD/MM/YYYY'))) OR " +
                    "    (TO_CHAR(TO_DATE(?, 'DD/MM/YYYY'), 'DD/MM') = '01/01' AND EXTRACT(YEAR FROM DT.Jour) = EXTRACT(YEAR FROM TO_DATE(?, 'DD/MM/YYYY'))) " +
                    ") " +
                    "GROUP BY DA.code_portef,da.programme,da.sous_programme";

            selectStatement = connection.prepareStatement(selectQuery);

            // Ajout des paramètres 'birthday' à la requête préparée
            for (int i = 1; i <= 7; i++) {
                selectStatement.setString(i, formattedBirthday);
            }

            resultSet = selectStatement.executeQuery();

            // Parcourir les résultats et ajouter chaque utilisateur à la liste
            while (resultSet.next()) {
                String code_portef = resultSet.getString("code_portef");
                String programme = resultSet.getString("programme");
                String sousProgramme = resultSet.getString("SousProgramme");
                BigDecimal TotalMontantDepense = resultSet.getBigDecimal("TotalMontantDepense");
                // Construire une chaîne représentant tout le résultat
                String resultat = "SousProgramme: " + code_portef + programme + sousProgramme
                        + ", TotalMontantDepense: " + TotalMontantDepense + ", DATE: " + birthday;
                getAllTotalMontantdepensePourChaqueSousProgrammeReel.add(resultat);
            }

        } catch (SQLException e) {
            // Gestion des exceptions SQL
            e.printStackTrace();
        } finally {
            // Fermeture des ressources
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
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

        return getAllTotalMontantdepensePourChaqueSousProgrammeReel;
    }



    public  List<String> getAllTotalMontantdepensePourChaqueSousProgrammePrevu(String scriptPath, List<String> inputData,String thirdParameter) {
        List<String> getAllTotalMontantdepensePourChaqueSousProgrammePrevu = new ArrayList<>();
        try {
            // Créer le processus pour exécuter le script Python
            ProcessBuilder pb = new ProcessBuilder("python", scriptPath);
    
            // Convertir la liste en une seule chaîne séparée par des barres verticales (pipes) pour passer en tant qu'argument au script Python
            String inputString = String.join("|", inputData);
    
            // Passer les chaînes de données comme arguments au script Python
            pb.command().add(inputString);
            pb.command().add(thirdParameter);
    
            // Rediriger la sortie standard du processus vers un flux
            pb.redirectErrorStream(true);
    
            // Démarrer le processus
            Process process = pb.start();
    
            // Lire la sortie standard du processus
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                
                // Ajouter chaque ligne de sortie à la liste de prédictions
                getAllTotalMontantdepensePourChaqueSousProgrammePrevu.add(line);
            }

            // Attendre la fin du processus
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.err.println("Erreur lors de l'exécution du script Python.");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return getAllTotalMontantdepensePourChaqueSousProgrammePrevu;
    }




    public List<String> getAllTotalMontantdepensePourTitreReel(String birthday) {
        List<String> getAllTotalMontantdepensePourTitreReel = new ArrayList<>();
        Connection connection = null;
        PreparedStatement selectStatement = null;
        ResultSet resultSet = null;

        try {
            // Connexion à la base de données
            String url = "jdbc:oracle:thin:@localhost:1521:orcl";
            String username = "Master2IL";
            String passwordDB = "psw";
            connection = DriverManager.getConnection(url, username, passwordDB);


            // Formatage de la date
            String formattedBirthday = "";
            try {
                LocalDate localDate = LocalDate.parse(birthday);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                formattedBirthday = localDate.format(formatter);
            } catch (DateTimeParseException e) {
                e.printStackTrace();
            }

            // Sélectionner tous les utilisateurs de la base de données
            String selectQuery = " SELECT  SUBSTR(DN.AXE_ECO, 1, 1) As Titre,  " +
                    "SUM(TO_NUMBER(FD.MONTANT, '9999999999.999')) AS TotalMontantDepense " +
                    "FROM FaitDepenses FD, DimensionNatureEconomic DN, Dimensionmandat DM , Dimensiontemps DT " +
                    "WHERE FD.Id_N_economic = DN.Id_N_economic AND FD.ID_MANDAT = DM.ID_MANDAT AND  FD.Id_Temps = DT.Id_Temps "+
                    "AND ( " +
                    "    (DT.Jour = TO_DATE(?, 'DD/MM/YYYY') AND TO_CHAR(TO_DATE(?, 'DD/MM/YYYY'), 'DD') <> '01') OR " +
                    "    (TO_CHAR(TO_DATE(?, 'DD/MM/YYYY'), 'DD') = '01' AND EXTRACT(MONTH FROM DT.Jour) = EXTRACT(MONTH FROM TO_DATE(?, 'DD/MM/YYYY')) AND EXTRACT(YEAR FROM DT.Jour) = EXTRACT(YEAR FROM TO_DATE(?, 'DD/MM/YYYY'))) OR " +
                    "    (TO_CHAR(TO_DATE(?, 'DD/MM/YYYY'), 'DD/MM') = '01/01' AND EXTRACT(YEAR FROM DT.Jour) = EXTRACT(YEAR FROM TO_DATE(?, 'DD/MM/YYYY'))) " +
                    ") " +
                    "GROUP BY SUBSTR(DN.AXE_ECO, 1, 1)";



            selectStatement = connection.prepareStatement(selectQuery);

            // Ajout des paramètres 'birthday' à la requête préparée
            for (int i = 1; i <= 7; i++) {
                selectStatement.setString(i, formattedBirthday);
            }

            resultSet = selectStatement.executeQuery();

            // Parcourir les résultats et ajouter chaque utilisateur à la liste
            while (resultSet.next()) {
                String Titre = resultSet.getString("Titre");
                BigDecimal TotalMontantDepense = resultSet.getBigDecimal("TotalMontantDepense");
                // Construire une chaîne représentant tout le résultat
                String resultat = "Titre: T" + Titre + ", TotalMontantDepense: "+ TotalMontantDepense+", DATE: "+birthday;
                getAllTotalMontantdepensePourTitreReel.add(resultat);
            }

        } catch (SQLException e) {
            // Gestion des exceptions SQL
            e.printStackTrace();
        } finally {
            // Fermeture des ressources
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
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

        return getAllTotalMontantdepensePourTitreReel;
    }



    public  List<String> getAllTotalMontantdepensePourTitrePrevu(String scriptPath, List<String> inputData,String thirdParameter) {
        List<String> getAllTotalMontantdepensePourTitrePrevu = new ArrayList<>();
        try {
            // Créer le processus pour exécuter le script Python
            ProcessBuilder pb = new ProcessBuilder("python", scriptPath);
    
            // Convertir la liste en une seule chaîne séparée par des barres verticales (pipes) pour passer en tant qu'argument au script Python
            String inputString = String.join("|", inputData);
    
            // Passer les chaînes de données comme arguments au script Python
            pb.command().add(inputString);
            pb.command().add(thirdParameter);
    
            // Rediriger la sortie standard du processus vers un flux
            pb.redirectErrorStream(true);
    
            // Démarrer le processus
            Process process = pb.start();
    
            // Lire la sortie standard du processus
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                
                // Ajouter chaque ligne de sortie à la liste de prédictions
                getAllTotalMontantdepensePourTitrePrevu.add(line);
            }

            // Attendre la fin du processus
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.err.println("Erreur lors de l'exécution du script Python.");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return getAllTotalMontantdepensePourTitrePrevu;
    }




    public List<String> getAllTotalMontantdepensePourCategorieReel(String birthday) {
        List<String> getAllTotalMontantdepensePourCategorieReel = new ArrayList<>();
        Connection connection = null;
        PreparedStatement selectStatement = null;
        ResultSet resultSet = null;

        try {
            // Connexion à la base de données
            String url = "jdbc:oracle:thin:@localhost:1521:orcl";
            String username = "Master2IL";
            String passwordDB = "psw";
            connection = DriverManager.getConnection(url, username, passwordDB);


            // Formatage de la date
            String formattedBirthday = "";
            try {
                LocalDate localDate = LocalDate.parse(birthday);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                formattedBirthday = localDate.format(formatter);
            } catch (DateTimeParseException e) {
                e.printStackTrace();
            }

            // Sélectionner tous les utilisateurs de la base de données
            String selectQuery = " SELECT  SUBSTR(DN.AXE_ECO, 1, 2) As Categorie,  " +
                    "SUM(TO_NUMBER(FD.MONTANT, '9999999999.999')) AS TotalMontantDepense " +
                    "FROM FaitDepenses FD, DimensionNatureEconomic DN, Dimensionmandat DM , Dimensiontemps DT " +
                    "WHERE FD.Id_N_economic = DN.Id_N_economic AND FD.ID_MANDAT = DM.ID_MANDAT AND DM.status_mandat ='REGLE' AND FD.Id_Temps = DT.Id_Temps "+
                    "AND ( " +
                    "    (DT.Jour = TO_DATE(?, 'DD/MM/YYYY') AND TO_CHAR(TO_DATE(?, 'DD/MM/YYYY'), 'DD') <> '01') OR " +
                    "    (TO_CHAR(TO_DATE(?, 'DD/MM/YYYY'), 'DD') = '01' AND EXTRACT(MONTH FROM DT.Jour) = EXTRACT(MONTH FROM TO_DATE(?, 'DD/MM/YYYY')) AND EXTRACT(YEAR FROM DT.Jour) = EXTRACT(YEAR FROM TO_DATE(?, 'DD/MM/YYYY'))) OR " +
                    "    (TO_CHAR(TO_DATE(?, 'DD/MM/YYYY'), 'DD/MM') = '01/01' AND EXTRACT(YEAR FROM DT.Jour) = EXTRACT(YEAR FROM TO_DATE(?, 'DD/MM/YYYY'))) " +
                    ") " +
                    "GROUP BY SUBSTR(DN.AXE_ECO, 1, 2)";



            selectStatement = connection.prepareStatement(selectQuery);

            // Ajout des paramètres 'birthday' à la requête préparée
            for (int i = 1; i <= 7; i++) {
                selectStatement.setString(i, formattedBirthday);
            }

            resultSet = selectStatement.executeQuery();

            // Parcourir les résultats et ajouter chaque utilisateur à la liste
            while (resultSet.next()) {
                String Categorie = resultSet.getString("Categorie");
                BigDecimal TotalMontantDepense = resultSet.getBigDecimal("TotalMontantDepense");
                // Construire une chaîne représentant tout le résultat
                String resultat = "Categorie: " + Categorie + ", TotalMontantDepense: "+ TotalMontantDepense+", DATE: "+birthday;
                getAllTotalMontantdepensePourCategorieReel.add(resultat);
            }

        } catch (SQLException e) {
            // Gestion des exceptions SQL
            e.printStackTrace();
        } finally {
            // Fermeture des ressources
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
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

        return getAllTotalMontantdepensePourCategorieReel;
    }



    public  List<String> getAllTotalMontantdepensePourCategoriePrevu(String scriptPath, List<String> inputData,String thirdParameter) {
        List<String> getAllTotalMontantdepensePourCategoriePrevu = new ArrayList<>();
        try {
            // Créer le processus pour exécuter le script Python
            ProcessBuilder pb = new ProcessBuilder("python", scriptPath);
    
            // Convertir la liste en une seule chaîne séparée par des barres verticales (pipes) pour passer en tant qu'argument au script Python
            String inputString = String.join("|", inputData);
    
            // Passer les chaînes de données comme arguments au script Python
            pb.command().add(inputString);
            pb.command().add(thirdParameter);
    
            // Rediriger la sortie standard du processus vers un flux
            pb.redirectErrorStream(true);
    
            // Démarrer le processus
            Process process = pb.start();
    
            // Lire la sortie standard du processus
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                
                // Ajouter chaque ligne de sortie à la liste de prédictions
                getAllTotalMontantdepensePourCategoriePrevu.add(line);
            }

            // Attendre la fin du processus
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.err.println("Erreur lors de l'exécution du script Python.");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return getAllTotalMontantdepensePourCategoriePrevu;
    }




    /**********************************Dépenses Prévu****************************************** */


    /**********************************Recette Réel****************************************** */

    public List<String> getTotalMontantPourChaqueCompteReel(String birthday) {
        List<String> getTotalMontantPourChaqueCompteReel = new ArrayList<>();
        Connection connection = null;
        PreparedStatement selectStatement = null;
        ResultSet resultSet = null;
    
        try {
            // Connexion à la base de données
            String url = "jdbc:oracle:thin:@localhost:1521:orcl";
            String username = "Master2IL";
            String passwordDB = "psw";
            connection = DriverManager.getConnection(url, username, passwordDB);



            // Formatage de la date
            String formattedBirthday = "";
            try {
                LocalDate localDate = LocalDate.parse(birthday);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                formattedBirthday = localDate.format(formatter);
            } catch (DateTimeParseException e) {
                e.printStackTrace();
            }

            // Sélectionner tous les utilisateurs de la base de données
            String selectQuery = "SELECT DC.CODE_CPT AS CodeCompte, " +
                    "SUM(TO_NUMBER(REPLACE(FR.MONTANT, ',', '.'), '9999999999.999')) AS TotalMontant " +
                    "FROM FaitRecettes FR, DimensionCompte DC, DimensionTemps DT " +
                    "WHERE FR.Id_Compte = DC.Id_Compte AND FR.Id_Temps = DT.Id_Temps " +
                    "AND ( " +
                    "    (DT.Jour = TO_DATE(?, 'DD/MM/YYYY') AND TO_CHAR(TO_DATE(?, 'DD/MM/YYYY'), 'DD') <> '01') OR " +
                    "    (TO_CHAR(TO_DATE(?, 'DD/MM/YYYY'), 'DD') = '01' AND EXTRACT(MONTH FROM DT.Jour) = EXTRACT(MONTH FROM TO_DATE(?, 'DD/MM/YYYY')) AND EXTRACT(YEAR FROM DT.Jour) = EXTRACT(YEAR FROM TO_DATE(?, 'DD/MM/YYYY'))) OR "
                    +
                    "    (TO_CHAR(TO_DATE(?, 'DD/MM/YYYY'), 'DD/MM') = '01/01' AND EXTRACT(YEAR FROM DT.Jour) = EXTRACT(YEAR FROM TO_DATE(?, 'DD/MM/YYYY'))) "
                    +
                    ") " +
                    "GROUP BY DC.CODE_CPT";



            selectStatement = connection.prepareStatement(selectQuery);

            // Ajout des paramètres 'birthday' à la requête préparée
            for (int i = 1; i <= 7; i++) {
                selectStatement.setString(i, formattedBirthday);
            }

            resultSet = selectStatement.executeQuery();
    
            // Parcourir les résultats et ajouter chaque utilisateur à la liste
            while (resultSet.next()) {
                String CodeCompte = resultSet.getString("CodeCompte");
                BigDecimal TotalMontant = resultSet.getBigDecimal("TotalMontant");
                // Construire une chaîne représentant tout le résultat
                String resultat = "CodeCompte: " + CodeCompte +", TotalMontantRecette: " + TotalMontant + ", DATE: " + birthday;
                getTotalMontantPourChaqueCompteReel.add(resultat);
            }
    
        } catch (SQLException e) {
            // Gestion des exceptions SQL
            e.printStackTrace();
        } finally {
            // Fermeture des ressources
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
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
    
        return getTotalMontantPourChaqueCompteReel;
    }
    
    public List<String> getTotalMontantPourChaquePosteComptableReel(String birthday) {
        List<String> getTotalMontantPourChaquePosteComptableReel = new ArrayList<>();
        Connection connection = null;
        PreparedStatement selectStatement = null;
        ResultSet resultSet = null;
    
        try {
            // Connexion à la base de données
            String url = "jdbc:oracle:thin:@localhost:1521:orcl";
            String username = "Master2IL";
            String passwordDB = "psw";
            connection = DriverManager.getConnection(url, username, passwordDB);



            // Formatage de la date
            String formattedBirthday = "";
            try {
                LocalDate localDate = LocalDate.parse(birthday);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                formattedBirthday = localDate.format(formatter);
            } catch (DateTimeParseException e) {
                e.printStackTrace();
            }

            // Sélectionner tous les utilisateurs de la base de données
            String selectQuery = "SELECT DW.CODE_WILAYA AS CodeWilaya, " +
                    "SUM(TO_NUMBER(REPLACE(FR.MONTANT, ',', '.'), '9999999999.999')) AS TotalMontant " +
                    "FROM FaitRecettes FR, DimensionWilayas DW, DimensionTemps DT " +
                    "WHERE FR.Id_Wilayas = DW.Id_Wilayas AND FR.Id_Temps = DT.Id_Temps " +
                    "AND ( " +
                    "    (DT.Jour = TO_DATE(?, 'DD/MM/YYYY') AND TO_CHAR(TO_DATE(?, 'DD/MM/YYYY'), 'DD') <> '01') OR " +
                    "    (TO_CHAR(TO_DATE(?, 'DD/MM/YYYY'), 'DD') = '01' AND EXTRACT(MONTH FROM DT.Jour) = EXTRACT(MONTH FROM TO_DATE(?, 'DD/MM/YYYY')) AND EXTRACT(YEAR FROM DT.Jour) = EXTRACT(YEAR FROM TO_DATE(?, 'DD/MM/YYYY'))) OR "
                    +
                    "    (TO_CHAR(TO_DATE(?, 'DD/MM/YYYY'), 'DD/MM') = '01/01' AND EXTRACT(YEAR FROM DT.Jour) = EXTRACT(YEAR FROM TO_DATE(?, 'DD/MM/YYYY'))) "
                    +
                    ") " +
                    "GROUP BY DW.CODE_WILAYA";



            selectStatement = connection.prepareStatement(selectQuery);

            // Ajout des paramètres 'birthday' à la requête préparée
            for (int i = 1; i <= 7; i++) {
                selectStatement.setString(i, formattedBirthday);
            }

            resultSet = selectStatement.executeQuery();
    
            // Parcourir les résultats et ajouter chaque utilisateur à la liste
            while (resultSet.next()) {
                String CodeWilaya = resultSet.getString("CodeWilaya");
                BigDecimal TotalMontant = resultSet.getBigDecimal("TotalMontant");
                // Construire une chaîne représentant tout le résultat
                String resultat = "CodeWilaya: " + CodeWilaya +", TotalMontantRecette: " + TotalMontant + ", DATE: " + birthday;
                getTotalMontantPourChaquePosteComptableReel.add(resultat);
            }
    
        } catch (SQLException e) {
            // Gestion des exceptions SQL
            e.printStackTrace();
        } finally {
            // Fermeture des ressources
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
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
    
        return getTotalMontantPourChaquePosteComptableReel;
    }  



    /**********************************Recette Réel****************************************** */


    /**********************************Recette Prévu****************************************** */


    public  List<String> getTotalMontantPourChaqueComptePrevu(String scriptPath, List<String> inputData,String thirdParameter) {
        List<String> getTotalMontantPourChaqueComptePrevu = new ArrayList<>();
        try {
            // Créer le processus pour exécuter le script Python
            ProcessBuilder pb = new ProcessBuilder("python", scriptPath);
    
            // Convertir la liste en une seule chaîne séparée par des barres verticales (pipes) pour passer en tant qu'argument au script Python
            String inputString = String.join("|", inputData);
    
            // Passer les chaînes de données comme arguments au script Python
            pb.command().add(inputString);
            pb.command().add(thirdParameter);
    
            // Rediriger la sortie standard du processus vers un flux
            pb.redirectErrorStream(true);
    
            // Démarrer le processus
            Process process = pb.start();
    
            // Lire la sortie standard du processus
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                
                // Ajouter chaque ligne de sortie à la liste de prédictions
                getTotalMontantPourChaqueComptePrevu.add(line);
            }

            // Attendre la fin du processus
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.err.println("Erreur lors de l'exécution du script Python.");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return getTotalMontantPourChaqueComptePrevu;
    }




    public  List<String> getTotalMontantPourChaquePosteComptablePrevu(String scriptPath, List<String> inputData,String thirdParameter) {
        List<String> getTotalMontantPourChaquePosteComptablePrevu = new ArrayList<>();
        try {
            // Créer le processus pour exécuter le script Python
            ProcessBuilder pb = new ProcessBuilder("python", scriptPath);
    
            // Convertir la liste en une seule chaîne séparée par des barres verticales (pipes) pour passer en tant qu'argument au script Python
            String inputString = String.join("|", inputData);
    
            // Passer les chaînes de données comme arguments au script Python
            pb.command().add(inputString);
            pb.command().add(thirdParameter);
    
            // Rediriger la sortie standard du processus vers un flux
            pb.redirectErrorStream(true);
    
            // Démarrer le processus
            Process process = pb.start();
    
            // Lire la sortie standard du processus
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                
                // Ajouter chaque ligne de sortie à la liste de prédictions
                getTotalMontantPourChaquePosteComptablePrevu.add(line);
            }

            // Attendre la fin du processus
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.err.println("Erreur lors de l'exécution du script Python.");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return getTotalMontantPourChaquePosteComptablePrevu;
    }


   /**********************************Recette Prévu****************************************** */


    /**********************************Dette Réel****************************************** */

    public List<String> getTotalMontantRembourcePourChaqueSoumissionaire(String birthday) {
        List<String> getTotalMontantRembourcePourChaqueSoumissionaire = new ArrayList<>();
        Connection connection = null;
        PreparedStatement selectStatement = null;
        ResultSet resultSet = null;
    
        try {
            // Connexion à la base de données
            String url = "jdbc:oracle:thin:@localhost:1521:orcl";
            String username = "Master2IL";
            String passwordDB = "psw";
            connection = DriverManager.getConnection(url, username, passwordDB);

            // Formatage de la date
            String formattedBirthday = "";
            try {
                LocalDate localDate = LocalDate.parse(birthday);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                formattedBirthday = localDate.format(formatter);
            } catch (DateTimeParseException e) {
                e.printStackTrace();
            }


            // Sélectionner tous les utilisateurs de la base de données
            String selectQuery = "SELECT DS.Code_Soumissionaire AS CodeSoumissionaire, DS.libelle_Soumissionaire AS libelleSoumissionaire, " +
                     "SUM(TO_NUMBER(REPLACE(FD.MONTANT_Propose_Par_etat, ',', '.'), '9999999999.999') + " +
                     "TO_NUMBER(REPLACE(FD.Montant_Adjuge, ',', '.'), '9999999999.999') + " +
                     "(CASE " +
                     "    WHEN DD.DUREE_bon = '13semaines' THEN 3 " + // 13 weeks ≈ 3 months
                     "    WHEN DD.DUREE_bon = '12semaines' THEN 3 " + // 12 weeks ≈ 3 months
                     "    WHEN DD.DUREE_bon = '1ans' THEN 12 " +     // 1 year = 12 months
                     "    WHEN DD.DUREE_bon = '5ans' THEN 60 " +     // 5 years = 60 months
                     "    WHEN DD.DUREE_bon = '10ans' THEN 120 " +   // 10 years = 120 months
                     "    ELSE 0 " +                                // Default case, if none of the above matches
                     "END *(TO_NUMBER(REPLACE(FD.Montant_Propose_Par_Soumissionaire, ',', '.'), '9999999999.999') *  TO_NUMBER(REPLACE(FD.Montant_Coupoun, ',', '.'), '9999999999.999'))/100 )) AS TotalMontantRemboourse " +
                     "FROM FaitDette FD, DimensionSoumissionaire DS, DimensionTitre DD,Dimensiontemps DT " +
                     "WHERE FD.Id_Soumissionaire = DS.Id_Soumissionaire AND FD.Id_Temps = DT.Id_Temps " +
                     "AND FD.Id_Titre = DD.Id_Titre " +
                     "AND ( " +
                    "    (DT.Jour = TO_DATE(?, 'DD/MM/YYYY') AND TO_CHAR(TO_DATE(?, 'DD/MM/YYYY'), 'DD') <> '01') OR " +
                    "    (TO_CHAR(TO_DATE(?, 'DD/MM/YYYY'), 'DD') = '01' AND EXTRACT(MONTH FROM DT.Jour) = EXTRACT(MONTH FROM TO_DATE(?, 'DD/MM/YYYY')) AND EXTRACT(YEAR FROM DT.Jour) = EXTRACT(YEAR FROM TO_DATE(?, 'DD/MM/YYYY'))) OR "
                    +
                    "    (TO_CHAR(TO_DATE(?, 'DD/MM/YYYY'), 'DD/MM') = '01/01' AND EXTRACT(YEAR FROM DT.Jour) = EXTRACT(YEAR FROM TO_DATE(?, 'DD/MM/YYYY'))) "
                    +
                    ") " +
                     "GROUP BY DS.Code_Soumissionaire, DS.libelle_Soumissionaire";

            selectStatement = connection.prepareStatement(selectQuery);
            // Ajout des paramètres 'birthday' à la requête préparée
            for (int i = 1; i <= 7; i++) {
                selectStatement.setString(i, formattedBirthday);
            }
            resultSet = selectStatement.executeQuery();
    
            // Parcourir les résultats et ajouter chaque utilisateur à la liste
            while (resultSet.next()) {
                String CodeSoumissionaire = resultSet.getString("CodeSoumissionaire");
                String libelleSoumissionaire = resultSet.getString("libelleSoumissionaire");
                BigDecimal TotalMontant = resultSet.getBigDecimal("TotalMontantRemboourse");
                // Construire une chaîne représentant tout le résultat
                String resultat = "CodeSoumissionaire: " + CodeSoumissionaire +", libelleSoumissionaire: " + libelleSoumissionaire +", TotalMontantDette: " + TotalMontant + ", DATE: " + birthday;
                getTotalMontantRembourcePourChaqueSoumissionaire.add(resultat);
            }
    
        } catch (SQLException e) {
            // Gestion des exceptions SQL
            e.printStackTrace();
        } finally {
            // Fermeture des ressources
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
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
    
        return getTotalMontantRembourcePourChaqueSoumissionaire;
    }  




    public List<String> getTotalMontantRembourcePourChaqueTitre(String birthday) {
        List<String> getTotalMontantRembourcePourChaqueTitre = new ArrayList<>();
        Connection connection = null;
        PreparedStatement selectStatement = null;
        ResultSet resultSet = null;
    
        try {
            // Connexion à la base de données
            String url = "jdbc:oracle:thin:@localhost:1521:orcl";
            String username = "Master2IL";
            String passwordDB = "psw";
            connection = DriverManager.getConnection(url, username, passwordDB);

            // Formatage de la date
            String formattedBirthday = "";
            try {
                LocalDate localDate = LocalDate.parse(birthday);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                formattedBirthday = localDate.format(formatter);
            } catch (DateTimeParseException e) {
                e.printStackTrace();
            }



            // Sélectionner tous les utilisateurs de la base de données
            String selectQuery = "SELECT DD.code_ISIN AS codeISIN, " +
                    "SUM(TO_NUMBER(REPLACE(FD.MONTANT_Propose_Par_etat, ',', '.'), '9999999999.999') + " +
                    "TO_NUMBER(REPLACE(FD.Montant_Adjuge, ',', '.'), '9999999999.999') + " +
                    "(CASE " +
                    "    WHEN DD.DUREE_bon = '13semaines' THEN 3 " + // 13 weeks ≈ 3 months
                    "    WHEN DD.DUREE_bon = '12semaines' THEN 3 " + // 12 weeks ≈ 3 months
                    "    WHEN DD.DUREE_bon = '1ans' THEN 12 " + // 1 year = 12 months
                    "    WHEN DD.DUREE_bon = '5ans' THEN 60 " + // 5 years = 60 months
                    "    WHEN DD.DUREE_bon = '10ans' THEN 120 " + // 10 years = 120 months
                    "    ELSE 0 " + // Default case, if none of the above matches
                    "END *(TO_NUMBER(REPLACE(FD.Montant_Propose_Par_Soumissionaire, ',', '.'), '9999999999.999') *  TO_NUMBER(REPLACE(FD.Montant_Coupoun, ',', '.'), '9999999999.999'))/100 )) AS TotalMontantRemboourse " +
                    "FROM FaitDette FD, DimensionSoumissionaire DS, DimensionTitre DD,Dimensiontemps DT " +
                     "WHERE FD.Id_Soumissionaire = DS.Id_Soumissionaire AND FD.Id_Temps = DT.Id_Temps " +
                     "AND FD.Id_Titre = DD.Id_Titre " +
                     "AND ( " +
                    "    (DT.Jour = TO_DATE(?, 'DD/MM/YYYY') AND TO_CHAR(TO_DATE(?, 'DD/MM/YYYY'), 'DD') <> '01') OR " +
                    "    (TO_CHAR(TO_DATE(?, 'DD/MM/YYYY'), 'DD') = '01' AND EXTRACT(MONTH FROM DT.Jour) = EXTRACT(MONTH FROM TO_DATE(?, 'DD/MM/YYYY')) AND EXTRACT(YEAR FROM DT.Jour) = EXTRACT(YEAR FROM TO_DATE(?, 'DD/MM/YYYY'))) OR "
                    +
                    "    (TO_CHAR(TO_DATE(?, 'DD/MM/YYYY'), 'DD/MM') = '01/01' AND EXTRACT(YEAR FROM DT.Jour) = EXTRACT(YEAR FROM TO_DATE(?, 'DD/MM/YYYY'))) "
                    +
                    ") " +
                    "GROUP BY DD.code_ISIN";

            selectStatement = connection.prepareStatement(selectQuery);
            // Ajout des paramètres 'birthday' à la requête préparée
            for (int i = 1; i <= 7; i++) {
                selectStatement.setString(i, formattedBirthday);
            }

            resultSet = selectStatement.executeQuery();
    
            // Parcourir les résultats et ajouter chaque utilisateur à la liste
            while (resultSet.next()) {
                String codeISIN = resultSet.getString("codeISIN");
                BigDecimal TotalMontant = resultSet.getBigDecimal("TotalMontantRemboourse");
                // Construire une chaîne représentant tout le résultat
                String resultat = "codeISIN: " + codeISIN + ", TotalMontantDette: " + TotalMontant + ", DATE: " + birthday;
                getTotalMontantRembourcePourChaqueTitre.add(resultat);
            }
    
        } catch (SQLException e) {
            // Gestion des exceptions SQL
            e.printStackTrace();
        } finally {
            // Fermeture des ressources
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
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
    
        return getTotalMontantRembourcePourChaqueTitre;
    }  
    



    /**********************************Dette Réel****************************************** */


    /**********************************Dette Prévu****************************************** */



    public  List<String> getTotalMontantRembourcePourChaqueSoumissionairePrevu(String scriptPath, List<String> inputData,String thirdParameter) {
        List<String> getTotalMontantRembourcePourChaqueSoumissionairePrevu = new ArrayList<>();
        try {
            // Créer le processus pour exécuter le script Python
            ProcessBuilder pb = new ProcessBuilder("python", scriptPath);
    
            // Convertir la liste en une seule chaîne séparée par des barres verticales (pipes) pour passer en tant qu'argument au script Python
            String inputString = String.join("|", inputData);
    
            // Passer les chaînes de données comme arguments au script Python
            pb.command().add(inputString);
            pb.command().add(thirdParameter);
    
            // Rediriger la sortie standard du processus vers un flux
            pb.redirectErrorStream(true);
    
            // Démarrer le processus
            Process process = pb.start();
    
            // Lire la sortie standard du processus
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                
                // Ajouter chaque ligne de sortie à la liste de prédictions
                getTotalMontantRembourcePourChaqueSoumissionairePrevu.add(line);
            }

            // Attendre la fin du processus
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.err.println("Erreur lors de l'exécution du script Python.");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return getTotalMontantRembourcePourChaqueSoumissionairePrevu;
    }




    public  List<String> getTotalMontantRembourcePourChaqueTitrePrevu(String scriptPath, List<String> inputData,String thirdParameter) {
        List<String> getTotalMontantRembourcePourChaqueTitrePrevu = new ArrayList<>();
        try {
            // Créer le processus pour exécuter le script Python
            ProcessBuilder pb = new ProcessBuilder("python", scriptPath);
    
            // Convertir la liste en une seule chaîne séparée par des barres verticales (pipes) pour passer en tant qu'argument au script Python
            String inputString = String.join("|", inputData);
    
            // Passer les chaînes de données comme arguments au script Python
            pb.command().add(inputString);
            pb.command().add(thirdParameter);
    
            // Rediriger la sortie standard du processus vers un flux
            pb.redirectErrorStream(true);
    
            // Démarrer le processus
            Process process = pb.start();
    
            // Lire la sortie standard du processus
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                
                // Ajouter chaque ligne de sortie à la liste de prédictions
                getTotalMontantRembourcePourChaqueTitrePrevu.add(line);
            }

            // Attendre la fin du processus
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.err.println("Erreur lors de l'exécution du script Python.");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return getTotalMontantRembourcePourChaqueTitrePrevu;
    }



     /**********************************Dette Prévu****************************************** */


     /*********************************** Dashbord Reel****************************************/


    public List<String> getTotalCreditsAlloue(String birthday) {
        List<String> getTotalCreditsAlloue = new ArrayList<>();
        Connection connection = null;
        PreparedStatement selectStatement = null;
        ResultSet resultSet = null;
    
        try {
            // Connexion à la base de données
            String url = "jdbc:oracle:thin:@localhost:1521:orcl";
            String username = "Master2IL";
            String passwordDB = "psw";
            connection = DriverManager.getConnection(url, username, passwordDB);
    
            // Formatage de la date
            String formattedBirthday = "";
            try {
                LocalDate localDate = LocalDate.parse(birthday);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                formattedBirthday = localDate.format(formatter);
            } catch (DateTimeParseException e) {
                e.printStackTrace();
            }
    
            // Sélectionner tous les utilisateurs de la base de données
            String selectQuery =  "SELECT SUM(TO_NUMBER(FC.CREDIT_Total, '9999999999.999')) AS TotalCredits " +
                "FROM FaitCredit FC, DimensionActivite DA, DimensionWilayas DW, DimensionOrdonnateur DO, DimensionNatureEconomic DE, Dimensiontemps DT " +
                "WHERE FC.ID_ACTIVITE = DA.ID_ACTIVITE AND FC.Id_Temps = DT.Id_Temps AND FC.Id_Ordonnateur = DO.Id_Ordonnateur AND FC.Id_Wilayas = DW.Id_Wilayas AND FC.Id_N_economic = DE.Id_N_economic " +
                "AND ( " +
                "    (DT.Jour = TO_DATE(?, 'DD/MM/YYYY') AND TO_CHAR(TO_DATE(?, 'DD/MM/YYYY'), 'DD') <> '01') OR " +
                "    (TO_CHAR(TO_DATE(?, 'DD/MM/YYYY'), 'DD') = '01' AND EXTRACT(MONTH FROM DT.Jour) = EXTRACT(MONTH FROM TO_DATE(?, 'DD/MM/YYYY')) AND EXTRACT(YEAR FROM DT.Jour) = EXTRACT(YEAR FROM TO_DATE(?, 'DD/MM/YYYY'))) OR " +
                "    (TO_CHAR(TO_DATE(?, 'DD/MM/YYYY'), 'DD/MM') = '01/01' AND EXTRACT(YEAR FROM DT.Jour) = EXTRACT(YEAR FROM TO_DATE(?, 'DD/MM/YYYY'))) " +
                ") ";
    
            selectStatement = connection.prepareStatement(selectQuery);
            // Ajout des paramètres 'birthday' à la requête préparée
            for (int i = 1; i <= 7; i++) {
                selectStatement.setString(i, formattedBirthday);
            }
            
            resultSet = selectStatement.executeQuery();

            BigDecimal TotalCredits = BigDecimal.ZERO;

            // Parcourir les résultats et ajouter chaque utilisateur à la liste
            if (resultSet.next()) {
                TotalCredits = resultSet.getBigDecimal("TotalCredits");
                if (TotalCredits == null) {
                    TotalCredits = BigDecimal.ZERO;
                }
            }


            // Construire une chaîne représentant tout le résultat
            String resultat = "TotalCredits: " + TotalCredits + ", DATE: " + birthday;
            getTotalCreditsAlloue.add(resultat);
    
        } catch (SQLException e) {
            // Gestion des exceptions SQL
            e.printStackTrace();
        } finally {
            // Fermeture des ressources
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
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
    
        return getTotalCreditsAlloue;
    }



    public List<String> getTotalDepensesAlloue(String birthday) {
        List<String> getTotalDepensesAlloue = new ArrayList<>();
        Connection connection = null;
        PreparedStatement selectStatement = null;
        ResultSet resultSet = null;
    
        try {
            // Connexion à la base de données
            String url = "jdbc:oracle:thin:@localhost:1521:orcl";
            String username = "Master2IL";
            String passwordDB = "psw";
            connection = DriverManager.getConnection(url, username, passwordDB);
    
            // Formatage de la date
            String formattedBirthday = "";
            try {
                LocalDate localDate = LocalDate.parse(birthday);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                formattedBirthday = localDate.format(formatter);
            } catch (DateTimeParseException e) {
                e.printStackTrace();
            }
    
            String selectQuery = "SELECT SUM(" +
                    "CASE " +
                    "    WHEN EXTRACT(YEAR FROM DT.Jour) > (EXTRACT(YEAR FROM TO_DATE(?, 'DD/MM/YYYY')) + " +
                    "    CASE " +
                    "       WHEN DD.DUREE_bon = '13semaines' THEN 3 " + // 13 weeks ≈ 3 months
                    "       WHEN DD.DUREE_bon = '12semaines' THEN 3 " + // 12 weeks ≈ 3 months
                    "       WHEN DD.DUREE_bon = '1ans' THEN 12 " + // 1 year = 12 months
                    "       WHEN DD.DUREE_bon = '5ans' THEN 60 " + // 5 years = 60 months
                    "       WHEN DD.DUREE_bon = '10ans' THEN 120 " + // 10 years = 120 months
                    "    ELSE 0 " + // Default case, if none of the above matches
                    "    END) " +
                    "    THEN TO_NUMBER(FD.MONTANT, '9999999999.999') + " +
                    "    TO_NUMBER(REPLACE(FDT.Montant_Adjuge, ',', '.'), '9999999999.999') " +
                    "    ELSE 0 " +
                    "END + " +
                    "CASE " +
                    "    WHEN EXTRACT(YEAR FROM DT.Jour) <= (EXTRACT(YEAR FROM TO_DATE(?, 'DD/MM/YYYY')) + " +
                    "    CASE " +
                    "        WHEN DD.DUREE_bon = '13semaines' THEN 3 " + // 13 weeks ≈ 3 months
                    "        WHEN DD.DUREE_bon = '12semaines' THEN 3 " + // 12 weeks ≈ 3 months
                    "        WHEN DD.DUREE_bon = '1ans' THEN 12 " + // 1 year = 12 months
                    "        WHEN DD.DUREE_bon = '5ans' THEN 60 " + // 5 years = 60 months
                    "        WHEN DD.DUREE_bon = '10ans' THEN 120 " + // 10 years = 120 months
                    "        ELSE 0 " + // Default case, if none of the above matches
                    "    END) " +
                    "    THEN TO_NUMBER(FD.MONTANT, '9999999999.999') + " +
                    "    (CASE " +
                    "        WHEN DD.DUREE_bon = '13semaines' THEN 3 " +
                    "        WHEN DD.DUREE_bon = '12semaines' THEN 3 " +
                    "        WHEN DD.DUREE_bon = '1ans' THEN 12 " +
                    "        WHEN DD.DUREE_bon = '5ans' THEN 60 " +
                    "        WHEN DD.DUREE_bon = '10ans' THEN 120 " +
                    "        ELSE 0 " +
                    "    END * (TO_NUMBER(REPLACE(FDT.Montant_Propose_Par_Soumissionaire, ',', '.'), '9999999999.999') * " +
                    "    TO_NUMBER(REPLACE(FDT.Montant_Coupoun, ',', '.'), '9999999999.999')) / 100) + " +
                    "    TO_NUMBER(REPLACE(FDT.MONTANT_Propose_Par_etat, ',', '.'), '9999999999.999') " +
                    "    ELSE 0 " +
                    "END) AS TotalDepenses " +
                    "FROM FaitDepenses FD, DimensionActivite DA, DimensionWilayas DW, Dimensiontemps DT, " +
                    "DimensionOrdonnateur DO, DimensionMandat DM, DimensionNatureEconomic DE, FaitDette FDT, " +
                    "DimensionSoumissionaire DS, DimensionTitre DD " +
                    "WHERE FDT.Id_Soumissionaire = DS.Id_Soumissionaire AND FDT.Id_Temps = DT.Id_Temps " +
                    "AND FDT.Id_Titre = DD.Id_Titre AND FD.ID_ACTIVITE = DA.ID_ACTIVITE " +
                    "AND FD.ID_MANDAT = DM.ID_MANDAT AND FD.Id_Temps = DT.Id_Temps " +
                    "AND FD.Id_Ordonnateur = DO.Id_Ordonnateur AND FD.Id_Wilayas = DW.Id_Wilayas " +
                    "AND FD.Id_N_economic = DE.Id_N_economic " +
                    "AND ( " +
                    "    (DT.Jour = TO_DATE(?, 'DD/MM/YYYY') AND TO_CHAR(TO_DATE(?, 'DD/MM/YYYY'), 'DD') <> '01') OR " +
                    "    (TO_CHAR(TO_DATE(?, 'DD/MM/YYYY'), 'DD') = '01' AND " +
                    "    EXTRACT(MONTH FROM DT.Jour) = EXTRACT(MONTH FROM TO_DATE(?, 'DD/MM/YYYY')) AND " +
                    "    EXTRACT(YEAR FROM DT.Jour) = EXTRACT(YEAR FROM TO_DATE(?, 'DD/MM/YYYY'))) OR " +
                    "    (TO_CHAR(TO_DATE(?, 'DD/MM/YYYY'), 'DD/MM') = '01/01' AND " +
                    "    EXTRACT(YEAR FROM DT.Jour) = EXTRACT(YEAR FROM TO_DATE(?, 'DD/MM/YYYY'))) " +
                    ")";



            // Préparation de la requête avec les paramètres
            selectStatement = connection.prepareStatement(selectQuery);
            for (int i = 1; i <= 9; i++) {
                selectStatement.setString(i, formattedBirthday);
            }

    
           
            
            resultSet = selectStatement.executeQuery();
            // Initialisation de la variable TotalRecettes à zéro
            BigDecimal TotalDepenses = BigDecimal.ZERO;

            // Parcourir les résultats et ajouter chaque utilisateur à la liste
            if (resultSet.next()) {
                TotalDepenses = resultSet.getBigDecimal("TotalDepenses");
                if (TotalDepenses == null) {
                    TotalDepenses = BigDecimal.ZERO;
                }
            }


            // Construire une chaîne représentant tout le résultat
            String resultat = "TotalDepenses: "+ TotalDepenses+", DATE: "+birthday;
            getTotalDepensesAlloue.add(resultat);
    
        } catch (SQLException e) {
            // Gestion des exceptions SQL
            e.printStackTrace();
        } finally {
            // Fermeture des ressources
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
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
    
        return getTotalDepensesAlloue;
    }






    public List<String> getTotalRecetteAlloue(String birthday) {
        List<String> getTotalRecetteAlloue = new ArrayList<>();
        Connection connection = null;
        PreparedStatement selectStatement = null;
        ResultSet resultSet = null;

        try {
            // Connexion à la base de données
            String url = "jdbc:oracle:thin:@localhost:1521:orcl";
            String username = "Master2IL";
            String passwordDB = "psw";
            connection = DriverManager.getConnection(url, username, passwordDB);

            // Formatage de la date
            String formattedBirthday = "";
            try {
                LocalDate localDate = LocalDate.parse(birthday);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                formattedBirthday = localDate.format(formatter);
            } catch (DateTimeParseException e) {
                e.printStackTrace();
            }

            String selectQuery = "SELECT SUM(" +
                    "CASE " +
                    "    WHEN EXTRACT(YEAR FROM DT.Jour) > EXTRACT(YEAR FROM TO_DATE(?, 'DD/MM/YYYY')) " +
                    "    THEN TO_NUMBER(REPLACE(FR.MONTANT, ',', '.'), '9999999999.999') + " +
                    "    TO_NUMBER(REPLACE(FDT.MONTANT_Propose_Par_etat, ',', '.'), '9999999999.999') " +
                    "    ELSE 0 " +
                    "END + " +
                    "CASE " +
                    "    WHEN EXTRACT(YEAR FROM DT.Jour) <= EXTRACT(YEAR FROM TO_DATE(?, 'DD/MM/YYYY')) " +
                    "    THEN TO_NUMBER(REPLACE(FR.MONTANT, ',', '.'), '9999999999.999') " +
                    "    ELSE 0 " +
                    "END) AS TotalRecettes " +
                    "FROM FaitRecettes FR, FaitDette FDT, DimensionWilayas DW, DimensionTemps DT, DimensionCompte DC, "
                    +
                    "DimensionSoumissionaire DS, DimensionTitre DD " +
                    "WHERE FDT.Id_Soumissionaire = DS.Id_Soumissionaire AND FDT.Id_Titre = DD.Id_Titre AND FDT.Id_Temps = DT.Id_Temps "
                    +
                    "AND FR.Id_Compte = DC.Id_Compte AND FR.Id_Wilayas = DW.Id_Wilayas AND FR.Id_Temps = DT.Id_Temps " +
                    "AND ( " +
                    "    (DT.Jour = TO_DATE(?, 'DD/MM/YYYY') AND TO_CHAR(TO_DATE(?, 'DD/MM/YYYY'), 'DD') <> '01') OR " +
                    "    (TO_CHAR(TO_DATE(?, 'DD/MM/YYYY'), 'DD') = '01' AND " +
                    "    EXTRACT(MONTH FROM DT.Jour) = EXTRACT(MONTH FROM TO_DATE(?, 'DD/MM/YYYY')) AND " +
                    "    EXTRACT(YEAR FROM DT.Jour) = EXTRACT(YEAR FROM TO_DATE(?, 'DD/MM/YYYY'))) OR " +
                    "    (TO_CHAR(TO_DATE(?, 'DD/MM/YYYY'), 'DD/MM') = '01/01' AND " +
                    "    EXTRACT(YEAR FROM DT.Jour) = EXTRACT(YEAR FROM TO_DATE(?, 'DD/MM/YYYY'))) " +
                    ")";

            // Préparation de la requête avec les paramètres
            selectStatement = connection.prepareStatement(selectQuery);
            for (int i = 1; i <= 9; i++) {
                selectStatement.setString(i, formattedBirthday);
            }

            resultSet = selectStatement.executeQuery();

            // Initialisation de la variable TotalRecettes à zéro
            BigDecimal TotalRecettes = BigDecimal.ZERO;

            // Parcourir les résultats et ajouter chaque utilisateur à la liste
            if (resultSet.next()) {
                TotalRecettes = resultSet.getBigDecimal("TotalRecettes");
                if (TotalRecettes == null) {
                    TotalRecettes = BigDecimal.ZERO;
                }
            }

            // Construire une chaîne représentant tout le résultat
            String resultat = "TotalRecettes: " + TotalRecettes + ", DATE: " + birthday;
            getTotalRecetteAlloue.add(resultat);

        } catch (SQLException e) {
            // Gestion des exceptions SQL
            e.printStackTrace();
        } finally {
            // Fermeture des ressources
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
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

        return getTotalRecetteAlloue;
    }
    



    public List<String> getTotalTresor(String birthday) {
        List<String> getTotalTresor = new ArrayList<>();
        Connection connection = null;
        PreparedStatement depensesStatement = null;
        PreparedStatement recettesStatement = null;
        ResultSet depensesResultSet = null;
        ResultSet recettesResultSet = null;
    
        try {
            // Connexion à la base de données
            String url = "jdbc:oracle:thin:@localhost:1521:orcl";
            String username = "Master2IL";
            String passwordDB = "psw";
            connection = DriverManager.getConnection(url, username, passwordDB);
    
            // Formatage de la date
            String formattedBirthday = "";
            try {
                LocalDate localDate = LocalDate.parse(birthday);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                formattedBirthday = localDate.format(formatter);
            } catch (DateTimeParseException e) {
                e.printStackTrace();
            }
    
            String depensesQuery = "SELECT SUM(" +
                    "CASE " +
                    "    WHEN EXTRACT(YEAR FROM DT.Jour) > (EXTRACT(YEAR FROM TO_DATE(?, 'DD/MM/YYYY')) + " +
                    "    CASE " +
                    "       WHEN DD.DUREE_bon = '13semaines' THEN 3 " +
                    "       WHEN DD.DUREE_bon = '12semaines' THEN 3 " +
                    "       WHEN DD.DUREE_bon = '1ans' THEN 12 " +
                    "       WHEN DD.DUREE_bon = '5ans' THEN 60 " +
                    "       WHEN DD.DUREE_bon = '10ans' THEN 120 " +
                    "       ELSE 0 " +
                    "    END) " +
                    "    THEN TO_NUMBER(FD.MONTANT, '9999999999.999') + " +
                    "    TO_NUMBER(REPLACE(FDT.Montant_Adjuge, ',', '.'), '9999999999.999') " +
                    "    ELSE 0 " +
                    "END + " +
                    "CASE " +
                    "    WHEN EXTRACT(YEAR FROM DT.Jour) <= (EXTRACT(YEAR FROM TO_DATE(?, 'DD/MM/YYYY')) + " +
                    "    CASE " +
                    "        WHEN DD.DUREE_bon = '13semaines' THEN 3 " +
                    "        WHEN DD.DUREE_bon = '12semaines' THEN 3 " +
                    "        WHEN DD.DUREE_bon = '1ans' THEN 12 " +
                    "        WHEN DD.DUREE_bon = '5ans' THEN 60 " +
                    "        WHEN DD.DUREE_bon = '10ans' THEN 120 " +
                    "        ELSE 0 " +
                    "    END) " +
                    "    THEN TO_NUMBER(FD.MONTANT, '9999999999.999') + " +
                    "    (CASE " +
                    "        WHEN DD.DUREE_bon = '13semaines' THEN 3 " +
                    "        WHEN DD.DUREE_bon = '12semaines' THEN 3 " +
                    "        WHEN DD.DUREE_bon = '1ans' THEN 12 " +
                    "        WHEN DD.DUREE_bon = '5ans' THEN 60 " +
                    "        WHEN DD.DUREE_bon = '10ans' THEN 120 " +
                    "        ELSE 0 " +
                    "    END * (TO_NUMBER(REPLACE(FDT.Montant_Propose_Par_Soumissionaire, ',', '.'), '9999999999.999') * " +
                    "    TO_NUMBER(REPLACE(FDT.Montant_Coupoun, ',', '.'), '9999999999.999')) / 100) + " +
                    "    TO_NUMBER(REPLACE(FDT.MONTANT_Propose_Par_etat, ',', '.'), '9999999999.999') " +
                    "    ELSE 0 " +
                    "END) AS TotalDepenses " +
                    "FROM FaitDepenses FD, DimensionActivite DA, DimensionWilayas DW, DimensionTemps DT, " +
                    "DimensionOrdonnateur DO, DimensionMandat DM, DimensionNatureEconomic DE, FaitDette FDT, " +
                    "DimensionSoumissionaire DS, DimensionTitre DD " +
                    "WHERE FDT.Id_Soumissionaire = DS.Id_Soumissionaire AND FDT.Id_Temps = DT.Id_Temps " +
                    "AND FDT.Id_Titre = DD.Id_Titre AND FD.ID_ACTIVITE = DA.ID_ACTIVITE " +
                    "AND FD.ID_MANDAT = DM.ID_MANDAT AND FD.Id_Temps = DT.Id_Temps " +
                    "AND FD.Id_Ordonnateur = DO.Id_Ordonnateur AND FD.Id_Wilayas = DW.Id_Wilayas " +
                    "AND FD.Id_N_economic = DE.Id_N_economic " +
                    "AND ( " +
                    "    (DT.Jour = TO_DATE(?, 'DD/MM/YYYY') AND TO_CHAR(TO_DATE(?, 'DD/MM/YYYY'), 'DD') <> '01') OR " +
                    "    (TO_CHAR(TO_DATE(?, 'DD/MM/YYYY'), 'DD') = '01' AND " +
                    "    EXTRACT(MONTH FROM DT.Jour) = EXTRACT(MONTH FROM TO_DATE(?, 'DD/MM/YYYY')) AND " +
                    "    EXTRACT(YEAR FROM DT.Jour) = EXTRACT(YEAR FROM TO_DATE(?, 'DD/MM/YYYY'))) OR " +
                    "    (TO_CHAR(TO_DATE(?, 'DD/MM/YYYY'), 'DD/MM') = '01/01' AND " +
                    "    EXTRACT(YEAR FROM DT.Jour) = EXTRACT(YEAR FROM TO_DATE(?, 'DD/MM/YYYY'))) " +
                    ")";
    
            String recettesQuery = "SELECT SUM(" +
                    "CASE " +
                    "    WHEN EXTRACT(YEAR FROM DT.Jour) > EXTRACT(YEAR FROM TO_DATE(?, 'DD/MM/YYYY')) " +
                    "    THEN TO_NUMBER(REPLACE(FR.MONTANT, ',', '.'), '9999999999.999') + " +
                    "    TO_NUMBER(REPLACE(FDT.MONTANT_Propose_Par_etat, ',', '.'), '9999999999.999') " +
                    "    ELSE 0 " +
                    "END + " +
                    "CASE " +
                    "    WHEN EXTRACT(YEAR FROM DT.Jour) <= EXTRACT(YEAR FROM TO_DATE(?, 'DD/MM/YYYY')) " +
                    "    THEN TO_NUMBER(REPLACE(FR.MONTANT, ',', '.'), '9999999999.999') " +
                    "    ELSE 0 " +
                    "END) AS TotalRecettes " +
                    "FROM FaitRecettes FR, FaitDette FDT, DimensionWilayas DW, DimensionTemps DT, DimensionCompte DC, " +
                    "DimensionSoumissionaire DS, DimensionTitre DD " +
                    "WHERE FDT.Id_Soumissionaire = DS.Id_Soumissionaire AND FDT.Id_Titre = DD.Id_Titre AND FDT.Id_Temps = DT.Id_Temps " +
                    "AND FR.Id_Compte = DC.Id_Compte AND FR.Id_Wilayas = DW.Id_Wilayas AND FR.Id_Temps = DT.Id_Temps " +
                    "AND ( " +
                    "    (DT.Jour = TO_DATE(?, 'DD/MM/YYYY') AND TO_CHAR(TO_DATE(?, 'DD/MM/YYYY'), 'DD') <> '01') OR " +
                    "    (TO_CHAR(TO_DATE(?, 'DD/MM/YYYY'), 'DD') = '01' AND " +
                    "    EXTRACT(MONTH FROM DT.Jour) = EXTRACT(MONTH FROM TO_DATE(?, 'DD/MM/YYYY')) AND " +
                    "    EXTRACT(YEAR FROM DT.Jour) = EXTRACT(YEAR FROM TO_DATE(?, 'DD/MM/YYYY'))) OR " +
                    "    (TO_CHAR(TO_DATE(?, 'DD/MM/YYYY'), 'DD/MM') = '01/01' AND " +
                    "    EXTRACT(YEAR FROM DT.Jour) = EXTRACT(YEAR FROM TO_DATE(?, 'DD/MM/YYYY'))) " +
                    ")";
    
            // Préparation de la requête pour les dépenses avec les paramètres
            depensesStatement = connection.prepareStatement(depensesQuery);
            for (int i = 1; i <= 9; i++) {
                depensesStatement.setString(i, formattedBirthday);
            }
    
            // Préparation de la requête pour les recettes avec les paramètres
            recettesStatement = connection.prepareStatement(recettesQuery);
            for (int i = 1; i <= 9; i++) {
                recettesStatement.setString(i, formattedBirthday);
            }
    
            // Exécution des requêtes
            depensesResultSet = depensesStatement.executeQuery();
            recettesResultSet = recettesStatement.executeQuery();
    
            // Récupération des résultats des dépenses
            BigDecimal totalDepenses = BigDecimal.ZERO;
            if (depensesResultSet.next()) {
                totalDepenses = depensesResultSet.getBigDecimal("TotalDepenses");
                if (totalDepenses == null) {
                    totalDepenses = BigDecimal.ZERO;
                }
            }
    
            // Récupération des résultats des recettes
            BigDecimal totalRecettes = BigDecimal.ZERO;
            if (recettesResultSet.next()) {
                totalRecettes = recettesResultSet.getBigDecimal("TotalRecettes");
                if (totalRecettes == null) {
                    totalRecettes = BigDecimal.ZERO;
                }
            }
    
            // Calcul du total (Recettes - Dépenses) avec gestion des exceptions
            BigDecimal totalTresor = BigDecimal.ZERO;
            try {
                totalTresor = totalRecettes.subtract(totalDepenses);
            } catch (NullPointerException e) {
                totalTresor = BigDecimal.ZERO;
            }
    
            // Construction de la chaîne de résultat
            String resultat = "TotalTresor: " + totalTresor + ", DATE: " + birthday;
            getTotalTresor.add(resultat);
    
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Fermeture des ressources
            try {
                if (depensesResultSet != null) {
                    depensesResultSet.close();
                }
                if (recettesResultSet != null) {
                    recettesResultSet.close();
                }
                if (depensesStatement != null) {
                    depensesStatement.close();
                }
                if (recettesStatement != null) {
                    recettesStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    
        return getTotalTresor;
    }
    
    



     /*********************************** Dashbord Reel****************************************/

    
}
