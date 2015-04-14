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
   public LeftAction(SceneNode sn){ 
      s = sn;
      sM = s.getLocalTranslation();     
   }
   public void performAction(float time, Event e){
      sM.translate(-0.1f,0,0);
      s.setLocalTranslation(sM);
      s.updateWorldBound();
      System.out.println("pos: " + s.getLocalTranslation());
   }

}