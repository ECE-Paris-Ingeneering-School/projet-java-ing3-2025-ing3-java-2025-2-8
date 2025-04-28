package controleur;

import dao.UtilisateurDAO;
import modele.Utilisateur;
import vue.AdminFrame;
import vue.CatalogView;
import vue.LoginView;
import vue.ClientFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginControleur implements ActionListener {

    private LoginView view;
    public static Utilisateur utilisateurConnecte;

    public LoginControleur(LoginView view) {
        this.view = view;
        this.view.setLoginAction(this); // Connecte le bouton "Se connecter"
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String email = view.getEmail();
        String password = view.getPassword();

        String userTypeInput = view.getUserType();
        String userType = userTypeInput.equalsIgnoreCase("Client") ? "client" : "admin";

        if (email.isEmpty() || password.isEmpty()) {
            view.showError("Veuillez remplir tous les champs");
            return;
        }

        UtilisateurDAO dao = new UtilisateurDAO();
        Utilisateur utilisateur = dao.authenticate(email, password);

        if (utilisateur != null && utilisateur.getType().equalsIgnoreCase(userType)) {
            LoginControleur.utilisateurConnecte = utilisateur;

            JOptionPane.showMessageDialog(view,
                    "Connexion réussie ! Bienvenue " + utilisateur.getNom(),
                    "Bienvenue",
                    JOptionPane.INFORMATION_MESSAGE);

            view.dispose();

            // ✅ Correction ici
            if (userType.equals("client")) {
                ClientFrame clientFrame = new ClientFrame(utilisateur);
                new ClientControleur(clientFrame, utilisateur);
                clientFrame.setVisible(true);
            } else {
                AdminFrame adminFrame = new AdminFrame();
                new AdminControleur(adminFrame);
                adminFrame.setVisible(true);
            }

        } else {
            view.showError("Identifiants ou type incorrect");
        }
    }

}
