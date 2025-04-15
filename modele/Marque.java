////
package modele;

public class Marque {
    private int idMarque;
    private String nom;

    public Marque(int idMarque, String nom) {
        this.idMarque = idMarque;
        this.nom = nom;
    }

    public int getIdMarque() {
        return idMarque;
    }

    public void setIdMarque(int idMarque) {
        this.idMarque = idMarque;
    }

    public String getNom() {
        return nom;
    }
    public int getId() {
        return idMarque;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public String toString() {
        return nom;
    }
}