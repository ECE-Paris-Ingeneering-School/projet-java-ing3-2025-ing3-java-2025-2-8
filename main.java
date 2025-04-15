

import controleur.LoginControleur;
import vue.LoginView;

import javax.swing.*;

public class main {
    public static void main(String[] args) {
        // Exécution dans le thread d'interface Swing
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Lancer la vue de connexion
            LoginView loginView = new LoginView();
            new LoginControleur(loginView);
            loginView.setVisible(true);
        });
    }
}
