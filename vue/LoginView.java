package vue;

import dao.UtilisateurDAO;
import modele.Utilisateur;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * LoginView - Interface graphique pour la connexion et l'inscription
 */
public class LoginView extends JFrame {

    private JTextField emailField;
    private JPasswordField passwordField;
    private JComboBox<String> userTypeComboBox;
    private JButton loginButton;
    private JButton registerButton;
    private JLabel statusLabel;

    public LoginView() {
        setTitle("Connexion - Application Shopping");
        setSize(400, 350);
        setMinimumSize(new Dimension(350, 300));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("SHOPPING APP");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(titleLabel);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel userTypeLabel = new JLabel("Type d'utilisateur:");
        String[] userTypes = {"Client", "Administrateur"};
        userTypeComboBox = new JComboBox<>(userTypes);

        JLabel emailLabel = new JLabel("Email:");
        emailField = new JTextField(15);

        JLabel passwordLabel = new JLabel("Mot de passe:");
        passwordField = new JPasswordField(15);

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(userTypeLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(userTypeComboBox, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(emailLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(passwordLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);

        JPanel buttonPanel = new JPanel();
        loginButton = new JButton("Se connecter");
        registerButton = new JButton("Créer un compte");
        buttonPanel.add(loginButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(registerButton);

        statusLabel = new JLabel(" ", JLabel.CENTER);
        statusLabel.setForeground(Color.RED);

        titlePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        mainPanel.add(titlePanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(formPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(buttonPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(statusLabel);

        add(mainPanel);
        getRootPane().setDefaultButton(loginButton);

        // Action inscription
        registerButton.addActionListener(e -> openRegistrationDialog());
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

        // ===== FORMULAIRE =====
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Nom
        JLabel nameLabel = new JLabel("Nom complet:");
        JTextField nameField = new JTextField(15);
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.0;
        formPanel.add(nameLabel, gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(nameField, gbc);

        // Email
        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField(15);
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.0;
        formPanel.add(emailLabel, gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(emailField, gbc);

        // Mot de passe
        JLabel passwordLabel = new JLabel("Mot de passe:");
        JPasswordField passwordField = new JPasswordField(15);
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.0;
        formPanel.add(passwordLabel, gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(passwordField, gbc);

        // Confirmation mot de passe
        JLabel confirmPasswordLabel = new JLabel("Confirmer mot de passe:");
        JPasswordField confirmPasswordField = new JPasswordField(15);
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.0;
        formPanel.add(confirmPasswordLabel, gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(confirmPasswordField, gbc);

        // ===== BOUTONS =====
        JPanel buttonPanel = new JPanel();
        JButton registerButton = new JButton("S'inscrire");
        JButton cancelButton = new JButton("Annuler");
        buttonPanel.add(registerButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(cancelButton);

        // ===== STATUS =====
        JLabel registerStatusLabel = new JLabel(" ", JLabel.CENTER);
        registerStatusLabel.setForeground(Color.RED);
        registerStatusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ===== AJOUT AUX PANNEAUX =====
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(formPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(buttonPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(registerStatusLabel);

        registerDialog.add(mainPanel);
        registerDialog.setResizable(false);

        // ===== ACTIONS =====
        registerButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());

            // Validation
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

            // Création de l'utilisateur
            Utilisateur newUser = new Utilisateur(0, name, email, password, "client");
            UtilisateurDAO dao = new UtilisateurDAO();
            boolean success = dao.ajouterUtilisateur(newUser);

            if (success) {
                JOptionPane.showMessageDialog(registerDialog,
                        "Inscription réussie ! Vous pouvez maintenant vous connecter.",
                        "Succès", JOptionPane.INFORMATION_MESSAGE);
                registerDialog.dispose();

                // Pré-remplir les champs de connexion
                LoginView.this.emailField.setText(email);
                LoginView.this.passwordField.setText("");
                LoginView.this.userTypeComboBox.setSelectedItem("Client");

            } else {
                registerStatusLabel.setText("Erreur : email déjà utilisé ou problème de base");
            }
        });

        cancelButton.addActionListener(e -> registerDialog.dispose());

        registerDialog.setVisible(true);
    }


    // ==== Méthodes utilisées par le contrôleur ====

    public void setLoginAction(ActionListener action) {
        loginButton.addActionListener(action);
    }

    public String getEmail() {
        return emailField.getText();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    public String getUserType() {
        return (String) userTypeComboBox.getSelectedItem();
    }

    public void showError(String message) {
        statusLabel.setText(message);
    }

    // ==== Lancement de l'application ====

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
            catch (Exception e) { e.printStackTrace(); }

            LoginView loginView = new LoginView();
            loginView.setVisible(true);
            new controleur.LoginControleur(loginView); // Lien MVC
        });
    }
}
