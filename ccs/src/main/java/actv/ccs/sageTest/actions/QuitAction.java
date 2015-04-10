package actv.ccs.sageTest.actions;

import sage.input.action.AbstractInputAction;
import sage.app.AbstractGame;
import net.java.games.input.Event;
import sage.camera.*;
import graphicslib3D.Point3D;
import graphicslib3D.Vector3D;

public class QuitAction extends AbstractInputAction{ 
   private AbstractGame theGame;
   public QuitAction(AbstractGame game){ 
      theGame = game;
   }
   public void performAction(float time, Event e){
      theGame.setGameOver(true);
   }
}