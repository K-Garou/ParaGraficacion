package graficacion.dash3d;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import java.io.File;
import java.io.IOException;

public class MotorGrafico implements GLEventListener {

    JuegoPrincipal padre;
    private GLU glu = new GLU();
    private GLUT glut = new GLUT();
    
    // --- NUESTRAS 4 TEXTURAS ---
    private Texture texturaCubo;
    private Texture texturaPiso;
    private Texture texturaPiramide;
    private Texture texturaEsfera;

    public MotorGrafico(JuegoPrincipal p) {
        this.padre = p;
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClearColor(0.1f, 0.1f, 0.2f, 1.0f); 
        gl.glEnable(GL2.GL_DEPTH_TEST);

        // Configuración de Iluminación
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_LIGHT0);
        
        float[] lightPos = { 8.0f, 6.0f, 4.0f, 1.0f };
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPos, 0);
        
        float[] luzAmbiente = { 0.7f, 0.7f, 0.7f, 1.0f }; 
        float[] luzDifusa = { 1.0f, 1.0f, 1.0f, 1.0f };
        float[] luzEspecular = { 1.0f, 1.0f, 1.0f, 1.0f };
        
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, luzAmbiente, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, luzDifusa, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, luzEspecular, 0);
        
        gl.glLightModeli(GL2.GL_LIGHT_MODEL_COLOR_CONTROL, GL2.GL_SEPARATE_SPECULAR_COLOR);
        
        gl.glShadeModel(GL2.GL_SMOOTH); 
        
        // --- PREPARAR TEXTURAS ---
        gl.glEnable(GL2.GL_TEXTURE_2D); 
        
        try {
            texturaCubo = TextureIO.newTexture(new File("texturas/creeperface.png"), true);
            texturaPiso = TextureIO.newTexture(new File("texturas/verde_oscuro.png"), true);
            texturaPiramide = TextureIO.newTexture(new File("texturas/pico_G.png"), true);
            texturaEsfera = TextureIO.newTexture(new File("texturas/agujero_negroR.png"), true);
            
            // Hacemos que la textura del piso se pueda repetir infinitamente
            texturaPiso.setTexParameteri(gl, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);
            texturaPiso.setTexParameteri(gl, GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);
            
            System.out.println("¡Texturas cargadas con éxito!");
            
        } catch (IOException e) {
            System.out.println("Error al cargar las texturas.");
        }
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        
        // --- FASE 1: LÓGICA Y FÍSICA ---
        if (padre.cuboY > 0 || padre.velocidadY > 0) {
            padre.velocidadY += padre.gravedad;
            padre.cuboY += padre.velocidadY;
        }
        if (padre.cuboY <= 0) { padre.cuboY = 0; padre.velocidadY = 0; }
        
        if (!padre.gameOver) {
            padre.obstaculoX -= padre.velocidadMapa;
            
            if (padre.obstaculoX + 34.0f < -8.0f) {
                padre.obstaculoX = 10.0f;
                padre.esferaActiva = true; 
            }
            
            boolean chocaEnY = (padre.cuboY < 0.6f);
            for (int grupo = 0; grupo < 3; grupo++) {
                float offsetGrupo = grupo * 15.0f;
                for (int i = 0; i < 2; i++) { 
                    float posPiramide = padre.obstaculoX + offsetGrupo + (i * 0.6f); 
                    boolean chocaEnX = (posPiramide - 0.3f < -1.5f) && (posPiramide + 0.3f > -2.5f);
                    if (chocaEnX && chocaEnY) { padre.gameOver = true; }
                }
            }
            
            if (padre.esferaActiva) {
                float posEsferaX = padre.obstaculoX + 7.5f;
                float posEsferaY = 2.0f;
                boolean tocaEsferaX = (posEsferaX - 0.5f < -1.5f) && (posEsferaX + 0.5f > -2.5f);
                boolean tocaEsferaY = (padre.cuboY + 1.0f >= posEsferaY - 0.4f); 
                if (tocaEsferaX && tocaEsferaY) {
                    padre.esferaActiva = false;
                    padre.puntuacion++;
                    System.out.println("¡ESFERA CAPTURADA! Puntos actuales: " + padre.puntuacion);
                }
            }
        } 
        
        if (padre.gameOver) {
            if (padre.escalaMuerte > 0.0f) {
                padre.escalaMuerte -= 0.05f;    
                padre.rotacionMuerte += 20.0f;  
            }
        }

        // --- FASE 2: DIBUJO ---
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
        glu.gluLookAt(0.0, 2.0, 8.0, 0.0, 1.0, 0.0, 0.0, 1.0, 0.0);

        // Usaremos color blanco puro como base para que se vean los colores reales de las texturas
        float[] colorBlanco = {1.0f, 1.0f, 1.0f, 1.0f};

        // 1. DIBUJAR PISO TEXTURIZADO
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE, colorBlanco, 0); //original
        
        //float[] colorPiso = {0.1f, 0.3f, 0.1f, 1.0f}; //comentar
        //gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE, colorPiso, 0); //comentar
        if (texturaPiso != null) texturaPiso.bind(gl); //quitar comentario
        
        gl.glBegin(GL2.GL_QUADS);
            gl.glNormal3f(0.0f, 1.0f, 0.0f);
            gl.glTexCoord2f(0.0f, 0.0f);   gl.glVertex3f(-20.0f, -0.5f,  5.0f);
            gl.glTexCoord2f(10.0f, 0.0f);  gl.glVertex3f( 50.0f, -0.5f,  5.0f);
            gl.glTexCoord2f(10.0f, 10.0f); gl.glVertex3f( 50.0f, -0.5f, -5.0f);
            gl.glTexCoord2f(0.0f, 10.0f);  gl.glVertex3f(-20.0f, -0.5f, -5.0f);
        gl.glEnd();

        // 2. DIBUJAR JUGADOR TEXTURIZADO (Cubo manual para soportar texturas)
        float[] brilloTextura = {1.0f, 1.0f, 1.0f, 1.0f};
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, brilloTextura, 0); //original
        gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, 60.0f); //original
        if (texturaCubo != null) texturaCubo.bind(gl); //quitar comentario
        //float[] colorCubo = {0.0f, 0.4f, 0.8f, 1.0f}; // comentar
        //gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE, colorCubo, 0); // comentar
        
        //gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, brilloTextura, 0); //comentar esto
        //gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, 60.0f); //y este tambien
        
        gl.glPushMatrix();
        gl.glTranslatef(-2.0f, padre.cuboY + 0.5f, 0.0f); 
        if (padre.gameOver) {
            gl.glRotatef(padre.rotacionMuerte, 0.0f, 0.0f, 1.0f);
            gl.glScalef(padre.escalaMuerte, padre.escalaMuerte, padre.escalaMuerte);
        }
        
        gl.glBegin(GL2.GL_QUADS);
            // Frontal
            gl.glNormal3f(0.0f, 0.0f, 1.0f);
            gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-0.5f, -0.5f,  0.5f);
            gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f( 0.5f, -0.5f,  0.5f);
            gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f( 0.5f,  0.5f,  0.5f);
            gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-0.5f,  0.5f,  0.5f);
            // Derecha
            gl.glNormal3f(1.0f, 0.0f, 0.0f);
            gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f( 0.5f, -0.5f,  0.5f);
            gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f( 0.5f, -0.5f, -0.5f);
            gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f( 0.5f,  0.5f, -0.5f);
            gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f( 0.5f,  0.5f,  0.5f);
            // Superior
            gl.glNormal3f(0.0f, 1.0f, 0.0f);
            gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-0.5f,  0.5f,  0.5f);
            gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f( 0.5f,  0.5f,  0.5f);
            gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f( 0.5f,  0.5f, -0.5f);
            gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-0.5f,  0.5f, -0.5f);
        gl.glEnd();
        gl.glPopMatrix();

        // 3. DIBUJAR PIRÁMIDES TEXTURIZADAS
        float[] sinBrillo = {0.0f, 0.0f, 0.0f, 1.0f}; // original
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, sinBrillo, 0); // Mate original
        if (texturaPiramide != null) texturaPiramide.bind(gl);
        
        //float[] colorObstaculo = {0.7f, 0.0f, 0.0f, 1.0f}; // comentar
        //gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE, colorObstaculo, 0); // comentar
        
        //gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, sinBrillo, 0); // Mate // comentar
        
        for (int grupo = 0; grupo < 3; grupo++) {
            float offsetGrupo = grupo * 15.0f;
            for (int i = 0; i < 2; i++) {
                gl.glPushMatrix();
                gl.glTranslatef(padre.obstaculoX + offsetGrupo + (i * 0.6f), 0.0f, 0.0f); 
                gl.glBegin(GL2.GL_TRIANGLES);
                    // Frontal
                    gl.glNormal3f(0.0f, 0.5f, 1.0f); 
                    gl.glTexCoord2f(0.5f, 1.0f); gl.glVertex3f(0.0f, 0.6f, 0.0f); 
                    gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-0.3f, 0.0f, 0.3f); 
                    gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(0.3f, 0.0f, 0.3f);
                    // Derecha
                    gl.glNormal3f(1.0f, 0.5f, 0.0f); 
                    gl.glTexCoord2f(0.5f, 1.0f); gl.glVertex3f(0.0f, 0.6f, 0.0f); 
                    gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(0.3f, 0.0f, 0.3f); 
                    gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(0.3f, 0.0f, -0.3f);
                    // Izquierda
                    gl.glNormal3f(-1.0f, 0.5f, 0.0f); 
                    gl.glTexCoord2f(0.5f, 1.0f); gl.glVertex3f(0.0f, 0.6f, 0.0f); 
                    gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-0.3f, 0.0f, 0.3f); 
                    gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-0.3f, 0.0f, -0.3f);
                gl.glEnd();
                gl.glPopMatrix();
            }
        }

        // 4. DIBUJAR ESFERA TEXTURIZADA
        if (padre.esferaActiva) {
            gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, brilloTextura, 0); //original
            gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, 100.0f); //original
            if (texturaEsfera != null) texturaEsfera.bind(gl); //original
            
            //float[] colorEsfera = {1.0f, 0.4f, 0.0f, 1.0f}; // comentar de aqui
            //gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE, colorEsfera, 0);
            
            //gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, brilloTextura, 0);
            //gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, 100.0f); // hasta aca
            
            
            gl.glPushMatrix();
            gl.glTranslatef(padre.obstaculoX + 7.5f, 2.0f, 0.0f); 
            
            // Usamos GLUquadric para que la textura envuelva la esfera automáticamente
            GLUquadric quad = glu.gluNewQuadric();
            glu.gluQuadricTexture(quad, true);
            glu.gluSphere(quad, 0.4, 20, 20);
            glu.gluDeleteQuadric(quad);
            
            gl.glPopMatrix();
        }
        gl.glFlush();
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();
        if (height <= 0) height = 1;
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(45.0, (float) width / height, 1.0, 100.0);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {}
}