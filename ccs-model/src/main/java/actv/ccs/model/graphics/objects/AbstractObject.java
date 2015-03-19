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
import graphicslib3D.Shape3D;

/**
 *
 * @author Victor
 */
abstract public class AbstractObject extends Shape3D
{
    private float []verts;

    public AbstractObject(String string, Matrix3D md, Matrix3D md1, Matrix3D md2, Material mtrl, Texture txtr, int i)
    {
        super(string, md, md1, md2, mtrl, txtr, i);
        this.verts = verts;
        
    }
    
    public AbstractObject(String string)
    {
        super(string);
        this.verts = verts;
        
    }

    public Point3D getPostion()
    {
       return new Point3D(getTranslation().elementAt(0, 3),this.getTranslation().elementAt(1, 3),this.getTranslation().elementAt(2, 3));
    }
    
    public void setPostion(float dx, float dy, float dz)
    {
        getTranslation().setToIdentity();
        translate(dx,dy,dz);
    }
}
