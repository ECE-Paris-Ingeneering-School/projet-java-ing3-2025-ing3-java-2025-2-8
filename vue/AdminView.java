package vue;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * AdminView - Interface graphique pour la gestion du catalogue et la visualisation des statistiques
 * Cette classe est réservée aux administrateurs de l'application
 */
public class AdminView extends JFrame {

    // Composants principaux
    private JTabbedPane tabbedPane;
    private JTable articlesTable;
    private DefaultTableModel tableModel;

    // Composants de l'onglet Articles
    private JTextField idField;
    private JTextField nameField;
    private JTextField unitPriceField;
    private JTextField bulkPriceField;
    private JTextField thresholdField;
    private JComboBox<String> brandComboBox;
    private JSpinner stockSpinner;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton clearButton;

    // Composants de l'onglet Reporting
    private JPanel chartPanel;
    private JComboBox<String> reportTypeComboBox;
    private JComboBox<String> periodComboBox;
    private JButton generateReportButton;

    /**
     * Constructeur de la fenêtre d'administration
     */
    public AdminView() {
        // Configuration de la fenêtre principale
        setTitle("Interface Administrateur - Gestion du Shopping");
        setSize(1000, 700);
        setMinimumSize(new Dimension(800, 600));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialisation des onglets et composants
        initComponents();
    }

    /**
     * Initialise les composants de l'interface
     */
    private void initComponents() {
        // Création du JTabbedPane pour les onglets
        tabbedPane = new JTabbedPane();

        // Création et ajout des onglets
        JPanel articlesPanel = createArticlesPanel();
        JPanel reportingPanel = createReportingPanel();

        tabbedPane.addTab("Articles", new ImageIcon(), articlesPanel, "Gestion des articles");
        tabbedPane.addTab("Reporting", new ImageIcon(), reportingPanel, "Statistiques et graphiques");

        // Ajout du JTabbedPane à la fenêtre
        getContentPane().add(tabbedPane);
    }

    /**
     * Crée le panneau de gestion des articles
     * @return le panneau configuré
     */
    private JPanel createArticlesPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panneau pour le tableau des articles
        JPanel tablePanel = createArticlesTablePanel();

        // Panneau pour le formulaire d'ajout/modification
        JPanel formPanel = createArticlesFormPanel();

        // Ajout des panneaux au panneau principal
        panel.add(tablePanel, BorderLayout.CENTER);
        panel.add(formPanel, BorderLayout.EAST);

        return panel;
    }

    /**
     * Crée le panneau contenant le tableau des articles
     * @return le panneau configuré
     */
    private JPanel createArticlesTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Liste des articles"));

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

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) return Integer.class;
                if (columnIndex == 2 || columnIndex == 3) return Double.class;
                if (columnIndex == 4 || columnIndex == 6) return Integer.class;
                return String.class;
            }
        };

        // Création du tableau
        articlesTable = new JTable(tableModel);
        articlesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        articlesTable.setRowHeight(25);

        // Configuration des largeurs de colonnes
        articlesTable.getColumnModel().getColumn(0).setPreferredWidth(50);    // ID
        articlesTable.getColumnModel().getColumn(1).setPreferredWidth(250);   // Nom
        articlesTable.getColumnModel().getColumn(2).setPreferredWidth(100);   // Prix unitaire
        articlesTable.getColumnModel().getColumn(3).setPreferredWidth(100);   // Prix en gros
        articlesTable.getColumnModel().getColumn(4).setPreferredWidth(100);   // Seuil
        articlesTable.getColumnModel().getColumn(5).setPreferredWidth(120);   // Marque
        articlesTable.getColumnModel().getColumn(6).setPreferredWidth(80);    // Stock

        // Ajout d'un écouteur pour le clic sur une ligne du tableau
        articlesTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = articlesTable.getSelectedRow();
                if (selectedRow != -1) {
                    // Remplissage des champs avec les données de la ligne sélectionnée
                    fillFormFromSelectedRow(selectedRow);

                    // Activation des boutons de modification et suppression
                    updateButton.setEnabled(true);
                    deleteButton.setEnabled(true);
                }
            }
        });

        // Ajout du tableau à un JScrollPane
        JScrollPane scrollPane = new JScrollPane(articlesTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Chargement des données initiales
        loadArticlesData();

        return panel;
    }

    /**
     * Crée le panneau contenant le formulaire d'ajout/modification d'article
     * @return le panneau configuré
     */
    private JPanel createArticlesFormPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Détails de l'article"));
        panel.setPreferredSize(new Dimension(300, 0));

        // Panel pour les champs de formulaire
        JPanel formFieldsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Champ ID (non éditable)
        JLabel idLabel = new JLabel("ID:");
        idField = new JTextField(10);
        idField.setEditable(false);

        gbc.gridx = 0;
        gbc.gridy = 0;
        formFieldsPanel.add(idLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formFieldsPanel.add(idField, gbc);

        // Champ Nom
        JLabel nameLabel = new JLabel("Nom:");
        nameField = new JTextField(20);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        formFieldsPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formFieldsPanel.add(nameField, gbc);

        // Champ Prix unitaire
        JLabel unitPriceLabel = new JLabel("Prix unitaire:");
        unitPriceField = new JTextField(10);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        formFieldsPanel.add(unitPriceLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formFieldsPanel.add(unitPriceField, gbc);

        // Champ Prix en gros
        JLabel bulkPriceLabel = new JLabel("Prix en gros:");
        bulkPriceField = new JTextField(10);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        formFieldsPanel.add(bulkPriceLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formFieldsPanel.add(bulkPriceField, gbc);

        // Champ Seuil quantité
        JLabel thresholdLabel = new JLabel("Seuil quantité:");
        thresholdField = new JTextField(5);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.0;
        formFieldsPanel.add(thresholdLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formFieldsPanel.add(thresholdField, gbc);

        // Champ Marque
        JLabel brandLabel = new JLabel("Marque:");
        brandComboBox = new JComboBox<>();
        brandComboBox.setEditable(true);
        brandComboBox.addItem("Apple");
        brandComboBox.addItem("Samsung");
        brandComboBox.addItem("Sony");
        brandComboBox.addItem("HP");
        brandComboBox.addItem("Dell");

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0.0;
        formFieldsPanel.add(brandLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formFieldsPanel.add(brandComboBox, gbc);

        // Champ Stock
        JLabel stockLabel = new JLabel("Stock:");
        SpinnerNumberModel stockModel = new SpinnerNumberModel(0, 0, 1000, 1);
        stockSpinner = new JSpinner(stockModel);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 0.0;
        formFieldsPanel.add(stockLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formFieldsPanel.add(stockSpinner, gbc);

        // Panel pour les boutons
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 5, 5));

        addButton = new JButton("Ajouter");
        updateButton = new JButton("Modifier");
        deleteButton = new JButton("Supprimer");
        clearButton = new JButton("Effacer");

        // Désactiver les boutons de modification et suppression par défaut
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        // Configuration des actions des boutons
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addArticle();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateArticle();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteArticle();
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });

        // Ajout des panneaux au panneau principal
        panel.add(formFieldsPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Crée le panneau de reporting
     * @return le panneau configuré
     */
    private JPanel createReportingPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panneau pour les contrôles de reporting
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        controlPanel.setBorder(BorderFactory.createTitledBorder("Options du rapport"));

        // Type de rapport
        JLabel reportTypeLabel = new JLabel("Type de rapport:");
        reportTypeComboBox = new JComboBox<>();
        reportTypeComboBox.addItem("Ventes par marque");
        reportTypeComboBox.addItem("Ventes par mois");
        reportTypeComboBox.addItem("Articles les plus vendus");

        // Période
        JLabel periodLabel = new JLabel("Période:");
        periodComboBox = new JComboBox<>();
        periodComboBox.addItem("Aujourd'hui");
        periodComboBox.addItem("Cette semaine");
        periodComboBox.addItem("Ce mois");
        periodComboBox.addItem("Cette année");
        periodComboBox.addItem("Toutes les périodes");

        // Bouton de génération
        generateReportButton = new JButton("Générer le rapport");
        generateReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateReport();
            }
        });

        // Ajout des composants au panneau de contrôle
        controlPanel.add(reportTypeLabel);
        controlPanel.add(reportTypeComboBox);
        controlPanel.add(periodLabel);
        controlPanel.add(periodComboBox);
        controlPanel.add(generateReportButton);

        // Panneau pour le graphique
        chartPanel = new JPanel(new BorderLayout());
        chartPanel.setBorder(BorderFactory.createTitledBorder("Graphique"));

        // Label initial indiquant qu'aucun rapport n'est généré
        JLabel initialLabel = new JLabel("Aucun rapport généré. Utilisez les options ci-dessus pour générer un rapport.", JLabel.CENTER);
        initialLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        chartPanel.add(initialLabel, BorderLayout.CENTER);

        // Ajout des panneaux au panneau principal
        panel.add(controlPanel, BorderLayout.NORTH);
        panel.add(chartPanel, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Remplit le formulaire avec les données de la ligne sélectionnée
     * @param rowIndex index de la ligne sélectionnée
     */
    private void fillFormFromSelectedRow(int rowIndex) {
        idField.setText(tableModel.getValueAt(rowIndex, 0).toString());
        nameField.setText((String) tableModel.getValueAt(rowIndex, 1));
        unitPriceField.setText(tableModel.getValueAt(rowIndex, 2).toString());
        bulkPriceField.setText(tableModel.getValueAt(rowIndex, 3).toString());
        thresholdField.setText(tableModel.getValueAt(rowIndex, 4).toString());
        brandComboBox.setSelectedItem(tableModel.getValueAt(rowIndex, 5));
        stockSpinner.setValue(tableModel.getValueAt(rowIndex, 6));
    }

    /**
     * Efface le formulaire
     */
    private void clearForm() {
        idField.setText("");
        nameField.setText("");
        unitPriceField.setText("");
        bulkPriceField.setText("");
        thresholdField.setText("");
        brandComboBox.setSelectedIndex(0);
        stockSpinner.setValue(0);

        // Désactiver les boutons de modification et suppression
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);

        // Désélectionner la ligne du tableau
        articlesTable.clearSelection();
    }

    /**
     * Ajoute un nouvel article à partir des données du formulaire
     */
    private void addArticle() {
        // Vérification des champs obligatoires
        if (nameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez saisir un nom d'article", "Champ obligatoire", JOptionPane.WARNING_MESSAGE);
            nameField.requestFocus();
            return;
        }

        try {
            // Récupération des valeurs
            String name = nameField.getText().trim();
            double unitPrice = Double.parseDouble(unitPriceField.getText().trim());
            double bulkPrice = Double.parseDouble(bulkPriceField.getText().trim());
            int threshold = Integer.parseInt(thresholdField.getText().trim());
            String brand = brandComboBox.getSelectedItem().toString();
            int stock = (int) stockSpinner.getValue();

            // Vérification des valeurs
            if (unitPrice <= 0) {
                JOptionPane.showMessageDialog(this, "Le prix unitaire doit être supérieur à 0", "Valeur incorrecte", JOptionPane.WARNING_MESSAGE);
                unitPriceField.requestFocus();
                return;
            }

            if (bulkPrice <= 0) {
                JOptionPane.showMessageDialog(this, "Le prix en gros doit être supérieur à 0", "Valeur incorrecte", JOptionPane.WARNING_MESSAGE);
                bulkPriceField.requestFocus();
                return;
            }

            if (threshold <= 0) {
                JOptionPane.showMessageDialog(this, "Le seuil de quantité doit être supérieur à 0", "Valeur incorrecte", JOptionPane.WARNING_MESSAGE);
                thresholdField.requestFocus();
                return;
            }

            // Génération d'un nouvel ID (dans un cas réel, ce serait fait par le backend)
            int newId = generateNewId();

            // Ajout de la ligne au tableau
            Object[] row = {newId, name, unitPrice, bulkPrice, threshold, brand, stock};
            tableModel.addRow(row);

            // Nettoyage du formulaire
            clearForm();

            // Message de confirmation
            JOptionPane.showMessageDialog(this, "Article ajouté avec succès", "Ajout réussi", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Veuillez saisir des valeurs numériques valides pour les prix et le seuil", "Format incorrect", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Met à jour l'article sélectionné avec les données du formulaire
     */
    private void updateArticle() {
        int selectedRow = articlesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un article à modifier", "Aucune sélection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Vérification des champs obligatoires
        if (nameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez saisir un nom d'article", "Champ obligatoire", JOptionPane.WARNING_MESSAGE);
            nameField.requestFocus();
            return;
        }

        try {
            // Récupération des valeurs
            int id = Integer.parseInt(idField.getText().trim());
            String name = nameField.getText().trim();
            double unitPrice = Double.parseDouble(unitPriceField.getText().trim());
            double bulkPrice = Double.parseDouble(bulkPriceField.getText().trim());
            int threshold = Integer.parseInt(thresholdField.getText().trim());
            String brand = brandComboBox.getSelectedItem().toString();
            int stock = (int) stockSpinner.getValue();

            // Vérification des valeurs
            if (unitPrice <= 0 || bulkPrice <= 0 || threshold <= 0) {
                JOptionPane.showMessageDialog(this, "Les prix et le seuil doivent être supérieurs à 0", "Valeur incorrecte", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Mise à jour de la ligne dans le tableau
            tableModel.setValueAt(name, selectedRow, 1);
            tableModel.setValueAt(unitPrice, selectedRow, 2);
            tableModel.setValueAt(bulkPrice, selectedRow, 3);
            tableModel.setValueAt(threshold, selectedRow, 4);
            tableModel.setValueAt(brand, selectedRow, 5);
            tableModel.setValueAt(stock, selectedRow, 6);

            // Nettoyage du formulaire
            clearForm();

            // Message de confirmation
            JOptionPane.showMessageDialog(this, "Article modifié avec succès", "Modification réussie", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Veuillez saisir des valeurs numériques valides pour les prix et le seuil", "Format incorrect", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Supprime l'article sélectionné
     */
    private void deleteArticle() {
        int selectedRow = articlesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un article à supprimer", "Aucune sélection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String articleName = (String) tableModel.getValueAt(selectedRow, 1);

        // Demande de confirmation
        int response = JOptionPane.showConfirmDialog(this,
                "Êtes-vous sûr de vouloir supprimer l'article \"" + articleName + "\" ?",
                "Confirmation de suppression",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (response == JOptionPane.YES_OPTION) {
            // Suppression de la ligne du tableau
            tableModel.removeRow(selectedRow);

            // Nettoyage du formulaire
            clearForm();

            // Message de confirmation
            JOptionPane.showMessageDialog(this, "Article supprimé avec succès", "Suppression réussie", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Génère un nouvel ID unique pour un article (simulation)
     * @return un nouvel ID
     */
    private int generateNewId() {
        int maxId = 0;

        // Recherche de l'ID le plus élevé
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            int id = (int) tableModel.getValueAt(i, 0);
            if (id > maxId) {
                maxId = id;
            }
        }

        // Retourner l'ID suivant
        return maxId + 1;
    }

    /**
     * Charge les données initiales dans le tableau des articles (simulation)
     */
    private void loadArticlesData() {
        // Vider le tableau
        tableModel.setRowCount(0);

        // Ajouter des données de test
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
    }

    /**
     * Génère un rapport en fonction des options sélectionnées
     */
    private void generateReport() {
        String reportType = (String) reportTypeComboBox.getSelectedItem();
        String period = (String) periodComboBox.getSelectedItem();

        // Vider le panneau de graphique
        chartPanel.removeAll();

        // Générer le graphique approprié selon le type de rapport
        if ("Ventes par marque".equals(reportType)) {
            generateBrandSalesChart(period);
        } else if ("Ventes par mois".equals(reportType)) {
            generateMonthlySalesChart(period);
        } else if ("Articles les plus vendus".equals(reportType)) {
            generateTopProductsChart(period);
        }

        // Mettre à jour l'affichage
        chartPanel.revalidate();
        chartPanel.repaint();
    }

    /**
     * Génère un graphique en camembert des ventes par marque sans utiliser JFreeChart
     * @param period la période sélectionnée
     */
    private void generateBrandSalesChart(String period) {
        // Données pour le graphique (simulation)
        Map<String, Double> data = new HashMap<>();
        data.put("Apple", 45.0);
        data.put("Samsung", 30.0);
        data.put("Sony", 15.0);
        data.put("HP", 7.0);
        data.put("Dell", 3.0);

        // Créer le panneau pour le graphique personnalisé
        JPanel customChartPanel = new JPanel(new BorderLayout());

        // Titre du graphique
        JLabel titleLabel = new JLabel("Répartition des ventes par marque - " + period, JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        customChartPanel.add(titleLabel, BorderLayout.NORTH);

        // Panneau pour le camembert
        PieChartPanel pieChart = new PieChartPanel(data);
        customChartPanel.add(pieChart, BorderLayout.CENTER);

        // Légende du graphique
        JPanel legendPanel = createLegendPanel(data);
        customChartPanel.add(legendPanel, BorderLayout.EAST);

        // Date de génération du rapport
        JLabel dateLabel = new JLabel("Rapport généré le " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()), JLabel.RIGHT);
        dateLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        customChartPanel.add(dateLabel, BorderLayout.SOUTH);

        // Ajouter le panneau au panneau principal
        chartPanel.add(customChartPanel);
    }

    /**
     * Génère un graphique en barres des ventes par mois sans utiliser JFreeChart
     * @param period la période sélectionnée
     */
    private void generateMonthlySalesChart(String period) {
        // Données pour le graphique (simulation)
        Map<String, Double> data = new HashMap<>();
        data.put("Jan", 1200.0);
        data.put("Fév", 1500.0);
        data.put("Mar", 1800.0);
        data.put("Avr", 2200.0);
        data.put("Mai", 2500.0);
        data.put("Juin", 2100.0);
        data.put("Juil", 1900.0);
        data.put("Août", 2000.0);
        data.put("Sep", 2300.0);
        data.put("Oct", 2700.0);
        data.put("Nov", 3000.0);
        data.put("Déc", 3500.0);

        // Créer le panneau pour le graphique personnalisé
        JPanel customChartPanel = new JPanel(new BorderLayout());

        // Titre du graphique
        JLabel titleLabel = new JLabel("Ventes mensuelles - " + period, JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        customChartPanel.add(titleLabel, BorderLayout.NORTH);

        // Panneau pour le graphique en barres
        BarChartPanel barChart = new BarChartPanel(data, "Montant des ventes (€)");
        customChartPanel.add(barChart, BorderLayout.CENTER);

        // Date de génération du rapport
        JLabel dateLabel = new JLabel("Rapport généré le " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()), JLabel.RIGHT);
        dateLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        customChartPanel.add(dateLabel, BorderLayout.SOUTH);

        // Ajouter le panneau au panneau principal
        chartPanel.add(customChartPanel);
    }