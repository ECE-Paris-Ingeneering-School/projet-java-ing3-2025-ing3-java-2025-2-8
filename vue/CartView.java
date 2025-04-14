package vue;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * CartView - Interface graphique pour l'affichage du panier d'achat
 * Cette classe permet au client de visualiser, modifier et valider son panier
 */
public class CartView extends JFrame {

    // Composants de l'interface
    private JTable cartTable;
    private DefaultTableModel tableModel;
    private JLabel totalLabel;
    private JButton deleteButton;
    private JButton modifyQuantityButton;
    private JButton checkoutButton;
    private JButton continueShoppingButton;
    private JLabel statusLabel;

    // Données
    private double cartTotal = 0.0;

    // Référence au catalogue
    private CatalogView catalogView;

    /**
     * Constructeur de la fenêtre du panier
     * @param catalogView référence à la vue du catalogue
     */
    public CartView(CatalogView catalogView) {
        // Sauvegarde de la référence au catalogue
        this.catalogView = catalogView;

        // Configuration de la fenêtre principale
        setTitle("Panier d'Achat");
        setSize(700, 500);
        setMinimumSize(new Dimension(600, 400));
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialisation des composants
        initComponents();
    }

    /**
     * Initialise et configure les composants de l'interface
     */
    private void initComponents() {
        // Utilisation d'un BorderLayout pour la fenêtre principale
        setLayout(new BorderLayout(10, 10));

        // Panneau titre en haut
        JPanel titlePanel = createTitlePanel();
        add(titlePanel, BorderLayout.NORTH);

        // Panneau central avec le tableau du panier
        JPanel centerPanel = createCenterPanel();
        add(centerPanel, BorderLayout.CENTER);

        // Panneau inférieur avec total et boutons
        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * Crée le panneau de titre en haut de la fenêtre
     * @return le panneau configuré
     */
    private JPanel createTitlePanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));

        JLabel titleLabel = new JLabel("Votre Panier d'Achat");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titleLabel);

        return panel;
    }

    /**
     * Crée le panneau central avec le tableau des articles du panier
     * @return le panneau configuré
     */
    private JPanel createCenterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        // Création des colonnes du tableau
        String[] columnNames = {
                "ID", "Nom de l'article", "Prix unitaire (€)", "Quantité",
                "Prix en gros (€)", "Seuil", "Sous-total (€)"
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
                // Pour permettre le tri des colonnes selon leur type
                if (columnIndex == 0) return Integer.class; // ID
                if (columnIndex == 2 || columnIndex == 4 || columnIndex == 6) return Double.class; // Prix et Sous-total
                if (columnIndex == 3 || columnIndex == 5) return Integer.class; // Quantité et Seuil
                return String.class; // Par défaut (Nom)
            }
        };

        // Création du tableau
        cartTable = new JTable(tableModel);

        // Configuration de l'apparence du tableau
        cartTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        cartTable.setRowHeight(25);
        cartTable.getTableHeader().setReorderingAllowed(false);

        // Configuration des largeurs de colonnes
        cartTable.getColumnModel().getColumn(0).setPreferredWidth(40);    // ID
        cartTable.getColumnModel().getColumn(1).setPreferredWidth(200);   // Nom
        cartTable.getColumnModel().getColumn(2).setPreferredWidth(90);    // Prix unitaire
        cartTable.getColumnModel().getColumn(3).setPreferredWidth(70);    // Quantité
        cartTable.getColumnModel().getColumn(4).setPreferredWidth(90);    // Prix en gros
        cartTable.getColumnModel().getColumn(5).setPreferredWidth(60);    // Seuil
        cartTable.getColumnModel().getColumn(6).setPreferredWidth(90);    // Sous-total

        // Ajout du tableau à un JScrollPane pour permettre le défilement
        JScrollPane scrollPane = new JScrollPane(cartTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Activation/désactivation des boutons selon la sélection
        cartTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                boolean hasSelection = cartTable.getSelectedRow() != -1;
                deleteButton.setEnabled(hasSelection);
                modifyQuantityButton.setEnabled(hasSelection);
            }
        });

        return panel;
    }

    /**
     * Crée le panneau inférieur avec le total et les boutons d'action
     * @return le panneau configuré
     */
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Sous-panneau pour le total
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalLabel = new JLabel("Total : 0.00 €");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalPanel.add(totalLabel);

        // Sous-panneau pour les boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));

        // Bouton de suppression d'article
        deleteButton = new JButton("Supprimer");
        deleteButton.setEnabled(false); // Désactivé par défaut
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedItem();
            }
        });
        buttonPanel.add(deleteButton);

        // Bouton de modification de quantité
        modifyQuantityButton = new JButton("Modifier quantité");
        modifyQuantityButton.setEnabled(false); // Désactivé par défaut
        modifyQuantityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modifySelectedItemQuantity();
            }
        });
        buttonPanel.add(modifyQuantityButton);

        // Bouton de passage au paiement
        checkoutButton = new JButton("Passer au paiement");
        checkoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                proceedToCheckout();
            }
        });
        buttonPanel.add(checkoutButton);

        // Bouton pour continuer les achats
        continueShoppingButton = new JButton("Continuer les achats");
        continueShoppingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                continueShopping();
            }
        });
        buttonPanel.add(continueShoppingButton);

        // Label de statut
        statusLabel = new JLabel(" ");
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        // Organisation des sous-panneaux
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(totalPanel, BorderLayout.NORTH);
        southPanel.add(buttonPanel, BorderLayout.CENTER);
        southPanel.add(statusLabel, BorderLayout.SOUTH);

        panel.add(southPanel, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Charge des données d'exemple dans le panier pour les tests
     */
    private void loadSampleData() {
        // Vider le tableau
        tableModel.setRowCount(0);

        // Ajouter des articles d'exemple au panier
        // Format: ID, Nom, Prix unitaire, Quantité, Prix en gros, Seuil, Sous-total
        addItemToCart(1, "iPhone 13", 899.99, 2, 849.99, 5, calculateSubtotal(899.99, 849.99, 2, 5));
        addItemToCart(4, "Sony WH-1000XM4", 349.99, 1, 299.99, 10, calculateSubtotal(349.99, 299.99, 1, 10));
        addItemToCart(7, "iPad Pro", 799.99, 3, 749.99, 5, calculateSubtotal(799.99, 749.99, 3, 5));

        // Mise à jour du statut
        statusLabel.setText("Panier chargé avec " + tableModel.getRowCount() + " articles");
    }

    /**
     * Ajoute un article au panier
     * @param id ID de l'article
     * @param name Nom de l'article
     * @param unitPrice Prix unitaire
     * @param quantity Quantité
     * @param bulkPrice Prix en gros
     * @param bulkThreshold Seuil pour le prix en gros
     * @param subtotal Sous-total
     */
    public void addItemToCart(int id, String name, double unitPrice, int quantity, double bulkPrice, int bulkThreshold, double subtotal) {
        // Vérifier si l'article est déjà dans le panier
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if ((int)tableModel.getValueAt(i, 0) == id) {
                // L'article existe déjà, mettre à jour la quantité
                int currentQuantity = (int)tableModel.getValueAt(i, 3);
                int newQuantity = currentQuantity + quantity;

                // Calculer le nouveau sous-total
                double newSubtotal = calculateSubtotal(unitPrice, bulkPrice, newQuantity, bulkThreshold);

                // Mettre à jour le tableau
                tableModel.setValueAt(newQuantity, i, 3);
                tableModel.setValueAt(newSubtotal, i, 6);

                // Mettre à jour le total
                updateTotal();

                // Mise à jour du statut
                statusLabel.setText("Quantité mise à jour pour : " + name);

                return;
            }
        }

        // L'article n'existe pas encore, l'ajouter
        Object[] row = {id, name, unitPrice, quantity, bulkPrice, bulkThreshold, subtotal};
        tableModel.addRow(row);

        // Mettre à jour le total
        updateTotal();

        // Mise à jour du statut
        statusLabel.setText("Article ajouté au panier : " + name);
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
     * Met à jour le total du panier
     */
    private void updateTotal() {
        cartTotal = 0.0;

        // Parcourir toutes les lignes du tableau
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            // Ajouter le sous-total de chaque ligne au total
            cartTotal += (double) tableModel.getValueAt(i, 6);
        }

        // Mettre à jour le label avec le nouveau total
        totalLabel.setText("Total : " + String.format("%.2f €", cartTotal));

        // Activer/désactiver le bouton de paiement selon que le panier est vide ou non
        checkoutButton.setEnabled(tableModel.getRowCount() > 0);
    }

    /**
     * Supprime l'article sélectionné du panier
     */
    private void deleteSelectedItem() {
        int selectedRow = cartTable.getSelectedRow();

        if (selectedRow != -1) {
            // Récupérer les informations de l'article sélectionné
            int articleId = (int) tableModel.getValueAt(selectedRow, 0);
            String articleName = (String) tableModel.getValueAt(selectedRow, 1);

            // Demander confirmation à l'utilisateur
            int response = JOptionPane.showConfirmDialog(
                    this,
                    "Voulez-vous supprimer l'article \"" + articleName + "\" du panier ?",
                    "Confirmation de suppression",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (response == JOptionPane.YES_OPTION) {
                // Supprimer la ligne du tableau
                tableModel.removeRow(selectedRow);

                // Mettre à jour le total
                updateTotal();

                // Mettre à jour le statut
                statusLabel.setText("Article \"" + articleName + "\" supprimé du panier");
            }
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Veuillez sélectionner un article à supprimer.",
                    "Aucune sélection",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    /**
     * Modifie la quantité de l'article sélectionné
     */
    private void modifySelectedItemQuantity() {
        int selectedRow = cartTable.getSelectedRow();

        if (selectedRow != -1) {
            // Récupérer les informations de l'article sélectionné
            int articleId = (int) tableModel.getValueAt(selectedRow, 0);
            String articleName = (String) tableModel.getValueAt(selectedRow, 1);
            double unitPrice = (double) tableModel.getValueAt(selectedRow, 2);
            int currentQuantity = (int) tableModel.getValueAt(selectedRow, 3);
            double bulkPrice = (double) tableModel.getValueAt(selectedRow, 4);
            int bulkThreshold = (int) tableModel.getValueAt(selectedRow, 5);

            // Ouvrir une boîte de dialogue pour la nouvelle quantité
            SpinnerNumberModel spinnerModel = new SpinnerNumberModel(currentQuantity, 1, 100, 1);
            JSpinner quantitySpinner = new JSpinner(spinnerModel);

            // Personnaliser la boîte de dialogue
            Object[] message = {
                    "Article : " + articleName,
                    "Nouvelle quantité :",
                    quantitySpinner
            };

            int option = JOptionPane.showConfirmDialog(
                    this,
                    message,
                    "Modifier la quantité",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (option == JOptionPane.OK_OPTION) {
                // Récupérer la nouvelle quantité
                int newQuantity = (int) quantitySpinner.getValue();

                // Calculer le nouveau sous-total
                double newSubtotal = calculateSubtotal(unitPrice, bulkPrice, newQuantity, bulkThreshold);

                // Mettre à jour le tableau
                tableModel.setValueAt(newQuantity, selectedRow, 3);
                tableModel.setValueAt(newSubtotal, selectedRow, 6);

                // Mettre à jour le total
                updateTotal();

                // Mettre à jour le statut
                statusLabel.setText("Quantité modifiée pour \"" + articleName + "\" : " + newQuantity);
            }
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Veuillez sélectionner un article pour modifier sa quantité.",
                    "Aucune sélection",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    /**
     * Procède au paiement du panier
     */
    private void proceedToCheckout() {
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Votre panier est vide. Veuillez ajouter des articles avant de passer au paiement.",
                    "Panier vide",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        // Pour le moment, simuler le passage au paiement
        String message = "Total de votre commande : " + String.format("%.2f €", cartTotal) + "\n\n";
        message += "Articles dans votre panier :\n";

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String name = (String) tableModel.getValueAt(i, 1);
            int quantity = (int) tableModel.getValueAt(i, 3);
            double subtotal = (double) tableModel.getValueAt(i, 6);

            message += "- " + quantity + " × " + name + " : " + String.format("%.2f €", subtotal) + "\n";
        }

        message += "\nProcéder au paiement ?";

        int option = JOptionPane.showConfirmDialog(
                this,
                message,
                "Récapitulatif de la commande",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (option == JOptionPane.YES_OPTION) {
            // TODO: Ouvrir la vue de paiement
            JOptionPane.showMessageDialog(
                    this,
                    "Redirection vers la page de paiement...",
                    "Paiement",
                    JOptionPane.INFORMATION_MESSAGE
            );

            // Ici, vous pourriez faire appel au contrôleur pour ouvrir la vue de paiement
            // et transmettre les informations du panier
        }
    }

    /**
     * Retourne au catalogue pour continuer les achats
     */
    private void continueShopping() {
        // Cacher la fenêtre du panier et revenir au catalogue
        this.setVisible(false);
        catalogView.setVisible(true);
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

                // Pour tester CartView indépendamment, créer d'abord CatalogView
                CatalogView catalogView = new CatalogView();
                // La CartView est déjà créée par CatalogView
                catalogView.setVisible(true);
            }
        });
    }
}