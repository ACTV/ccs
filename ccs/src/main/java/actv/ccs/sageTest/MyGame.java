package actv.ccs.sageTest;

import java.awt.Color;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import actv.ccs.sageTest.actions.BackwardAction;
import actv.ccs.sageTest.actions.ForwardAction;
import actv.ccs.sageTest.actions.LeftAction;
import actv.ccs.sageTest.actions.QuitAction;
import actv.ccs.sageTest.actions.RightAction;
import graphicslib3D.Matrix3D;
import graphicslib3D.Point3D;
import graphicslib3D.Vector3D;
import sage.app.BaseGame;
import sage.camera.ICamera;
import sage.camera.JOGLCamera;
import sage.display.IDisplaySystem;
import sage.input.IInputManager;
import sage.input.InputManager;
import sage.input.action.IAction;
import sage.renderer.IRenderer;
import sage.scene.Group;
import sage.scene.HUDString;
import sage.scene.SceneNode;
import sage.scene.SkyBox;
import sage.scene.bounding.BoundingSphere;
import sage.scene.shape.Cube;
import sage.scene.shape.Line;
import sage.scene.shape.Pyramid;
import sage.scene.shape.Rectangle;
import sage.scene.state.RenderState.RenderStateType;
import sage.scene.state.TextureState;
import sage.terrain.AbstractHeightMap;
import sage.terrain.HillHeightMap;
import sage.terrain.ImageBasedHeightMap;
import sage.terrain.TerrainBlock;
import sage.texture.*;

public class MyGame extends BaseGame {

	IDisplaySystem display;
	IInputManager im;
	private ICamera camera;
	private CameraOrbit cc;
	private SkyBox skybox;
	private Connection conn;
	private ResultSet rs, rsI;
	private TerrainBlock floor;
	private TestCichlid cichlidA, cichlidB, cichlidC;
	private SceneNode cameraGuy;
	private Line yAxis1, zYPAxis, zyPtoxEnd3, pPart, zPart, yEndtoZPart, xEndtoZPart, xxPart, finishPart;
	
	public void initGame()
	{
		initObjects();
		spawnCichlids();
		createPerson();
	//	createScene();
	//	initTerrain();
		initActions();
	}
	
	protected void initObjects()
	{
		// this is for initializing objects
		display = getDisplaySystem();
		display.setTitle("sage implementation of the pain");
		
		camera = display.getRenderer().getCamera();
		camera.setPerspectiveFrustum(45, 1, 0.01, 1000);
		camera.setLocation(new Point3D(1, 1, 20));
		/*
		 * as much as i hate it, it's going to look like the previous team's project where our view is pretty much the closest face out of the 4 sided aquarium.
		 */

		
		// so instead of an array, it will be a group class that will hold the objects so instead of iterating ... it will go like that. so maybe i will have to change
		// the convict cichlid iterator to become a scenenode iterator.
		
		
		// this part will soon be the equivalent of spawnCichlids

		
		// equivalent of spawn plants
		
		
		
		// grab tank values here, which should be the same since it's not representational data
		
		// creating x, y, z lines for a basis
		 Point3D origin = new Point3D(0,0,0);
		 Point3D xEnd1 = new Point3D(100, 100, 0);
		 Point3D xEnd3 = new Point3D(100, 100, 100);
		 Point3D xEnd2 = new Point3D(100, 0, 100);
		 Point3D zyP = new Point3D(0, 100, 100);
		 Point3D xEnd = new Point3D(100,0,0);
		 Point3D yEnd = new Point3D(0,100,0);
		 Point3D zEnd = new Point3D(0,0,100);
		 
		 // base 
		 Line xAxis = new Line (origin, xEnd, Color.red, 2);
		 Line yAxis = new Line (origin, yEnd, Color.green, 2);
		 Line zAxis = new Line (origin, zEnd, Color.blue, 2); // Base 
		 
		// Line xAxis1 = new Line (xEnd1, xEnd3, Color.cyan, 2);
		 yAxis1 = new Line (xEnd2, xEnd3, Color.GRAY, 2);
		 zyPtoxEnd3 = new Line (new Point3D(100, 0, 0), new Point3D(100, 100, 0), Color.BLUE, 2);
		 pPart = new Line(new Point3D(100, 0, 0), new Point3D(100, 0, 100), Color.green, 2);
		 finishPart = new Line(new Point3D(0, 100, 0), new Point3D(100, 100, 0), Color.PINK, 2);
		 yEndtoZPart = new Line(yEnd, new Point3D(0, 100, 100), Color.orange, 2);
		 xEndtoZPart = new Line(new Point3D(0, 100, 100), new Point3D(100, 100, 100), Color.orange, 2);
		 xxPart = new Line(new Point3D(100, 100, 0), new Point3D(100, 100, 100), Color.magenta, 2);
		 zPart = new Line(zEnd, xEnd2, Color.orange, 2);
		 zYPAxis = new Line(zEnd, zyP, Color.gray, 2);
		// Line zAxis1 = new Line (xEnd3, xEnd1, Color.MAGENTA, 2);
		 
		 
		 addGameWorldObject(yAxis1);
		 yAxis1.updateWorldBound();
		 addGameWorldObject(zYPAxis);
		 zYPAxis.updateWorldBound();
		 addGameWorldObject(zyPtoxEnd3);
		 zyPtoxEnd3.updateWorldBound();
		 addGameWorldObject(pPart);
		 pPart.updateWorldBound();
		 addGameWorldObject(zPart);
		 zPart.updateWorldBound();
		 addGameWorldObject(yEndtoZPart);
		 yEndtoZPart.updateWorldBound();
		 addGameWorldObject(xEndtoZPart);
		 xEndtoZPart.updateWorldBound();
		 addGameWorldObject(xxPart);
		 xxPart.updateWorldBound();
		 addGameWorldObject(finishPart);
		 finishPart.updateWorldBound();
		 
		 
		 
		 addGameWorldObject(xAxis); 
		 addGameWorldObject(yAxis);
		 addGameWorldObject(zAxis);
		 
	}
	public void createPerson()
	{
		cameraGuy = new Cube("Cameraguy");
		cameraGuy.translate(0, 0, 0);
		cameraGuy.rotate(180, new Vector3D(0, 1, 0));
		addGameWorldObject(cameraGuy);
		cameraGuy.updateWorldBound();
		
	}
	public void spawnCichlids()
	{
		try {
			conn = DriverManager.getConnection("jdbc:ucanaccess://FishPool.accdb");
		
		Statement s = conn.createStatement();
		rs = s.executeQuery("SELECT fishID FROM [SimulationFish]");
		while (rs.next())
		{
			String id = rs.getString("fishID"); //Field from database ex. FishA, FishB
			int idS =  Integer.parseInt(id);

			System.out.println(idS);
				
			if (id.equals("1"))
			{
				rsI = s.executeQuery("SELECT * FROM [FishPool] WHERE Type='Fish A'");
				while (rsI.next())
					{
						String name = rsI.getString("Type"); //Field from database ex. FishA, FishB
			        	String weight = rsI.getString("Weight");
			        	String width = rsI.getString("Width");
			        	String height = rsI.getString("Height");
			        	String gender = rsI.getString("Gender");
			        	String aggro = rsI.getString("AggroLevel"); //default to 10
			        	String xLocS = rsI.getString("StartingXPos");
			        	String yLocS = rsI.getString("StartingYPos");
			        	String zLocS = rsI.getString("StartingZPos");
			        	
			        	float weightW = Float.parseFloat(weight);
			        	float widthW = Float.parseFloat(width);
			        	float heightW = Float.parseFloat(height);
			        	float aggroW = Float.parseFloat(aggro);
			        	double xStartW = Double.parseDouble(xLocS);
			        	double yStartY = Double.parseDouble(yLocS);
			        	double zStartZ = Double.parseDouble(zLocS);
			        	
			    		cichlidA = new TestCichlid();
			    		cichlidA.setName(name);
			    		cichlidA.setGender(gender);
			    		cichlidA.setAggroLevel(aggroW);
			    		Matrix3D cichlidAT = cichlidA.getLocalTranslation(); // this is for position
			    		cichlidAT.translate(xStartW, yStartY, zStartZ);
			    		cichlidA.setLocalTranslation(cichlidAT);
			    		Matrix3D cichlidAS = cichlidA.getLocalScale(); // this is for size of object
			    		cichlidAS.scale(widthW*weightW*.100, heightW*weightW*.100, 0); // the scale might be too big so we will have to do the weight*.10
			    		cichlidA.setLocalScale(cichlidAS);
			    		Matrix3D cichlidAR = new Matrix3D(); // this is for the rotation of the object
			    		cichlidAR.rotateX(30);
			    		cichlidA.setLocalRotation(cichlidAR);
			    		addGameWorldObject(cichlidA);
			    		cichlidA.updateWorldBound();

			    	}
				}
				else if (id.equals("2"))
				{
					rsI = s.executeQuery("SELECT * FROM [FishPool] WHERE Type='Fish B'");
					
					while (rsI.next())
					{
					String name = rsI.getString("Type"); //Field from database ex. FishA, FishB
		        	String weight = rsI.getString("Weight");
		        	String width = rsI.getString("Width");
		        	String height = rsI.getString("Height");
		        	String gender = rsI.getString("Gender");
		        	String aggro = rsI.getString("AggroLevel"); //default to 10
		        	String xLocS = rsI.getString("StartingXPos");
		        	String yLocS = rsI.getString("StartingYPos");
		        	String zLocS = rsI.getString("StartingZPos");
		        	
		        	float weightW = Float.parseFloat(weight);
		        	float widthW = Float.parseFloat(width);
		        	float heightW = Float.parseFloat(height);
		        	float aggroW = Float.parseFloat(aggro);
		        	double xStartW = Double.parseDouble(xLocS);
		        	double yStartY = Double.parseDouble(yLocS);
		        	double zStartZ = Double.parseDouble(zLocS);
		        	
		    		cichlidB = new TestCichlid();
		    		cichlidB.setName(name);
		    		cichlidB.setGender(gender);
		    		cichlidB.setAggroLevel(aggroW);
		    		Matrix3D cichlidBT = cichlidB.getLocalTranslation(); // this is for position
		    		cichlidBT.translate(xStartW, yStartY, zStartZ);
		    		cichlidB.setLocalTranslation(cichlidBT);
		    		Matrix3D cichlidBS = cichlidB.getLocalScale(); // this is for size of object
		    		cichlidBS.scale(widthW*weightW*.100, heightW*weightW*.100, 0); // the scale might be too big so we will have to do the weight*.10
		    		cichlidB.setLocalScale(cichlidBS);
		    		Matrix3D cichlidBR = new Matrix3D(); // this is for the rotation of the object
		    		cichlidBR.rotateX(30);
		    		cichlidB.setLocalRotation(cichlidBR);
		    		addGameWorldObject(cichlidB);
		    		cichlidB.updateWorldBound();
	
		    		// the issue with this thing is that the function is automatically called when new game starts... maybe i can call this from something..
					}
				}
				else if (id.equals("3"))
				{
					rsI = s.executeQuery("SELECT * FROM [FishPool] WHERE Type='Fish C'");
					
					while (rsI.next())
					{
					String name = rsI.getString("Type"); //Field from database ex. FishA, FishB
		        	String weight = rsI.getString("Weight");
		        	String width = rsI.getString("Width");
		        	String height = rsI.getString("Height");
		        	String gender = rsI.getString("Gender");
		        	String aggro = rsI.getString("AggroLevel"); //default to 10
		        	String xLocS = rsI.getString("StartingXPos");
		        	String yLocS = rsI.getString("StartingYPos");
		        	String zLocS = rsI.getString("StartingZPos");
		        	
		        	float weightW = Float.parseFloat(weight);
		        	float widthW = Float.parseFloat(width);
		        	float heightW = Float.parseFloat(height);
		        	float aggroW = Float.parseFloat(aggro);
		        	double xStartW = Double.parseDouble(xLocS);
		        	double yStartY = Double.parseDouble(yLocS);
		        	double zStartZ = Double.parseDouble(zLocS);
		        	
		    		cichlidC = new TestCichlid();
		    		cichlidC.setName(name);
		    		cichlidC.setGender(gender);
		    		cichlidC.setAggroLevel(aggroW);
		    		Matrix3D cichlidCT = cichlidC.getLocalTranslation(); // this is for position
		    		cichlidCT.translate(xStartW, yStartY, zStartZ);
		    		cichlidC.setLocalTranslation(cichlidCT);
		    		Matrix3D cichlidCS = cichlidC.getLocalScale(); // this is for size of object
		    		cichlidCS.scale(widthW*weightW*.100, heightW*weightW*.100, 0); // the scale might be too big so we will have to do the weight*.10
		    		cichlidC.setLocalScale(cichlidCS);
		    		Matrix3D cichlidCR = new Matrix3D(); // this is for the rotation of the object
		    		cichlidCR.rotateX(30);
		    		cichlidC.setLocalRotation(cichlidCR);
		    		addGameWorldObject(cichlidC);
		    		cichlidC.updateWorldBound();
					}
				}
			}
			conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	private void createScene() // the scene is the background of the fish tank ... non issue for now.
	{
	 	   skybox = new SkyBox("SkyBox", 10.0f, 10.0f, 10.0f); 
	 	  

	 	   Texture testMountain = TextureManager.loadTexture2D("src/main/java/actv/ccs/sageTest/Images/floorMountain.bmp"); 
	 	   Texture skyThing = TextureManager.loadTexture2D("src/main/java/actv/ccs/sageTest/Images/sky.jpg"); 
	// 	   skybox.setTexture(SkyBox.Face.North, skyThing); 
	// 	   skybox.setTexture(SkyBox.Face.South, skyThing);
	//     skybox.setTexture(SkyBox.Face.East, skyThing); 
	// 	   skybox.setTexture(SkyBox.Face.West, skyThing); 
	// 	   skybox.setTexture(SkyBox.Face.Up, skyThing);
	      
	 	  addGameWorldObject(skybox);
	      
/*	 	  
	 try {		 
	 		AbstractHeightMap heightmap = null; 
	 
	 
	 		heightmap = new ImageBasedHeightMap(testMountain.getImage()); 
	 		heightmap.load(); 
	 
	 
	 		Vector3D scaleFactor = new Vector3D(new Point3D(1, 1, 1)); 

	 		floor = new TerrainBlock("tblock", 512, scaleFactor, heightmap.getHeightData(), new Point3D( 0, 0, 0));
	 	//	floor.setTexture(skyThing);
	 		Matrix3D p1LotT = floor.getLocalTranslation();
	 		p1LotT.translate(0.0f, -0.5f, 0.0f);
	 		floor.setLocalTranslation(p1LotT);
	 		Matrix3D p1Scale = floor.getLocalScale();
	 		p1Scale.scale(10f, 10f, 0);
	 		floor.setLocalScale(p1Scale);
	 		Matrix3D p1Rot = new Matrix3D();
	 		p1Rot.rotateX(-90);
	 		floor.setLocalRotation(p1Rot);
	 		
	 		addGameWorldObject(floor);
	} catch (Exception e)
	{
		e.printStackTrace();
	}
	*/
	
	 		
	}
	private void initActions()
	{
		im = getInputManager();
		String kbName = im.getKeyboardName(); // error here. it shouldn't be null
		String mName = im.getMouseName();
		// sFindComponents f = new FindComponents();
		
		cc = new CameraOrbit(camera, cameraGuy, im, mName);
		
		System.out.println("controller: " + mName);

	//	for this area, need to do a checker if A and B and C are called...	
	//	IAction moveForwardA = new ForwardAction(cichlidA);
	//	IAction moveForwardB = new ForwardAction(cichlidB);
	//	IAction moveForwardC = new ForwardAction(cichlidC);
		IAction moveForwardO = new ForwardAction(cameraGuy);
		
	//	IAction moveBackA = new BackwardAction(cichlidA);
	//	IAction moveBackB = new BackwardAction(cichlidB);
	//	IAction moveBackC = new BackwardAction(cichlidC);
		IAction moveBackO = new BackwardAction(cameraGuy);
		
	//	IAction moveLeftA = new LeftAction(cichlidA);
	//	IAction moveLeftB = new LeftAction(cichlidB);
	//	IAction moveLeftC = new LeftAction(cichlidC);
		IAction moveLeftO = new LeftAction(cameraGuy);
		
	//	IAction moveRightA = new RightAction(cichlidA);
	//	IAction moveRightB = new RightAction(cichlidB);
	//	IAction moveRightC = new RightAction(cichlidC);
		IAction moveRightO = new RightAction(cameraGuy);
		
		IAction quitGame = new QuitAction(this);
		
		im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.ESCAPE, 
				quitGame, IInputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
		// here is the movement options of the character ..
		im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.W, 
				moveForwardO, IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.S, 
				moveBackO, IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.A, 
				moveLeftO, IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.D, 
				moveRightO, IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		
		

	}
	private void initTerrain() // non issue
	{
		   // create height map and terrain block
			 ImageBasedHeightMap myHeightMap =
			 new ImageBasedHeightMap("floorMountain.jpg");
			 TerrainBlock imageTerrain = createTerBlock(myHeightMap);
			 // create texture and texture state to color the terrain
	//		 Texture grassTexture = TextureManager.loadTexture2D("stones.jpg");
			 // apply the texture to the terrain
	//		 imageTerrain.setTexture(grassTexture);
			 addGameWorldObject(imageTerrain);

	}
	private TerrainBlock createTerBlock(AbstractHeightMap heightMap) // issue
	{
		 float heightScale = 0.05f;
		 Vector3D terrainScale = new Vector3D(1, heightScale, 1);
		 // use the size of the height map as the size of the terrain
		 int terrainSize = heightMap.getSize();
		 // specify terrain origin so heightmap (0,0) is at world origin
		 float cornerHeight =
		 heightMap.getTrueHeightAtPoint(0, 0) * heightScale;
		 Point3D terrainOrigin = new Point3D(0, -cornerHeight, 0);
		 // create a terrain block using the height map
		 String name = "Terrain:" + heightMap.getClass().getSimpleName();
		 TerrainBlock tb = new TerrainBlock(name, terrainSize, terrainScale,
		 heightMap.getHeightData(), terrainOrigin);
		 return tb;
	}
	public void update(float time) // this will be where the objects will move
	{
		super.update(time);
		cc.update(time);
		for (SceneNode s: getGameWorld())
		{
			if (s instanceof TestCichlid) // here will be where the objects will have be able to move, but i will implement that later.
			{
				if (s == cichlidA)
				{
					s.translate(0, 0, .1f);
					s.updateWorldBound();
				}
				if (s == cichlidB)
				{
					// call move stuff here
					s.translate(0, 0, -.1f);
					s.updateWorldBound();
				}
				if (s == cichlidC)
				{
					// call move stuff here
					s.translate(0, 0.1f, 0.1f);
					s.updateWorldBound();
				}

			}
			
		}
		
		/*
		 * collision example
		 *      if (tpt.getWorldBound().intersects(p1.getWorldBound()) && collidedWTeapot == false){
         collidedWTeapot = true;
         numCrashes++;
         score1 += 100;
         CrashEvent newCrash = new CrashEvent(numCrashes);
         removeGameWorldObject(tpt);
         eventMgr.triggerEvent(newCrash);
      }
		 */
		/*
		if (cichlidA.getWorldBound().intersects(yAxis1.getWorldBound()) || cichlidA.getWorldBound().intersects(zYPAxis.getWorldBound()) ||  cichlidA.getWorldBound().intersects(zyPtoxEnd3.getWorldBound())
				||  cichlidA.getWorldBound().intersects(pPart.getWorldBound()) ||  cichlidA.getWorldBound().intersects(zPart.getWorldBound()) ||  cichlidA.getWorldBound().intersects(yEndtoZPart.getWorldBound())
	||  cichlidA.getWorldBound().intersects(xEndtoZPart.getWorldBound()) ||  cichlidA.getWorldBound().intersects(xxPart.getWorldBound()) ||  cichlidA.getWorldBound().intersects(finishPart.getWorldBound()));
		{
			// here is where the shit happens. you can make a rule change here.
			System.out.println("YOU HIT THE DAMN BOUNDS!!!");
		}
		*/
		
		
		/* 
		 *  collision example here
		 *  if (cichlidA.getWorldBound().contains(something something avadar)
		 *  {
		 *  	crahsevent etc. 
		 *  }
		 *  
		 *  can also do bounds work here...
		 *  let's set the bounds to be 100*20 etc. i'll think about the variables later.
		 *  
		 */
	}
	private IDisplaySystem createDisplaySystem()
	 {
	 IDisplaySystem display = new MyDisplaySystem(1000, 500, 24, 20, false,
	 "sage.renderer.jogl.JOGLRenderer");
	 System.out.print("\nWaiting for display creation...");
	 int count = 0;
	 // wait until display creation completes or a timeout occurs
	 while (!display.isCreated())
	 {
	 try
	 { Thread.sleep(10); }
	 catch (InterruptedException e)
	 { throw new RuntimeException("Display creation interrupted"); }
	 count++;
	 System.out.print("+");
	 if (count % 80 == 0) { System.out.println(); }
	 if (count > 2000) // 20 seconds (approx.)
	 { throw new RuntimeException("Unable to create display");
	 }
	 }
	 System.out.println();
	 return display ;
	 }
	
	protected void initSystem()
	{
		IDisplaySystem display = createDisplaySystem();
		setDisplaySystem(display);
		
		IInputManager inputManager = new InputManager();
		setInputManager(inputManager);
		
		ArrayList<SceneNode> gameWorld = new ArrayList<SceneNode>();
		setGameWorld(gameWorld);
		
	}
	protected void shutdown()
	{
		display.close();
		// database clear?
		Connection conn;
		try {
			conn = DriverManager.getConnection("jdbc:ucanaccess://FishPool.accdb");
			Statement s = conn.createStatement();
        	int a = s.executeUpdate("UPDATE SimulationFish set fishID = 0 where ID = 1");
        	int b = s.executeUpdate("UPDATE SimulationFish set fishID = 0 where ID = 2");
        	int c = s.executeUpdate("UPDATE SimulationFish set fishID = 0 where ID = 3");


        	conn.close();
        	// End the Rules Knowledge Session
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}
	/*
	protected void render()
	{
		renderer.setCamera(camera1);
		super.render();
	}
	*/
}
