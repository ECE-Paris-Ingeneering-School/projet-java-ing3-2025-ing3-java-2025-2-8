
/////// MAIN
// Imports
import controleur.LoginControleur;
import vue.LoginView;

import javax.swing.*;

public class main {
    public static void main(String[] args) {

        // Execution dans Swing
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            // vue de connexion :
            LoginView loginView = new LoginView();
            new LoginControleur(loginView);
            loginView.setVisible(true);
        });
    }
}
