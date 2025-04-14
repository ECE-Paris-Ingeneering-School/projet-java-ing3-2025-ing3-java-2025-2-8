package modele;

public class Produit {
    private int idProduit;
    private String nom;
    private double prixUnitaire;
    private int stock;
    private int qteLotPromo;
    private double prixLotPromo;
    private int idMarque;

    public Produit(int idProduit, String nom, double prixUnitaire, int stock, int qteLotPromo, double prixLotPromo, int idMarque) {
        this.idProduit = idProduit;
        this.nom = nom;
        this.prixUnitaire = prixUnitaire;
        this.stock = stock;
        this.qteLotPromo = qteLotPromo;
        this.prixLotPromo = prixLotPromo;
        this.idMarque = idMarque;
    }

    // Getters
    public int getIdProduit() { return idProduit; }
    public String getNom() { return nom; }
    public double getPrixUnitaire() { return prixUnitaire; }
    public int getStock() { return stock; }
    public int getQteLotPromo() { return qteLotPromo; }
    public double getPrixLotPromo() { return prixLotPromo; }
    public int getIdMarque() { return idMarque; }

    // Setters
    public void setIdProduit(int idProduit) { this.idProduit = idProduit; }
    public void setNom(String nom) { this.nom = nom; }
    public void setPrixUnitaire(double prixUnitaire) { this.prixUnitaire = prixUnitaire; }
    public void setStock(int stock) { this.stock = stock; }
    public void setQteLotPromo(int qteLotPromo) { this.qteLotPromo = qteLotPromo; }
    public void setPrixLotPromo(double prixLotPromo) { this.prixLotPromo = prixLotPromo; }
    public void setIdMarque(int idMarque) { this.idMarque = idMarque; }
}
