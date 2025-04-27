//  model PanierItem
package modele;
public class PanierItem
{
    private Produit produit;
    private int quantite;

    // Constructeur
    public PanierItem(Produit produit, int quantite)
    {
        this.produit = produit;
        this.quantite = quantite;
    }

    public Produit getProduit()
    {
        return produit;
    }

    public int getQuantite()
    {
        return quantite;
    }

    public void setQuantite(int quantite)
    {
        this.quantite = quantite;
    }

    // Calcul Sous Total
    public double getSousTotal()
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

    @Override
    public String toString()
    {
        return produit.getNom() + " x" + quantite + " = " + getSousTotal() + " €";
    }
}