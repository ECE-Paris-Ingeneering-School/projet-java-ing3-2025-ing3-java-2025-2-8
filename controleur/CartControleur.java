package controleur;

import dao.CommandeDAO;
import dao.LigneCommandeDAO;
import modele.Commande;
import modele.LigneCommande;
import modele.Utilisateur;
import vue.CartView;
import controleur.LoginControleur;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CartControleur {
    private CartView vue;

    public CartControleur(CartView vue) {
        this.vue = vue;
        initListeners();
    }

    private void initListeners() {
        vue.getClearButton().addActionListener(e -> clearCart());
        vue.getRetourButton().addActionListener(e -> retour());
        vue.getCheckoutButton().addActionListener(e -> passerCommande());
    }

    private void clearCart() {
        vue.getModel().setRowCount(0);
        vue.updateTotal(0.0);
    }

    private void retour() {
        vue.setVisible(false);
        vue.getParentWindow().setVisible(true);
    }

    private void passerCommande() {
        DefaultTableModel model = vue.getModel();

        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(vue, "Votre panier est vide.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Utilisateur utilisateur = LoginControleur.utilisateurConnecte;
        if (utilisateur == null) {
            JOptionPane.showMessageDialog(vue, "Utilisateur non connecté.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double total = 0;
        List<LigneCommande> lignes = new ArrayList<>();
        StringBuilder recap = new StringBuilder("RÉCAPITULATIF DE COMMANDE :\n\n");

        for (int i = 0; i < model.getRowCount(); i++) {
            int idProduit = (int) model.getValueAt(i, 0);
            String nom = (String) model.getValueAt(i, 1);
            double prixUnitaire = (double) model.getValueAt(i, 2);
            int quantite = (int) model.getValueAt(i, 3);
            double sousTotal = (double) model.getValueAt(i, 6);

            total += sousTotal;

            lignes.add(new LigneCommande(0, 0, idProduit, quantite, prixUnitaire, sousTotal));

            recap.append("- ").append(nom)
                    .append(" x ").append(quantite)
                    .append(" → ").append(String.format("%.2f €", sousTotal))
                    .append("\n");
        }

        recap.append("\nTOTAL : ").append(String.format("%.2f €", total));
        recap.append("\n\nVoulez-vous confirmer cette commande ?");

        int confirmation = JOptionPane.showConfirmDialog(vue, recap.toString(), "Confirmer la commande",
                JOptionPane.YES_NO_OPTION);

        if (confirmation != JOptionPane.YES_OPTION) return;

        Commande commande = new Commande(0, new Date(), utilisateur.getId(), total);
        int idCommande = new CommandeDAO().ajouterCommande(commande);

        if (idCommande != -1) {
            boolean success = true;
            for (LigneCommande ligne : lignes) {
                ligne.setIdCommande(idCommande);
                if (!new LigneCommandeDAO().ajouterLigneCommande(ligne)) {
                    success = false;
                    break;
                }
            }

            if (success) {
                JOptionPane.showMessageDialog(vue, "Commande enregistrée avec succès !");
                clearCart();
                retour();
            } else {
                JOptionPane.showMessageDialog(vue, "Erreur lors de l'enregistrement des lignes de commande.");
            }
        } else {
            JOptionPane.showMessageDialog(vue, "Erreur lors de la création de la commande.");
        }
    }
}
