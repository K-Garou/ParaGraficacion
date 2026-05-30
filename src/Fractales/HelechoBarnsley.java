package Fractales;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JFrame;

public class HelechoBarnsley extends JFrame {

    public HelechoBarnsley() {
        super("Helecho de Barnsley");
        setBounds(100, 100, 800, 600);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        
        // Color verde bosque para el helecho
        g.setColor(new Color(34, 139, 34)); 

        // Variables de inicio
        double x = 0;
        double y = 0;

        // Iteramos 60,000 veces para dibujar suficientes puntos y formar la figura
        for (int i = 0; i < 60000; i++) {
            double nextX, nextY;
            
            // "Lanzamos un dado" entre 0.0 y 1.0
            double r = Math.random();

            // Transformaciones matemáticas basadas en probabilidades
            if (r < 0.01) {
                // 1% de probabilidad: Dibuja el tallo base
                nextX = 0;
                nextY = 0.16 * y;
            } else if (r < 0.86) {
                // 85% de probabilidad: Construye las hojas principales sucesivas
                nextX = 0.85 * x + 0.04 * y;
                nextY = -0.04 * x + 0.85 * y + 1.6;
            } else if (r < 0.93) {
                // 7% de probabilidad: Construye la hoja izquierda
                nextX = 0.20 * x - 0.26 * y;
                nextY = 0.23 * x + 0.22 * y + 1.6;
            } else {
                // 7% de probabilidad: Construye la hoja derecha
                nextX = -0.15 * x + 0.28 * y;
                nextY = 0.26 * x + 0.24 * y + 0.44;
            }

            // Actualizamos nuestras coordenadas
            x = nextX;
            y = nextY;

            // Mapeamos las coordenadas matemáticas (que son muy pequeñas) al tamaño de la pantalla
            // Multiplicamos para escalar y sumamos/restamos para centrar en la ventana
            int drawX = (int) (400 + (x * 55));
            int drawY = (int) (550 - (y * 50));

            // Dibujamos un solo píxel en esa coordenada
            g.drawLine(drawX, drawY, drawX, drawY);
        }
    }

    public static void main(String[] args) {
        new HelechoBarnsley().setVisible(true);
    }
}