package controleur;

import vue.PaymentView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PaymentControleur {
    private PaymentView vue;

    public PaymentControleur(PaymentView vue) {
        this.vue = vue;
        initListeners();
    }

    private void initListeners() {
        vue.getPayButton().addActionListener(e -> processPayment());
        vue.getCancelButton().addActionListener(e -> cancelPayment());
    }

    private void processPayment() {
        if (vue.getPanierItems().isEmpty()) {
            vue.afficherMessageErreurPanierVide();
            return;
        }

        vue.getPayButton().setEnabled(false);
        vue.getCancelButton().setEnabled(false);
        vue.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        Timer timer = new Timer(1500, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                vue.setCursor(Cursor.getDefaultCursor());
                vue.afficherMessagePaiementSucces();
                vue.dispose();
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void cancelPayment() {
        if (vue.confirmerAnnulation()) {
            vue.dispose();
        }
    }
}
