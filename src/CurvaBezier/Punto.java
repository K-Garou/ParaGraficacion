/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package CurvaBezier;

/**
 *
 * @author GenaroML
 */
public class Punto {
    double x;
    double y;

    public void setLocation(double x, double y){
        this.x = x;
        this.y = y;
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }

}
