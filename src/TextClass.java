
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

/**
 *
 * @author Šarūnas Navickas
 *
 * Klasė pasiskolinta iš ankščiau daryto mano projekto
 */
public class TextClass
{
    public static void print(float x, float y,int size, String msg)
    {
        float xInc = 32.0f / 512.0f;
        float yInc = 32.0f / 512.0f;

        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0, Display.getDisplayMode().getWidth(), 0.0, Display.getDisplayMode().getHeight(), -1.0, 1.0);

        GL11.glTranslatef(x, y, 0.0f);

        char[] symbols = msg.toCharArray();
        for(int i = 0; i<msg.length();i++)
        {
            float xStart = 32.0f*getCollumn((int)symbols[i]) / 512.0f;
            float yStart = 32.0f*getRow((int)symbols[i]) / 512.0f;


            GL11.glBindTexture(GL11.GL_TEXTURE_2D, Duomenys._tekstūros.get("font").name);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glBegin(GL11.GL_QUADS);

            GL11.glTexCoord2f (xStart, yStart);
            GL11.glVertex2i(size*i, size);

            GL11.glTexCoord2f (xStart, yStart + yInc);
            GL11.glVertex2i(size*i, 0);


            GL11.glTexCoord2f (xStart + xInc, yStart + yInc);
            GL11.glVertex2i(size + (i * size), 0);

            GL11.glTexCoord2f (xStart + xInc, yStart);
            GL11.glVertex2i(size + (i * size), size);   
            
          GL11.glEnd();
          GL11.glDisable(GL11.GL_TEXTURE_2D);
        }
        GL11.glPopMatrix();
    }
    public static int getRow(int num)
    {
        int res = 0;
        for(int i = 0; i<16;i++)
        {
            if(num < 16+16*i)
            {
                res = i;
                break;
            }
        }
        //System.out.println("Row:"+res);
        return res;
    }
    public static int getCollumn(int num)
    {
        //System.out.println("Collumn:"+((getRow(num)+1)*16-num));
        return num - getRow(num)*16;
    }

}
