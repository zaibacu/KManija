import java.io.*;
import org.lwjgl.opengl.*;
import java.nio.*;
import java.util.*;
import net.phys2d.raw.Body;
import net.phys2d.raw.StaticBody;
import net.phys2d.raw.shapes.Box;

/**
 *
 * @author Šarūnas Navickas
 */
public class Duomenys
{
    private static DataInputStream dis = null;
    private static FileInputStream fis = null;
    private static BufferedInputStream bis = null;
    public static Map<String,Tekstūra> _tekstūros = new HashMap<String, Tekstūra>();
    public static List<Objektas> _objektai = new ArrayList<Objektas>();

    public static void addObj(boolean stat,String tex,int x, int y,int width,int height,float rot)
    {
        if(!Logika.tikrinimas(x, y))
        {
            Objektas obj = new Objektas();
            obj.tex = _tekstūros.get(tex);
            if(stat)
            {
                StaticBody body = new StaticBody(new Box(width,height));
                body.setPosition(x, y);
                body.setRotation(rot);
                body.setFriction(1.0f);
                obj.body = body;
                Logika.world.add(body);
            }
            else
            {
                Body body = new Body("obj",new Box(width,height),100);
                body.setPosition(x, y);
                body.setRotation(rot);
                body.setFriction(1.0f);
                obj.body = body;
                Logika.world.add(body);
            }
            obj.shape = "box";
            obj.width = width;
            obj.height = height;
            _objektai.add(obj);
        }
    }
    public static void loadObj(String dir, boolean stat) throws FileNotFoundException, IOException
    {
        int i = 1;
        Objektas obj;
        File file = new File(dir);
        fis = new FileInputStream(file);
        bis = new BufferedInputStream(fis);
        dis = new DataInputStream(bis);
        String buffer;
        while (dis.available() != 0)
        {
            buffer = dis.readLine();
            String[] buffer2 = buffer.split("&/");
            /*
             * 0 elementas: tekstūros pavadinimas
             * 1 elementas: x
             * 2 elementas: y
             * 3 elementas: width
             * 4 elementas: height
             * 5 elementas: rotacija
             */
            obj = new Objektas();
            obj.tex = _tekstūros.get(buffer2[0]);
            if(obj.tex == Duomenys._tekstūros.get("deze_pilka"))
                obj.role = "pilka";
            else if(obj.tex == Duomenys._tekstūros.get("deze_ruda"))
            {
                obj.role = "ruda";
                Logika._rudos += 1;
            }
            else if(obj.tex == Duomenys._tekstūros.get("deze_raudona"))
                obj.role = "raudona";
            else
                obj.role = null;
            if(stat)
            {

   
                StaticBody body = new StaticBody(new Box(Integer.parseInt(buffer2[3]),Integer.parseInt(buffer2[4])));
                body.setPosition(Integer.parseInt(buffer2[1]), Integer.parseInt(buffer2[2]));
                body.setRotation(Float.parseFloat(buffer2[5]));
                body.setFriction(1.0f);
                obj.body = body;
                Logika.world.add(body);
            }
            else
            {
                int weight = 0;
                if(obj.role.equalsIgnoreCase("pilka"))
                    weight = 1000;
                else if(obj.role.equalsIgnoreCase("raudona"))
                    weight = 50;
                else
                    weight = 100;
                Body body = new Body("obj"+i,new Box(Integer.parseInt(buffer2[3]),Integer.parseInt(buffer2[4])),weight);
                body.setPosition(Integer.parseInt(buffer2[1]), Integer.parseInt(buffer2[2]));
                body.setRotation(Float.parseFloat(buffer2[5]));
                body.setFriction(1.0f);
                obj.body = body;
                Logika.world.add(body);
            }
            obj.shape = "box";
            obj.width = Integer.parseInt(buffer2[3]);
            obj.height = Integer.parseInt(buffer2[4]);
            _objektai.add(obj);
            i++;
        }
        fis.close();
        bis.close();
        dis.close();
    }
    public static void loadTex(String pav, String dir)
    {
        Tekstūra tex = null;

        try
        {
            dis = new DataInputStream(new FileInputStream(new File(dir)));
        }
        catch (FileNotFoundException ex)
        {
            System.out.println(ex.toString());
        }
        byte[] TGARAWHEADER = new byte[12];
        try
        {
            dis.readFully(TGARAWHEADER);
        }
        catch (IOException ex)
        {
             System.out.println(ex.toString());
        }
        if (TGARAWHEADER[2] == 2)
        {
            try
            {
                tex = nesuspausta();
            }
            catch (IOException ex)
            {
                 System.out.println(ex.toString());
            }
        }
        else if (TGARAWHEADER[2] == 10)
        {
            try
            {
                tex = suspausta();
            }
            catch (IOException ex)
            {
                 System.out.println(ex.toString());
            }
        }
        TGARAWHEADER = null;

        _tekstūros.put(pav, tex);
    }
    private static Tekstūra suspausta() throws IOException
    {
        byte[] TGAHEADER = new byte[6];
	Tekstūra tex = new Tekstūra();

	dis.readFully(TGAHEADER);
	texInfo(TGAHEADER,tex);

	final byte[] textureData = new byte[tex.width * tex.height * tex.bpp];

	final int pixelCount = tex.width * tex.height;
	int currentByte		= 0;
	int currentPixel	= 0;
	int chunkHeader		= 0;
	byte[] chunkByte	= new byte[1];
	byte[] colorBuffer	= new byte[tex.bpp];

	do
        {
		dis.readFully(chunkByte);
		chunkHeader = chunkByte[0] & 0xFF;

	    	if (chunkHeader < 128)
                {
	    		chunkHeader++;

	    		for (int counter = 0; counter < chunkHeader; counter++) {
	    			dis.readFully(colorBuffer);

    				textureData[currentByte] = colorBuffer[0];	// R
	    			textureData[currentByte+1] = colorBuffer[1];	// G
	    			textureData[currentByte+2] = colorBuffer[2];	// B

    				// 32bit (alpha) check
	    			if (tex.bpp == 4)
    					textureData[currentByte+3] = colorBuffer[3];	// A

    				// increase the currByte counter
	    			currentByte+=tex.bpp;
	    			currentPixel++;
				}
	    	}
                else
                {	// RLE Header
    			chunkHeader -= 127;

	    		dis.readFully(colorBuffer);

	    		for (int counter = 0; counter < chunkHeader; counter++) {

	    			textureData[currentByte] = colorBuffer[0];	// R
    				textureData[currentByte+1] = colorBuffer[1];	// G
    				textureData[currentByte+2] = colorBuffer[2];	// B

	    			if (tex.bpp == 4)
    					textureData[currentByte+3] = colorBuffer[3];	// A

    				currentByte+=tex.bpp;
	    			currentPixel++;
    			}
	    	}

	}
        while (currentPixel < pixelCount);
	      tex = generuoti(tex, textureData);

	return tex;
    }
    private static Tekstūra nesuspausta() throws IOException
    {
        byte[] TGAHEADER = new byte[6];
	Tekstūra tex = new Tekstūra();

	dis.readFully(TGAHEADER);
	texInfo(TGAHEADER,tex);

	final byte[] textureData = new byte[tex.width * tex.height * tex.bpp];

	dis.readFully(textureData);

	tex = generuoti(tex, textureData);

	return tex;
    }
    private static void texInfo(byte[] header, Tekstūra tex)
    {
        tex.width	= header[1] * 256 + header[0];
	tex.height	= header[3] * 256 + header[2];
	tex.bitdepth	= header[4];
	tex.bpp		= (byte)(header[4] / 8);
    }
    private static Tekstūra generuoti(Tekstūra tex, byte[] textureData)
    {
        final ByteBuffer textureBuffer = ByteBuffer.allocateDirect(textureData.length).order(ByteOrder.nativeOrder());
    	textureBuffer.clear();
    	textureBuffer.put(textureData);
    	textureBuffer.flip();
    	final IntBuffer glName = ByteBuffer.allocateDirect(4).order(ByteOrder.nativeOrder()).asIntBuffer();
    	GL11.glGenTextures(glName);
    	tex.name = glName.get(0);
    	GL11.glBindTexture(GL11.GL_TEXTURE_2D,tex.name);
    	GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
    	GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
    	GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);

    	int glColor = tex.bitdepth == 32 ? GL12.GL_BGRA : GL12.GL_BGR;
    	int glColor2 = tex.bitdepth == 32 ? GL11.GL_RGBA : GL11.GL_RGB;
        try
        {
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D,0,glColor2,tex.width,tex.height,0,glColor,GL11.GL_UNSIGNED_BYTE,textureBuffer);
        }
        catch(Exception ex)
        {
            System.out.println(ex);
        }

        return tex;
    }
}
class Tekstūra
{
    public int  width    = 0;
    public int  height   = 0;
    public byte bitdepth = 0;
    public int  name     = -1;
    public byte bpp      = 0;
}
class Objektas
{
    public int width = 0;
    public int height = 0;
    public String shape = null;
    public Tekstūra tex = null;
    public Body body = null;
    public int id = 0;
    public String role = null;
}
