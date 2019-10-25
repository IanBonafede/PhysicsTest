/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import org.jbox2d.dynamics.World;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;





/**
 *
 * @author Ian Bonafede
 */
public class Main {

    private static final String WINDOW_TITLE = "TEST!";
    private static final int[] WINDOW_DIMENIONS = {1024, 768};
    
    private static World world;
    private static Set<PhysObject> physObjects;
    private static Set<Floor> floors;
    private static Set<Player> players;
    
    private static ArrayList lastKeysPressed = new ArrayList();
    private static ArrayList newKeysPressed = new ArrayList();
    
    private static Player p1;
    
    
    
    private static void render() {
        glClear(GL_COLOR_BUFFER_BIT);
        
        for(PhysObject phys : physObjects) {
            
            glPushMatrix();
                
            phys.display();
                
            glPopMatrix();
        }
        
        for(Floor fls : floors) {
            
            glPushMatrix();
                
            fls.display();
                
            glPopMatrix();
        }
        
        for(Player playrs : players) {
            
            glPushMatrix();
                
            playrs.display();
                
            glPopMatrix();
        }
    }
    
    private static void logic() {
        world.step(1/60f, 8, 3);
    }
    
    private static void input() {
        
        newKeys();
        
        for(Player player : players) {
            
            player.checkAbilityActivation(isOnGround(player), lastKeysPressed, newKeysPressed);
                
        }
        
        
    }
    
    private static void cleanUp(boolean asCrash) {
        
        Display.destroy();
    }
    
    private static void setUpMatrices() {
        glMatrixMode(GL_PROJECTION);
        glOrtho(0, 512, 0, 384, 1, -1);
        glMatrixMode(GL_MODELVIEW);
    }
    
    private static void setUpObjects() {
        world = new World(new Vec2(0, -9.8f), false);
        
        //setting up walls and roofs
        
        PhysObject lBarrier = new PhysObject(world, 0,        384/30/2f, 0.1f, 100f, Color.WHITE);
        PhysObject rBarrier = new PhysObject(world, 512/30f,  384/30/2f, 0.1f, 100f, Color.WHITE);
        PhysObject tBarrier = new PhysObject(world, 512/30/2, 384/30f,   100f, 0.1f, Color.WHITE);
        
        
        //setting up any type of floor the player might walk on
        Floor floor1 = new Floor(world, 512/30/2f, 0, 100f, 0.1f, Color.WHITE, 0);
        Floor floor2 = new Floor(world, 512/30/2f, -2, 100f, 0.1f, Color.WHITE, Math.PI/6);
        Floor floor3 = new Floor(world, 512/30/2f, -2, 100f, 0.1f, Color.WHITE, -Math.PI/4);
        Floor floor4 = new Floor(world, 512/30/2f, -14, 100f, 0.1f, Color.WHITE, Math.PI*3/8);
        
        floors = (Set<Floor>) new HashSet<Floor>();
        
        
        
        floors.add(floor1);
        floors.add(floor2);
        floors.add(floor3);
        floors.add(floor4);
        
        
        //setting up simple physics objects that will bounce around
        PhysObject box1 = new PhysObject(world, 225/30f, 240/30f, 1f, 1f, Color.CYAN, BodyType.DYNAMIC);
        PhysObject circle1 = new PhysObject(world, 275/30f, 240/30f, 1f, Color.ORANGE, BodyType.DYNAMIC);
        
        physObjects = (Set<PhysObject>) new HashSet<PhysObject>();
        
        physObjects.add(lBarrier);
        physObjects.add(rBarrier);
        physObjects.add(tBarrier);
        
        physObjects.add(box1);
        physObjects.add(circle1);
        
        //initializing players movements that correspond to abilities
        Movement moveRunLeft = new Movement(new Vec2(-4, 0), 5);
        Movement moveRunRight = new Movement(new Vec2(4, 0), 5);
        
        Movement moveAirRunLeft = new Movement(new Vec2(-1.5f, 0), 20);
        Movement moveAirRunRight = new Movement(new Vec2(1.5f, 0), 20);
        
        Movement moveJump = new Movement(new Vec2(0, 60), 60);
        
        //initializing the players abilities
        Ability runLeft = new Ability( new ArrayList(){{add(Keyboard.KEY_A);}}, new ArrayList(){{add(Keyboard.KEY_D);}}, true, true, 0, 0, 0, moveRunLeft, 0);
        Ability runRight = new Ability( new ArrayList(){{add(Keyboard.KEY_D);}}, new ArrayList(){{add(Keyboard.KEY_A);}}, true, true, 0, 0, 0, moveRunRight, 0);
        
        
        Ability airRunLeft = new Ability( new ArrayList(){{add(Keyboard.KEY_A);}}, new ArrayList(){{add(Keyboard.KEY_D);}}, false, true, 0, 0, 0, moveAirRunLeft, 0);
        Ability airRunRight = new Ability( new ArrayList(){{add(Keyboard.KEY_D);}}, new ArrayList(){{add(Keyboard.KEY_A);}}, false, true, 0, 0, 0, moveAirRunRight, 0);
        
        
        Ability jump = new Ability( new ArrayList(){{add(Keyboard.KEY_SPACE);}}, new ArrayList(), true, false, 0, 0, 0, moveJump, 0);
        
        
        //adding the abilities the the ability list
        ArrayList<Ability> p1Abilities = new ArrayList<Ability>();
        p1Abilities.add(runLeft);
        p1Abilities.add(runRight);
        p1Abilities.add(airRunLeft);
        p1Abilities.add(airRunRight);
        p1Abilities.add(jump);
        
        //initializing the player
        p1 = new Player(world, 100/30f, 100/30f, Color.WHITE, p1Abilities, 100, 100, 100);
        
        players = (Set<Player>) new HashSet<Player>();
        players.add(p1);
        
        
        
        
        
    }
    
    private static void update() {
        Display.update();
        Display.sync(60);
    }
    
    private static void enterGameLoop() {
        while(!Display.isCloseRequested()) {
            render();
            logic();
            input();
            update();
        }
    }
    
    private static void setUpStates() {
        
    }
    
    private static void setUpDisplay() {
        

        
        try {
            Display.setDisplayMode(new DisplayMode(WINDOW_DIMENIONS[0], WINDOW_DIMENIONS[1]));
            Display.setTitle(WINDOW_TITLE);
            Display.create();
        }
        catch (LWJGLException e) {
            e.printStackTrace();
            cleanUp(true);
        }
    }
    
    public static void main(String[] args) {
        setUpDisplay();
        setUpStates();
        setUpObjects();
        setUpMatrices();
        enterGameLoop();
        cleanUp(false);
        
    }

    
    public static boolean isOnGround(Player p) {
        
        for(Floor fls : floors) {
            
            if(fls.getAngle() == 0) {
                                             
                
                if(p.getBody().getPosition().y - p.h/2 - (fls.y + fls.h/2) < 0.02 && p.getBody().getPosition().x < fls.x + fls.w/2 && p.getBody().getPosition().x > fls.x - fls.w/2) {
                    return true;
                }
                
            }
            
            if(fls.getAngle() > 0 && fls.getAngle() < Math.PI*3/8) {
                
                if(p.getBody().getPosition().y - p.h/2 - fls.righty - (p.getBody().getPosition().x + p.w/2 - fls.rightx)*fls.m  < 0.03 ) {
                    return true;
                }
                
            }
            
            if(fls.getAngle() < 0 && fls.getAngle() > -Math.PI*3/8) {
                if(p.getBody().getPosition().y - p.h/2 - fls.righty - (p.getBody().getPosition().x - p.w/2 - fls.rightx)*fls.m  < 0.03 ) {
                    return true;
                }
                
            }
       
            
        }
        
        return false;
    }
    

    
    
    
    private static void newKeys() {
        lastKeysPressed.clear();
        
        System.out.print("Keys Pressing: ");
        
        for(int i = 0; i < newKeysPressed.size(); i++) {
            lastKeysPressed.add(newKeysPressed.get(i));
            
            if(i != 0)
                System.out.print(", ");
            System.out.print(newKeysPressed.get(i));
        }
        System.out.println();
        
        newKeysPressed.clear();
        
        
        if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
            newKeysPressed.add(Keyboard.KEY_SPACE);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_0)) {
            newKeysPressed.add(Keyboard.KEY_0);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_1)) {
            newKeysPressed.add(Keyboard.KEY_1);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_2)) {
            newKeysPressed.add(Keyboard.KEY_2);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_3)) {
            newKeysPressed.add(Keyboard.KEY_3);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_4)) {
            newKeysPressed.add(Keyboard.KEY_4);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_5)) {
            newKeysPressed.add(Keyboard.KEY_5);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_6)) {
            newKeysPressed.add(Keyboard.KEY_6);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_7)) {
            newKeysPressed.add(Keyboard.KEY_7);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_8)) {
            newKeysPressed.add(Keyboard.KEY_8);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_9)) {
            newKeysPressed.add(Keyboard.KEY_9);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
            newKeysPressed.add(Keyboard.KEY_A);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_B)) {
            newKeysPressed.add(Keyboard.KEY_B);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_C)) {
            newKeysPressed.add(Keyboard.KEY_C);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
            newKeysPressed.add(Keyboard.KEY_D);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_E)) {
            newKeysPressed.add(Keyboard.KEY_E);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_F)) {
            newKeysPressed.add(Keyboard.KEY_F);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_G)) {
            newKeysPressed.add(Keyboard.KEY_G);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_H)) {
            newKeysPressed.add(Keyboard.KEY_H);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_I)) {
            newKeysPressed.add(Keyboard.KEY_I);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_J)) {
            newKeysPressed.add(Keyboard.KEY_J);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_K)) {
            newKeysPressed.add(Keyboard.KEY_K);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_L)) {
            newKeysPressed.add(Keyboard.KEY_L);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_M)) {
            newKeysPressed.add(Keyboard.KEY_M);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_N)) {
            newKeysPressed.add(Keyboard.KEY_N);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_O)) {
            newKeysPressed.add(Keyboard.KEY_O);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_P)) {
            newKeysPressed.add(Keyboard.KEY_P);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_Q)) {
            newKeysPressed.add(Keyboard.KEY_Q);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_R)) {
            newKeysPressed.add(Keyboard.KEY_R);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
            newKeysPressed.add(Keyboard.KEY_S);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_T)) {
            newKeysPressed.add(Keyboard.KEY_T);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_U)) {
            newKeysPressed.add(Keyboard.KEY_U);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_V)) {
            newKeysPressed.add(Keyboard.KEY_V);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
            newKeysPressed.add(Keyboard.KEY_W);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_X)) {
            newKeysPressed.add(Keyboard.KEY_X);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_Y)) {
            newKeysPressed.add(Keyboard.KEY_Y);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_Z)) {
            newKeysPressed.add(Keyboard.KEY_Z);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_TAB)) {
            newKeysPressed.add(Keyboard.KEY_TAB);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_BACK)) {
            newKeysPressed.add(Keyboard.KEY_BACK);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            newKeysPressed.add(Keyboard.KEY_LSHIFT);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
            newKeysPressed.add(Keyboard.KEY_RSHIFT);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_F1)) {
            newKeysPressed.add(Keyboard.KEY_F1);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_F2)) {
            newKeysPressed.add(Keyboard.KEY_F2);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_F3)) {
            newKeysPressed.add(Keyboard.KEY_F3);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_F4)) {
            newKeysPressed.add(Keyboard.KEY_F4);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_F5)) {
            newKeysPressed.add(Keyboard.KEY_F5);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_F6)) {
            newKeysPressed.add(Keyboard.KEY_F6);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_F7)) {
            newKeysPressed.add(Keyboard.KEY_F7);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_F8)) {
            newKeysPressed.add(Keyboard.KEY_F8);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_F9)) {
            newKeysPressed.add(Keyboard.KEY_F9);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_F10)) {
            newKeysPressed.add(Keyboard.KEY_F10);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_F11)) {
            newKeysPressed.add(Keyboard.KEY_F11);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_F12)) {
            newKeysPressed.add(Keyboard.KEY_F12);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_FUNCTION)) {
            newKeysPressed.add(Keyboard.KEY_FUNCTION);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_F13)) {
            newKeysPressed.add(Keyboard.KEY_F13);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_F14)) {
            newKeysPressed.add(Keyboard.KEY_F14);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_F15)) {
            newKeysPressed.add(Keyboard.KEY_F15);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_F16)) {
            newKeysPressed.add(Keyboard.KEY_F16);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_F17)) {
            newKeysPressed.add(Keyboard.KEY_F17);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_F18)) {
            newKeysPressed.add(Keyboard.KEY_F18);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_F19)) {
            newKeysPressed.add(Keyboard.KEY_F19);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_NUMPAD0)) {
            newKeysPressed.add(Keyboard.KEY_NUMPAD0);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_NUMPAD1)) {
            newKeysPressed.add(Keyboard.KEY_NUMPAD1);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_NUMPAD2)) {
            newKeysPressed.add(Keyboard.KEY_NUMPAD2);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_NUMPAD3)) {
            newKeysPressed.add(Keyboard.KEY_NUMPAD3);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_NUMPAD4)) {
            newKeysPressed.add(Keyboard.KEY_NUMPAD4);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_NUMPAD5)) {
            newKeysPressed.add(Keyboard.KEY_NUMPAD5);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_NUMPAD6)) {
            newKeysPressed.add(Keyboard.KEY_NUMPAD6);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_NUMPAD7)) {
            newKeysPressed.add(Keyboard.KEY_NUMPAD7);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_NUMPAD8)) {
            newKeysPressed.add(Keyboard.KEY_NUMPAD8);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_NUMPAD9)) {
            newKeysPressed.add(Keyboard.KEY_NUMPAD9);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_NUMPADCOMMA)) {
            newKeysPressed.add(Keyboard.KEY_NUMPADCOMMA);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_NUMPADENTER)) {
            newKeysPressed.add(Keyboard.KEY_NUMPADENTER);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_NUMPADEQUALS)) {
            newKeysPressed.add(Keyboard.KEY_NUMPADEQUALS);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_ADD)) {
            newKeysPressed.add(Keyboard.KEY_ADD);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_MINUS)) {
            newKeysPressed.add(Keyboard.KEY_MINUS);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_SUBTRACT)) {
            newKeysPressed.add(Keyboard.KEY_SUBTRACT);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_MULTIPLY)) {
            newKeysPressed.add(Keyboard.KEY_MULTIPLY);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_BACKSLASH)) {
            newKeysPressed.add(Keyboard.KEY_BACKSLASH);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_SLASH)) {
            newKeysPressed.add(Keyboard.KEY_SLASH);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_DIVIDE)) {
            newKeysPressed.add(Keyboard.KEY_DIVIDE);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
            newKeysPressed.add(Keyboard.KEY_LCONTROL);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)) {
            newKeysPressed.add(Keyboard.KEY_RCONTROL);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_CAPITAL)) {
            newKeysPressed.add(Keyboard.KEY_CAPITAL);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_LBRACKET)) {
            newKeysPressed.add(Keyboard.KEY_LBRACKET);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_RBRACKET)) {
            newKeysPressed.add(Keyboard.KEY_RBRACKET);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_INSERT)) {
            newKeysPressed.add(Keyboard.KEY_INSERT);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_HOME)) {
            newKeysPressed.add(Keyboard.KEY_HOME);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            newKeysPressed.add(Keyboard.KEY_ESCAPE);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_DECIMAL)) {
            newKeysPressed.add(Keyboard.KEY_DECIMAL);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_DELETE)) {
            newKeysPressed.add(Keyboard.KEY_DELETE);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_COLON)) {
            newKeysPressed.add(Keyboard.KEY_COLON);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_APOSTROPHE)) {
            newKeysPressed.add(Keyboard.KEY_APOSTROPHE);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_UP)) {
            newKeysPressed.add(Keyboard.KEY_UP);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
            newKeysPressed.add(Keyboard.KEY_DOWN);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
            newKeysPressed.add(Keyboard.KEY_LEFT);
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
            newKeysPressed.add(Keyboard.KEY_RIGHT);
        }
        
        
    }
}
