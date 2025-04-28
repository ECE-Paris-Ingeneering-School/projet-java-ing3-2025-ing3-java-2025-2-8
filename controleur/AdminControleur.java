package controleur;

import dao.ProduitDao;
import modele.Produit;
import vue.AdminFrame;
import vue.FormulaireProduit;

import javax.swing.*;
import java.util.List;

public class AdminControleur {
    private AdminFrame vue;
    private ProduitDao produitDao;

    public AdminControleur(AdminFrame vue) {
        this.vue = vue;
        this.produitDao = new ProduitDao();
        initListeners();
        loadProduits();
    }

    private void initListeners() {
        vue.getAddButton().addActionListener(e -> ouvrirFormulaireAjout());
        vue.getEditButton().addActionListener(e -> ouvrirFormulaireEdition());
        vue.getDeleteButton().addActionListener(e -> supprimerProduit());
    }

    private void ouvrirFormulaireAjout() {
        FormulaireProduit form = new FormulaireProduit(vue, this, null);
        form.setVisible(true);
    }

    private void ouvrirFormulaireEdition() {
        Produit produit = vue.getSelectedProduit();
        if (produit == null) {
            JOptionPane.showMessageDialog(vue, "Veuillez sélectionner un produit.");
            return;
        }
        FormulaireProduit form = new FormulaireProduit(vue, this, produit);
        form.setVisible(true);
    }

    private void supprimerProduit() {
        Produit produit = vue.getSelectedProduit();
        if (produit == null) {
            JOptionPane.showMessageDialog(vue, "Sélectionnez un produit à supprimer.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(vue, "Supprimer ce produit ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (produitDao.supprimerProduit(produit.getIdProduit())) {
                loadProduits();
                JOptionPane.showMessageDialog(vue, "Produit supprimé !");
            } else {
                JOptionPane.showMessageDialog(vue, "Erreur lors de la suppression.");
            }
        }
    }

    public void loadProduits() {
        List<Produit> produits = produitDao.getAllProduitsAvecMarque();
        vue.remplirTableau(produits);
    }

    public boolean ajouterProduit(Produit produit) {
        return produitDao.ajouterProduit(produit);
    }

    public boolean modifierProduit(Produit produit) {
        return produitDao.modifierProduit(produit);
    }
}
