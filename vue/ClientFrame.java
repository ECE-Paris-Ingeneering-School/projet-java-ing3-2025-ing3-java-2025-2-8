/**
 * Fichier : ClientFrame.java
 * Fenêtre principale client (interface Swing) pour le projet Java ING3 2025 Shopping.
 * Sources :
 * - Cours 7 Java – Swing, composants de base et layout (JP Segado, Boostcamp/PDF)
 * - OpenClassrooms – Fenêtre principale et interface Java Swing :
 *   https://openclassrooms.com/fr/courses/26832-apprenez-a-programmer-en-java/26892-creez-une-interface-graphique-avec-swing
 * - Java Doc officielle (Swing/AWT) :
 *   https://docs.oracle.com/javase/8/docs/api/javax/swing/
 *
 * @author Adam Zeidan et Florent Meunier
 * @date Avril 2025
 */

package vue;

import modele.Utilisateur;

import javax.swing.*;
import java.awt.*;


/**
 * Fenetre principale pour les clients après connexion
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

        // Barre superieure
        JLabel welcomeLabel = new JLabel("Bienvenue, " + utilisateur.getNom() + " 👋", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        mainPanel.add(welcomeLabel, BorderLayout.NORTH);

        // Zone centrale (catalogue produit)
        JTextArea placeholder = new JTextArea("Catalogue de produits ici...");
        placeholder.setEditable(false);
        placeholder.setFont(new Font("Arial", Font.ITALIC, 16));
        mainPanel.add(placeholder, BorderLayout.CENTER);

        // barre inferieure
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
