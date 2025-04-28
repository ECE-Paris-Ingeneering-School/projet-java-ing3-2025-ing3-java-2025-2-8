package vue;

import modele.Utilisateur;

import javax.swing.*;
import java.awt.*;

public class ClientFrame extends JFrame {
    private Utilisateur utilisateur;
    private JButton enterCatalogButton;
    private JButton myOrdersButton;
    private JButton logoutButton;

    public ClientFrame(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;

        setTitle("Menu Client - Bienvenue " + utilisateur.getNom());
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        JLabel welcomeLabel = new JLabel("Bienvenue, " + utilisateur.getNom() + " 👋", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 22));
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        add(welcomeLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        enterCatalogButton = new JButton("Entrer dans le Catalogue");
        myOrdersButton = new JButton("Mes Commandes");
        logoutButton = new JButton("Déconnexion");

        buttonPanel.add(enterCatalogButton);
        buttonPanel.add(myOrdersButton);
        buttonPanel.add(logoutButton);

        add(buttonPanel, BorderLayout.CENTER);
    }

    // Getters
    public JButton getEnterCatalogButton() {
        return enterCatalogButton;
    }

    public JButton getMyOrdersButton() {
        return myOrdersButton;
    }

    public JButton getLogoutButton() {
        return logoutButton;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }
}
