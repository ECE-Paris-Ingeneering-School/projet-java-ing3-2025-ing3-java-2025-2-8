import dao.CommandeDAO;
import dao.LigneCommandeDAO;
import dao.ProduitDao;
import modele.Commande;
import modele.LigneCommande;
import modele.Produit;

import java.util.*;

public class main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ProduitDao produitDAO = new ProduitDao();
        CommandeDAO commandeDAO = new CommandeDAO();
        LigneCommandeDAO ligneCommandeDAO = new LigneCommandeDAO();

        // === 1. Simulation d'un panier ===
        List<LigneCommande> panier = new ArrayList<>();
        double totalCommande = 0;

        // ➤ Choisir un client déjà existant (ex: idUtilisateur = 1)
        int idClient = 1;

        System.out.println("=== PRODUITS DISPONIBLES ===");
        List<Produit> produits = produitDAO.getAllProduits();
        for (Produit p : produits) {
            System.out.println(p.getIdProduit() + " - " + p.getNom() + " - " + p.getPrixUnitaire() + " €");
        }

        while (true) {
            System.out.print("ID produit à ajouter (ou 0 pour valider) : ");
            int idProduit = scanner.nextInt();
            if (idProduit == 0) break;

            System.out.print("Quantité : ");
            int quantite = scanner.nextInt();

            // Recherche du produit
            Produit p = produits.stream()
                    .filter(prod -> prod.getIdProduit() == idProduit)
                    .findFirst()
                    .orElse(null);

            if (p == null) {
                System.out.println("Produit non trouvé.");
                continue;
            }

            // Application éventuelle d'une remise en lot
            double prixFinal = calculerPrixAvecRemise(p, quantite);
            double sousTotal = prixFinal;

            LigneCommande ligne = new LigneCommande(0, 0, p.getIdProduit(), quantite, p.getPrixUnitaire(), sousTotal);
            panier.add(ligne);
            totalCommande += sousTotal;

            System.out.println("Ajouté au panier. Sous-total : " + sousTotal + " €");
        }

        if (panier.isEmpty()) {
            System.out.println("Panier vide. Fin du programme.");
            return;
        }

        // === 2. Enregistrement de la commande ===
        Commande commande = new Commande(0, new Date(), idClient, totalCommande);
        int idCommande = commandeDAO.ajouterCommande(commande);

        if (idCommande == -1) {
            System.out.println("Échec de l'enregistrement de la commande.");
            return;
        }

        // === 3. Enregistrement des lignes de commande ===
        for (LigneCommande ligne : panier) {
            ligne.setIdCommande(idCommande);
            ligneCommandeDAO.ajouterLigneCommande(ligne);
        }

        System.out.println("✅ Commande enregistrée avec succès !");
        System.out.println("Total payé : " + totalCommande + " €");
    }

    // Méthode utilitaire : calcul de remise en lot
    private static double calculerPrixAvecRemise(Produit p, int qte) {
        if (p.getQteLotPromo() > 0 && p.getPrixLotPromo() > 0) {
            int lots = qte / p.getQteLotPromo();
            int reste = qte % p.getQteLotPromo();
            return lots * p.getPrixLotPromo() + reste * p.getPrixUnitaire();
        } else {
            return qte * p.getPrixUnitaire();
        }
    }
}
