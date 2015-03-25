/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package actv.ccs.model.graphics.objects;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.Vector;
import javax.media.opengl.GL;
import static javax.media.opengl.GL.GL_TRUE;
import static javax.media.opengl.GL2ES2.GL_FRAGMENT_SHADER;
import static javax.media.opengl.GL2ES2.GL_PROGRAM_SEPARABLE;
import static javax.media.opengl.GL2ES2.GL_VERTEX_SHADER;
import javax.media.opengl.GL3;
import static javax.media.opengl.GL3.GL_GEOMETRY_SHADER;
import javax.media.opengl.GL4;
import static javax.media.opengl.GL4.GL_TESS_CONTROL_SHADER;
import static javax.media.opengl.GL4.GL_TESS_EVALUATION_SHADER;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;

/** //TEST!!!!!!!
 *
 * @author Victor
 */
public class shaderObject
{
    private int shdr, type;
    private static GLAutoDrawable shdrContext;
    private static boolean seperable;
    private String name, source;

    private int getShdr()
    {
        return shdr;
    }
    
    static public void setShdrContext(GLAutoDrawable context)
    {
        shdrContext = context;
    }

    public static void isSeperable(boolean seperable)
    {
        shaderObject.seperable = seperable;
    }

    public String getName()
    {
        return name;
    }
    
    

    public shaderObject(String name,String source, int type)
    {
        this.name = name;
        this.source = source;
        this.type = type;
    }
    
    public void initShader()
    {
        shdr = createShader(name,source,type);
    }
    
    static private int createShader(String name,String source, int type)
    {
        
        String shaderSrcs[];
        int ret;

        GL4 GL = (GL4) shdrContext.getGL();
        int lengths[];

        shaderSrcs = readShaderFiles(source);

        lengths = new int[shaderSrcs.length];
        for (int i = 0; i < lengths.length; i++)
        {
            lengths[i] = shaderSrcs[i].length();
        }

        ret = createShdrObject(name,type, lengths.length, lengths, shaderSrcs);

        return ret;
    }
    
    
    static private int createShdrObject(String name,int type, int length, int lenghs[],String source[])
    {
        GL4 GL = (GL4) shdrContext.getGL();
        int c[] = new int[1];
       
        int shdr  = GL.glCreateShader(type);
        
        GL.glShaderSource(shdr,length,source,lenghs,0);
     
        GL.glCompileShader(shdr);
        System.out.println("Shader: " + name);
        checkOpenGLError(shdrContext);
        GL.glGetShaderiv(shdr, GL4.GL_COMPILE_STATUS, c, 0);
        if (c[0] == 1)
        {
            System.out.println("... compilation succeeded.");
        } else
        {
            System.out.println("...  compilation failed.");
            printShaderLog(shdrContext, shdr);
        }
        
       
        return shdr; 
    }

    static private GLU glu = new GLU();

    static private boolean checkOpenGLError(GLAutoDrawable drawable)
    {
        GL4 gl = (GL4) drawable.getGL();
        boolean foundError = false;
        int glErr = gl.glGetError();
        while (glErr != GL.GL_NO_ERROR)
        {
            System.err.println("glError: " + glu.gluErrorString(glErr));
            foundError = true;
            glErr = gl.glGetError();
        }
        return foundError;
    }

    static private void printShaderLog(GLAutoDrawable d, int shader)
    {
        GL4 gl = (GL4) d.getGL();
        int[] len = new int[1];
        int[] charsWritten = new int[1];
        byte[] log = null;
        // determine the length of the shader compilation log   
        gl.glGetShaderiv(shader, GL4.GL_INFO_LOG_LENGTH, len, 0);
        if (len[0] > 0)
        {
            log = new byte[len[0]];
            gl.glGetShaderInfoLog(shader, len[0], charsWritten, 0, log, 0);
            System.out.println("Shader Info Log: ");
            for (int i = 0; i < log.length; i++)
            {
                System.out.print((char) log[i]);
            }
        }
    }

   static private void printProgramLog(GLAutoDrawable d, int prog)
    {
        GL4 gl = (GL4) d.getGL();
        int[] len = new int[1];
        int[] charsWritten = new int[1];
        byte[] log = null;

        // determine the length of the shader compilation log   
        gl.glGetProgramiv(prog, GL4.GL_INFO_LOG_LENGTH, len, 0);
        if (len[0] > 0)
        {
            log = new byte[len[0]];
            gl.glGetProgramInfoLog(prog, len[0], charsWritten, 0, log, 0);
            System.out.println("Program Info Log: ");
            for (int i = 0; i < log.length; i++)
            {
                System.out.print((char) log[i]);
            }
        }
    }

   static private String[] readShaderFiles(String ShadSrc)
    {
        Vector<String> lines = new Vector<String>();
        Scanner sc = null;
        File fl = null;
        String shaders[];

        try
        {
            fl = new File(ShadSrc);
            sc = new Scanner(fl);

        } catch (IOException e)
        {
            System.err.printf("Ecxetpion reading shaders!!!");
        }

        while (sc.hasNext())
        {
            lines.addElement(sc.nextLine());
        }

        shaders = new String[lines.size()];

        for (int j = 0; j < lines.size(); j++)
        {
            shaders[j] = (String) lines.elementAt(j) + "\n";
        }
        lines.clear();
        sc.close();
        fl = null;

        return shaders;
    }
    
   
    public static int generateProgram(shaderObject objs[])
    {
        int ret, linked[] = new int[1];
        GL4 GL = (GL4) shdrContext.getGL();

        int prog = GL.glCreateProgram();
        
        for (int i = 0; i < objs.length; i++)
            GL.glAttachShader(prog, objs[i].getShdr());
        
        
        if(seperable)
            GL.glProgramParameteri(prog,GL_PROGRAM_SEPARABLE,GL_TRUE);
        
        GL.glLinkProgram(prog);
        
        
        System.out.println("------------------------------------------");
        
        System.out.printf("\n Status of linking these shaders:\n");
        
        for(int i = 0; i < objs.length;i++)
            System.out.printf( objs[i].getName() + "  ");
        
        System.out.printf("\n");
        
        checkOpenGLError(shdrContext);
        GL.glGetProgramiv(prog, GL3.GL_LINK_STATUS, linked, 0);
        if (linked[0] == 1)
        {
            System.out.println("... linking succeeded.");
        } else
        {
            System.out.println("... linking failed.");
            printProgramLog(shdrContext, prog);
        }
        return prog;
    }
    
}
