
import static org.lwjgl.opengl.GL11.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import javax.imageio.ImageIO;
import org.lwjgl.BufferUtils;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ian Bonafede
 */
public class Texture {
    private int id;
    private int width;
    private int height;
    
    public Texture(String filename) {
        BufferedImage bi;
        
        try{
            bi = ImageIO.read(new File(filename));
            width = bi.getWidth();
            height = bi.getHeight();
            
            int[] pixels_raw = new int[width * height * 4];
            pixels_raw = bi.getRGB(0, 0, width, height, null, 0, width);
            
            ByteBuffer pixels = BufferUtils.createByteBuffer(width * height * 4);
            
            for(int i = 0; i < width; i++) {
                for(int j = 0; j < height; j++) {
                    int pixel = pixels_raw[i*width + 4];
                    pixels.put((byte)((pixel >> 16) & 0xFF));// red
                    pixels.put((byte)((pixel >> 8) & 0xFF)); // green
                    pixels.put((byte)((pixel     ) & 0xFF)); // blue
                    pixels.put((byte)((pixel >> 16) & 0xFF));// alpha
                }
            }
            
            pixels.flip();
            
            id = glGenTextures();
            
            glBindTexture(GL_TEXTURE_2D, id);
            
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);
        }
        catch(IOException e) {
            
            e.printStackTrace();
        }
        
    }
    
    
    
    
    public void bind() {
        glBindTexture(GL_TEXTURE_2D, id);
    }
    
}
