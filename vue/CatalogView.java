package vue;

import dao.ProduitDao;
import modele.Produit;
import modele.Utilisateur;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * CatalogView: on Affiche le catalogue connecte a la BDD
 * la classse represente l'interface principale du catalogue
 * Permet à l'utilisateur de rechercher filtrer ajouter au panier et pouvoir payer pour ahceter
 */
public class CatalogView extends JFrame {

    private Utilisateur utilisateur;
    private JTextField searchField;
    private JComboBox<String> brandComboBox;
    private JTable articlesTable;
    private DefaultTableModel tableModel;
    private JLabel statusLabel;
    private CartView cartView;

    public CatalogView(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;

        setTitle("Catalogue - Application Shopping");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.cartView = new CartView(this); // création du panier

        initComponents();
        loadArticles(); // charge la BDD
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        // Titre
        JLabel welcomeLabel = new JLabel("Bienvenue, " + utilisateur.getNom(), JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(welcomeLabel, BorderLayout.NORTH);

        // Top Panel avec recherche
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        // Premiere ligne
        JPanel ligne1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JButton commandesBtn = new JButton("Mes commandes");
        commandesBtn.addActionListener(e -> new CommandesView().setVisible(true));
        ligne1.add(commandesBtn);

        // Deuxième ligne
        JPanel ligne2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchField = new JTextField(15);
        brandComboBox = new JComboBox<>();
        brandComboBox.addItem("Toutes les marques");
        brandComboBox.addActionListener(e -> searchArticles()); //filtre direct

        JButton searchButton = new JButton("Rechercher");
        searchButton.addActionListener(e -> searchArticles());

        JButton refreshButton = new JButton("Rafraîchir");
        refreshButton.addActionListener(e -> loadArticles());

        ligne2.add(new JLabel("Recherche :"));
        ligne2.add(searchField);
        ligne2.add(new JLabel("Marque :"));
        ligne2.add(brandComboBox);
        ligne2.add(searchButton);
        ligne2.add(refreshButton);

        topPanel.add(ligne1);
        topPanel.add(ligne2);

        add(topPanel, BorderLayout.PAGE_START);

        // 🧾 Tableau
        String[] columns = {
                "ID", "Nom", "Prix unitaire", "Prix en lot",
                "Quantité lot", "Marque", "Stock"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        articlesTable = new JTable(tableModel);
        articlesTable.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(articlesTable);
        add(scrollPane, BorderLayout.CENTER);

        // Panel bas
        JPanel bottomPanel = new JPanel(new BorderLayout());

        statusLabel = new JLabel(" ", JLabel.CENTER);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));
        bottomPanel.add(statusLabel, BorderLayout.NORTH);

        JButton addToCartButton = new JButton("Ajouter au panier");
        JButton viewCartButton = new JButton("Voir le panier");
        JButton checkoutButton = new JButton("Payer maintenant");

        addToCartButton.addActionListener(e -> ajouterProduitSelectionneAuPanier());
        viewCartButton.addActionListener(e -> cartView.setVisible(true));
        checkoutButton.addActionListener(e -> cartView.passerCommande());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addToCartButton);
        buttonPanel.add(viewCartButton);
        buttonPanel.add(checkoutButton);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);
    }


    /**
     * Charge tous les produits de la base via ProduitDAO
     */
    private void loadArticles() {
        tableModel.setRowCount(0);
        brandComboBox.removeAllItems();
        brandComboBox.addItem("Toutes les marques");

        ProduitDao dao = new ProduitDao();
        List<Produit> produits = dao.getAllProduitsAvecMarque();

        Set<String> marquesVues = new HashSet<>();

        for (Produit p : produits) {
            tableModel.addRow(new Object[]{
                    p.getIdProduit(),
                    p.getNom(),
                    p.getPrixUnitaire(),
                    p.getPrixLotPromo(),
                    p.getQteLotPromo(),
                    p.getNomMarque(),
                    p.getStock()
            });

            if (!marquesVues.contains(p.getNomMarque())) {
                marquesVues.add(p.getNomMarque());
                brandComboBox.addItem(p.getNomMarque());
            }
        }

        statusLabel.setText("Catalogue chargé : " + produits.size() + " produit(s)");
    }

    /**
     * Recherche les produits par nom ou marque
     */
    private void searchArticles() {
        String texte = searchField.getText().trim().toLowerCase();
        String marqueChoisie = (String) brandComboBox.getSelectedItem();
        boolean filtrerMarque = marqueChoisie != null && !marqueChoisie.equals("Toutes les marques");

        ProduitDao dao = new ProduitDao();
        List<Produit> produits = dao.getAllProduitsAvecMarque();

        tableModel.setRowCount(0);
        int total = 0;

        for (Produit p : produits) {
            boolean matchNom = texte.isEmpty() || p.getNom().toLowerCase().contains(texte);
            boolean matchMarque = !filtrerMarque || p.getNomMarque().equalsIgnoreCase(marqueChoisie);

            if (matchNom && matchMarque) {
                tableModel.addRow(new Object[]{
                        p.getIdProduit(),
                        p.getNom(),
                        p.getPrixUnitaire(),
                        p.getPrixLotPromo(),
                        p.getQteLotPromo(),
                        p.getNomMarque(),
                        p.getStock()
                });
                total++;
            }
        }

        statusLabel.setText(total + " produit(s) trouvé(s)");
    }

    /**
     * Ajoute le produit sélectionné dans le panier
     */
    private void ajouterProduitSelectionneAuPanier() {
        int row = articlesTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un article.");
            return;
        }

        int id = (int) tableModel.getValueAt(row, 0);
        String nom = (String) tableModel.getValueAt(row, 1);
        double prixU = (double) tableModel.getValueAt(row, 2);
        double prixLot = (double) tableModel.getValueAt(row, 3);
        int seuil = (int) tableModel.getValueAt(row, 4);
        int stock = (int) tableModel.getValueAt(row, 6);

        String input = JOptionPane.showInputDialog(this, "Quantité à ajouter au panier :", "Quantité", JOptionPane.PLAIN_MESSAGE);
        if (input == null) return;

        int quantite;
        try {
            quantite = Integer.parseInt(input);
            if (quantite <= 0 || quantite > stock) {
                JOptionPane.showMessageDialog(this, "Quantité invalide ou supérieure au stock.");
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Veuillez entrer un nombre valide.");
            return;
        }

        // Calcul du sous-total
        int lots = seuil > 0 ? quantite / seuil : 0;
        int reste = seuil > 0 ? quantite % seuil : quantite;
        double sousTotal = lots * prixLot + reste * prixU;

        cartView.addItemToCart(id, nom, prixU, quantite, prixLot, seuil, sousTotal);
        JOptionPane.showMessageDialog(this, "Produit ajouté au panier.");
    }
}
