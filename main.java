// Imports
import controleur.LoginControleur;
import vue.LoginView;
import javax.swing.*;

/**
 * Classe main pour lancer l'application.
 * @author chris
 */
public class main
{
    /**
     * Lance l'interface graphique.
     * @param args Arg de la ligne de commande.
     */
    public static void main(String[] args)
    {

        //Interface Graphique
        SwingUtilities.invokeLater(() ->
        {
            try
            {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e)
            {
                e.printStackTrace();
            }

            // Fenetre connnexion
            LoginView loginView = new LoginView();
            new LoginControleur(loginView);
            loginView.setVisible(true);
        });
    }
}
