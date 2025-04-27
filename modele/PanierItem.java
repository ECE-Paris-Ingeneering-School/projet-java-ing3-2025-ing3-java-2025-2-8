//  model PanierItem
package modele;

/**
 * Classe qui represente un article dans le panier avec son produit et sa quantite.
 * Permet de calculer le sous-total pour cet article.
 * @author Chris
 */
public class PanierItem
{
    private Produit produit;
    private int quantite;


    /**
     * Constructeur de la classe PanierItem.
     * @param produit Produit ajoute au panier
     * @param quantite Quantité du produit
     */    public PanierItem(Produit produit, int quantite)
    {
        this.produit = produit;
        this.quantite = quantite;
    }

    /**
     * Retourne le produit associé à l'article du panier.
     * @return produit
     */
    public Produit getProduit()
    {
        return produit;
    }

    /**
     * Retourne la qtte de produits.
     * @return quantite
     */
    public int getQuantite()
    {
        return quantite;
    }

    /**
     * Modifie la qtte de produits.
     * @param quantite Nouvelle quantite
     */
    public void setQuantite(int quantite)
    {
        this.quantite = quantite;
    }

    /**
     * Calcul le sous-total pour cet article en tenant compte des reductions eventuelle.
     * @return Le sous-total
     */    public double getSousTotal()
    {
        if (produit.getQteLotPromo() > 0 && produit.getPrixLotPromo() > 0)
        {
            int lots = quantite / produit.getQteLotPromo();
            int reste = quantite % produit.getQteLotPromo();
            return lots * produit.getPrixLotPromo() + reste * produit.getPrixUnitaire();
        }
        else
        {
            return quantite * produit.getPrixUnitaire();
        }
    }

    /**
     * Retourne une description texte de l'article du panier.
     * @return chaine de caractere avec le nom, la qtte et le sousTotal
     */
    @Override
    public String toString()
    {
        return produit.getNom() + " x" + quantite + " = " + getSousTotal() + " €";
    }
}