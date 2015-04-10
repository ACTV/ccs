package actv.ccs.sageTest;

import java.awt.Color;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import actv.ccs.model.ConvictCichlid;
import graphicslib3D.Matrix3D;
import graphicslib3D.Point3D;
import graphicslib3D.Vector3D;
import sage.app.BaseGame;
import sage.camera.ICamera;
import sage.display.IDisplaySystem;
import sage.input.IInputManager;
import sage.input.InputManager;
import sage.scene.Group;
import sage.scene.SceneNode;
import sage.scene.SkyBox;
import sage.scene.shape.Line;
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
	ICamera camera;
	private SkyBox skybox;
	private Connection conn;
	private ResultSet rs, rsI;
	private TerrainBlock floor;
	private TestCichlid cichlidA, cichlidB, cichlidC;
	
	public void initGame()
	{
		initObjects();
	//	 createScene();
	//	 initTerrain();
	}
	
	protected void initObjects()
	{
		// this is for initializing objects
		display = getDisplaySystem();
		display.setTitle("sage implementation of the pain");
		camera = display.getRenderer().getCamera(); // this is for the camera where we can modify so i can put to the side of the tank etc.
		/*
		 * as much as i hate it, it's going to look like the previous team's project where our view is pretty much the closest face out of the 4 sided aquarium.
		 */
		camera.setPerspectiveFrustum(45, 1, 0.01, 1000);
		camera.setLocation(new Point3D(1, 1, 20));
		
		spawnCichlids();
		
		// so instead of an array, it will be a group class that will hold the objects so instead of iterating ... it will go like that. so maybe i will have to change
		// the convict cichlid iterator to become a scenenode iterator.
		
		
		// this part will soon be the equivalent of spawnCichlids

		
		// equivalent of spawn plants
		
		
		
		// grab tank values here, which should be the same since it's not representational data
		
		// creating x, y, z lines for a basis
		 Point3D origin = new Point3D(0,0,0);
		 Point3D xEnd = new Point3D(100,0,0);
		 Point3D yEnd = new Point3D(0,100,0);
		 Point3D zEnd = new Point3D(0,0,100);
		 Line xAxis = new Line (origin, xEnd, Color.red, 2);
		 Line yAxis = new Line (origin, yEnd, Color.green, 2);
		 Line zAxis = new Line (origin, zEnd, Color.blue, 2);
		 addGameWorldObject(xAxis); addGameWorldObject(yAxis);
		 addGameWorldObject(zAxis);
		 

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
 	
			    		// the issue with this thing is that the function is automatically called when new game starts... maybe i can call this from something..
					}
				}
				else if (id.equals("2"))
				{
					/*
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
			        	
			        	float weightW = Float.parseFloat(weight);
			        	float widthW = Float.parseFloat(width);
			        	float heightW = Float.parseFloat(height);
			        	float aggroW = Float.parseFloat(aggro);
			        	double xStartW = Double.parseDouble(xLocS);
			        	double yStartY = Double.parseDouble(yLocS);
			        	
			        	

			        	
					}
					*/
				}
				else if (id.equals("3"))
				{
				/*	rsI = s.executeQuery("SELECT * FROM [FishPool] WHERE Type='Fish C'");
					
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
			        	
			        	float weightW = Float.parseFloat(weight);
			        	float widthW = Float.parseFloat(width);
			        	float heightW = Float.parseFloat(height);
			        	float aggroW = Float.parseFloat(aggro);
			        	double xStartW = Double.parseDouble(xLocS);
			        	double yStartY = Double.parseDouble(yLocS);
			        	
			        	
		
					}
					*/
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
	/* 	  
	 		Texture northTex = TextureManager.loadTexture2D("src/a3/images/heightMapTest.JPG"); 
	 		Texture southTex = TextureManager.loadTexture2D("src/a3/images/heightMapTest.JPG");
	        Texture eastTex = TextureManager.loadTexture2D("src/a3/images/lotTest.jpg"); 
	 		Texture westTex = TextureManager.loadTexture2D("src/a3/images/lotTest.jpg");
	        Texture upTex = TextureManager.loadTexture2D("src/a3/images/clouds.jpg"); 
	 		Texture downTex = TextureManager.loadTexture2D("src/a3/images/lot_floor.jpg");  
	 		Texture testTerr = TextureManager.loadTexture2D("src/a3/images/squaresquare.bmp");
	*/
	 	   Texture testMountain = TextureManager.loadTexture2D("floorMountain.jpg");
	 	   Texture skyThing = TextureManager.loadTexture2D("sky.jpg"); 
	 	   skybox.setTexture(SkyBox.Face.North, skyThing); 
	 	   skybox.setTexture(SkyBox.Face.South, skyThing);
	       skybox.setTexture(SkyBox.Face.East, skyThing); 
	 	   skybox.setTexture(SkyBox.Face.West, skyThing); 
	// 	   skybox.setTexture(SkyBox.Face.Up, skyThing);
	      
	 	  addGameWorldObject(skybox);
	      
	 /*	   
	 try {		 
	 		AbstractHeightMap heightmap = null; 
	 
	 
	 		heightmap = new ImageBasedHeightMap(testMountain.getImage()); 
	 		heightmap.load(); 
	 
	 
	 		Vector3D scaleFactor = new Vector3D(new Point3D(1, 1, 1)); 

	 		floor = new TerrainBlock("tblock", 512, scaleFactor, heightmap.getHeightData(), new Point3D( 0, 0, 0));
	 		floor.setTexture(testMountain);
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
	private void initTerrain() // non issue
	{
		   // create height map and terrain block
			 ImageBasedHeightMap myHeightMap =
			 new ImageBasedHeightMap("floorMountain.jpg");
			 TerrainBlock imageTerrain = createTerBlock(myHeightMap);
			 // create texture and texture state to color the terrain
			 Texture grassTexture = TextureManager.loadTexture2D("stones.jpg");
			 // apply the texture to the terrain
			 imageTerrain.setTexture(grassTexture);
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
		
		for (SceneNode s: getGameWorld())
		{
			if (s instanceof TestCichlid) // here will be where the objects will have be able to move, but i will implement that later.
			{
				// for now the objects can move forward
				Matrix3D sM = s.getLocalTranslation();
				sM.translate(0, 0, .1f);
				s.setLocalTranslation(sM);
				s.updateWorldBound();
			}
		}
	}
	private IDisplaySystem createDisplaySystem()
	 {
	 IDisplaySystem display = new MyDisplaySystem(700, 300, 24, 20, false,
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
}
