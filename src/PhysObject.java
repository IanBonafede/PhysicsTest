/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import org.jbox2d.collision.shapes.*;
import org.jbox2d.common.*;
import org.jbox2d.dynamics.*;


import java.awt.Color;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_TRIANGLE_FAN;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glRectf;
import static org.lwjgl.opengl.GL11.glRotated;
import static org.lwjgl.opengl.GL11.glTranslated;
import static org.lwjgl.opengl.GL11.glVertex2f;


/**
 *
 * @author Ian Bonafede
 */
public class PhysObject {
    private Color color;
    private BodyDef bd;
    
    private Body body;
    private PolygonShape ps;
    private CircleShape cs;
    private FixtureDef fd;
    
    float x, y , w , h , r , s ;
    
    //wall constructor
    public PhysObject(World wld, float posx, float posy, float width, float height, Color c) {
        color = c;
        
        x = posx;
        y = posy;
        w = width;
        h = height;
        
        bd = new BodyDef();
        bd.position.set(new Vec2(x, y));
        bd.bullet = true;
        bd.type = BodyType.STATIC;
        
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
    //box constructor
    public PhysObject(World wld, float posx, float posy, float width, float height, Color c, BodyType bdtp) {
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
        fd.friction = 0.3f;
        fd.restitution = 0.8f;
        fd.density = 1;
        
        body.createFixture(fd);
        
    }
    
    //right triangle constructor
    public PhysObject(World wld, float posx, float posy, Color c, BodyType bdtp, float size) {
        color = c;
        
        x = posx;
        y = posy;
        s = size/2;
        
        bd = new BodyDef();
        bd.position.set(new Vec2(x, y));
        bd.bullet = true;
        bd.type = bdtp;
        
        
        body = wld.createBody(bd);
        
        
        
        ps = new PolygonShape();
        
       
        ps.m_vertexCount = 3;
        ps.m_vertices[0].set(-s, -s);
        ps.m_vertices[1].set(s, -s);
        ps.m_vertices[2].set(s, s);
        
        
        
        
        fd = new FixtureDef();
        fd.shape = ps;
        fd.friction = 0.3f;
        fd.restitution = 0.8f;
        fd.density = 1;
        
        body.createFixture(fd);
        
    }
    
    //circle constructor
    public PhysObject(World wld, float posx, float posy, float rad, Color c, BodyType bdtp) {
        color = c;
        
        x = posx;
        y = posy;
        r = rad/2;
        
        bd = new BodyDef();
        bd.position.set(new Vec2(x, y));
        bd.bullet = true;
        bd.type = bdtp;
        
        
        
        body = wld.createBody(bd);
        //body.setLinearVelocity(new Vec2((float) (-Math.PI), 10));
        
        cs = new CircleShape();
        cs.m_radius = r;
        
        fd = new FixtureDef();
        fd.shape = cs;
        fd.friction = 0.3f;
        fd.restitution = 0.8f;
        fd.density = 1;
        
        body.createFixture(fd);
        
    }
    
    public Body getBody() {
        return body;
    }
    
    void display() {
        Vec2 bodyPosition = body.getPosition().mul(30);
        glTranslated(bodyPosition.x, bodyPosition.y, 0);
        glRotated(Math.toDegrees(body.getAngle()), 0, 0, 1);
        
        glColor3f(color.getRed(), color.getGreen(), color.getBlue());
        
        
        if(ps != null) {
            glRectf(-w/2*30, -h/2*30, w/2*30, h/2*30);
        }
        if(cs != null) {
            drawCircle();
        }
        
        if(s != 0) {
            drawTriangle();
        }
        
    }
    
    
    private void drawTriangle() { 
	glBegin(GL_TRIANGLES); 
        
        for(int i = 0; i < ps.m_vertexCount; i++) {
            glVertex2f(ps.m_vertices[i].x*30, ps.m_vertices[i].y*30);
        }
	
	glEnd(); 
    }
    
    
    private void drawCircle() { 
	glBegin(GL_TRIANGLE_FAN); 
	for(float angle = 0; angle < 360.0; angle += 1) 
	{ 
		float newx = (float) (Math.sin(angle)*r*30);
                float newy = (float) (Math.cos(angle)*r*30);
                glVertex2f(newx,newy);

	} 
	glEnd(); 
    }
    
    
    
    
    
}
    
    
    
    

