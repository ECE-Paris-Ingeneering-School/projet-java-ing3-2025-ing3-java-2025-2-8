package controleur;

import dao.UtilisateurDAO;
import modele.Utilisateur;
import vue.AdminFrame;
import vue.ClientFrame;
import vue.LoginView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginControleur implements ActionListener {

    private LoginView view;


    public LoginControleur(LoginView view) {
        this.view = view;
        this.view.setLoginAction(this); // Connecte le bouton "Se connecter"
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String email = view.getEmail();
        String password = view.getPassword();

        // Conversion du type sélectionné (comboBox)
        String userTypeInput = view.getUserType();
        String userType = userTypeInput.equalsIgnoreCase("Client") ? "client" : "admin";

        if (email.isEmpty() || password.isEmpty()) {
            view.showError("Veuillez remplir tous les champs");
            return;
        }

        UtilisateurDAO dao = new UtilisateurDAO();
        Utilisateur utilisateur = dao.authenticate(email, password);

        if (utilisateur != null && utilisateur.getType().equalsIgnoreCase(userType)) {
            JOptionPane.showMessageDialog(view,
                    "Connexion réussie ! Bienvenue " + utilisateur.getNom(),
                    "Bienvenue",
                    JOptionPane.INFORMATION_MESSAGE);

            view.dispose(); // Fermer la fenêtre de login

            // Redirection selon le type d'utilisateur
            if (userType.equals("client")) {
                new vue.CatalogView(utilisateur).setVisible(true);
            }
            else {
                new AdminFrame(utilisateur).setVisible(true);

            }

        } else {
            view.showError("Identifiants ou type incorrect");
        }
    }
}
