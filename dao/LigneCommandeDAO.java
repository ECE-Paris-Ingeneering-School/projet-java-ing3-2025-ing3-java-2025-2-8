package dao;

import modele.LigneCommande;
import util.Databaseconnection;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

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
    public List<LigneCommande> getLignesParCommande(int idCommande) {
        List<LigneCommande> lignes = new ArrayList<>();
        String sql = "SELECT * FROM LigneCommande WHERE idCommande = ?";

        try (Connection conn = Databaseconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCommande);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                LigneCommande l = new LigneCommande(
                        rs.getInt("idLigne"),
                        rs.getInt("idCommande"),
                        rs.getInt("idProduit"),
                        rs.getInt("quantite"),
                        rs.getDouble("prixUnitaire"),
                        rs.getDouble("sousTotal")
                );
                lignes.add(l);
            }

        } catch (Exception e) {
            System.err.println("Erreur chargement lignes commande : " + e.getMessage());
        }

        return lignes;
    }

}
