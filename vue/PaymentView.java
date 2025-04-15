package vue;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class PaymentView extends JFrame {
    private JTextArea summaryTextArea;
    private JButton payButton;
    private JButton cancelButton;
    private JLabel totalLabel;

    private List<OrderItem> orderItems;
    private double totalAmount;
    private CartView cartView;

    public PaymentView(CartView cartView, List<OrderItem> items, double total) {
        this.cartView = cartView;
        this.orderItems = items;
        this.totalAmount = total;

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

        //les pay button

        payButton = new JButton("Payer maintenant");
        payButton.setPreferredSize(new Dimension(150, 40));
        payButton.addActionListener(e -> processPayment());

        cancelButton = new JButton("Annuler");
        cancelButton.setPreferredSize(new Dimension(100, 40));
        cancelButton.addActionListener(e -> cancelPayment());

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

        for (OrderItem item : orderItems) {
            summary.append(String.format(format,
                    item.getName(),
                    item.getQuantity(),
                    String.format("%.2f", item.getUnitPrice()),
                    String.format("%.2f", item.getSubtotal())
            ));
        }

        summaryTextArea.setText(summary.toString());
    }
    //payment processeur
    private void processPayment() {
        if (orderItems.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Erreur : Panier vide.",
                    "Erreur de paiement",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        //curseur
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        payButton.setEnabled(false);
        cancelButton.setEnabled(false);
        //timer

        Timer timer = new Timer(1500, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setCursor(Cursor.getDefaultCursor());

                JOptionPane.showMessageDialog(PaymentView.this,
                        "Paiement validé ! Merci pour votre achat.",
                        "Succès",
                        JOptionPane.INFORMATION_MESSAGE);

                closeAndReturnToCatalog();
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void cancelPayment() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Annuler le paiement ?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (cartView != null) {
                setVisible(false);
                cartView.setVisible(true);
                dispose();
            } else {
                dispose();
            }
        }
    }

    private void closeAndReturnToCatalog() {
        dispose();
        if (cartView != null) {
            cartView.clearCart();
            if (cartView.getCatalogView() != null) {
                cartView.getCatalogView().setVisible(true);
            }
        }
    }
    //order item
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

        public int getId() { return id; }
        public String getName() { return name; }
        public double getUnitPrice() { return unitPrice; }
        public int getQuantity() { return quantity; }
        public double getSubtotal() { return subtotal; }
    }
}
