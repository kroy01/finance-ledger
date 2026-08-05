package com.roycorp.financeledger;

import org.gnome.adw.Application;
import org.gnome.gio.ApplicationFlags;

/**
 * Entry point for the Finance Ledger prototype.
 */
public final class FinanceLedgerApplication {

    private static final String APPLICATION_ID =
            "com.roycorp.FinanceLedger";

    private FinanceLedgerApplication() {
    }

    public static void main(String[] args) {
        Application application = new Application(
                APPLICATION_ID,
                ApplicationFlags.DEFAULT_FLAGS
        );

        application.onActivate(
                () -> activate(application)
        );

        application.run(args);
    }

    private static void activate(Application application) {
        var activeWindow = application.getActiveWindow();

        if (activeWindow != null) {
            activeWindow.present();
            return;
        }

        MainWindow window = new MainWindow(application);
        window.present();
    }
}