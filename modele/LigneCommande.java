package modele;



public class LigneCommande {
    private int idLigne;
    private int idCommande;
    private int idProduit;
    private int quantite;
    private double prixUnitaire;
    private double sousTotal;

    public LigneCommande(int idLigne, int idCommande, int idProduit, int quantite, double prixUnitaire, double sousTotal) {
        this.idLigne = idLigne;
        this.idCommande = idCommande;
        this.idProduit = idProduit;
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
        this.sousTotal = sousTotal;
    }

    // Getters et Setters
    public int getIdLigne() { return idLigne; }
    public int getIdCommande() { return idCommande; }
    public int getIdProduit() { return idProduit; }
    public int getQuantite() { return quantite; }
    public double getPrixUnitaire() { return prixUnitaire; }
    public double getSousTotal() { return sousTotal; }

    public void setIdCommande(int idCommande) { this.idCommande = idCommande; }
    public void setIdProduit(int idProduit) { this.idProduit = idProduit; }
    public void setQuantite(int quantite) { this.quantite = quantite; }
    public void setPrixUnitaire(double prixUnitaire) { this.prixUnitaire = prixUnitaire; }
    public void setSousTotal(double sousTotal) { this.sousTotal = sousTotal; }
}

