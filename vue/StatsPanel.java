/**
 * Fichier : StatsPanel.java
 *
 * Panneau d'affichage des statistiques (Swing) pour le projet Java ING3 2025 Shopping.
 *
 * Sources :
 * - Cours 7 Java – Composants Swing, JLabel, JPanel, BoxLayout, accès DAO (JP Segado, Boostcamp/PDF)
 * - OpenClassrooms – Création d’interfaces Swing et panels :
 *   https://openclassrooms.com/fr/courses/26832-apprenez-a-programmer-en-java/26892-creez-une-interface-graphique-avec-swing
 * - GeeksforGeeks – JPanel in Java Swing :
 *   https://www.geeksforgeeks.org/jpanel-java-swing/
 * - JavaDoc officielle (Swing) :
 *   https://docs.oracle.com/javase/8/docs/api/javax/swing/JPanel.html
 *
 * @author Adam Zeidan et Florent Meunier
 * @date Avril 2025
 */


package vue;

import dao.CommandeDAO;
import dao.LigneCommandeDAO;
import modele.Produit;

import javax.swing.*;
import java.awt.*;

public class StatsPanel extends JPanel {

    private JLabel totalCommandesLabel;
    private JLabel totalVentesLabel;
    private JLabel produitTopLabel;

    public StatsPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createTitledBorder("Statistiques"));

        totalCommandesLabel = new JLabel("Total des commandes : ...");
        totalVentesLabel = new JLabel("Montant total des ventes : ... €");
        produitTopLabel = new JLabel("Produit le plus commandé : ...");

        totalCommandesLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalVentesLabel.setFont(new Font("Arial", Font.BOLD, 16));
        produitTopLabel.setFont(new Font("Arial", Font.BOLD, 16));

        add(Box.createVerticalStrut(10));
        add(totalCommandesLabel);
        add(Box.createVerticalStrut(10));
        add(totalVentesLabel);
        add(Box.createVerticalStrut(10));
        add(produitTopLabel);

        chargerStats();
    }

    private void chargerStats() {
        CommandeDAO commandeDAO = new CommandeDAO();
        LigneCommandeDAO ligneDAO = new LigneCommandeDAO();

        int nbCommandes = commandeDAO.getNombreCommandes();
        double totalVentes = commandeDAO.getTotalVentes();
        Produit topProduit = ligneDAO.getProduitLePlusCommandé();

        totalCommandesLabel.setText("Total des commandes : " + nbCommandes);
        totalVentesLabel.setText("Montant total des ventes : " + String.format("%.2f €", totalVentes));

        if (topProduit != null) {
            produitTopLabel.setText("Produit le plus commandé : " + topProduit.getNom());
        } else {
            produitTopLabel.setText("Produit le plus commandé : Aucun");
        }
    }
}

