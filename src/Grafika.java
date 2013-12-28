import net.phys2d.math.ROVector2f;
import net.phys2d.raw.Body;
import net.phys2d.raw.shapes.*;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.*;
/**
 *
 * @author Šarūnas Navickas
 */
public class Grafika
{
    private static int _fps = 0;
    private static int _lastfps = 0;
    private static long _deltatime = 0;
    private static long _time = 0;
    private static long _lasttime = 0;
    private static int fps()
    {
        if(_lasttime != 0)
        _deltatime = (Sys.getTime() * 1000 / Sys.getTimerResolution()) - _lasttime;
        _lasttime = (Sys.getTime() * 1000 / Sys.getTimerResolution());
        _time += _deltatime;
        _fps++;
        if(_time >= 1000)
        {
            _lastfps = _fps;
            _fps = 0;
            _time = 0;
            return _lastfps;
        }
        return _lastfps;
    }
    public static void render() throws LWJGLException
    {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_STENCIL_BUFFER_BIT);
        //Logika.world.step();

        switch(Logika._būsena)
        {
            case 0:
                TextClass.print(315, 384, 24, "Press Enter to begin");
                break;
            case 1:
                objektai();
                ui();
                TextClass.print(150, 25, 12, "Points: "+Logika._taškai);
                TextClass.print(150, 12, 12, "Level: "+Logika._lygis);
                break;
            case 2:
                TextClass.print(315, 384, 24, "Game Over");
                TextClass.print(325, 354, 12, "Points: "+Logika._taškai+". Press Enter to restart");
                break;
            case 3:
                TextClass.print(315, 384, 24, "Victory!");
                TextClass.print(325, 354, 12, "Points: "+Logika._taškai+". Press Enter to continue");
                break;
            default:
                break;
        }
        fps();
        TextClass.print(0, 748, 12, "FPS: "+fps());
        
        Display.swapBuffers();
    }
    private static void ui()
    {
        //Paišom jėgos juostą
        for(int i = 1; i<10;i++)
        {
            GL11.glPushMatrix();
            GL11.glLoadIdentity();
            GL11.glOrtho(0.0, Display.getDisplayMode().getWidth(), 0.0, Display.getDisplayMode().getHeight(), -1.0, 1.0);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            if(Logika._x > i*0.1f)
            {
                if(i<=3)
                    GL11.glBindTexture(GL11.GL_TEXTURE_2D, Duomenys._tekstūros.get("jėga_geltonas").name);
                else if(i<=6)
                    GL11.glBindTexture(GL11.GL_TEXTURE_2D, Duomenys._tekstūros.get("jėga_žalias").name);
                else
                    GL11.glBindTexture(GL11.GL_TEXTURE_2D, Duomenys._tekstūros.get("jėga_raudonas").name);
            }
            else
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, Duomenys._tekstūros.get("jėga_pilkas").name);

            GL11.glTranslatef(i*10, 20, 0.0f);
            GL11.glBegin(GL11.GL_QUADS);
                 GL11.glTexCoord2f (0.0f, 0.0f);
                 GL11.glVertex2f(-4, 8);
                 GL11.glTexCoord2f (0.0f, 1.0f);
                 GL11.glVertex2f(-4,-8);
                 GL11.glTexCoord2f (1.0f, 1.0f);
                 GL11.glVertex2f(4,-8);
                 GL11.glTexCoord2f (1.0f, 0.0f);
                 GL11.glVertex2f(4,8);
            GL11.glEnd();

            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glPopMatrix();
        }
        //Paišom Patranką
        //Pabuklas
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0, Display.getDisplayMode().getWidth(), 0.0, Display.getDisplayMode().getHeight(), -1.0, 1.0);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, Duomenys._tekstūros.get("patranka2").name);

        GL11.glTranslatef(64, 144, 0.0f);
        GL11.glRotatef(Logika._kampas, 0.0f, 0.0f, 1.0f);
        GL11.glBegin(GL11.GL_QUADS);
             GL11.glTexCoord2f (0.0f, 0.0f);
             GL11.glVertex2f(-48, 24);
             GL11.glTexCoord2f (0.0f, 1.0f);
             GL11.glVertex2f(-48,-24);
             GL11.glTexCoord2f (1.0f, 1.0f);
             GL11.glVertex2f(48,-24);
             GL11.glTexCoord2f (1.0f, 0.0f);
             GL11.glVertex2f(48,24);
        GL11.glEnd();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glPopMatrix();
        //Stovas
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0, Display.getDisplayMode().getWidth(), 0.0, Display.getDisplayMode().getHeight(), -1.0, 1.0);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, Duomenys._tekstūros.get("patranka").name);

        GL11.glTranslatef(64, 116, 0.0f);
        GL11.glBegin(GL11.GL_QUADS);
             GL11.glTexCoord2f (0.0f, 0.0f);
             GL11.glVertex2f(-64, 64);
             GL11.glTexCoord2f (0.0f, 1.0f);
             GL11.glVertex2f(-64,-64);
             GL11.glTexCoord2f (1.0f, 1.0f);
             GL11.glVertex2f(64,-64);
             GL11.glTexCoord2f (1.0f, 0.0f);
             GL11.glVertex2f(64,64);
        GL11.glEnd();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glPopMatrix();    
    }
    private static void objektai()
    {
        for(int i = 0; i<Duomenys._objektai.size();i++)
        {
            GL11.glPushMatrix();
            GL11.glLoadIdentity();
            GL11.glOrtho(0.0, Display.getDisplayMode().getWidth(), 0.0, Display.getDisplayMode().getHeight(), -1.0, 1.0);
            Body body = Duomenys._objektai.get(i).body;
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, Duomenys._objektai.get(i).tex.name);
            if(Duomenys._objektai.get(i).shape == "box")
            {
                Box box = (Box) body.getShape();
                ROVector2f pos = body.getPosition();
                float width=Duomenys._objektai.get(i).width;
                float height=Duomenys._objektai.get(i).height;
                GL11.glTranslatef(pos.getX(), pos.getY(), 0.0f);
                GL11.glRotatef(body.getRotation()*57, 0.0f, 0.0f, 1.0f);
                GL11.glBegin(GL11.GL_QUADS);
                     GL11.glTexCoord2f (0.0f, 0.0f);
                     GL11.glVertex2f(-width/2, height/2);
                     GL11.glTexCoord2f (0.0f, 1.0f);
                     GL11.glVertex2f(-width/2,-height/2);
                     GL11.glTexCoord2f (1.0f, 1.0f);
                     GL11.glVertex2f(width/2,-height/2);
                     GL11.glTexCoord2f (1.0f, 0.0f);
                     GL11.glVertex2f(width/2,height/2);
                GL11.glEnd();
            }
            else if(Duomenys._objektai.get(i).shape == "circle")
            {
                Circle circle = (Circle) body.getShape();
                ROVector2f pos = body.getPosition();
                GL11.glTranslatef(pos.getX(), pos.getY(), 0.0f);
                GL11.glRotatef(body.getRotation()*57, 0.0f, 0.0f, 1.0f);
                GL11.glBegin(GL11.GL_QUADS);
                     GL11.glTexCoord2f (0.0f, 0.0f);
                     GL11.glVertex2f(-circle.getRadius(),circle.getRadius());
                     GL11.glTexCoord2f (0.0f, 1.0f);
                     GL11.glVertex2f(-circle.getRadius(),-circle.getRadius());
                     GL11.glTexCoord2f (1.0f, 1.0f);
                     GL11.glVertex2f(circle.getRadius(),-circle.getRadius());
                     GL11.glTexCoord2f (1.0f, 0.0f);
                     GL11.glVertex2f(circle.getRadius(),circle.getRadius());
                GL11.glEnd();
            }
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glPopMatrix();
            
        } 
    }
}
