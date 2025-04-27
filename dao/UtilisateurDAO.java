package dao;



import modele.Utilisateur;
import util.Databaseconnection;

import java.sql.*;
/**
 * UtilisateurDAO est la classe responsable de l'accès aux données
 * liées aux utilisateurs dans la base de données.
 *
 * Cette classe permet :
 * - d'authentifier un utilisateur,
 * - d'ajouter un nouvel utilisateur lors de l'inscription.
 *
 * Utilise JDBC avec des requêtes préparées (PreparedStatement) pour plus de sécurité.
 * @author jeanhaj
 */


public class UtilisateurDAO {

    // Vérifie les identifiants et retourne l'utilisateur s’il existe
    public Utilisateur authenticate(String email, String password) {
        /**
         * Authentifie un utilisateur en vérifiant son email et son mot de passe.
         *
         * @param email L'adresse email de l'utilisateur.
         * @param password Le mot de passe de l'utilisateur.
         * @return L'objet Utilisateur correspondant si l'authentification réussit, sinon null.
         */

        String sql = "SELECT * FROM Utilisateur WHERE email = ? AND mot_de_passe = ?";
        try (Connection conn = Databaseconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Utilisateur(
                        rs.getInt("idUtilisateur"),
                        rs.getString("nom"),
                        rs.getString("email"),
                        rs.getString("mot_de_passe"),
                        rs.getString("type")
                );
            }

        } catch (SQLException e) {
            System.err.println("Erreur d'authentification : " + e.getMessage());
        }
        return null;
    }

    // Inscription d’un nouveau client
    public boolean ajouterUtilisateur(Utilisateur user) {
        /**
         * Ajoute un nouvel utilisateur (client) dans la base de données.
         *
         * @param user L'objet Utilisateur à ajouter.
         * @return true si l'ajout a réussi, false sinon.
         */

        String sql = "INSERT INTO Utilisateur(nom, email, mot_de_passe, type) VALUES (?, ?, ?, ?)";
        try (Connection conn = Databaseconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getNom());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getMotDePasse());
            stmt.setString(4, user.getType());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur d'ajout utilisateur : " + e.getMessage());
            return false;
        }
    }
}

