//test
package vue;

import modele.Utilisateur;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CatalogView extends JFrame {

    private Utilisateur utilisateur;
    private JTextField searchField;
    private JComboBox<String> brandComboBox;
    private JTable articlesTable;
    private DefaultTableModel tableModel;
    private JLabel statusLabel;
    private JButton searchButton;
    private JButton refreshButton;
    private JButton addToCartButton;
    private JButton viewCartButton;
    private JButton checkoutButton;
    private JButton returnButton;


    public CatalogView(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;

        setTitle("Catalogue - Application Shopping");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        JLabel welcomeLabel = new JLabel("Bienvenue, " + utilisateur.getNom(), JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(welcomeLabel, BorderLayout.NORTH);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        JPanel ligne1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        viewCartButton = new JButton("Voir le Panier");
        checkoutButton = new JButton("Payer maintenant");
        returnButton = new JButton("Retour");


        ligne1.add(viewCartButton);
        ligne1.add(checkoutButton);
        ligne1.add(returnButton);

        JPanel ligne2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchField = new JTextField(15);
        brandComboBox = new JComboBox<>();
        brandComboBox.addItem("Toutes les marques");

        searchButton = new JButton("Rechercher");
        refreshButton = new JButton("Rafraîchir");

        ligne2.add(new JLabel("Recherche :"));
        ligne2.add(searchField);
        ligne2.add(new JLabel("Marque :"));
        ligne2.add(brandComboBox);
        ligne2.add(searchButton);
        ligne2.add(refreshButton);

        topPanel.add(ligne1);
        topPanel.add(ligne2);

        add(topPanel, BorderLayout.PAGE_START);

        String[] columns = {"ID", "Nom", "Prix unitaire", "Prix en lot", "Qté lot", "Marque", "Stock"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        articlesTable = new JTable(tableModel);
        articlesTable.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(articlesTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());

        statusLabel = new JLabel(" ", JLabel.CENTER);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));
        bottomPanel.add(statusLabel, BorderLayout.NORTH);

        addToCartButton = new JButton("Ajouter au panier");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addToCartButton);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    // --- Getters pour le contrôleur ---
    public JButton getSearchButton() {
        return searchButton;
    }

    public JButton getRefreshButton() {
        return refreshButton;
    }

    public JButton getAddToCartButton() {
        return addToCartButton;
    }

    public JButton getViewCartButton() {
        return viewCartButton;
    }

    public JButton getCheckoutButton() {
        return checkoutButton;
    }

    public String getSearchText() {
        return searchField.getText();
    }

    public String getSelectedBrand() {
        return (String) brandComboBox.getSelectedItem();
    }

    public void addBrand(String brand) {
        brandComboBox.addItem(brand);
    }

    public void clearBrandComboBox() {
        brandComboBox.removeAllItems();
    }

    public void updateStatus(String status) {
        statusLabel.setText(status);
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public int getSelectedRow() {
        return articlesTable.getSelectedRow();
    }
    public JButton getReturnButton() {
        return returnButton;
    }


    public Object getValueAt(int row, int column) {
        return tableModel.getValueAt(row, column);
    }
}
