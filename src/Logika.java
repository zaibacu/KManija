import java.io.IOException;
import net.phys2d.math.*;
import net.phys2d.raw.*;
import net.phys2d.raw.shapes.*;
/**
 *
 * @author Šarūnas Navickas
 */
public class Logika
{
    public static float _g = 9.8f;
    public static float _m = 100.0f;
    public static float _k = 25000.0f;
    public static float _xmax = 1.0f;

    public static float _kampas = 0.0f;
    public static float _max_kampas = 90.0f;
    public static float _min_kampas = 0.0f;

    public static float _x = 0.0f;

    public static World world;
    private static boolean _init = false;
    public static boolean _debug = false;
    public static int _būsena = 0;

    public static int _taškai = 0;
    public static int _lygis = 1;
    public static int _rudos = 0;

    public static void init()
    {
        _init = true;
        world = new World(new Vector2f(0.0f, _g*-10),10);
        world.setDamping(1.0f);
    }
    public static void update() throws IOException
    {
        if(!_init)
            init();
        if(_būsena == 1)
        {
            if(_rudos <= 0)
                _būsena = 3;
            
            for(int i = 0; i<Duomenys._objektai.size();i++)
            {
                if(Duomenys._objektai.get(i).role != null)
                {
                    if(Duomenys._objektai.get(i).role == "raudona")
                    {
                        if(Duomenys._objektai.get(i).body.getPosition().getY() <= 150 && !_debug)
                            _būsena = 2;
                    }
                    else if(Duomenys._objektai.get(i).role == "ruda")
                    {
                        if(Duomenys._objektai.get(i).body.getPosition().getY() <= 100)
                        {
                            world.remove(Duomenys._objektai.get(i).body);
                            Duomenys._objektai.remove(i);
                            _taškai += 15;
                            _rudos -= 1;
                        }
                    }
                    else if(Duomenys._objektai.get(i).role == "kamuoliukas")
                    {
                        if(Duomenys._objektai.get(i).body.getPosition().getY() <= 75)
                        {
                            world.remove(Duomenys._objektai.get(i).body);
                            Duomenys._objektai.remove(i);
                        }
                    }
                }
            }
        }
        world.step();
    }
    public static boolean tikrinimas(int x, int y)
    {
        for(int i = 0; i<Duomenys._objektai.size();i++)
        {
            float x1, x2, y1, y2,ox1,ox2,oy1,oy2;
            x1 = Duomenys._objektai.get(i).body.getPosition().getX()-Duomenys._objektai.get(i).width/2;
            x2 = Duomenys._objektai.get(i).body.getPosition().getX()+Duomenys._objektai.get(i).width/2;
            y1 = Duomenys._objektai.get(i).body.getPosition().getY()-Duomenys._objektai.get(i).height/2;
            y2 = Duomenys._objektai.get(i).body.getPosition().getY()+Duomenys._objektai.get(i).height/2;
            if(tarp(x,x1,x2) && tarp(y,y1,y2))
                return true;
        }
        return false;
    }
    private static boolean tarp(float num1, float num2, float num3)
    {
        if(num2 < num3)
        {
            if(num1 > num2 && num1 < num3)
                return true;
            else
                return false;
        }
        else
        {
            if(num1 > num3 && num1 < num2)
                return true;
            else
                return false;
        }
    }
    public static void šauti(float jėga)
    {
        Objektas obj = new Objektas();
        obj.tex = Duomenys._tekstūros.get("kamuoliukas");
        Body body = new Body("kamuoliukas", new Circle(16),Logika._m);
        body.setPosition(64, 144);
        body.setDamping(0.5f);
        body.setRestitution(0.5f);
	body.setFriction(1.0f);
        body.setRotatable(true);
        body.setRotation(0.0f);
        body.setTorque(5.0f);
        body.addForce(new Vector2f((float)(jėga*Logika._m*Logika._g*Math.cos(Valdymas._a/Valdymas._c)),(float)(jėga*Logika._m*Logika._g*Math.sin(Valdymas._b/Valdymas._c))));
        //System.out.println("Force: "+body.getForce()+" kampas: "+_kampas);
        obj.body = body;
        obj.shape = "circle";
        obj.width = 16;
        obj.height = 16;
        obj.id = world.getBodies().size();
        obj.role = "kamuoliukas";
        Duomenys._objektai.add(obj);
        world.add(body);
    }
}
