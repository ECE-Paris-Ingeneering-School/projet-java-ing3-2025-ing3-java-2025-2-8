package dao;

import modele.Commande;
import util.Databaseconnection;
import java.util.List;
import java.util.ArrayList;


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
    public List<Commande> getCommandesParUtilisateur(int idUtilisateur) {
        List<Commande> commandes = new ArrayList<>();
        String sql = "SELECT * FROM Commande WHERE idUtilisateur = ? ORDER BY dateCommande DESC";

        try (Connection conn = Databaseconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUtilisateur);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int idCommande = rs.getInt("idCommande");
                Date dateCommande = new Date(rs.getTimestamp("dateCommande").getTime());
                double total = rs.getDouble("total");

                commandes.add(new Commande(idCommande, dateCommande, idUtilisateur, total));
            }

        } catch (SQLException e) {
            System.err.println("Erreur récupération commandes : " + e.getMessage());
        }

        return commandes;
    }
    public int getNombreCommandes() {
        String sql = "SELECT COUNT(*) FROM Commande";
        try (Connection conn = Databaseconnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    public double getTotalVentes() {
        String sql = "SELECT SUM(total) FROM Commande";
        try (Connection conn = Databaseconnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getDouble(1);
        } catch (Exception e) { e.printStackTrace(); }
        return 0.0;
    }


}
