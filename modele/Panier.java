package modele;

import java.util.ArrayList;
import java.util.List;
///
public class Panier {
    private List<PanierItem> items;

    public Panier() {
        this.items = new ArrayList<>();
    }

    public void ajouterProduit(Produit produit, int quantite) {
        for (PanierItem item : items) {
            if (item.getProduit().getIdProduit() == produit.getIdProduit()) {
                item.setQuantite(item.getQuantite() + quantite);
                return;
            }
        }
        items.add(new PanierItem(produit, quantite));
    }

    public void supprimerProduit(int idProduit) {
        items.removeIf(item -> item.getProduit().getIdProduit() == idProduit);
    }

    public List<PanierItem> getItems() {
        return items;
    }

    public double getTotal() {
        double total = 0;
        for (PanierItem item : items) {
            total += item.getSousTotal();
        }
        return total;
    }

    public void vider() {
        items.clear();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("🛒 Panier :\n");
        for (PanierItem item : items) {
            sb.append(item.toString()).append("\n");
        }
        sb.append("Total : ").append(getTotal()).append(" €");
        return sb.toString();
    }
}
