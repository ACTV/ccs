package actv.ccs.model.ui;

import graphicslib3D.Material;
import graphicslib3D.Matrix3D;
import graphicslib3D.Point3D;

import javax.media.opengl.GLAutoDrawable;

import com.jogamp.opengl.util.texture.Texture;

import actv.ccs.model.graphics.objects.AbstractObject;

public class TestObject extends AbstractObject {

	
    private float []verts;

    public TestObject(String string, Matrix3D md, Matrix3D md1, Matrix3D md2, Material mtrl, Texture txtr, int i)
    {
        super(string, md, md1, md2, mtrl, txtr, i);
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


	@Override
	public void draw(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub
		
	}

}
