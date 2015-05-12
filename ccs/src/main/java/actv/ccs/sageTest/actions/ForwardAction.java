package actv.ccs.sageTest.actions;

import actv.ccs.model.ConvictCichlid;
import sage.input.action.AbstractInputAction;
import sage.app.AbstractGame;
import net.java.games.input.Event;
import sage.camera.*;
import graphicslib3D.Point3D;
import graphicslib3D.Vector3D;
import graphicslib3D.Matrix3D;
import sage.scene.Model3DTriMesh;
import sage.scene.SceneNode;
import sage.scene.shape.*;

public class ForwardAction extends AbstractInputAction {
	private SceneNode s;
	private Matrix3D sM;
	private float speed = 0.01f;
	private Model3DTriMesh avatar;
	public ForwardAction(SceneNode sn, Model3DTriMesh n) {
		s = sn;
		avatar = n;
		sM = s.getLocalTranslation();
	}

	public void performAction(float time, Event e) {
		/*
		 * sM.translate(0,0,0.1f); s.setLocalTranslation(sM);
		 * s.updateWorldBound();
		 */
		Matrix3D rot = s.getLocalRotation();
//		Vector3D dir = new Vector3D(0, 0, 1);
		Vector3D dir = ((ConvictCichlid)s).getDirection().normalize();
		dir = dir.mult(rot);
		dir.scale((double) (speed * time));
		s.translate((float) dir.getX(), (float) dir.getY(), (float) dir.getZ());
		s.updateWorldBound();
	//	avatar.startAnimation("ArmatureAction.001");
	}

}