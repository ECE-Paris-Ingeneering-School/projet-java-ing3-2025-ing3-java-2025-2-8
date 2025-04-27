/**
 * Fichier : AdminFrame.java
 * - Support de cours Boostcamp/POO Java :
 *   > "Projet Java 2025 Shopping", JP Segado – Partie architecture MVC, DAO, et exigences sur l’interface graphique.
 *     Référence: https://boostcamp.omneseducation.com/course/view.php?id=377194&section=2
 * - Support de cours
 *   > "Cours 7 Java – Interface graphique Swing, AWT, Listeners", JP Segado – Utilisation des composants Swing (JFrame, JPanel, JTable, JButton, JScrollPane, etc.),
 *     gestion des layouts (BorderLayout), des événements (addActionListener), et exemples de code pour interfaces graphiques.
 *     Référence: PDF «Cours7-Java-ING3 S6 Interface graphique Swing, AWT, Listeners » / Boostcamp
 *   > Java Swing : https://docs.oracle.com/javase/8/docs/api/javax/swing/package-summary.html
 *   > Java AWT : https://docs.oracle.com/javase/8/docs/api/java/awt/package-summary.html
 * @author Adam Zeidan et Florent Meunier
 * @date Avril 2025
 */

package vue;

import dao.ProduitDao;
import modele.Produit;
import modele.Utilisateur;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminFrame extends JFrame {
    private JTable table;
    private DefaultTableModel model;

    public AdminFrame(Utilisateur utilisateur) {
        setTitle("Administration - Gestion des Produits");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initComponents();
        loadProduits();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        String[] columns = {"ID", "Nom", "Prix", "Stock", "QteLot", "PrixLot", "Marque"};
        model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(25);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttons = new JPanel();

        JButton addBtn = new JButton("Ajouter");
        JButton editBtn = new JButton("Modifier");
        JButton delBtn = new JButton("Supprimer");

        addBtn.addActionListener(e -> ouvrirFormulaireAjout());
        editBtn.addActionListener(e -> ouvrirFormulaireEdition());
        delBtn.addActionListener(e -> supprimerProduit());

        buttons.add(addBtn);
        buttons.add(editBtn);
        buttons.add(delBtn);

        add(buttons, BorderLayout.SOUTH);
        StatsPanel statsPanel = new StatsPanel();
        add(statsPanel, BorderLayout.EAST);

    }

    private void ouvrirFormulaireAjout() {
        FormulaireProduit form = new FormulaireProduit(this, null);
        form.setVisible(true);
    }

    private void ouvrirFormulaireEdition() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un produit.");
            return;
        }

        Produit p = new Produit(
                (int) model.getValueAt(row, 0),
                (String) model.getValueAt(row, 1),
                (double) model.getValueAt(row, 2),
                (int) model.getValueAt(row, 3),
                (int) model.getValueAt(row, 4),
                (double) model.getValueAt(row, 5),
                0
        );
        p.setNomMarque((String) model.getValueAt(row, 6));

        FormulaireProduit form = new FormulaireProduit(this, p);
        form.setVisible(true);
    }

    private void supprimerProduit() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un produit à supprimer.");
            return;
        }

        int id = (int) model.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Supprimer ce produit ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (new ProduitDao().supprimerProduit(id)) {
                loadProduits();
                JOptionPane.showMessageDialog(this, "Produit supprimé !");
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de la suppression.");
            }
        }
    }

    public void loadProduits() {
        model.setRowCount(0);
        List<Produit> produits = new ProduitDao().getAllProduitsAvecMarque();
        for (Produit p : produits) {
            model.addRow(new Object[]{
                    p.getIdProduit(), p.getNom(), p.getPrixUnitaire(),
                    p.getStock(), p.getQteLotPromo(), p.getPrixLotPromo(),
                    p.getNomMarque()
            });
        }
    }
}
