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
		dir.scale((double) (speed * 2));
		s.translate((float) dir.getX(), (float) dir.getY(), (float) dir.getZ());
		((ConvictCichlid)s).setlocation(dir.getX(), dir.getY(), dir.getZ());
		s.updateWorldBound();
	}
	
	public static void turn(SceneNode s, float degrees){
		Matrix3D rot = s.getLocalRotation();
		Vector3D dir = ((ConvictCichlid)s).getDirection().normalize();
		
		dir = dir.mult(rot);
		s.rotate(degrees, new Vector3D(0,1,0));
		((ConvictCichlid)s).setDirection(dir);
		s.updateWorldBound();
	}

	/**
	 * 
	 * This method returns true if the distance between each object's
	 * 	location is greater than both of the object's influence reach.
	 * 
	 */
	public static boolean withinRange(SceneNode s1, SceneNode s2){
		
		double distance = distance(s1, s2);
		
		if(distance < ((ConvictCichlid)s1).getInfluence() && distance > 0)
			return true;
		
		return false;
	}
	
	/**
	 * 
	 * This method returns true if the distance between each object's
	 * 	location is greater than both of the object's influence reach.
	 * 
	 */
	public static float distance(SceneNode s1, SceneNode s2){
		ConvictCichlid cc1 = (ConvictCichlid)s1;
		ConvictCichlid cc2 = (ConvictCichlid)s2;
		
		double dx = cc2.getLocation().getX() - cc1.getLocation().getX();
		double dy = cc2.getLocation().getY() - cc1.getLocation().getY();
		double dz = cc2.getLocation().getZ() - cc1.getLocation().getZ();
		
		return (float)Math.sqrt( Math.pow(dx, 2) + Math.pow(dy, 2) + Math.pow(dz, 2));
	}
	
	public static Vector3D getDirection(SceneNode s1, SceneNode s2){
		return ((ConvictCichlid)s2).getDirection().minus(((ConvictCichlid)s1).getDirection());
	}
	
	public static Vector3D chase(SceneNode s1, SceneNode s2){
		ConvictCichlid cc1 = (ConvictCichlid)s1;
		ConvictCichlid cc2 = (ConvictCichlid)s2;
		
		double d = cc1.getDirection().dot(cc2.getDirection());
		double m = cc1.getDirection().magnitude() * cc2.getDirection().magnitude();
		float theta = (float)((Math.acos(d/m))*(180/Math.PI));
		
		Vector3D v = (cc1.getDirection().cross(cc2.getDirection())).normalize();
		
		Vector3D chase = cc1.getDirection().normalize().mult(Math.cos(theta)).add(v.mult(Math.sin(theta)));
		
		turn(cc1,theta);
		
		return chase;
	}
}
