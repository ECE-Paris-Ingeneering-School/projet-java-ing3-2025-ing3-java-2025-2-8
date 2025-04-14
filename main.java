import util.Databaseconnection;
import java.sql.Connection;
import java.sql.SQLException;

import dao.ProduitDao;
import modele.Produit;

import java.util.List;

import dao.UtilisateurDAO;
import modele.Utilisateur;

public class main {
    public static void main(String[] args) {
        UtilisateurDAO dao = new UtilisateurDAO();

        // Test d'ajout d'utilisateur
        Utilisateur nouveau = new Utilisateur(0, "Jean Test", "jean@example.com", "1234", "client");
        boolean ajoute = dao.ajouterUtilisateur(nouveau);
        System.out.println("Utilisateur ajouté ? " + ajoute);

        // Test de connexion
        Utilisateur user = dao.authenticate("jean@example.com", "1234");
        if (user != null) {
            System.out.println("Bienvenue " + user.getNom() + " (type: " + user.getType() + ")");
        } else {
            System.out.println("Échec de la connexion.");
        }
    }
}


