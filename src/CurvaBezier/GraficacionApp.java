/*
 * GraficacionApp.java
 */

package CurvaBezier;

/**
 * The main class of the application.
 */
public class GraficacionApp  {
    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GraficacionView().setVisible(true);
            }
        });
    }
}
