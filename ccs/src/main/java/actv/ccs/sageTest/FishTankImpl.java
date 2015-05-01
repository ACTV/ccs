package actv.ccs.sageTest;

import graphicslib3D.Matrix3D;
import graphicslib3D.Point3D;
import graphicslib3D.Vector3D;

import java.awt.Color;

import sage.scene.Group;
import sage.scene.SceneNode.CULL_MODE;
import sage.scene.shape.Rectangle;
import sage.terrain.AbstractHeightMap;
import sage.terrain.ImageBasedHeightMap;
import sage.terrain.TerrainBlock;
import sage.texture.Texture;
import sage.texture.TextureManager;

public class FishTankImpl implements FishTank{
	private Group fishWalls;
	private TerrainBlock floor;
	private int cichlidCount;
	private int objectCount;
	private float temperature;
	private int timer;
	
	public FishTankImpl(){
		super();
	}
	
	public Group createFishTankWalls(){
		fishWalls = new Group();
		
		Texture groundTex = TextureManager.loadTexture2D("./aquasoil.jpg");
		Texture backWallTex = TextureManager.loadTexture2D("./background.jpg");
		// add a rectangle, and turn it into a plane
		Rectangle ground = new Rectangle(WIDTH, HEIGHT);
		ground.rotate(90, new Vector3D(1, 0, 0));
		ground.translate(101.0f, -2f, 101.0f);
//		ground.setColor(Color.orange);
		ground.setTexture(groundTex);
		fishWalls.addChild(ground);
		ground.updateWorldBound();

		
		Rectangle leftWall = new Rectangle(WIDTH, HEIGHT);
		Matrix3D leftRot = new Matrix3D();
		leftRot.rotate(0, 90, 90);
		leftWall.setLocalRotation(leftRot);
		leftWall.translate(-0.1f, 101f, 101.0f);
		leftWall.setColor(Color.blue);
		// leftWall.setCullMode(CULL_MODE.ALWAYS);
		fishWalls.addChild(leftWall);
		leftWall.updateWorldBound();

		Rectangle rightWall = new Rectangle(WIDTH, HEIGHT);
		Matrix3D rightRot = new Matrix3D();
		rightRot.rotate(0, 90, 90);
		rightWall.setLocalRotation(rightRot);
		rightWall.translate(201.0f, 101f, 101.0f);
		rightWall.setColor(Color.blue);
		// rightWall.setCullMode(CULL_MODE.ALWAYS);
		fishWalls.addChild(rightWall);
		rightWall.updateWorldBound();

		Rectangle backWall = new Rectangle(WIDTH, HEIGHT);
		Matrix3D backRot = new Matrix3D();
		backRot.rotate(0, 0, 0);
		backWall.setLocalRotation(backRot);
		backWall.translate(101.0f, 101.0f, -0.10f);
//		backWall.setColor(Color.blue);
		backWall.setTexture(backWallTex);
		// backWall.setCullMode(CULL_MODE.ALWAYS);
		fishWalls.addChild(backWall);
		backWall.updateWorldBound();

		Rectangle ceiling = new Rectangle(WIDTH, HEIGHT);
		Matrix3D ceilingRot = new Matrix3D();
		ceilingRot.rotate(90, 0, 0);
		ceiling.setLocalRotation(ceilingRot);
		ceiling.translate(101.0f, 201f, 101.0f);
		ceiling.setColor(Color.blue);
		// ceiling.setCullMode(CULL_MODE.ALWAYS);
		fishWalls.addChild(ceiling);
		ceiling.updateWorldBound();

		// find transparency for this
		Rectangle frontWall = new Rectangle(WIDTH, HEIGHT);
		Matrix3D frontRot = new Matrix3D();
		frontRot.rotate(0, 180, 0);
		frontWall.setLocalRotation(frontRot);
		frontWall.translate(101.0f, 101.0f, 201.0f);
		frontWall.setCullMode(CULL_MODE.ALWAYS);
		fishWalls.addChild(frontWall);
		frontWall.updateWorldBound();
		
		return fishWalls;
	}
	
	public TerrainBlock createTankTerrain(){
		AbstractHeightMap heightMap = null;
		heightMap = new ImageBasedHeightMap("mountains512.jpg");
		heightMap.load();

		Vector3D scaleFactor = new Vector3D(new Point3D(1, 1, 1));
		floor = new TerrainBlock("floor", 512, scaleFactor,
				heightMap.getHeightData(), new Point3D(0, 0, 0));
		
		try {
			Matrix3D floorT = floor.getLocalTranslation();
			floorT.translate(0.0f, 0.0f, 0.0f);
			floor.setLocalTranslation(floorT);
			Matrix3D floorScale = floor.getLocalScale();
			floorScale.scale(0.42f, 0.5f, 0);
			floor.setLocalScale(floorScale);
			Matrix3D p1Rot = new Matrix3D();
			p1Rot.rotateX(-90);
			floor.setLocalRotation(p1Rot);
			Texture grassTexture = TextureManager
					.loadTexture2D("sky.jpg");
			grassTexture.setApplyMode(sage.texture.Texture.ApplyMode.Replace);

			floor.setTexture(grassTexture);
		} catch (Exception pe) {
			pe.printStackTrace();
		}
		
		return floor;
	}

	public int getCichlidCount() {
		return cichlidCount;
	}

	public void setCichlidCount(int cichlidCount) {
		this.cichlidCount = cichlidCount;
	}

	public int getObjectCount() {
		return objectCount;
	}

	public void setObjectCount(int objectCount) {
		this.objectCount = objectCount;
	}

	public float getTemperature() {
		return temperature;
	}

	public void setTemperature(float temperature) {
		this.temperature = temperature;
	}

	public int getTimer() {
		return timer;
	}

	public void setTimer(int timer) {
		this.timer = timer;
	}

}
