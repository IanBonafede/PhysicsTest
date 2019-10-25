
import java.awt.Color;
import java.util.ArrayList;
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



/**
 *
 * @author Ian Bonafede
 */
public class Player {
    
    
    private int maxHP;
    private int maxMP;
    private int maxSP;
    
    private int HP;
    private int MP;
    private int SP;
    
    float x, y, w, h;
    private BodyDef bd;
    
    private Body body;
    private PolygonShape ps;
    
    private FixtureDef fd;
    
    private Color c;
    
    private ArrayList<Ability> abilityList;
    
    
    
    public Player(World wld, float posx, float posy, Color color, ArrayList<Ability> abilities, int mhp, int mmp, int msp) {
        c = color;
        
        x = posx;
        y = posy;
        w = 1f;
        h = 2f;
        
        bd = new BodyDef();
        bd.position.set(new Vec2(x, y));
        bd.bullet = true;
        bd.type = BodyType.DYNAMIC;
        bd.fixedRotation = true;
        
        
        body = wld.createBody(bd);
        
        
        ps = new PolygonShape();
        ps.setAsBox(w/2, h/2);
        
        fd = new FixtureDef();
        fd.shape = ps;
        fd.friction = 0.2f;
        fd.restitution = 0;
        fd.density = 5;
        
        body.createFixture(fd);
        
        abilityList = new ArrayList<Ability>();
        
        for(int i = 0; i < abilities.size(); i++) {
            abilityList.add(abilities.get(i));
        }
        
        maxHP = 100;
        maxMP = 100;
        maxSP = 100;
        HP = maxHP;
        MP = maxMP;
        SP = maxSP;
    }
    
    void display() {
        
        
        
        Vec2 bodyPosition = body.getPosition().mul(30);
        glTranslated(bodyPosition.x, bodyPosition.y, 0);
        glRotated(Math.toDegrees(body.getAngle()), 0, 0, 1);
        
        glColor3f(c.getRed(), c.getGreen(), c.getBlue());
        
        glRectf(-w/2*30, -h/2*30, w/2*30, h/2*30);
        
        
    }
    
    public void checkAbilityActivation(boolean onGround, ArrayList lastKeysPressed, ArrayList newKeysPressed) {
        
        for(int i = 0; i < abilityList.size(); i++) {
            
            
            abilityList.get(i).activate(onGround, lastKeysPressed, newKeysPressed, HP, MP, SP, body);
            
            if(abilityList.get(i).isActive()) {
                HP -= abilityList.get(i).getHPCost();
                MP -= abilityList.get(i).getHPCost();
                SP -= abilityList.get(i).getSPCost();
                
             
            }
        }
    }
    
    public Body getBody() {
        return body;
    }
}
