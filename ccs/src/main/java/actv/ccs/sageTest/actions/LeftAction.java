package actv.ccs.sageTest.actions;

import sage.input.action.AbstractInputAction;
import sage.app.AbstractGame;
import net.java.games.input.Event;
import sage.camera.*;
import graphicslib3D.Point3D;
import graphicslib3D.Vector3D;
import graphicslib3D.Matrix3D;
import sage.scene.SceneNode;
import sage.scene.shape.*;
import sage.terrain.*;
public class LeftAction extends AbstractInputAction{ 
   private SceneNode s;
   private Matrix3D sM;
   private float speed = 0.01f;
   public LeftAction(SceneNode sn){ 
      s = sn;
      sM = s.getLocalTranslation();
   }
   public void performAction(float time, Event e){
      /* sM.translate(-0.1f,0,0);
      s.setLocalTranslation(sM);
      s.updateWorldBound();
      */
	   Matrix3D rot = s.getLocalRotation();
	   Vector3D dir = new Vector3D(1,0,0);
	   dir = dir.mult(rot);
	   dir.scale((double)(speed * time));
	   s.translate((float)dir.getX(),(float)dir.getY(),(float)dir.getZ());
	   s.updateWorldBound();
   }
}