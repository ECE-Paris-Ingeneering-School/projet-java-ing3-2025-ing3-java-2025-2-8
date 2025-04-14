package modele;

import java.util.Date;

public class Reduction {
    private int idReduction;
    private String description;
    private String typeReduction; // "pourcentage" ou "fixe"
    private double valeur;
    private Date dateDebut;
    private Date dateFin;
    private int idProduit;

    public Reduction(int idReduction, String description, String typeReduction, double valeur, Date dateDebut, Date dateFin, int idProduit) {
        this.idReduction = idReduction;
        this.description = description;
        this.typeReduction = typeReduction;
        this.valeur = valeur;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.idProduit = idProduit;
    }

    // Getters & Setters...

    public int getIdReduction() { return idReduction; }
    public String getDescription() { return description; }
    public String getTypeReduction() { return typeReduction; }
    public double getValeur() { return valeur; }
    public Date getDateDebut() { return dateDebut; }
    public Date getDateFin() { return dateFin; }
    public int getIdProduit() { return idProduit; }

    public void setIdReduction(int idReduction) { this.idReduction = idReduction; }
    public void setDescription(String description) { this.description = description; }
    public void setTypeReduction(String typeReduction) { this.typeReduction = typeReduction; }
    public void setValeur(double valeur) { this.valeur = valeur; }
    public void setDateDebut(Date dateDebut) { this.dateDebut = dateDebut; }
    public void setDateFin(Date dateFin) { this.dateFin = dateFin; }
    public void setIdProduit(int idProduit) { this.idProduit = idProduit; }

    @Override
    public String toString() {
        return description + " (" + typeReduction + ": " + valeur + ")";
    }
}