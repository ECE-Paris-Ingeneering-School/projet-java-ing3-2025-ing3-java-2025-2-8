package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Databaseconnection {
    // URL de connexion (adaptée à votre configuration, ici sur localhost et port 3306)
    private static final String URL = "jdbc:mysql://localhost:8889/ShoppingDB";
    private static final String USER = "root";        // Remplacez par votre utilisateur MySQL
    private static final String PASSWORD = "root";      // Remplacez par votre mot de passe MySQL

    public static Connection getConnection() {
        Connection connection = null;
        try {
            // Chargement du driver JDBC MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Établissement de la connexion
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connexion à la base réussie !");
        } catch (ClassNotFoundException e) {
            System.err.println("Driver MySQL introuvable : " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Erreur lors de la connexion à la base de données : " + e.getMessage());
        }
        return connection;
    }
}

