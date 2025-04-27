package vue;

import dao.CommandeDAO;
import dao.LigneCommandeDAO;
import modele.Commande;
import modele.LigneCommande;
import modele.Utilisateur;
import controleur.LoginControleur;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Fichier : CartView.java
 *
 * Interface du panier (Swing) pour le projet Java ING3 2025 Shopping.
 *
 * Sources :
 * - Projet Java 2025 Shopping, consignes et pattern MVC (cours Boostcamp)
 * - Cours 7 Java : Swing, JTable, Layouts, Events (JP Segado, PDF cours 7)
 * - Docs Java officielle : Swing/AWT
 *
 * @author Adam Zeidan et Florent Meunier
 * @date Avril 2025
 */


/**
 * CartView: IU du panier.
 * Permet d’ajouter visualiser, modifier et valider les produits dans le panier
 */

public class CartView extends JFrame {
    // Composants UI
    private JTable table;
    private DefaultTableModel model;
    private JLabel totalLabel;
    private CatalogView catalogView;
    private JButton checkoutButton;

    /**
     * Constructeur du panier lie a une vue catalogue.
     */
    public CartView(CatalogView catalogView) {
        this.catalogView = catalogView;

        setTitle("Panier");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        initComponents(); // Initialise l’interface
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Définition des colonnes du tableau

        String[] columns = {"ID", "Nom", "Prix unit.", "Quantité", "Prix en lot", "Seuil", "Sous-total"};
        model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            public Class<?> getColumnClass(int columnIndex) {
                return switch (columnIndex) {
                    case 0, 3, 5 -> Integer.class;
                    case 2, 4, 6 -> Double.class;
                    default -> String.class;
                };
            }
        };
        table = new JTable(model);
        table.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout());

        totalLabel = new JLabel("Total : 0.00 €");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        bottom.add(totalLabel, BorderLayout.WEST);

        JButton clearBtn = new JButton("Vider");
        JButton retourBtn = new JButton("Continuer les achats");
        checkoutButton = new JButton("Passer au paiement");

        clearBtn.addActionListener(e -> clearCart());
        retourBtn.addActionListener(e -> {
            this.setVisible(false);
            catalogView.setVisible(true);
        });

        checkoutButton.addActionListener(e -> passerCommande());

        JPanel btnPanel = new JPanel();
        btnPanel.add(clearBtn);
        btnPanel.add(retourBtn);
        btnPanel.add(checkoutButton);

        bottom.add(btnPanel, BorderLayout.EAST);
        add(bottom, BorderLayout.SOUTH);
    }

    public void addItemToCart(int id, String nom, double prixUnitaire, int qte, double prixLot, int seuil, double sousTotal) {
        for (int i = 0; i < model.getRowCount(); i++) {
            if ((int) model.getValueAt(i, 0) == id) {
                int qteExistante = (int) model.getValueAt(i, 3);
                int nouvelleQte = qteExistante + qte;
                double nouveauTotal = calculerSousTotal(prixUnitaire, prixLot, nouvelleQte, seuil);

                model.setValueAt(nouvelleQte, i, 3);
                model.setValueAt(nouveauTotal, i, 6);
                updateTotal();
                return;
            }
        }

        Vector<Object> row = new Vector<>();
        row.add(id);
        row.add(nom);
        row.add(prixUnitaire);
        row.add(qte);
        row.add(prixLot);
        row.add(seuil);
        row.add(sousTotal);
        model.addRow(row);

        updateTotal();
    }

    private double calculerSousTotal(double prixU, double prixLot, int qte, int seuil) {
        int lots = seuil > 0 ? qte / seuil : 0;
        int restants = seuil > 0 ? qte % seuil : qte;
        return lots * prixLot + restants * prixU;
    }

    private void updateTotal() {
        double total = 0;
        for (int i = 0; i < model.getRowCount(); i++) {
            total += (double) model.getValueAt(i, 6);
        }
        totalLabel.setText("Total : " + String.format("%.2f €", total));
    }

    public void clearCart() {
        model.setRowCount(0);
        updateTotal();
    }

    public void passerCommande() {
        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Votre panier est vide.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Utilisateur utilisateur = LoginControleur.utilisateurConnecte;
        if (utilisateur == null) {
            JOptionPane.showMessageDialog(this, "Utilisateur non connecté.", "Erreur", JOptionPane.ERROR_MESSAGE);
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

        int confirmation = JOptionPane.showConfirmDialog(this, recap.toString(), "Confirmer la commande",
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
                JOptionPane.showMessageDialog(this, "Commande enregistrée avec succès !");
                clearCart();
                catalogView.setVisible(true);
                this.setVisible(false);
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'enregistrement des lignes de commande.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Erreur lors de la création de la commande.");
        }
    }

    public CatalogView getCatalogView() {
        return catalogView;
    }

}
