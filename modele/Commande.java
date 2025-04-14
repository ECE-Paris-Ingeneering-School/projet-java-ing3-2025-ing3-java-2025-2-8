package modele;


import java.util.Date;

public class Commande {
    private int idCommande;
    private Date dateCommande;
    private int idUtilisateur;
    private double total;

    public Commande(int idCommande, Date dateCommande, int idUtilisateur, double total) {
        this.idCommande = idCommande;
        this.dateCommande = dateCommande;
        this.idUtilisateur = idUtilisateur;
        this.total = total;
    }

    // Getters et Setters
    public int getIdCommande() { return idCommande; }
    public Date getDateCommande() { return dateCommande; }
    public int getIdUtilisateur() { return idUtilisateur; }
    public double getTotal() { return total; }

    public void setIdCommande(int idCommande) { this.idCommande = idCommande; }
    public void setDateCommande(Date dateCommande) { this.dateCommande = dateCommande; }
    public void setIdUtilisateur(int idUtilisateur) { this.idUtilisateur = idUtilisateur; }
    public void setTotal(double total) { this.total = total; }
}

