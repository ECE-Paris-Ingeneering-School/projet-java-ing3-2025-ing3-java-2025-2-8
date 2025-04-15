package vue;

import dao.LigneCommandeDAO;
import modele.LigneCommande;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DetailsCommandeDialog extends JDialog {

    public DetailsCommandeDialog(JFrame parent, int idCommande) {
        super(parent, "Détails de la commande n°" + idCommande, true);
        setSize(600, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        String[] columns = {"Produit ID", "Quantité", "Prix Unitaire (€)", "Sous-total (€)"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Récupérer les lignes de la commande
        List<LigneCommande> lignes = new LigneCommandeDAO().getLignesParCommande(idCommande);
        for (LigneCommande l : lignes) {
            model.addRow(new Object[]{
                    l.getIdProduit(),
                    l.getQuantite(),
                    l.getPrixUnitaire(),
                    l.getSousTotal()
            });
        }
    }
}
