package vue;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * PaymentView - Interface graphique pour la simulation du paiement
 * Cette classe permet d'afficher un récapitulatif de commande et de simuler le paiement
 */
public class PaymentView extends JFrame {

    // Composants de l'interface
    private JTextArea summaryTextArea;
    private JButton payButton;
    private JButton cancelButton;
    private JLabel totalLabel;

    // Données de la commande
    private List<OrderItem> orderItems;
    private double totalAmount;
    private CartView cartView;

    /**
     * Constructeur pour une utilisation réelle avec panier
     * @param cartView Vue du panier qui contient les informations de la commande
     * @param items Liste des articles commandés
     * @param total Montant total de la commande
     */
    public PaymentView(CartView cartView, List<OrderItem> items, double total) {
        this.cartView = cartView;
        this.orderItems = items;
        this.totalAmount = total;

        initializeFrame();
        initializeComponents();
        populateSummary();
    }

    /**
     * Constructeur pour les tests (avec données fictives)
     */
    public PaymentView() {
        // Création de données de test
        this.orderItems = createSampleData();
        this.totalAmount = calculateTotal(orderItems);

        initializeFrame();
        initializeComponents();
        populateSummary();
    }

    /**
     * Initialise les paramètres de la fenêtre
     */
    private void initializeFrame() {
        setTitle("Paiement - Confirmation de commande");
        setSize(600, 500);
        setMinimumSize(new Dimension(500, 400));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
    }

    /**
     * Initialise et configure les composants de l'interface
     */
    private void initializeComponents() {
        // Panel du haut : en-tête
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Panel central : récapitulatif de la commande
        JPanel summaryPanel = createSummaryPanel();
        add(summaryPanel, BorderLayout.CENTER);

        // Panel du bas : total et boutons d'action
        JPanel actionPanel = createActionPanel();
        add(actionPanel, BorderLayout.SOUTH);
    }

    /**
     * Crée le panneau d'en-tête
     * @return le panneau configuré
     */
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 5, 10));

        JLabel titleLabel = new JLabel("Récapitulatif de votre commande", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        JLabel dateLabel = new JLabel("Date : " + sdf.format(new Date()), JLabel.RIGHT);
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        panel.add(titleLabel, BorderLayout.CENTER);
        panel.add(dateLabel, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Crée le panneau de récapitulatif de commande
     * @return le panneau configuré
     */
    private JPanel createSummaryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Table header
        JPanel headerPanel = new JPanel(new GridLayout(1, 5));
        headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));

        JLabel articleLabel = new JLabel("Article", JLabel.LEFT);
        JLabel quantityLabel = new JLabel("Quantité", JLabel.CENTER);
        JLabel priceLabel = new JLabel("Prix unitaire", JLabel.CENTER);
        JLabel discountLabel = new JLabel("Remise", JLabel.CENTER);
        JLabel subtotalLabel = new JLabel("Sous-total", JLabel.RIGHT);

        headerPanel.add(articleLabel);
        headerPanel.add(quantityLabel);
        headerPanel.add(priceLabel);
        headerPanel.add(discountLabel);
        headerPanel.add(subtotalLabel);

        // Text area for order items
        summaryTextArea = new JTextArea();
        summaryTextArea.setEditable(false);
        summaryTextArea.setLineWrap(true);
        summaryTextArea.setWrapStyleWord(true);
        summaryTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(summaryTextArea);

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Crée le panneau des actions (total et boutons)
     * @return le panneau configuré
     */
    private JPanel createActionPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Panel pour le total
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalLabel = new JLabel("Total à payer : " + String.format("%.2f €", totalAmount));
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalPanel.add(totalLabel);

        // Panel pour les boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        payButton = new JButton("Payer maintenant");
        payButton.setPreferredSize(new Dimension(150, 40));
        payButton.setFont(new Font("Arial", Font.BOLD, 14));
        payButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processPayment();
            }
        });

        cancelButton = new JButton("Annuler");
        cancelButton.setPreferredSize(new Dimension(100, 40));
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelPayment();
            }
        });

        buttonPanel.add(payButton);
        buttonPanel.add(cancelButton);

        // Assemblage du panel
        panel.add(totalPanel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Remplit la zone de récapitulatif avec les détails de la commande
     */
    private void populateSummary() {
        StringBuilder summary = new StringBuilder();

        // Format de la ligne de récapitulatif
        String format = "%-30s %7s %13s %10s %12s\n";

        // Ajouter chaque article
        for (OrderItem item : orderItems) {
            String name = truncateString(item.getName(), 28);
            String quantity = String.valueOf(item.getQuantity());
            String unitPrice = String.format("%.2f €", item.getUnitPrice());

            // Calcul de la remise
            double regularTotal = item.getQuantity() * item.getUnitPrice();
            double discountAmount = regularTotal - item.getSubtotal();
            String discount = discountAmount > 0 ? String.format("-%.2f €", discountAmount) : "-";

            String subtotal = String.format("%.2f €", item.getSubtotal());

            summary.append(String.format(format, name, quantity, unitPrice, discount, subtotal));
        }

        // Définir le texte dans la zone de récapitulatif
        summaryTextArea.setText(summary.toString());
    }

    /**
     * Simule le traitement du paiement
     */
    private void processPayment() {
        // Vérifier si la commande est valide
        if (orderItems.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Erreur : Panier vide. Impossible de procéder au paiement.",
                    "Erreur de paiement",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Simuler un délai de traitement
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        payButton.setEnabled(false);
        cancelButton.setEnabled(false);

        // Utiliser un Timer pour simuler le délai de traitement
        Timer timer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setCursor(Cursor.getDefaultCursor());

                // Afficher la confirmation
                JOptionPane.showMessageDialog(PaymentView.this,
                        "Paiement validé ! Merci pour votre achat.\n\n" +
                                "Un e-mail de confirmation a été envoyé avec les détails de votre commande.",
                        "Paiement réussi",
                        JOptionPane.INFORMATION_MESSAGE);

                // Fermer les fenêtres et retourner au catalogue
                closeAndReturnToCatalog();
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    /**
     * Annule le paiement et retourne au panier
     */
    private void cancelPayment() {
        int response = JOptionPane.showConfirmDialog(this,
                "Êtes-vous sûr de vouloir annuler le paiement ?",
                "Confirmation d'annulation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (response == JOptionPane.YES_OPTION) {
            // Fermer cette fenêtre et retourner au panier
            if (cartView != null) {
                this.setVisible(false);
                cartView.setVisible(true);
                this.dispose();
            } else {
                this.dispose();
            }
        }
    }

    /**
     * Ferme toutes les fenêtres et retourne au catalogue
     */
    private void closeAndReturnToCatalog() {
        // Fermer cette fenêtre et le panier, et retourner au catalogue
        this.dispose();

        if (cartView != null) {
            // Si nous avons une référence au panier, on peut obtenir le catalogue
            // et fermer toutes les fenêtres intermédiaires

            // Vider le panier après un paiement réussi
            cartView.clearCart();

            // Revenir au catalogue
            if (cartView.getCatalogView() != null) {
                cartView.getCatalogView().setVisible(true);
            }
        }
    }

    /**
     * Crée des données de test pour la démo
     * @return une liste d'articles de commande factices
     */
    private List<OrderItem> createSampleData() {
        List<OrderItem> items = new ArrayList<>();

        // Ajouter des articles fictifs
        items.add(new OrderItem(1, "iPhone 13", 899.99, 2, 1799.98));
        items.add(new OrderItem(4, "Sony WH-1000XM4", 349.99, 1, 349.99));

        // Article avec remise en gros
        double regularPrice = 799.99;
        int quantity = 3;
        double discountedTotal = 2249.97; // Prix avec remise
        items.add(new OrderItem(7, "iPad Pro", regularPrice, quantity, discountedTotal));

        return items;
    }

    /**
     * Calcule le total de la commande
     * @param items Liste des articles
     * @return le montant total
     */
    private double calculateTotal(List<OrderItem> items) {
        double total = 0.0;
        for (OrderItem item : items) {
            total += item.getSubtotal();
        }
        return total;
    }

    /**
     * Tronque une chaîne si elle est trop longue
     * @param str Chaîne à tronquer
     * @param maxLength Longueur maximale
     * @return la chaîne tronquée avec "..." si nécessaire
     */
    private String truncateString(String str, int maxLength) {
        if (str.length() <= maxLength) {
            return str;
        }
        return str.substring(0, maxLength - 3) + "...";
    }

    /**
     * Classe interne représentant un article dans la commande
     */
    public static class OrderItem {
        private int id;
        private String name;
        private double unitPrice;
        private int quantity;
        private double subtotal;

        public OrderItem(int id, String name, double unitPrice, int quantity, double subtotal) {
            this.id = id;
            this.name = name;
            this.unitPrice = unitPrice;
            this.quantity = quantity;
            this.subtotal = subtotal;
        }

        // Getters
        public int getId() { return id; }
        public String getName() { return name; }
        public double getUnitPrice() { return unitPrice; }
        public int getQuantity() { return quantity; }
        public double getSubtotal() { return subtotal; }
    }

    /**
     * Méthode principale pour tester la vue indépendamment
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

                PaymentView paymentView = new PaymentView();
                paymentView.setVisible(true);
            }
        });
    }
}