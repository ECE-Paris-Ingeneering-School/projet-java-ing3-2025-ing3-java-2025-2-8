package dao;

import modele.Commande;
import util.Databaseconnection;

import java.sql.*;
import java.util.Date;

public class CommandeDAO {

    // Ajoute une nouvelle commande
    public int ajouterCommande(Commande commande) {
        String sql = "INSERT INTO Commande(dateCommande, idUtilisateur, total) VALUES (?, ?, ?)";
        try (Connection conn = Databaseconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setTimestamp(1, new Timestamp(commande.getDateCommande().getTime()));
            stmt.setInt(2, commande.getIdUtilisateur());
            stmt.setDouble(3, commande.getTotal());

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1); // retourne idCommande généré
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur ajout commande : " + e.getMessage());
        }

        return -1;
    }
}
