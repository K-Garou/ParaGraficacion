package Fractales;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JFrame;

public class TrianguloSierpinski extends JFrame {

    public TrianguloSierpinski() {
        super("Triángulo de Sierpinski");
        setBounds(100, 100, 800, 600);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    // Método recursivo para dibujar los triángulos
    private void dibujarSierpinski(Graphics g, int nivel, int x1, int y1, int x2, int y2, int x3, int y3) {
        if (nivel == 0) {
            int[] x = {x1, x2, x3};
            int[] y = {y1, y2, y3};
            g.fillPolygon(x, y, 3);
        } else {
            // Calcular los puntos medios de cada lado del triángulo
            int m12x = (x1 + x2) / 2; int m12y = (y1 + y2) / 2;
            int m23x = (x2 + x3) / 2; int m23y = (y2 + y3) / 2;
            int m31x = (x3 + x1) / 2; int m31y = (y3 + y1) / 2;
            
            // Llamadas recursivas para los 3 triángulos exteriores
            dibujarSierpinski(g, nivel - 1, x1, y1, m12x, m12y, m31x, m31y);
            dibujarSierpinski(g, nivel - 1, m12x, m12y, x2, y2, m23x, m23y);
            dibujarSierpinski(g, nivel - 1, m31x, m31y, m23x, m23y, x3, y3);
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g); // Limpia el fondo
        g.setColor(new Color(0, 102, 204)); // Color azul
        // Nivel de profundidad 7
        dibujarSierpinski(g, 7, 400, 50, 50, 550, 750, 550); 
    }

    public static void main(String[] args) {
        new TrianguloSierpinski().setVisible(true);
    }
}