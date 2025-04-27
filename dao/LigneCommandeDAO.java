package dao;

import modele.LigneCommande;
import util.Databaseconnection;
import modele.Produit;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;
/**
 * LigneCommandeDAO est la classe responsable des opérations sur les lignes de commande
 * dans la base de données.
 *
 * Elle permet :
 * - d'ajouter une nouvelle ligne de commande,
 * - de récupérer toutes les lignes d'une commande spécifique,
 * - d'identifier le produit le plus commandé.
 *
 * Utilise JDBC avec gestion sécurisée des connexions (try-with-resources).
 * @author jeanhaj
 */


public class LigneCommandeDAO {

    public boolean ajouterLigneCommande(LigneCommande ligne) {
        /**
         * Ajoute une nouvelle ligne de commande dans la base de données.
         *
         * @param ligne L'objet LigneCommande contenant les détails de la ligne (produit, quantité, prix, sous-total).
         * @return true si l'insertion a réussi, false sinon.
         */

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
        /**
         * Récupère toutes les lignes de commande associées à une commande donnée.
         *
         * @param idCommande L'identifiant de la commande.
         * @return Une liste de LigneCommande correspondant à cette commande.
         */

        List<LigneCommande> lignes = new ArrayList<>();
        String sql = """
        SELECT lc.*, p.nom AS nomProduit
        FROM LigneCommande lc
        JOIN Produit p ON lc.idProduit = p.idProduit
        WHERE lc.idCommande = ?
        """;

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
                l.setNomProduit(rs.getString("nomProduit"));
                lignes.add(l);
            }

        } catch (Exception e) {
            System.err.println("Erreur chargement lignes commande : " + e.getMessage());
        }

        return lignes;
    }

    public Produit getProduitLePlusCommandé() {
        /**
         * Récupère le produit qui a été commandé en plus grande quantité.
         *
         * @return Un objet Produit représentant le produit le plus vendu, ou null en cas d'erreur.
         */

        String sql = """
        SELECT lc.idProduit, SUM(lc.quantite) AS totalQte, p.nom
        FROM LigneCommande lc
        JOIN Produit p ON lc.idProduit = p.idProduit
        GROUP BY lc.idProduit
        ORDER BY totalQte DESC
        LIMIT 1
        """;

        try (Connection conn = Databaseconnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                Produit p = new Produit();
                p.setIdProduit(rs.getInt("idProduit"));
                p.setNom(rs.getString("nom"));
                return p;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


}
