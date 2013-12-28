
import java.io.IOException;
import org.lwjgl.input.Keyboard;

/**
 *
 * @author Šarūnas Navickas
 */
public class LogicsThread implements Runnable
{
    private Thread _run;
    public LogicsThread()
    {
        _run = new Thread(this);
        System.out.println("Logics thread started");
        _run.start();
    }
    public void run()
    {
        while(!Main._baigtas)
        {
            try
            {
                Logika.update();
                if(Keyboard.isCreated())
                   Valdymas.update();
            }
            catch (IOException ex)
            {
                System.out.println(ex.toString());
            }
        }
    }
}
