package graficacion.dash3d;

import java.awt.Dimension;
import javax.swing.JFrame;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.util.FPSAnimator;

public class JuegoPrincipal {
    
    // --- VARIABLES DEL JUEGO (El estado de nuestro mundo) ---
    
    // 1. Variables del Cubo (Jugador)
    public float cuboY = 0.0f;       
    public float velocidadY = 0.0f;  
    public float gravedad = -0.03f;  
    
    // 2. Variables del Escenario (La "Caminadora")
    public float obstaculoX = 5.0f;  
    public float velocidadMapa = 0.11f; 
    
    // 3. Variables de Cámara y Luz
    public float rotacionEscena = 0.0f; 
    
    // 4. Variable de Estado del Juego
    public boolean gameOver = false; 
    public float escalaMuerte = 1.0f;     
    public float rotacionMuerte = 0.0f;   

    // 5. Variables de la Esfera Recolectable
    public int puntuacion = 0;              
    public boolean esferaActiva = true;     
    // -------------------------------------

    // --- COMPONENTES DE LA VENTANA ---
    public JuegoPrincipal() {
        // 1. Crear la ventana básica
        JFrame frame = new JFrame("Geometry Dash 3D - Proyecto Gráficas");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // 2. Crear el lienzo de OpenGL
        GLJPanel canvas = new GLJPanel();
        canvas.setPreferredSize(new Dimension(800, 600));
        
        // 3. Conectar nuestras otras clases
        MotorGrafico motor = new MotorGrafico(this); 
        Controles controles = new Controles(this);   
        
        canvas.addGLEventListener(motor);
        
        canvas.addKeyListener(controles);
        canvas.setFocusable(true);
        canvas.requestFocusInWindow();
        
        // 4. Armar la ventana y el motor de animación
        frame.add(canvas);
        frame.pack();
        frame.setVisible(true);
        
        FPSAnimator animator = new FPSAnimator(canvas, 60);
        animator.start();
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JuegoPrincipal();
            }
        });
    }
}