package CurvaBezier;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import javax.swing.JPanel;

public class JPanelDibujo extends JPanel implements MouseMotionListener {

    double[][] Px, Py; // Matrices para guardar múltiples curvas
    int n=4, n1, w, h, h1, w2;
    int numCurvas = 3; // Total de curvas a dibujar
    boolean initiated = false;

    public JPanelDibujo() {
        super();        
    }
  
    public void init(){
        n = 4;
        n1 = n + 1;
        h = this.getHeight();
        w = this.getWidth();

        h1 = h - 1;
        w2 = w ;

        // Inicializamos las matrices
        Px = new double[numCurvas][n];
        Py = new double[numCurvas][n];

        // Coordenadas iniciales - Curva 0
        Px[0][0] = .1 * w2; Px[0][1] = .1 * w2; Px[0][2] = .9 * w2; Px[0][3] = .9 * w2;
        Py[0][0] = .1 * h1; Py[0][1] = .9 * h1; Py[0][2] = .9 * h1; Py[0][3] = .1 * h1;

        // Coordenadas iniciales - Curva 1
        Px[1][0] = .2 * w2; Px[1][1] = .4 * w2; Px[1][2] = .6 * w2; Px[1][3] = .8 * w2;
        Py[1][0] = .3 * h1; Py[1][1] = .7 * h1; Py[1][2] = .7 * h1; Py[1][3] = .3 * h1;

        // Coordenadas iniciales - Curva 2
        Px[2][0] = .1 * w2; Px[2][1] = .3 * w2; Px[2][2] = .7 * w2; Px[2][3] = .9 * w2;
        Py[2][0] = .5 * h1; Py[2][1] = .3 * h1; Py[2][2] = .3 * h1; Py[2][3] = .5 * h1;

        addMouseMotionListener(this);
        initiated = true;
    }

    public void drawSpline(Graphics2D g2d) {
        double step = 1. / w2, t;
        double[] Pxi = new double[n], Pyi = new double[n];
        int X, Y, Xold, Yold;

        // Ciclo para dibujar cada curva independientemente
        for (int c = 0; c < numCurvas; c++) {
            t = step;
            Xold = (int) Px[c][0];
            Yold = h1 - (int) Py[c][0];

            g2d.setColor(Color.blue);
            for (int i = 0; i < n; i++) {
                X = (int) Px[c][i];
                Y = h1 - (int) Py[c][i];
                g2d.drawRect(X - 1, Y - 1, 3, 3);
            }
            if (n > 2) {
                int Xo = Xold, Yo = Yold;
                for (int i = 1; i < n; i++) {
                    X = (int) Px[c][i];
                    Y = h1 - (int) Py[c][i];
                    g2d.drawLine(Xo, Yo, X, Y);
                    Xo = X;
                    Yo = Y;
                }
            }

            g2d.setColor(Color.red);
            for (int k = 1; k < w2; k++) {
                System.arraycopy(Px[c], 0, Pxi, 0, n);
                System.arraycopy(Py[c], 0, Pyi, 0, n);

                for (int j = n - 1; j > 0; j--) {
                    for (int i = 0; i < j; i++) {
                        Pxi[i] = (1 - t) * Pxi[i] + t * Pxi[i + 1];
                        Pyi[i] = (1 - t) * Pyi[i] + t * Pyi[i + 1];
                    }
                }

                X = (int) Pxi[0];
                Y = h1 - (int) Pyi[0];
                g2d.drawLine(Xold, Yold, X, Y);
                Xold = X;
                Yold = Y;
                t += step;
            }
        }
    }

    protected void clear(Graphics g) {
        super.paintComponent(g);
    }

    @Override
    public void paintComponent(Graphics g) {
        if (initiated){
            clear(g);
            Graphics2D g2d = (Graphics2D) g;
            drawSpline(g2d);
        }
    }

    public void destroy() {
        removeMouseMotionListener(this);
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
        int x = e.getX();
        if (x < 0) x = 0;
        if (x > w2 - 3) x = w2 - 3;
        int y = h1 - e.getY();
        if (y < 0) y = 0;
        if (y > h1) y = h1;

        int iMin = 0;
        int cMin = 0; 
        double Rmin = 1e10, r2, xi, yi;

        // Evalúa a qué curva pertenece el punto que estás arrastrando
        for (int c = 0; c < numCurvas; c++) {
            for (int i = 0; i < n; i++) {
                xi = (x - Px[c][i]);
                yi = (y - Py[c][i]);
                r2 = xi * xi + yi * yi;
                if (r2 < Rmin) {
                    cMin = c;
                    iMin = i;
                    Rmin = r2;
                }
            }
        }
        
        Px[cMin][iMin] = x;
        Py[cMin][iMin] = y;

        drawSpline((Graphics2D) this.getGraphics());
        repaint();
    }

    @Override
    public void repaint(){
        super.repaint();
        h = this.getHeight();
        w = this.getWidth();
        h1 = h - 1;
        w2 = w ;
    }
}