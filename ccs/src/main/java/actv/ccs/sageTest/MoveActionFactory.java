package actv.ccs.sageTest;

import graphicslib3D.Matrix3D;
import graphicslib3D.Vector3D;
import sage.scene.SceneNode;

/**
 * 
 * The MoveActionFactory provides static methods to perform move updates
 * 	to the SceneNodes (aka convict cichlids).
 *
 */
public class MoveActionFactory {

	public static void moveForward(SceneNode s, float speed){
		Matrix3D rot = s.getLocalRotation();
		Vector3D dir = ((TestCichlid)s).getDirection().normalize();
		dir = dir.mult(rot);
		dir.scale((double) (speed * 1));
		s.translate((float) dir.getX(), (float) dir.getY(), (float) dir.getZ());
		s.updateWorldBound();
	}
	
	public static void turn(SceneNode s, float degrees){
		Matrix3D rot = s.getLocalRotation();
		Vector3D dir = ((TestCichlid)s).getDirection().normalize();
		
		dir = dir.mult(rot);
		s.rotate(degrees, new Vector3D(0,1,0));
		s.updateWorldBound();
	}
}
