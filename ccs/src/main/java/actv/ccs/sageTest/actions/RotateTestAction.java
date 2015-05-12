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
public class RotateTestAction extends AbstractInputAction{ 
   private SceneNode s;
   private Matrix3D sM;
   private float speed = 0.01f;
   public RotateTestAction(SceneNode sn){ 
      s = sn;
      sM = s.getLocalTranslation();
   }
   public void performAction(float time, Event e){
      /* sM.translate(0,0,0.1f);
      s.setLocalTranslation(sM);
      s.updateWorldBound();
      */
	   Matrix3D rot = s.getLocalRotation();
	   Vector3D dir = new Vector3D(0,1,-1);
	   dir = dir.mult(rot);
	   dir.scale((double)(speed * time));
	   s.rotate(15, new Vector3D(0, 1, 0));

	   s.rotate(-50, new Vector3D(0, 1, 0));
	   s.updateWorldBound();
   }
   
}