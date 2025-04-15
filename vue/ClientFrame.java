package vue;

import modele.Utilisateur;

import javax.swing.*;
import java.awt.*;

/**
 * Fenêtre principale pour les clients après connexion
 */
public class ClientFrame extends JFrame {

    private Utilisateur utilisateur;

    public ClientFrame(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;

        setTitle("Bienvenue - Espace Client");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initUI();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Barre supérieure
        JLabel welcomeLabel = new JLabel("Bienvenue, " + utilisateur.getNom() + " 👋", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        mainPanel.add(welcomeLabel, BorderLayout.NORTH);

        // Zone centrale (catalogue produit - à ajouter plus tard)
        JTextArea placeholder = new JTextArea("Catalogue de produits ici...");
        placeholder.setEditable(false);
        placeholder.setFont(new Font("Arial", Font.ITALIC, 16));
        mainPanel.add(placeholder, BorderLayout.CENTER);

        // Barre inférieure
        JPanel bottomPanel = new JPanel();
        JButton viewCartButton = new JButton("Voir le panier");
        JButton logoutButton = new JButton("Déconnexion");

        bottomPanel.add(viewCartButton);
        bottomPanel.add(logoutButton);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Actions
        logoutButton.addActionListener(e -> {
            dispose();
            new LoginView().setVisible(true);
        });

        viewCartButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Fonctionnalité panier à implémenter 🛒");
        });
    }
}
