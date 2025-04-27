// Model Marque
package modele;

/**
 * Classe qui représente la marque avec un id et un nom.
 * @author Chris
 */
public class Marque
{

    private int idMarque;
    private String nom;

    /**
     * Constructeur de la classe Marque.
     * @param idMarque
     * @param nom
     */
    public Marque(int idMarque, String nom)
    {
        this.idMarque = idMarque;
        this.nom = nom;
    }

    /**
     * Retourne l'id de la marque.
     * @return idMarque
     */
    public int getIdMarque()
    {
        return idMarque;
    }

    /**
     * Modifie l'id de la marque.
     * @param idMarque  nouveau id
     */
    public void setIdMarque(int idMarque)
    {
        this.idMarque = idMarque;
    }

    /**
     * Retourne le nom de la marque.
     * @return nom
     */
    public String getNom()
    {
        return nom;
    }

    /**
     * Retourne l'id de la marque.
     * @return idMarque
     */
    public int getId()
    {
        return idMarque;
    }

    /**
     * Modifie le nom de la marque.
     * @param nom Nouveau nom
     */
    public void setNom(String nom)
    {
        this.nom = nom;
    }

    /**
     * Retourne le nom de la marque en texte.
     * @return nom
     */
    @Override
    public String toString()
    {
        return nom;
    }
}