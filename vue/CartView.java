package vue;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CartView extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private JLabel totalLabel;
    private JFrame parent;
    private JButton clearButton;
    private JButton retourButton;
    private JButton checkoutButton;

    public CartView(JFrame parent) {
        this.parent = parent;

        setTitle("Panier");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

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
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout());

        totalLabel = new JLabel("Total : 0.00 €");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        bottom.add(totalLabel, BorderLayout.WEST);

        clearButton = new JButton("Vider");
        retourButton = new JButton("Continuer les achats");
        checkoutButton = new JButton("Passer au paiement");

        JPanel btnPanel = new JPanel();
        btnPanel.add(clearButton);
        btnPanel.add(retourButton);
        btnPanel.add(checkoutButton);

        bottom.add(btnPanel, BorderLayout.EAST);
        add(bottom, BorderLayout.SOUTH);
    }

    // Méthodes exposées pour le contrôleur
    public JButton getClearButton() {
        return clearButton;
    }

    public JButton getRetourButton() {
        return retourButton;
    }

    public JButton getCheckoutButton() {
        return checkoutButton;
    }

    public JTable getTable() {
        return table;
    }

    public DefaultTableModel getModel() {
        return model;
    }

    public JLabel getTotalLabel() {
        return totalLabel;
    }

    public JFrame getParentWindow() {
        return parent;
    }

    public void updateTotal(double total) {
        totalLabel.setText("Total : " + String.format("%.2f €", total));
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

        model.addRow(new Object[]{id, nom, prixUnitaire, qte, prixLot, seuil, sousTotal});
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
        updateTotal(total);
    }


}
