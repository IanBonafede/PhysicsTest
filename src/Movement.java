
import java.awt.Point;
import org.jbox2d.common.Vec2;
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
public class Movement {
    
    private String type;
    
    private int maxSpeed;
    
    private Vec2 impulseVec;
    private Vec2 vecNot;
    
    private Point[] points;
    private Float[] times;
    
    public Movement() {
        
    }
    
    public Movement(Vec2 vec, int ms) {
        impulseVec = vec;
        vecNot = new Vec2(0,0);
        maxSpeed = ms;
        
        type = "free";
    }
    
    public Movement(Point[] ps, Float[] ts) {
        
        points = new Point[ps.length];
        for(int i = 0; i < ps.length; i++) {
            points[i] = ps[i];
        }
        
        times = new Float[ts.length];
        for(int i = 0; i < ts.length; i++) {
            times[i] = ts[i];
        }
        
        
        type = "restricted";
    }
    
    
    public void doMovement(Body body) {
        if(type.equals("free")) {
            if(Math.sqrt(   body.getLinearVelocity().x*body.getLinearVelocity().x + body.getLinearVelocity().y*body.getLinearVelocity().y ) <= maxSpeed)
                body.applyLinearImpulse(impulseVec, vecNot);
        }
        
        if(type.equals("restricted")) {
            
        }
        
    }
    
    public String getType() {
        return type;
    }
}
