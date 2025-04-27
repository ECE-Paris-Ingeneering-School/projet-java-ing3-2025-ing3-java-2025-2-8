package dao;

import modele.Commande;
import util.Databaseconnection;
import java.util.List;
import java.util.ArrayList;


import java.sql.*;
import java.util.Date;

public class CommandeDAO {

    /**
     * CommandeDAO est la classe responsable de toutes les opérations CRUD
     * liées aux commandes dans la base de données.
     *
     * Elle permet d'ajouter une nouvelle commande, de récupérer les commandes
     * d'un utilisateur donné, ainsi que de calculer des statistiques
     * comme le nombre total de commandes et le chiffre d'affaires.
     *
     * Utilise JDBC avec gestion sécurisée des ressources (try-with-resources).
     */

    public int ajouterCommande(Commande commande) {
        /**
         * Ajoute une nouvelle commande à la base de données.
         * @autheur Jean haj
         * @param commande L'objet Commande contenant la date, l'utilisateur, et le total.
         * @return L'identifiant (idCommande) généré pour la commande ajoutée, ou -1 en cas d'échec.
         */

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
        /**
         * Récupère toutes les commandes passées par un utilisateur spécifique.
         *
         * @param idUtilisateur L'identifiant de l'utilisateur.
         * @return Une liste d'objets Commande correspondant à l'utilisateur.
         */

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
        /**
         * Récupère le nombre total de commandes dans la base de données.
         *
         * @return Le nombre total de commandes.
         */

        String sql = "SELECT COUNT(*) FROM Commande";
        try (Connection conn = Databaseconnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    public double getTotalVentes() {
        /**
         * Calcule le montant total des ventes réalisées (somme de tous les totaux de commandes).
         *
         * @return Le chiffre d'affaires total sous forme de double.
         */

        String sql = "SELECT SUM(total) FROM Commande";
        try (Connection conn = Databaseconnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getDouble(1);
        } catch (Exception e) { e.printStackTrace(); }
        return 0.0;
    }


}
