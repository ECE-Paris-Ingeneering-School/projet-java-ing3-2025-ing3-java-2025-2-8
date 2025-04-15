package vue;

import dao.ProduitDao;
import modele.Produit;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class FormulaireProduit extends JDialog {
    private JTextField nomField, prixField, stockField, qteLotField, prixLotField, marqueField;
    private Produit produit;
    private AdminFrame adminFrame;

    public FormulaireProduit(AdminFrame adminFrame, Produit produit) {
        super(adminFrame, "Formulaire Produit", true);
        this.adminFrame = adminFrame;
        this.produit = produit;

        setSize(400, 400);
        setLocationRelativeTo(adminFrame);
        setLayout(new GridLayout(7, 2, 10, 10));

        nomField = new JTextField();
        prixField = new JTextField();
        stockField = new JTextField();
        qteLotField = new JTextField();
        prixLotField = new JTextField();
        marqueField = new JTextField();

        add(new JLabel("Nom :"));
        add(nomField);
        add(new JLabel("Prix unitaire :"));
        add(prixField);
        add(new JLabel("Stock :"));
        add(stockField);
        add(new JLabel("Qté lot promo :"));
        add(qteLotField);
        add(new JLabel("Prix lot promo :"));
        add(prixLotField);
        add(new JLabel("Marque :"));
        add(marqueField);

        JButton valider = new JButton("Valider");
        JButton annuler = new JButton("Annuler");
        add(valider);
        add(annuler);

        if (produit != null) {
            nomField.setText(produit.getNom());
            prixField.setText(String.valueOf(produit.getPrixUnitaire()));
            stockField.setText(String.valueOf(produit.getStock()));
            qteLotField.setText(String.valueOf(produit.getQteLotPromo()));
            prixLotField.setText(String.valueOf(produit.getPrixLotPromo()));
            marqueField.setText(produit.getNomMarque());
        }

        valider.addActionListener(this::valider);
        annuler.addActionListener(e -> dispose());
    }

    private void valider(ActionEvent e) {
        try {
            String nom = nomField.getText();
            double prix = Double.parseDouble(prixField.getText());
            int stock = Integer.parseInt(stockField.getText());
            int qteLot = Integer.parseInt(qteLotField.getText());
            double prixLot = Double.parseDouble(prixLotField.getText());
            String marque = marqueField.getText();

            boolean success;
            ProduitDao dao = new ProduitDao();

            if (produit == null) {
                produit = new Produit(0, nom, prix, stock, qteLot, prixLot, 0);
                produit.setNomMarque(marque);
                success = dao.ajouterProduit(produit);
            } else {
                produit.setNom(nom);
                produit.setPrixUnitaire(prix);
                produit.setStock(stock);
                produit.setQteLotPromo(qteLot);
                produit.setPrixLotPromo(prixLot);
                produit.setNomMarque(marque);
                success = dao.modifierProduit(produit);
            }

            if (success) {
                adminFrame.loadProduits();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'enregistrement en base de données.");
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Vérifiez vos champs numériques !");
        }
    }
}
