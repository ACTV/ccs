package actv.ccs.sageTest;

import java.awt.Color;

import graphicslib3D.Matrix3D;
import graphicslib3D.Point3D;
import graphicslib3D.Vector3D;
import sage.app.BaseGame;
import sage.camera.ICamera;
import sage.display.IDisplaySystem;
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
	private TerrainBlock floor;
	
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
		
		
		// so instead of an array, it will be a group class that will hold the objects so instead of iterating ... it will go like that. so maybe i will have to change
		// the convict cichlid iterator to become a scenenode iterator.
		
		
		// this part will soon be the equivalent of spawnCichlids
		TestCichlid test = new TestCichlid();
		Matrix3D testT = test.getLocalTranslation(); // this is for position
		testT.translate(2, -2, -8);
		test.setLocalTranslation(testT);
		Matrix3D testS = test.getLocalScale(); // this is for size of object
		testS.scale(.5, .5, .7); // the scale might be too big so we will have to do the weight*.10
		test.setLocalScale(testS);
		Matrix3D testR = new Matrix3D(); // this is for the rotation of the object
		testR.rotateX(30);
		test.setLocalRotation(testR);
		addGameWorldObject(test);
		
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
				s.translate(0.001f, 0, 0);
			}
		}
	}
}
