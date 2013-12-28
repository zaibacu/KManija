import java.io.FileNotFoundException;
import java.io.IOException;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

/**
 *
 * @author Šarūnas Navickas
 */
public class Main
{
    private static int _fps = 60;
    public static boolean _baigtas = false;
    private static Thread _logic;
    public static boolean _done = true;
    private static void cleanup()
    {        
        Display.destroy();
    }
    public static void cleanObj()
    {
        for(int i = 0; i<Duomenys._objektai.size();i++)
            {
                try
                {
                    Logika.world.remove(Duomenys._objektai.get(i).body);
                    Duomenys._objektai.remove(i);
                }
                catch(Exception ex)
                {}
            }
    }
    public static void objects() throws IOException
    {
        _done = false;
        System.out.print("Objects..");
        //Išvalom viską
        while(Duomenys._objektai.size() > 0)
        {
            try
            {
                cleanObj();
            }
            catch(Exception ex)
            {

            }
        }
        Logika._rudos = 0;
        Duomenys.loadObj("Lygiai/base.txt",true);
        try
        {
            Duomenys.loadObj("Lygiai/"+Logika._lygis+"/boxes.txt",false);
        }
        catch(Exception ex)
        {
            Duomenys.loadObj("Lygiai/1/boxes.txt",false);
        }
        System.out.print("done.\n");
        _done = true;
    }
    private static void init() throws LWJGLException, FileNotFoundException, IOException
    {
        System.out.println("Initializing:");
        System.out.print("Screen..");
        Display.setTitle("Kamuoliukų manija");
        Display.setFullscreen(true);
        Display.setVSyncEnabled(true);
        Display.create();
        Display.setDisplayMode(new DisplayMode(1024,768));
        System.out.print("done.\n");

        System.out.print("OpenGL..");
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0, Display.getDisplayMode().getWidth(), 0.0, Display.getDisplayMode().getHeight(), -1.0, 1.0);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        GL11.glViewport(0, 0, Display.getDisplayMode().getWidth(), Display.getDisplayMode().getHeight());
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        System.out.print("done.\n");
        System.out.print("Textures..");
        Duomenys.loadTex("kamuoliukas", "Tekstūros/kamuoliukas.tga");
        Duomenys.loadTex("blokelis", "Tekstūros/blokelis.tga");
        Duomenys.loadTex("patranka", "Tekstūros/patranka.tga");
        Duomenys.loadTex("patranka2", "Tekstūros/patranka2.tga");
        Duomenys.loadTex("jėga_pilkas", "Tekstūros/jėga_pilkas.tga");
        Duomenys.loadTex("jėga_geltonas", "Tekstūros/jėga_geltonas.tga");
        Duomenys.loadTex("jėga_žalias", "Tekstūros/jėga_žalias.tga");
        Duomenys.loadTex("jėga_raudonas", "Tekstūros/jėga_raudonas.tga");
        Duomenys.loadTex("deze_raudona", "Tekstūros/dėžė_raudona.tga");
        Duomenys.loadTex("deze_ruda", "Tekstūros/dėžė_ruda.tga");
        Duomenys.loadTex("deze_pilka", "Tekstūros/dėžė_pilka.tga");
        Duomenys.loadTex("font", "Tekstūros/font.tga");
        System.out.print("done.\n");
        _logic = new Thread(new LogicsThread());
    }
    private static void run() throws LWJGLException, IOException
    {
        while(!_baigtas)
        {
            Display.update();

            if (Display.isCloseRequested())
            {
                _baigtas = true;
            }
            else if (Display.isActive())
            {
                Grafika.render();
                Display.sync(_fps);
            }
            else
            {
                try
                {
                  Thread.sleep(100);
                }
                catch (InterruptedException e) {}
            }
            if (Display.isVisible() || Display.isDirty())
            {
                 Grafika.render();
            }
        }
    }
    public static void main(String[] args)
    {
        try
        {
            init();
            run();   
        }
        catch (Exception ex)
        {
            System.out.println(ex.toString());
            Sys.alert("Kamuoliukų Manija", "Nepavyko paleisti žaidimo.");
        }
        finally
        {
            cleanup();
        }
        System.exit(0);
    }

}
