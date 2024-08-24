import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Users{

public List<String> getAllUsers() {
    List<String> users = new ArrayList<>();
    Connection connection = null;
    PreparedStatement selectStatement = null;

    try {
        // Connexion à la base de données
        String url = "jdbc:oracle:thin:@localhost:1521/orcl";
        String username = "projetMaster";
        String passwordDB = "psw";
        connection = DriverManager.getConnection(url, username, passwordDB);

        // Sélectionner tous les utilisateurs de la base de données
        String selectQuery = "SELECT * FROM utilisateur";
        selectStatement = connection.prepareStatement(selectQuery);
        ResultSet resultSet = selectStatement.executeQuery();

        // Parcourir les résultats et ajouter chaque utilisateur à la liste
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("nom");
            String email = resultSet.getString("email");
            String role = resultSet.getString("role");
            // Construire une chaîne représentant l'utilisateur
            String userInfo = "ID: " + id + ", Nom: " + name + ", Email: " + email + ", Role: " + role;
            users.add(userInfo);
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

    return users;
}




public String deleteUserById(int userId) {
    Connection connection = null;
    PreparedStatement deleteStatement = null;

    try {
        // Connexion à la base de données
        String url = "jdbc:oracle:thin:@localhost:1521/orcl";
        String username = "projetMaster";
        String passwordDB = "psw";
        connection = DriverManager.getConnection(url, username, passwordDB);

        // Supprimer l'utilisateur de la base de données en fonction de son ID
        String deleteQuery = "DELETE FROM utilisateur WHERE id = ?";
        deleteStatement = connection.prepareStatement(deleteQuery);
        deleteStatement.setInt(1, userId);
        int rowsAffected = deleteStatement.executeUpdate();

        if (rowsAffected > 0) {
            return "Utilisateur supprimé avec succès";
        } else {
            return "Aucun utilisateur trouvé avec l'ID spécifié.";
        }

    } catch (SQLException e) {
        // Gestion des exceptions SQL
        e.printStackTrace();
        return "Erreur lors de la suppression de l'utilisateur : " + e.getMessage();
    } finally {
        // Fermeture des ressources
        try {
            if (deleteStatement != null) {
                deleteStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}



public String updateUserTo_User_haut_niveau(int userId,String user_haut_niveau) {
    Connection connection = null;
    PreparedStatement updateStatement = null;

    try {
        // Connexion à la base de données
        String url = "jdbc:oracle:thin:@localhost:1521/orcl";
        String username = "projetMaster";
        String passwordDB = "psw";
        connection = DriverManager.getConnection(url, username, passwordDB);

        // Supprimer l'utilisateur de la base de données en fonction de son ID
        String updateQuery = "UPDATE utilisateur SET role = 'User haut niveau' WHERE id = ?";
        updateStatement = connection.prepareStatement(updateQuery);
        updateStatement.setInt(1, userId);
        int rowsAffected = updateStatement.executeUpdate();

        if (rowsAffected > 0) {
            return "Utilisateur modifié to User haut niveau avec succès";
        } else {
            return "Aucun utilisateur trouvé avec l'ID spécifié.";
        }

    } catch (SQLException e) {
        // Gestion des exceptions SQL
        e.printStackTrace();
        return "Erreur lors de la modification de l'utilisateur : " + e.getMessage();
    } finally {
        // Fermeture des ressources
        try {
            if (updateStatement != null) {
                updateStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}



public String updateUserTo_user_Depenses(int userId,String user_Depenses) {
    Connection connection = null;
    PreparedStatement updateStatement = null;

    try {
        // Connexion à la base de données
        String url = "jdbc:oracle:thin:@localhost:1521/orcl";
        String username = "projetMaster";
        String passwordDB = "psw";
        connection = DriverManager.getConnection(url, username, passwordDB);

        // Supprimer l'utilisateur de la base de données en fonction de son ID
        String updateQuery = "UPDATE utilisateur SET role = 'User Depenses' WHERE id = ?";
        updateStatement = connection.prepareStatement(updateQuery);
        updateStatement.setInt(1, userId);
        int rowsAffected = updateStatement.executeUpdate();

        if (rowsAffected > 0) {
            return "Utilisateur modifié to User Dépenses avec succès";
        } else {
            return "Aucun utilisateur trouvé avec l'ID spécifié.";
        }

    } catch (SQLException e) {
        // Gestion des exceptions SQL
        e.printStackTrace();
        return "Erreur lors de la modification de l'utilisateur : " + e.getMessage();
    } finally {
        // Fermeture des ressources
        try {
            if (updateStatement != null) {
                updateStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}



public String updateUserTo_Admin_Back_Office(int userId,String AdminBackOffice) {
    Connection connection = null;
    PreparedStatement updateStatement = null;

    try {
        // Connexion à la base de données
        String url = "jdbc:oracle:thin:@localhost:1521/orcl";
        String username = "projetMaster";
        String passwordDB = "psw";
        connection = DriverManager.getConnection(url, username, passwordDB);

        // Supprimer l'utilisateur de la base de données en fonction de son ID
        String updateQuery = "UPDATE utilisateur SET role = 'Admin Back Office' WHERE id = ?";
        updateStatement = connection.prepareStatement(updateQuery);
        updateStatement.setInt(1, userId);
        int rowsAffected = updateStatement.executeUpdate();

        if (rowsAffected > 0) {
            return "Utilisateur modifié to Admin Back Office avec succès";
        } else {
            return "Aucun utilisateur trouvé avec l'ID spécifié.";
        }

    } catch (SQLException e) {
        // Gestion des exceptions SQL
        e.printStackTrace();
        return "Erreur lors de la modification de l'utilisateur : " + e.getMessage();
    } finally {
        // Fermeture des ressources
        try {
            if (updateStatement != null) {
                updateStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}



public String updateUserTo_Admin_front_Office(int userId,String AdminfrontOffice) {
    Connection connection = null;
    PreparedStatement updateStatement = null;

    try {
        // Connexion à la base de données
        String url = "jdbc:oracle:thin:@localhost:1521/orcl";
        String username = "projetMaster";
        String passwordDB = "psw";
        connection = DriverManager.getConnection(url, username, passwordDB);

        // Supprimer l'utilisateur de la base de données en fonction de son ID
        String updateQuery = "UPDATE utilisateur SET role = 'Admin front Office' WHERE id = ?";
        updateStatement = connection.prepareStatement(updateQuery);
        updateStatement.setInt(1, userId);
        int rowsAffected = updateStatement.executeUpdate();

        if (rowsAffected > 0) {
            return "Utilisateur modifié to Admin front Office avec succès";
        } else {
            return "Aucun utilisateur trouvé avec l'ID spécifié.";
        }

    } catch (SQLException e) {
        // Gestion des exceptions SQL
        e.printStackTrace();
        return "Erreur lors de la modification de l'utilisateur : " + e.getMessage();
    } finally {
        // Fermeture des ressources
        try {
            if (updateStatement != null) {
                updateStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}



public String updateUSER_AU_User_Financement(int userId,String UserFinancement) {
    Connection connection = null;
    PreparedStatement updateStatement = null;

    try {
        // Connexion à la base de données
        String url = "jdbc:oracle:thin:@localhost:1521/orcl";
        String username = "projetMaster";
        String passwordDB = "psw";
        connection = DriverManager.getConnection(url, username, passwordDB);

        // Supprimer l'utilisateur de la base de données en fonction de son ID
        String updateQuery = "UPDATE utilisateur SET role = 'utilisateur financement' WHERE id = ?";
        updateStatement = connection.prepareStatement(updateQuery);
        updateStatement.setInt(1, userId);
        int rowsAffected = updateStatement.executeUpdate();

        if (rowsAffected > 0) {
            return "Utilisateur modifié to utilisateur financement avec succès";
        } else {
            return "Aucun utilisateur trouvé avec l'ID spécifié.";
        }

    } catch (SQLException e) {
        // Gestion des exceptions SQL
        e.printStackTrace();
        return "Erreur lors de la modification de l'utilisateur : " + e.getMessage();
    } finally {
        // Fermeture des ressources
        try {
            if (updateStatement != null) {
                updateStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}




public String updateUSER_AU_User_Recettes(int userId,String UserRecettes) {
    Connection connection = null;
    PreparedStatement updateStatement = null;

    try {
        // Connexion à la base de données
        String url = "jdbc:oracle:thin:@localhost:1521/orcl";
        String username = "projetMaster";
        String passwordDB = "psw";
        connection = DriverManager.getConnection(url, username, passwordDB);

        // Supprimer l'utilisateur de la base de données en fonction de son ID
        String updateQuery = "UPDATE utilisateur SET role = 'User Recettes' WHERE id = ?";
        updateStatement = connection.prepareStatement(updateQuery);
        updateStatement.setInt(1, userId);
        int rowsAffected = updateStatement.executeUpdate();

        if (rowsAffected > 0) {
            return "Utilisateur modifié to User Recettes avec succès";
        } else {
            return "Aucun utilisateur trouvé avec l'ID spécifié.";
        }

    } catch (SQLException e) {
        // Gestion des exceptions SQL
        e.printStackTrace();
        return "Erreur lors de la modification de l'utilisateur : " + e.getMessage();
    } finally {
        // Fermeture des ressources
        try {
            if (updateStatement != null) {
                updateStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}








}