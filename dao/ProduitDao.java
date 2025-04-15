package dao;

import modele.Produit;
import util.Databaseconnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProduitDao {

    // Récupère tous les produits de la base
    public List<Produit> getAllProduits() {
        List<Produit> produits = new ArrayList<>();
        String sql = "SELECT * FROM Produit";

        try (Connection conn = Databaseconnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Produit produit = new Produit(
                        rs.getInt("idProduit"),
                        rs.getString("nom"),
                        rs.getDouble("prixUnitaire"),
                        rs.getInt("stock"),
                        rs.getInt("qteLotPromo"),
                        rs.getDouble("prixLotPromo"),
                        rs.getInt("idMarque")
                );
                produits.add(produit);
            }

        } catch (SQLException e) {
            System.err.println("Erreur DAO Produit : " + e.getMessage());
        }

        return produits;
    }

    // Ajoute un produit
    public boolean ajouterProduit(Produit p) {
        String sql = "INSERT INTO Produit(nom, prixUnitaire, stock, qteLotPromo, prixLotPromo, idMarque) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = Databaseconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, p.getNom());
            stmt.setDouble(2, p.getPrixUnitaire());
            stmt.setInt(3, p.getStock());
            stmt.setInt(4, p.getQteLotPromo());
            stmt.setDouble(5, p.getPrixLotPromo());
            stmt.setInt(6, p.getIdMarque());

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout d'un produit : " + e.getMessage());
            return false;
        }
    }
}
