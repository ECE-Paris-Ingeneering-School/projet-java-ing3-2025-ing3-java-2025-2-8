package vue;

import dao.LigneCommandeDAO;
import modele.LigneCommande;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DetailsCommandeDialog extends JDialog {
    private JTable table;

    public DetailsCommandeDialog(JFrame parent, int idCommande) {
        super(parent, "Détails de la commande n°" + idCommande, true);
        setSize(600, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // Création du modèle avec colonnes
        String[] colonnes = {"Nom du produit", "Quantité", "Prix unitaire", "Sous-total"};
        DefaultTableModel model = new DefaultTableModel(colonnes, 0);

        // Création de la table
        table = new JTable(model);
        table.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Récupération des lignes
        List<LigneCommande> lignes = new LigneCommandeDAO().getLignesParCommande(idCommande);
        for (LigneCommande l : lignes) {
            model.addRow(new Object[]{
                    l.getNomProduit(),  // doit exister dans LigneCommande.java
                    l.getQuantite(),
                    l.getPrixUnitaire(),
                    l.getSousTotal()
            });
        }

        // Bouton fermer
        JButton fermerBtn = new JButton("Fermer");
        fermerBtn.addActionListener(e -> dispose());
        JPanel btnPanel = new JPanel();
        btnPanel.add(fermerBtn);
        add(btnPanel, BorderLayout.SOUTH);
    }
}
