/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package actv.ccs.model.graphics.objects;

import actv.ccs.model.graphics.MainGraphics;
import actv.ccs.model.graphics.ImportedModel;
import com.jogamp.opengl.util.texture.Texture;
import graphicslib3D.Material;
import graphicslib3D.Matrix3D;
import graphicslib3D.Point3D;
import graphicslib3D.Vector3D;
import graphicslib3D.Vertex3D;
import graphicslib3D.light.AmbientLight;
import graphicslib3D.light.PositionalLight;
import java.io.IOException;
import java.nio.FloatBuffer;
import javax.media.opengl.GL;
import static javax.media.opengl.GL.GL_TRIANGLES;
import static javax.media.opengl.GL2ES2.GL_FRAGMENT_SHADER_BIT;
import static javax.media.opengl.GL2ES2.GL_VERTEX_SHADER_BIT;
import javax.media.opengl.GL4;
import static javax.media.opengl.GL4.GL_GEOMETRY_SHADER_BIT;
import static javax.media.opengl.GL4.GL_PATCHES;
import javax.media.opengl.GLAutoDrawable;

/**
 *
 * @author Victor
 */
public class GeoShape extends AbstractObject
{

    private float[] verts, texCoords, normals;
    private String textFilePath;
    private PositionalLight positionalLight;
    private float [] ambientLight;
    private Matrix3D mv, rotateAxis, transAxis;
    private int bufferID, numOfVerts, texID, texBufferID, texLoc, normalBufferID,vLoc, norLoc, cubeMapTexId, attribID;
    private boolean reneder, light;
    //private boolean hasHericalObject;
    private GeoShape hericalObject;
        
    public GeoShape(String string, Matrix3D md, Matrix3D md1, Matrix3D md2, Material mtrl, Texture txtr, int i, float[] verts, float[] texCoords)
    {
        super(string, md, md1, md2, mtrl, txtr, i);
        this.verts = verts;
        this.texCoords = texCoords;
        numOfVerts = verts.length / 3;
        //hasHericalObject = false;
        rotateAxis = new Matrix3D();
        transAxis = new Matrix3D();
        rotateAxis.setToIdentity();
        transAxis.setToIdentity();
        hericalObject = null;
        reneder = true;
        texID = normalBufferID = texBufferID = bufferID = -1;
        light = false;
    }

    public GeoShape(String string, float[] verts)
    {
        super(string);
        this.verts = verts;
        numOfVerts = verts.length / 3;
        //hasHericalObject = false;
        rotateAxis = new Matrix3D();
        transAxis = new Matrix3D();
        rotateAxis.setToIdentity();
        transAxis.setToIdentity();
        hericalObject = null;
        reneder = true;
        texID = normalBufferID = texBufferID = bufferID = -1;
        light = false;
    }

    public GeoShape(String string, float[] verts, float[] texCoords, String texFilePath, float x, float y, float z)
    {
        super(string);
        translate(x, y, z);
        this.verts = verts;
        numOfVerts = verts.length / 3;
        //hasHericalObject = false;
        rotateAxis = new Matrix3D();
        transAxis = new Matrix3D();
        rotateAxis.setToIdentity();
        transAxis.setToIdentity();
        hericalObject = null;
        reneder = true;
        this.texCoords = texCoords;
        textFilePath = texFilePath;
        texID = normalBufferID = texBufferID = bufferID = -1;
        light = false;
    }
    
    public GeoShape(String string,String modelFilePath,String texFilePath, float x, float y, float z) throws IOException
    {
        super(string);
        translate(x, y, z);
        //hasHericalObject = false;
        rotateAxis = new Matrix3D();
        transAxis = new Matrix3D();
        rotateAxis.setToIdentity();
        transAxis.setToIdentity();
        hericalObject = null;
        reneder = true; //depricated
        this.texCoords = texCoords;
        textFilePath = texFilePath;
        texID = normalBufferID = texBufferID = bufferID = -1;
  //      loadModelFromFile(modelFilePath);
        light = false;
    }
    
    
    private void loadModelFromFile(String filepath) throws IOException
    {
        ImportedModel modelData = new ImportedModel(filepath);
        int numofIndicies = modelData.getNumVertices(), indicies[] = modelData.getIndices();
        Vertex3D vertPoint[] = modelData.getVertices();
        
        numOfVerts = modelData.getNumVertices();
        
        verts = new float[numofIndicies * 3];
        texCoords = new float[numofIndicies * 2];
        normals = new float[numofIndicies * 3];
        
        for (int i = 0; i < numofIndicies; i++)
        {
            verts[i * 3] = (float) (vertPoint[indicies[i]]).getX();
            verts[i * 3 + 1] = (float) (vertPoint[indicies[i]]).getY();
            verts[i * 3 + 2] = (float) (vertPoint[indicies[i]]).getZ();
            texCoords[i * 2] = (float) (vertPoint[indicies[i]]).getS();
            texCoords[i * 2 + 1] = (float) (vertPoint[indicies[i]]).getT();
            normals[i * 3] = (float) (vertPoint[indicies[i]]).getNormalX();
            normals[i * 3 + 1] = (float) (vertPoint[indicies[i]]).getNormalY();
            normals[i * 3 + 2] = (float) (vertPoint[indicies[i]]).getNormalZ();
        }
    }

    public int getAttribID()
    {
        return attribID;
    }

    public void setAttribID(int attribID)
    {
        this.attribID = attribID;
    }
    public void setPositionalLight(PositionalLight positionalLight)
    {
        this.positionalLight = positionalLight;
    }

    public boolean isLight()
    {
        return light;
    }
    
    public void setAmbientLight(float []ambientLight)
    {
        this.ambientLight = ambientLight;
    }
    
    public void setMaterial(Material matr)
    {
        super.setMaterial(matr);
    }

    public void setLight(boolean light)
    {
        this.light = light;
    }
    
    
    public int getNumNeededBuffers()
    {
        int ret = 1;
        
        if(texCoords != null)
            ret++;
        
        if(normals != null)
            ret++;
        
        return ret;
    }
    
    public int loadIntoBuffer(int preOffSet,int[] buffers,GLAutoDrawable drawable)
    {
        GL4 gl = (GL4) drawable.getGL();
        FloatBuffer buffer = null;
        int postOffSet = 1;
        
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, buffers[preOffSet]);
 
        buffer = FloatBuffer.wrap(verts);
        System.out.println("where!!!!!");
        gl.glBufferData(GL.GL_ARRAY_BUFFER, buffer.limit() * 4,buffer, GL.GL_STATIC_DRAW);
        bufferID = buffers[preOffSet];
        //  enable the associated vertex position attribute in the shader.

        if (texCoords != null && textFilePath != null)
        {
            
            gl.glBindBuffer(GL.GL_ARRAY_BUFFER, buffers[preOffSet + postOffSet]);
             
            buffer = FloatBuffer.wrap(texCoords);
            gl.glBufferData(GL.GL_ARRAY_BUFFER, buffer.limit() * 4, buffer, GL.GL_STATIC_DRAW);
            texBufferID = buffers[preOffSet + postOffSet];
            postOffSet++;
            texID = MainGraphics.loadTexture(drawable,textFilePath);
        }
        
        if (normals != null)
        {
            System.out.println("NORMAL!!!!");
            gl.glBindBuffer(GL.GL_ARRAY_BUFFER, buffers[preOffSet + postOffSet]);
            
            buffer = FloatBuffer.wrap(normals);
            gl.glBufferData(GL.GL_ARRAY_BUFFER, buffer.limit() * 4, buffer, GL.GL_STATIC_DRAW);
            normalBufferID = buffers[preOffSet + postOffSet];
            postOffSet++;
        }

        return postOffSet;
    }
    
    @Override
    public void draw(GLAutoDrawable drawable)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void draw2(GLAutoDrawable drawable, Camera camera, Matrix3D globalTransform, int progs[], int pipes[],int uniforms[][])
    {
        GL4 gl = (GL4) drawable.getGL();

        if (globalTransform != null)
            mv = globalTransform;
         else
            mv = camera.getViewMat();
        
        mv.concatenate(this.getTranslation());
        mv.concatenate(rotateAxis);
        mv.concatenate(transAxis);
        mv.concatenate(this.getRotation());
        
        if (hericalObject != null)
        {
            hericalObject.draw2(drawable, camera, (Matrix3D) mv.clone(), progs,pipes,uniforms);
        }
          
   
        //setupLights(drawable,camera.getViewMat(),progs, uniforms);
        
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, bufferID);
        gl.glVertexAttribPointer(vLoc, 3, GL.GL_FLOAT, false, 0, 0);
        gl.glEnableVertexAttribArray(0);
       

        if (texID == -1 || texLoc == -1)
        {
            gl.glUseProgram(progs[0]);
           
            gl.glUniformMatrix4fv(camera.getMv_loc(), 1, false, mv.getFloatValues(), 0);
            gl.glUniformMatrix4fv(camera.getProj_loc(), 1, false, camera.getPerspectiveMat().getFloatValues(), 0);
            //System.out.println("herioeiieif");
            
            
        } else
        {
            //gl.glUseProgramStages(pipes[0], GL_GEOMETRY_SHADER_BIT,progs[4][0]);  //GEOSHADER NOT WORKING!!!!
            
            gl.glUseProgram(progs[1]);
            
            //gl.glUniformMatrix4fv(camera.getTex_mv_loc(), 1, false, mv.getFloatValues(), 0);
            //gl.glUniformMatrix4fv(camera.getTex_proj_loc(), 1, false, camera.getPerspectiveMat().getFloatValues(), 0);
            
            gl.glUniformMatrix4fv(camera.getTex_mv_locL(), 1, false, mv.getFloatValues(), 0);
            gl.glUniformMatrix4fv(camera.getTex_proj_locL(), 1, false, camera.getPerspectiveMat().getFloatValues(), 0);
            
            
            //gl.glActiveShaderProgram(pipes[0],progs[4][0] );
            
            gl.glBindBuffer(GL.GL_ARRAY_BUFFER,texBufferID);
            gl.glVertexAttribPointer(texLoc, 2, GL.GL_FLOAT, false, 0, 0);
            gl.glEnableVertexAttribArray(1);
            gl.glEnableVertexAttribArray(0);
            gl.glActiveTexture(gl.GL_TEXTURE0);
            gl.glBindTexture(gl.GL_TEXTURE_2D, texID);
            
            
        }
        
        if(normalBufferID != -1 && norLoc != -1)
        {
            /*gl.glBindBuffer(GL.GL_ARRAY_BUFFER,normalBufferID); 
            gl.glVertexAttribPointer(norLoc, 4, GL.GL_FLOAT, false, 0, 0);
            gl.glEnableVertexAttribArray(2);
            
            */
            //gl.glEnableVertexAttribArray(1);
            //gl.glEnableVertexAttribArray(0);
        }
        
        
        //System.out.println("CRSHHHHH!!!");
        gl.glDrawArrays(GL_TRIANGLES,0,numOfVerts);
        transAxis.setToIdentity();
    }
    
    /*
       uniShaderLocs[0][1][0] = GL.glGetUniformLocation(rndPrograms[0][1], "light.ambient");
       uniShaderLocs[0][1][1] = GL.glGetUniformLocation(rndPrograms[0][1], "light.diffuse");
       uniShaderLocs[0][1][2] = GL.glGetUniformLocation(rndPrograms[0][1], "light.specular");
       uniShaderLocs[0][1][3] = GL.glGetUniformLocation(rndPrograms[0][1], "light.position");
        
       uniShaderLocs[0][1][4] = GL.glGetUniformLocation(rndPrograms[0][1], "material.ambient");
       uniShaderLocs[0][1][5] = GL.glGetUniformLocation(rndPrograms[0][1], "material.diffuse");
       uniShaderLocs[0][1][6] = GL.glGetUniformLocation(rndPrograms[0][1], "material.specular");
       uniShaderLocs[0][1][7] = GL.glGetUniformLocation(rndPrograms[0][1], "material.position");
       
       uniShaderLocs[0][1][8] = GL.glGetUniformLocation(rndPrograms[0][1], "globalAmbient");
       uniShaderLocs[0][1][9] = GL.glGetUniformLocation(rndPrograms[0][1], "normalMat");
       
       uniShaderLocs[1][1] = new int[10]; 
       
       //frag
       uniShaderLocs[1][1][0] = GL.glGetUniformLocation(rndPrograms[1][1], "light.ambient");
       uniShaderLocs[1][1][1] = GL.glGetUniformLocation(rndPrograms[1][1], "light.diffuse");
       uniShaderLocs[1][1][2] = GL.glGetUniformLocation(rndPrograms[1][1], "light.specular");
       uniShaderLocs[1][1][3] = GL.glGetUniformLocation(rndPrograms[1][1], "light.position");
        
       uniShaderLocs[1][1][4] = GL.glGetUniformLocation(rndPrograms[1][1], "material.ambient");
       uniShaderLocs[1][1][5] = GL.glGetUniformLocation(rndPrograms[1][1], "material.diffuse");
       uniShaderLocs[1][1][6] = GL.glGetUniformLocation(rndPrograms[1][1], "material.specular");
       uniShaderLocs[1][1][7] = GL.glGetUniformLocation(rndPrograms[1][1], "material.position");
       
       uniShaderLocs[1][1][8] = GL.glGetUniformLocation(rndPrograms[1][1], "globalAmbient");
       uniShaderLocs[1][1][9] = GL.glGetUniformLocation(rndPrograms[1][1], "normalMat");*/
       
    
    private void setupLights(GLAutoDrawable drawable,Matrix3D view,int progs[],int uni[][])
    {
        GL4 gl = (GL4) drawable.getGL();
        
        Point3D lpos = positionalLight.getPosition().mult(view);
        float [] lposbuf = new float[] {(float)lpos.getX(),(float)lpos.getY(),(float)lpos.getZ()};
       
        System.out.println("xpos:" + lposbuf[0]);
        System.out.println("ypos:" + lposbuf[1]);
        System.out.println("zpos:" + lposbuf[2]);
        
        /*
       uni[0][1][0] = gl.glGetUniformLocation(progs[0][1], "light.ambient");
       uni[0][1][1] = gl.glGetUniformLocation(progs[0][1], "light.diffuse");
       uni[0][1][2] = gl.glGetUniformLocation(progs[0][1], "light.specular");
       uni[0][1][3] = gl.glGetUniformLocation(progs[0][1], "light.position");
        
       uni[0][1][4] = gl.glGetUniformLocation(progs[0][1], "material.ambient");
       uni[0][1][5] = gl.glGetUniformLocation(progs[0][1], "material.diffuse");
       uni[0][1][6] = gl.glGetUniformLocation(progs[0][1], "material.specular");
       uni[0][1][7] = gl.glGetUniformLocation(progs[0][1], "material.position");
       
       uni[0][1][8] = gl.glGetUniformLocation(progs[0][1], "globalAmbient");
       uni[0][1][9] = gl.glGetUniformLocation(progs[0][1], "normalMat");
      
       
       //frag
       uni[1][1][0] = gl.glGetUniformLocation(progs[1][1], "light.ambient");
       uni[1][1][1] = gl.glGetUniformLocation(progs[1][1], "light.diffuse");
       uni[1][1][2] = gl.glGetUniformLocation(progs[1][1], "light.specular");
       uni[1][1][3] = gl.glGetUniformLocation(progs[1][1], "light.position");
        
       uni[1][1][4] = gl.glGetUniformLocation(progs[1][1], "material.ambient");
       uni[1][1][5] = gl.glGetUniformLocation(progs[1][1], "material.diffuse");
       uni[1][1][6] = gl.glGetUniformLocation(progs[1][1], "material.specular");
       uni[1][1][7] = gl.glGetUniformLocation(progs[1][1], "material.position");
       
       uni[1][1][8] = gl.glGetUniformLocation(progs[1][1], "globalAmbient");
       uni[1][1][9] = gl.glGetUniformLocation(progs[1][1], "normalMat");
        */
            
       
        //System.out.println("uniform:" + uni[0][1][0]);
        gl.glProgramUniform4fv(progs[4],uni[4][0],1, positionalLight.getAmbient(), 0);  
        gl.glProgramUniform4fv(progs[4],uni[4][1],1,positionalLight.getDiffuse(), 0);  
        gl.glProgramUniform4fv(progs[4],uni[4][2],1, positionalLight.getSpecular(), 0); 
        gl.glProgramUniform3fv(progs[4],uni[4][3],1,lposbuf, 0);
        
        gl.glProgramUniform4fv(progs[4],uni[4][4],1, this.getMaterial().getAmbient(), 0);  
        gl.glProgramUniform4fv(progs[4],uni[4][5],1,this.getMaterial().getDiffuse(), 0);  
        gl.glProgramUniform4fv(progs[4],uni[4][6],1,this.getMaterial().getSpecular(), 0); 
        gl.glProgramUniform1f(progs[4], uni[4][7],this.getMaterial().getShininess());
        
        gl.glProgramUniform4fv(progs[4],uni[4][8],1,ambientLight, 0);
        gl.glProgramUniformMatrix4fv(progs[4],uni[4][9],0, false,((mv.inverse()).transpose()).getFloatValues(),0);
    
        /*
        gl.glProgramUniform4fv(progs[4],uni[1][1][0],1, (float[]) ((light) ? positionalLight.getAmbient() : 0.0), 0);  
        gl.glProgramUniform4fv(progs[4],uni[1][1][1],1,(float[]) ((light) ? positionalLight.getDiffuse() : 0.0), 0);  
        gl.glProgramUniform4fv(progs[4],uni[1][1][2],1,(float[]) ((light) ? positionalLight.getSpecular() : 0.0), 0); 
        gl.glProgramUniform3fv(progs[4],uni[1][1][3],1,lposbuf, 0);
        
        gl.glProgramUniform4fv(progs[4],uni[1][1][4],1,(float[]) ((light) ? this.getMaterial().getAmbient() : 0.0), 0);  
        gl.glProgramUniform4fv(progs[4],uni[1][1][5],1,(float[]) ((light) ? this.getMaterial().getDiffuse() : 0.0), 0);  
        gl.glProgramUniform4fv(progs[4],uni[1][1][6],1,(float[]) ((light) ? this.getMaterial().getSpecular() : 0.0), 0); 
        gl.glProgramUniform1f(progs[4], uni[1][1][7],(float) ((light) ? this.getMaterial().getShininess() : 0.0));
        
        gl.glProgramUniform4fv(progs[4],uni[1][1][8],1,ambientLight, 0);
        //gl.glUniformMatrix4fv(progs[0][1],uni[0][1][9], false, mv.getFloatValues(), 0);
        gl.glProgramUniformMatrix4fv(progs[4],uni[1][1][9],0, false,((mv.inverse()).transpose()).getFloatValues(),0);*/
    }
    
    
    public void setvLoc(int vLoc)
    {
        this.vLoc = vLoc;
    }

    public void setNorLoc(int norLoc)
    {
        this.norLoc = norLoc;
    }
    
    public float[] getVerts()
    {
        return verts;
    }

    public String getTextFilePath()
    {
        return textFilePath;
    }

    public void setHericalObject(GeoShape hericalObject)
    {
        this.hericalObject = hericalObject;
    }

    public void setRotationAxis(float dtheata, Vector3D vec, float dx, float dy, float dz)
    {
        rotateAxis.rotate(dtheata, vec);
        transAxis.translate(dx, dy, dz);
    }

    public void setRotationAxis(float dtheatax, float dtheatay, float dtheataz, float dx, float dy, float dz)
    {
        rotateAxis.rotate(dtheatax, dtheatay, dtheataz);
        transAxis.translate(dx, dy, dz);
    }

    public void setVerts(float[] verts)
    {
        this.verts = verts;
        numOfVerts = verts.length / 3;
    }

    public float[] getTexCoords()
    {
        return texCoords;
    }

    public void setTexID(int texID)
    {
        this.texID = texID;
    }
    
    public void setBuffTexID(int texBuffID)
    {
        texBufferID = texBuffID;
    }
    
    
    public void setTexLoc(int texLoc)
    {
        this.texLoc = texLoc;
    }
    
    public int getNumOfVerts()
    {
        return numOfVerts;
    }

    public int getBufferID()
    {
        return bufferID;
    }

    public void setBufferID(int bufferID)
    {
        this.bufferID = bufferID;
    }

}
