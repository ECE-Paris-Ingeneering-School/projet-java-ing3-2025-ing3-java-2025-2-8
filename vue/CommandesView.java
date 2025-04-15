package vue;

import controleur.LoginControleur;
import dao.CommandeDAO;
import modele.Commande;
import modele.Utilisateur;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CommandesView extends JFrame {

    private JTable table;
    private DefaultTableModel model;

    public CommandesView() {
        setTitle("Mes commandes");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initComponents();
        chargerCommandes();
    }

    private void initComponents() {
        String[] columns = {"ID", "Date", "Total (€)"};
        model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        JButton voirBtn = new JButton("Voir les produits");
        voirBtn.addActionListener(e -> voirDetails());

        JPanel bottom = new JPanel();
        bottom.add(voirBtn);

        add(scrollPane, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }

    private void chargerCommandes() {
        Utilisateur u = LoginControleur.utilisateurConnecte;
        if (u == null) {
            JOptionPane.showMessageDialog(this, "Utilisateur non connecté");
            return;
        }

        List<Commande> commandes = new CommandeDAO().getCommandesParUtilisateur(u.getId());
        model.setRowCount(0);
        for (Commande c : commandes) {
            model.addRow(new Object[]{
                    c.getIdCommande(),
                    c.getDateCommande(),
                    c.getTotal()
            });
        }
    }

    private void voirDetails() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez une commande.");
            return;
        }

        int idCommande = (int) model.getValueAt(row, 0);
        new DetailsCommandeDialog(this, idCommande).setVisible(true);
    }
}
