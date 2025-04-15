package vue;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Vector;
import java.util.ArrayList;
import java.util.List;

/**
 * CartView - Vue du panier d’achat
 */
public class CartView extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private JLabel totalLabel;
    private CatalogView catalogView;
    private JButton checkoutButton;

    public CartView(CatalogView catalogView) {
        this.catalogView = catalogView;

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

        checkoutButton.addActionListener(e -> proceedToCheckout());

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

    public JTable getTable() {
        return table;
    }

    public CatalogView getCatalogView() {
        return catalogView;
    }

    public void proceedToCheckout() {
        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this,
                    "Votre panier est vide. Veuillez ajouter des articles avant de passer au paiement.",
                    "Panier vide",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        List<PaymentView.OrderItem> orderItems = new ArrayList<>();
        for (int i = 0; i < model.getRowCount(); i++) {
            int id = (int) model.getValueAt(i, 0);
            String nom = (String) model.getValueAt(i, 1);
            double prixUnitaire = (double) model.getValueAt(i, 2);
            int quantite = (int) model.getValueAt(i, 3);
            double sousTotal = (double) model.getValueAt(i, 6);

            PaymentView.OrderItem item = new PaymentView.OrderItem(id, nom, prixUnitaire, quantite, sousTotal);
            orderItems.add(item);
        }

        double total = 0.0;
        for (PaymentView.OrderItem item : orderItems) {
            total += item.getSubtotal();
        }

        PaymentView paymentView = new PaymentView(this, orderItems, total);
        this.setVisible(false);
        paymentView.setVisible(true);
    }
}
