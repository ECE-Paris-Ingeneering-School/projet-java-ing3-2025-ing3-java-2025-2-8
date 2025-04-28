package controleur;

import dao.ProduitDao;
import modele.Produit;
import modele.Utilisateur;
import vue.CatalogView;
import vue.CartView;
import vue.ClientFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CatalogControleur {
    private CatalogView vue;
    private Utilisateur utilisateur;
    private CartView cartView;



    public CatalogControleur(CatalogView vue, Utilisateur utilisateur) {
        this.vue = vue;
        this.utilisateur = utilisateur;
        this.cartView = new CartView(vue);
        new CartControleur(cartView);

        initListeners();
        loadArticles();
    }

    private void initListeners() {

        vue.getSearchButton().addActionListener(e -> searchArticles());
        vue.getRefreshButton().addActionListener(e -> loadArticles());
        vue.getAddToCartButton().addActionListener(e -> ajouterProduitSelectionneAuPanier());
        vue.getViewCartButton().addActionListener(e -> cartView.setVisible(true));
        vue.getCheckoutButton().addActionListener(e -> cartView.setVisible(true));

    }



    private void loadArticles() {
        DefaultTableModel model = vue.getTableModel();
        model.setRowCount(0);
        vue.clearBrandComboBox();
        vue.addBrand("Toutes les marques");

        ProduitDao dao = new ProduitDao();
        List<Produit> produits = dao.getAllProduitsAvecMarque();
        Set<String> marquesVues = new HashSet<>();

        for (Produit p : produits) {
            model.addRow(new Object[]{
                    p.getIdProduit(), p.getNom(), p.getPrixUnitaire(), p.getPrixLotPromo(),
                    p.getQteLotPromo(), p.getNomMarque(), p.getStock()
            });

            if (!marquesVues.contains(p.getNomMarque())) {
                marquesVues.add(p.getNomMarque());
                vue.addBrand(p.getNomMarque());
            }
        }

        vue.updateStatus("Catalogue chargé : " + produits.size() + " produit(s)");
    }

    private void searchArticles() {
        String texte = vue.getSearchText().trim().toLowerCase();
        String marqueChoisie = vue.getSelectedBrand();
        boolean filtrerMarque = marqueChoisie != null && !marqueChoisie.equals("Toutes les marques");

        ProduitDao dao = new ProduitDao();
        List<Produit> produits = dao.getAllProduitsAvecMarque();

        DefaultTableModel model = vue.getTableModel();
        model.setRowCount(0);
        int total = 0;

        for (Produit p : produits) {
            boolean matchNom = texte.isEmpty() || p.getNom().toLowerCase().contains(texte);
            boolean matchMarque = !filtrerMarque || p.getNomMarque().equalsIgnoreCase(marqueChoisie);

            if (matchNom && matchMarque) {
                model.addRow(new Object[]{
                        p.getIdProduit(), p.getNom(), p.getPrixUnitaire(), p.getPrixLotPromo(),
                        p.getQteLotPromo(), p.getNomMarque(), p.getStock()
                });
                total++;
            }
        }

        vue.updateStatus(total + " produit(s) trouvé(s)");
    }

    private void ajouterProduitSelectionneAuPanier() {
        int row = vue.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(vue, "Veuillez sélectionner un article.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id = (int) vue.getValueAt(row, 0);
        String nom = (String) vue.getValueAt(row, 1);
        double prixU = (double) vue.getValueAt(row, 2);
        double prixLot = (double) vue.getValueAt(row, 3);
        int seuil = (int) vue.getValueAt(row, 4);
        int stock = (int) vue.getValueAt(row, 6);

        String input = JOptionPane.showInputDialog(vue, "Quantité à ajouter au panier :", "Quantité", JOptionPane.PLAIN_MESSAGE);
        if (input == null) return;

        int quantite;
        try {
            quantite = Integer.parseInt(input);
            if (quantite <= 0 || quantite > stock) {
                JOptionPane.showMessageDialog(vue, "Quantité invalide ou supérieure au stock.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(vue, "Veuillez entrer un nombre valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int lots = seuil > 0 ? quantite / seuil : 0;
        int reste = seuil > 0 ? quantite % seuil : quantite;
        double sousTotal = lots * prixLot + reste * prixU;

        cartView.addItemToCart(id, nom, prixU, quantite, prixLot, seuil, sousTotal);
        JOptionPane.showMessageDialog(vue, "Produit ajouté au panier !");
    }
}
