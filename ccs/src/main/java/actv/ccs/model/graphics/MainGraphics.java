/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package actv.ccs.model.graphics;

import actv.ccs.model.graphics.objects.*;
import actv.ccs.model.ui.Iterator;
import graphicslib3D.Matrix3D;
import graphicslib3D.Point3D;
import graphicslib3D.Vector3D;
import graphicslib3D.light.PositionalLight;
import java.awt.Color;
import java.awt.Graphics2D;
import static java.awt.PageAttributes.ColorType.COLOR;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import javax.media.opengl.GL4;
import java.nio.*;
import java.util.Scanner;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.media.opengl.*;
import static javax.media.opengl.GL.GL_CCW;
import static javax.media.opengl.GL.GL_CULL_FACE;
import static javax.media.opengl.GL.GL_CW;
import static javax.media.opengl.GL.GL_DEPTH_BUFFER_BIT;
import static javax.media.opengl.GL.GL_DEPTH_TEST;
import static javax.media.opengl.GL.GL_FRONT_AND_BACK;
import static javax.media.opengl.GL.GL_LEQUAL;
import static javax.media.opengl.GL.GL_LINES;
import static javax.media.opengl.GL.GL_POINTS;
import static javax.media.opengl.GL.GL_TRIANGLES;
import static javax.media.opengl.GL.GL_TRUE;
import static javax.media.opengl.GL2ES2.GL_FRAGMENT_SHADER;
import static javax.media.opengl.GL2ES2.GL_FRAGMENT_SHADER_BIT;
import static javax.media.opengl.GL2ES2.GL_VERTEX_SHADER;
import static javax.media.opengl.GL2ES2.GL_VERTEX_SHADER_BIT;
import static javax.media.opengl.GL2ES3.GL_COLOR;
import static javax.media.opengl.GL2GL3.GL_LINE;
import static javax.media.opengl.GL3.GL_GEOMETRY_SHADER;
import static javax.media.opengl.GL3.GL_PROGRAM_POINT_SIZE;
import static javax.media.opengl.GL4.GL_PATCHES;
import static javax.media.opengl.GL4.GL_PATCH_VERTICES;
import static javax.media.opengl.GL4.GL_TESS_CONTROL_SHADER;
import static javax.media.opengl.GL4.GL_TESS_CONTROL_SHADER_BIT;
import static javax.media.opengl.GL4.GL_TESS_EVALUATION_SHADER;
import static javax.media.opengl.GL4.GL_TESS_EVALUATION_SHADER_BIT;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.GLU;

/**
 * ALL JOGL and opengl stuff is in here!
 *
 * 
 * 
 * The main logic of the graghics routine. This is where the main graghics
 * draw calls are here.
 * 
 * @author Victor
 */
public class MainGraphics implements GLEventListener
{
//TEST
    private GLCanvas GLc;
    private int rndPrograms[], tVertexArray[] = new int[1], texLoc[], vLoc[], norLoc[], samplers[], samplers2[] = new int[1] , txLoc0, txLoc1,
            uniShaderLocs[][], pipes[] = new int[6], cubeTexId;
    private shaderObject shadSrc[][];
    private String cubeMapTexSrc;
    private FloatBuffer offset, bgColor, triColor1, triColor2, triColor3;
    private GeoShape shapes[], attachedShapes[];
    private PositionalLight light;
    private Camera[] camera;
    private boolean showAxis;
    private int[] lightIndBuffer, cubeMapBuffer;
    private float[] cubeMapCoord, cubeMapVerts;
    private int GL_CC;
    private int GL_PROGRAM_SEPERABLE;
    private Iterator  itr;
    

    public MainGraphics(GLCanvas GLc, shaderObject shadSrc[][], PositionalLight pl, String cubeMapSrc,Iterator itr)
    {
        this.GLc = GLc;
        this.shadSrc = shadSrc; 
        this.itr = itr;
        
        camera = new Camera[1];
        light = pl;
        uniShaderLocs = new int[shadSrc.length][];
        cubeMapTexSrc = cubeMapSrc;
        uniShaderLocs[0] = new int[5];
        uniShaderLocs[1] = new int[5];
        uniShaderLocs[2] = new int[5];
        uniShaderLocs[3] = new int[5];
        uniShaderLocs[4] = new int[25];
        
        
        texLoc = new int[]{-1,-1,-1,-1,-1};
        vLoc = new int[]{-1,-1,-1,-1,-1};
        norLoc = new int[]{-1,-1,-1,-1,-1};
        
        
        //offset = FloatBuffer.allocate(4);
        bgColor = FloatBuffer.allocate(4);
        //init all the floatbuffers
        //triColor1 = FloatBuffer.allocate(4);
        //triColor2 = FloatBuffer.allocate(4);
        //triColor3 = FloatBuffer.allocate(4);
        camera[0] = new Camera("camera", 0f, 0f, 7.0f, 50.0f, 0.1f, 1000.0f, 1);
        //offset.put(2, 0.0f);
        //offset.put(3, 0.0f); //IMPORTANT!

        GLc.addGLEventListener(this);
        //triGradColor = triSolidColor = bgColor = new Color(0, 0, 0);
        showAxis = false;
        lightIndBuffer = new int[1];
        //cubeMapCoord = new float[] {0.25f, 0.667f, };

    }

    private void loadObjects(GLAutoDrawable drawable) //ASSUME VAO is binded ALREADY!!!!!
    {
        GL4 gl = (GL4) drawable.getGL();
        int count = 0;
        
        GeoShape obj;
        
        itr.reset();
        
        while(itr.hasNext())
        {
            obj = itr.getNext();
            count += obj.getNumNeededBuffers();
        }

        //for (int k = 0; k < attachedShapes.length; k++)
        //   count += attachedShapes[k].getNumNeededBuffers();
        int[] bufferIDs = new int[count];     // space for 2, we will just use 1    
        //  put the desired vertices into a float buffer,  //  then copy them into geometric buffer.  //  *4 because each float is 4 bytes.  

        gl.glGenBuffers(count, bufferIDs, 0);

        int offset = 0;
        
        
            
        
        itr.reset();
        
        while(itr.hasNext())
        {    
            obj = itr.getNext();
        
            obj.setTexLoc(texLoc[obj.getAttribID()]);
            obj.setvLoc(vLoc[obj.getAttribID()]);
            obj.setNorLoc(norLoc[obj.getAttribID()]);
            offset += obj.loadIntoBuffer(offset, bufferIDs, drawable);
        }

        /* for (int i = 0; i < attachedShapes.length; i++)
         {
         attachedShapes[i].setTexLoc(texLoc);
         attachedShapes[i].setvLoc(vLoc);
         attachedShapes[i].setNorLoc(norLoc);
         offset += attachedShapes[i].loadIntoBuffer(offset, bufferIDs, drawable); 
         }*/
    }

    private void setupCubeMap(GLAutoDrawable drawable)
    {
        GL4 gl = (GL4) drawable.getGL();
        FloatBuffer hnd;
        System.out.println("ffefefef");
        cubeMapBuffer = new int[2];
        
        gl.glGenBuffers(2, cubeMapBuffer, 0);
System.out.println("ffefefef");
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, cubeMapBuffer[0]);
        System.out.println("ffefefef");
        hnd = FloatBuffer.wrap(new float[]
        {
            -0.25f, 0.25f, -0.25f,
            -0.25f, -0.25f, -0.25f,
            0.25f, -0.25f, -0.25f,
            0.25f, -0.25f, -0.25f,
            0.25f, 0.25f, -0.25f,
            -0.25f, 0.25f, -0.25f,
            0.25f, -0.25f, -0.25f,
            0.25f, -0.25f, 0.25f,
            0.25f, 0.25f, -0.25f,
            0.25f, -0.25f, 0.25f,
            0.25f, 0.25f, 0.25f,
            0.25f, 0.25f, -0.25f,
            0.25f, -0.25f, 0.25f,
            -0.25f, -0.25f, 0.25f,
            0.25f, 0.25f, 0.25f,
            -0.25f, -0.25f, 0.25f,
            -0.25f, 0.25f, 0.25f,
            0.25f, 0.25f, 0.25f,
            -0.25f, -0.25f, 0.25f,
            -0.25f, -0.25f, -0.25f,
            -0.25f, 0.25f, 0.25f,
            -0.25f, -0.25f, -0.25f,
            -0.25f, 0.25f, -0.25f,
            -0.25f, 0.25f, 0.25f,
            -0.25f, -0.25f, 0.25f,
            0.25f, -0.25f, 0.25f,
            0.25f, -0.25f, -0.25f,
            0.25f, -0.25f, -0.25f,
            -0.25f, -0.25f, -0.25f,
            -0.25f, -0.25f, 0.25f,
            -0.25f, 0.25f, -0.25f,
            0.25f, 0.25f, -0.25f,
            0.25f, 0.25f, 0.25f,
            0.25f, 0.25f, 0.25f,
            -0.25f, 0.25f, 0.25f,
            -0.25f, 0.25f, -0.25f
        });
        gl.glBufferData(GL.GL_ARRAY_BUFFER, hnd.limit() * 4, hnd, GL.GL_STATIC_DRAW);

        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, cubeMapBuffer[1]);

        hnd = FloatBuffer.wrap(new float[]
        {
            .25f, .667f, .25f, .333f, .5f, .333f, .5f, .333f, .5f, .667f, .25f, .667f, .5f, .333f, .75f, .333f, .5f, .667f, .75f, .333f, .75f, .667f, .5f, .667f, .75f, .333f, 1.0f, .333f, .75f, .667f, 1.0f, .333f, 1.0f, .667f, .75f, .667f, 0.0f, .333f, .25f, .333f, 0.0f, .667f, .25f, .333f, .25f, .667f, 0.0f, .667f, .25f, 0.0f, .5f, 0.0f, .5f, .333f, .5f, .333f, .25f, .333f, .25f, 0.0f, .25f, .667f, .5f, .667f, .5f, 1.0f, .5f, 1.0f, .25f, 1.0f, .25f, .667f
        });
        System.out.println("ffefefef");
        gl.glBufferData(GL.GL_ARRAY_BUFFER, hnd.limit() * 4, hnd, GL.GL_STATIC_DRAW);

        cubeTexId = MainGraphics.loadTexture(drawable, cubeMapTexSrc);

    }
    
    
    private void setupBezierSurface(GLAutoDrawable drawable)
    {
        GL4 gl = (GL4) drawable.getGL();
        
       
      
        
        uniShaderLocs[3][0] = gl.glGetUniformLocation(rndPrograms[3], "mvp");
                
        
        //uniShaderLocs[1][4][0] = gl.glGetUniformLocation(rndPrograms[0][3], "mvp");
        
        
        uniShaderLocs[3][1] = gl.glGetUniformLocation(rndPrograms[3], "uOuter02");
        uniShaderLocs[3][2] = gl.glGetUniformLocation(rndPrograms[3], "uOuter13");
        uniShaderLocs[3][3] = gl.glGetUniformLocation(rndPrograms[3], "uInner0");
        uniShaderLocs[3][4] = gl.glGetUniformLocation(rndPrograms[3], "uInner1");
        
        
        
        
        
        
       
        
        int tex = gl.glGetUniformLocation(rndPrograms[3], "tex_color");
System.out.println("whatIgetnow!!!!!!!!!!!!!!!!!:" + uniShaderLocs[3][1]);
        gl.glGenSamplers(1, samplers2, 0);
  
        gl.glBindSampler(1,tex);
        
        
    }
    

    static public int loadTexture(GLAutoDrawable drawable, String texFile)
    {
        GL gl = drawable.getGL();
        String fullName = texFile;
        BufferedImage textureImage = getBufferedImage(fullName);
        System.out.println("Texture file: '" + fullName + "',  " + "size = " + textureImage.getWidth() + " x " + textureImage.getHeight());
        byte[] imgRGBA = getRGBAPixelData(textureImage);
        ByteBuffer wrappedRGBA = ByteBuffer.wrap(imgRGBA);
        // reserve an integer texture ID (name)  
        int[] textureIDs = new int[1];
        gl.glGenTextures(1, textureIDs, 0);
        int textureID = textureIDs[0];
        // make the textureID the "current texture"  
        gl.glBindTexture(GL.GL_TEXTURE_2D, textureID);
        // attach image texture to the currently active OpenGL texture ID  
        gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, // MIPMAP Level    
                GL.GL_RGBA, // number of color components    
                textureImage.getWidth(), // image size     
                textureImage.getHeight(), 0, // border size in pixels     
                GL.GL_RGBA, // pixel format     
                GL.GL_UNSIGNED_BYTE, // pixel data type   
                wrappedRGBA); // buffer holding texture data  
        // enable linear filtering for minification  // (or else default is MUST use MIPMaps...)  
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
        return textureID;
    }

    // Read the specified file and return a BufferedImage containing // the file's content. The file is assumed to be in a format // understood by ImageIO.read  (GIF, JPG, PNG, or BMP).  
    static public BufferedImage getBufferedImage(String fileName)
    {
        BufferedImage img;
        try
        {
            img = ImageIO.read(new File(fileName));
        } catch (IOException e)
        {
            System.err.println("Error reading '" + fileName + '"');
            throw new RuntimeException(e);
        }
        return img;
    }

    static public byte[] getRGBAPixelData(BufferedImage img)
    {
        byte[] imgRGBA;
        int height = img.getHeight(null);
        int width = img.getWidth(null);
        // create an (empty) BufferedImage with Raster and ColorModel  
        WritableRaster raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE, width, height, 4, null);
        ComponentColorModel colorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB), new int[]
        {
            8, 8, 8, 8
        }, // bits     
                true, // hasAlpha    
                false, // isAlphaPreMultiplied     
                ComponentColorModel.TRANSLUCENT, // transparency     
                DataBuffer.TYPE_BYTE); // data transfertype  
        BufferedImage newImage = new BufferedImage(colorModel, raster, false, // isRasterPremultiplied     
                null); // properties  
        // Since Java expects images to have their origin at the upper left  // while OpenGL expects the origin at the lower left,  // we must flip the image upside down.  // We create an AffineTransform to perform the flipping,  // and use the Graphics object for the new image to draw  // the old image into the new one, applying the AffineTransform  // as it draws (i.e. "upside down" in the Java sense, which will  // make it rightside up in the OpenGL sense).  
        AffineTransform gt = new AffineTransform();
        gt.translate(0, height);
        gt.scale(1, -1d);
        Graphics2D g = newImage.createGraphics();
        g.transform(gt);
        g.drawImage(img, null, null); // draw into new image  
        g.dispose();
        // retrieve the underlying byte array from the raster data buffer  
        DataBufferByte dataBuf = (DataBufferByte) raster.getDataBuffer();
        imgRGBA = dataBuf.getData();
        return imgRGBA;
    }

    public void panSelectedCam(int cameraNumber, float theta)
    {
        camera[cameraNumber].pan(theta);
    }

    public void fowardSelectedCam(int cameraNumber, float dx)
    {
        camera[cameraNumber].forward(dx);
    }

    public void pitchSelectedCam(int cameraNumber, float theta)
    {
        camera[cameraNumber].pitch(theta);
    }

    public void straefSelectedCam(int cameraNumber, float dx)
    {
        camera[cameraNumber].straef(dx);
    }

    public void setCameraPostion(int cameraNumber, Point3D f)
    {
        camera[cameraNumber].setCamPos(f);
    }

    public void vertmoveSelectedCam(int cameraNumber, float dx)
    {
        camera[cameraNumber].vertmove(dx);
    }

    public void translateObject(int objectIndex, float dx, float dy, float dz)
    {
        shapes[objectIndex].translate(dx, dy, dz);
    }

    public void setHeircalObject(int objectIndex, GeoShape shape)
    {
        shapes[objectIndex].setHericalObject(shape);
    }

    public void setObjectPos(int objectIndex, float dx, float dy, float dz)
    {
        shapes[objectIndex].setPostion(dx, dy, dz);
    }

    public void setLightPos(float x, float y, float z)
    {
        light.setPosition(new Point3D(x, y, z));
    }

    public Point3D getLightPos()
    {
        return light.getPosition();
    }

    public void incLightPos(float dx, float dy, float dz)
    {
        Point3D tmp = light.getPosition();

        light.setPosition(new Point3D(dx + tmp.getX(), dy + tmp.getY(), dz + tmp.getZ()));
    }

    public void setRotationAxis(int objectIndex, float dtheata, Vector3D vec, float dx, float dy, float dz)
    {
        shapes[objectIndex].setRotationAxis(dtheata, vec, dx, dy, dz);
    }

    public void setRotationAxis(int objectIndex, float dtheatax, float dtheatay, float dtheataz, float dx, float dy, float dz)
    {
        shapes[objectIndex].setRotationAxis(dtheatax, dtheatay, dtheataz, dx, dy, dz);
    }

    public void rotateObject(int objectIndex, float theta, Vector3D vec)
    {
        shapes[objectIndex].rotate(theta, vec);
    }

    public void rotateObject(int objectIndex, float dtheatax, float dtheatay, float dtheataz)
    {
        shapes[objectIndex].rotate(dtheatax, dtheatay, dtheataz);
    }

    public void translateObjectA(int objectIndex, float dx, float dy, float dz)
    {
        attachedShapes[objectIndex].translate(dx, dy, dz);
    }

    public void setHeircalObjectA(int objectIndex, GeoShape shape)
    {
        attachedShapes[objectIndex].setHericalObject(shape);
    }

    public void setObjectPosA(int objectIndex, float dx, float dy, float dz)
    {
        attachedShapes[objectIndex].setPostion(dx, dy, dz);
    }

    public void setRotationAxisA(int objectIndex, float dtheata, Vector3D vec, float dx, float dy, float dz)
    {
        attachedShapes[objectIndex].setRotationAxis(dtheata, vec, dx, dy, dz);
    }

    public void setRotationAxisA(int objectIndex, float dtheatax, float dtheatay, float dtheataz, float dx, float dy, float dz)
    {
        attachedShapes[objectIndex].setRotationAxis(dtheatax, dtheatay, dtheataz, dx, dy, dz);
    }

    public void rotateObjectA(int objectIndex, float theta, Vector3D vec)
    {
        attachedShapes[objectIndex].rotate(theta, vec);
    }

    public void rotateObjectA(int objectIndex, float dtheatax, float dtheatay, float dtheataz)
    {
        attachedShapes[objectIndex].rotate(dtheatax, dtheatay, dtheataz);
    }

    public void setShowAxis(boolean showAxis)
    {
        this.showAxis = showAxis;
    }

    private void drawObjects(GLAutoDrawable drawable)
    {
        
        itr.reset();
        
        while(itr.hasNext())
        {    
           itr.getNext().draw2(drawable, camera[0], null, rndPrograms, pipes, uniShaderLocs);
        }

    }

    public void setBGColor(Color color)
    {
        bgColor.put(0, (float) (((float) color.getRed()) / 255.0f));
        bgColor.put(1, (float) (((float) color.getGreen()) / 255.0f));
        bgColor.put(2, (float) (((float) color.getBlue()) / 255.0f));
        bgColor.put(3, (float) (((float) color.getAlpha()) / 255.0f));
    }

    public void toggleLights()
    {
        for (GeoShape shape : shapes)
        {
            shape.setLight(!shape.isLight());
        }

        for (GeoShape shape : attachedShapes)
        {
            shape.setLight(!shape.isLight());
        }
    }

    public void setTriColor(Color color)
    {

    }

    public void setTriColor(Color color1, Color color2, Color color3)
    {

    }

    public void paintScreen()
    {
        GLc.display();
    }

    private void clearBuffer(GL4 gl)
    {
        gl.glClearBufferfv(GL_COLOR, 0, bgColor);
    }

    private void drawTriangle(GL4 gl)
    {
        gl.glDrawArrays(GL4.GL_TRIANGLES, 0, 3);
    }

    public void offSet(float x, float y)
    {
        offset.put(0, x);
        offset.put(1, y);
    }

    public void init(GLAutoDrawable drawable)
    {
        GL4 GL = (GL4) drawable.getGL();
        System.out.printf("JOGL version in use: " + Package.getPackage("javax.media.opengl").getImplementationVersion() + "\nOPENGL version in use: " + GL.getGLProfile().getImplName() + "\n");
        samplers = new int[1];
        //GL.glGenProgramPipelines(5, pipes, 0);
       System.out.println("HERE!!wefefefef222wefwef!");
       
       shaderObject.setShdrContext(drawable);
       shaderObject.isSeperable(false);
       
       for(int i = 0; i < shadSrc.length; i++)
           for(int j = 0; j < shadSrc[i].length; j++)
               shadSrc[i][j].initShader();
       
       
       rndPrograms = createShaderPrograms(); //create the program
        //System.out.println("geo: " + rndPrograms[4][0]);
        vLoc[0] = GL.glGetAttribLocation(rndPrograms[0], "position");
        //texLoc[0] = GL.glGetAttribLocation(rndPrograms[1], "texPos");
       // norLoc[1] = GL.glGetAttribLocation(rndPrograms[1], "vertNormal"); 
       
       
        vLoc[1] = GL.glGetAttribLocation(rndPrograms[1], "vertPos");
        texLoc[1] = GL.glGetAttribLocation(rndPrograms[1], "texPos");
        //norLoc[1] = GL.glGetAttribLocation(rndPrograms[1], "vertNormal");
        
        vLoc[4] = GL.glGetAttribLocation(rndPrograms[4], "vertPos");
        texLoc[4] = GL.glGetAttribLocation(rndPrograms[4], "texPos");
        norLoc[4] = GL.glGetAttribLocation(rndPrograms[4], "vertNormal");
        
        
        System.out.println(vLoc);
        System.out.println(texLoc);
        System.out.println(norLoc);
        //System.out.println(rndPrograms[3][0]);
        
        
        //System.out.println("nloc:" + norLoc);

        camera[0].setMv_loc(GL.glGetUniformLocation(rndPrograms[0], "mv_matrix"));
        camera[0].setProj_loc(GL.glGetUniformLocation(rndPrograms[0], "proj_matrix"));

        camera[0].setTex_mv_loc(GL.glGetUniformLocation(rndPrograms[1], "mv_matrix"));
        camera[0].setTex_proj_loc(GL.glGetUniformLocation(rndPrograms[1], "proj_matrix"));

        camera[0].setAxismv_loc(GL.glGetUniformLocation(rndPrograms[2], "mv_matrix"));
        camera[0].setAxisproj_loc(GL.glGetUniformLocation(rndPrograms[2], "proj_matrix"));
        
        
        camera[0].setTex_mv_locL(GL.glGetUniformLocation(rndPrograms[4], "mv_matrix"));
        camera[0].setTex_proj_locL(GL.glGetUniformLocation(rndPrograms[4], "proj_matrix"));
        
        
         //uniShaderLocs[0][1] = new int[10]; 
        // uniShaderLocs[1][1] = new int[10]; 
         //vertex
         uniShaderLocs[4][0] = GL.glGetUniformLocation(rndPrograms[4], "light.ambient");
         uniShaderLocs[4][1] = GL.glGetUniformLocation(rndPrograms[4], "light.diffuse");
         uniShaderLocs[4][2] = GL.glGetUniformLocation(rndPrograms[4], "light.specular");
         uniShaderLocs[4][3] = GL.glGetUniformLocation(rndPrograms[4], "light.position");
        /* System.out.println("uniform0:" + uniShaderLocs[0][1][0]);
         System.out.println("uniform1:" + uniShaderLocs[0][1][1]);
         System.out.println("uniform2:" + uniShaderLocs[0][1][2]);
         System.out.println("uniform3:" + uniShaderLocs[0][1][3]);
         System.out.println("uniform4:" + uniShaderLocs[0][1][4]);
         System.out.println("uniform5:" + uniShaderLocs[0][1][5]);
         System.out.println("uniform6:" + uniShaderLocs[0][1][6]);
         System.out.println("uniform7:" + uniShaderLocs[0][1][7]);
         System.out.println("uniform8:" + uniShaderLocs[0][1][8]);
         System.out.println("uniform9:" + uniShaderLocs[0][1][9]);*/
       
         uniShaderLocs[4][4] = GL.glGetUniformLocation(rndPrograms[4], "material.ambient");
         uniShaderLocs[4][5] = GL.glGetUniformLocation(rndPrograms[4], "material.diffuse");
         uniShaderLocs[4][6] = GL.glGetUniformLocation(rndPrograms[4], "material.specular");
         uniShaderLocs[4][7] = GL.glGetUniformLocation(rndPrograms[4], "material.shininess");
       
         uniShaderLocs[4][8] = GL.glGetUniformLocation(rndPrograms[4], "globalAmbient");
         uniShaderLocs[4][9] = GL.glGetUniformLocation(rndPrograms[4], "normalMat");
       
       
         //frag 
         /*
         uniShaderLocs[4][0] = GL.glGetUniformLocation(rndPrograms[4], "light.ambient");
         uniShaderLocs[4][1] = GL.glGetUniformLocation(rndPrograms[4], "light.diffuse");
         uniShaderLocs[4][2] = GL.glGetUniformLocation(rndPrograms[4], "light.specular");
         uniShaderLocs[4][3] = GL.glGetUniformLocation(rndPrograms[4], "light.position");
        
         uniShaderLocs[4][4] = GL.glGetUniformLocation(rndPrograms[4], "material.ambient");
         uniShaderLocs[4][5] = GL.glGetUniformLocation(rndPrograms[4], "material.diffuse");
         uniShaderLocs[4][6] = GL.glGetUniformLocation(rndPrograms[4], "material.specular");
         uniShaderLocs[4][7] = GL.glGetUniformLocation(rndPrograms[4], "material.position");
       
         uniShaderLocs[4][8] = GL.glGetUniformLocation(rndPrograms[4], "globalAmbient");
         uniShaderLocs[4][9] = GL.glGetUniformLocation(rndPrograms[4], "normalMat");
       
         System.out.println("funiform0:" + uniShaderLocs[1][1][0]);
         System.out.println("funiform1:" + uniShaderLocs[1][1][1]);
         System.out.println("funiform2:" + uniShaderLocs[1][1][2]);
         System.out.println("funiform3:" + uniShaderLocs[1][1][3]);
         System.out.println("funiform4:" + uniShaderLocs[1][1][4]);
         System.out.println("funiform5:" + uniShaderLocs[1][1][5]);
         System.out.println("funiform6:" + uniShaderLocs[1][1][6]);
         System.out.println("funiform7:" + uniShaderLocs[1][1][7]);
         System.out.println("funiform8:" + uniShaderLocs[1][1][8]);
         System.out.println("funiform9:" + uniShaderLocs[1][1][9]);*/
        
         
        
         GL.glGenVertexArrays(tVertexArray.length, tVertexArray, 0);
        GL.glBindVertexArray(tVertexArray[0]);
        GL.glGenSamplers(3, samplers, 0);
        
        txLoc0 = GL.glGetUniformLocation(rndPrograms[1], "s");
        txLoc1 = GL.glGetUniformLocation(rndPrograms[4], "s");
        

        GL.glBindSampler(1, txLoc0);
        GL.glBindSampler(1, txLoc1);

       
        System.out.println("HERE!!wefwefwef!");
        loadObjects(drawable);
        System.out.println("HERE!!wefwefwef!");
        setupCubeMap(drawable);
        initLightindicator(drawable);
        
       setupBezierSurface(drawable);

        System.out.println("HERE!!!");

        
        
        
        System.out.println("sampler:" + samplers[0]);

        GL.glEnable(GL_CULL_FACE);
        GL.glFrontFace(GL_CW);
        GL.glEnable(GL_DEPTH_TEST);
        GL.glEnable(GL_PROGRAM_POINT_SIZE);
        //GL.glDepthFunc(GL_LEQUAL);
        System.out.println("HERE!!!");

    }
    
    /*
    shdSource[0][0] = new shaderObject("stdVert",mainFilePath + "shaders" + File.separator + "vs.fx",GL_VERTEX_SHADER);
        shdSource[1][0] = new shaderObject("stdFrag",mainFilePath + "shaders" + File.separator + "fs.fx",GL_FRAGMENT_SHADER);
        
        
       
        shdSource[0][1] = new shaderObject("TexVert",mainFilePath + "shaders" + File.separator + "vsTex.fx",GL_VERTEX_SHADER);
        shdSource[1][1] = new shaderObject("TexFrag",mainFilePath + "shaders" + File.separator + "fsTex.fx",GL_FRAGMENT_SHADER);
        
        
      
        shdSource[0][2] = new shaderObject("axisvert",mainFilePath + "shaders" + File.separator + "axisvs.fx",GL_VERTEX_SHADER);
        shdSource[1][2] = new shaderObject("axisfrag",mainFilePath + "shaders" + File.separator + "axisfs.fx",GL_FRAGMENT_SHADER);
        shdSource[1][3] = new shaderObject("yellowfrag",mainFilePath + "shaders" + File.separator + "fs_yellow.fx",GL_FRAGMENT_SHADER);
             
        
        
        shdSource[0][3] = new shaderObject("surVert",mainFilePath + "shaders" + File.separator + "vsSurface.fx",GL_VERTEX_SHADER);
        shdSource[1][4] = new shaderObject("surFrag",mainFilePath + "shaders" + File.separator + "fsSurface.fx",GL_FRAGMENT_SHADER);
        shdSource[2][0] = new shaderObject("surTcs",mainFilePath + "shaders" + File.separator + "tcsSurface.fx",GL_TESS_CONTROL_SHADER);
        shdSource[3][0] = new shaderObject("surTes",mainFilePath + "shaders" + File.separator + "tesSurface.fx",GL_TESS_EVALUATION_SHADER);
       
        shdSource[4][0] = new shaderObject("geodiscard",mainFilePath + "shaders" + File.separator + "geo_discard.fx",GL_GEOMETRY_SHADER);
    
    */
    private int[] createShaderPrograms()
    {
        int ret[] = new int[6];
        //my clever way around seperable programs while still FEELING like seperable programs
        
        ret[0] = shaderObject.generateProgram(new shaderObject[]{shadSrc[0][0],shadSrc[1][0]}); //program 1
        ret[1] = shaderObject.generateProgram(new shaderObject[]{shadSrc[0][1],shadSrc[1][1],shadSrc[4][0]}); //program 1
        ret[2] = shaderObject.generateProgram(new shaderObject[]{shadSrc[0][2],shadSrc[1][2]}); //program 1
        ret[3] = shaderObject.generateProgram(new shaderObject[]{shadSrc[0][3],shadSrc[1][4],shadSrc[2][0],shadSrc[3][0]}); //program 1
        ret[4] = shaderObject.generateProgram(new shaderObject[]{shadSrc[0][4],shadSrc[4][0],shadSrc[1][5]}); //program 
        ret[5] = shaderObject.generateProgram(new shaderObject[]{shadSrc[0][0],shadSrc[1][3]}); //program 
        return ret;
    }

    private void initLightindicator(GLAutoDrawable drawable)
    {
        GL4 gl = (GL4) drawable.getGL();
        gl.glGenBuffers(1, lightIndBuffer, 0);
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, lightIndBuffer[0]);
        FloatBuffer buffer = FloatBuffer.wrap(new float[]
        {
            0.0f, 0.0f, 0.0f
        });
        gl.glBufferData(GL.GL_ARRAY_BUFFER, buffer.limit() * 4, buffer, GL.GL_STATIC_DRAW);
        
        System.out.println("HEHEHEHSHSSHSH");

    }

   
    
    
    
    


    public void dispose(GLAutoDrawable drawable)
    {
    }

    public void display(GLAutoDrawable drawable)
    {
        GL4 GL = (GL4) drawable.getGL();
        //System.out.println("HERE!!!");
        //GL.glUseProgram(rndPrograms[0]);
        GL.glClearBufferfv(GL_COLOR, 0, bgColor);
        GL.glClear(GL_DEPTH_BUFFER_BIT);
        
        //drawCubeMap(drawable);
        
        drawObjects(drawable);
        
        //drawLightIndicator(drawable);
        
        //drawBezierSurface(drawable);    //
        
        //if (showAxis)
        //{
        //    drawAxis(drawable);
        //}
        
        

        //other draw functions
    }

    public void drawLightIndicator(GLAutoDrawable drawable)
    {
        GL4 gl = (GL4) drawable.getGL();
        gl.glUseProgram(rndPrograms[5]);

        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, lightIndBuffer[0]);
        gl.glVertexAttribPointer(vLoc[0], 3, GL.GL_FLOAT, false, 0, 0);
        gl.glEnableVertexAttribArray(0);

        Point3D pos = light.getPosition();
        Matrix3D tmp = camera[0].getViewMat(), trans = new Matrix3D();
        
        trans.translate(pos.getX(), pos.getY(), pos.getZ());
        tmp.concatenate(trans);
        gl.glUniformMatrix4fv(camera[0].getMv_loc(), 1, false, tmp.getFloatValues(), 0);
        gl.glUniformMatrix4fv(camera[0].getProj_loc(), 1, false, camera[0].getPerspectiveMat().getFloatValues(), 0);
        gl.glDrawArrays(GL_POINTS, 0, 1);

    }

    private void drawCubeMap(GLAutoDrawable drawable)
    {
        GL4 gl = (GL4) drawable.getGL();
        Matrix3D m = new Matrix3D(), mv;

        m.setToIdentity();
        
        Point3D camPos = camera[0].getMCamMat();
        
        mv = camera[0].getViewMat();
        
        //System.out.println(f.getX());
        //System.out.println(f.getY());
        //System.out.println(f.getZ());
        m.translate(camPos.getX() ,camPos.getY(),camPos.getZ());
        mv.concatenate(m);

        gl.glUseProgram(rndPrograms[1]);

        gl.glUniformMatrix4fv(camera[0].getTex_mv_loc(), 1, false, mv.getFloatValues(), 0);
        gl.glUniformMatrix4fv(camera[0].getTex_proj_loc(), 1, false, camera[0].getPerspectiveMat().getFloatValues(), 0);

        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, cubeMapBuffer[0]);
        gl.glVertexAttribPointer(vLoc[1], 3, GL.GL_FLOAT, false, 0, 0);
        gl.glEnableVertexAttribArray(0);

        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, cubeMapBuffer[1]);
        gl.glVertexAttribPointer(texLoc[1], 2, GL.GL_FLOAT, false, 0, 0);
        gl.glEnableVertexAttribArray(1);
        gl.glActiveTexture(gl.GL_TEXTURE0);
        gl.glBindTexture(gl.GL_TEXTURE_2D, cubeTexId);

        gl.glEnable(GL_CULL_FACE);
        gl.glFrontFace(GL_CC);
        gl.glDisable(GL_DEPTH_TEST); // put it behind everything else   
        gl.glDrawArrays(GL_TRIANGLES, 0, 36);
        gl.glEnable(GL_DEPTH_TEST);
        gl.glFrontFace(GL_CW);
    }

    public void drawAxis(GLAutoDrawable drawable)
    {
        GL4 gl = (GL4) drawable.getGL();
        gl.glUseProgram(rndPrograms[2]);

        //gl.glUseProgram(rndPrograms[2]);
        gl.glUniformMatrix4fv(camera[0].getAxismv_loc(), 1, false, camera[0].getViewMat().getFloatValues(), 0);
        gl.glUniformMatrix4fv(camera[0].getAxisproj_loc(), 1, false, camera[0].getPerspectiveMat().getFloatValues(), 0);
        gl.glDrawArrays(GL_LINES, 0, 6);

    }
    /*
    uniShaderLocs[2][0][0] = gl.glGetUniformLocation(rndPrograms[2][0], "uOuter02");
        uniShaderLocs[2][0][1] = gl.glGetUniformLocation(rndPrograms[2][0], "uOuter13");
        uniShaderLocs[2][0][2] = gl.glGetUniformLocation(rndPrograms[2][0], "uInner0");
        uniShaderLocs[2][0][3] = gl.glGetUniformLocation(rndPrograms[2][0], "uInner1");
        
        uniShaderLocs[3][0][0] = gl.glGetUniformLocation(rndPrograms[3][0], "mvp");
    */
    
    private void drawBezierSurface(GLAutoDrawable drawable)
    {
        GL4 gl = (GL4) drawable.getGL();
        
        Matrix3D mvp = camera[0].getViewMat();
        
        mvp.concatenate(camera[0].getPerspectiveMat());
       
       gl.glUseProgram(rndPrograms[3]);
  
       
       // gl.glProgramUniform1f(progs[1][1], uni[1][1][7],(float) ((light) ? this.getMaterial().getShininess() : 0.0));
        
        //gl.glProgramUniformMatrix4fv(rndPrograms[3],uniShaderLocs[3][0],0, false,(new Matrix3D()).getFloatValues(),0);
        System.out.println("ffefefefefeffe:" + uniShaderLocs[3][0]);     
        System.out.println("ffefefefefeffe:" + uniShaderLocs[3][1]);
        System.out.println("ffefefefefeffe:" + uniShaderLocs[3][2]);
        System.out.println("ffefefefefeffe:" + uniShaderLocs[3][3]);
       
        System.out.println("ffefefefefeffe:" + uniShaderLocs[3][0]);
        
       
        gl.glProgramUniform1f(rndPrograms[3], uniShaderLocs[3][1],3);
        gl.glProgramUniform1f(rndPrograms[3], uniShaderLocs[3][2],3);
        gl.glProgramUniform1f(rndPrograms[3], uniShaderLocs[3][3],3);
        gl.glProgramUniform1f(rndPrograms[3], uniShaderLocs[3][4],3);
        
        
        
        
        gl.glProgramUniformMatrix4fv(rndPrograms[3],uniShaderLocs[3][0],0, false,mvp.getFloatValues(),0);
        
       gl.glPatchParameteri(GL_PATCH_VERTICES, 16);
       gl.glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);  // FILL or LINE 
       gl.glDrawArrays(GL_PATCHES, 0, 16);
    }
    
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height)
    {
    }
}
