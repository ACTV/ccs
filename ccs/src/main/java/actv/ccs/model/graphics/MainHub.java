/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package actv.ccs.model.graphics;

import actv.ccs.model.graphics.objects.*;
import graphicslib3D.Material;
import graphicslib3D.Point3D;
import graphicslib3D.light.AmbientLight;
import graphicslib3D.light.PositionalLight;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import javax.media.opengl.awt.GLCanvas;
import javax.swing.JFrame;
import java.nio.*;
import static javax.media.opengl.GL2ES2.GL_FRAGMENT_SHADER;
import static javax.media.opengl.GL2ES2.GL_VERTEX_SHADER;
import static javax.media.opengl.GL3.GL_GEOMETRY_SHADER;
import javax.media.opengl.GL4;
import static javax.media.opengl.GL4.GL_TESS_CONTROL_SHADER;
import static javax.media.opengl.GL4.GL_TESS_EVALUATION_SHADER;

/**
 * Main class thats is the back bone of it all
 * @author Victor
 * 
 * 
 * The purpose of this class is to handle all the order and time senitive inits
 *and resource sharing. To use the graghics engine, you must init this class first
 * its constructor.
 * 
 * 

 * 
 */
public class MainHub
{
   // private MainFrame mF;
    private MainFunction mf;
    private MainGraphics mg;
    private GLCanvas GLc; 
    private String mainFilePath;
    private GeoShape shapes[], attachedShapes[];
    
    //Setup, create and init all related classes 
    
   // public MainHub(String mainFilePath, Iterator ) throws IOException 
    public MainHub(String mainFilePath, Iterator itr ) throws IOException 
    {
    	GLc = new GLCanvas();///////UPDATE THIS!!!!!!!!!!!!!!!!!
        attachedShapes = new GeoShape[1];
        
        //when out of IDE
        //mg = new MainGraghics(GLc,"." + File.separator + "a1" + File.separator + "shaders" + File.separator + "vs.shader" ,"." + File.separator + "a1" + File.separator + "shaders" + File.separator + "fs.shader");
        this.mainFilePath = mainFilePath;
        //3D OBJECT DEFINTION 
       //     -0.5f,  -0.25f, 0.5f, 
           // 0f, 0.25f, 0.5f, 
           // 0f,  -0.25f, 0.5f
      
        shaderObject shdSource[][] = new shaderObject[5][];
        
        shdSource[0] = new shaderObject[5]; //vertex
        shdSource[1] = new shaderObject[6]; //frag
        shdSource[2] = new shaderObject[1]; //TC
        shdSource[3] = new shaderObject[1]; //TE
        shdSource[4] = new shaderObject[1]; //Geo
        
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
        
         shdSource[0][4] = new shaderObject("lightVert",mainFilePath + "shaders" + File.separator + "vsTexL.fx",GL_VERTEX_SHADER);
        shdSource[1][5] = new shaderObject("lightFrag",mainFilePath + "shaders" + File.separator + "fsTexL.fx",GL_FRAGMENT_SHADER);
        
        
        float [] amblight = new float [] {0.1f, 0.1f, 0.1f,1.0f};
        PositionalLight pl = new PositionalLight();
        Point3D lightPostion = new Point3D(0,3,3);
        pl.setSpecular(new float[]{1.0f,0f,0f});
        pl.setDiffuse(amblight);
        shapes[0].setAmbientLight(amblight);
        shapes[0].setLight(true);
        pl.setPosition(lightPostion);
        shapes[0].setPositionalLight(pl);
        shapes[0].setMaterial(Material.SILVER);
        
        shapes[0].setAttribID(4);
        
        //attachedShapes[0].setAmbientLight(amblight);
        
        //pl.setPosition(lightPostion);
       // attachedShapes[0].setPositionalLight(pl);
       
        //attachedShapes[0].setMaterial(Material.SILVER);
       
        mg = new MainGraphics(GLc,shdSource, shapes, attachedShapes,pl,mainFilePath + "cubemap" + File.separator + "skybox2.png");
        mf = new MainFunction(mg);
        
        //INIT varibles or/and classes HERE before they are displayed in the window//////
        mg.setTriColor(Color.blue);
        mg.setBGColor(Color.blue);
        ///////////////////END OF INIT////////////////////////////////////
        
    //    mF = new MainFrame(mf,GLc);
    }
    
}
