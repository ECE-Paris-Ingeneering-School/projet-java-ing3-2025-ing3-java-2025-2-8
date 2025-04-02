import util.Databaseconnection;
import java.sql.Connection;
import java.sql.SQLException;

public class main {
    public static void main(String[] args) {
        // Test de la connexion à la base de données
        Connection conn = Databaseconnection.getConnection();
        if (conn != null) {
            try {
                // Vous pouvez ici lancer votre interface graphique, par exemple :
                // new LoginFrame().setVisible(true);

                // Pour l'instant, on ferme la connexion après test
                conn.close();
            } catch (SQLException e) {
                System.err.println("Erreur lors de la fermeture de la connexion : " + e.getMessage());
            }
        }
    }
}
