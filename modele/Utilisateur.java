package modele;



public class Utilisateur {
    private int idUtilisateur;
    private String nom;
    private String email;
    private String motDePasse;
    private String type; // "client" ou "admin"

    public Utilisateur(int idUtilisateur, String nom, String email, String motDePasse, String type) {
        this.idUtilisateur = idUtilisateur;
        this.nom = nom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.type = type;
    }

    // Getters
    public int getIdUtilisateur() { return idUtilisateur; }
    public String getNom() { return nom; }
    public String getEmail() { return email; }
    public String getMotDePasse() { return motDePasse; }
    public String getType() { return type; }
    public int getId() {
        return idUtilisateur;
    }


    // Setters
    public void setIdUtilisateur(int id) { this.idUtilisateur = id; }
    public void setNom(String nom) { this.nom = nom; }
    public void setEmail(String email) { this.email = email; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }
    public void setType(String type) { this.type = type; }
}

