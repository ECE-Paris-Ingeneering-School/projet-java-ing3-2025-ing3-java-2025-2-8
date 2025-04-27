package dao;

import modele.Marque;
import modele.Produit;
import util.Databaseconnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 * ProduitDao est la classe responsable de toutes les opérations
 * liées aux produits et aux marques dans la base de données.
 *
 * Cette classe permet de :
 * - Récupérer tous les produits avec leur marque,
 * - Ajouter un nouveau produit,
 * - Modifier un produit existant,
 * - Supprimer un produit,
 * - Gérer les marques associées aux produits.
 *
 * Utilise JDBC avec gestion sécurisée des ressources (try-with-resources).
 * @author jeanhaj
 */


public class ProduitDao {
    /**
     * Récupère tous les produits depuis la base de données,
     * en incluant le nom de la marque associée.
     *
     * @return Liste de produits avec leur marque.
     */

    public List<Produit> getAllProduitsAvecMarque() {
        List<Produit> produits = new ArrayList<>();
        String sql = "SELECT p.*, m.nom AS nomMarque FROM Produit p JOIN Marque m ON p.idMarque = m.idMarque";

        try (Connection conn = Databaseconnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Produit p = new Produit(
                        rs.getInt("idProduit"),
                        rs.getString("nom"),
                        rs.getDouble("prixUnitaire"),
                        rs.getInt("stock"),
                        rs.getInt("qteLotPromo"),
                        rs.getDouble("prixLotPromo"),
                        rs.getInt("idMarque")
                );
                p.setNomMarque(rs.getString("nomMarque"));
                produits.add(p);
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors du chargement des produits : " + e.getMessage());
        }

        return produits;
    }

    // Ajoute un produit (et la marque si elle est nouvelle)
    public boolean ajouterProduit(Produit p) {
        /**
         * Ajoute un nouveau produit à la base de données.
         * Si la marque n'existe pas, elle est automatiquement créée.
         *
         * @param p L'objet Produit à ajouter.
         * @return true si l'insertion a réussi, false sinon.
         */

        try (Connection conn = Databaseconnection.getConnection()) {
            int idMarque = getOrCreateMarque(p.getNomMarque(), conn);
            p.setIdMarque(idMarque);

            String sql = "INSERT INTO Produit(nom, prixUnitaire, stock, qteLotPromo, prixLotPromo, idMarque) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, p.getNom());
                stmt.setDouble(2, p.getPrixUnitaire());
                stmt.setInt(3, p.getStock());
                stmt.setInt(4, p.getQteLotPromo());
                stmt.setDouble(5, p.getPrixLotPromo());
                stmt.setInt(6, idMarque);

                int rows = stmt.executeUpdate();
                return rows > 0;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout d'un produit : " + e.getMessage());
            return false;
        }
    }

    public boolean supprimerProduit(int idProduit) {
        /**
         * Supprime un produit de la base de données selon son identifiant.
         *
         * @param idProduit L'identifiant du produit à supprimer.
         * @return true si la suppression a réussi, false sinon.
         */

        String sql = "DELETE FROM Produit WHERE idProduit = ?";
        System.out.println("Suppression du produit ID: " + idProduit);
        try (Connection conn = Databaseconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idProduit);
            int rows = stmt.executeUpdate();
            System.out.println("Nombre de lignes supprimées : " + rows);
            return rows > 0;

        } catch (SQLException e) {
            System.err.println("Erreur SQL pendant la suppression : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }



    public boolean modifierProduit(Produit p) {
        /**
         * Modifie les informations d'un produit existant dans la base de données.
         * Si la marque est nouvelle, elle est créée automatiquement.
         *
         * @param p L'objet Produit contenant les nouvelles données.
         * @return true si la mise à jour a réussi, false sinon.
         */

        try (Connection conn = Databaseconnection.getConnection()) {
            int idMarque = getOrCreateMarque(p.getNomMarque(), conn);
            p.setIdMarque(idMarque);

            String sql = "UPDATE Produit SET nom=?, prixUnitaire=?, stock=?, qteLotPromo=?, prixLotPromo=?, idMarque=? WHERE idProduit=?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, p.getNom());
                stmt.setDouble(2, p.getPrixUnitaire());
                stmt.setInt(3, p.getStock());
                stmt.setInt(4, p.getQteLotPromo());
                stmt.setDouble(5, p.getPrixLotPromo());
                stmt.setInt(6, idMarque);
                stmt.setInt(7, p.getIdProduit());

                int rows = stmt.executeUpdate();
                return rows > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Marque> getAllMarques() {
        /**
         * Récupère toutes les marques disponibles dans la base de données.
         *
         * @return Liste de toutes les marques.
         */

        List<Marque> marques = new ArrayList<>();
        String sql = "SELECT * FROM Marque";
        try (Connection conn = Databaseconnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                marques.add(new Marque(rs.getInt("idMarque"), rs.getString("nom")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return marques;
    }

    private int getOrCreateMarque(String nomMarque, Connection conn) throws SQLException {
        /**
         * Récupère l'identifiant d'une marque existante par son nom,
         * ou crée une nouvelle marque si elle n'existe pas encore.
         *
         * @param nomMarque Le nom de la marque à rechercher ou créer.
         * @param conn La connexion active à la base de données.
         * @return L'identifiant de la marque existante ou nouvellement créée.
         * @throws SQLException En cas d'erreur lors de la recherche ou de l'insertion.
         */
        String selectSql = "SELECT idMarque FROM Marque WHERE nom = ?";
        try (PreparedStatement stmt = conn.prepareStatement(selectSql)) {
            stmt.setString(1, nomMarque);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("idMarque");
            }
        }

        String insertSql = "INSERT INTO Marque(nom) VALUES (?)";
        try (PreparedStatement stmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, nomMarque);
            stmt.executeUpdate();
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            }
        }
        throw new SQLException("Échec lors de la création de la marque.");
    }
}