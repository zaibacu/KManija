import java.io.IOException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
/**
 *
 * @author Šarūnas Navickas
 */
public class Valdymas
{
    public static double _a,_b,_c;
    private static boolean _mouse_down = false;
    public static int _mouseX = 0;
    public static int _mouseY = 0;
    public static void update() throws IOException
    {
        _mouseX = Mouse.getX();
        _mouseY = Mouse.getY();
        
        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))//Išjungimas
        {
          Main._baigtas = true;
        }
        if(Main._done)
        {
            if (Keyboard.isKeyDown(Keyboard.KEY_RETURN) && Logika._būsena == 0 || Keyboard.isKeyDown(Keyboard.KEY_RETURN) && Logika._būsena == 2 || Keyboard.isKeyDown(Keyboard.KEY_RETURN) && Logika._būsena == 3)
            {
                if(Logika._būsena != 3)
                {
                    Logika._taškai = 0;
                    Logika._lygis = 1;
                }
                else
                    Logika._lygis++;
                Main.objects();
                Logika._būsena = 1;
            }
        }
        if(Mouse.isButtonDown(1) && Logika._debug)
        {
            Duomenys.addObj(false, "deze_ruda", Mouse.getX(), Mouse.getY(), 64, 64, 0);
        }
        if(Mouse.isButtonDown(0))
            _mouse_down = true;
        else
            _mouse_down = false;

        if(_mouse_down)
        {
            if(Logika._x < Logika._xmax)
                Logika._x += 0.01f;
        }
        else if(!_mouse_down && Logika._x > 0.1f)
        {
            Logika.šauti(Logika._k*Logika._x);
            Logika._x = 0.0f;
        }
        //Randam kampą kuriuo šausime
        float x1 = 64.0f;
        float y1 = 144.0f;
        float x2 = Mouse.getX();
        float y2 = Mouse.getY();
        if(x2 < x1)
            x2 = x1;
        if(y2 < y1)
            y2 = y1;
        
        _a = Math.sqrt(Math.pow(x2-x1, 2.0));
        _b = Math.sqrt(Math.pow(y2-y1, 2.0));
        _c = Math.sqrt(Math.pow(_a,2.0)+Math.pow(_b, 2.0));
        double sin = (_b/_c);
        float kampas = (float)(sin*180/Math.PI);
        
        if(kampas > Logika._max_kampas)
            kampas = Logika._max_kampas;
        else if(kampas < Logika._min_kampas)
            kampas = Logika._min_kampas;
        
        Logika._kampas = kampas;
        //System.out.println(tan*180/Math.PI);
    }
}
