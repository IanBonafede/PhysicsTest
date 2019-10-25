/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import org.jbox2d.collision.shapes.*;
import org.jbox2d.common.*;
import org.jbox2d.dynamics.*;


import java.awt.Color;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glRectf;
import static org.lwjgl.opengl.GL11.glRotated;
import static org.lwjgl.opengl.GL11.glTranslated;


/**
 *
 * @author Ian Bonafede
 */
public class Box {
    private Color color;
    private BodyDef bd;
    private Body body;
    private PolygonShape ps;
    private FixtureDef fd;
    
    float x, y, w, h;
    
    
    //default constructor
    public Box(World wld, float posx, float posy, float width, float height, Color c, BodyType bdtp) {
        color = c;
        
        x = posx;
        y = posy;
        w = width;
        h = height;
        
        bd = new BodyDef();
        bd.position.set(new Vec2(x, y));
        bd.bullet = true;
        bd.type = bdtp;
        
        body = wld.createBody(bd);
        
        ps = new PolygonShape();
        ps.setAsBox(w/2, h/2);
        
        fd = new FixtureDef();
        fd.shape = ps;
        fd.friction = (float) 0.3;
        fd.restitution = (float) 0.7;
        fd.density = 1;
        
        body.createFixture(fd);
        
    }
    
    public Body getBody() {
        return body;
    }
    
    void display() {
        Vec2 bodyPosition = body.getPosition().mul(30);
        glTranslated(bodyPosition.x, bodyPosition.y, 0);
        glRotated(-Math.toDegrees(body.getAngle()), 0, 0, 1);
        
        glColor3f(color.getRed(), color.getGreen(), color.getBlue());
        
        glRectf(-w/2*30, -h/2*30, w/2*30, h/2*30);
        
        System.out.println("x: " + bodyPosition.x + "\ny: " + bodyPosition.y + "\n\n");
    }
    
    
    
    
    
    
}
    
    
    
    

