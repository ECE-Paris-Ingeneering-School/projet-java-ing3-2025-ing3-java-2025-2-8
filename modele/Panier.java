//  model panier:
package modele;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe qui represente un panier qui contient plusieurs articles.
 * Permet d'ajouter, de retirer et de calculer le total des produits ajoute au panier.
 * @author Chris
 */
public class Panier
{
    private List<PanierItem> items;

    /**
     * Constructeur de la classe Panier.
     * Initialise un panier vide.
     */    public Panier()
    {
        this.items = new ArrayList<>();
    }

    /**
     * Ajoute un produit au panier.
     * Si le produit est déjà présent, augmente la quantité.
     * @param produit Produit à ajouter
     * @param quantite Quantité à ajouter
     */    public void ajouterProduit(Produit produit, int quantite)
    {
        for (PanierItem item : items)
        {
            if (item.getProduit().getIdProduit() == produit.getIdProduit())
            {
                item.setQuantite(item.getQuantite() + quantite);
                return;
            }
        }
        items.add(new PanierItem(produit, quantite));
    }

    /**
     * Supprime un produit du panier selon son id.
     * @param idProduit id du produit à supprimer
     */    public void supprimerProduit(int idProduit)
    {
        items.removeIf(item -> item.getProduit().getIdProduit() == idProduit);
    }

    /**
     * Retourne la liste des articles presents dans le panier.
     * @return Liste d'objets PanierItem
     */
    public List<PanierItem> getItems()
    {
        return items;
    }

    /**
     * Calcule le total du panier en additionnant les sous-totaux des articles.
     * @return Le total du panier
     */    public double getTotal()
    {
        double total = 0;
        for (PanierItem item : items)
        {
            total += item.getSousTotal();
        }
        return total;
    }

    /**
     * Vide tout le panier.
     */
    public void vider()
    {
        items.clear();
    }

    /**
     * Retourne en texte le contenu du panier.
     * @return Chaine de caracteres représentant le panier et son total
     */
    @Override

    public String toString()
    {
        StringBuilder sb = new StringBuilder("🛒 Panier :\n");
        for (PanierItem item : items)
        {
            sb.append(item.toString()).append("\n");
        }
        sb.append("Total : ").append(getTotal()).append(" €");
        return sb.toString();
    }
}
