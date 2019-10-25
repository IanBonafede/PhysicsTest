
import java.awt.Color;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glRectf;
import static org.lwjgl.opengl.GL11.glRotated;
import static org.lwjgl.opengl.GL11.glTranslated;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ian Bonafede
 */
public class Floor {
    private Color color;
    private BodyDef bd;
    
    private Body body;
    private PolygonShape ps;
    private FixtureDef fd;
    
    float x, y , w , h, m;
    double a;
    
    float leftx, lefty, rightx, righty;
    
    
    
    
    
    public Floor(World wld, float posx, float posy, float width, float height, Color c, double angle) {
        color = c;
        
        x = posx;
        y = posy;
        w = width;
        h = height;
        a = angle;
        
        
        
        
        
        
        if(a != 0) {
            lefty = (float) (y + h/2*Math.cos(a) - w/2*Math.sin(a));
            righty = (float) (y + h/2*Math.cos(a) + w/2*Math.sin(a));
            
            leftx = (float) (x - w/2*Math.cos(a) - h/2*Math.sin(a));
            rightx = (float) (x + w/2*Math.cos(a) - h/2*Math.sin(a));
            
            m = (righty - lefty)/(rightx - leftx);
        }
        
        
        
        if(a == 0) {
            lefty = y + h/2;
            righty = y + h/2;
            
            leftx = x - w/2;
            leftx = x + w/2;
            
            m = 0;
        }
        
        
        bd = new BodyDef();
        bd.position.set(new Vec2(x, y));
        bd.angle = (float) a;
        bd.bullet = true;
        bd.type = BodyType.KINEMATIC;
        
        body = wld.createBody(bd);
        
        ps = new PolygonShape();
        ps.setAsBox(w/2, h/2);
        
        fd = new FixtureDef();
        fd.shape = ps;
        fd.friction = 0.9f;
        fd.restitution = 0;
        fd.density = 1;
        
        body.createFixture(fd);
        
        
        
        
    }
    
    
    void display() {
        
        
        
        Vec2 bodyPosition = body.getPosition().mul(30);
        glTranslated(bodyPosition.x, bodyPosition.y, 0);
        glRotated(Math.toDegrees(body.getAngle()), 0, 0, 1);
        
        glColor3f(color.getRed(), color.getGreen(), color.getBlue());
        
        
        
        glRectf(-w/2*30, -h/2*30, w/2*30, h/2*30);
        
        
        
        
    }
    
    public float getAngle() {
        return body.getAngle();
    }
}
