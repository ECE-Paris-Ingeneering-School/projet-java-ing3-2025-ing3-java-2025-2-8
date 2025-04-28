package controleur;

import modele.Utilisateur;
import vue.CatalogView;
import vue.ClientFrame;
import vue.CommandesView;
import vue.LoginView;

import javax.swing.*;
import java.awt.event.ActionListener;

public class ClientControleur {
    private ClientFrame vue;
    private Utilisateur utilisateur;

    public ClientControleur(ClientFrame vue, Utilisateur utilisateur) {
        this.vue = vue;
        this.utilisateur = utilisateur;

        initListeners();
    }

    private void initListeners() {
        vue.getEnterCatalogButton().addActionListener(e -> ouvrirCatalogue());
        vue.getMyOrdersButton().addActionListener(e -> afficherCommandes());
        vue.getLogoutButton().addActionListener(e -> deconnexion());
    }

    private void ouvrirCatalogue() {
        CatalogView catalogView = new CatalogView(utilisateur);
        new CatalogControleur(catalogView, utilisateur);
        catalogView.setVisible(true);
        vue.dispose();
    }

    private void afficherCommandes() {
        CommandesView commandesView = new CommandesView();
        commandesView.setVisible(true);
    }

    private void deconnexion() {
        vue.dispose();
        new LoginView().setVisible(true);
    }
}
