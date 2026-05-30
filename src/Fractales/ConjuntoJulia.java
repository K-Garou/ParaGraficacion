package Fractales;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;

public class ConjuntoJulia extends JFrame {

    private final int MAX_ITER = 300;
    private final double ZOOM = 250;
    private BufferedImage I;

    public ConjuntoJulia() {
        super("Conjunto de Julia");
        setBounds(100, 100, 800, 600);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        I = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);

        // Constante C que define la forma específica de este fractal de Julia
        double cX = -0.7;
        double cY = 0.27015;

        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                double zx = (x - 400) / ZOOM;
                double zy = (y - 300) / ZOOM;
                int iter = MAX_ITER;

                // Ecuación Z = Z^2 + C
                while (zx * zx + zy * zy < 4 && iter > 0) {
                    double tmp = zx * zx - zy * zy + cX;
                    zy = 2.0 * zx * zy + cY;
                    zx = tmp;
                    iter--;
                }
                // Coloreado usando operaciones de bits similar a tus archivos anteriores
                I.setRGB(x, y, iter | (iter << 6)); 
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(I, 0, 0, this);
    }

    public static void main(String[] args) {
        new ConjuntoJulia().setVisible(true);
    }
}