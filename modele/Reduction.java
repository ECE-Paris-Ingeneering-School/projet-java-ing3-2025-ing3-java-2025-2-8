//  Model Reduction:
package modele;

import java.util.Date;

/**
 * Classe réduction appliquée à un produit
 * @author Chris
 */
public class Reduction
{
    private int idReduction;
    private String description;
    private String typeReduction; // pourcentage
    private double valeur;
    private Date dateDebut;
    private Date dateFin;
    private int idProduit;

    /**
     * Constructeur de la classe Reduction.
     * @param idReduction
     * @param description
     * @param typeReduction
     * @param valeur
     * @param dateDebut
     * @param dateFin
     * @param idProduit
     */
    public Reduction(int idReduction, String description, String typeReduction, double valeur, Date dateDebut, Date dateFin, int idProduit)
    {
        this.idReduction = idReduction;
        this.description = description;
        this.typeReduction = typeReduction;
        this.valeur = valeur;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.idProduit = idProduit;
    }

    // Getters:

    /**
     * Retourne l'id de la réduction.
     * @return idReduction
     */
    public int getIdReduction()
    {
        return idReduction;
    }

    /**
     * Retourne la description de la réduction.
     * @return description
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Retourne le type de réduction.
     * @return typeReduction
     */
    public String getTypeReduction()
    {
        return typeReduction;
    }

    /**
     * Retourne la valeur de la réduction.
     * @return valeur
     */
    public double getValeur()
    {
        return valeur;
    }

    /**
     * Retourne la date de début de la réduction.
     * @return dateDebut
     */
    public Date getDateDebut()
    {
        return dateDebut;
    }

    /**
     * Retourne la date de fin de la réduction.
     * @return dateFin
     */
    public Date getDateFin()
    {
        return dateFin;
    }

    /**
     * Retourne l'id du produit concerné par la réduction.
     * @return idProduit
     */
    public int getIdProduit()
    {
        return idProduit;
    }


    // Setters:

    /**
     * Modifie l'id de la réduction.
     * @param idReduction Nouvel identifiant
     */
    public void setIdReduction(int idReduction)
    {
        this.idReduction = idReduction;
    }

    /**
     * Modifie la description de la réduction.
     * @param description Nouvelle description
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * Modifie le type de réduction.
     * @param typeReduction Nouveau type de réduction
     */
    public void setTypeReduction(String typeReduction)
    {
        this.typeReduction = typeReduction;
    }

    /**
     * Modifie la valeur de la réduction.
     * @param valeur Nouvelle valeur
     */
    public void setValeur(double valeur)
    {
        this.valeur = valeur;
    }

    /**
     * Modifie la date de debut de la réduction.
     * @param dateDebut Nouvelle date de debut
     */
    public void setDateDebut(Date dateDebut)
    {
        this.dateDebut = dateDebut;
    }

    /**
     * Modifie la date de fin de la réduction.
     * @param dateFin Nouvelle date de fin
     */
    public void setDateFin(Date dateFin)
    {
        this.dateFin = dateFin;
    }

    /**
     * Modifie l'identifiant du produit concerné.
     * @param idProduit Nouvel id du produit
     */
    public void setIdProduit(int idProduit)
    {
        this.idProduit = idProduit;
    }

    /**
     * Retourne une description de la réduction sous forme de texte.
     * @return Description de la réduction
     */
    @Override
    public String toString()
    {
        return description + " (" + typeReduction + ": " + valeur + ")";
    }
}