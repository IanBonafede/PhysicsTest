
import java.awt.Point;
import org.jbox2d.dynamics.Body;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ian Bonafede
 */
public class RestrictedMovement extends Movement {
    Point[] points;
    Float[] times;
    
    
    
    public RestrictedMovement(Point[] ps, Float[] ts) {
        super();
        
        points = new Point[ps.length];
        for(int i = 0; i < ps.length; i++) {
            points[i] = ps[i];
        }
        
        times = new Float[ts.length];
        for(int i = 0; i < ts.length; i++) {
            times[i] = ts[i];
        }
    }
    
    
    
    
    public void doMovement(Body body) {
        
    }
}
