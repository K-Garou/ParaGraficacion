package graficacion.dash3d;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Controles implements KeyListener {
    
    // Necesitamos una referencia a la clase principal para poder modificar sus variables
    JuegoPrincipal padre;

    public Controles(JuegoPrincipal p) {
        this.padre = p;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // No lo usamos, pero la interfaz KeyListener nos obliga a ponerlo
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int codigo = e.getKeyCode();

        // Si presionamos la BARRA ESPACIADORA
        if (codigo == KeyEvent.VK_SPACE) {
            
            if (this.padre.gameOver) { 
                this.padre.gameOver = false;
                this.padre.obstaculoX = 10.0f; 
                this.padre.cuboY = 0.0f;      
                this.padre.velocidadY = 0.0f; 
                this.padre.escalaMuerte = 1.0f;
                this.padre.rotacionMuerte = 0.0f;
                
                this.padre.puntuacion = 0;
                this.padre.esferaActiva = true;
                System.out.println("--- JUEGO REINICIADO ---");
                
            } else if (this.padre.cuboY <= 0.0f) {
                // Si NO hemos perdido y estamos en el piso, SALTAMOS
                this.padre.velocidadY = 0.47f; 
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Tampoco lo usamos en este juego
    }
}