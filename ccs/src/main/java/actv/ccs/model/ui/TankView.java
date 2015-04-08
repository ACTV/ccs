package actv.ccs.model.ui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import javax.swing.JPanel;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;
import javax.media.j3d.Alpha;
import javax.media.j3d.Appearance;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Material;
import javax.media.j3d.PositionInterpolator;
import javax.media.j3d.Texture;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.opengl.awt.GLCanvas;

import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.SimpleUniverse;

import actv.ccs.model.*;
import actv.ccs.model.graphics.*;
import actv.ccs.model.IDrawable;


public class TankView extends JPanel implements IObserver {
	private SimulationWorld sW;
	private GLCanvas glC;
	private MainGraphics mg;
	AffineTransform worldToND, ndToScreen, theVTM, inverseVTM;
	private boolean pauseView;
	private double winLeft = 0;
	private double winWidth = 1000;
	private double winBottom = 0;
	private double winHeight = 500;
	private Canvas3D canvas;
	public TankView(SimulationWorld s)
	{
            sW = s;
            SimpleUniverse u = new SimpleUniverse();
            BranchGroup bgRoot = new BranchGroup();
            u.addBranchGraph(createBackground());
            
            TransformGroup tg = createBehaviors(bgRoot);
            tg.addChild(createSceneGraph());
            
            u.getViewingPlatform().setNominalViewingTransform();
            
            addLights(bgRoot);
            
            u.addBranchGraph(bgRoot);
            
	}
	
	
	 public BranchGroup createSceneGraph() {
		    // create a parent BranchGroup node for the Sphere
		    BranchGroup bg = new BranchGroup();

		    // create an Appearance for the Sphere.
		    // The Appearance object controls various rendering
		    // options for the Sphere geometry.
		    Appearance app = new Appearance();

		    // assign a Material to the Appearance. For the Sphere
		    // to respond to the light in the scene it must have a Material.
		    // Assign some colors to the Material and a shininess setting
		    // that controls how reflective the surface is to lighting.
		    Color3f objColor = new Color3f(0.8f, 0.2f, 1.0f);
		    Color3f black = new Color3f(0.0f, 0.0f, 0.0f);
		    app.setMaterial(new Material(objColor, black, objColor, black, 80.0f));

		    // create a Sphere with a radius of 0.1
		    // and associate the Appearance that we described.
		    // the option GENERATE_NORMALS is required to ensure that the
		    // Sphere responds correctly to lighting.
		    Sphere sphere = new Sphere(0.1f, Primitive.GENERATE_NORMALS, app);

		    // add the sphere to the BranchGroup to wire
		    // it into the scene.
		    bg.addChild(sphere);
		    return bg;
		  }

		  /*
		   * Add a directional light to the BranchGroup.
		   */
		  public void addLights(BranchGroup bg) {
		    // create the color for the light
		    Color3f color = new Color3f(1.0f, 1.0f, 0.0f);

		    // create a vector that describes the direction that
		    // the light is shining.
		    Vector3f direction = new Vector3f(-1.0f, -1.0f, -1.0f);

		    // create the directional light with the color and direction
		    DirectionalLight light = new DirectionalLight(color, direction);

		    // set the volume of influence of the light.
		    // Only objects within the Influencing Bounds
		    // will be illuminated.
		    light.setInfluencingBounds(getBoundingSphere());

		    // add the light to the BranchGroup
		    bg.addChild(light);
		  }

		  /*
		   * Create some Background geometry to use as a backdrop for the application.
		   * Here we create a Sphere that will enclose the entire scene and apply a
		   * texture image onto the inside of the Sphere to serve as a graphical
		   * backdrop for the scene.
		   */
		  public BranchGroup createBackground() {
		    // create a parent BranchGroup for the Background
		    BranchGroup backgroundGroup = new BranchGroup();

		    // create a new Background node
		    Background back = new Background();

		    // set the range of influence of the background
		    back.setApplicationBounds(getBoundingSphere());

		    // create a BranchGroup that will hold
		    // our Sphere geometry
		    BranchGroup bgGeometry = new BranchGroup();

		    // create an appearance for the Sphere
		    Appearance app = new Appearance();

		    // load a texture image using the Java 3D texture loader
		    Texture tex = new TextureLoader("tex.jpg", this).getTexture();

		    // apply the texture to the Appearance
		    app.setTexture(tex);

		    // create the Sphere geometry with radius 1.0
		    // we tell the Sphere to generate texture coordinates
		    // to enable the texture image to be rendered
		    // and because we are *inside* the Sphere we have to generate
		    // Normal coordinates inwards or the Sphere will not be visible.
		    Sphere sphere = new Sphere(1.0f, Primitive.GENERATE_TEXTURE_COORDS
		        | Primitive.GENERATE_NORMALS_INWARD, app);

		    // start wiring everything together
		    // add the Sphere to its parent BranchGroup
		    bgGeometry.addChild(sphere);

		    // assign the BranchGroup to the Background as geometry.
		    back.setGeometry(bgGeometry);

		    // add the Background node to its parent BranchGroup
		    backgroundGroup.addChild(back);

		    return backgroundGroup;
		  }

		  /*
		   * Create a behavior to move child nodes along the X-axis. The behavior is
		   * added to the BranchGroup bg, whereas any nodes added to the returned
		   * TransformGroup will be effected by the behavior.
		   */
		  public TransformGroup createBehaviors(BranchGroup bg) {
		    // create a TransformGroup.
		    //
		    // A TransformGroup is a Group node (can have children)
		    // and contains a Transform3D member.
		    //
		    // The Transform3D member contains a 4x4 transformation matrix
		    // that is applied during rendering to all the TransformGroup's
		    // child nodes. The 4x4 matrix can describe:
		    // scaling, translation and rotation in one neat package!

		    // enable the TRANSFORM_WRITE capability so that
		    // our behavior code can modify it at runtime
		    TransformGroup objTrans = new TransformGroup();
		    objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		    // create a new Transform3D that will describe
		    // the direction we want to move.
		    Transform3D xAxis = new Transform3D();

		    // create an Alpha object.
		    // The Alpha object describes a function against time.
		    // The Alpha will output a value that ranges between 0 and 1
		    // using the time parameters (in milliseconds).
		    Alpha xAlpha = new Alpha(-1, Alpha.DECREASING_ENABLE
		        | Alpha.INCREASING_ENABLE, 1000, 1000, 5000, 1000, 1000, 10000,
		        2000, 4000);

		    // create a PositionInterpolator
		    // The PositionInterpolator will modify the translation components
		    // of a TransformGroup's Transform3D (objTrans) based on the output
		    // from the Alpha. In this case the movement will range from
		    // -0.8 along the X-axis with Alpha=0 to X=0.8 when Alpha=1.
		    PositionInterpolator posInt = new PositionInterpolator(xAlpha,
		        objTrans, xAxis, -0.8f, 0.8f);

		    // set the range of influence of the PositionInterpolator
		    posInt.setSchedulingBounds(getBoundingSphere());

		    // wire the PositionInterpolator into its parent
		    // TransformGroup. Just like rendering nodes behaviors
		    // must be added to the scenegraph.
		    objTrans.addChild(posInt);

		    // add the TransformGroup to its parent BranchGroup
		    bg.addChild(objTrans);

		    // we return the TransformGroup with the
		    // behavior attached so that we can add nodes to it
		    // (which will be effected by the PositionInterpolator).
		    return objTrans;
		  }

		  /*
		   * Return a BoundingSphere that describes the volume of the scene.
		   */
		  BoundingSphere getBoundingSphere() {
		    return new BoundingSphere(new Point3d (0.0, 0.0, 0.0), 200.0);
		  }
// test	
/*	public TankView(SimulationWorld s, GLCanvas glc)
	{
            add(glc);
            sW = s;
	}
	*/
	
        
        //NOT REALLY NEEDED. KEEPING IT HERE FOR REFERENCE -VICTOR HUBA
        
	public void update(IObservable o, Object obj)
	{
		obj = (SimulationWorldProxy) o;
		repaint();
	}
	public void paintComponent(Graphics g)
	{

         super.paintComponent(g);
         
        
/*		 super.paintComponent(g); // paint objects
		 Graphics2D g2d = (Graphics2D) g;
		 AffineTransform saveAT = g2d.getTransform();
		 
		 // update vtm
		 worldToND = buildWorldToNDXform(winLeft, winWidth, winBottom, winHeight);
		 ndToScreen = buildNDToScreenXform(1000, 300);
		 theVTM = (AffineTransform) ndToScreen.clone();
		 theVTM.concatenate(worldToND);
	
		 g2d.transform(theVTM);
		 
		 Iterator it = sW.getIterator(); 
		 while (it.hasNext()) // get every game object
		 {
			 IDrawable obj = (IDrawable) it.getNext();
			 obj.draw(g2d); // draw the object
		 }
		 g2d.setTransform(saveAT);
		 
*/
	}
	public AffineTransform buildWorldToNDXform(double wiLeft, double wiWidth, double wiBottom, double wiHeight)
	{
		AffineTransform at = new AffineTransform();
	    at.scale(1/wiWidth, 1/wiHeight); 
		at.translate(-wiLeft, -wiBottom);

		return at;
	}
	public AffineTransform buildNDToScreenXform(int x, int y)
	{
		AffineTransform at = new AffineTransform();
		at.translate(0, y);
		at.scale(x, -y);
		return at;
	}	
        
	// here is where i want to have sage engine information connected to here
}

