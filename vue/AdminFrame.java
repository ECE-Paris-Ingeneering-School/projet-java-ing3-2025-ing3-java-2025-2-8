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

import modele.Produit;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminFrame extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private JButton addBtn, editBtn, delBtn;

    public AdminFrame() {
        setTitle("Administration - Gestion des Produits");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initComponents();
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

        addBtn = new JButton("Ajouter");
        editBtn = new JButton("Modifier");
        delBtn = new JButton("Supprimer");

        buttons.add(addBtn);
        buttons.add(editBtn);
        buttons.add(delBtn);

        add(buttons, BorderLayout.SOUTH);
        add(new StatsPanel(), BorderLayout.EAST);
    }

    public JButton getAddButton() {
        return addBtn;
    }

    public JButton getEditButton() {
        return editBtn;
    }

    public JButton getDeleteButton() {
        return delBtn;
    }

    public Produit getSelectedProduit() {
        int row = table.getSelectedRow();
        if (row == -1) {
            return null;
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
        return p;
    }

    public void remplirTableau(List<Produit> produits) {
        model.setRowCount(0);
        for (Produit p : produits) {
            model.addRow(new Object[]{
                    p.getIdProduit(), p.getNom(), p.getPrixUnitaire(),
                    p.getStock(), p.getQteLotPromo(), p.getPrixLotPromo(),
                    p.getNomMarque()
            });
        }
    }
}
