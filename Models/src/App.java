import java.io.*;
import java.net.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class App {

  // Méthode pour hacher le mot de passe
  public static String hashPassword(String password)
      throws NoSuchAlgorithmException {
    try {
      // Utilisation de l'algorithme SHA-256 pour le hachage
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hashedBytes = digest.digest(password.getBytes());

      // Encodage du hash en base64
      return Base64.getEncoder().encodeToString(hashedBytes);
    } catch (NoSuchAlgorithmException e) {
      // Gestion de l'exception NoSuchAlgorithmException
      throw new NoSuchAlgorithmException("Algorithm not found: SHA-256");
    }
  }

  // Méthode pour enregistrer un utilisateur dans la base de données
  public static String registerUser(
      String name,
      String email,
      String password,
      String role) {
    Connection connection = null;
    PreparedStatement selectStatement = null;
    PreparedStatement insertStatement = null;

    try {
      // Connexion à la base de données
      String url = "jdbc:oracle:thin:@localhost:1521/orcl";
      String username = "projetMaster";
      String passwordDB = "psw";
      connection = DriverManager.getConnection(url, username, passwordDB);

      // Vérifier si l'e-mail existe déjà dans la base de données
      String selectQuery = "SELECT COUNT(*) FROM utilisateur WHERE email = ?";
      selectStatement = connection.prepareStatement(selectQuery);
      selectStatement.setString(1, email);
      ResultSet resultSet = selectStatement.executeQuery();
      resultSet.next();
      int count = resultSet.getInt(1);

      if (count > 0) {
        return "Email existe deja.....";
      } else {
        // Hasher le mot de passe
        String hashedPassword = hashPassword(password);

        // Insérer l'utilisateur dans la base de données
        String insertQuery = "INSERT INTO utilisateur (id,nom, email, mot_de_passe, role) VALUES (utilisateur_seq.NEXTVAL,?, ?, ?, ?)";
        insertStatement = connection.prepareStatement(insertQuery);
        insertStatement.setString(1, name);
        insertStatement.setString(2, email);
        insertStatement.setString(3, hashedPassword);
        insertStatement.setString(4, role); // Utilisation du rôle passé en argument
        insertStatement.executeUpdate();

        return "Enregistrement réussi....";
      }
    } catch (SQLException | NoSuchAlgorithmException e) {
      // Gestion des exceptions SQL et NoSuchAlgorithmException
      e.printStackTrace();
      return "Erreur lors de l'enregistrement: " + e.getMessage();
    } finally {
      // Fermeture des ressources
      try {
        if (selectStatement != null) {
          selectStatement.close();
        }
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

  public static String authenticateUser(String email, String password) {
    Connection connection = null;
    PreparedStatement selectStatement = null;

    try {
      // Connexion à la base de données
      String url = "jdbc:oracle:thin:@localhost:1521/orcl";
      String username = "projetMaster";
      String passwordDB = "psw";
      connection = DriverManager.getConnection(url, username, passwordDB);

      // Vérifier si l'e-mail existe dans la base de données
      String selectQuery = "SELECT role, mot_de_passe FROM utilisateur WHERE email = ?";
      selectStatement = connection.prepareStatement(selectQuery);
      selectStatement.setString(1, email);
      ResultSet resultSet = selectStatement.executeQuery();

      if (resultSet.next()) {
        // L'utilisateur existe, récupérer son rôle et son mot de passe crypté
        String role = resultSet.getString("role");
        String hashedPassword = resultSet.getString("mot_de_passe");

        // Comparer le mot de passe fourni avec le mot de passe crypté
        if (verifyPassword(password, hashedPassword)) {
          // Le mot de passe est correct, vérifier le rôle
          if (role == null || role.isEmpty()) {
            System.out.println("Role vide");
            return "Role vide";
          } else {
            System.out.println("Login avec succès....");
            return role;
          }
        } else {
          // Le mot de passe est incorrect
          System.out.println("Mot de passe incorrect");
          return "Mot de passe incorrect";
        }
      } else {
        // Aucun utilisateur trouvé avec cet e-mail
        System.out.println("Compte inexistant");
        return "Compte inexistant";
      }
    } catch (SQLException e) {
      // Gestion des exceptions SQL
      e.printStackTrace();
      return "Erreur SQL : " + e.getMessage();
    } catch (NoSuchAlgorithmException e) {
      // Gestion de l'exception NoSuchAlgorithmException
      e.printStackTrace();
      return "Erreur de hachage du mot de passe : " + e.getMessage();
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
        // Gestion des exceptions lors de la fermeture des ressources
        e.printStackTrace();
      }
    }
  }

  // Méthode pour vérifier si le mot de passe fourni correspond au mot de passe
  // crypté
  private static boolean verifyPassword(String password, String hashedPassword)
      throws NoSuchAlgorithmException {
    // Hasher le mot de passe fourni
    String hashedInputPassword = hashPassword(password);
    // System.out.println(hashedInputPassword);

    // Comparer le hachage du mot de passe fourni avec le hachage stocké
    return hashedInputPassword.equals(hashedPassword);
  }

  public static String[] convertStringToArray(String input) {
    // Diviser la chaîne en fonction des virgules
    String myChar = "\u001F";
    String[] array = input.split(myChar);

    return array;
  }

  public static void lireFichierTexte(String cheminFichier) {
    try (
        BufferedReader br = new BufferedReader(new FileReader(cheminFichier))) {
      String ligne;

      int position = 1; // Initialisation de la position à 1

      // Lecture de chaque ligne du fichier texte
      while ((ligne = br.readLine()) != null) {
        // Affichage de la position et de la ligne lue
        System.out.println("Position " + position + " : " + ligne);

        // Incrément de la position pour la ligne suivante
        position++;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static boolean contains(Set<String> tab1, String[] tab2) {
    boolean contientToutAxeProgramatique = true;
    for (String valeur : tab1) {
      boolean estPresent = false;
      for (String element : tab2) {
        if (element.equals(valeur)) {
          estPresent = true;
          break; // Sort de la boucle interne dès qu'on trouve la valeur
        }
      }
      if (!estPresent) {
        contientToutAxeProgramatique = false;
        break; // Si une valeur n'est pas trouvée, inutile de continuer à parcourir le tableau
      }
    }
    return contientToutAxeProgramatique;
  }

  public static void main(String[] args) {
    try {
      // Créez une instance de la classe Users
      Users users = new Users();
      relationnel relationnel = new relationnel();
      Entrepot Entrepot = new Entrepot();
      DatagramSocket serverSocket = new DatagramSocket(4000); // Port sur lequel le serveur écoute
      System.out.println("Server listening on port 4000...");

      while (true) {
        byte[] receiveData = new byte[5000000];

        DatagramPacket receivePacket = new DatagramPacket(
            receiveData,
            receiveData.length);
        serverSocket.receive(receivePacket);

        String message = new String(
            receivePacket.getData(),
            0,
            receivePacket.getLength());

        // Diviser la chaîne
        String groupSeparator = "\u001D";
        String[] parts = message.split(groupSeparator);

        // Affichage des éléments du tableau
        /*
         * for (String part : parts) {
         * System.out.println(part);
         * }
         */
        System.out.println(parts.length);

        if (parts.length == 3) { // login + register
          // Accéder à chaque élément individuellement
          String part1 = parts[0];
          String part2 = parts[1];
          String part3 = parts[2];

          // System.out.println("Received part from client: " + part1+" "+part2+"
          // "+part3);

          if (part3.equals("login")) {
            String Role = authenticateUser(part1, part2);
            System.out.println(Role);

            // Récupérer l'adresse IP et le numéro de port du client
            InetAddress clientAddress = receivePacket.getAddress();
            int clientPort = receivePacket.getPort();

            // System.out.println(clientAddress);
            // System.out.println(clientPort);

            // Convertir le role de l'utilisateur en tableau de bytes
            byte[] RoleBytes = String.valueOf(Role).getBytes();

            // Créer un nouveau paquet avec le role de l'utilisateur comme données
            DatagramPacket responsePacket = new DatagramPacket(
                RoleBytes,
                RoleBytes.length,
                clientAddress,
                clientPort);

            // Envoyer le paquet contenant le role de l'utilisateur au client
            serverSocket.send(responsePacket);
          } else {
            // Test de l'enregistrement d'un utilisateur avec un rôle par défaut
            String msjRegister = registerUser(
                part1,
                part2,
                part3,
                "");
            System.out.println(msjRegister);

            // Récupérer l'adresse IP et le numéro de port du client
            InetAddress clientAddress = receivePacket.getAddress();
            int clientPort = receivePacket.getPort();

            // System.out.println(clientAddress);
            // System.out.println(clientPort);

            // Convertir la chaîne en tableau de bytes
            byte[] msjRegisterBytes = msjRegister.getBytes();

            // Créer un nouveau paquet avec l'ID de l'utilisateur comme données
            DatagramPacket responsePacket = new DatagramPacket(
                msjRegisterBytes,
                msjRegisterBytes.length,
                clientAddress,
                clientPort);

            // Envoyer le paquet contenant l'ID de l'utilisateur au client
            serverSocket.send(responsePacket);
          }
        } else if (parts.length == 2) { // All users + dashbord Reel
          String Part1 = parts[0];
          String Part2 = parts[1];
          if (Part2.equals("Users")) {
            // Utilisez les méthodes de la classe Users
            List<String> allUsers = users.getAllUsers();

            // Faites quelque chose avec la liste des utilisateurs, par exemple affichez-les
            for (String user : allUsers) {
              System.out.println(user);
            }

            // Convertir la liste des utilisateurs en une seule chaîne
            StringBuilder userListString = new StringBuilder();
            for (String user : allUsers) {
              userListString.append(user).append("\n");
            }

            // Convertir la chaîne en tableau de bytes
            byte[] userData = userListString.toString().getBytes();

            // Récupérer l'adresse IP et le numéro de port du client
            InetAddress clientAddress = receivePacket.getAddress();
            int clientPort = receivePacket.getPort();

            // Créer un DatagramPacket avec les données des utilisateurs et les envoyer au
            // client
            DatagramPacket responsePacket = new DatagramPacket(
                userData,
                userData.length,
                clientAddress,
                clientPort);
            serverSocket.send(responsePacket);

          } else if (Part2.equals("Reel")) { // dashbord Reel

            List<String> getTotalCreditsAlloue = Entrepot.getTotalCreditsAlloue(Part1);

            // Faites quelque chose avec la liste , par exemple affichez-les
            for (String value : getTotalCreditsAlloue) {
              System.out.println(value);
            }

            // Convertir la liste des utilisateurs en une seule chaîne
            StringBuilder getTotalCreditsAlloueString = new StringBuilder();
            for (String user : getTotalCreditsAlloue) {
              getTotalCreditsAlloueString.append(user).append("\n");
            }

            // Convertir la chaîne en tableau de bytes
            byte[] CreditsData = getTotalCreditsAlloueString.toString().getBytes();

            // Récupérer l'adresse IP et le numéro de port du client
            InetAddress clientAddress1 = receivePacket.getAddress();
            int clientPort1 = receivePacket.getPort();

            // Créer un DatagramPacket avec les données des utilisateurs et les envoyer au
            // client
            DatagramPacket responsePacket1 = new DatagramPacket(
              CreditsData,
              CreditsData.length,
              clientAddress1,
              clientPort1);
            serverSocket.send(responsePacket1);

            List<String> getTotalDepensesAlloue = Entrepot.getTotalDepensesAlloue(Part1);

            // Faites quelque chose avec la liste , par exemple affichez-les
            for (String user : getTotalDepensesAlloue) {
              System.out.println(user);
            }

            // Convertir la liste des utilisateurs en une seule chaîne
            StringBuilder getTotalDepensesAlloueString = new StringBuilder();
            for (String user : getTotalDepensesAlloue) {
              getTotalDepensesAlloueString.append(user).append("\n");
            }

            // Convertir la chaîne en tableau de bytes
            byte[] DepensesData = getTotalDepensesAlloueString.toString().getBytes();

            // Récupérer l'adresse IP et le numéro de port du client
            InetAddress clientAddress2 = receivePacket.getAddress();
            int clientPort2 = receivePacket.getPort();

            // Créer un DatagramPacket avec les données des utilisateurs et les envoyer au
            // client
            DatagramPacket responsePacket2 = new DatagramPacket(
              DepensesData,
              DepensesData.length,
              clientAddress2,
              clientPort2);
            serverSocket.send(responsePacket2);

            List<String> getTotalRecetteAlloue = Entrepot.getTotalRecetteAlloue(Part1);

            // Faites quelque chose avec la liste , par exemple affichez-les
            for (String user : getTotalRecetteAlloue) {
              System.out.println(user);
            }

            // Convertir la liste des utilisateurs en une seule chaîne
            StringBuilder getTotalRecetteAlloueString = new StringBuilder();
            for (String user : getTotalRecetteAlloue) {
              getTotalRecetteAlloueString.append(user).append("\n");
            }

            // Convertir la chaîne en tableau de bytes
            byte[] RecetteData = getTotalRecetteAlloueString.toString().getBytes();

            // Récupérer l'adresse IP et le numéro de port du client
            InetAddress clientAddress3 = receivePacket.getAddress();
            int clientPort3 = receivePacket.getPort();

            // Créer un DatagramPacket avec les données des utilisateurs et les envoyer au
            // client
            DatagramPacket responsePacket3 = new DatagramPacket(
              RecetteData,
              RecetteData.length,
              clientAddress3,
              clientPort3);
            serverSocket.send(responsePacket3);


            List<String> getTotalTresor = Entrepot.getTotalTresor(Part1);

            // Faites quelque chose avec la liste , par exemple affichez-les
            for (String user : getTotalTresor) {
              System.out.println(user);
            }

            // Convertir la liste des utilisateurs en une seule chaîne
            StringBuilder getTotalTresorString = new StringBuilder();
            for (String user : getTotalTresor) {
              getTotalTresorString.append(user).append("\n");
            }

            // Convertir la chaîne en tableau de bytes
            byte[] TresorData = getTotalTresorString.toString().getBytes();

            // Récupérer l'adresse IP et le numéro de port du client
            InetAddress clientAddress4 = receivePacket.getAddress();
            int clientPort4 = receivePacket.getPort();

            // Créer un DatagramPacket avec les données des utilisateurs et les envoyer au
            // client
            DatagramPacket responsePacket4 = new DatagramPacket(
              TresorData,
              TresorData.length,
              clientAddress4,
              clientPort4);
            serverSocket.send(responsePacket4);
            System.out.println("fin");

          } else if (Part2.equals("Actions")) {
            // Utilisez les méthodes de la classe Relationnel
            List<String> allActions = relationnel.getAllActions();

            // Faites quelque chose avec la liste des Actions, par exemple affichez-les
            for (String Action : allActions) {
              System.out.println(Action);
            }

            // Convertir la liste des Actions en une seule chaîne
            StringBuilder ActionListString = new StringBuilder();
            for (String Action : allActions) {
              ActionListString.append(Action).append("\n");
            }

            // Convertir la chaîne en tableau de bytes
            byte[] ActionData = ActionListString.toString().getBytes();

            // Récupérer l'adresse IP et le numéro de port du client
            InetAddress clientAddress = receivePacket.getAddress();
            int clientPort = receivePacket.getPort();

            // Créer un DatagramPacket avec les données des Action et les envoyer au
            // client
            DatagramPacket responsePacket = new DatagramPacket(
                ActionData,
                ActionData.length,
                clientAddress,
                clientPort);
            serverSocket.send(responsePacket);


          }else if (Part2.equals("Wilaya")) {
            // Utilisez les méthodes de la classe Relationnel
            List<String> AllWilaya = relationnel.getAllWilaya();

            // Faites quelque chose avec la liste des Wilayas, par exemple affichez-les
            for (String Wilaya : AllWilaya) {
              System.out.println(Wilaya);
            }

            // Convertir la liste des Wilayas en une seule chaîne
            StringBuilder WilayaListString = new StringBuilder();
            for (String Wilaya : AllWilaya) {
              WilayaListString.append(Wilaya).append("\n");
            }

            // Convertir la chaîne en tableau de bytes
            byte[] WilayaData = WilayaListString.toString().getBytes();

            // Récupérer l'adresse IP et le numéro de port du client
            InetAddress clientAddress = receivePacket.getAddress();
            int clientPort = receivePacket.getPort();

            // Créer un DatagramPacket avec les données des Action et les envoyer au
            // client
            DatagramPacket responsePacket = new DatagramPacket(
              WilayaData,
              WilayaData.length,
                clientAddress,
                clientPort);
            serverSocket.send(responsePacket);


          } else if (Part2.equals("Credit")) {
            System.out.println("Bonjour");
            // Utilisez les méthodes de la classe Relationnel
            List<String> Alldette = relationnel.getAllCredit();

            // Faites quelque chose avec la liste des Dette, par exemple affichez-les
            for (String Dette : Alldette) {
              System.out.println(Dette);
            }

            // Convertir la liste des Dette en une seule chaîne
            StringBuilder DetteListString = new StringBuilder();
            for (String Dette : Alldette) {
              DetteListString.append(Dette).append("\n");
            }

            // Convertir la chaîne en tableau de bytes
            byte[] DetteData = DetteListString.toString().getBytes();

            // Récupérer l'adresse IP et le numéro de port du client
            InetAddress clientAddress = receivePacket.getAddress();
            int clientPort = receivePacket.getPort();

            // Créer un DatagramPacket avec les données des Action et les envoyer au
            // client
            DatagramPacket responsePacket = new DatagramPacket(
              DetteData,
              DetteData.length,
                clientAddress,
                clientPort);
            serverSocket.send(responsePacket);


          } else if (Part2.equals("Dette")) {
            System.out.println("Bonjour");
            // Utilisez les méthodes de la classe Relationnel
            List<String> Alldette = relationnel.getAllDette();

            // Faites quelque chose avec la liste des Dette, par exemple affichez-les
            for (String Dette : Alldette) {
              System.out.println(Dette);
            }

            // Convertir la liste des Dette en une seule chaîne
            StringBuilder DetteListString = new StringBuilder();
            for (String Dette : Alldette) {
              DetteListString.append(Dette).append("\n");
            }

            // Convertir la chaîne en tableau de bytes
            byte[] DetteData = DetteListString.toString().getBytes();

            // Récupérer l'adresse IP et le numéro de port du client
            InetAddress clientAddress = receivePacket.getAddress();
            int clientPort = receivePacket.getPort();

            // Créer un DatagramPacket avec les données des Action et les envoyer au
            // client
            DatagramPacket responsePacket = new DatagramPacket(
              DetteData,
              DetteData.length,
                clientAddress,
                clientPort);
            serverSocket.send(responsePacket);


          } else if (Part2.equals("Recette")) {
            System.out.println("Bonjour");
            // Utilisez les méthodes de la classe Relationnel
            List<String> Alldette = relationnel.getAllRecettes();

            // Faites quelque chose avec la liste des Dette, par exemple affichez-les
            for (String Dette : Alldette) {
              System.out.println(Dette);
            }

            // Convertir la liste des Dette en une seule chaîne
            StringBuilder DetteListString = new StringBuilder();
            for (String Dette : Alldette) {
              DetteListString.append(Dette).append("\n");
            }

            // Convertir la chaîne en tableau de bytes
            byte[] DetteData = DetteListString.toString().getBytes();

            // Récupérer l'adresse IP et le numéro de port du client
            InetAddress clientAddress = receivePacket.getAddress();
            int clientPort = receivePacket.getPort();

            // Créer un DatagramPacket avec les données des Action et les envoyer au
            // client
            DatagramPacket responsePacket = new DatagramPacket(
              DetteData,
              DetteData.length,
                clientAddress,
                clientPort);
            serverSocket.send(responsePacket);


          }

          

        } else if (parts.length == 1) { // supprimer un utilisateur
          String UserId = parts[0];
          int userIdInt = Integer.parseInt(UserId);
          String SuppUser = users.deleteUserById(userIdInt);
          System.out.println(SuppUser);
          // Convertir la chaîne en tableau de bytes
          byte[] userData = SuppUser.toString().getBytes();

          // Récupérer l'adresse IP et le numéro de port du client
          InetAddress clientAddress = receivePacket.getAddress();
          int clientPort = receivePacket.getPort();

          // Créer un DatagramPacket avec les données des utilisateurs et les envoyer au
          // client
          DatagramPacket responsePacket = new DatagramPacket(
              userData,
              userData.length,
              clientAddress,
              clientPort);
          serverSocket.send(responsePacket);
        } else if (parts.length == 5) { // update role user
          String UserId = parts[0];
          int userIdInt = Integer.parseInt(UserId);
          String userRole = parts[1];
          String parts4 = parts[4];
          System.out.println(parts4);
          if (parts4.equals("moi")) { // modifier user to user haut niveau
            String updateUSERToHautNiveau = users.updateUserTo_User_haut_niveau(
                userIdInt,
                userRole);
            System.out.println(updateUSERToHautNiveau);
            // Convertir la chaîne en tableau de bytes
            byte[] userData = updateUSERToHautNiveau.toString().getBytes();

            // Récupérer l'adresse IP et le numéro de port du client
            InetAddress clientAddress = receivePacket.getAddress();
            int clientPort = receivePacket.getPort();

            // Créer un DatagramPacket avec les données des utilisateurs et les envoyer au
            // client
            DatagramPacket responsePacket = new DatagramPacket(
                userData,
                userData.length,
                clientAddress,
                clientPort);
            serverSocket.send(responsePacket);
          }

          if (parts4.equals("lui")) { // modifier user to user dépenses
            String updateUSERToUserDepenses = users.updateUserTo_user_Depenses(
                userIdInt,
                userRole);
            System.out.println(updateUSERToUserDepenses);
            // Convertir la chaîne en tableau de bytes
            byte[] userData = updateUSERToUserDepenses.toString().getBytes();

            // Récupérer l'adresse IP et le numéro de port du client
            InetAddress clientAddress = receivePacket.getAddress();
            int clientPort = receivePacket.getPort();

            // Créer un DatagramPacket avec les données des utilisateurs et les envoyer au
            // client
            DatagramPacket responsePacket = new DatagramPacket(
                userData,
                userData.length,
                clientAddress,
                clientPort);
            serverSocket.send(responsePacket);
          }

          if (parts4.equals("leur")) { // modifier user to admin back office
            String updateUSERToAdminBackOffice = users.updateUserTo_Admin_Back_Office(
                userIdInt,
                userRole);
            System.out.println(updateUSERToAdminBackOffice);
            // Convertir la chaîne en tableau de bytes
            byte[] userData = updateUSERToAdminBackOffice.toString().getBytes();

            // Récupérer l'adresse IP et le numéro de port du client
            InetAddress clientAddress = receivePacket.getAddress();
            int clientPort = receivePacket.getPort();

            // Créer un DatagramPacket avec les données des utilisateurs et les envoyer au
            // client
            DatagramPacket responsePacket = new DatagramPacket(
                userData,
                userData.length,
                clientAddress,
                clientPort);
            serverSocket.send(responsePacket);
          }

          if (parts4.equals("nous")) { // modifier user to admin front office
            String updateUSERToAdminFrontOffice = users.updateUserTo_Admin_front_Office(
                userIdInt,
                userRole);
            System.out.println(updateUSERToAdminFrontOffice);
            // Convertir la chaîne en tableau de bytes
            byte[] userData = updateUSERToAdminFrontOffice
                .toString()
                .getBytes();

            // Récupérer l'adresse IP et le numéro de port du client
            InetAddress clientAddress = receivePacket.getAddress();
            int clientPort = receivePacket.getPort();

            // Créer un DatagramPacket avec les données des utilisateurs et les envoyer au
            // client
            DatagramPacket responsePacket = new DatagramPacket(
                userData,
                userData.length,
                clientAddress,
                clientPort);
            serverSocket.send(responsePacket);
          }

          if (parts4.equals("vous")) { // modifier user to User Financement
            String updateUSERToUserFinancement = users.updateUSER_AU_User_Financement(
                userIdInt,
                userRole);
            System.out.println(updateUSERToUserFinancement);
            // Convertir la chaîne en tableau de bytes
            byte[] userData = updateUSERToUserFinancement
                .toString()
                .getBytes();

            // Récupérer l'adresse IP et le numéro de port du client
            InetAddress clientAddress = receivePacket.getAddress();
            int clientPort = receivePacket.getPort();

            // Créer un DatagramPacket avec les données des utilisateurs et les envoyer au
            // client
            DatagramPacket responsePacket = new DatagramPacket(
                userData,
                userData.length,
                clientAddress,
                clientPort);
            serverSocket.send(responsePacket);
          }

          if (parts4.equals("tu")) { // modifier user to User Recettes
            String updateUSERToUserRecettes = users.updateUSER_AU_User_Recettes(
                userIdInt,
                userRole);
            System.out.println(updateUSERToUserRecettes);
            // Convertir la chaîne en tableau de bytes
            byte[] userData = updateUSERToUserRecettes
                .toString()
                .getBytes();

            // Récupérer l'adresse IP et le numéro de port du client
            InetAddress clientAddress = receivePacket.getAddress();
            int clientPort = receivePacket.getPort();

            // Créer un DatagramPacket avec les données des utilisateurs et les envoyer au
            // client
            DatagramPacket responsePacket = new DatagramPacket(
                userData,
                userData.length,
                clientAddress,
                clientPort);
            serverSocket.send(responsePacket);
          }

        } else if (parts.length == 4) { // ETL Fichier
            String part1 = parts[0];
            String part2 = parts[1];
            // Ajouter la valeur de part2 à la liste des valeurs reçues
             //System.out.println(part1);
             //System.out.println(part2);
            String[] array = convertStringToArray(part2);

            // Afficher les éléments du tableau
            for (String str : array) {
              System.out.println(str);
            }
            String cheminFichier = "donnees.txt";

            BufferedReader bfr = new BufferedReader(
                new FileReader(cheminFichier));
            String premiereLigne = bfr.readLine(); // Lecture de la première ligne du fichier
            bfr.close();

            if (premiereLigne == null || premiereLigne.isEmpty()) {
              // Le fichier est vide, ouvrir en mode écriture pour créer le fichier et ajouter
              // une nouvelle entrée
              // ajouterEntree(cheminFichier,
              // "GESTION|MOIS|ORD|PORTEF|PROG|S_PROG|ACTION|S_ACTION|S_CAT|NVL(DISPOS,'0')|SUM(TOT_CREDIT)|SUM(TOT_DEB)|SUM(SOLDE)",
              // "2023|12|108004|011|044|01|2004|000|27800|0|800000|705399,37|94600,63");
              System.out.println("fichier vide");

              // Créer le fichier et écrire les données
              try {
                FileWriter fichier = new FileWriter(cheminFichier);

                // Écrire l'entête de colonne
                fichier.write(part1 + ";" + "\n");

                // Écrire les valeurs dans la colonne
                for (String valeur : array) {
                  fichier.write(valeur + ";" + "\n");
                }

                fichier.close(); // Fermer le fichier après écriture
                System.out.println(
                    "Les données ont été écrites avec succès dans le fichier.");
              } catch (IOException e) {
                System.out.println(
                    "Une erreur s'est produite lors de l'écriture dans le fichier : " +
                        e.getMessage());
              }
            } else {
              // Le fichier contient déjà des données, ouvrir en mode lecture pour lire le
              // contenu
              // lireFichier(cheminFichier);
              // Ensuite, ajouter une nouvelle entrée
              System.out.println("fichier non vide");
              // Appel de la méthode pour lire le fichier texte
              BufferedReader br = null;
              try {
                br = new BufferedReader(new FileReader(cheminFichier));
                String ligne;
                int position = 0; // Initialisation de la position à 0

                // Détermination du nombre de lignes dans le fichier pour créer le tableau
                int nombreLignes = (int) br.lines().count();
                String[] tableau = new String[nombreLignes];
                System.out.println(
                    "nombre de ligne est égal à " + nombreLignes);

                // Retour au début du fichier pour le lire à nouveau
                br.close();
                br = new BufferedReader(new FileReader(cheminFichier));

                // Lecture de chaque ligne du fichier texte
                while ((ligne = br.readLine()) != null) {
                  // Ajout de la ligne actuelle au tableau
                  tableau[position] = ligne;
                  System.out.println(ligne);
                  position++;
                }

                // Affichage du tableau
                /*
                 * for (int i = 0; i < tableau.length; i++) {
                 * System.out.print(tableau[i]);
                 * System.out.println();
                 * }
                 */

                // Suppression du contenu du fichier
                try {
                  FileWriter writer = new FileWriter(cheminFichier);
                  writer.write(""); // Écriture d'une chaîne vide pour vider le fichier
                  writer.close();
                  System.out.println(
                      "Contenu du fichier supprimé avec succès.");
                } catch (IOException e) {
                  e.printStackTrace();
                  System.out.println(
                      "Une erreur s'est produite lors de la suppression du contenu du fichier.");
                }

                // Créer un StringBuilder pour construire la chaîne résultante
                StringBuilder sb = new StringBuilder();
                int i = 0; // Initialisation du compteur i
                int j = 0; // Initialisation du compteur j
                while ((i < tableau.length) && (j < array.length)) {
                  if (i == 0) {
                    sb
                        .append(tableau[i])
                        .append(part1)
                        .append(";")
                        .append("\n");
                    String ligneAAjouter = sb.toString();
                    // Écriture de la ligne dans le fichier
                    // System.out.println(ligneAAjouter);
                    FileWriter writer = null;
                    try {
                      writer = new FileWriter(cheminFichier, true); // Le deuxième paramètre indique que le fichier sera
                                                                    // écrasé
                      writer.write(ligneAAjouter); // Écriture de la ligne dans le fichier
                      // System.out.println(ligneAAjouter);
                      // String[] elements = ligneAAjouter.split(";");
                      // Afficher chaque élément
                      /*
                       * for (String element : elements) {
                       * System.out.println(element);
                       * }
                       */

                    } catch (IOException e) {
                      e.printStackTrace();
                    } finally {
                      // Fermeture du FileWriter dans le bloc finally
                      try {
                        if (writer != null) {
                          writer.close();
                        }
                      } catch (IOException e) {
                        e.printStackTrace();
                      }
                    }
                    i++;
                    sb.setLength(0); // vider le contenu de StringBuilder
                  } else {
                    // Parcourir les éléments de array et les concaténer avec le StringBuilder
                    sb
                        .append(tableau[i])
                        .append(array[j])
                        .append(";")
                        .append("\n");
                    String ligneAAjouter = sb.toString();
                    // Écriture de la ligne dans le fichier
                    // System.out.println(ligneAAjouter);
                    FileWriter writer = null;
                    try {
                      writer = new FileWriter(cheminFichier, true); // Le deuxième paramètre indique que le fichier sera
                                                                    // écrasé
                      writer.write(ligneAAjouter); // Écriture de la ligne dans le fichier
                      // String[] elements = ligneAAjouter.split(";");
                      // Afficher chaque élément
                      /*
                       * for (String element : elements) {
                       * System.out.println(element);
                       * }
                       */
                    } catch (IOException e) {
                      e.printStackTrace();
                    } finally {
                      // Fermeture du FileWriter dans le bloc finally
                      try {
                        if (writer != null) {
                          writer.close();
                        }
                      } catch (IOException e) {
                        e.printStackTrace();
                      }
                    }
                    // TODO: Écrire la ligne dans le fichier
                    // ...
                    // Incrémentation des compteurs i et j
                    i++;
                    j++;
                    // Réinitialisation du StringBuilder pour la prochaine itération
                    sb.setLength(0);
                  }
                }
              } catch (IOException e) {
                e.printStackTrace();
              } finally {
                // Fermeture du BufferedReader dans le bloc finally
                try {
                  if (br != null) {
                    br.close();
                  }
                } catch (IOException e) {
                  e.printStackTrace();
                }
              }


              BufferedReader br3 = null;

              try {
                br3 = new BufferedReader(new FileReader(cheminFichier));
                String ligneOne = br3.readLine(); // Lire la première ligne
                // System.out.println(ligneOne);
                String[] valus = ligneOne.split(";"); // Séparer les valeurs par ';'

                // -------------------------- set axeprogramatique
                // ------------------------------

                Set<String> valeursUniquesAxeProgramatique = new HashSet<>();
                valeursUniquesAxeProgramatique.add("CODE_PORTEF");
                valeursUniquesAxeProgramatique.add("LIB_PORTEF");
                valeursUniquesAxeProgramatique.add("PROGRAMME");
                valeursUniquesAxeProgramatique.add("SOUS_PROGRAMME");
                valeursUniquesAxeProgramatique.add("LIBELLE");

                // ----------------------- set Axeeconomique
                // ------------------------------------

                Set<String> valeursUniquesAxeeconomique = new HashSet<>();
                valeursUniquesAxeeconomique.add("AXE_ECO");
                valeursUniquesAxeeconomique.add("LIBELLE");

                // ----------------------- set Action
                // -------------------------------------------

                Set<String> valeursUniquesAction = new HashSet<>();
                valeursUniquesAction.add("CODE_PORTEF");
                valeursUniquesAction.add("PROGRAMME");
                valeursUniquesAction.add("SOUS_PROGRAMME");
                valeursUniquesAction.add("ACTION");
                valeursUniquesAction.add("SOUS_ACTION");
                valeursUniquesAction.add("LIBELLE_ACTION");

                // -----------------------set Ordonnateur ------------------------------------
                Set<String> valeursUniquesOrdonnateur = new HashSet<>();
                valeursUniquesOrdonnateur.add("CODE_ORD");
                valeursUniquesOrdonnateur.add("LIBELLE_ORD");
                valeursUniquesOrdonnateur.add("Code_Wilaya");

                // -----------------------set Credit ------------------------------------
                Set<String> valeursUniquesCredit = new HashSet<>();
                valeursUniquesCredit.add("GESTION");
                valeursUniquesCredit.add("MOIS");
                valeursUniquesCredit.add("ORD");
                valeursUniquesCredit.add("PORTEF");
                valeursUniquesCredit.add("PROG");
                valeursUniquesCredit.add("S_PROG");
                valeursUniquesCredit.add("ACTION");
                valeursUniquesCredit.add("S_ACTION");
                valeursUniquesCredit.add("AXE_ECO");
                valeursUniquesCredit.add("NVL(DISPOS,'0')");
                valeursUniquesCredit.add("SUM(TOT_CREDIT)");
                valeursUniquesCredit.add("SUM(TOT_DEB)");
                valeursUniquesCredit.add("SUM(SOLDE)");

                // -----------------------set Mandat ------------------------------------
                Set<String> valeursUniquesMandat = new HashSet<>();
                valeursUniquesMandat.add("CODE_PORTEF");
                valeursUniquesMandat.add("ORD");
                valeursUniquesMandat.add("GESTION");
                valeursUniquesMandat.add("CODE_MANDAT");
                valeursUniquesMandat.add("DT_EMISSION");
                valeursUniquesMandat.add("STATUT");
                valeursUniquesMandat.add("PROG");
                valeursUniquesMandat.add("S_PROG");
                valeursUniquesMandat.add("ACTION");
                valeursUniquesMandat.add("S_ACTION");
                valeursUniquesMandat.add("AXE_ECO");
                valeursUniquesMandat.add("DISPOS");
                valeursUniquesMandat.add("MONTANT");

                // -----------------------set Wilayas ------------------------------------
                Set<String> valeursUniquesWilayas = new HashSet<>();
                valeursUniquesWilayas.add("Code_Wilaya");
                valeursUniquesWilayas.add("Libelle_wilaya");

                // -----------------------set Recette ------------------------------------
                Set<String> valeursUniquesRecette = new HashSet<>();
                valeursUniquesRecette.add("GESTION");
                valeursUniquesRecette.add("MOIS");
                valeursUniquesRecette.add("CODE_CPT");
                valeursUniquesRecette.add("LIB_CPT_G");
                valeursUniquesRecette.add("POSTE_C");
                valeursUniquesRecette.add("MT_MOIS");



                // -----------------------set Soumissionaire ------------------------------------
                Set<String> valeursUniquesSoumissionaire = new HashSet<>();
                valeursUniquesSoumissionaire.add("CODE_Soumissionaire");
                valeursUniquesSoumissionaire.add("Type_Soumissionaire");
                valeursUniquesSoumissionaire.add("libelle_Soumissionaire");


                // -----------------------set Dette ------------------------------------
                Set<String> valeursUniquesDette = new HashSet<>();
                valeursUniquesDette.add("code_ISIN");
                valeursUniquesDette.add("DATE_DEB");
                valeursUniquesDette.add("DUREE_bon");
                valeursUniquesDette.add("DATE_ECHEANCE");
                valeursUniquesDette.add("Code_Soumissionaire");
                valeursUniquesDette.add("MONTANT_Propose_Par_etat");
                valeursUniquesDette.add("Montant_Propose_Par_Soumissionaire");
                valeursUniquesDette.add("Montant_Adjuge");
                valeursUniquesDette.add("Montant_Coupoun");



                // Vérifier si chaque valeur est présente et unique dans le tableau
                // axeprogramatique
                boolean contientToutAxeProgramatique = contains(
                    valeursUniquesAxeProgramatique,
                    valus);

                // Vérifier si chaque valeur est présente et unique dans le tableau
                // axeeconomique
                boolean contientToutAxeeconomique = contains(
                    valeursUniquesAxeeconomique,
                    valus);

                // Vérifier si chaque valeur est présente et unique dans le tableau action
                boolean contientToutAction = contains(
                    valeursUniquesAction,
                    valus);

                // Vérifier si chaque valeur est présente et unique dans le tableau ordonnateur
                boolean contientToutOrdonnateur = contains(
                    valeursUniquesOrdonnateur,
                    valus);

                // Vérifier si chaque valeur est présente et unique dans le tableau credit
                boolean contientToutCredit = contains(
                    valeursUniquesCredit,
                    valus);

                // Vérifier si chaque valeur est présente et unique dans le tableau mandat
                boolean contientToutMandat = contains(
                    valeursUniquesMandat,
                    valus);

                // Vérifier si chaque valeur est présente et unique dans le tableau Wilayas
                boolean contientToutWilayas = contains(
                    valeursUniquesWilayas,
                    valus);


                // Vérifier si chaque valeur est présente et unique dans le tableau Recette
                boolean contientToutRecette= contains(
                    valeursUniquesRecette,
                    valus);

                
                // Vérifier si chaque valeur est présente et unique dans le tableau Soumissionaire
                boolean contientToutSoumissionaire = contains(
                  valeursUniquesSoumissionaire,
                  valus);

                // Vérifier si chaque valeur est présente et unique dans le tableau Dette
                boolean contientToutDette = contains(
                  valeursUniquesDette,
                  valus);

                  
                if (contientToutAxeProgramatique) {
                  System.out.println(
                      "Le tableau contient toutes les valeurs uniques de l'axe programatique.");
                  String ligneDonnee;

                  while ((ligneDonnee = br3.readLine()) != null) {
                    String[] valeurs = ligneDonnee.split(";");

                    // Afficher les valeurs de la deuxième ligne
                    /*
                     * for (String valeur : valeurs) {
                     * System.out.print(valeur + " ");
                     * }
                     */
                    System.out.println(); // Nouvelle ligne après chaque ligne du fichier
                    String x = valus[0];
                    String y = valus[1];
                    String z = valus[2];
                    String a = valus[3];
                    String b = valus[4];
                    String codePortef = "";
                    String libPortef = "";
                    String programme = "";
                    String libelle = "";
                    String sousProgramme = "";

                    // test pour le 1er élément de tableau
                    if (x.equals("CODE_PORTEF")) {
                      codePortef = valeurs[0]; // Premier élément
                    } else if (x.equals("LIB_PORTEF")) {
                      libPortef = valeurs[0]; // Deuxième élément
                    } else if (x.equals("PROGRAMME")) {
                      programme = valeurs[0]; // Troisième élément
                    } else if (x.equals("SOUS_PROGRAMME")) {
                      sousProgramme = valeurs[0]; // Quatrième élément
                    } else if (x.equals("LIBELLE")) {
                      libelle = valeurs[0]; // Cinquième élément
                    }

                    // test pour le 2er élément de tableau
                    if (y.equals("CODE_PORTEF")) {
                      codePortef = valeurs[1]; // Premier élément
                    } else if (y.equals("LIB_PORTEF")) {
                      libPortef = valeurs[1]; // Deuxième élément
                    } else if (y.equals("PROGRAMME")) {
                      programme = valeurs[1]; // Troisième élément
                    } else if (y.equals("SOUS_PROGRAMME")) {
                      sousProgramme = valeurs[1]; // Quatrième élément
                    } else if (y.equals("LIBELLE")) {
                      libelle = valeurs[1]; // Cinquième élément
                    }

                    // test pour le 3er élément de tableau
                    if (z.equals("CODE_PORTEF")) {
                      codePortef = valeurs[2]; // Premier élément
                    } else if (z.equals("LIB_PORTEF")) {
                      libPortef = valeurs[2]; // Deuxième élément
                    } else if (z.equals("PROGRAMME")) {
                      programme = valeurs[2]; // Troisième élément
                    } else if (z.equals("SOUS_PROGRAMME")) {
                      sousProgramme = valeurs[2]; // Quatrième élément
                    } else if (z.equals("LIBELLE")) {
                      libelle = valeurs[2]; // Cinquième élément
                    }

                    // test pour le 4er élément de tableau
                    if (a.equals("CODE_PORTEF")) {
                      codePortef = valeurs[3]; // Premier élément
                    } else if (a.equals("LIB_PORTEF")) {
                      libPortef = valeurs[3]; // Deuxième élément
                    } else if (a.equals("PROGRAMME")) {
                      programme = valeurs[3]; // Troisième élément
                    } else if (a.equals("SOUS_PROGRAMME")) {
                      sousProgramme = valeurs[3]; // Quatrième élément
                    } else if (a.equals("LIBELLE")) {
                      libelle = valeurs[3]; // Cinquième élément
                    }

                    // test pour le 5er élément de tableau
                    if (b.equals("CODE_PORTEF")) {
                      codePortef = valeurs[4]; // Premier élément
                    } else if (b.equals("LIB_PORTEF")) {
                      libPortef = valeurs[4]; // Deuxième élément
                    } else if (b.equals("PROGRAMME")) {
                      programme = valeurs[4]; // Troisième élément
                    } else if (b.equals("SOUS_PROGRAMME")) {
                      sousProgramme = valeurs[4]; // Quatrième élément
                    } else if (b.equals("LIBELLE")) {
                      libelle = valeurs[4]; // Cinquième élément
                    }

                    System.out.println(valus[0] + ":" + valeurs[0]);
                    System.out.println(valus[1] + ":" + valeurs[1]);
                    System.out.println(valus[2] + ":" + valeurs[2]);
                    System.out.println(valus[3] + ":" + valeurs[3]);
                    System.out.println(valus[4] + ":" + valeurs[4]);

                    // Appel de la fonction d'insertion avec les valeurs récupérées
                    String insertAxeProgr = relationnel.insertIntoAxeProgramatique(
                        codePortef,
                        libPortef,
                        programme,
                        sousProgramme,
                        libelle);
                    System.out.println(insertAxeProgr);
                  }

                  br3.close(); // Fermer le lecteur de fichier existant

                  // supression de contenu de ficher aprés traiatment
                  try {
                    FileWriter writer1 = new FileWriter(cheminFichier);
                    writer1.write(""); // Écriture d'une chaîne vide pour vider le fichier
                    writer1.close();
                    System.out.println(
                        "Contenu du fichier supprimé avec succès aprés insertion des éléments.");
                  } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println(
                        "Une erreur s'est produite lors de la suppression du contenu du fichier.");
                  }
                } else if (contientToutAxeeconomique) {
                  System.out.println(
                      "Le tableau contient toutes les valeurs uniques de l'axe économique.");
                  String ligneDonnee;

                  while ((ligneDonnee = br3.readLine()) != null) {
                    String[] valeurs = ligneDonnee.split(";");

                    // Afficher les valeurs de la deuxième ligne
                    /*
                     * for (String valeur : valeurs) {
                     * System.out.print(valeur + " ");
                     * }
                     */
                    System.out.println(); // Nouvelle ligne après chaque ligne du fichier
                    String x = valus[0];
                    String y = valus[1];
                    String AxeEco = "";
                    String libelle = "";

                    // test pour le 1er élément de tableau
                    if (x.equals("AXE_ECO")) {
                      AxeEco = valeurs[0]; // Premier élément
                    } else if (x.equals("LIBELLE")) {
                      libelle = valeurs[0]; // Deuxième élément
                    }

                    // test pour le 1er élément de tableau
                    if (y.equals("AXE_ECO")) {
                      AxeEco = valeurs[1]; // Premier élément
                    } else if (y.equals("LIBELLE")) {
                      libelle = valeurs[1]; // Deuxième élément
                    }

                    System.out.println(valus[0] + ":" + valeurs[0]);
                    System.out.println(valus[1] + ":" + valeurs[1]);

                    // Appel de la fonction d'insertion avec les valeurs récupérées
                    String insertAxeeco = relationnel.insertIntoAxeEconomique(
                        AxeEco,
                        libelle);
                    System.out.println(insertAxeeco);
                  }

                  br3.close(); // Fermer le lecteur de fichier existant

                  // supression de contenu de ficher aprés traiatment
                  try {
                    FileWriter writer1 = new FileWriter(cheminFichier);
                    writer1.write(""); // Écriture d'une chaîne vide pour vider le fichier
                    writer1.close();
                    System.out.println(
                        "Contenu du fichier supprimé avec succès aprés insertion des éléments.");
                  } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println(
                        "Une erreur s'est produite lors de la suppression du contenu du fichier.");
                  }
                } else if (contientToutAction) {
                  System.out.println(
                      "Le tableau contient toutes les valeurs uniques d'action.");
                  String ligneDonnee;

                  while ((ligneDonnee = br3.readLine()) != null) {
                    String[] valeurs = ligneDonnee.split(";");

                    // Afficher les valeurs de la deuxième ligne
                    /*
                     * for (String valeur : valeurs) {
                     * System.out.print(valeur + " ");
                     * }
                     */
                    System.out.println(); // Nouvelle ligne après chaque ligne du fichier
                    String x = valus[0];
                    String y = valus[1];
                    String z = valus[2];
                    String a = valus[3];
                    String b = valus[4];
                    String c = valus[5];
                    String codePortef = "";
                    String programme = "";
                    String sousprogramme = "";
                    String Action = "";
                    String sousAction = "";
                    String libelleAction = "";

                    // test pour le 1er élément de tableau
                    if (x.equals("CODE_PORTEF")) {
                      codePortef = valeurs[0]; // Premier élément
                    } else if (x.equals("PROGRAMME")) {
                      programme = valeurs[0]; // Deuxième élément
                    } else if (x.equals("SOUS_PROGRAMME")) {
                      sousprogramme = valeurs[0]; // Troisième élément
                    } else if (x.equals("ACTION")) {
                      Action = valeurs[0]; // Quatrième élément
                    } else if (x.equals("SOUS_ACTION")) {
                      sousAction = valeurs[0]; // Cinquième élément
                    } else if (x.equals("LIBELLE_ACTION")) {
                      libelleAction = valeurs[0]; // 6 éme élément
                    }

                    // test pour le 2er élément de tableau
                    if (y.equals("CODE_PORTEF")) {
                      codePortef = valeurs[1]; // Premier élément
                    } else if (y.equals("PROGRAMME")) {
                      programme = valeurs[1]; // Deuxième élément
                    } else if (y.equals("SOUS_PROGRAMME")) {
                      sousprogramme = valeurs[1]; // Troisième élément
                    } else if (y.equals("ACTION")) {
                      Action = valeurs[1]; // Quatrième élément
                    } else if (y.equals("SOUS_ACTION")) {
                      sousAction = valeurs[1]; // Cinquième élément
                    } else if (y.equals("LIBELLE_ACTION")) {
                      libelleAction = valeurs[1]; // 6 éme élément
                    }

                    // test pour le 3er élément de tableau
                    if (z.equals("CODE_PORTEF")) {
                      codePortef = valeurs[2]; // Premier élément
                    } else if (z.equals("PROGRAMME")) {
                      programme = valeurs[2]; // Deuxième élément
                    } else if (z.equals("SOUS_PROGRAMME")) {
                      sousprogramme = valeurs[2]; // Troisième élément
                    } else if (z.equals("ACTION")) {
                      Action = valeurs[2]; // Quatrième élément
                    } else if (z.equals("SOUS_ACTION")) {
                      sousAction = valeurs[2]; // Cinquième élément
                    } else if (z.equals("LIBELLE_ACTION")) {
                      libelleAction = valeurs[2]; // 6 éme élément
                    }

                    // test pour le 4er élément de tablea3
                    if (a.equals("CODE_PORTEF")) {
                      codePortef = valeurs[3]; // Premier élément
                    } else if (a.equals("PROGRAMME")) {
                      programme = valeurs[3]; // Deuxième élément
                    } else if (a.equals("SOUS_PROGRAMME")) {
                      sousprogramme = valeurs[3]; // Troisième élément
                    } else if (a.equals("ACTION")) {
                      Action = valeurs[3]; // Quatrième élément
                    } else if (a.equals("SOUS_ACTION")) {
                      sousAction = valeurs[3]; // Cinquième élément
                    } else if (a.equals("LIBELLE_ACTION")) {
                      libelleAction = valeurs[3]; // 6 éme élément
                    }

                    // test pour le 5er élément de tableau
                    if (b.equals("CODE_PORTEF")) {
                      codePortef = valeurs[4]; // Pre4ier élément
                    } else if (b.equals("PROGRAMME")) {
                      programme = valeurs[4]; // Deuxième élément
                    } else if (b.equals("SOUS_PROGRAMME")) {
                      sousprogramme = valeurs[4]; // Troisième élément
                    } else if (b.equals("ACTION")) {
                      Action = valeurs[4]; // Quatrième élément
                    } else if (b.equals("SOUS_ACTION")) {
                      sousAction = valeurs[4]; // Cinquième élément
                    } else if (b.equals("LIBELLE_ACTION")) {
                      libelleAction = valeurs[4]; // 6 éme élément
                    }

                    // test pour le 5er élément de tab5eau
                    if (c.equals("CODE_PORTEF")) {
                      codePortef = valeurs[5]; // Pre4ier élément
                    } else if (c.equals("PROGRAMME")) {
                      programme = valeurs[5]; // Deuxième élément
                    } else if (c.equals("SOUS_PROGRAMME")) {
                      sousprogramme = valeurs[5]; // Troisième élément
                    } else if (c.equals("ACTION")) {
                      Action = valeurs[5]; // Quatrième élément
                    } else if (c.equals("SOUS_ACTION")) {
                      sousAction = valeurs[5]; // Cinquième élément
                    } else if (c.equals("LIBELLE_ACTION")) {
                      libelleAction = valeurs[5]; // 6 éme élément
                    }

                    System.out.println(valus[0] + ":" + valeurs[0]);
                    System.out.println(valus[1] + ":" + valeurs[1]);
                    System.out.println(valus[2] + ":" + valeurs[2]);
                    System.out.println(valus[3] + ":" + valeurs[3]);
                    System.out.println(valus[4] + ":" + valeurs[4]);
                    System.out.println(valus[5] + ":" + valeurs[5]);

                    // Appel de la fonction d'insertion avec les valeurs récupérées
                    String insertAction = relationnel.insertIntoAction(
                        codePortef,
                        programme,
                        sousprogramme,
                        Action,
                        sousAction,
                        libelleAction);
                    System.out.println(insertAction);
                  }

                  br3.close(); // Fermer le lecteur de fichier existant

                  // supression de contenu de ficher aprés traiatment
                  try {
                    FileWriter writer1 = new FileWriter(cheminFichier);
                    writer1.write(""); // Écriture d'une chaîne vide pour vider le fichier
                    writer1.close();
                    System.out.println(
                        "Contenu du fichier supprimé avec succès aprés insertion des éléments.");
                  } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println(
                        "Une erreur s'est produite lors de la suppression du contenu du fichier.");
                  }
                } else if (contientToutOrdonnateur) {
                  System.out.println(
                      "Le tableau contient toutes les valeurs uniques de l'orodonnateur.");
                  String ligneDonnee;

                  while ((ligneDonnee = br3.readLine()) != null) {
                    String[] valeurs = ligneDonnee.split(";");

                    // Afficher les valeurs de la deuxième ligne
                    /*
                     * for (String valeur : valeurs) {
                     * System.out.print(valeur + " ");
                     * }
                     */
                    System.out.println(); // Nouvelle ligne après chaque ligne du fichier
                    String x = valus[0];
                    String y = valus[1];
                    String z = valus[2];
                    String CodeOrd = "";
                    String libelleOrd = "";
                    String CodeWilaya = "";

                    // test pour le 1er élément de tableau
                    if (x.equals("CODE_ORD")) {
                      CodeOrd = valeurs[0]; // Premier élément
                    } else if (x.equals("LIBELLE_ORD")) {
                      libelleOrd = valeurs[0]; // Deuxième élément
                    } else if (x.equals("Code_Wilaya")) {
                      CodeWilaya = valeurs[0]; // Deuxième élément
                    }

                    // test pour le 1er élément de tableau
                    if (y.equals("CODE_ORD")) {
                      CodeOrd = valeurs[1]; // Premier élément
                    } else if (y.equals("LIBELLE_ORD")) {
                      libelleOrd = valeurs[1]; // Deuxième élément
                    } else if (y.equals("Code_Wilaya")) {
                      CodeWilaya = valeurs[1]; // Deuxième élément
                    }

                    // test pour le 1er élément de tableau
                    if (z.equals("CODE_ORD")) {
                      CodeOrd = valeurs[2]; // Premier élément
                    } else if (z.equals("LIBELLE_ORD")) {
                      libelleOrd = valeurs[2]; // Deuxième élément
                    } else if (z.equals("Code_Wilaya")) {
                      CodeWilaya = valeurs[2]; // Deuxième élément
                    }

                    System.out.println(valus[0] + ":" + valeurs[0]);
                    System.out.println(valus[1] + ":" + valeurs[1]);
                    System.out.println(valus[2] + ":" + valeurs[2]);

                    // Appel de la fonction d'insertion avec les valeurs récupérées
                    String insertAxeeco = relationnel.insertIntoOrdonnateur(
                        CodeOrd,
                        libelleOrd,
                        CodeWilaya);
                    System.out.println(insertAxeeco);
                  }

                  br3.close(); // Fermer le lecteur de fichier existant

                  // supression de contenu de ficher aprés traiatment
                  try {
                    FileWriter writer1 = new FileWriter(cheminFichier);
                    writer1.write(""); // Écriture d'une chaîne vide pour vider le fichier
                    writer1.close();
                    System.out.println(
                        "Contenu du fichier supprimé avec succès aprés insertion des éléments.");
                  } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println(
                        "Une erreur s'est produite lors de la suppression du contenu du fichier.");
                  }
                } else if (contientToutCredit) {
                  System.out.println(
                      "Le tableau contient toutes les valeurs uniques de crédit.");
                  String ligneDonnee;

                  while ((ligneDonnee = br3.readLine()) != null) {
                    String[] valeurs = ligneDonnee.split(";");

                    // Afficher les valeurs de la deuxième ligne
                    /*
                     * for (String valeur : valeurs) {
                     * System.out.print(valeur + " ");
                     * }
                     */
                    System.out.println(); // Nouvelle ligne après chaque ligne du fichier
                    String x = valus[0];
                    String y = valus[1];
                    String z = valus[2];
                    String a = valus[3];
                    String b = valus[4];
                    String c = valus[5];
                    String d = valus[6];
                    String e = valus[7];
                    String f = valus[8];
                    String g = valus[9];
                    String h = valus[10];
                    String l = valus[11];
                    String k = valus[12];
                    String gestion = "";
                    String mois = "";
                    String codeOrdonnateur = "";
                    String CodeProtef = "";
                    String programme = "";
                    String sousprogramme = "";
                    String Action = "";
                    String sousAction = "";
                    String AxeEconomique = "";
                    String Dispos = "";
                    String TotCredit = "";
                    String TotDeb = "";
                    String Solde = "";

                    // test pour le 1er élément de tableau
                    if (x.equals("GESTION")) {
                      gestion = valeurs[0]; // Premier élément
                    } else if (x.equals("MOIS")) {
                      mois = valeurs[0]; // Deuxième élément
                    } else if (x.equals("ORD")) {
                      codeOrdonnateur = valeurs[0]; // Troisième élément
                    } else if (x.equals("PORTEF")) {
                      CodeProtef = valeurs[0]; // Quatrième élément
                    } else if (x.equals("PROG")) {
                      programme = valeurs[0]; // Cinquième élément
                    } else if (x.equals("S_PROG")) {
                      sousprogramme = valeurs[0]; // 6 éme élément
                    } else if (x.equals("ACTION")) {
                      Action = valeurs[0]; // 7 éme élément
                    } else if (x.equals("S_ACTION")) {
                      sousAction = valeurs[0]; // 8 éme élément
                    } else if (x.equals("AXE_ECO")) {
                      AxeEconomique = valeurs[0]; // 9 éme élément
                    } else if (x.equals("NVL(DISPOS,'0')")) {
                      Dispos = valeurs[0]; // 10 éme élément
                    } else if (x.equals("SUM(TOT_CREDIT)")) {
                      TotCredit = valeurs[0]; // 11 éme élément
                    } else if (x.equals("SUM(TOT_DEB)")) {
                      TotDeb = valeurs[0]; // 12 éme élément
                    } else if (x.equals("SUM(SOLDE)")) {
                      Solde = valeurs[0]; // 13 éme élément
                    }

                    // test pour le 2er élément de tableau
                    if (y.equals("GESTION")) {
                      gestion = valeurs[1]; // Premier élément
                    } else if (y.equals("MOIS")) {
                      mois = valeurs[1]; // Deuxième élément
                    } else if (y.equals("ORD")) {
                      codeOrdonnateur = valeurs[1]; // Troisième élément
                    } else if (y.equals("PORTEF")) {
                      CodeProtef = valeurs[1]; // Quatrième élément
                    } else if (y.equals("PROG")) {
                      programme = valeurs[1]; // Cinquième élément
                    } else if (y.equals("S_PROG")) {
                      sousprogramme = valeurs[1]; // 6 éme élément
                    } else if (y.equals("ACTION")) {
                      Action = valeurs[1]; // 7 éme élément
                    } else if (y.equals("S_ACTION")) {
                      sousAction = valeurs[1]; // 8 éme élément
                    } else if (y.equals("AXE_ECO")) {
                      AxeEconomique = valeurs[1]; // 9 éme élément
                    } else if (y.equals("NVL(DISPOS,'0')")) {
                      Dispos = valeurs[1]; // 10 éme élément
                    } else if (y.equals("SUM(TOT_CREDIT)")) {
                      TotCredit = valeurs[1]; // 11 éme élément
                    } else if (y.equals("SUM(TOT_DEB)")) {
                      TotDeb = valeurs[1]; // 12 éme élément
                    } else if (y.equals("SUM(SOLDE)")) {
                      Solde = valeurs[1]; // 13 éme élément
                    }

                    // test pour le 3er élément de tableau
                    if (z.equals("GESTION")) {
                      gestion = valeurs[2]; // Premier élément
                    } else if (z.equals("MOIS")) {
                      mois = valeurs[2]; // Deuxième élément
                    } else if (z.equals("ORD")) {
                      codeOrdonnateur = valeurs[2]; // Troisième élément
                    } else if (z.equals("PORTEF")) {
                      CodeProtef = valeurs[2]; // Quatrième élément
                    } else if (z.equals("PROG")) {
                      programme = valeurs[2]; // Cinquième élément
                    } else if (z.equals("S_PROG")) {
                      sousprogramme = valeurs[2]; // 6 éme élément
                    } else if (z.equals("ACTION")) {
                      Action = valeurs[2]; // 7 éme élément
                    } else if (z.equals("S_ACTION")) {
                      sousAction = valeurs[2]; // 8 éme élément
                    } else if (z.equals("AXE_ECO")) {
                      AxeEconomique = valeurs[2]; // 9 éme élément
                    } else if (z.equals("NVL(DISPOS,'0')")) {
                      Dispos = valeurs[2]; // 10 éme élément
                    } else if (z.equals("SUM(TOT_CREDIT)")) {
                      TotCredit = valeurs[2]; // 11 éme élément
                    } else if (z.equals("SUM(TOT_DEB)")) {
                      TotDeb = valeurs[2]; // 12 éme élément
                    } else if (z.equals("SUM(SOLDE)")) {
                      Solde = valeurs[2]; // 13 éme élément
                    }

                    // test pour le 4er élément de tablea3
                    if (a.equals("GESTION")) {
                      gestion = valeurs[3]; // Premier élément
                    } else if (a.equals("MOIS")) {
                      mois = valeurs[3]; // Deuxième élément
                    } else if (a.equals("ORD")) {
                      codeOrdonnateur = valeurs[3]; // Troisième élément
                    } else if (a.equals("PORTEF")) {
                      CodeProtef = valeurs[3]; // Quatrième élément
                    } else if (a.equals("PROG")) {
                      programme = valeurs[3]; // Cinquième élément
                    } else if (a.equals("S_PROG")) {
                      sousprogramme = valeurs[3]; // 6 éme élément
                    } else if (a.equals("ACTION")) {
                      Action = valeurs[3]; // 7 éme élément
                    } else if (a.equals("S_ACTION")) {
                      sousAction = valeurs[3]; // 8 éme élément
                    } else if (a.equals("AXE_ECO")) {
                      AxeEconomique = valeurs[3]; // 9 éme élément
                    } else if (a.equals("NVL(DISPOS,'0')")) {
                      Dispos = valeurs[3]; // 10 éme élément
                    } else if (a.equals("SUM(TOT_CREDIT)")) {
                      TotCredit = valeurs[3]; // 11 éme élément
                    } else if (a.equals("SUM(TOT_DEB)")) {
                      TotDeb = valeurs[3]; // 12 éme élément
                    } else if (a.equals("SUM(SOLDE)")) {
                      Solde = valeurs[3]; // 13 éme élément
                    }

                    // test pour le 5er élément de tableau
                    if (b.equals("GESTION")) {
                      gestion = valeurs[4]; // Premier élément
                    } else if (b.equals("MOIS")) {
                      mois = valeurs[4]; // Deuxième élément
                    } else if (b.equals("ORD")) {
                      codeOrdonnateur = valeurs[4]; // Troisième élément
                    } else if (b.equals("PORTEF")) {
                      CodeProtef = valeurs[4]; // Quatrième élément
                    } else if (b.equals("PROG")) {
                      programme = valeurs[4]; // Cinquième élément
                    } else if (b.equals("S_PROG")) {
                      sousprogramme = valeurs[4]; // 6 éme élément
                    } else if (b.equals("ACTION")) {
                      Action = valeurs[4]; // 7 éme élément
                    } else if (b.equals("S_ACTION")) {
                      sousAction = valeurs[4]; // 8 éme élément
                    } else if (b.equals("AXE_ECO")) {
                      AxeEconomique = valeurs[4]; // 9 éme élément
                    } else if (b.equals("NVL(DISPOS,'0')")) {
                      Dispos = valeurs[4]; // 10 éme élément
                    } else if (b.equals("SUM(TOT_CREDIT)")) {
                      TotCredit = valeurs[4]; // 11 éme élément
                    } else if (b.equals("SUM(TOT_DEB)")) {
                      TotDeb = valeurs[4]; // 12 éme élément
                    } else if (b.equals("SUM(SOLDE)")) {
                      Solde = valeurs[4]; // 13 éme élément
                    }

                    // test pour le 6er élément de tab5eau
                    if (c.equals("GESTION")) {
                      gestion = valeurs[5]; // Premier élément
                    } else if (c.equals("MOIS")) {
                      mois = valeurs[5]; // Deuxi5me élément
                    } else if (c.equals("ORD")) {
                      codeOrdonnateur = valeurs[5]; // Troisième élément
                    } else if (c.equals("PORTEF")) {
                      CodeProtef = valeurs[5]; // Quatrième élément
                    } else if (c.equals("PROG")) {
                      programme = valeurs[5]; // Cinquième élément
                    } else if (c.equals("S_PROG")) {
                      sousprogramme = valeurs[5]; // 6 éme élément
                    } else if (c.equals("ACTION")) {
                      Action = valeurs[5]; // 7 éme élément
                    } else if (c.equals("S_ACTION")) {
                      sousAction = valeurs[5]; // 8 éme élément
                    } else if (c.equals("AXE_ECO")) {
                      AxeEconomique = valeurs[5]; // 9 éme élément
                    } else if (c.equals("NVL(DISPOS,'0')")) {
                      Dispos = valeurs[5]; // 10 éme élément
                    } else if (c.equals("SUM(TOT_CREDIT)")) {
                      TotCredit = valeurs[5]; // 11 éme élément
                    } else if (c.equals("SUM(TOT_DEB)")) {
                      TotDeb = valeurs[5]; // 12 éme élément
                    } else if (c.equals("SUM(SOLDE)")) {
                      Solde = valeurs[5]; // 13 éme élément
                    }

                    // test pour le 7er élément de tab5eau
                    if (d.equals("GESTION")) {
                      gestion = valeurs[6]; // Premi6r élément
                    } else if (d.equals("MOIS")) {
                      mois = valeurs[6]; // Deuxi5me élément
                    } else if (d.equals("ORD")) {
                      codeOrdonnateur = valeurs[6]; // Troisième élément
                    } else if (d.equals("PORTEF")) {
                      CodeProtef = valeurs[6]; // Quatrième élément
                    } else if (d.equals("PROG")) {
                      programme = valeurs[6]; // Cinquième élément
                    } else if (d.equals("S_PROG")) {
                      sousprogramme = valeurs[6]; // 6 éme élément
                    } else if (d.equals("ACTION")) {
                      Action = valeurs[6]; // 7 éme élément
                    } else if (d.equals("S_ACTION")) {
                      sousAction = valeurs[6]; // 8 éme élément
                    } else if (d.equals("AXE_ECO")) {
                      AxeEconomique = valeurs[6]; // 9 éme élément
                    } else if (d.equals("NVL(DISPOS,'0')")) {
                      Dispos = valeurs[6]; // 10 éme élément
                    } else if (d.equals("SUM(TOT_CREDIT)")) {
                      TotCredit = valeurs[6]; // 11 éme élément
                    } else if (d.equals("SUM(TOT_DEB)")) {
                      TotDeb = valeurs[6]; // 12 éme élément
                    } else if (d.equals("SUM(SOLDE)")) {
                      Solde = valeurs[6]; // 13 éme élément
                    }

                    // test pour le 8er élément de tab5ea7
                    if (e.equals("GESTION")) {
                      gestion = valeurs[7]; // Premier élément
                    } else if (e.equals("MOIS")) {
                      mois = valeurs[7]; // Deuxi5me élément
                    } else if (e.equals("ORD")) {
                      codeOrdonnateur = valeurs[7]; // Troisième élément
                    } else if (e.equals("PORTEF")) {
                      CodeProtef = valeurs[7]; // Quatrième élément
                    } else if (e.equals("PROG")) {
                      programme = valeurs[7]; // Cinquième élément
                    } else if (e.equals("S_PROG")) {
                      sousprogramme = valeurs[7]; // 6 éme élément
                    } else if (e.equals("ACTION")) {
                      Action = valeurs[7]; // 7 éme élément
                    } else if (e.equals("S_ACTION")) {
                      sousAction = valeurs[7]; // 8 éme élément
                    } else if (e.equals("AXE_ECO")) {
                      AxeEconomique = valeurs[7]; // 9 éme élément
                    } else if (e.equals("NVL(DISPOS,'0')")) {
                      Dispos = valeurs[7]; // 10 éme élément
                    } else if (e.equals("SUM(TOT_CREDIT)")) {
                      TotCredit = valeurs[7]; // 11 éme élément
                    } else if (e.equals("SUM(TOT_DEB)")) {
                      TotDeb = valeurs[7]; // 12 éme élément
                    } else if (e.equals("SUM(SOLDE)")) {
                      Solde = valeurs[7]; // 13 éme élément
                    }

                    // test pour le 9er élément de tab5eau
                    if (f.equals("GESTION")) {
                      gestion = valeurs[8]; // Premier élément
                    } else if (f.equals("MOIS")) {
                      mois = valeurs[8]; // Deuxi5me élément
                    } else if (f.equals("ORD")) {
                      codeOrdonnateur = valeurs[8]; // Troisième élément
                    } else if (f.equals("PORTEF")) {
                      CodeProtef = valeurs[8]; // Quatrième élément
                    } else if (f.equals("PROG")) {
                      programme = valeurs[8]; // Cinquième élément
                    } else if (f.equals("S_PROG")) {
                      sousprogramme = valeurs[8]; // 6 éme élément
                    } else if (f.equals("ACTION")) {
                      Action = valeurs[8]; // 7 éme élément
                    } else if (f.equals("S_ACTION")) {
                      sousAction = valeurs[8]; // 8 éme élément
                    } else if (f.equals("AXE_ECO")) {
                      AxeEconomique = valeurs[8]; // 9 éme élément
                    } else if (f.equals("NVL(DISPOS,'0')")) {
                      Dispos = valeurs[8]; // 10 éme élément
                    } else if (f.equals("SUM(TOT_CREDIT)")) {
                      TotCredit = valeurs[8]; // 11 éme élément
                    } else if (f.equals("SUM(TOT_DEB)")) {
                      TotDeb = valeurs[8]; // 12 éme élément
                    } else if (f.equals("SUM(SOLDE)")) {
                      Solde = valeurs[8]; // 13 éme élément
                    }

                    // test pour le 10er élément de tab5eau
                    if (g.equals("GESTION")) {
                      gestion = valeurs[9]; // Premier élément
                    } else if (g.equals("MOIS")) {
                      mois = valeurs[9]; // Deuxi5me élément
                    } else if (g.equals("ORD")) {
                      codeOrdonnateur = valeurs[9]; // Troisième élément
                    } else if (g.equals("PORTEF")) {
                      CodeProtef = valeurs[9]; // Quatrième élément
                    } else if (g.equals("PROG")) {
                      programme = valeurs[9]; // Cinquième élément
                    } else if (g.equals("S_PROG")) {
                      sousprogramme = valeurs[9]; // 6 éme élément
                    } else if (g.equals("ACTION")) {
                      Action = valeurs[9]; // 7 éme élément
                    } else if (g.equals("S_ACTION")) {
                      sousAction = valeurs[9]; // 8 éme élément
                    } else if (g.equals("AXE_ECO")) {
                      AxeEconomique = valeurs[9]; // 9 éme élément
                    } else if (g.equals("NVL(DISPOS,'0')")) {
                      Dispos = valeurs[9]; // 10 éme élément
                    } else if (g.equals("SUM(TOT_CREDIT)")) {
                      TotCredit = valeurs[9]; // 11 éme élément
                    } else if (g.equals("SUM(TOT_DEB)")) {
                      TotDeb = valeurs[9]; // 12 éme élément
                    } else if (g.equals("SUM(SOLDE)")) {
                      Solde = valeurs[9]; // 13 éme élément
                    }

                    // test pour lhe 6er élément de tab5eau
                    if (h.equals("GESTION")) {
                      gestion = valeurs[10]; // Premier 10lément
                    } else if (h.equals("MOIS")) {
                      mois = valeurs[10]; // Deuxi5me élément
                    } else if (h.equals("ORD")) {
                      codeOrdonnateur = valeurs[10]; // Troisième élément
                    } else if (h.equals("PORTEF")) {
                      CodeProtef = valeurs[10]; // Quatrième élément
                    } else if (h.equals("PROG")) {
                      programme = valeurs[10]; // Cinquième élément
                    } else if (h.equals("S_PROG")) {
                      sousprogramme = valeurs[10]; // 6 éme élément
                    } else if (h.equals("ACTION")) {
                      Action = valeurs[10]; // 7 éme élément
                    } else if (h.equals("S_ACTION")) {
                      sousAction = valeurs[10]; // 8 éme élément
                    } else if (h.equals("AXE_ECO")) {
                      AxeEconomique = valeurs[10]; // 9 éme élément
                    } else if (h.equals("NVL(DISPOS,'0')")) {
                      Dispos = valeurs[10]; // 10 éme élément
                    } else if (h.equals("SUM(TOT_CREDIT)")) {
                      TotCredit = valeurs[10]; // 11 éme élément
                    } else if (h.equals("SUM(TOT_DEB)")) {
                      TotDeb = valeurs[10]; // 12 éme élément
                    } else if (h.equals("SUM(SOLDE)")) {
                      Solde = valeurs[10]; // 13 éme élément
                    }

                    // test pour le 12er élément de tab5eau
                    if (l.equals("GESTION")) {
                      gestion = valeurs[11]; // Premier élément
                    } else if (l.equals("MOIS")) {
                      mois = valeurs[11]; // Deuxi5me élément
                    } else if (l.equals("ORD")) {
                      codeOrdonnateur = valeurs[11]; // Troisième élément
                    } else if (l.equals("PORTEF")) {
                      CodeProtef = valeurs[11]; // Quatrième élément
                    } else if (l.equals("PROG")) {
                      programme = valeurs[11]; // Cinquième élément
                    } else if (l.equals("S_PROG")) {
                      sousprogramme = valeurs[11]; // 6 éme élément
                    } else if (l.equals("ACTION")) {
                      Action = valeurs[11]; // 7 éme élément
                    } else if (l.equals("S_ACTION")) {
                      sousAction = valeurs[11]; // 8 éme élément
                    } else if (l.equals("AXE_ECO")) {
                      AxeEconomique = valeurs[11]; // 9 éme élément
                    } else if (l.equals("NVL(DISPOS,'0')")) {
                      Dispos = valeurs[11]; // 10 éme élément
                    } else if (l.equals("SUM(TOT_CREDIT)")) {
                      TotCredit = valeurs[11]; // 11 éme élément
                    } else if (l.equals("SUM(TOT_DEB)")) {
                      TotDeb = valeurs[11]; // 12 éme élément
                    } else if (l.equals("SUM(SOLDE)")) {
                      Solde = valeurs[11]; // 13 éme élément
                    }

                    // test pour le 13er élément de tab5eau
                    if (k.equals("GESTION")) {
                      gestion = valeurs[12]; // Premier élément
                    } else if (k.equals("MOIS")) {
                      mois = valeurs[12]; // Deuxi5me élément
                    } else if (k.equals("ORD")) {
                      codeOrdonnateur = valeurs[12]; // Troisième élément
                    } else if (k.equals("PORTEF")) {
                      CodeProtef = valeurs[12]; // Quatrième élément
                    } else if (k.equals("PROG")) {
                      programme = valeurs[12]; // Cinquième élément
                    } else if (k.equals("S_PROG")) {
                      sousprogramme = valeurs[12]; // 6 éme élément
                    } else if (k.equals("ACTION")) {
                      Action = valeurs[12]; // 7 éme élément
                    } else if (k.equals("S_ACTION")) {
                      sousAction = valeurs[12]; // 8 éme élément
                    } else if (k.equals("AXE_ECO")) {
                      AxeEconomique = valeurs[12]; // 9 éme élément
                    } else if (k.equals("NVL(DISPOS,'0')")) {
                      Dispos = valeurs[12]; // 10 éme élément
                    } else if (k.equals("SUM(TOT_CREDIT)")) {
                      TotCredit = valeurs[12]; // 11 éme élément
                    } else if (k.equals("SUM(TOT_DEB)")) {
                      TotDeb = valeurs[12]; // 12 éme élément
                    } else if (k.equals("SUM(SOLDE)")) {
                      Solde = valeurs[12]; // 13 éme élément
                    }

                    System.out.println(valus[0] + ":" + valeurs[0]);
                    System.out.println(valus[1] + ":" + valeurs[1]);
                    System.out.println(valus[2] + ":" + valeurs[2]);
                    System.out.println(valus[3] + ":" + valeurs[3]);
                    System.out.println(valus[4] + ":" + valeurs[4]);
                    System.out.println(valus[5] + ":" + valeurs[5]);
                    System.out.println(valus[6] + ":" + valeurs[6]);
                    System.out.println(valus[7] + ":" + valeurs[7]);
                    System.out.println(valus[8] + ":" + valeurs[8]);
                    System.out.println(valus[9] + ":" + valeurs[9]);
                    System.out.println(valus[10] + ":" + valeurs[10]);
                    System.out.println(valus[11] + ":" + valeurs[11]);
                    System.out.println(valus[12] + ":" + valeurs[12]);

                    // Appel de la fonction d'insertion avec les valeurs récupérées
                    String insertCredit = relationnel.insertIntoCredit(
                        gestion,
                        mois,
                        codeOrdonnateur,
                        CodeProtef,
                        programme,
                        sousprogramme,
                        Action,
                        sousAction,
                        AxeEconomique,
                        Dispos,
                        TotCredit,
                        TotDeb,
                        Solde);
                    System.out.println(insertCredit);
                  }

                  br3.close(); // Fermer le lecteur de fichier existant

                  // supression de contenu de ficher aprés traiatment
                  try {
                    FileWriter writer1 = new FileWriter(cheminFichier);
                    writer1.write(""); // Écriture d'une chaîne vide pour vider le fichier
                    writer1.close();
                    System.out.println(
                        "Contenu du fichier supprimé avec succès aprés insertion des éléments.");
                  } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println(
                        "Une erreur s'est produite lors de la suppression du contenu du fichier.");
                  }


                } else if (contientToutMandat) {
                  System.out.println(
                      "Le tableau contient toutes les valeurs uniques de Mandat.");
                  String ligneDonnee;

                  while ((ligneDonnee = br3.readLine()) != null) {
                    String[] valeurs = ligneDonnee.split(";");

                    // Afficher les valeurs de la deuxième ligne
                    /*
                     * for (String valeur : valeurs) {
                     * System.out.print(valeur + " ");
                     * }
                     */
                    System.out.println(); // Nouvelle ligne après chaque ligne du fichier
                    String x = valus[0];
                    String y = valus[1];
                    String z = valus[2];
                    String a = valus[3];
                    String b = valus[4];
                    String c = valus[5];
                    String d = valus[6];
                    String e = valus[7];
                    String f = valus[8];
                    String g = valus[9];
                    String h = valus[10];
                    String l = valus[11];
                    String k = valus[12];
                    String CodePortef = "";
                    String codeOrdonnateur = "";
                    String gestion = "";
                    String CodeMandat = "";
                    String DateEmission = "";
                    String Statut = "";
                    String Programme = "";
                    String sousProgramme = "";
                    String Action = "";
                    String SousAction = "";
                    String AxeEconomique = "";
                    String Dispos = "";
                    String Mantant = "";

                    // test pour le 1er élément de tableau
                    if (x.equals("CODE_PORTEF")) {
                      CodePortef = valeurs[0]; // Premier élément
                    } else if (x.equals("ORD")) {
                      codeOrdonnateur = valeurs[0]; // Deuxième élément
                    } else if (x.equals("GESTION")) {
                      gestion = valeurs[0]; // Troisième élément
                    } else if (x.equals("CODE_MANDAT")) {
                      CodeMandat = valeurs[0]; // Quatrième élément
                    } else if (x.equals("DT_EMISSION")) {
                      DateEmission = valeurs[0]; // Cinquième élément
                    } else if (x.equals("STATUT")) {
                      Statut = valeurs[0]; // 6 éme élément
                    } else if (x.equals("PROG")) {
                      Programme = valeurs[0]; // 7 éme élément
                    } else if (x.equals("S_PROG")) {
                      sousProgramme = valeurs[0]; // 8 éme élément
                    } else if (x.equals("ACTION")) {
                      Action = valeurs[0]; // 9 éme élément
                    } else if (x.equals("S_ACTION")) {
                      SousAction = valeurs[0]; // 10 éme élément
                    } else if (x.equals("AXE_ECO")) {
                      AxeEconomique = valeurs[0]; // 11 éme élément
                    } else if (x.equals("DISPOS")) {
                      Dispos = valeurs[0]; // 12 éme élément
                    } else if (x.equals("MONTANT")) {
                      Mantant = valeurs[0]; // 13 éme élément
                    }

                    // test pour le 2er élément de tableau
                    if (y.equals("CODE_PORTEF")) {
                      CodePortef = valeurs[1]; // Premier élément
                    } else if (y.equals("ORD")) {
                      codeOrdonnateur = valeurs[1]; // Deuxième élément
                    } else if (y.equals("GESTION")) {
                      gestion = valeurs[1]; // Troisième élément
                    } else if (y.equals("CODE_MANDAT")) {
                      CodeMandat = valeurs[1]; // Quatrième élément
                    } else if (y.equals("DT_EMISSION")) {
                      DateEmission = valeurs[1]; // Cinquième élément
                    } else if (y.equals("STATUT")) {
                      Statut = valeurs[1]; // 6 éme élément
                    } else if (y.equals("PROG")) {
                      Programme = valeurs[1]; // 7 éme élément
                    } else if (y.equals("S_PROG")) {
                      sousProgramme = valeurs[1]; // 8 éme élément
                    } else if (y.equals("ACTION")) {
                      Action = valeurs[1]; // 9 éme élément
                    } else if (y.equals("S_ACTION")) {
                      SousAction = valeurs[1]; // 10 éme élément
                    } else if (y.equals("AXE_ECO")) {
                      AxeEconomique = valeurs[1]; // 11 éme élément
                    } else if (y.equals("DISPOS")) {
                      Dispos = valeurs[1]; // 12 éme élément
                    } else if (y.equals("MONTANT")) {
                      Mantant = valeurs[1]; // 13 éme élément
                    }

                    // test pour le 3er élément de tablea2
                    if (z.equals("CODE_PORTEF")) {
                      CodePortef = valeurs[2]; // Premier élément
                    } else if (z.equals("ORD")) {
                      codeOrdonnateur = valeurs[2]; // Deuxième élément
                    } else if (z.equals("GESTION")) {
                      gestion = valeurs[2]; // Troisième élément
                    } else if (z.equals("CODE_MANDAT")) {
                      CodeMandat = valeurs[2]; // Quatrième élément
                    } else if (z.equals("DT_EMISSION")) {
                      DateEmission = valeurs[2]; // Cinquième élément
                    } else if (z.equals("STATUT")) {
                      Statut = valeurs[2]; // 6 éme élément
                    } else if (z.equals("PROG")) {
                      Programme = valeurs[2]; // 7 éme élément
                    } else if (z.equals("S_PROG")) {
                      sousProgramme = valeurs[2]; // 8 éme élément
                    } else if (z.equals("ACTION")) {
                      Action = valeurs[2]; // 9 éme élément
                    } else if (z.equals("S_ACTION")) {
                      SousAction = valeurs[2]; // 10 éme élément
                    } else if (z.equals("AXE_ECO")) {
                      AxeEconomique = valeurs[2]; // 11 éme élément
                    } else if (z.equals("DISPOS")) {
                      Dispos = valeurs[2]; // 12 éme élément
                    } else if (z.equals("MONTANT")) {
                      Mantant = valeurs[2]; // 13 éme élément
                    }

                    // test pour le 4er élément de tablea3
                    if (a.equals("CODE_PORTEF")) {
                      CodePortef = valeurs[3]; // Premier élément
                    } else if (a.equals("ORD")) {
                      codeOrdonnateur = valeurs[3]; // Deuxième élément
                    } else if (a.equals("GESTION")) {
                      gestion = valeurs[3]; // Troisième élément
                    } else if (a.equals("CODE_MANDAT")) {
                      CodeMandat = valeurs[3]; // Quatrième élément
                    } else if (a.equals("DT_EMISSION")) {
                      DateEmission = valeurs[3]; // Cinquième élément
                    } else if (a.equals("STATUT")) {
                      Statut = valeurs[3]; // 6 éme élément
                    } else if (a.equals("PROG")) {
                      Programme = valeurs[3]; // 7 éme élément
                    } else if (a.equals("S_PROG")) {
                      sousProgramme = valeurs[3]; // 8 éme élément
                    } else if (a.equals("ACTION")) {
                      Action = valeurs[3]; // 9 éme élément
                    } else if (a.equals("S_ACTION")) {
                      SousAction = valeurs[3]; // 10 éme élément
                    } else if (a.equals("AXE_ECO")) {
                      AxeEconomique = valeurs[3]; // 11 éme élément
                    } else if (a.equals("DISPOS")) {
                      Dispos = valeurs[3]; // 12 éme élément
                    } else if (a.equals("MONTANT")) {
                      Mantant = valeurs[3]; // 13 éme élément
                    }

                    // test pour le 5er élément de tableau
                    if (b.equals("CODE_PORTEF")) {
                      CodePortef = valeurs[4]; // Premier élément
                    } else if (b.equals("ORD")) {
                      codeOrdonnateur = valeurs[4]; // Deuxième élément
                    } else if (b.equals("GESTION")) {
                      gestion = valeurs[4]; // Troisième élément
                    } else if (b.equals("CODE_MANDAT")) {
                      CodeMandat = valeurs[4]; // Quatrième élément
                    } else if (b.equals("DT_EMISSION")) {
                      DateEmission = valeurs[4]; // Cinquième élément
                    } else if (b.equals("STATUT")) {
                      Statut = valeurs[4]; // 6 éme élément
                    } else if (b.equals("PROG")) {
                      Programme = valeurs[4]; // 7 éme élément
                    } else if (b.equals("S_PROG")) {
                      sousProgramme = valeurs[4]; // 8 éme élément
                    } else if (b.equals("ACTION")) {
                      Action = valeurs[4]; // 9 éme élément
                    } else if (b.equals("S_ACTION")) {
                      SousAction = valeurs[4]; // 10 éme élément
                    } else if (b.equals("AXE_ECO")) {
                      AxeEconomique = valeurs[4]; // 11 éme élément
                    } else if (b.equals("DISPOS")) {
                      Dispos = valeurs[4]; // 12 éme élément
                    } else if (b.equals("MONTANT")) {
                      Mantant = valeurs[4]; // 13 éme élément
                    }

                    // test pour le 6er élément de tab5eau
                    if (c.equals("CODE_PORTEF")) {
                      CodePortef = valeurs[5]; // Premier élément
                    } else if (c.equals("ORD")) {
                      codeOrdonnateur = valeurs[5]; // Deuxième élément
                    } else if (c.equals("GESTION")) {
                      gestion = valeurs[5]; // Troisième élément
                    } else if (c.equals("CODE_MANDAT")) {
                      CodeMandat = valeurs[5]; // Quatrième élément
                    } else if (c.equals("DT_EMISSION")) {
                      DateEmission = valeurs[5]; // Cinquième élément
                    } else if (c.equals("STATUT")) {
                      Statut = valeurs[5]; // 6 éme élément
                    } else if (c.equals("PROG")) {
                      Programme = valeurs[5]; // 7 éme élément
                    } else if (c.equals("S_PROG")) {
                      sousProgramme = valeurs[5]; // 8 éme élément
                    } else if (c.equals("ACTION")) {
                      Action = valeurs[5]; // 9 éme élément
                    } else if (c.equals("S_ACTION")) {
                      SousAction = valeurs[5]; // 10 éme élément
                    } else if (c.equals("AXE_ECO")) {
                      AxeEconomique = valeurs[5]; // 11 éme élément
                    } else if (c.equals("DISPOS")) {
                      Dispos = valeurs[5]; // 12 éme élément
                    } else if (c.equals("MONTANT")) {
                      Mantant = valeurs[5]; // 13 éme élément
                    }

                    // test pour le 7er élément de tab5eau
                    if (d.equals("CODE_PORTEF")) {
                      CodePortef = valeurs[6]; // Premier élément
                    } else if (d.equals("ORD")) {
                      codeOrdonnateur = valeurs[6]; // Deuxième élément
                    } else if (d.equals("GESTION")) {
                      gestion = valeurs[6]; // Troisième élément
                    } else if (d.equals("CODE_MANDAT")) {
                      CodeMandat = valeurs[6]; // Quatrième élément
                    } else if (d.equals("DT_EMISSION")) {
                      DateEmission = valeurs[6]; // Cinquième élément
                    } else if (d.equals("STATUT")) {
                      Statut = valeurs[6]; // 6 éme élément
                    } else if (d.equals("PROG")) {
                      Programme = valeurs[6]; // 7 éme élément
                    } else if (d.equals("S_PROG")) {
                      sousProgramme = valeurs[6]; // 8 éme élément
                    } else if (d.equals("ACTION")) {
                      Action = valeurs[6]; // 9 éme élément
                    } else if (d.equals("S_ACTION")) {
                      SousAction = valeurs[6]; // 10 éme élément
                    } else if (d.equals("AXE_ECO")) {
                      AxeEconomique = valeurs[6]; // 11 éme élément
                    } else if (d.equals("DISPOS")) {
                      Dispos = valeurs[6]; // 12 éme élément
                    } else if (d.equals("MONTANT")) {
                      Mantant = valeurs[6]; // 13 éme élément
                    }

                    // test pour le 8er élément de tab5ea7
                    if (e.equals("CODE_PORTEF")) {
                      CodePortef = valeurs[7]; // Premier élément
                    } else if (e.equals("ORD")) {
                      codeOrdonnateur = valeurs[7]; // Deuxième élément
                    } else if (e.equals("GESTION")) {
                      gestion = valeurs[7]; // Troisième élément
                    } else if (e.equals("CODE_MANDAT")) {
                      CodeMandat = valeurs[7]; // Quatrième élément
                    } else if (e.equals("DT_EMISSION")) {
                      DateEmission = valeurs[7]; // Cinquième élément
                    } else if (e.equals("STATUT")) {
                      Statut = valeurs[7]; // 6 éme élément
                    } else if (e.equals("PROG")) {
                      Programme = valeurs[7]; // 7 éme élément
                    } else if (e.equals("S_PROG")) {
                      sousProgramme = valeurs[7]; // 8 éme élément
                    } else if (e.equals("ACTION")) {
                      Action = valeurs[7]; // 9 éme élément
                    } else if (e.equals("S_ACTION")) {
                      SousAction = valeurs[7]; // 10 éme élément
                    } else if (e.equals("AXE_ECO")) {
                      AxeEconomique = valeurs[7]; // 11 éme élément
                    } else if (e.equals("DISPOS")) {
                      Dispos = valeurs[7]; // 12 éme élément
                    } else if (e.equals("MONTANT")) {
                      Mantant = valeurs[7]; // 13 éme élément
                    }

                    // test pour le 9er élément de tab5eau
                    if (f.equals("CODE_PORTEF")) {
                      CodePortef = valeurs[8]; // Premier élément
                    } else if (f.equals("ORD")) {
                      codeOrdonnateur = valeurs[8]; // Deuxième élément
                    } else if (f.equals("GESTION")) {
                      gestion = valeurs[8]; // Troisième élément
                    } else if (f.equals("CODE_MANDAT")) {
                      CodeMandat = valeurs[8]; // Quatrième élément
                    } else if (f.equals("DT_EMISSION")) {
                      DateEmission = valeurs[8]; // Cinquième élément
                    } else if (f.equals("STATUT")) {
                      Statut = valeurs[8]; // 6 éme élément
                    } else if (f.equals("PROG")) {
                      Programme = valeurs[8]; // 7 éme élément
                    } else if (f.equals("S_PROG")) {
                      sousProgramme = valeurs[8]; // 8 éme élément
                    } else if (f.equals("ACTION")) {
                      Action = valeurs[8]; // 9 éme élément
                    } else if (f.equals("S_ACTION")) {
                      SousAction = valeurs[8]; // 10 éme élément
                    } else if (f.equals("AXE_ECO")) {
                      AxeEconomique = valeurs[8]; // 11 éme élément
                    } else if (f.equals("DISPOS")) {
                      Dispos = valeurs[8]; // 12 éme élément
                    } else if (f.equals("MONTANT")) {
                      Mantant = valeurs[8]; // 13 éme élément
                    }

                    // test pour le 10er élément de tab5eau
                    if (g.equals("CODE_PORTEF")) {
                      CodePortef = valeurs[9]; // Premier élément
                    } else if (g.equals("ORD")) {
                      codeOrdonnateur = valeurs[9]; // Deuxième élément
                    } else if (g.equals("GESTION")) {
                      gestion = valeurs[9]; // Troisième élément
                    } else if (g.equals("CODE_MANDAT")) {
                      CodeMandat = valeurs[9]; // Quatrième élément
                    } else if (g.equals("DT_EMISSION")) {
                      DateEmission = valeurs[9]; // Cinquième élément
                    } else if (g.equals("STATUT")) {
                      Statut = valeurs[9]; // 6 éme élément
                    } else if (g.equals("PROG")) {
                      Programme = valeurs[9]; // 7 éme élément
                    } else if (g.equals("S_PROG")) {
                      sousProgramme = valeurs[9]; // 8 éme élément
                    } else if (g.equals("ACTION")) {
                      Action = valeurs[9]; // 9 éme élément
                    } else if (g.equals("S_ACTION")) {
                      SousAction = valeurs[9]; // 10 éme élément
                    } else if (g.equals("AXE_ECO")) {
                      AxeEconomique = valeurs[9]; // 11 éme élément
                    } else if (g.equals("DISPOS")) {
                      Dispos = valeurs[9]; // 12 éme élément
                    } else if (g.equals("MONTANT")) {
                      Mantant = valeurs[9]; // 13 éme élément
                    }

                    // test pour 11er élément de tab5eau
                    if (h.equals("CODE_PORTEF")) {
                      CodePortef = valeurs[10]; // Premier élément
                    } else if (h.equals("ORD")) {
                      codeOrdonnateur = valeurs[10]; // Deuxième élément
                    } else if (h.equals("GESTION")) {
                      gestion = valeurs[10]; // Troisième élément
                    } else if (h.equals("CODE_MANDAT")) {
                      CodeMandat = valeurs[10]; // Quatrième élément
                    } else if (h.equals("DT_EMISSION")) {
                      DateEmission = valeurs[10]; // Cinquième élément
                    } else if (h.equals("STATUT")) {
                      Statut = valeurs[10]; // 6 éme élément
                    } else if (h.equals("PROG")) {
                      Programme = valeurs[10]; // 7 éme élément
                    } else if (h.equals("S_PROG")) {
                      sousProgramme = valeurs[10]; // 8 éme élément
                    } else if (h.equals("ACTION")) {
                      Action = valeurs[10]; // 9 éme élément
                    } else if (h.equals("S_ACTION")) {
                      SousAction = valeurs[10]; // 10 éme élément
                    } else if (h.equals("AXE_ECO")) {
                      AxeEconomique = valeurs[10]; // 11 éme élément
                    } else if (h.equals("DISPOS")) {
                      Dispos = valeurs[10]; // 12 éme élément
                    } else if (h.equals("MONTANT")) {
                      Mantant = valeurs[10]; // 13 éme élément
                    }

                    // test pour le 12er élément de tab5eau
                    if (l.equals("CODE_PORTEF")) {
                      CodePortef = valeurs[11]; // Premier élément
                    } else if (l.equals("ORD")) {
                      codeOrdonnateur = valeurs[11]; // Deuxième élément
                    } else if (l.equals("GESTION")) {
                      gestion = valeurs[11]; // Troisième élément
                    } else if (l.equals("CODE_MANDAT")) {
                      CodeMandat = valeurs[11]; // Quatrième élément
                    } else if (l.equals("DT_EMISSION")) {
                      DateEmission = valeurs[11]; // Cinquième élément
                    } else if (l.equals("STATUT")) {
                      Statut = valeurs[11]; // 6 éme élément
                    } else if (l.equals("PROG")) {
                      Programme = valeurs[11]; // 7 éme élément
                    } else if (l.equals("S_PROG")) {
                      sousProgramme = valeurs[11]; // 8 éme élément
                    } else if (l.equals("ACTION")) {
                      Action = valeurs[11]; // 9 éme élément
                    } else if (l.equals("S_ACTION")) {
                      SousAction = valeurs[11]; // 10 éme élément
                    } else if (l.equals("AXE_ECO")) {
                      AxeEconomique = valeurs[11]; // 11 éme élément
                    } else if (l.equals("DISPOS")) {
                      Dispos = valeurs[11]; // 12 éme élément
                    } else if (l.equals("MONTANT")) {
                      Mantant = valeurs[11]; // 13 éme élément
                    }

                    // test pour le 13er élément de tab5eau
                    if (k.equals("CODE_PORTEF")) {
                      CodePortef = valeurs[12]; // Premier élément
                    } else if (k.equals("ORD")) {
                      codeOrdonnateur = valeurs[12]; // Deuxième élément
                    } else if (k.equals("GESTION")) {
                      gestion = valeurs[12]; // Troisième élément
                    } else if (k.equals("CODE_MANDAT")) {
                      CodeMandat = valeurs[12]; // Quatrième élément
                    } else if (k.equals("DT_EMISSION")) {
                      DateEmission = valeurs[12]; // Cinquième élément
                    } else if (k.equals("STATUT")) {
                      Statut = valeurs[12]; // 6 éme élément
                    } else if (k.equals("PROG")) {
                      Programme = valeurs[12]; // 7 éme élément
                    } else if (k.equals("S_PROG")) {
                      sousProgramme = valeurs[12]; // 8 éme élément
                    } else if (k.equals("ACTION")) {
                      Action = valeurs[12]; // 9 éme élément
                    } else if (k.equals("S_ACTION")) {
                      SousAction = valeurs[12]; // 10 éme élément
                    } else if (k.equals("AXE_ECO")) {
                      AxeEconomique = valeurs[12]; // 11 éme élément
                    } else if (k.equals("DISPOS")) {
                      Dispos = valeurs[12]; // 12 éme élément
                    } else if (k.equals("MONTANT")) {
                      Mantant = valeurs[12]; // 13 éme élément
                    }

                    System.out.println(valus[0] + ":" + valeurs[0]);
                    System.out.println(valus[1] + ":" + valeurs[1]);
                    System.out.println(valus[2] + ":" + valeurs[2]);
                    System.out.println(valus[3] + ":" + valeurs[3]);
                    System.out.println(valus[4] + ":" + valeurs[4]);
                    System.out.println(valus[5] + ":" + valeurs[5]);
                    System.out.println(valus[6] + ":" + valeurs[6]);
                    System.out.println(valus[7] + ":" + valeurs[7]);
                    System.out.println(valus[8] + ":" + valeurs[8]);
                    System.out.println(valus[9] + ":" + valeurs[9]);
                    System.out.println(valus[10] + ":" + valeurs[10]);
                    System.out.println(valus[11] + ":" + valeurs[11]);
                    System.out.println(valus[12] + ":" + valeurs[12]);

                    // Appel de la fonction d'insertion avec les valeurs récupérées
                    String insertMandat = relationnel.insertIntoMndat(
                        CodePortef,
                        codeOrdonnateur,
                        gestion,
                        CodeMandat,
                        DateEmission,
                        Statut,
                        Programme,
                        sousProgramme,
                        Action,
                        SousAction,
                        AxeEconomique,
                        Dispos,
                        Mantant);
                    System.out.println(insertMandat);
                  }

                  br3.close(); // Fermer le lecteur de fichier existant

                  // supression de contenu de ficher aprés traiatment
                  try {
                    FileWriter writer1 = new FileWriter(cheminFichier);
                    writer1.write(""); // Écriture d'une chaîne vide pour vider le fichier
                    writer1.close();
                    System.out.println(
                        "Contenu du fichier supprimé avec succès aprés insertion des éléments.");
                  } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println(
                        "Une erreur s'est produite lors de la suppression du contenu du fichier.");
                  }
                } else if (contientToutWilayas) {
                  System.out.println(
                      "Le tableau contient toutes les valeurs uniques de la wilaya.");
                  String ligneDonnee;

                  while ((ligneDonnee = br3.readLine()) != null) {
                    String[] valeurs = ligneDonnee.split(";");

                    // Afficher les valeurs de la deuxième ligne
                    /*
                     * for (String valeur : valeurs) {
                     * System.out.print(valeur + " ");
                     * }
                     */
                    System.out.println(); // Nouvelle ligne après chaque ligne du fichier
                    String x = valus[0];
                    String y = valus[1];
                    String CodeWilaya = "";
                    String libelleWilaya = "";

                    // test pour le 1er élément de tableau
                    if (x.equals("Code_Wilaya")) {
                      CodeWilaya = valeurs[0]; // Premier élément
                    } else if (x.equals("Libelle_wilaya")) {
                      libelleWilaya = valeurs[0]; // Deuxième élément
                    }

                    // test pour le 1er élément de tableau
                    if (y.equals("Code_Wilaya")) {
                      CodeWilaya = valeurs[1]; // Premier élément
                    } else if (y.equals("Libelle_wilaya")) {
                      libelleWilaya = valeurs[1]; // Deuxième élément
                    }

                    System.out.println(valus[0] + ":" + valeurs[0]);
                    System.out.println(valus[1] + ":" + valeurs[1]);

                    // Appel de la fonction d'insertion avec les valeurs récupérées
                    String insertAxeeco = relationnel.insertIntoWilaya(
                        CodeWilaya,
                        libelleWilaya);
                    System.out.println(insertAxeeco);
                  }

                  br3.close(); // Fermer le lecteur de fichier existant

                  // supression de contenu de ficher aprés traiatment
                  try {
                    FileWriter writer1 = new FileWriter(cheminFichier);
                    writer1.write(""); // Écriture d'une chaîne vide pour vider le fichier
                    writer1.close();
                    System.out.println(
                        "Contenu du fichier supprimé avec succès aprés insertion des éléments.");
                  } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println(
                        "Une erreur s'est produite lors de la suppression du contenu du fichier.");
                  }


                }else if (contientToutRecette) {
                  System.out.println(
                      "Le tableau contient toutes les valeurs uniques de Recette.");
                  String ligneDonnee;

                  while ((ligneDonnee = br3.readLine()) != null) {
                    String[] valeurs = ligneDonnee.split(";");

                    // Afficher les valeurs de la deuxième ligne
                    /*
                     * for (String valeur : valeurs) {
                     * System.out.print(valeur + " ");
                     * }
                     */
                    System.out.println(); // Nouvelle ligne après chaque ligne du fichier
                    String x = valus[0];
                    String y = valus[1];
                    String z = valus[2];
                    String a = valus[3];
                    String b = valus[4];
                    String c = valus[5];
                    
                    String Gestion = "";
                    String Mois = "";
                    String CodeCompte = "";
                    String libelleCompte = "";
                    String PosteComptable = "";
                    String Montant = "";

                    // test pour le 1er élément de tableau
                    if (x.equals("GESTION")) {
                      Gestion = valeurs[0]; // Premier élément
                    } else if (x.equals("MOIS")) {
                      Mois = valeurs[0]; // Deuxième élément
                    } else if (x.equals("CODE_CPT")) {
                      CodeCompte = valeurs[0]; // troisiéme élément
                    } else if (x.equals("LIB_CPT_G")) {
                      libelleCompte = valeurs[0]; // qutriéme élément
                    }else if (x.equals("POSTE_C")) {
                      PosteComptable = valeurs[0]; // sixiéme élément
                    }else if (x.equals("MT_MOIS")) {
                      Montant = valeurs[0]; // sixiéme élément
                    }


                    // test pour le 2er élément de tableau
                    if (y.equals("GESTION")) {
                      Gestion = valeurs[1]; // Premier élément
                    } else if (y.equals("MOIS")) {
                      Mois = valeurs[1]; // Deuxième élément
                    } else if (y.equals("CODE_CPT")) {
                      CodeCompte = valeurs[1]; // troisiéme élément
                    } else if (y.equals("LIB_CPT_G")) {
                      libelleCompte = valeurs[1]; // qutriéme élément
                    }else if (y.equals("POSTE_C")) {
                      PosteComptable = valeurs[1]; // sixiéme élément
                    }else if (y.equals("MT_MOIS")) {
                      Montant = valeurs[1]; // sixiéme élément
                    }


                    // test pour le 3er élément de tableau
                    if (z.equals("GESTION")) {
                      Gestion = valeurs[2]; // Premier élément
                    } else if (z.equals("MOIS")) {
                      Mois = valeurs[2]; // Deuxième élément
                    } else if (z.equals("CODE_CPT")) {
                      CodeCompte = valeurs[2]; // troisiéme élément
                    } else if (z.equals("LIB_CPT_G")) {
                      libelleCompte = valeurs[2]; // qutriéme élément
                    }else if (z.equals("POSTE_C")) {
                      PosteComptable = valeurs[2]; // sixiéme élément
                    } else if (z.equals("MT_MOIS")) {
                      Montant = valeurs[2]; // sixiéme élément
                    }


                    // test pour le 4er élément de tableau
                    if (a.equals("GESTION")) {
                      Gestion = valeurs[3]; // Premier élément
                    } else if (a.equals("MOIS")) {
                      Mois = valeurs[3]; // Deuxième élément
                    } else if (a.equals("CODE_CPT")) {
                      CodeCompte = valeurs[3]; // troisiéme élément
                    } else if (a.equals("LIB_CPT_G")) {
                      libelleCompte = valeurs[3]; // qutriéme élément
                    } else if (a.equals("POSTE_C")) {
                      PosteComptable = valeurs[3]; // sixiéme élément
                    }else if (a.equals("MT_MOIS")) {
                      Montant = valeurs[3]; // sixiéme élément
                    }



                    // test pour le 5er élément de tableau
                    if (b.equals("GESTION")) {
                      Gestion = valeurs[4]; // Premier élément
                    } else if (b.equals("MOIS")) {
                      Mois = valeurs[4]; // Deuxième élément
                    } else if (b.equals("CODE_CPT")) {
                      CodeCompte = valeurs[4]; // troisiéme élément
                    } else if (b.equals("LIB_CPT_G")) {
                      libelleCompte = valeurs[4]; // qutriéme élément
                    } else if (b.equals("POSTE_C")) {
                      PosteComptable = valeurs[4]; // sixiéme élément
                    }else if (b.equals("MT_MOIS")) {
                      Montant = valeurs[4]; // sixiéme élément
                    }




                    // test pour le 6er élément de tableau
                    if (c.equals("GESTION")) {
                      Gestion = valeurs[5]; // Premier élément
                    } else if (c.equals("MOIS")) {
                      Mois = valeurs[5]; // Deuxième élément
                    } else if (c.equals("CODE_CPT")) {
                      CodeCompte = valeurs[5]; // troisiéme élément
                    } else if (c.equals("LIB_CPT_G")) {
                      libelleCompte = valeurs[5]; // qutriéme élément
                    } else if (c.equals("POSTE_C")) {
                      PosteComptable = valeurs[5]; // sixiéme élément
                    }else if (c.equals("MT_MOIS")) {
                      Montant = valeurs[5]; // sixiéme élément
                    }





                    System.out.println(valus[0] + ":" + valeurs[0]);
                    System.out.println(valus[1] + ":" + valeurs[1]);
                    System.out.println(valus[2] + ":" + valeurs[2]);
                    System.out.println(valus[3] + ":" + valeurs[3]);
                    System.out.println(valus[4] + ":" + valeurs[4]);
                    System.out.println(valus[5] + ":" + valeurs[5]);
          
                    // Appel de la fonction d'insertion avec les valeurs récupérées
                    String insertRecette = relationnel.insertIntoRecette(
                      Gestion,
                      Mois,
                      CodeCompte,
                      libelleCompte,
                      PosteComptable,
                      Montant);
                    System.out.println(insertRecette);
                  }

                  br3.close(); // Fermer le lecteur de fichier existant

                  // supression de contenu de ficher aprés traiatment
                  try {
                    FileWriter writer1 = new FileWriter(cheminFichier);
                    writer1.write(""); // Écriture d'une chaîne vide pour vider le fichier
                    writer1.close();
                    System.out.println(
                        "Contenu du fichier supprimé avec succès aprés insertion des éléments.");
                  } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println(
                        "Une erreur s'est produite lors de la suppression du contenu du fichier.");
                  }





                } else if(contientToutSoumissionaire){


                  System.out.println(
                      "Le tableau contient toutes les valeurs uniques de Soumissionaire.");
                  String ligneDonnee;

                  while ((ligneDonnee = br3.readLine()) != null) {
                    String[] valeurs = ligneDonnee.split(";");

                    // Afficher les valeurs de la deuxième ligne
                    /*
                     * for (String valeur : valeurs) {
                     * System.out.print(valeur + " ");
                     * }
                     */
                    System.out.println(); // Nouvelle ligne après chaque ligne du fichier
                    String x = valus[0];
                    String y = valus[1];
                    String z = valus[2];
                    
                    String codeSoumissionaire = "";
                    String TypeSoumissionaire = "";
                    String libelleSoumissionaire = "";

                    // test pour le 1er élément de tableau
                    if (x.equals("CODE_Soumissionaire")) {
                      codeSoumissionaire = valeurs[0]; // Premier élément
                    } else if (x.equals("Type_Soumissionaire")) {
                      TypeSoumissionaire = valeurs[0]; // Deuxième élément
                    } else if (x.equals("libelle_Soumissionaire")) {
                      libelleSoumissionaire = valeurs[0]; // troisiéme élément
                    } 


                    // test pour le 2er élément de tableau
                    if (y.equals("CODE_Soumissionaire")) {
                      codeSoumissionaire = valeurs[1]; // Premier élément
                    } else if (y.equals("Type_Soumissionaire")) {
                      TypeSoumissionaire = valeurs[1]; // Deuxième élément
                    } else if (y.equals("libelle_Soumissionaire")) {
                      libelleSoumissionaire = valeurs[1]; // troisiéme élément
                    } 


                    // test pour le 2er élément de tableau
                    if (z.equals("CODE_Soumissionaire")) {
                      codeSoumissionaire = valeurs[2]; // Premier élément
                    } else if (z.equals("Type_Soumissionaire")) {
                      TypeSoumissionaire = valeurs[2]; // Deuxième élément
                    } else if (z.equals("libelle_Soumissionaire")) {
                      libelleSoumissionaire = valeurs[2]; // troisiéme élément
                    } 


                    





                    System.out.println(valus[0] + ":" + valeurs[0]);
                    System.out.println(valus[1] + ":" + valeurs[1]);
                    System.out.println(valus[2] + ":" + valeurs[2]);
          
                    // Appel de la fonction d'insertion avec les valeurs récupérées
                    String insertSoumissionaire = relationnel.insertIntoSoumissionaire(
                      codeSoumissionaire,
                      TypeSoumissionaire,
                      libelleSoumissionaire);
                    System.out.println(insertSoumissionaire);

                  }

                  br3.close(); // Fermer le lecteur de fichier existant

                  // supression de contenu de ficher aprés traiatment
                  try {
                    FileWriter writer1 = new FileWriter(cheminFichier);
                    writer1.write(""); // Écriture d'une chaîne vide pour vider le fichier
                    writer1.close();
                    System.out.println(
                        "Contenu du fichier supprimé avec succès aprés insertion des éléments.");
                  } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println(
                        "Une erreur s'est produite lors de la suppression du contenu du fichier.");
                  }

                  

                } else if (contientToutDette){

                  System.out.println(
                      "Le tableau contient toutes les valeurs uniques de Dette.");
                  String ligneDonnee;

                  System.out.println(valus.length);

                  while ((ligneDonnee = br3.readLine()) != null) {
                    String[] valeurs = ligneDonnee.split(";");

                    // Afficher les valeurs de la deuxième ligne
                    /*
                     * for (String valeur : valeurs) {
                     * System.out.print(valeur + " ");
                     * }
                     */
                    System.out.println(); // Nouvelle ligne après chaque ligne du fichier
                    String x = valus[0];
                    String y = valus[1];
                    String z = valus[2];
                    String a = valus[3];
                    String b = valus[4];
                    String c = valus[5];
                    String d = valus[6];
                    String e = valus[7];
                    String f = valus[8];



                    String codeISIN = "";
                    String DateDeb = "";
                    String DureeBon = "";
                    String DateEcheance = "";
                    String CodeSoumissionaire = "";
                    String MontantProposeParEtat = "";
                    String MontantProposeParSoumissionaire = "";
                    String MontantAdjuge = "";
                    String MontantCoupoun = "";

                    // test pour le 1er élément de tableau
                    if (x.equals("code_ISIN")) {
                      codeISIN = valeurs[0]; // Premier élément
                    } else if (x.equals("DATE_DEB")) {
                      DateDeb = valeurs[0]; // Troisième élément
                    } else if (x.equals("DUREE_bon")) {
                      DureeBon = valeurs[0]; // Quatrième élément
                    } else if (x.equals("DATE_ECHEANCE")) {
                      DateEcheance = valeurs[0]; // Cinquième élément
                    } else if (x.equals("Code_Soumissionaire")) {
                      CodeSoumissionaire = valeurs[0]; // 6 éme élément
                    } else if (x.equals("MONTANT_Propose_Par_etat")) {
                      MontantProposeParEtat = valeurs[0]; // 7 éme élément
                    } else if (x.equals("Montant_Propose_Par_Soumissionaire")) {
                      MontantProposeParSoumissionaire = valeurs[0]; // 8 éme élément
                    } else if (x.equals("Montant_Adjuge")) {
                      MontantAdjuge = valeurs[0]; // 9 éme élément
                    } else if (x.equals("Montant_Coupoun")) {
                      MontantCoupoun = valeurs[0]; // 10 éme élément
                    }

                    // test pour le 2er élément de tableau
                    if (y.equals("code_ISIN")) {
                      codeISIN = valeurs[1]; // Premier élément
                    } else if (y.equals("DATE_DEB")) {
                      DateDeb = valeurs[1]; // Troisième élément
                    } else if (y.equals("DUREE_bon")) {
                      DureeBon = valeurs[1]; // Quatrième élément
                    } else if (y.equals("DATE_ECHEANCE")) {
                      DateEcheance = valeurs[1]; // Cinquième élément
                    } else if (y.equals("Code_Soumissionaire")) {
                      CodeSoumissionaire = valeurs[1]; // 6 éme élément
                    } else if (y.equals("MONTANT_Propose_Par_etat")) {
                      MontantProposeParEtat = valeurs[1]; // 7 éme élément
                    } else if (y.equals("Montant_Propose_Par_Soumissionaire")) {
                      MontantProposeParSoumissionaire = valeurs[1]; // 8 éme élément
                    } else if (y.equals("Montant_Adjuge")) {
                      MontantAdjuge = valeurs[1]; // 9 éme élément
                    } else if (y.equals("Montant_Coupoun")) {
                      MontantCoupoun = valeurs[1]; // 10 éme élément
                    }



                    // test pour le 3er élément de tableau
                    if (z.equals("code_ISIN")) {
                      codeISIN = valeurs[2]; // Premier élément
                    }  else if (z.equals("DATE_DEB")) {
                      DateDeb = valeurs[2]; // Troisième élément
                    } else if (z.equals("DUREE_bon")) {
                      DureeBon = valeurs[2]; // Quatrième élément
                    } else if (z.equals("DATE_ECHEANCE")) {
                      DateEcheance = valeurs[2]; // Cinquième élément
                    } else if (z.equals("Code_Soumissionaire")) {
                      CodeSoumissionaire = valeurs[2]; // 6 éme élément
                    } else if (z.equals("MONTANT_Propose_Par_etat")) {
                      MontantProposeParEtat = valeurs[2]; // 7 éme élément
                    } else if (z.equals("Montant_Propose_Par_Soumissionaire")) {
                      MontantProposeParSoumissionaire = valeurs[2]; // 8 éme élément
                    } else if (z.equals("Montant_Adjuge")) {
                      MontantAdjuge = valeurs[2]; // 9 éme élément
                    } else if (z.equals("Montant_Coupoun")) {
                      MontantCoupoun = valeurs[2]; // 10 éme élément
                    }

                    // test pour le 4er élément de tablea3
                    if (a.equals("code_ISIN")) {
                      codeISIN = valeurs[3]; // Premier élément
                    }  else if (a.equals("DATE_DEB")) {
                      DateDeb = valeurs[3]; // Troisième élément
                    } else if (a.equals("DUREE_bon")) {
                      DureeBon = valeurs[3]; // Quatrième élément
                    } else if (a.equals("DATE_ECHEANCE")) {
                      DateEcheance = valeurs[3]; // Cinquième élément
                    } else if (a.equals("Code_Soumissionaire")) {
                      CodeSoumissionaire = valeurs[3]; // 6 éme élément
                    } else if (a.equals("MONTANT_Propose_Par_etat")) {
                      MontantProposeParEtat = valeurs[3]; // 7 éme élément
                    } else if (a.equals("Montant_Propose_Par_Soumissionaire")) {
                      MontantProposeParSoumissionaire = valeurs[3]; // 8 éme élément
                    } else if (a.equals("Montant_Adjuge")) {
                      MontantAdjuge = valeurs[3]; // 9 éme élément
                    } else if (a.equals("Montant_Coupoun")) {
                      MontantCoupoun = valeurs[3]; // 10 éme élément
                    }

                    // test pour le 5er élément de tableau
                    if (b.equals("code_ISIN")) {
                      codeISIN = valeurs[4]; // Premier élément
                    } else if (b.equals("DATE_DEB")) {
                      DateDeb = valeurs[4]; // Troisième élément
                    } else if (b.equals("DUREE_bon")) {
                      DureeBon = valeurs[4]; // Quatrième élément
                    } else if (b.equals("DATE_ECHEANCE")) {
                      DateEcheance = valeurs[4]; // Cinquième élément
                    } else if (b.equals("Code_Soumissionaire")) {
                      CodeSoumissionaire = valeurs[4]; // 6 éme élément
                    } else if (b.equals("MONTANT_Propose_Par_etat")) {
                      MontantProposeParEtat = valeurs[4]; // 7 éme élément
                    } else if (b.equals("Montant_Propose_Par_Soumissionaire")) {
                      MontantProposeParSoumissionaire = valeurs[4]; // 8 éme élément
                    } else if (b.equals("Montant_Adjuge")) {
                      MontantAdjuge = valeurs[4]; // 9 éme élément
                    } else if (b.equals("Montant_Coupoun")) {
                      MontantCoupoun = valeurs[4]; // 10 éme élément
                    }

                    // test pour le 6er élément de tableau
                    if (c.equals("code_ISIN")) {
                      codeISIN = valeurs[5]; // Premier élément
                    } else if (c.equals("DATE_DEB")) {
                      DateDeb = valeurs[5]; // Troisième élément
                    } else if (c.equals("DUREE_bon")) {
                      DureeBon = valeurs[5]; // Quatrième élément
                    } else if (c.equals("DATE_ECHEANCE")) {
                      DateEcheance = valeurs[5]; // Cinquième élément
                    } else if (c.equals("Code_Soumissionaire")) {
                      CodeSoumissionaire = valeurs[5]; // 6 éme élément
                    } else if (c.equals("MONTANT_Propose_Par_etat")) {
                      MontantProposeParEtat = valeurs[5]; // 7 éme élément
                    } else if (c.equals("Montant_Propose_Par_Soumissionaire")) {
                      MontantProposeParSoumissionaire = valeurs[5]; // 8 éme élément
                    } else if (c.equals("Montant_Adjuge")) {
                      MontantAdjuge = valeurs[5]; // 9 éme élément
                    } else if (c.equals("Montant_Coupoun")) {
                      MontantCoupoun = valeurs[5]; // 10 éme élément
                    }

                    // test pour le 7er élément de tableau
                    if (d.equals("code_ISIN")) {
                      codeISIN = valeurs[6]; // Premier élément
                    } else if (d.equals("DATE_DEB")) {
                      DateDeb = valeurs[6]; // Troisième élément
                    } else if (d.equals("DUREE_bon")) {
                      DureeBon = valeurs[6]; // Quatrième élément
                    } else if (d.equals("DATE_ECHEANCE")) {
                      DateEcheance = valeurs[6]; // Cinquième élément
                    } else if (d.equals("Code_Soumissionaire")) {
                      CodeSoumissionaire = valeurs[6]; // 6 éme élément
                    } else if (d.equals("MONTANT_Propose_Par_etat")) {
                      MontantProposeParEtat = valeurs[6]; // 7 éme élément
                    } else if (d.equals("Montant_Propose_Par_Soumissionaire")) {
                      MontantProposeParSoumissionaire = valeurs[6]; // 8 éme élément
                    } else if (d.equals("Montant_Adjuge")) {
                      MontantAdjuge = valeurs[6]; // 9 éme élément
                    } else if (d.equals("Montant_Coupoun")) {
                      MontantCoupoun = valeurs[6]; // 10 éme élément
                    }

                    // test pour le 8er élément de tableau
                    if (e.equals("code_ISIN")) {
                      codeISIN = valeurs[7]; // Premier élément
                    } else if (e.equals("DATE_DEB")) {
                      DateDeb = valeurs[7]; // Troisième élément
                    } else if (e.equals("DUREE_bon")) {
                      DureeBon = valeurs[7]; // Quatrième élément
                    } else if (e.equals("DATE_ECHEANCE")) {
                      DateEcheance = valeurs[7]; // Cinquième élément
                    } else if (e.equals("Code_Soumissionaire")) {
                      CodeSoumissionaire = valeurs[7]; // 6 éme élément
                    } else if (e.equals("MONTANT_Propose_Par_etat")) {
                      MontantProposeParEtat = valeurs[7]; // 7 éme élément
                    } else if (e.equals("Montant_Propose_Par_Soumissionaire")) {
                      MontantProposeParSoumissionaire = valeurs[7]; // 8 éme élément
                    } else if (e.equals("Montant_Adjuge")) {
                      MontantAdjuge = valeurs[7]; // 9 éme élément
                    } else if (e.equals("Montant_Coupoun")) {
                      MontantCoupoun = valeurs[7]; // 10 éme élément
                    }

                    // test pour le 9er élément de tab5eau
                    if (f.equals("code_ISIN")) {
                      codeISIN = valeurs[8]; // Premier élément
                    }  else if (f.equals("DATE_DEB")) {
                      DateDeb = valeurs[8]; // Troisième élément
                    } else if (f.equals("DUREE_bon")) {
                      DureeBon = valeurs[8]; // Quatrième élément
                    } else if (f.equals("DATE_ECHEANCE")) {
                      DateEcheance = valeurs[8]; // Cinquième élément
                    } else if (f.equals("Code_Soumissionaire")) {
                      CodeSoumissionaire = valeurs[8]; // 6 éme élément
                    } else if (f.equals("MONTANT_Propose_Par_etat")) {
                      MontantProposeParEtat = valeurs[8]; // 7 éme élément
                    } else if (f.equals("Montant_Propose_Par_Soumissionaire")) {
                      MontantProposeParSoumissionaire = valeurs[8]; // 8 éme élément
                    } else if (f.equals("Montant_Adjuge")) {
                      MontantAdjuge = valeurs[8]; // 9 éme élément
                    } else if (f.equals("Montant_Coupoun")) {
                      MontantCoupoun = valeurs[8]; // 10 éme élément
                    }


                    System.out.println(valus[0] + ":" + valeurs[0]);
                    System.out.println(valus[1] + ":" + valeurs[1]);
                    System.out.println(valus[2] + ":" + valeurs[2]);
                    System.out.println(valus[3] + ":" + valeurs[3]);
                    System.out.println(valus[4] + ":" + valeurs[4]);
                    System.out.println(valus[5] + ":" + valeurs[5]);
                    System.out.println(valus[6] + ":" + valeurs[6]);
                    System.out.println(valus[7] + ":" + valeurs[7]);
                    System.out.println(valus[8] + ":" + valeurs[8]);

                    // Appel de la fonction d'insertion avec les valeurs récupérées
                    String insertDette = relationnel.insertIntoDette(
                        codeISIN,
                        DateDeb,
                        DureeBon,
                        DateEcheance,
                        CodeSoumissionaire,
                        MontantProposeParEtat,
                        MontantProposeParSoumissionaire,
                        MontantAdjuge,
                        MontantCoupoun);

                    System.out.println(insertDette);
                  }

                  br3.close(); // Fermer le lecteur de fichier existant

                  // supression de contenu de ficher aprés traiatment
                  try {
                    FileWriter writer1 = new FileWriter(cheminFichier);
                    writer1.write(""); // Écriture d'une chaîne vide pour vider le fichier
                    writer1.close();
                    System.out.println(
                        "Contenu du fichier supprimé avec succès aprés insertion des éléments.");
                  } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println(
                        "Une erreur s'est produite lors de la suppression du contenu du fichier.");
                  }

                } else {
                  System.out.println(
                      "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                }

              } catch (IOException e) {
                e.printStackTrace();
              } finally {
                // Fermeture du BufferedReader dans le bloc finally
                try {
                  if (br3 != null) {
                    br3.close();
                  }
                } catch (IOException e) {
                  e.printStackTrace();
                }
              }
            }




        } else if (parts.length == 14){ //ETL Formulaire
            String part1 = parts[0];
            String part2 = parts[1];
            String part3 = parts[2];
            String part4 = parts[3];
            String part5 = parts[4];
            String part6 = parts[5];
            String part7 = parts[6];
            String part8 = parts[7];
            String part9 = parts[8];
            String part10 = parts[9];
            String part11 = parts[10];
            String part12 = parts[11];
            String part13 = parts[12];
            String part14 = parts[13];
            // System.out.println(part1);
            // System.out.println(part2);
            // System.out.println(part3);
            // System.out.println(part4);
            // System.out.println(part5);
            // System.out.println(part6);
            // System.out.println(part7);
            // System.out.println(part8);
            // System.out.println(part9);
            // System.out.println(part10);
            // System.out.println(part11);
            // System.out.println(part12);
            // System.out.println(part13);
            // System.out.println(part14);

            if(part14.equals("Credits")){

              String insertCredits = relationnel.insertIntoCredit(part1, part2,part3,part4,part5,part6,part7,part8,part9,part10,part11,part12,part13);
              System.out.println(insertCredits);

              // Convertir la chaîne en tableau de bytes
              byte[] userData = insertCredits.toString().getBytes();

              // Récupérer l'adresse IP et le numéro de port du client
              InetAddress clientAddress = receivePacket.getAddress();
              int clientPort = receivePacket.getPort();

              // Créer un DatagramPacket avec les données et les envoyer au client
              DatagramPacket responsePacket = new DatagramPacket(
                  userData,
                  userData.length,
                  clientAddress,
                  clientPort);
              serverSocket.send(responsePacket);
              
            } else if(part14.equals("Depenses")){

              String insertDepenses = relationnel.insertIntoMndat(part1, part2,part3,part4,part5,part6,part7,part8,part9,part10,part11,part12,part13);
              System.out.println(insertDepenses);


              // Convertir la chaîne en tableau de bytes
              byte[] userData = insertDepenses.toString().getBytes();

              // Récupérer l'adresse IP et le numéro de port du client
              InetAddress clientAddress = receivePacket.getAddress();
              int clientPort = receivePacket.getPort();

              // Créer un DatagramPacket avec les données et les envoyer au client
              DatagramPacket responsePacket = new DatagramPacket(
                  userData,
                  userData.length,
                  clientAddress,
                  clientPort);
              serverSocket.send(responsePacket);

            } else if(part14.equals("Recette")){

              String insertRecette = relationnel.insertIntoRecette(part1, part2,part3,part4,part5,part6);
              System.out.println(insertRecette);


              // Convertir la chaîne en tableau de bytes
              byte[] userData = insertRecette.toString().getBytes();

              // Récupérer l'adresse IP et le numéro de port du client
              InetAddress clientAddress = receivePacket.getAddress();
              int clientPort = receivePacket.getPort();

              // Créer un DatagramPacket avec les données et les envoyer au client
              DatagramPacket responsePacket = new DatagramPacket(
                  userData,
                  userData.length,
                  clientAddress,
                  clientPort);
              serverSocket.send(responsePacket);

            } else if(part14.equals("Dette")){
              String insertDette = relationnel.insertIntoDette(part1, part2,part3,part4,part5,part6,part7,part8,part9);
              System.out.println(insertDette);


              // Convertir la chaîne en tableau de bytes
              byte[] userData = insertDette.toString().getBytes();

              // Récupérer l'adresse IP et le numéro de port du client
              InetAddress clientAddress = receivePacket.getAddress();
              int clientPort = receivePacket.getPort();

              // Créer un DatagramPacket avec les données et les envoyer au client
              DatagramPacket responsePacket = new DatagramPacket(
                  userData,
                  userData.length,
                  clientAddress,
                  clientPort);
              serverSocket.send(responsePacket);
            } 

          


        } else if (parts.length == 7) { // requete SQL
          String part3 = parts[2];
          String part4 = parts[3];
          String part5 = parts[4];
          String part6 = parts[5];
          String part7 = parts[6];
          if (part4.equals("Credits")) { // crédits

            if (part5.equals("reel")) { // crédits réels

              if (part6.equals("a")) { // getTotalCreditsAllouePourChaquePortefeuille
                List<String> getTotalCreditsAllouePourChaquePortefeuilleReel = Entrepot
                    .getTotalCreditsAllouePourChaquePortefeuilleReel(part3);

                // Faites quelque chose avec la liste , par exemple affichez-les
                for (String Res : getTotalCreditsAllouePourChaquePortefeuilleReel) {
                  System.out.println(Res);
                }

                // Convertir la liste en une seule chaîne
                StringBuilder resultat = new StringBuilder();
                for (String Res : getTotalCreditsAllouePourChaquePortefeuilleReel) {
                  resultat.append(Res).append("\n");
                }

                // Convertir la chaîne en tableau de bytes
                byte[] userData = resultat.toString().getBytes();

                // Récupérer l'adresse IP et le numéro de port du client
                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();

                // Créer un DatagramPacket avec les données et les envoyer au client
                DatagramPacket responsePacket = new DatagramPacket(
                    userData,
                    userData.length,
                    clientAddress,
                    clientPort);
                serverSocket.send(responsePacket);

              } else if (part6.equals("b")) { // getTotalCreditsAllouePourChaqueProgramme
                List<String> getTotalCreditsAllouePourChaqueProgrammeReel = Entrepot
                    .getTotalCreditsAllouePourChaqueProgrammeReel(part3);

                // Faites quelque chose avec la liste , par exemple affichez-les
                for (String Res : getTotalCreditsAllouePourChaqueProgrammeReel) {
                  System.out.println(Res);
                }

                // Convertir la liste en une seule chaîne
                StringBuilder resultat = new StringBuilder();
                for (String Res : getTotalCreditsAllouePourChaqueProgrammeReel) {
                  resultat.append(Res).append("\n");
                }

                // Convertir la chaîne en tableau de bytes
                byte[] userData = resultat.toString().getBytes();

                // Récupérer l'adresse IP et le numéro de port du client
                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();

                // Créer un DatagramPacket avec les données et les envoyer au client
                DatagramPacket responsePacket = new DatagramPacket(
                    userData,
                    userData.length,
                    clientAddress,
                    clientPort);
                serverSocket.send(responsePacket);

              } else if (part6.equals("c")) { // getTotalCreditsAllouePourChaqueSousProgramme
                List<String> getTotalCreditsAllouePourChaqueSousProgrammeReel = Entrepot
                    .getTotalCreditsAllouePourChaqueSousProgrammeReel(part3);

                // Faites quelque chose avec la liste , par exemple affichez-les
                for (String Res : getTotalCreditsAllouePourChaqueSousProgrammeReel) {
                  System.out.println(Res);
                }

                // Convertir la liste en une seule chaîne
                StringBuilder resultat = new StringBuilder();
                for (String Res : getTotalCreditsAllouePourChaqueSousProgrammeReel) {
                  resultat.append(Res).append("\n");
                }

                // Convertir la chaîne en tableau de bytes
                byte[] userData = resultat.toString().getBytes();

                // Récupérer l'adresse IP et le numéro de port du client
                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();

                // Créer un DatagramPacket avec les données et les envoyer au client
                DatagramPacket responsePacket = new DatagramPacket(
                    userData,
                    userData.length,
                    clientAddress,
                    clientPort);
                serverSocket.send(responsePacket);

              } else if (part6.equals("d")) { // getTotalCreditsAllouePourChaqueTitre
                List<String> getTotalCreditsAllouePourChaqueTitreReel = Entrepot
                    .getTotalCreditsAllouePourChaqueTitreReel(part3);

                // Faites quelque chose avec la liste , par exemple affichez-les
                for (String Res : getTotalCreditsAllouePourChaqueTitreReel) {
                  System.out.println(Res);
                }

                // Convertir la liste en une seule chaîne
                StringBuilder resultat = new StringBuilder();
                for (String Res : getTotalCreditsAllouePourChaqueTitreReel) {
                  resultat.append(Res).append("\n");
                }

                // Convertir la chaîne en tableau de bytes
                byte[] userData = resultat.toString().getBytes();

                // Récupérer l'adresse IP et le numéro de port du client
                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();

                // Créer un DatagramPacket avec les données et les envoyer au client
                DatagramPacket responsePacket = new DatagramPacket(
                    userData,
                    userData.length,
                    clientAddress,
                    clientPort);
                serverSocket.send(responsePacket);

              } else if (part6.equals("e")) { // getTotalCreditsAllouePourChaqueCategorie
                List<String> getTotalCreditsAllouePourChaqueCategorieReel = Entrepot
                    .getTotalCreditsAllouePourChaqueCategorieReel(part3);

                // Faites quelque chose avec la liste , par exemple affichez-les
                for (String Res : getTotalCreditsAllouePourChaqueCategorieReel) {
                  System.out.println(Res);
                }

                // Convertir la liste en une seule chaîne
                StringBuilder resultat = new StringBuilder();
                for (String Res : getTotalCreditsAllouePourChaqueCategorieReel) {
                  resultat.append(Res).append("\n");
                }

                // Convertir la chaîne en tableau de bytes
                byte[] userData = resultat.toString().getBytes();

                // Récupérer l'adresse IP et le numéro de port du client
                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();

                // Créer un DatagramPacket avec les données et les envoyer au client
                DatagramPacket responsePacket = new DatagramPacket(
                    userData,
                    userData.length,
                    clientAddress,
                    clientPort);
                serverSocket.send(responsePacket);

              }

            } else if (part5.equals("prevu")) {// crédits prévu

              if (part6.equals("a")) { // getTotalCreditsAllouePourChaquePortefeuillePrevu

                List<String> getTotalCreditsAllouePourChaquePortefeuilleReel = Entrepot
                    .getTotalCreditsAllouePourChaquePortefeuilleReel(part3);

                if (getTotalCreditsAllouePourChaquePortefeuilleReel.size() > 0) {

                  // Afficher les valeurs réels
                  System.out.println("***** les valeurs reels des crédits Par Portefeuille ****");

                  System.out.println();

                  for (String value : getTotalCreditsAllouePourChaquePortefeuilleReel) {
                    System.out.println(value);
                  }

                  List<String> getTotalCreditsAllouePourChaquePortefeuillePrevu = Entrepot
                      .getTotalCreditsAllouePourChaquePortefeuillePrevu(
                          "ScriptsPython\\Crédits\\TotalCreditsPourChaquePortef.py",
                          getTotalCreditsAllouePourChaquePortefeuilleReel,part7);

                  System.out.println();

                  // Afficher les prédictions
                  System.out.println("***** les valeurs prevus des crédits Par Portefeuille ****");
                  System.out.println();

                  for (String prediction : getTotalCreditsAllouePourChaquePortefeuillePrevu) {
                    System.out.println(prediction);
                  }

                  System.out.println();
                  // Convertir la liste getTotalCreditsAllouePourChaquePortefeuilleReel en une
                  // seule chaîne
                  StringBuilder resReel = new StringBuilder();
                  for (String Res : getTotalCreditsAllouePourChaquePortefeuilleReel) {
                    resReel.append(Res).append("\n");
                  }

                  // Convertir la chaîne getTotalCreditsAllouePourChaquePortefeuilleReel en
                  // tableau de bytes
                  byte[] DataReel = resReel.toString().getBytes();

                  // Récupérer l'adresse IP et le numéro de port du client
                  InetAddress clientAddressReel = receivePacket.getAddress();
                  int clientPortReel = receivePacket.getPort();

                  // Créer un DatagramPacket avec les données et les envoyer au client
                  DatagramPacket responsePacketReel = new DatagramPacket(
                      DataReel,
                      DataReel.length,
                      clientAddressReel,
                      clientPortReel);
                  serverSocket.send(responsePacketReel);

                  // Convertir la liste getTotalCreditsAllouePourChaquePortefeuillePrevu en une
                  // seule chaîne
                  StringBuilder resPrevu = new StringBuilder();
                  for (String Res : getTotalCreditsAllouePourChaquePortefeuillePrevu) {
                    resPrevu.append(Res).append("\n");
                  }

                  // Convertir la chaîne getTotalCreditsAllouePourChaquePortefeuillePrevu en
                  // tableau de bytes
                  byte[] DataPrevu = resPrevu.toString().getBytes();

                  // Récupérer l'adresse IP et le numéro de port du client
                  InetAddress clientAddressPrevu = receivePacket.getAddress();
                  int clientPortPrevu = receivePacket.getPort();

                  // Créer un DatagramPacket avec les données et les envoyer au client
                  DatagramPacket responsePacketPrevu = new DatagramPacket(
                      DataPrevu,
                      DataPrevu.length,
                      clientAddressPrevu,
                      clientPortPrevu);
                  serverSocket.send(responsePacketPrevu);

                }


                System.out.println("fin");




              } else if (part6.equals("b")) { // getTotalCreditsAllouePourChaqueProgrammePrevu

                List<String> getTotalCreditsAllouePourChaqueProgrammeReel = Entrepot
                    .getTotalCreditsAllouePourChaqueProgrammeReel(part3);

                if (getTotalCreditsAllouePourChaqueProgrammeReel.size() > 0) {

                  // Afficher les valeurs réels
                  System.out.println("***** les valeurs reels des crédits Par Programme ****");

                  System.out.println();

                  for (String value : getTotalCreditsAllouePourChaqueProgrammeReel) {
                    System.out.println(value);
                  }

                  List<String> getTotalCreditsAllouePourChaqueProgrammePrevu = Entrepot
                      .getTotalCreditsAllouePourChaqueProgrammePrevu(
                          "ScriptsPython\\Crédits\\TotalCreditsPourChaqueProg.py",
                          getTotalCreditsAllouePourChaqueProgrammeReel,part7);

                  System.out.println();

                  // Afficher les prédictions
                  System.out.println("***** les valeurs prevus des crédits Par Programme ****");
                  System.out.println();

                  for (String prediction : getTotalCreditsAllouePourChaqueProgrammePrevu) {
                    System.out.println(prediction);
                  }

                  System.out.println();

                  // Convertir la liste getTotalCreditsAllouePourChaqueProgrammeReel en une
                  // seule chaîne
                  StringBuilder resReel = new StringBuilder();
                  for (String Res : getTotalCreditsAllouePourChaqueProgrammeReel) {
                    resReel.append(Res).append("\n");
                  }

                  // Convertir la chaîne getTotalCreditsAllouePourChaqueProgrammeReel en
                  // tableau de bytes
                  byte[] DataReel = resReel.toString().getBytes();

                  // Récupérer l'adresse IP et le numéro de port du client
                  InetAddress clientAddressReel = receivePacket.getAddress();
                  int clientPortReel = receivePacket.getPort();

                  // Créer un DatagramPacket avec les données et les envoyer au client
                  DatagramPacket responsePacketReel = new DatagramPacket(
                      DataReel,
                      DataReel.length,
                      clientAddressReel,
                      clientPortReel);
                  serverSocket.send(responsePacketReel);

                  // Convertir la liste getTotalCreditsAllouePourChaqueProgrammePrevu en une
                  // seule chaîne
                  StringBuilder resPrevu = new StringBuilder();
                  for (String Res : getTotalCreditsAllouePourChaqueProgrammePrevu) {
                    resPrevu.append(Res).append("\n");
                  }

                  // Convertir la chaîne getTotalCreditsAllouePourChaqueProgrammePrevu en
                  // tableau de bytes
                  byte[] DataPrevu = resPrevu.toString().getBytes();

                  // Récupérer l'adresse IP et le numéro de port du client
                  InetAddress clientAddressPrevu = receivePacket.getAddress();
                  int clientPortPrevu = receivePacket.getPort();

                  // Créer un DatagramPacket avec les données et les envoyer au client
                  DatagramPacket responsePacketPrevu = new DatagramPacket(
                      DataPrevu,
                      DataPrevu.length,
                      clientAddressPrevu,
                      clientPortPrevu);
                  serverSocket.send(responsePacketPrevu);

                }

                System.out.println("fin");



              }else if (part6.equals("c")) { // getTotalCreditsAllouePourChaqueSousProgrammePrevu

                List<String> getTotalCreditsAllouePourChaqueSousProgrammeReel = Entrepot
                    .getTotalCreditsAllouePourChaqueSousProgrammeReel(part3);

                if (getTotalCreditsAllouePourChaqueSousProgrammeReel.size() > 0) {

                  // Afficher les valeurs réels
                  System.out.println("***** les valeurs reels des crédits Par SousProgramme ****");

                  System.out.println();

                  for (String value : getTotalCreditsAllouePourChaqueSousProgrammeReel) {
                    System.out.println(value);
                  }

                  List<String> getTotalCreditsAllouePourChaqueSousProgrammePrevu = Entrepot
                      .getTotalCreditsAllouePourChaqueSousProgrammePrevu(
                          "ScriptsPython\\Crédits\\TotalCreditsPourChaqueSousProg.py",
                          getTotalCreditsAllouePourChaqueSousProgrammeReel,part7);

                  System.out.println();

                  // Afficher les prédictions
                  System.out.println("***** les valeurs prevus des crédits Par SousProgramme ****");
                  System.out.println();

                  for (String prediction : getTotalCreditsAllouePourChaqueSousProgrammePrevu) {
                    System.out.println(prediction);
                  }

                  System.out.println();

                  // Convertir la liste getTotalCreditsAllouePourChaqueSousProgrammeReel en une
                  // seule chaîne
                  StringBuilder resReel = new StringBuilder();
                  for (String Res : getTotalCreditsAllouePourChaqueSousProgrammeReel) {
                    resReel.append(Res).append("\n");
                  }

                  // Convertir la chaîne getTotalCreditsAllouePourChaqueSousProgrammeReel en
                  // tableau de bytes
                  byte[] DataReel = resReel.toString().getBytes();

                  // Récupérer l'adresse IP et le numéro de port du client
                  InetAddress clientAddressReel = receivePacket.getAddress();
                  int clientPortReel = receivePacket.getPort();

                  // Créer un DatagramPacket avec les données et les envoyer au client
                  DatagramPacket responsePacketReel = new DatagramPacket(
                      DataReel,
                      DataReel.length,
                      clientAddressReel,
                      clientPortReel);
                  serverSocket.send(responsePacketReel);

                  // Convertir la liste getTotalCreditsAllouePourChaqueSousProgrammePrevu en une
                  // seule chaîne
                  StringBuilder resPrevu = new StringBuilder();
                  for (String Res : getTotalCreditsAllouePourChaqueSousProgrammePrevu) {
                    resPrevu.append(Res).append("\n");
                  }

                  // Convertir la chaîne getTotalCreditsAllouePourChaqueSousProgrammePrevu en
                  // tableau de bytes
                  byte[] DataPrevu = resPrevu.toString().getBytes();

                  // Récupérer l'adresse IP et le numéro de port du client
                  InetAddress clientAddressPrevu = receivePacket.getAddress();
                  int clientPortPrevu = receivePacket.getPort();

                  // Créer un DatagramPacket avec les données et les envoyer au client
                  DatagramPacket responsePacketPrevu = new DatagramPacket(
                      DataPrevu,
                      DataPrevu.length,
                      clientAddressPrevu,
                      clientPortPrevu);
                  serverSocket.send(responsePacketPrevu);

                }



                System.out.println("fin");



              }else if (part6.equals("d")) { // getTotalCreditsAllouePourChaqueTitrePrevu

                List<String> getTotalCreditsAllouePourChaqueTitreReel = Entrepot
                    .getTotalCreditsAllouePourChaqueTitreReel(part3);

                if (getTotalCreditsAllouePourChaqueTitreReel.size() > 0) {

                  // Afficher les valeurs réels
                  System.out.println("***** les valeurs reels des crédits Par Titre ****");

                  System.out.println();

                  for (String value : getTotalCreditsAllouePourChaqueTitreReel) {
                    System.out.println(value);
                  }

                  List<String> getTotalCreditsAllouePourChaqueTitrePrevu = Entrepot
                      .getTotalCreditsAllouePourChaqueTitrePrevu(
                          "ScriptsPython\\Crédits\\TotalCreditsPourChaqueTitre.py",
                          getTotalCreditsAllouePourChaqueTitreReel,part7);

                  System.out.println();

                  // Afficher les prédictions
                  System.out.println("***** les valeurs prevus des crédits Par Titre ****");
                  System.out.println();

                  for (String prediction : getTotalCreditsAllouePourChaqueTitrePrevu) {
                    System.out.println(prediction);
                  }

                  System.out.println();

                  // Convertir la liste getTotalCreditsAllouePourChaqueTitreReel en une
                  // seule chaîne
                  StringBuilder resReel = new StringBuilder();
                  for (String Res : getTotalCreditsAllouePourChaqueTitreReel) {
                    resReel.append(Res).append("\n");
                  }

                  // Convertir la chaîne getTotalCreditsAllouePourChaqueTitreReel en
                  // tableau de bytes
                  byte[] DataReel = resReel.toString().getBytes();

                  // Récupérer l'adresse IP et le numéro de port du client
                  InetAddress clientAddressReel = receivePacket.getAddress();
                  int clientPortReel = receivePacket.getPort();

                  // Créer un DatagramPacket avec les données et les envoyer au client
                  DatagramPacket responsePacketReel = new DatagramPacket(
                      DataReel,
                      DataReel.length,
                      clientAddressReel,
                      clientPortReel);
                  serverSocket.send(responsePacketReel);

                  // Convertir la liste getTotalCreditsAllouePourChaqueTitrePrevu en une
                  // seule chaîne
                  StringBuilder resPrevu = new StringBuilder();
                  for (String Res : getTotalCreditsAllouePourChaqueTitrePrevu) {
                    resPrevu.append(Res).append("\n");
                  }

                  // Convertir la chaîne getTotalCreditsAllouePourChaqueTitrePrevu en
                  // tableau de bytes
                  byte[] DataPrevu = resPrevu.toString().getBytes();

                  // Récupérer l'adresse IP et le numéro de port du client
                  InetAddress clientAddressPrevu = receivePacket.getAddress();
                  int clientPortPrevu = receivePacket.getPort();

                  // Créer un DatagramPacket avec les données et les envoyer au client
                  DatagramPacket responsePacketPrevu = new DatagramPacket(
                      DataPrevu,
                      DataPrevu.length,
                      clientAddressPrevu,
                      clientPortPrevu);
                  serverSocket.send(responsePacketPrevu);

                }



                System.out.println("fin");



              }else if (part6.equals("e")) {  // getTotalCreditsAllouePourChaqueCategoriePrevu
                
                List<String> getTotalCreditsAllouePourChaqueCategorieReel = Entrepot
                    .getTotalCreditsAllouePourChaqueCategorieReel(part3);

                if (getTotalCreditsAllouePourChaqueCategorieReel.size() > 0) {

                  // Afficher les valeurs réels
                  System.out.println("***** les valeurs reels des crédits par Catégorie ****");

                  System.out.println();

                  for (String value : getTotalCreditsAllouePourChaqueCategorieReel) {
                    System.out.println(value);
                  }

                  List<String> getTotalCreditsAllouePourChaqueCategoriePrevu = Entrepot
                      .getTotalCreditsAllouePourChaqueCategoriePrevu(
                          "ScriptsPython\\Crédits\\TotalCreditsPourChaqueCateg.py",
                          getTotalCreditsAllouePourChaqueCategorieReel,part7);

                  System.out.println();

                  // Afficher les prédictions
                  System.out.println("***** les valeurs prevus des crédits par Catégorie ****");
                  System.out.println();

                  for (String prediction : getTotalCreditsAllouePourChaqueCategoriePrevu) {
                    System.out.println(prediction);
                  }

                  System.out.println();

                  // Convertir la liste getTotalCreditsAllouePourChaqueCategorieReel en une
                  // seule chaîne
                  StringBuilder resReel = new StringBuilder();
                  for (String Res : getTotalCreditsAllouePourChaqueCategorieReel) {
                    resReel.append(Res).append("\n");
                  }

                  // Convertir la chaîne getTotalCreditsAllouePourChaqueCategorieReel en
                  // tableau de bytes
                  byte[] DataReel = resReel.toString().getBytes();

                  // Récupérer l'adresse IP et le numéro de port du client
                  InetAddress clientAddressReel = receivePacket.getAddress();
                  int clientPortReel = receivePacket.getPort();

                  // Créer un DatagramPacket avec les données et les envoyer au client
                  DatagramPacket responsePacketReel = new DatagramPacket(
                      DataReel,
                      DataReel.length,
                      clientAddressReel,
                      clientPortReel);
                  serverSocket.send(responsePacketReel);

                  // Convertir la liste getTotalCreditsAllouePourChaqueCategoriePrevu en une
                  // seule chaîne
                  StringBuilder resPrevu = new StringBuilder();
                  for (String Res : getTotalCreditsAllouePourChaqueCategoriePrevu) {
                    resPrevu.append(Res).append("\n");
                  }

                  // Convertir la chaîne getTotalCreditsAllouePourChaqueCategoriePrevu en
                  // tableau de bytes
                  byte[] DataPrevu = resPrevu.toString().getBytes();

                  // Récupérer l'adresse IP et le numéro de port du client
                  InetAddress clientAddressPrevu = receivePacket.getAddress();
                  int clientPortPrevu = receivePacket.getPort();

                  // Créer un DatagramPacket avec les données et les envoyer au client
                  DatagramPacket responsePacketPrevu = new DatagramPacket(
                      DataPrevu,
                      DataPrevu.length,
                      clientAddressPrevu,
                      clientPortPrevu);
                  serverSocket.send(responsePacketPrevu);

                }

                System.out.println("fin");

              } 

            }

          } else if (part4.equals("Depenses")) { // dépenses


            if (part5.equals("reel")) { // dépenses réels
              if (part6.equals("a")) { // getTotalMontantdepensePourChaquePortefeuille
                List<String> getTotalMontantdepensePourChaquePortefeuilleReel = Entrepot
                    .getTotalMontantdepensePourChaquePortefeuilleReel(part3);

                // Faites quelque chose avec la liste , par exemple affichez-les
                for (String Res : getTotalMontantdepensePourChaquePortefeuilleReel) {
                  System.out.println(Res);
                }

                // Convertir la liste en une seule chaîne
                StringBuilder resultat = new StringBuilder();
                for (String Res : getTotalMontantdepensePourChaquePortefeuilleReel) {
                  resultat.append(Res).append("\n");
                }

                // Convertir la chaîne en tableau de bytes
                byte[] userData = resultat.toString().getBytes();

                // Récupérer l'adresse IP et le numéro de port du client
                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();

                // Créer un DatagramPacket avec les données et les envoyer au client
                DatagramPacket responsePacket = new DatagramPacket(
                    userData,
                    userData.length,
                    clientAddress,
                    clientPort);
                serverSocket.send(responsePacket);

              } else if (part6.equals("b")) { // getTotalMontantdepensePourChaqueProgramme
                List<String> getTotalMontantdepensePourChaqueProgrammeReel = Entrepot
                    .getTotalMontantdepensePourChaqueProgrammeReel(part3);

                // Faites quelque chose avec la liste , par exemple affichez-les
                for (String Res : getTotalMontantdepensePourChaqueProgrammeReel) {
                  System.out.println(Res);
                }

                // Convertir la liste en une seule chaîne
                StringBuilder resultat = new StringBuilder();
                for (String Res : getTotalMontantdepensePourChaqueProgrammeReel) {
                  resultat.append(Res).append("\n");
                }

                // Convertir la chaîne en tableau de bytes
                byte[] userData = resultat.toString().getBytes();

                // Récupérer l'adresse IP et le numéro de port du client
                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();

                // Créer un DatagramPacket avec les données et les envoyer au client
                DatagramPacket responsePacket = new DatagramPacket(
                    userData,
                    userData.length,
                    clientAddress,
                    clientPort);
                serverSocket.send(responsePacket);

              } else if (part6.equals("c")) { // getTotalMontantdepensePourChaqueSousProgramme
                List<String> getTotalMontantdepensePourChaqueSousProgrammeReel = Entrepot
                    .getTotalMontantdepensePourChaqueSousProgrammeReel(part3);

                // Faites quelque chose avec la liste , par exemple affichez-les
                for (String Res : getTotalMontantdepensePourChaqueSousProgrammeReel) {
                  System.out.println(Res);
                }

                // Convertir la liste en une seule chaîne
                StringBuilder resultat = new StringBuilder();
                for (String Res : getTotalMontantdepensePourChaqueSousProgrammeReel) {
                  resultat.append(Res).append("\n");
                }

                // Convertir la chaîne en tableau de bytes
                byte[] userData = resultat.toString().getBytes();

                // Récupérer l'adresse IP et le numéro de port du client
                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();

                // Créer un DatagramPacket avec les données et les envoyer au client
                DatagramPacket responsePacket = new DatagramPacket(
                    userData,
                    userData.length,
                    clientAddress,
                    clientPort);
                serverSocket.send(responsePacket);

              } else if (part6.equals("d")) { // getTotalMontantdepensePourChaqueTitre
                List<String> getTotalMontantdepensePourChaqueTitreReel = Entrepot
                    .getTotalMontantdepensePourChaqueTitreReel(part3);

                // Faites quelque chose avec la liste , par exemple affichez-les
                for (String Res : getTotalMontantdepensePourChaqueTitreReel) {
                  System.out.println(Res);
                }

                // Convertir la liste en une seule chaîne
                StringBuilder resultat = new StringBuilder();
                for (String Res : getTotalMontantdepensePourChaqueTitreReel) {
                  resultat.append(Res).append("\n");
                }

                // Convertir la chaîne en tableau de bytes
                byte[] userData = resultat.toString().getBytes();

                // Récupérer l'adresse IP et le numéro de port du client
                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();

                // Créer un DatagramPacket avec les données et les envoyer au client
                DatagramPacket responsePacket = new DatagramPacket(
                    userData,
                    userData.length,
                    clientAddress,
                    clientPort);
                serverSocket.send(responsePacket);

              } else if (part6.equals("e")) { // getTotalMontantdepensePourChaqueCategorie
                List<String> getTotalMontantdepensePourChaqueCategorieReel = Entrepot
                    .getTotalMontantdepensePourChaqueCategorieReel(part3);

                // Faites quelque chose avec la liste , par exemple affichez-les
                for (String Res : getTotalMontantdepensePourChaqueCategorieReel) {
                  System.out.println(Res);
                }

                // Convertir la liste en une seule chaîne
                StringBuilder resultat = new StringBuilder();
                for (String Res : getTotalMontantdepensePourChaqueCategorieReel) {
                  resultat.append(Res).append("\n");
                }

                // Convertir la chaîne en tableau de bytes
                byte[] userData = resultat.toString().getBytes();

                // Récupérer l'adresse IP et le numéro de port du client
                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();

                // Créer un DatagramPacket avec les données et les envoyer au client
                DatagramPacket responsePacket = new DatagramPacket(
                    userData,
                    userData.length,
                    clientAddress,
                    clientPort);
                serverSocket.send(responsePacket);

              }

            } else if (part5.equals("prevu")) { // dépenses prevu



              if (part6.equals("a")) {  // getAllTotalMontantdepensePourChaquePortefeuillePrevu
                
                List<String> getAllTotalMontantdepensePourChaquePortefeuilleReel = Entrepot
                    .getAllTotalMontantdepensePourChaquePortefeuilleReel(part3);

                if (getAllTotalMontantdepensePourChaquePortefeuilleReel.size() > 0) {

                  // Afficher les valeurs réels
                  System.out.println("***** les valeurs reels des dépenses par Portefeuille ****");

                  System.out.println();

                  for (String value : getAllTotalMontantdepensePourChaquePortefeuilleReel) {
                    System.out.println(value);
                  }

                  List<String> getAllTotalMontantdepensePourChaquePortefeuillePrevu = Entrepot
                      .getAllTotalMontantdepensePourChaquePortefeuillePrevu(
                          "ScriptsPython\\Dépenses\\TotalAllDepPourChaquePortef.py",
                          getAllTotalMontantdepensePourChaquePortefeuilleReel,part7);

                  System.out.println();

                  // Afficher les prédictions
                  System.out.println("***** les valeurs prevus des dépenses par Portefeuille ****");
                  System.out.println();

                  for (String prediction : getAllTotalMontantdepensePourChaquePortefeuillePrevu) {
                    System.out.println(prediction);
                  }

                  System.out.println();

                  // Convertir la liste getAllTotalMontantdepensePourChaquePortefeuilleReel en une
                  // seule chaîne
                  StringBuilder resReel = new StringBuilder();
                  for (String Res : getAllTotalMontantdepensePourChaquePortefeuilleReel) {
                    resReel.append(Res).append("\n");
                  }

                  // Convertir la chaîne getAllTotalMontantdepensePourChaquePortefeuilleReel en
                  // tableau de bytes
                  byte[] DataReel = resReel.toString().getBytes();

                  // Récupérer l'adresse IP et le numéro de port du client
                  InetAddress clientAddressReel = receivePacket.getAddress();
                  int clientPortReel = receivePacket.getPort();

                  // Créer un DatagramPacket avec les données et les envoyer au client
                  DatagramPacket responsePacketReel = new DatagramPacket(
                      DataReel,
                      DataReel.length,
                      clientAddressReel,
                      clientPortReel);
                  serverSocket.send(responsePacketReel);

                  // Convertir la liste getAllTotalMontantdepensePourChaquePortefeuillePrevu en une
                  // seule chaîne
                  StringBuilder resPrevu = new StringBuilder();
                  for (String Res : getAllTotalMontantdepensePourChaquePortefeuillePrevu) {
                    resPrevu.append(Res).append("\n");
                  }

                  // Convertir la chaîne getAllTotalMontantdepensePourChaquePortefeuillePrevu en
                  // tableau de bytes
                  byte[] DataPrevu = resPrevu.toString().getBytes();

                  // Récupérer l'adresse IP et le numéro de port du client
                  InetAddress clientAddressPrevu = receivePacket.getAddress();
                  int clientPortPrevu = receivePacket.getPort();

                  // Créer un DatagramPacket avec les données et les envoyer au client
                  DatagramPacket responsePacketPrevu = new DatagramPacket(
                      DataPrevu,
                      DataPrevu.length,
                      clientAddressPrevu,
                      clientPortPrevu);
                  serverSocket.send(responsePacketPrevu);

                }

                System.out.println("fin");


              } else if (part6.equals("b")) { // getAllTotalMontantdepensePourChaqueProgrammeReel

                List<String> getAllTotalMontantdepensePourChaqueProgrammeReel = Entrepot
                    .getAllTotalMontantdepensePourChaqueProgrammeReel(part3);

                if (getAllTotalMontantdepensePourChaqueProgrammeReel.size() > 0) {

                  // Afficher les valeurs réels
                  System.out.println("***** les valeurs reels des dépenses par Programme ****");

                  System.out.println();

                  for (String value : getAllTotalMontantdepensePourChaqueProgrammeReel) {
                    System.out.println(value);
                  }

                  List<String> getAllTotalMontantdepensePourChaqueProgrammePrevu = Entrepot
                      .getAllTotalMontantdepensePourChaqueProgrammePrevu(
                          "ScriptsPython\\Dépenses\\TotalAllDepPourChaqueProg.py",
                          getAllTotalMontantdepensePourChaqueProgrammeReel,part7);

                  System.out.println();

                  // Afficher les prédictions
                  System.out.println("***** les valeurs prevus des dépenses par Programme ****");
                  System.out.println();

                  for (String prediction : getAllTotalMontantdepensePourChaqueProgrammePrevu) {
                    System.out.println(prediction);
                  }

                  System.out.println();

                  // Convertir la liste getAllTotalMontantdepensePourChaqueProgrammeReel en une
                  // seule chaîne
                  StringBuilder resReel = new StringBuilder();
                  for (String Res : getAllTotalMontantdepensePourChaqueProgrammeReel) {
                    resReel.append(Res).append("\n");
                  }

                  // Convertir la chaîne getAllTotalMontantdepensePourChaqueProgrammeReel en
                  // tableau de bytes
                  byte[] DataReel = resReel.toString().getBytes();

                  // Récupérer l'adresse IP et le numéro de port du client
                  InetAddress clientAddressReel = receivePacket.getAddress();
                  int clientPortReel = receivePacket.getPort();

                  // Créer un DatagramPacket avec les données et les envoyer au client
                  DatagramPacket responsePacketReel = new DatagramPacket(
                      DataReel,
                      DataReel.length,
                      clientAddressReel,
                      clientPortReel);
                  serverSocket.send(responsePacketReel);

                  // Convertir la liste getAllTotalMontantdepensePourChaqueProgrammePrevu en
                  // une
                  // seule chaîne
                  StringBuilder resPrevu = new StringBuilder();
                  for (String Res : getAllTotalMontantdepensePourChaqueProgrammePrevu) {
                    resPrevu.append(Res).append("\n");
                  }

                  // Convertir la chaîne getAllTotalMontantdepensePourChaqueProgrammePrevu en
                  // tableau de bytes
                  byte[] DataPrevu = resPrevu.toString().getBytes();

                  // Récupérer l'adresse IP et le numéro de port du client
                  InetAddress clientAddressPrevu = receivePacket.getAddress();
                  int clientPortPrevu = receivePacket.getPort();

                  // Créer un DatagramPacket avec les données et les envoyer au client
                  DatagramPacket responsePacketPrevu = new DatagramPacket(
                      DataPrevu,
                      DataPrevu.length,
                      clientAddressPrevu,
                      clientPortPrevu);
                  serverSocket.send(responsePacketPrevu);

                }

                System.out.println("fin");

              } else if (part6.equals("c")) { // getAllTotalMontantdepensePourChaqueSousProgrammePrevu

                List<String> getAllTotalMontantdepensePourChaqueSousProgrammeReel = Entrepot
                    .getAllTotalMontantdepensePourChaqueSousProgrammeReel(part3);

                if (getAllTotalMontantdepensePourChaqueSousProgrammeReel.size() > 0) {

                  // Afficher les valeurs réels
                  System.out.println("***** les valeurs reels des dépenses par SousProgramme ****");

                  System.out.println();

                  for (String value : getAllTotalMontantdepensePourChaqueSousProgrammeReel) {
                    System.out.println(value);
                  }

                  List<String> getAllTotalMontantdepensePourChaqueSousProgrammePrevu = Entrepot
                      .getAllTotalMontantdepensePourChaqueProgrammePrevu(
                          "ScriptsPython\\Dépenses\\TotalAllDepPourChaqueSousProg.py",
                          getAllTotalMontantdepensePourChaqueSousProgrammeReel,part7);

                  System.out.println();

                  // Afficher les prédictions
                  System.out.println("***** les valeurs prevus des dépenses par SousProgramme ****");
                  System.out.println();

                  for (String prediction : getAllTotalMontantdepensePourChaqueSousProgrammePrevu) {
                    System.out.println(prediction);
                  }

                  System.out.println();

                  // Convertir la liste getAllTotalMontantdepensePourChaqueSousProgrammeReel en une
                  // seule chaîne
                  StringBuilder resReel = new StringBuilder();
                  for (String Res : getAllTotalMontantdepensePourChaqueSousProgrammeReel) {
                    resReel.append(Res).append("\n");
                  }

                  // Convertir la chaîne getAllTotalMontantdepensePourChaqueSousProgrammeReel en
                  // tableau de bytes
                  byte[] DataReel = resReel.toString().getBytes();

                  // Récupérer l'adresse IP et le numéro de port du client
                  InetAddress clientAddressReel = receivePacket.getAddress();
                  int clientPortReel = receivePacket.getPort();

                  // Créer un DatagramPacket avec les données et les envoyer au client
                  DatagramPacket responsePacketReel = new DatagramPacket(
                      DataReel,
                      DataReel.length,
                      clientAddressReel,
                      clientPortReel);
                  serverSocket.send(responsePacketReel);

                  // Convertir la liste getAllTotalMontantdepensePourChaqueSousProgrammePrevu en
                  // une
                  // seule chaîne
                  StringBuilder resPrevu = new StringBuilder();
                  for (String Res : getAllTotalMontantdepensePourChaqueSousProgrammePrevu) {
                    resPrevu.append(Res).append("\n");
                  }

                  // Convertir la chaîne getAllTotalMontantdepensePourChaqueSousProgrammePrevu en
                  // tableau de bytes
                  byte[] DataPrevu = resPrevu.toString().getBytes();

                  // Récupérer l'adresse IP et le numéro de port du client
                  InetAddress clientAddressPrevu = receivePacket.getAddress();
                  int clientPortPrevu = receivePacket.getPort();

                  // Créer un DatagramPacket avec les données et les envoyer au client
                  DatagramPacket responsePacketPrevu = new DatagramPacket(
                      DataPrevu,
                      DataPrevu.length,
                      clientAddressPrevu,
                      clientPortPrevu);
                  serverSocket.send(responsePacketPrevu);

                }

                System.out.println("fin");

              } else if (part6.equals("d")) { // getAllTotalMontantdepensePourTitrePrevu

                List<String> getAllTotalMontantdepensePourTitreReel = Entrepot
                    .getAllTotalMontantdepensePourTitreReel(part3);

                if (getAllTotalMontantdepensePourTitreReel.size() > 0) {

                  // Afficher les valeurs réels
                  System.out.println("***** les valeurs reels des dépenses par Titre ****");

                  System.out.println();

                  for (String value : getAllTotalMontantdepensePourTitreReel) {
                    System.out.println(value);
                  }

                  List<String> getAllTotalMontantdepensePourTitrePrevu = Entrepot
                      .getAllTotalMontantdepensePourChaqueProgrammePrevu(
                          "ScriptsPython\\Dépenses\\TotalAllDepPourChaqueTitre.py",
                          getAllTotalMontantdepensePourTitreReel,part7);

                  System.out.println();

                  // Afficher les prédictions
                  System.out.println("***** les valeurs prevus des dépenses par SousProgramme ****");
                  System.out.println();

                  for (String prediction : getAllTotalMontantdepensePourTitrePrevu) {
                    System.out.println(prediction);
                  }

                  System.out.println();

                  // Convertir la liste getAllTotalMontantdepensePourTitreReel en une
                  // seule chaîne
                  StringBuilder resReel = new StringBuilder();
                  for (String Res : getAllTotalMontantdepensePourTitreReel) {
                    resReel.append(Res).append("\n");
                  }

                  // Convertir la chaîne getAllTotalMontantdepensePourTitreReel en
                  // tableau de bytes
                  byte[] DataReel = resReel.toString().getBytes();

                  // Récupérer l'adresse IP et le numéro de port du client
                  InetAddress clientAddressReel = receivePacket.getAddress();
                  int clientPortReel = receivePacket.getPort();

                  // Créer un DatagramPacket avec les données et les envoyer au client
                  DatagramPacket responsePacketReel = new DatagramPacket(
                      DataReel,
                      DataReel.length,
                      clientAddressReel,
                      clientPortReel);
                  serverSocket.send(responsePacketReel);

                  // Convertir la liste getAllTotalMontantdepensePourTitrePrevu en
                  // une
                  // seule chaîne
                  StringBuilder resPrevu = new StringBuilder();
                  for (String Res : getAllTotalMontantdepensePourTitrePrevu) {
                    resPrevu.append(Res).append("\n");
                  }

                  // Convertir la chaîne getAllTotalMontantdepensePourTitrePrevu en
                  // tableau de bytes
                  byte[] DataPrevu = resPrevu.toString().getBytes();

                  // Récupérer l'adresse IP et le numéro de port du client
                  InetAddress clientAddressPrevu = receivePacket.getAddress();
                  int clientPortPrevu = receivePacket.getPort();

                  // Créer un DatagramPacket avec les données et les envoyer au client
                  DatagramPacket responsePacketPrevu = new DatagramPacket(
                      DataPrevu,
                      DataPrevu.length,
                      clientAddressPrevu,
                      clientPortPrevu);
                  serverSocket.send(responsePacketPrevu);

                }

                System.out.println("fin");

              } else if (part6.equals("e")) { // getAllTotalMontantdepensePourCategoriePrevu

                List<String> getAllTotalMontantdepensePourCategorieReel = Entrepot
                    .getAllTotalMontantdepensePourCategorieReel(part3);

                if (getAllTotalMontantdepensePourCategorieReel.size() > 0) {

                  // Afficher les valeurs réels
                  System.out.println("***** les valeurs reels des dépenses par Categorie ****");

                  System.out.println();

                  for (String value : getAllTotalMontantdepensePourCategorieReel) {
                    System.out.println(value);
                  }

                  List<String> getAllTotalMontantdepensePourCategoriePrevu = Entrepot
                      .getAllTotalMontantdepensePourCategoriePrevu(
                          "ScriptsPython\\Dépenses\\TotalAllDepPourChaqueCateg.py",
                          getAllTotalMontantdepensePourCategorieReel,part7);

                  System.out.println();

                  // Afficher les prédictions
                  System.out.println("***** les valeurs prevus des dépenses par Categorie ****");
                  System.out.println();

                  for (String prediction : getAllTotalMontantdepensePourCategoriePrevu) {
                    System.out.println(prediction);
                  }

                  System.out.println();

                  // Convertir la liste getAllTotalMontantdepensePourCategorieReel en une
                  // seule chaîne
                  StringBuilder resReel = new StringBuilder();
                  for (String Res : getAllTotalMontantdepensePourCategorieReel) {
                    resReel.append(Res).append("\n");
                  }

                  // Convertir la chaîne getAllTotalMontantdepensePourCategorieReel en
                  // tableau de bytes
                  byte[] DataReel = resReel.toString().getBytes();

                  // Récupérer l'adresse IP et le numéro de port du client
                  InetAddress clientAddressReel = receivePacket.getAddress();
                  int clientPortReel = receivePacket.getPort();

                  // Créer un DatagramPacket avec les données et les envoyer au client
                  DatagramPacket responsePacketReel = new DatagramPacket(
                      DataReel,
                      DataReel.length,
                      clientAddressReel,
                      clientPortReel);
                  serverSocket.send(responsePacketReel);

                  // Convertir la liste getAllTotalMontantdepensePourCategoriePrevu en
                  // une
                  // seule chaîne
                  StringBuilder resPrevu = new StringBuilder();
                  for (String Res : getAllTotalMontantdepensePourCategoriePrevu) {
                    resPrevu.append(Res).append("\n");
                  }

                  // Convertir la chaîne getAllTotalMontantdepensePourCategoriePrevu en
                  // tableau de bytes
                  byte[] DataPrevu = resPrevu.toString().getBytes();

                  // Récupérer l'adresse IP et le numéro de port du client
                  InetAddress clientAddressPrevu = receivePacket.getAddress();
                  int clientPortPrevu = receivePacket.getPort();

                  // Créer un DatagramPacket avec les données et les envoyer au client
                  DatagramPacket responsePacketPrevu = new DatagramPacket(
                      DataPrevu,
                      DataPrevu.length,
                      clientAddressPrevu,
                      clientPortPrevu);
                  serverSocket.send(responsePacketPrevu);

                }

                System.out.println("fin");

              }
            } 


          } else if (part4.equals("Recette")) { // Recette
            if (part5.equals("reel")) { // Recette réels

              if (part6.equals("a")) { // getTotalMontantPourChaqueCompteReel
                List<String> getTotalMontantPourChaqueCompteReel = Entrepot
                    .getTotalMontantPourChaqueCompteReel(part3);

                // Faites quelque chose avec la liste , par exemple affichez-les
                for (String Res : getTotalMontantPourChaqueCompteReel) {
                  System.out.println(Res);
                }

                // Convertir la liste en une seule chaîne
                StringBuilder resultat = new StringBuilder();
                for (String Res : getTotalMontantPourChaqueCompteReel) {
                  resultat.append(Res).append("\n");
                }

                // Convertir la chaîne en tableau de bytes
                byte[] userData = resultat.toString().getBytes();

                // Récupérer l'adresse IP et le numéro de port du client
                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();

                // Créer un DatagramPacket avec les données et les envoyer au client
                DatagramPacket responsePacket = new DatagramPacket(
                    userData,
                    userData.length,
                    clientAddress,
                    clientPort);
                serverSocket.send(responsePacket);

              }else if (part6.equals("b")) { // getTotalMontantPourChaquePosteComptableReel
                List<String> getTotalMontantPourChaquePosteComptableReel = Entrepot
                    .getTotalMontantPourChaquePosteComptableReel(part3);

                // Faites quelque chose avec la liste , par exemple affichez-les
                for (String Res : getTotalMontantPourChaquePosteComptableReel) {
                  System.out.println(Res);
                }

                // Convertir la liste en une seule chaîne
                StringBuilder resultat = new StringBuilder();
                for (String Res : getTotalMontantPourChaquePosteComptableReel) {
                  resultat.append(Res).append("\n");
                }

                // Convertir la chaîne en tableau de bytes
                byte[] userData = resultat.toString().getBytes();

                // Récupérer l'adresse IP et le numéro de port du client
                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();

                // Créer un DatagramPacket avec les données et les envoyer au client
                DatagramPacket responsePacket = new DatagramPacket(
                    userData,
                    userData.length,
                    clientAddress,
                    clientPort);
                serverSocket.send(responsePacket);

              }

            }else if (part5.equals("prevu")) {// Recette prévu

              if (part6.equals("a")) { // getTotalMontantPourChaqueComptePrevu

                List<String> getTotalMontantPourChaqueCompteReel = Entrepot
                    .getTotalMontantPourChaqueCompteReel(part3);

                if (getTotalMontantPourChaqueCompteReel.size() > 0) {

                  // Afficher les valeurs réels
                  System.out.println("***** les valeurs reels des Recettes par Compte ****");

                  System.out.println();

                  for (String value : getTotalMontantPourChaqueCompteReel) {
                    System.out.println(value);
                  }

                  List<String> getTotalMontantPourChaqueComptePrevu = Entrepot
                      .getTotalMontantPourChaqueComptePrevu(
                          "ScriptsPython\\Recettes\\TotalCreditsPourChaqueCompte.py",
                          getTotalMontantPourChaqueCompteReel,part7);

                  System.out.println();

                  // Afficher les prédictions
                  System.out.println("***** les valeurs prevus des Recettes par Compte ****");
                  System.out.println();

                  for (String prediction : getTotalMontantPourChaqueComptePrevu) {
                    System.out.println(prediction);
                  }

                  System.out.println();

                  // Convertir la liste getTotalMontantPourChaqueCompteReel en une
                  // seule chaîne
                  StringBuilder resReel = new StringBuilder();
                  for (String Res : getTotalMontantPourChaqueCompteReel) {
                    resReel.append(Res).append("\n");
                  }

                  // Convertir la chaîne getTotalMontantPourChaqueCompteReel en
                  // tableau de bytes
                  byte[] DataReel = resReel.toString().getBytes();

                  // Récupérer l'adresse IP et le numéro de port du client
                  InetAddress clientAddressReel = receivePacket.getAddress();
                  int clientPortReel = receivePacket.getPort();

                  // Créer un DatagramPacket avec les données et les envoyer au client
                  DatagramPacket responsePacketReel = new DatagramPacket(
                      DataReel,
                      DataReel.length,
                      clientAddressReel,
                      clientPortReel);
                  serverSocket.send(responsePacketReel);

                  // Convertir la liste getTotalMontantPourChaqueComptePrevu en
                  // une
                  // seule chaîne
                  StringBuilder resPrevu = new StringBuilder();
                  for (String Res : getTotalMontantPourChaqueComptePrevu) {
                    resPrevu.append(Res).append("\n");
                  }

                  // Convertir la chaîne getTotalMontantPourChaqueComptePrevu en
                  // tableau de bytes
                  byte[] DataPrevu = resPrevu.toString().getBytes();

                  // Récupérer l'adresse IP et le numéro de port du client
                  InetAddress clientAddressPrevu = receivePacket.getAddress();
                  int clientPortPrevu = receivePacket.getPort();

                  // Créer un DatagramPacket avec les données et les envoyer au client
                  DatagramPacket responsePacketPrevu = new DatagramPacket(
                      DataPrevu,
                      DataPrevu.length,
                      clientAddressPrevu,
                      clientPortPrevu);
                  serverSocket.send(responsePacketPrevu);

                }

                System.out.println("fin");

              }else if (part6.equals("b")) { // getTotalMontantPourChaquePosteComptablePrevu

                List<String> getTotalMontantPourChaquePosteComptableReel = Entrepot
                    .getTotalMontantPourChaquePosteComptableReel(part3);

                if (getTotalMontantPourChaquePosteComptableReel.size() > 0) {

                  // Afficher les valeurs réels
                  System.out.println("***** les valeurs reels des Recettes par PosteComptable ****");

                  System.out.println();

                  for (String value : getTotalMontantPourChaquePosteComptableReel) {
                    System.out.println(value);
                  }

                  List<String> getTotalMontantPourChaquePosteComptablePrevu = Entrepot
                      .getTotalMontantPourChaquePosteComptablePrevu(
                          "ScriptsPython\\Recettes\\TotalCreditsPourChaquePosteCompte.py",
                          getTotalMontantPourChaquePosteComptableReel,part7);

                  System.out.println();

                  // Afficher les prédictions
                  System.out.println("***** les valeurs prevus des Recettes par Poste Comptable ****");
                  System.out.println();

                  for (String prediction : getTotalMontantPourChaquePosteComptablePrevu) {
                    System.out.println(prediction);
                  }

                  System.out.println();

                  // Convertir la liste getTotalMontantPourChaquePosteComptableReel en une
                  // seule chaîne
                  StringBuilder resReel = new StringBuilder();
                  for (String Res : getTotalMontantPourChaquePosteComptableReel) {
                    resReel.append(Res).append("\n");
                  }

                  // Convertir la chaîne getTotalMontantPourChaquePosteComptableReel en
                  // tableau de bytes
                  byte[] DataReel = resReel.toString().getBytes();

                  // Récupérer l'adresse IP et le numéro de port du client
                  InetAddress clientAddressReel = receivePacket.getAddress();
                  int clientPortReel = receivePacket.getPort();

                  // Créer un DatagramPacket avec les données et les envoyer au client
                  DatagramPacket responsePacketReel = new DatagramPacket(
                      DataReel,
                      DataReel.length,
                      clientAddressReel,
                      clientPortReel);
                  serverSocket.send(responsePacketReel);

                  // Convertir la liste getTotalMontantPourChaquePosteComptablePrevu en
                  // une
                  // seule chaîne
                  StringBuilder resPrevu = new StringBuilder();
                  for (String Res : getTotalMontantPourChaquePosteComptablePrevu) {
                    resPrevu.append(Res).append("\n");
                  }

                  // Convertir la chaîne getTotalMontantPourChaquePosteComptablePrevu en
                  // tableau de bytes
                  byte[] DataPrevu = resPrevu.toString().getBytes();

                  // Récupérer l'adresse IP et le numéro de port du client
                  InetAddress clientAddressPrevu = receivePacket.getAddress();
                  int clientPortPrevu = receivePacket.getPort();

                  // Créer un DatagramPacket avec les données et les envoyer au client
                  DatagramPacket responsePacketPrevu = new DatagramPacket(
                      DataPrevu,
                      DataPrevu.length,
                      clientAddressPrevu,
                      clientPortPrevu);
                  serverSocket.send(responsePacketPrevu);

                }

                System.out.println("fin");

              }
            } 
          } else if (part4.equals("Dette")) { // Dette
            if (part5.equals("reel")) { // Dette réels

              if (part6.equals("a")) { // getTotalMontantRembourcePourChaqueSoumissionaire
                List<String> getTotalMontantRembourcePourChaqueSoumissionaire = Entrepot
                    .getTotalMontantRembourcePourChaqueSoumissionaire(part3);

                // Faites quelque chose avec la liste , par exemple affichez-les
                for (String Res : getTotalMontantRembourcePourChaqueSoumissionaire) {
                  System.out.println(Res);
                }

                // Convertir la liste en une seule chaîne
                StringBuilder resultat = new StringBuilder();
                for (String Res : getTotalMontantRembourcePourChaqueSoumissionaire) {
                  resultat.append(Res).append("\n");
                }

                // Convertir la chaîne en tableau de bytes
                byte[] userData = resultat.toString().getBytes();

                // Récupérer l'adresse IP et le numéro de port du client
                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();

                // Créer un DatagramPacket avec les données et les envoyer au client
                DatagramPacket responsePacket = new DatagramPacket(
                    userData,
                    userData.length,
                    clientAddress,
                    clientPort);
                serverSocket.send(responsePacket);


              } else if (part6.equals("b")) { // getTotalMontantRembourcePourChaqueTitre
                List<String> getTotalMontantRembourcePourChaqueTitre = Entrepot
                    .getTotalMontantRembourcePourChaqueTitre(part3);

                // Faites quelque chose avec la liste , par exemple affichez-les
                for (String Res : getTotalMontantRembourcePourChaqueTitre) {
                  System.out.println(Res);
                }

                // Convertir la liste en une seule chaîne
                StringBuilder resultat = new StringBuilder();
                for (String Res : getTotalMontantRembourcePourChaqueTitre) {
                  resultat.append(Res).append("\n");
                }

                // Convertir la chaîne en tableau de bytes
                byte[] userData = resultat.toString().getBytes();

                // Récupérer l'adresse IP et le numéro de port du client
                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();

                // Créer un DatagramPacket avec les données et les envoyer au client
                DatagramPacket responsePacket = new DatagramPacket(
                    userData,
                    userData.length,
                    clientAddress,
                    clientPort);
                serverSocket.send(responsePacket);


              }
            }else if (part5.equals("prevu")) {// Dette prévu

              if (part6.equals("a")) { // getTotalMontantRembourcePourChaqueSoumissionairePrevu

                List<String> getTotalMontantRembourcePourChaqueSoumissionaire = Entrepot
                    .getTotalMontantRembourcePourChaqueSoumissionaire(part3);

                if (getTotalMontantRembourcePourChaqueSoumissionaire.size() > 0) {

                  // Afficher les valeurs réels
                  System.out.println("***** les valeurs reels des Dettes par Soumissionaire ****");

                  System.out.println();

                  for (String value : getTotalMontantRembourcePourChaqueSoumissionaire) {
                    System.out.println(value);
                  }

                  List<String> getTotalMontantPourChaqueComptePrevu = Entrepot
                      .getTotalMontantPourChaqueComptePrevu(
                          "ScriptsPython\\Financement\\getT.M.RembourcePourChaqueSoum.py",
                          getTotalMontantRembourcePourChaqueSoumissionaire,part7);

                  System.out.println();

                  // Afficher les prédictions
                  System.out.println("***** les valeurs prévus des Dettes par Soumissionaire ****");
                  System.out.println();

                  for (String prediction : getTotalMontantPourChaqueComptePrevu) {
                    System.out.println(prediction);
                  }

                  System.out.println();

                  // Convertir la liste getTotalMontantRembourcePourChaqueSoumissionaire en une
                  // seule chaîne
                  StringBuilder resReel = new StringBuilder();
                  for (String Res : getTotalMontantRembourcePourChaqueSoumissionaire) {
                    resReel.append(Res).append("\n");
                  }

                  // Convertir la chaîne getTotalMontantRembourcePourChaqueSoumissionaire en
                  // tableau de bytes
                  byte[] DataReel = resReel.toString().getBytes();

                  // Récupérer l'adresse IP et le numéro de port du client
                  InetAddress clientAddressReel = receivePacket.getAddress();
                  int clientPortReel = receivePacket.getPort();

                  // Créer un DatagramPacket avec les données et les envoyer au client
                  DatagramPacket responsePacketReel = new DatagramPacket(
                      DataReel,
                      DataReel.length,
                      clientAddressReel,
                      clientPortReel);
                  serverSocket.send(responsePacketReel);

                  // Convertir la liste getTotalMontantPourChaqueComptePrevu en
                  // une
                  // seule chaîne
                  StringBuilder resPrevu = new StringBuilder();
                  for (String Res : getTotalMontantPourChaqueComptePrevu) {
                    resPrevu.append(Res).append("\n");
                  }

                  // Convertir la chaîne getTotalMontantPourChaqueComptePrevu en
                  // tableau de bytes
                  byte[] DataPrevu = resPrevu.toString().getBytes();

                  // Récupérer l'adresse IP et le numéro de port du client
                  InetAddress clientAddressPrevu = receivePacket.getAddress();
                  int clientPortPrevu = receivePacket.getPort();

                  // Créer un DatagramPacket avec les données et les envoyer au client
                  DatagramPacket responsePacketPrevu = new DatagramPacket(
                      DataPrevu,
                      DataPrevu.length,
                      clientAddressPrevu,
                      clientPortPrevu);
                  serverSocket.send(responsePacketPrevu);

                }

                System.out.println("fin");

              }else if (part6.equals("b")) { // getTotalMontantRembourcePourChaqueTitre

                List<String> getTotalMontantRembourcePourChaqueTitre = Entrepot
                    .getTotalMontantRembourcePourChaqueTitre(part3);

                if (getTotalMontantRembourcePourChaqueTitre.size() > 0) {

                  // Afficher les valeurs réels
                  System.out.println("***** les valeurs reels des Dettes par Titre ****");

                  System.out.println();

                  for (String value : getTotalMontantRembourcePourChaqueTitre) {
                    System.out.println(value);
                  }

                  List<String> getTotalMontantRembourcePourChaqueTitrePrevu = Entrepot
                      .getTotalMontantRembourcePourChaqueTitrePrevu(
                          "ScriptsPython\\Financement\\getT.M.RembourcePourChaqueTitre.py",
                          getTotalMontantRembourcePourChaqueTitre,part7);

                  System.out.println();

                  // Afficher les prédictions
                  System.out.println("***** les valeurs prevus des Dettes par Titre ****");
                  System.out.println();

                  for (String prediction : getTotalMontantRembourcePourChaqueTitrePrevu) {
                    System.out.println(prediction);
                  }

                  System.out.println();

                  // Convertir la liste getTotalMontantRembourcePourChaqueTitre en une
                  // seule chaîne
                  StringBuilder resReel = new StringBuilder();
                  for (String Res : getTotalMontantRembourcePourChaqueTitre) {
                    resReel.append(Res).append("\n");
                  }

                  // Convertir la chaîne getTotalMontantRembourcePourChaqueTitre en
                  // tableau de bytes
                  byte[] DataReel = resReel.toString().getBytes();

                  // Récupérer l'adresse IP et le numéro de port du client
                  InetAddress clientAddressReel = receivePacket.getAddress();
                  int clientPortReel = receivePacket.getPort();

                  // Créer un DatagramPacket avec les données et les envoyer au client
                  DatagramPacket responsePacketReel = new DatagramPacket(
                      DataReel,
                      DataReel.length,
                      clientAddressReel,
                      clientPortReel);
                  serverSocket.send(responsePacketReel);

                  // Convertir la liste getTotalMontantRembourcePourChaqueTitrePrevu en
                  // une
                  // seule chaîne
                  StringBuilder resPrevu = new StringBuilder();
                  for (String Res : getTotalMontantRembourcePourChaqueTitrePrevu) {
                    resPrevu.append(Res).append("\n");
                  }

                  // Convertir la chaîne getTotalMontantRembourcePourChaqueTitrePrevu en
                  // tableau de bytes
                  byte[] DataPrevu = resPrevu.toString().getBytes();

                  // Récupérer l'adresse IP et le numéro de port du client
                  InetAddress clientAddressPrevu = receivePacket.getAddress();
                  int clientPortPrevu = receivePacket.getPort();

                  // Créer un DatagramPacket avec les données et les envoyer au client
                  DatagramPacket responsePacketPrevu = new DatagramPacket(
                      DataPrevu,
                      DataPrevu.length,
                      clientAddressPrevu,
                      clientPortPrevu);
                  serverSocket.send(responsePacketPrevu);

                }

                System.out.println("fin");

              }
            }

          }
        }

      } // endWhile

    } catch (Exception e) {
      e.printStackTrace();
    }

  }
}
