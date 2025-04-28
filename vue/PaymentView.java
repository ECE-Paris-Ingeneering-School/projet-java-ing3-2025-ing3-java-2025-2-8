package vue;

import modele.PanierItem;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PaymentView extends JFrame {
    private JTextArea summaryTextArea;
    private JButton payButton;
    private JButton cancelButton;
    private JLabel totalLabel;

    private List<PanierItem> panierItems;
    private double totalAmount;

    public PaymentView(List<PanierItem> panierItems, double totalAmount) {
        this.panierItems = panierItems;
        this.totalAmount = totalAmount;

        initializeFrame();
        initializeComponents();
        populateSummary();
    }

    private void initializeFrame() {
        setTitle("Paiement - Confirmation de commande");
        setSize(600, 500);
        setMinimumSize(new Dimension(500, 400));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
    }

    private void initializeComponents() {
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        JPanel summaryPanel = createSummaryPanel();
        add(summaryPanel, BorderLayout.CENTER);

        JPanel actionPanel = createActionPanel();
        add(actionPanel, BorderLayout.SOUTH);
    }

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

    private JPanel createSummaryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        summaryTextArea = new JTextArea();
        summaryTextArea.setEditable(false);
        summaryTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(summaryTextArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createActionPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalLabel = new JLabel("Total à payer : " + String.format("%.2f €", totalAmount));
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalPanel.add(totalLabel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        payButton = new JButton("Payer maintenant");
        payButton.setPreferredSize(new Dimension(150, 40));

        cancelButton = new JButton("Annuler");
        cancelButton.setPreferredSize(new Dimension(100, 40));

        buttonPanel.add(payButton);
        buttonPanel.add(cancelButton);

        panel.add(totalPanel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.CENTER);

        return panel;
    }

    private void populateSummary() {
        StringBuilder summary = new StringBuilder();
        String format = "%-25s %5s %10s %10s\n";
        summary.append(String.format(format, "Article", "Qte", "PU", "Sous-total"));
        summary.append("-----------------------------------------------------------\n");

        for (PanierItem item : panierItems) {
            summary.append(String.format(format,
                    item.getProduit().getNom(),
                    item.getQuantite(),
                    String.format("%.2f", item.getProduit().getPrixUnitaire()),
                    String.format("%.2f", item.getSousTotal())
            ));
        }

        summaryTextArea.setText(summary.toString());
    }

    // --- Getters pour le contrôleur ---
    public JButton getPayButton() {
        return payButton;
    }

    public JButton getCancelButton() {
        return cancelButton;
    }

    public List<PanierItem> getPanierItems() {
        return panierItems;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    // Méthodes accessibles par le contrôleur
    public void afficherMessagePaiementSucces() {
        JOptionPane.showMessageDialog(this,
                "Paiement validé ! Merci pour votre achat.",
                "Succès",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public void afficherMessageErreurPanierVide() {
        JOptionPane.showMessageDialog(this,
                "Erreur : Panier vide.",
                "Erreur de paiement",
                JOptionPane.ERROR_MESSAGE);
    }

    public boolean confirmerAnnulation() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Annuler le paiement ?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION);
        return confirm == JOptionPane.YES_OPTION;
    }
}
