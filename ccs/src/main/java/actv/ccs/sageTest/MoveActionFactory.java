package actv.ccs.sageTest;

import actv.ccs.model.ConvictCichlid;
import graphicslib3D.Matrix3D;
import graphicslib3D.Point3D;
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
		Vector3D dir = ((ConvictCichlid)s).getDirection().normalize();
		dir = dir.mult(rot);
		dir.scale((double) (speed * 1));
		s.translate((float) dir.getX(), (float) dir.getY(), (float) dir.getZ());
		((ConvictCichlid)s).setlocation(dir.getX(), dir.getY(), dir.getZ());
		s.updateWorldBound();
	}
	
	public static void turn(SceneNode s, float degrees){
		Matrix3D rot = s.getLocalRotation();
		Vector3D dir = ((ConvictCichlid)s).getDirection().normalize();
		
		dir = dir.mult(rot);
		s.rotate(degrees, new Vector3D(0,1,0));
		s.updateWorldBound();
	}

	/**
	 * 
	 * This method returns true if the distance between each object's
	 * 	location is greater than both of the object's influence reach.
	 * 
	 */
	public static boolean withinRange(SceneNode s1, SceneNode s2){
		ConvictCichlid cc1 = (ConvictCichlid)s1;
		ConvictCichlid cc2 = (ConvictCichlid)s2;
		
		Vector3D res = getDirection(s1, s2);
		
		// Grab the magnitude of the distance between the locations and compare to influences
		if(Math.abs(res.magnitude()) > cc1.getInfluence() && res.magnitude() > cc2.getInfluence())
			return true;
		
		return true;
	}
	
	public static Vector3D getDirection(SceneNode s1, SceneNode s2){
		return ((ConvictCichlid)s2).getDirection().minus(((ConvictCichlid)s1).getDirection());
	}
}
