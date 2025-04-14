package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * LoginView - Interface graphique pour la connexion des utilisateurs
 */
public class LoginView extends JFrame {

    // Composants de l'interface
    private JTextField emailField;
    private JPasswordField passwordField;
    private JComboBox<String> userTypeComboBox;
    private JButton loginButton;
    private JButton registerButton;
    private JLabel statusLabel;

    /**
     * Constructeur de la fenêtre
     */
    public LoginView() {
        // Configuration de la fenêtre principale
        setTitle("Connexion - Application Shopping");
        setSize(400, 350);
        setMinimumSize(new Dimension(350, 300));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialisation des composants
        initComponents();
    }

    /**
     * Initialise les composants de l'interface
     */
    private void initComponents() {
        // Panneau principal avec espacement
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panneau de titre centré
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("SHOPPING APP");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(titleLabel);

        // Panneau de formulaire avec GridBagLayout pour un meilleur contrôle
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Type d'utilisateur
        JLabel userTypeLabel = new JLabel("Type d'utilisateur:");
        String[] userTypes = {"Client", "Administrateur"};
        userTypeComboBox = new JComboBox<>(userTypes);

        // Email
        JLabel emailLabel = new JLabel("Email:");
        emailField = new JTextField(15);

        // Mot de passe
        JLabel passwordLabel = new JLabel("Mot de passe:");
        passwordField = new JPasswordField(15);

        // Placement des composants avec GridBagLayout
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(userTypeLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formPanel.add(userTypeComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        formPanel.add(emailLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formPanel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        formPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formPanel.add(passwordField, gbc);

        // Panneau de boutons
        JPanel buttonPanel = new JPanel();
        loginButton = new JButton("Se connecter");
        registerButton = new JButton("Créer un compte");

        buttonPanel.add(loginButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0))); // Espacement entre les boutons
        buttonPanel.add(registerButton);

        // Label de statut
        statusLabel = new JLabel(" ", JLabel.CENTER);
        statusLabel.setForeground(Color.RED);

        // Configuration de l'alignement des panneaux
        titlePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Ajout d'espacement entre les panneaux
        mainPanel.add(titlePanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(formPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(buttonPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(statusLabel);

        // Ajout du panneau principal à la fenêtre
        add(mainPanel);

        // Configuration des actions des boutons
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openRegistrationDialog();
            }
        });

        // Utiliser la touche Entrée pour se connecter
        getRootPane().setDefaultButton(loginButton);
    }

    /**
     * Gère l'action de connexion
     */
    private void handleLogin() {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        String userType = (String) userTypeComboBox.getSelectedItem();

        if (email.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Veuillez remplir tous les champs");
            return;
        }

        // Simulation d'authentification (à remplacer par le code réel avec le contrôleur)
        if ("Administrateur".equals(userType)) {
            if (email.equals("admin@shop.com") && password.equals("admin")) {
                JOptionPane.showMessageDialog(this,
                        "Connexion administrateur réussie!",
                        "Bienvenue",
                        JOptionPane.INFORMATION_MESSAGE);
                // TODO: Ouvrir la vue admin
            } else {
                statusLabel.setText("Identifiants administrateur incorrects");
            }
        } else {
            if (email.contains("@") && password.length() >= 4) {
                JOptionPane.showMessageDialog(this,
                        "Connexion client réussie!",
                        "Bienvenue",
                        JOptionPane.INFORMATION_MESSAGE);
                // TODO: Ouvrir la vue client
            } else {
                statusLabel.setText("Email ou mot de passe incorrect");
            }
        }
    }

    /**
     * Ouvre la boîte de dialogue d'inscription
     */
    private void openRegistrationDialog() {
        JDialog registerDialog = new JDialog(this, "Inscription - Nouveau compte", true);
        registerDialog.setSize(400, 350);
        registerDialog.setLocationRelativeTo(this);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Création de compte");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel nameLabel = new JLabel("Nom complet:");
        JTextField nameField = new JTextField(15);

        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField(15);

        JLabel passwordLabel = new JLabel("Mot de passe:");
        JPasswordField passwordField = new JPasswordField(15);

        JLabel confirmPasswordLabel = new JLabel("Confirmer mot de passe:");
        JPasswordField confirmPasswordField = new JPasswordField(15);

        // Placement des champs
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        formPanel.add(emailLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formPanel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        formPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        formPanel.add(confirmPasswordLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formPanel.add(confirmPasswordField, gbc);

        // Boutons
        JPanel buttonPanel = new JPanel();
        JButton registerButton = new JButton("S'inscrire");
        JButton cancelButton = new JButton("Annuler");
        buttonPanel.add(registerButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0))); // Espacement
        buttonPanel.add(cancelButton);

        // Label de statut pour l'inscription
        JLabel registerStatusLabel = new JLabel(" ", JLabel.CENTER);
        registerStatusLabel.setForeground(Color.RED);
        registerStatusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Ajout des composants au panneau principal
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(formPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(buttonPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(registerStatusLabel);

        // Actions des boutons
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());

                // Validation des champs
                if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    registerStatusLabel.setText("Veuillez remplir tous les champs");
                    return;
                }

                if (!email.contains("@")) {
                    registerStatusLabel.setText("Email invalide");
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    registerStatusLabel.setText("Les mots de passe ne correspondent pas");
                    return;
                }

                if (password.length() < 4) {
                    registerStatusLabel.setText("Mot de passe trop court (min. 4 caractères)");
                    return;
                }

                // TODO: Appeler le contrôleur pour enregistrer l'utilisateur
                JOptionPane.showMessageDialog(registerDialog,
                        "Inscription réussie! Vous pouvez maintenant vous connecter.",
                        "Inscription terminée",
                        JOptionPane.INFORMATION_MESSAGE);
                registerDialog.dispose();

                // Pré-remplir l'email pour la connexion
                LoginView.this.emailField.setText(email);
                LoginView.this.passwordField.setText("");
                LoginView.this.userTypeComboBox.setSelectedItem("Client");
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerDialog.dispose();
            }
        });

        registerDialog.add(mainPanel);
        registerDialog.setResizable(false);
        registerDialog.setVisible(true);
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
                LoginView loginView = new LoginView();
                loginView.setVisible(true);
            }
        });
    }
}