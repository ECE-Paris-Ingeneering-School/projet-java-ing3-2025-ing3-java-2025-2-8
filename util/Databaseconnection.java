package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Databaseconnection {
    //URL de connexion adaptee a notre local host
    private static final String URL = "jdbc:mysql://localhost:8889/ShoppingDB";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public static Connection getConnection() {
        Connection connection = null;
        try {
            // Chargement du JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");
            // etablissement de la connexion
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

