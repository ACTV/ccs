package actv.ccs.sageTest;

import java.awt.Color;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import actv.ccs.listener.RuleEngineRunner;
import actv.ccs.model.CCSMemoryObject;
import actv.ccs.model.type.FishState;
import actv.ccs.sageTest.actions.BackwardAction;
import actv.ccs.sageTest.actions.DownBackAction;
import actv.ccs.sageTest.actions.DownForwardAction;
import actv.ccs.sageTest.actions.ForwardAction;
import actv.ccs.sageTest.actions.LeftAction;
import actv.ccs.sageTest.actions.QuitAction;
import actv.ccs.sageTest.actions.RightAction;
import actv.ccs.sageTest.actions.UpBackAction;
import actv.ccs.sageTest.actions.UpForwardAction;
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
import sage.model.loader.OBJLoader;
import sage.renderer.IRenderer;
import sage.scene.*;
import sage.scene.SceneNode.CULL_MODE;
import sage.scene.bounding.BoundingSphere;
import sage.scene.shape.Cube;
import sage.scene.shape.Cylinder;
import sage.scene.shape.Line;
import sage.scene.shape.Pyramid;
import sage.scene.shape.Rectangle;
import sage.scene.state.RenderState;
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
	// private Pot largePot, mediumPot, smallPot;
	// private Plant largePlant, mediumPlant, smallPlant;
	private TriMesh largePlant, mediumPlant, smallPlant, largePot,
			mediumPot, smallPot;
	private ICamera camera;
	private CameraOrbit cc;
	private SkyBox skybox;
	private Connection conn;
	private ResultSet rs, rsI;
	private TerrainBlock floor, leftWindowPane, BackWindowPane,
			RightWindowPane, ceilingWater;
	private Texture skyThing;
	private Rectangle ground, leftWall, rightWall, ceiling, backWall,
			frontWall;
	private TestCichlid cichlidA, cichlidB, cichlidC;
	private SceneNode cameraGuy;
	private Line yAxis1, zYPAxis, zyPtoxEnd3, pPart, zPart, yEndtoZPart,
			xEndtoZPart, xxPart, finishPart;
	private RuleEngineRunner runner;
	private ArrayList<CCSMemoryObject> objs;

	{
		objs = new ArrayList<CCSMemoryObject>();
	}

	public void initGame() {
		initObjects();
		spawnCichlids();
		spawnObjects();
		createPerson();
		// createScene();
		initActions();
		// createFishTank();
		createFishTankWalls();
		startRunner();

	}

	private void startRunner() {
		if (runner == null) {
			runner = RuleEngineRunner.getInstance();
			runner.newMap(objs);
			runner.start();
		}
	}
	
	private void stopRunner(){
		if(runner.isRunning()){
			try{
				runner.closeSession();
				runner.join();
			}catch(InterruptedException e){
				throw new RuntimeException("Unable to end the rule session thread!");
			}
		}
		
	}

	protected void initObjects() {
		// this is for initializing objects
		display = getDisplaySystem();
		display.setTitle("sage implementation of the pain");

		camera = display.getRenderer().getCamera();
		camera.setPerspectiveFrustum(45, 1, 0.01, 1000);
		camera.setLocation(new Point3D(1, 1, 20));
		/*
		 * as much as i hate it, it's going to look like the previous team's
		 * project where our view is pretty much the closest face out of the 4
		 * sided aquarium.
		 */

		// so instead of an array, it will be a group class that will hold the
		// objects so instead of iterating ... it will go like that. so maybe i
		// will have to change
		// the convict cichlid iterator to become a scenenode iterator.

		// this part will soon be the equivalent of spawnCichlids

		// equivalent of spawn plants

		// grab tank values here, which should be the same since it's not
		// representational data

		// creating x, y, z lines for a basis
		Point3D origin = new Point3D(0, 0, 0);
		Point3D xEnd1 = new Point3D(200, 200, 0);
		Point3D xEnd3 = new Point3D(200, 200, 200);
		Point3D xEnd2 = new Point3D(200, 0, 200);
		Point3D zyP = new Point3D(0, 200, 200);
		Point3D xEnd = new Point3D(200, 0, 0);
		Point3D yEnd = new Point3D(0, 200, 0);
		Point3D zEnd = new Point3D(0, 0, 200);

		// base
		Line xAxis = new Line(origin, xEnd, Color.red, 2);
		Line yAxis = new Line(origin, yEnd, Color.green, 2);
		Line zAxis = new Line(origin, zEnd, Color.blue, 2); // Base

		// Line xAxis1 = new Line (xEnd1, xEnd3, Color.cyan, 2);
		yAxis1 = new Line(xEnd2, xEnd3, Color.GRAY, 2);
		zyPtoxEnd3 = new Line(new Point3D(200, 0, 0), new Point3D(200, 200, 0),
				Color.BLUE, 2);
		pPart = new Line(new Point3D(200, 0, 0), new Point3D(200, 0, 200),
				Color.green, 2);
		finishPart = new Line(new Point3D(0, 200, 0), new Point3D(200, 200, 0),
				Color.PINK, 2);
		yEndtoZPart = new Line(yEnd, new Point3D(0, 200, 200), Color.orange, 2);
		xEndtoZPart = new Line(new Point3D(0, 200, 200), new Point3D(200, 200,
				200), Color.orange, 2);
		xxPart = new Line(new Point3D(200, 200, 0), new Point3D(200, 200, 200),
				Color.magenta, 2);
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

	public void createPerson() {
		cameraGuy = new CameraGuy();
		cameraGuy.translate(100, 100, 500);
		cameraGuy.scale(-1, -1, -1);
		cameraGuy.rotate(180, new Vector3D(0, 1, 0));
		addGameWorldObject(cameraGuy);
		cameraGuy.updateWorldBound();

	}

	public void spawnObjects() {
		try {
			conn = DriverManager
					.getConnection("jdbc:ucanaccess://FishPool.accdb");

			Statement s = conn.createStatement();
			rs = s.executeQuery("SELECT objID FROM [SimulationObjects]");
			while (rs.next()) {
				String id = rs.getString("objID"); // Field from database ex.
													// largepot etc.
				int idS = Integer.parseInt(id);

				System.out.println(idS);

				if (id.equals("1")) {
					rsI = s.executeQuery("SELECT * FROM [Objects] WHERE Name='Large Plant'");
					while (rsI.next()) {
						String name = rsI.getString("Name"); // Field from
																// database
						String type = rsI.getString("Type");
						String length = rsI.getString("Length");
						String width = rsI.getString("Width");
						String height = rsI.getString("Height");
						String xLocS = rsI.getString("StartingXPos");
						String yLocS = rsI.getString("StartingYPos");
						String zLocS = rsI.getString("StartingZPos");

						float lengthW = Float.parseFloat(length);
						float widthW = Float.parseFloat(width);
						float heightW = Float.parseFloat(height);
						double xStartW = Double.parseDouble(xLocS);
						double yStartY = Double.parseDouble(yLocS);
						double zStartZ = Double.parseDouble(zLocS);

						OBJLoader loader = new OBJLoader();
						largePlant = loader
								.loadModel("plantBlend.obj");
						largePlant.setName(name);
						Matrix3D largePlantT = largePlant.getLocalTranslation(); // this
																					// is
																					// for
																					// position
						largePlantT.translate(xStartW, yStartY, zStartZ);
						largePlant.setLocalTranslation(largePlantT);
						Matrix3D largePlantS = largePlant.getLocalScale(); // this
																			// is
																			// for
																			// size
																			// of
																			// object
						largePlantS.scale(lengthW, widthW, heightW); // the
																		// scale
																		// might
																		// be
																		// too
																		// big
																		// so we
																		// largePlant.setLocalScale(largePlantS);
						largePlant.setLocalScale(largePlantS);

						addGameWorldObject(largePlant);
						largePlant.updateLocalBound();
						largePlant.updateGeometricState(0, true);
						largePlant.updateWorldBound();

					}
				} else if (id.equals("2")) {
					rsI = s.executeQuery("SELECT * FROM [Objects] WHERE Name='Medium Plant'");
					while (rsI.next()) {
						String name = rsI.getString("Name"); // Field from
																// database
						String type = rsI.getString("Type");
						String length = rsI.getString("Length");
						String width = rsI.getString("Width");
						String height = rsI.getString("Height");
						String xLocS = rsI.getString("StartingXPos");
						String yLocS = rsI.getString("StartingYPos");
						String zLocS = rsI.getString("StartingZPos");

						float lengthW = Float.parseFloat(length);
						float widthW = Float.parseFloat(width);
						float heightW = Float.parseFloat(height);
						double xStartW = Double.parseDouble(xLocS);
						double yStartY = Double.parseDouble(yLocS);
						double zStartZ = Double.parseDouble(zLocS);

						OBJLoader loader1 = new OBJLoader();
						mediumPlant = loader1
								.loadModel("plantBlend.obj");
						mediumPlant.setName(name);
						Matrix3D mediumPlantT = mediumPlant
								.getLocalTranslation(); // this is for position
						mediumPlantT.translate(xStartW, yStartY, zStartZ);
						mediumPlant.setLocalTranslation(mediumPlantT);
						Matrix3D mediumPlantS = mediumPlant.getLocalScale(); // this
																				// is
																				// for
																				// size
																				// of
																				// object
						mediumPlantS.scale(lengthW, widthW, heightW); // the
																		// scale
																		// might
																		// be
																		// too
																		// big
																		// so we
																		// largePlant.setLocalScale(largePlantS);
						mediumPlant.setLocalScale(mediumPlantS);

						addGameWorldObject(mediumPlant);
						mediumPlant.updateLocalBound();
						mediumPlant.updateGeometricState(0, true);
						mediumPlant.updateWorldBound();

					}
				} else if (id.equals("3")) {
					rsI = s.executeQuery("SELECT * FROM [Objects] WHERE Name='Small Plant'");
					while (rsI.next()) {
						String name = rsI.getString("Name"); // Field from
																// database
						String type = rsI.getString("Type");
						String length = rsI.getString("Length");
						String width = rsI.getString("Width");
						String height = rsI.getString("Height");
						String xLocS = rsI.getString("StartingXPos");
						String yLocS = rsI.getString("StartingYPos");
						String zLocS = rsI.getString("StartingZPos");

						float lengthW = Float.parseFloat(length);
						float widthW = Float.parseFloat(width);
						float heightW = Float.parseFloat(height);
						double xStartW = Double.parseDouble(xLocS);
						double yStartY = Double.parseDouble(yLocS);
						double zStartZ = Double.parseDouble(zLocS);

						OBJLoader loader2 = new OBJLoader();
						smallPlant = loader2
								.loadModel("plantBlend.obj");
						smallPlant.setName(name);
						Matrix3D smallPlantT = smallPlant.getLocalTranslation(); // this
																					// is
																					// for
																					// position
						smallPlantT.translate(xStartW, yStartY, zStartZ);
						smallPlant.setLocalTranslation(smallPlantT);
						Matrix3D smallPlantS = smallPlant.getLocalScale(); // this
																			// is
																			// for
																			// size
																			// of
																			// object
						smallPlantS.scale(lengthW, widthW, heightW); // the
																		// scale
																		// might
																		// be
																		// too
																		// big
																		// so we
																		// largePlant.setLocalScale(largePlantS);
						smallPlant.setLocalScale(smallPlantS);

						addGameWorldObject(smallPlant);
						smallPlant.updateLocalBound();
						smallPlant.updateGeometricState(0, true);
						smallPlant.updateWorldBound();

					}
				} else if (id.equals("4")) {
					rsI = s.executeQuery("SELECT * FROM [Objects] WHERE Name='Large Pot'");
					while (rsI.next()) {
						String name = rsI.getString("Name"); // Field from
																// database
						String type = rsI.getString("Type");
						String length = rsI.getString("Length");
						String width = rsI.getString("Width");
						String height = rsI.getString("Height");
						String xLocS = rsI.getString("StartingXPos");
						String yLocS = rsI.getString("StartingYPos");
						String zLocS = rsI.getString("StartingZPos");

						float lengthW = Float.parseFloat(length);
						float widthW = Float.parseFloat(width);
						float heightW = Float.parseFloat(height);
						double xStartW = Double.parseDouble(xLocS);
						double yStartY = Double.parseDouble(yLocS);
						double zStartZ = Double.parseDouble(zLocS);

						OBJLoader loader3 = new OBJLoader();
						largePot =  loader3
								.loadModel("potBlend.obj");
						largePot.setName(name);
						Matrix3D largePotT = largePot.getLocalTranslation(); // this
																				// is
																				// for
																				// position
						largePotT.translate(xStartW, yStartY, zStartZ);
						largePot.setLocalTranslation(largePotT);
						Matrix3D largePotS = largePlant.getLocalScale(); // this
																			// is
																			// for
																			// size
																			// of
																			// object
						largePotS.scale(lengthW, widthW, heightW); // the scale
																	// might be
																	// too big
																	// so we
																	// largePlant.setLocalScale(largePlantS);
						largePot.setLocalScale(largePotS);

						addGameWorldObject(largePot);
						largePot.updateLocalBound();
						largePot.updateGeometricState(0, true);
						largePot.updateWorldBound();

					}
				} else if (id.equals("5")) {
					rsI = s.executeQuery("SELECT * FROM [Objects] WHERE Name='Medium Pot'");
					while (rsI.next()) {
						String name = rsI.getString("Name"); // Field from
																// database
						String type = rsI.getString("Type");
						String length = rsI.getString("Length");
						String width = rsI.getString("Width");
						String height = rsI.getString("Height");
						String xLocS = rsI.getString("StartingXPos");
						String yLocS = rsI.getString("StartingYPos");
						String zLocS = rsI.getString("StartingZPos");

						float lengthW = Float.parseFloat(length);
						float widthW = Float.parseFloat(width);
						float heightW = Float.parseFloat(height);
						double xStartW = Double.parseDouble(xLocS);
						double yStartY = Double.parseDouble(yLocS);
						double zStartZ = Double.parseDouble(zLocS);

						OBJLoader loader4 = new OBJLoader();
						mediumPot = loader4
								.loadModel("potBlend.obj");
						mediumPot.setName(name);
						Matrix3D mediumPotT = mediumPot.getLocalTranslation(); // this
																				// is
																				// for
																				// position
						mediumPotT.translate(xStartW, yStartY, zStartZ);
						mediumPot.setLocalTranslation(mediumPotT);
						Matrix3D mediumPotS = largePlant.getLocalScale(); // this
																			// is
																			// for
																			// size
																			// of
																			// object
						mediumPotS.scale(lengthW, widthW, heightW); // the scale
																	// might be
																	// too big
																	// so we
																	// largePlant.setLocalScale(largePlantS);
						mediumPot.setLocalScale(mediumPotS);

						addGameWorldObject(mediumPot);
						mediumPot.updateLocalBound();
						mediumPot.updateGeometricState(0, true);
						mediumPot.updateWorldBound();

					}
				} else if (id.equals("6")) {
					rsI = s.executeQuery("SELECT * FROM [Objects] WHERE Name='Small Pot'");
					while (rsI.next()) {
						String name = rsI.getString("Name"); // Field from
																// database
						String type = rsI.getString("Type");
						String length = rsI.getString("Length");
						String width = rsI.getString("Width");
						String height = rsI.getString("Height");
						String xLocS = rsI.getString("StartingXPos");
						String yLocS = rsI.getString("StartingYPos");
						String zLocS = rsI.getString("StartingZPos");

						float lengthW = Float.parseFloat(length);
						float widthW = Float.parseFloat(width);
						float heightW = Float.parseFloat(height);
						double xStartW = Double.parseDouble(xLocS);
						double yStartY = Double.parseDouble(yLocS);
						double zStartZ = Double.parseDouble(zLocS);

						OBJLoader loader5 = new OBJLoader();
						smallPot = loader5
								.loadModel("potBlend.obj");
						smallPot.setName(name);
						Matrix3D smallPotT = smallPot.getLocalTranslation(); // this
																				// is
																				// for
																				// position
						smallPotT.translate(xStartW, yStartY, zStartZ);
						smallPot.setLocalTranslation(smallPotT);
						Matrix3D smallPotS = smallPot.getLocalScale(); // this
																		// is
																		// for
																		// size
																		// of
																		// object
						smallPotS.scale(lengthW, widthW, heightW); // the scale
																	// might be
																	// too big
																	// so we
																	// largePlant.setLocalScale(largePlantS);
						smallPot.setLocalScale(smallPotS);

						addGameWorldObject(smallPot);
						smallPot.updateLocalBound();
						smallPot.updateGeometricState(0, true);
						smallPot.updateWorldBound();

					}
				}
			}
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void spawnCichlids() {
		try {
			conn = DriverManager
					.getConnection("jdbc:ucanaccess://FishPool.accdb");

			Statement s = conn.createStatement();
			rs = s.executeQuery("SELECT fishID FROM [SimulationFish]");
			while (rs.next()) {
				String id = rs.getString("fishID"); // Field from database ex.
													// FishA, FishB
				int idS = Integer.parseInt(id);

				System.out.println(idS);

				if (id.equals("1")) {
					rsI = s.executeQuery("SELECT * FROM [FishPool] WHERE Type='Fish A'");
					while (rsI.next()) {
						String name = rsI.getString("Type"); // Field from
																// database ex.
																// FishA, FishB
						String weight = rsI.getString("Weight");
						String width = rsI.getString("Width");
						String height = rsI.getString("Height");
						String gender = rsI.getString("Gender");
						String aggro = rsI.getString("AggroLevel"); // default
																	// to 10
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
						//TODO: temporary
						cichlidA.setBaseSpeed(3f);
						cichlidA.setBaseCautionLevel(4f);
						cichlidA.setBaseCautionLevel(4f);
						cichlidA.setDirection(new Vector3D(1,1,0));
						cichlidA.setlocation(xStartW,  yStartY, zStartZ);
						cichlidA.setState(FishState.IDLE);
						//
						Matrix3D cichlidAT = cichlidA.getLocalTranslation(); // this
																				// is
																				// for
																				// position
						cichlidAT.translate(xStartW, yStartY, zStartZ);
						cichlidA.setLocalTranslation(cichlidAT);
						Matrix3D cichlidAS = cichlidA.getLocalScale(); // this
																		// is
																		// for
																		// size
																		// of
																		// object
						cichlidAS.scale(widthW * weightW * .100, heightW
								* weightW * .100, 0); // the scale might be too
														// big so we will have
														// to do the weight*.10
						cichlidA.setLocalScale(cichlidAS);
						Matrix3D cichlidAR = new Matrix3D(); // this is for the
																// rotation of
																// the object
						cichlidAR.rotateX(30);
						cichlidA.setLocalRotation(cichlidAR);
						addGameWorldObject(cichlidA);
						if (cichlidA == null) {
							System.out.println("cichlidA is null!?!");
						} else {
							objs.add(cichlidA);
						}
						cichlidA.updateWorldBound();

					}
				} else if (id.equals("2")) {
					rsI = s.executeQuery("SELECT * FROM [FishPool] WHERE Type='Fish B'");

					while (rsI.next()) {
						String name = rsI.getString("Type"); // Field from
																// database ex.
																// FishA, FishB
						String weight = rsI.getString("Weight");
						String width = rsI.getString("Width");
						String height = rsI.getString("Height");
						String gender = rsI.getString("Gender");
						String aggro = rsI.getString("AggroLevel"); // default
																	// to 10
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
						//TODO: temporary
						cichlidB.setBaseSpeed(3f);
						cichlidB.setBaseCautionLevel(4f);
						cichlidB.setDirection(new Vector3D(1,1,1));
						cichlidB.setlocation(xStartW,  yStartY, zStartZ);
						cichlidB.setState(FishState.IDLE);
						//
						Matrix3D cichlidBT = cichlidB.getLocalTranslation(); // this
																				// is
																				// for
																				// position
						cichlidBT.translate(xStartW, yStartY, zStartZ);
						cichlidB.setLocalTranslation(cichlidBT);
						Matrix3D cichlidBS = cichlidB.getLocalScale(); // this
																		// is
																		// for
																		// size
																		// of
																		// object
						cichlidBS.scale(widthW * weightW * .100, heightW
								* weightW * .100, 0); // the scale might be too
														// big so we will have
														// to do the weight*.10
						cichlidB.setLocalScale(cichlidBS);
						Matrix3D cichlidBR = new Matrix3D(); // this is for the
																// rotation of
																// the object
						cichlidBR.rotateX(30);
						cichlidB.setLocalRotation(cichlidBR);
						addGameWorldObject(cichlidB);
						objs.add(cichlidB);
						cichlidB.updateWorldBound();

						// the issue with this thing is that the function is
						// automatically called when new game starts... maybe i
						// can call this from something..
					}
				} else if (id.equals("3")) {
					rsI = s.executeQuery("SELECT * FROM [FishPool] WHERE Type='Fish C'");

					while (rsI.next()) {
						String name = rsI.getString("Type"); // Field from
																// database ex.
																// FishA, FishB
						String weight = rsI.getString("Weight");
						String width = rsI.getString("Width");
						String height = rsI.getString("Height");
						String gender = rsI.getString("Gender");
						String aggro = rsI.getString("AggroLevel"); // default
																	// to 10
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
						
						//TODO: temporary
						cichlidC.setBaseSpeed(3f);
						cichlidC.setBaseCautionLevel(4f);
						cichlidC.setDirection(new Vector3D(-.5,.8,.1));
						cichlidC.setlocation(xStartW,  yStartY, zStartZ);
						cichlidC.setState(FishState.IDLE);
						//
						
						Matrix3D cichlidCT = cichlidC.getLocalTranslation(); // this
																				// is
																				// for
																				// position
						cichlidCT.translate(xStartW, yStartY, zStartZ);
						cichlidC.setLocalTranslation(cichlidCT);
						Matrix3D cichlidCS = cichlidC.getLocalScale(); // this
																		// is
																		// for
																		// size
																		// of
																		// object
						cichlidCS.scale(widthW * weightW * .100, heightW
								* weightW * .100, 0); // the scale might be too
														// big so we will have
														// to do the weight*.10
						cichlidC.setLocalScale(cichlidCS);
						Matrix3D cichlidCR = new Matrix3D(); // this is for the
																// rotation of
																// the object
						cichlidCR.rotateX(30);
						cichlidC.setLocalRotation(cichlidCR);
						addGameWorldObject(cichlidC);
						objs.add(cichlidC);
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

	private void createScene() // the scene is the background of the fish tank
								// ... non issue for now.
	{
		skybox = new SkyBox("SkyBox", 10.0f, 10.0f, 10.0f);

		// Texture testMountain =
		// TextureManager.loadTexture2D("src/main/java/actv/ccs/sageTest/Images/floorMountain.bmp");
		Texture thingSky = TextureManager.loadTexture2D("sky.jpg");
		skybox.setTexture(SkyBox.Face.North, thingSky);
		skybox.setTexture(SkyBox.Face.South, thingSky);
		skybox.setTexture(SkyBox.Face.East, thingSky);
		skybox.setTexture(SkyBox.Face.West, thingSky);
		skybox.setTexture(SkyBox.Face.Up, thingSky);

		addGameWorldObject(skybox);

		/*
		 * try { AbstractHeightMap heightmap = null;
		 * 
		 * 
		 * heightmap = new ImageBasedHeightMap(testMountain.getImage());
		 * heightmap.load();
		 * 
		 * 
		 * Vector3D scaleFactor = new Vector3D(new Point3D(1, 1, 1));
		 * 
		 * floor = new TerrainBlock("tblock", 512, scaleFactor,
		 * heightmap.getHeightData(), new Point3D( 0, 0, 0)); //
		 * floor.setTexture(skyThing); Matrix3D p1LotT =
		 * floor.getLocalTranslation(); p1LotT.translate(0.0f, -0.5f, 0.0f);
		 * floor.setLocalTranslation(p1LotT); Matrix3D p1Scale =
		 * floor.getLocalScale(); p1Scale.scale(10f, 10f, 0);
		 * floor.setLocalScale(p1Scale); Matrix3D p1Rot = new Matrix3D();
		 * p1Rot.rotateX(-90); floor.setLocalRotation(p1Rot);
		 * 
		 * addGameWorldObject(floor); } catch (Exception e) {
		 * e.printStackTrace(); }
		 */

	}

	private void initActions() {
		im = getInputManager();
		String kbName = im.getKeyboardName(); // error here. it shouldn't be
												// null
		String mName = im.getMouseName();
		// sFindComponents f = new FindComponents();

		cc = new CameraOrbit(camera, cameraGuy, im, mName);

		System.out.println("controller: " + mName);

		// for this area, need to do a checker if A and B and C are called...
		IAction moveForwardA = new ForwardAction(cichlidA);
		// IAction moveForwardB = new ForwardAction(cichlidB);
		// IAction moveForwardC = new ForwardAction(cichlidC);
		// IAction moveForwardO = new ForwardAction(cameraGuy);

		IAction moveBackA = new BackwardAction(cichlidA);
		// IAction moveBackB = new BackwardAction(cichlidB);
		// IAction moveBackC = new BackwardAction(cichlidC);
		// IAction moveBackO = new BackwardAction(cameraGuy);

		IAction moveLeftA = new LeftAction(cichlidA);
		// IAction moveLeftB = new LeftAction(cichlidB);
		// IAction moveLeftC = new LeftAction(cichlidC);
		// IAction moveLeftO = new LeftAction(cameraGuy);

		IAction moveRightA = new RightAction(cichlidA);
		// IAction moveRightB = new RightAction(cichlidB);
		// IAction moveRightC = new RightAction(cichlidC);
		// IAction moveRightO = new RightAction(cameraGuy);
		
		IAction upForwardA = new UpForwardAction(cichlidA);
		IAction upBackA = new UpBackAction(cichlidA);
		IAction downForwardA = new DownForwardAction(cichlidA);
		IAction downBackA = new DownBackAction(cichlidA);
		IAction quitGame = new QuitAction(this);

		im.associateAction(kbName,
				net.java.games.input.Component.Identifier.Key.ESCAPE, quitGame,
				IInputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
		// here is the movement options of the character ..
		im.associateAction(kbName,
				net.java.games.input.Component.Identifier.Key.W, moveForwardA,
				IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateAction(kbName,
				net.java.games.input.Component.Identifier.Key.S, moveBackA,
				IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateAction(kbName,
				net.java.games.input.Component.Identifier.Key.A, moveLeftA,
				IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateAction(kbName,
				net.java.games.input.Component.Identifier.Key.D, moveRightA,
				IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);

		
		im.associateAction(kbName,
				net.java.games.input.Component.Identifier.Key.NUMPAD9, upForwardA,
				IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateAction(kbName,
				net.java.games.input.Component.Identifier.Key.NUMPAD7, upBackA,
				IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateAction(kbName,
				net.java.games.input.Component.Identifier.Key.NUMPAD3, downForwardA,
				IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateAction(kbName,
				net.java.games.input.Component.Identifier.Key.NUMPAD1, downBackA,
				IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
	}

	public void createFishTankWalls() {
		 Texture tex = TextureManager.loadTexture2D("sky.jpg");
		// add a rectangle, and turn it into a plane
		ground = new Rectangle(200, 200);
		ground.rotate(90, new Vector3D(1, 0, 0));
		ground.translate(101.0f, -2f, 101.0f);
		ground.setColor(Color.orange);
		/*
		// testing out new stuff
		TextureState grassState;
		tex.setApplyMode(sage.texture.Texture.ApplyMode.Replace);
		grassState = (TextureState) display.getRenderer().createRenderState(RenderState.RenderStateType.Texture);
		grassState.setTexture(tex, 0);
		grassState.setEnabled(true);
		
		ground.setRenderState(grassState);
		*/
		addGameWorldObject(ground);
		ground.updateWorldBound();

		leftWall = new Rectangle(200, 200);
		Matrix3D leftRot = new Matrix3D();
		leftRot.rotate(0, 90, 90);
		leftWall.setLocalRotation(leftRot);
		leftWall.translate(-0.1f, 101f, 101.0f);
		leftWall.setColor(Color.blue);
		// leftWall.setCullMode(CULL_MODE.ALWAYS);
		addGameWorldObject(leftWall);
		leftWall.updateWorldBound();

		rightWall = new Rectangle(200, 200);
		Matrix3D rightRot = new Matrix3D();
		rightRot.rotate(0, 90, 90);
		rightWall.setLocalRotation(rightRot);
		rightWall.translate(201.0f, 101f, 101.0f);
		rightWall.setColor(Color.blue);
		// rightWall.setCullMode(CULL_MODE.ALWAYS);
		addGameWorldObject(rightWall);
		rightWall.updateWorldBound();

		backWall = new Rectangle(200, 200);
		Matrix3D backRot = new Matrix3D();
		backRot.rotate(0, 0, 0);
		backWall.setLocalRotation(backRot);
		backWall.translate(101.0f, 101.0f, -0.10f);
		backWall.setColor(Color.blue);
		// backWall.setCullMode(CULL_MODE.ALWAYS);
		addGameWorldObject(backWall);
		backWall.updateWorldBound();

		ceiling = new Rectangle(200, 200);
		Matrix3D ceilingRot = new Matrix3D();
		ceilingRot.rotate(90, 0, 0);
		ceiling.setLocalRotation(ceilingRot);
		ceiling.translate(101.0f, 201f, 101.0f);
		ceiling.setColor(Color.blue);
		// ceiling.setCullMode(CULL_MODE.ALWAYS);
		addGameWorldObject(ceiling);
		ceiling.updateWorldBound();

		// find transparency for this
		frontWall = new Rectangle(200, 200);
		Matrix3D frontRot = new Matrix3D();
		frontRot.rotate(0, 180, 0);
		frontWall.setLocalRotation(frontRot);
		frontWall.translate(101.0f, 101.0f, 201.0f);
		frontWall.setCullMode(CULL_MODE.ALWAYS);
		addGameWorldObject(frontWall);
		frontWall.updateWorldBound();

	}

	// private TerrainBlock floor, leftWindowPane, BackWindowPane,
	// RightWindowPane, ceilingWater;
	public void createFishTank() // issue with this.
	{
		Texture walls = TextureManager.loadTexture2D("lotTest.bmp");
		skyThing = TextureManager.loadTexture2D("lot_floor.jpg");
		AbstractHeightMap heightMap = null;
		heightMap = new ImageBasedHeightMap("floorMountain.jpg");
		heightMap.load();

		Vector3D scaleFactor = new Vector3D(new Point3D(1, 1, 1));

		try {
			floor = new TerrainBlock("floor", 512, scaleFactor,
					heightMap.getHeightData(), new Point3D(0, 0, 0));
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
					.loadTexture2D("lot_floor.jpg");
			grassTexture.setApplyMode(sage.texture.Texture.ApplyMode.Replace);

			floor.setTexture(grassTexture);
		} catch (Exception pe) {
			pe.printStackTrace();
		}
		addGameWorldObject(floor);
	}

	public void update(float time) // this will be where the objects will move
	{
		super.update(time);
		cc.update(time);
		for (SceneNode s : getGameWorld()) {
			if (s instanceof TestCichlid) // here will be where the objects will
											// have be able to move, but i will
											// implement that later.
			{
				if (s == cichlidA) {
					// s.translate(0, 0, .1f);
					// s.updateWorldBound();
					Point3D loc = new Point3D(s.getWorldTranslation().getCol(3));
					if (loc.getX() > 200 || loc.getX() < 0.0)
					{
						System.out.println("X BOUNDS");
						
					}
					if (loc.getY() > 200 || loc.getY() < 0.0)
					{
						System.out.println("Y BOUNDS");
						
					}
					if (loc.getZ() > 200 || loc.getZ() < 0.0)
					{
						System.out.println("Z BOUNDS");
						
					}
					
					
				}
				if (s == cichlidB) {
					// call move stuff here
					Point3D loc = new Point3D(s.getWorldTranslation().getCol(3));
					if (loc.getX() > 200 || loc.getX() < 0.0)
					{
						System.out.println("X BOUNDS");
						
					}
					if (loc.getY() > 200 || loc.getY() < 0.0)
					{
						System.out.println("Y BOUNDS");
						
					}
					if (loc.getZ() > 200 || loc.getZ() < 0.0)
					{
						System.out.println("Z BOUNDS");
						
					}
				}
				if (s == cichlidC) {
					// call move stuff here
					Point3D loc = new Point3D(s.getWorldTranslation().getCol(3));
					if (loc.getX() > 200 || loc.getX() < 0.0)
					{
						System.out.println("X BOUNDS");
						
					}
					if (loc.getY() > 200 || loc.getY() < 0.0)
					{
						System.out.println("Y BOUNDS");
						
					}
					if (loc.getZ() > 200 || loc.getZ() < 0.0)
					{
						System.out.println("Z BOUNDS");
						
					}
				}

			}

		}

		/*
		 * COMMENTING OUT. WILL FIX LATER - ALBERT collision example if
		 * (tpt.getWorldBound().intersects(p1.getWorldBound()) &&
		 * collidedWTeapot == false){ collidedWTeapot = true; numCrashes++;
		 * score1 += 100; CrashEvent newCrash = new CrashEvent(numCrashes);
		 * removeGameWorldObject(tpt); eventMgr.triggerEvent(newCrash); }
		 */
		
		
		

		try {
			conn = DriverManager
					.getConnection("jdbc:ucanaccess://FishPool.accdb");

			Statement s = conn.createStatement();
			rs = s.executeQuery("SELECT fishID FROM [SimulationFish]");
			while (rs.next()) {
				String id = rs.getString("fishID"); // Field from database ex.
													// FishA, FishB
				int idS = Integer.parseInt(id);

				/*
				 * public Point3D returnTargetPos() { return new
				 * Point3D(target.getWorldTranslation().getCol(3)); }
				 */

				if (id.equals("1")) {

					// object collision example
//					if (((TriMesh)largePlant).getWorldBound().intersects(
//							cichlidA.getWorldBound())) {
//						System.out.println("MAKE THIS THING GO SLOWER!!!");
//						cichlidA.translate(0, 0, -1f); // make the cichlid go in
//														// reverse
//						cichlidA.updateWorldBound();
//					}

				} else if (id.equals("2")) {


				} else if (id.equals("3")) {


				}

			}
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

	}

	private IDisplaySystem createDisplaySystem() {
		IDisplaySystem display = new MyDisplaySystem(1000, 500, 24, 20, false,
				"sage.renderer.jogl.JOGLRenderer");
		System.out.print("\nWaiting for display creation...");
		int count = 0;
		// wait until display creation completes or a timeout occurs
		while (!display.isCreated()) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				throw new RuntimeException("Display creation interrupted");
			}
			count++;
			System.out.print("+");
			if (count % 80 == 0) {
				System.out.println();
			}
			if (count > 2000) // 20 seconds (approx.)
			{
				throw new RuntimeException("Unable to create display");
			}
		}
		System.out.println();
		return display;
	}

	protected void initSystem() {
		IDisplaySystem display = createDisplaySystem();
		setDisplaySystem(display);

		IInputManager inputManager = new InputManager();
		setInputManager(inputManager);

		ArrayList<SceneNode> gameWorld = new ArrayList<SceneNode>();
		setGameWorld(gameWorld);

	}

	protected void shutdown() {
		display.close();
		// database clear?
		Connection conn;
		try {
			conn = DriverManager
					.getConnection("jdbc:ucanaccess://FishPool.accdb");
			Statement s = conn.createStatement();
			int a = s
					.executeUpdate("UPDATE SimulationFish set fishID = 0 where ID = 1");
			int b = s
					.executeUpdate("UPDATE SimulationFish set fishID = 0 where ID = 2");
			int c = s
					.executeUpdate("UPDATE SimulationFish set fishID = 0 where ID = 3");
			int d = s
					.executeUpdate("UPDATE SimulationObjects set objID = 0 where ID = 1");
			int e = s
					.executeUpdate("UPDATE SimulationObjects set objID = 0 where ID = 2");
			int f = s
					.executeUpdate("UPDATE SimulationObjects set objID = 0 where ID = 3");
			int g = s
					.executeUpdate("UPDATE SimulationObjects set objID = 0 where ID = 4");
			int h = s
					.executeUpdate("UPDATE SimulationObjects set objID = 0 where ID = 5");
			int i = s
					.executeUpdate("UPDATE SimulationObjects set objID = 0 where ID = 6");

			conn.close();
			// End the Rules Knowledge Session
			stopRunner();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}
	/*
	 * protected void render() { renderer.setCamera(camera1); super.render(); }
	 */
}
