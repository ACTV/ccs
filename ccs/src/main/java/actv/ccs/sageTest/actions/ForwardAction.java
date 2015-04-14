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
public class ForwardAction extends AbstractInputAction{ 
   private SceneNode s;
   private Matrix3D sM;
   public ForwardAction(SceneNode sn){ 
      s = sn;
      sM = s.getLocalTranslation();
   }
   public void performAction(float time, Event e){
      sM.translate(0,0,0.1f);
      s.setLocalTranslation(sM);
      s.updateWorldBound();
      System.out.println("pos: " + s.getLocalTranslation());
   }
   
}