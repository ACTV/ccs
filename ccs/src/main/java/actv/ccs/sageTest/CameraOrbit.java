package actv.ccs.sageTest;

import net.java.games.input.Component.Identifier.Axis;
import net.java.games.input.Event;


import graphicslib3D.Point3D;
import graphicslib3D.Vector3D;
import sage.camera.ICamera;
import sage.input.IInputManager;
import sage.input.action.AbstractInputAction;
import sage.input.action.IAction;
import sage.scene.SceneNode;
import sage.util.MathUtils;

public class CameraOrbit {

	 private ICamera cam; //the camera being controlled
	 private SceneNode target; //the target the camera looks at
	 private float cameraAzimuth; //rotation of camera around target Y axis
	 private float cameraElevation; //elevation of camera above target
	 private float cameraDistanceFromTarget;
	 private Point3D targetPos; // avatar’s position in the world
	 private Vector3D worldUpVec;
	 public CameraOrbit (ICamera cam, SceneNode target,
	 IInputManager inputMgr, String controllerName)
	 { this.cam = cam;
	 this.target = target;
	 worldUpVec = new Vector3D(0,1,0);
	 cameraDistanceFromTarget = 10.0f;
	 cameraAzimuth = 360; // start from BEHIND and ABOVE the target
	 cameraElevation = 0.0f; // elevation is in degrees
	 update(0.0f); // initialize camera state
	 setupInput(inputMgr, controllerName);
	 }
	 public void update(float time)
	 {
	 updateTarget();
	 updateCameraPosition();
	 cam.lookAt(targetPos, worldUpVec); // SAGE built-in function
	 }
	 private void updateTarget()
	 { targetPos = new Point3D(target.getWorldTranslation().getCol(3)); }
	 private void updateCameraPosition()
	 {
	 double theta = cameraAzimuth;
	 double phi = cameraElevation ;
	 double r = cameraDistanceFromTarget;
	 // calculate new camera position in Cartesian coords
	 Point3D relativePosition = MathUtils.sphericalToCartesian(theta, phi, r);
	 Point3D desiredCameraLoc = relativePosition.add(targetPos);
	 cam.setLocation(desiredCameraLoc);
	 }
	 private void setupInput(IInputManager im, String cn)
	 { IAction orbitAction = new OrbitAroundAction();
     IAction zoomInAction = new ZoomInAction();
     IAction zoomOutAction = new ZoomOutAction();
	 im.associateAction(cn, Axis.RX, orbitAction, IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
	/*	if (cn.equals("HID-compliant mouse"))
		{
		System.out.println("mouse");
		im.associateAction(cn, net.java.games.input.Component.Identifier.Button.LEFT, zoomInAction, IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		// mess around with this later
		im.associateAction(cn, net.java.games.input.Component.Identifier.Button.RIGHT, zoomOutAction, IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
			
		}
		*/
	 }
	 private class OrbitAroundAction extends AbstractInputAction
	 {
	 public void performAction(float time, Event evt)
	 {
	 float rotAmount;
	 if (evt.getValue() < -0.2) { rotAmount=-0.1f; }
	 else { if (evt.getValue() > 0.2) { rotAmount=0.1f; }
	 else { rotAmount=0.0f; }
	 }
	 cameraAzimuth += rotAmount ;
	 cameraAzimuth = cameraAzimuth % 360 ;
	} }
	   private class ZoomInAction extends AbstractInputAction{
		      public void performAction(float time, Event evt){
		         cameraDistanceFromTarget -= 0.1f;
		      } 
		   } 
		   private class ZoomOutAction extends AbstractInputAction{
		      public void performAction(float time, Event evt){
		         cameraDistanceFromTarget += 0.1f;
		      } 
		   }

}
