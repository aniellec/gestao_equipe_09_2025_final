//arquivo Main.java

package app;

import swing.UsuarioSwing;

public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new UsuarioSwing().iniciar();
        });
    }
}
