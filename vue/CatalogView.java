package vue;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * CatalogView - Interface graphique pour l'affichage du catalogue des articles
 * Cette classe permet au client de visualiser, rechercher et filtrer les articles disponibles
 */
public class CatalogView extends JFrame {

    // Composants de l'interface
    private JTextField searchField;
    private JComboBox<String> brandComboBox;
    private JButton searchButton;
    private JButton refreshButton;
    private JButton addToCartButton;
    private JButton viewCartButton;
    private JTable articlesTable;
    private DefaultTableModel tableModel;
    private JLabel statusLabel;

    // Référence au panier
    private CartView cartView;

    /**
     * Constructeur de la fenêtre du catalogue
     */
    public CatalogView() {
        // Configuration de la fenêtre principale
        setTitle("Catalogue des Articles");
        setSize(800, 600);
        setMinimumSize(new Dimension(700, 500));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialisation du panier
        cartView = new CartView(this);

        // Initialisation des composants
        initComponents();

        // Chargement initial des données
        loadArticles();
    }

    /**
     * Initialise et configure les composants de l'interface
     */
    private void initComponents() {
        // Utilisation d'un BorderLayout pour la fenêtre principale
        setLayout(new BorderLayout(10, 10));

        // Panel Nord : Recherche et filtrage
        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);

        // Panel Centre : Tableau des articles
        JPanel centerPanel = createCenterPanel();
        add(centerPanel, BorderLayout.CENTER);

        // Panel Sud : Actions et statut
        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * Crée le panneau supérieur avec les contrôles de recherche
     * @return le panneau configuré
     */
    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Label de recherche
        JLabel searchLabel = new JLabel("Recherche : ");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.0;
        panel.add(searchLabel, gbc);

        // Champ de recherche
        searchField = new JTextField(20);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(searchField, gbc);

        // Label de marque
        JLabel brandLabel = new JLabel("Marque : ");
        gbc.gridx = 2;
        gbc.weightx = 0.0;
        panel.add(brandLabel, gbc);

        // Liste déroulante des marques
        brandComboBox = new JComboBox<>();
        brandComboBox.addItem("Toutes les marques");
        // Les marques seront ajoutées dynamiquement depuis la base de données
        // Exemple d'ajout de marques pour le test
        brandComboBox.addItem("Apple");
        brandComboBox.addItem("Samsung");
        brandComboBox.addItem("Sony");
        brandComboBox.addItem("HP");
        brandComboBox.addItem("Dell");

        gbc.gridx = 3;
        gbc.weightx = 0.5;
        panel.add(brandComboBox, gbc);

        // Bouton de recherche
        searchButton = new JButton("Rechercher");
        gbc.gridx = 4;
        gbc.weightx = 0.0;
        panel.add(searchButton, gbc);

        // Configuration de l'action du bouton de recherche
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchArticles();
            }
        });

        return panel;
    }

    /**
     * Crée le panneau central avec le tableau des articles
     * @return le panneau configuré
     */
    private JPanel createCenterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        // Création des colonnes du tableau
        String[] columnNames = {
                "ID", "Nom de l'article", "Prix unitaire", "Prix en gros",
                "Seuil quantité", "Marque", "Stock"
        };

        // Création du modèle de tableau
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Empêcher l'édition directe des cellules
                return false;
            }
        };

        // Création du tableau
        articlesTable = new JTable(tableModel);

        // Configuration de l'apparence du tableau
        articlesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        articlesTable.setRowHeight(25);
        articlesTable.getTableHeader().setReorderingAllowed(false);

        // Configuration des largeurs de colonnes
        articlesTable.getColumnModel().getColumn(0).setPreferredWidth(50);    // ID
        articlesTable.getColumnModel().getColumn(1).setPreferredWidth(250);   // Nom
        articlesTable.getColumnModel().getColumn(2).setPreferredWidth(100);   // Prix unitaire
        articlesTable.getColumnModel().getColumn(3).setPreferredWidth(100);   // Prix en gros
        articlesTable.getColumnModel().getColumn(4).setPreferredWidth(80);    // Seuil
        articlesTable.getColumnModel().getColumn(5).setPreferredWidth(120);   // Marque
        articlesTable.getColumnModel().getColumn(6).setPreferredWidth(80);    // Stock

        // Ajout d'un écouteur pour le double-clic sur une ligne du tableau
        articlesTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = articlesTable.getSelectedRow();
                    if (selectedRow != -1) {
                        // Récupérer les informations de l'article sélectionné
                        int articleId = (int) tableModel.getValueAt(selectedRow, 0);
                        String articleName = (String) tableModel.getValueAt(selectedRow, 1);
                        double unitPrice = (double) tableModel.getValueAt(selectedRow, 2);

                        // Ouvrir la boîte de dialogue pour ajouter au panier
                        openAddToCartDialog(articleId, articleName, unitPrice);
                    }
                }
            }
        });

        // Ajout du tableau à un JScrollPane pour permettre le défilement
        JScrollPane scrollPane = new JScrollPane(articlesTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Crée le panneau inférieur avec les boutons d'action
     * @return le panneau configuré
     */
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Sous-panneau pour les boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));

        // Bouton de rafraîchissement
        refreshButton = new JButton("Rafraîchir");
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadArticles();
            }
        });
        buttonPanel.add(refreshButton);

        // Bouton d'ajout au panier
        addToCartButton = new JButton("Ajouter au panier");
        addToCartButton.setEnabled(false); // Désactivé par défaut
        addToCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = articlesTable.getSelectedRow();
                if (selectedRow != -1) {
                    int articleId = (int) tableModel.getValueAt(selectedRow, 0);
                    String articleName = (String) tableModel.getValueAt(selectedRow, 1);
                    double unitPrice = (double) tableModel.getValueAt(selectedRow, 2);

                    openAddToCartDialog(articleId, articleName, unitPrice);
                }
            }
        });
        buttonPanel.add(addToCartButton);

        // Bouton pour voir le panier
        viewCartButton = new JButton("Voir le panier");
        viewCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showCart();
            }
        });
        buttonPanel.add(viewCartButton);

        // Activation du bouton d'ajout au panier lorsqu'une ligne est sélectionnée
        articlesTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                addToCartButton.setEnabled(articlesTable.getSelectedRow() != -1);
            }
        });

        // Label de statut
        statusLabel = new JLabel(" ");
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        // Ajout des composants au panneau
        panel.add(buttonPanel, BorderLayout.CENTER);
        panel.add(statusLabel, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Charge la liste des articles dans le tableau
     * Cette méthode sera remplacée par un appel au contrôleur
     */
    private void loadArticles() {
        // Vider le tableau
        tableModel.setRowCount(0);

        // Ajouter des données de test (à remplacer par des données réelles)
        // Format: ID, Nom, Prix unitaire, Prix en gros, Seuil quantité, Marque, Stock
        Object[][] testData = {
                {1, "iPhone 13", 899.99, 849.99, 5, "Apple", 50},
                {2, "Galaxy S21", 799.99, 759.99, 5, "Samsung", 45},
                {3, "MacBook Pro", 1299.99, 1199.99, 3, "Apple", 30},
                {4, "Sony WH-1000XM4", 349.99, 299.99, 10, "Sony", 60},
                {5, "HP LaserJet Pro", 249.99, 219.99, 5, "HP", 25},
                {6, "Dell XPS 15", 1499.99, 1399.99, 3, "Dell", 20},
                {7, "iPad Pro", 799.99, 749.99, 5, "Apple", 40},
                {8, "Samsung TV QLED", 1099.99, 999.99, 3, "Samsung", 15},
                {9, "Sony PlayStation 5", 499.99, 479.99, 5, "Sony", 10},
                {10, "HP Pavilion", 699.99, 649.99, 5, "HP", 35}
        };

        // Ajouter les données au tableau
        for (Object[] row : testData) {
            tableModel.addRow(row);
        }

        // Mettre à jour le statut
        statusLabel.setText("Catalogue chargé avec " + tableModel.getRowCount() + " articles");
    }

    /**
     * Recherche les articles selon les critères spécifiés
     */
    private void searchArticles() {
        String searchText = searchField.getText().toLowerCase().trim();
        String selectedBrand = (String) brandComboBox.getSelectedItem();

        // Si "Toutes les marques" est sélectionné, on ignore le filtre de marque
        boolean filterByBrand = !selectedBrand.equals("Toutes les marques");

        // Vider le tableau
        tableModel.setRowCount(0);

        // Données de test (à remplacer par des données réelles)
        // Format: ID, Nom, Prix unitaire, Prix en gros, Seuil quantité, Marque, Stock
        Object[][] testData = {
                {1, "iPhone 13", 899.99, 849.99, 5, "Apple", 50},
                {2, "Galaxy S21", 799.99, 759.99, 5, "Samsung", 45},
                {3, "MacBook Pro", 1299.99, 1199.99, 3, "Apple", 30},
                {4, "Sony WH-1000XM4", 349.99, 299.99, 10, "Sony", 60},
                {5, "HP LaserJet Pro", 249.99, 219.99, 5, "HP", 25},
                {6, "Dell XPS 15", 1499.99, 1399.99, 3, "Dell", 20},
                {7, "iPad Pro", 799.99, 749.99, 5, "Apple", 40},
                {8, "Samsung TV QLED", 1099.99, 999.99, 3, "Samsung", 15},
                {9, "Sony PlayStation 5", 499.99, 479.99, 5, "Sony", 10},
                {10, "HP Pavilion", 699.99, 649.99, 5, "HP", 35}
        };

        // Filtrer les données selon les critères de recherche
        int matchCount = 0;
        for (Object[] row : testData) {
            String articleName = ((String) row[1]).toLowerCase();
            String brand = (String) row[5];

            boolean nameMatches = searchText.isEmpty() || articleName.contains(searchText);
            boolean brandMatches = !filterByBrand || brand.equals(selectedBrand);

            if (nameMatches && brandMatches) {
                tableModel.addRow(row);
                matchCount++;
            }
        }

        // Mettre à jour le statut
        if (matchCount == 0) {
            statusLabel.setText("Aucun article ne correspond aux critères de recherche");
        } else {
            statusLabel.setText(matchCount + " article(s) trouvé(s)");
        }
    }

    /**
     * Ouvre la vue du panier
     */
    private void showCart() {
        cartView.setVisible(true);
    }

    /**
     * Ouvre une boîte de dialogue pour ajouter un article au panier
     * @param articleId ID de l'article
     * @param articleName Nom de l'article
     * @param unitPrice Prix unitaire de l'article
     */
    private void openAddToCartDialog(int articleId, String articleName, double unitPrice) {
        // Création de la boîte de dialogue
        JDialog dialog = new JDialog(this, "Ajouter au panier", true);
        dialog.setSize(350, 200);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));

        // Panneau principal
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Information sur l'article
        JLabel nameLabel = new JLabel("Article : " + articleName);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(nameLabel, gbc);

        JLabel priceLabel = new JLabel("Prix unitaire : " + String.format("%.2f €", unitPrice));
        gbc.gridy = 1;
        mainPanel.add(priceLabel, gbc);

        // Sélection de la quantité
        JLabel quantityLabel = new JLabel("Quantité : ");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        mainPanel.add(quantityLabel, gbc);

        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1, 1, 100, 1);
        JSpinner quantitySpinner = new JSpinner(spinnerModel);
        gbc.gridx = 1;
        mainPanel.add(quantitySpinner, gbc);

        // Panneau de boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));

        JButton addButton = new JButton("Ajouter");
        JButton cancelButton = new JButton("Annuler");

        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);

        // Ajouter les panneaux à la boîte de dialogue
        dialog.add(mainPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // Configuration des actions des boutons
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int quantity = (int) quantitySpinner.getValue();
                addArticleToCart(articleId, articleName, unitPrice, quantity);
                dialog.dispose();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        // Afficher la boîte de dialogue
        dialog.setVisible(true);
    }

    /**
     * Ajoute un article au panier
     * Cette méthode sera remplacée par un appel au contrôleur
     * @param articleId ID de l'article
     * @param articleName Nom de l'article
     * @param unitPrice Prix unitaire de l'article
     * @param quantity Quantité à ajouter
     */
    private void addArticleToCart(int articleId, String articleName, double unitPrice, int quantity) {
        // Récupérer les informations complètes de l'article
        double bulkPrice = 0.0;
        int bulkThreshold = 0;

        // Recherche des informations détaillées de l'article
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if ((int)tableModel.getValueAt(i, 0) == articleId) {
                bulkPrice = (double)tableModel.getValueAt(i, 3);
                bulkThreshold = (int)tableModel.getValueAt(i, 4);
                break;
            }
        }

        // Calculer le sous-total
        double subtotal = calculateSubtotal(unitPrice, bulkPrice, quantity, bulkThreshold);

        // Ajouter l'article au panier
        cartView.addItemToCart(articleId, articleName, unitPrice, quantity, bulkPrice, bulkThreshold, subtotal);

        // Afficher un message de confirmation
        String message = quantity + " × " + articleName + " ajouté(s) au panier\n";
        message += "Total : " + String.format("%.2f €", subtotal);

        JOptionPane.showMessageDialog(this, message, "Article ajouté", JOptionPane.INFORMATION_MESSAGE);

        // Mettre à jour le statut
        statusLabel.setText("Article ajouté au panier : " + articleName + " (Quantité : " + quantity + ")");
    }

    /**
     * Calcule le sous-total pour un article en tenant compte des remises en gros
     * @param unitPrice Prix unitaire
     * @param bulkPrice Prix en gros
     * @param quantity Quantité
     * @param bulkThreshold Seuil pour le prix en gros
     * @return le sous-total calculé
     */
    private double calculateSubtotal(double unitPrice, double bulkPrice, int quantity, int bulkThreshold) {
        double subtotal = 0.0;

        // Appliquer le prix en gros pour autant de groupes complets que possible
        int bulkGroups = quantity / bulkThreshold;
        int remainingItems = quantity % bulkThreshold;

        // Prix des groupes en gros
        if (bulkGroups > 0 && bulkPrice > 0 && bulkThreshold > 0) {
            subtotal += bulkGroups * bulkPrice;
        } else {
            bulkGroups = 0;
        }

        // Prix des articles restants au prix unitaire
        subtotal += (quantity - (bulkGroups * bulkThreshold)) * unitPrice;

        return subtotal;
    }

    /**
     * Méthode principale pour tester la vue
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                CatalogView catalogView = new CatalogView();
                catalogView.setVisible(true);
            }
        });
    }
}