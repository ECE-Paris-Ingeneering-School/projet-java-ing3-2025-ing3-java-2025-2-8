package dao;

import modele.LigneCommande;
import util.Databaseconnection;

import java.sql.*;

public class LigneCommandeDAO {

    public boolean ajouterLigneCommande(LigneCommande ligne) {
        String sql = "INSERT INTO LigneCommande(idCommande, idProduit, quantite, prixUnitaire, sousTotal) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Databaseconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, ligne.getIdCommande());
            stmt.setInt(2, ligne.getIdProduit());
            stmt.setInt(3, ligne.getQuantite());
            stmt.setDouble(4, ligne.getPrixUnitaire());
            stmt.setDouble(5, ligne.getSousTotal());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erreur ajout ligne de commande : " + e.getMessage());
            return false;
        }
    }
}
