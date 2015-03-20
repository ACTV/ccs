/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package actv.ccs.model.graphics.objects;

import com.jogamp.opengl.util.texture.Texture;
import graphicslib3D.Material;
import graphicslib3D.Matrix3D;
import graphicslib3D.Point3D;
import graphicslib3D.Vector3D;
import javax.media.opengl.GLAutoDrawable;

/**
 *
 * @author Victor
 */
public class Camera extends AbstractObject
{
   
    private float fovy, near, far, aspect;
    private int mv_loc, proj_loc, tex_mv_loc, tex_proj_loc, axismv_loc, axisproj_loc,tex_mv_locL, tex_proj_locL ;
    private Matrix3D projMatrix;
   
    public Camera(String string, Matrix3D md, Matrix3D md1, Matrix3D md2, float fovy, float near, float far, float aspect)
    {
        super(string, md, md1, md2,null,null,0);
        this.fovy = fovy;
        this.near = near;
        this.far = far;
        this.aspect = aspect;
        initCamAxis();
        generatePerspectiveMat();
    }
    
    public Camera(String string,float x,float y,float z, float fovy, float near, float far, float aspect)
    {
        super(string);
        this.fovy = fovy;
        this.near = near;
        this.far = far;
        this.aspect = aspect;
        this.translate(x, y, z);
        initCamAxis();
        generatePerspectiveMat();
    }
    
    private void initCamAxis()
    {
        Matrix3D tmp = new Matrix3D();
        tmp.setToIdentity();
        tmp.setRow(0,new Vector3D(1,0,0,0));
        tmp.setRow(1,new Vector3D(0,1,0,0));
        tmp.setRow(2,new Vector3D(0,0,-1,0));
        this.setRotation(tmp);
    }

    public int getTex_mv_locL()
    {
        return tex_mv_locL;
    }

    public void setTex_mv_locL(int tex_mv_locL)
    {
        this.tex_mv_locL = tex_mv_locL;
    }

    public int getTex_proj_locL()
    {
        return tex_proj_locL;
    }

    public void setTex_proj_locL(int tex_proj_locL)
    {
        this.tex_proj_locL = tex_proj_locL;
    }
    
    
    
    public void forward(float dx)
    {
        Vector3D vec = this.getRotation().getCol(2);
        translate(vec.getX() * dx,vec.getY() * dx,vec.getZ() * dx);
    }
    
    public void straef(float dx)
    {
        Vector3D vec = this.getRotation().getCol(0);
        translate(vec.getX() * dx,vec.getY() * dx,vec.getZ() * dx);
    }
    
    public void vertmove(float dx)
    {
        Vector3D vec = this.getRotation().getCol(1);
        translate(vec.getX() * dx,vec.getY() * dx,vec.getZ() * dx);
    }
    
    public void pan(float theta)
    {
        this.rotate(theta,this.getRotation().getRow(1));
    }
    
    public void pitch(float theta)
    {
        this.rotate(theta,this.getRotation().getRow(0));
    }
    
    @Override
    public void draw(GLAutoDrawable glad)
    {
    }
    
    public void setCamPos(Point3D p3)
    {
        translate(0.003f,0,0);
    }
    
    public Point3D getMCamMat()
    {
        Vector3D f = getTranslation().getCol(3);
        Point3D ret = new Point3D(f);
        
        ret.setX(ret.getX() * -1);
        ret.setY(ret.getY() * -1);
        ret.setZ(ret.getZ() * -1);
        
        return ret;
    }

    public int getTex_mv_loc()
    {
        return tex_mv_loc;
    }

    public void setTex_mv_loc(int tex_mv_loc)
    {
        this.tex_mv_loc = tex_mv_loc;
    }

    public int getTex_proj_loc()
    {
        return tex_proj_loc;
    }

    public void setTex_proj_loc(int tex_proj_loc)
    {
        this.tex_proj_loc = tex_proj_loc;
    }
    
    public int getMv_loc()
    {
        return mv_loc;
    }

    public void setMv_loc(int mv_loc)
    {
        this.mv_loc = mv_loc;
    }

    public int getProj_loc()
    {
        return proj_loc;
    }

    public void setProj_loc(int proj_loc)
    {
        this.proj_loc = proj_loc;
    }
    
    private static Matrix3D tmp;
    
    public Matrix3D getViewMat()
    { 
        tmp = (Matrix3D) this.getRotation().clone();
        tmp.concatenate(this.getTranslation());
        return tmp;
    }

    public int getAxismv_loc()
    {
        return axismv_loc;
    }

    public void setAxismv_loc(int axismv_loc)
    {
        this.axismv_loc = axismv_loc;
    }

    public int getAxisproj_loc()
    {
        return axisproj_loc;
    }

    public void setAxisproj_loc(int axisproj_loc)
    {
        this.axisproj_loc = axisproj_loc;
    }
    
    
    public void setFovy(float fovy)
    {
        this.fovy = fovy;
    }

    public void setNear(float near)
    {
        this.near = near;
    }

    public void setFar(float far)
    {
        this.far = far;
    }

    public void setAspect(float aspect)
    {
        this.aspect = aspect;
    }
   
    final public void generatePerspectiveMat()
    {
        float q = 1f / ((float) Math.tan(Math.toRadians(0.5f * fovy)));
        float A = q / aspect;
        float B = (near + far) / (near - far);
        float C = (2.0f * near * far) / (near - far);
        Matrix3D r = new Matrix3D();
        r.setElementAt(0, 0, A);
        r.setElementAt(0, 1, 0.0f);
        r.setElementAt(0, 2, 0.0f);
        r.setElementAt(0, 3, 0.0f);
        r.setElementAt(1, 0, 0.0f);
        r.setElementAt(1, 1, q);
        r.setElementAt(1, 2, 0.0f);
        r.setElementAt(1, 3, 0.0f);
        r.setElementAt(2, 0, 0.0f);
        r.setElementAt(2, 1, 0.0f);
        r.setElementAt(2, 2, B);
        r.setElementAt(2, 3, -1.0f);
        r.setElementAt(3, 0, 0.0f);
        r.setElementAt(3, 1, 0.0f);
        r.setElementAt(3, 2, C);
        r.setElementAt(3, 3, 0.0f);
        r = r.transpose();
        projMatrix = r;
    }
    
    public Matrix3D getPerspectiveMat()
    {
        return projMatrix;
    }
    
    public Matrix3D generateLookAtMat()
    {
        return null;
    }
    
}
