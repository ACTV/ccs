package actv.ccs.sageTest;

import java.awt.Color;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import actv.ccs.listener.RuleEngineRunner;
import actv.ccs.model.*;
import actv.ccs.model.type.FishState;
import actv.ccs.sageTest.actions.*;
import graphicslib3D.*;
import sage.app.BaseGame;
import sage.camera.ICamera;
import sage.display.IDisplaySystem;
import sage.input.IInputManager;
import sage.input.InputManager;
import sage.input.action.IAction;
import sage.model.loader.OBJLoader;
import sage.renderer.IRenderer;
import sage.scene.*;
import sage.scene.SceneNode.*;
import sage.scene.shape.*;
import sage.terrain.*;
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
	private TerrainBlock floor;
	private Texture skyThing;
	private Rectangle ground, leftWall, rightWall, ceiling, backWall,
			frontWall;
	private ConvictCichlid cichlidA, cichlidB, cichlidC;
	private SceneNode cameraGuy;
	private Line yAxis1, zYPAxis, zyPtoxEnd3, pPart, zPart, yEndtoZPart,
			xEndtoZPart, xxPart, finishPart;
	private RuleEngineRunner runner;
	private ArrayList<CCSMemoryObject> objs = new ArrayList<CCSMemoryObject>();
	private boolean largePotC, mediumPotC, smallPotC, largePlantC, mediumPlantC, smallPlantC;
	private float simulationTime = 100;
	private float time = 0;
	private HUDString timeString;
	private Sphere aggroRangeA, aggroRangeB, aggroRangeC;
	private Group fishWalls;
	private IRenderer renderer;
	
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
		createHUD();
		setUpTank();
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
		
		largePlantC = false;
		mediumPlantC = false;
		smallPlantC = false;
		largePotC = false;
		mediumPotC = false;
		smallPotC = false;				

	}

	public void createPerson() {
		cameraGuy = new CameraGuy();
		cameraGuy.translate(100, 100, 500);
		cameraGuy.scale(-1, -1, -1);
		cameraGuy.rotate(180, new Vector3D(0, 1, 0));
		addGameWorldObject(cameraGuy);
		cameraGuy.updateWorldBound();

	}
	public void createHUD()
	{
		timeString = new HUDString("Time = " + time);
		timeString.setLocation(0, 0.05);
		addGameWorldObject(timeString);
		
	}
	public void setUpTank()
	{

		try 
		{
			conn = DriverManager.getConnection("jdbc:ucanaccess://FishPool.accdb");

			Statement s = conn.createStatement();
			rs = s.executeQuery("SELECT * FROM [TankData] WHERE ID = 1");
			while (rs.next()) 
			{
				String timeGrab = rs.getString("Time"); // Field from database ex.
													// FishA, FishB
				float timeParse = Float.parseFloat(timeGrab);

				simulationTime = timeParse;
				System.out.println("Here is the simulationTime! " + simulationTime);
			}
				
		}	catch (Exception epp)
				
		{
					epp.printStackTrace();
		}
				

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
						largePlantC = true;

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
						mediumPlantC = true;

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
						smallPlantC = true;

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
						Matrix3D largePotS = largePot.getLocalScale(); // this
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
						largePotC = true;

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
						mediumPotC = true;

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
						this.smallPotC = true;

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

						
						cichlidA = new ConvictCichlid();
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
						objs.add(cichlidA);
						cichlidA.updateWorldBound();
						
						
						// here is where i add my aggro circle
						aggroRangeA = new Sphere();
						Matrix3D aRangeT = aggroRangeA.getLocalTranslation();
						aRangeT.translate(xStartW, yStartY, zStartZ);
						aggroRangeA.setLocalTranslation(aRangeT);
						Matrix3D aScale = aggroRangeA.getLocalScale();
						aScale.scale(15f, 15f, 15f);
						aggroRangeA.setLocalScale(aScale);
						addGameWorldObject(aggroRangeA);
						aggroRangeA.updateWorldBound();

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

						cichlidB = new ConvictCichlid();
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

						// here is where i add my aggro circle
						aggroRangeB = new Sphere();
						Matrix3D aRangeT = aggroRangeB.getLocalTranslation();
						aRangeT.translate(xStartW, yStartY, zStartZ);
						aggroRangeB.setLocalTranslation(aRangeT);
						Matrix3D aScale = aggroRangeB.getLocalScale();
						aScale.scale(15f, 15f, 15f);
						aggroRangeB.setLocalScale(aScale);
						addGameWorldObject(aggroRangeB);
						aggroRangeB.updateWorldBound();
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

						cichlidC = new ConvictCichlid();
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
						
						// here is where i add my aggro circle
						aggroRangeC = new Sphere();
						Matrix3D aRangeT = aggroRangeC.getLocalTranslation();
						aRangeT.translate(xStartW, yStartY, zStartZ);
						aggroRangeC.setLocalTranslation(aRangeT);
						Matrix3D aScale = aggroRangeC.getLocalScale();
						aScale.scale(20f, 20f, 20f);
						aggroRangeC.setLocalScale(aScale);
						addGameWorldObject(aggroRangeC);
						aggroRangeC.updateWorldBound();
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
		fishWalls = new Group();
		
		Texture texture = TextureManager.loadTexture2D("./clouds.jpg");
		// add a rectangle, and turn it into a plane
		ground = new Rectangle(200, 200);
		ground.rotate(90, new Vector3D(1, 0, 0));
		ground.translate(101.0f, -2f, 101.0f);
		ground.setColor(Color.orange);
		/*
		// testing out new stuff
		TextureState texState = (TextureState) renderer.createRenderState(RenderStateType.Texture);
		texState.setTexture(texture);
		texState.setEnabled(true);
		ground.setRenderState(texState);
		ground.setTexture(texture);

		   		*/
		ground.setTexture(texture);
		fishWalls.addChild(ground);
		ground.updateWorldBound();

		
		
		
		
		leftWall = new Rectangle(200, 200);
		Matrix3D leftRot = new Matrix3D();
		leftRot.rotate(0, 90, 90);
		leftWall.setLocalRotation(leftRot);
		leftWall.translate(-0.1f, 101f, 101.0f);
		leftWall.setColor(Color.blue);
		// leftWall.setCullMode(CULL_MODE.ALWAYS);
		fishWalls.addChild(leftWall);
		leftWall.updateWorldBound();

		rightWall = new Rectangle(200, 200);
		Matrix3D rightRot = new Matrix3D();
		rightRot.rotate(0, 90, 90);
		rightWall.setLocalRotation(rightRot);
		rightWall.translate(201.0f, 101f, 101.0f);
		rightWall.setColor(Color.blue);
		// rightWall.setCullMode(CULL_MODE.ALWAYS);
		fishWalls.addChild(rightWall);
		rightWall.updateWorldBound();

		backWall = new Rectangle(200, 200);
		Matrix3D backRot = new Matrix3D();
		backRot.rotate(0, 0, 0);
		backWall.setLocalRotation(backRot);
		backWall.translate(101.0f, 101.0f, -0.10f);
		backWall.setColor(Color.blue);
		// backWall.setCullMode(CULL_MODE.ALWAYS);
		fishWalls.addChild(backWall);
		backWall.updateWorldBound();

		ceiling = new Rectangle(200, 200);
		Matrix3D ceilingRot = new Matrix3D();
		ceilingRot.rotate(90, 0, 0);
		ceiling.setLocalRotation(ceilingRot);
		ceiling.translate(101.0f, 201f, 101.0f);
		ceiling.setColor(Color.blue);
		// ceiling.setCullMode(CULL_MODE.ALWAYS);
		fishWalls.addChild(ceiling);
		ceiling.updateWorldBound();

		// find transparency for this
		frontWall = new Rectangle(200, 200);
		Matrix3D frontRot = new Matrix3D();
		frontRot.rotate(0, 180, 0);
		frontWall.setLocalRotation(frontRot);
		frontWall.translate(101.0f, 101.0f, 201.0f);
		frontWall.setCullMode(CULL_MODE.ALWAYS);
		fishWalls.addChild(frontWall);
		frontWall.updateWorldBound();
		
		addGameWorldObject(fishWalls);

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

	public void update(float elapsedTimeMS) // this will be where the objects will move
	{
		
		// creating timer thing
		time += elapsedTimeMS;
		timeString.setText("Time: " + time/1000);
		float timeCompare = time/1000;

	
	if (timeCompare >= simulationTime)
	{
	 //	System.out.println("RIGHT HERE IS WHERE I STOP EVERYTHING!!!");
	// this works	
		// here would be where you want to pause the simulation
	}
	
		super.update(time);
		cc.update(time);
		for (SceneNode s : getGameWorld()) {
			if (s instanceof ConvictCichlid) // here will be where the objects will
											// have be able to move, but i will
											// implement that later.
			{
				if (s == cichlidA) {
					// s.translate(0, 0, .1f);
					// s.updateWorldBound();
					// bound collision
					Point3D loc = new Point3D(s.getWorldTranslation().getCol(3));
		
					
					// here is where i will test my newfound collision for spheres
					
					Matrix3D cichlidAlocalT = s.getLocalTranslation();
					aggroRangeA.setLocalTranslation(cichlidAlocalT);
					
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
					// object collision
					/*
					if (largePlantC == true) // ERROR
					{
						if (cichlidA.getWorldBound().intersects(largePlant.getWorldBound()))
						{
							System.out.println("a hit largePl");
						}
					}
					*/
					/*
					if (largePlantC == true)
					{
						Point3D largePlantloc = new Point3D(largePlant.getWorldTranslation().getCol(3));
						if ((loc.getX() == largePlantloc.getX()) && (loc.getY() == largePlantloc.getY()) 
								&& (loc.getZ() == largePlantloc.getZ()) )
						{
							System.out.println("b hit large plant");
						}
					}
					*/
					if (largePlantC == true)
					{
						if (cichlidA.getWorldBound().intersects(largePlant.getWorldBound()))
						{
							System.out.println("a hit large pl");
						}
					}
					if (mediumPotC == true)
					{
						if (cichlidA.getWorldBound().intersects(mediumPot.getWorldBound()))
						{
							System.out.println("a hit med pot");
						}
					}
					if (mediumPlantC == true)
					{
						if	 (cichlidA.getWorldBound().intersects(mediumPlant.getWorldBound()))
						{
							System.out.println("a hit med pl");
						}
					}
					if (smallPlantC == true)
					{
						if (cichlidA.getWorldBound().intersects(smallPlant.getWorldBound()))
						{
							System.out.println("a hit small pla");
						}
					}
					if (smallPotC == true)
					{
						if (cichlidA.getWorldBound().intersects(smallPot.getWorldBound()))
						{
							System.out.println("a hit small pot");
						}
					}
					// cichlid collision
					if (cichlidB != null)
					{
						if (cichlidA.getWorldBound().intersects(cichlidB.getWorldBound()))
						{
							System.out.println("a hits b");
							// this is where shit goes down
						}
						if (aggroRangeA.getWorldBound().intersects(aggroRangeB.getWorldBound()))
						{
							System.out.println("aggro from a to B");
						}
					}
					if (cichlidC != null)
					{
						if (cichlidA.getWorldBound().intersects(cichlidC.getWorldBound()))
						{
							System.out.println("a hits c");
							// this is where shit goes down
						}
						if (aggroRangeA.getWorldBound().intersects(aggroRangeC.getWorldBound()))
						{
							System.out.println("aggro from a to C");
						}
					}
					
					
				}
				if (s == cichlidB) {
					// call move stuff here
					Point3D loc = new Point3D(s.getWorldTranslation().getCol(3));

					
					Matrix3D cichlidBlocalT = s.getLocalTranslation();
					aggroRangeB.setLocalTranslation(cichlidBlocalT);
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
					if (largePotC == true)
					{
						if (cichlidB.getWorldBound().intersects(largePot.getWorldBound()))
						{
							System.out.println("b hit largePo");
						}
					}
					/*
					if (largePlantC == true) // ERROR
					{
						if (cichlidB.getWorldBound().intersects(largePlant.getWorldBound()))
						{
							System.out.println("b hit largePl");
						}
					}
					*/
					if (largePlantC == true)
					{
						Point3D largePlantloc = new Point3D(largePlant.getWorldTranslation().getCol(3));
						if ((loc.getX() == largePlantloc.getX()) && (loc.getY() == largePlantloc.getY()) 
								&& (loc.getZ() == largePlantloc.getZ()) )
						{
							System.out.println("b hit large plant");
						}
					}
					if (mediumPotC == true)
					{
						if (cichlidB.getWorldBound().intersects(mediumPot.getWorldBound()))
						{
							System.out.println("b hit medP");
						}
					}
					if (mediumPlantC == true)
					{
						if (cichlidB.getWorldBound().intersects(mediumPlant.getWorldBound()))
						{
							System.out.println("b hit medPL");
						}
					}
					if (smallPlantC == true)
					{
						if (cichlidB.getWorldBound().intersects(smallPlant.getWorldBound()))
						{
							System.out.println("b hit smallPl");
						}
					}
					if (smallPotC == true)
					{
						if (cichlidB.getWorldBound().intersects(smallPot.getWorldBound()))
						{
							System.out.println("b hit smallPot");
						}
					}
					// cichlid collision
					if (cichlidA != null)
					{
						if (cichlidB.getWorldBound().intersects(cichlidA.getWorldBound()))
						{
							System.out.println("b hits a");
							// this is where shit goes down
						}
						if (aggroRangeB.getWorldBound().intersects(aggroRangeA.getWorldBound()))
						{
							System.out.println("aggro from B to A");
						}
					}
					if (cichlidC != null)
					{
						if (cichlidB.getWorldBound().intersects(cichlidC.getWorldBound()))
						{
							System.out.println("b hits c");
							// this is where shit goes down
						}
						if (aggroRangeB.getWorldBound().intersects(aggroRangeC.getWorldBound()))
						{
							System.out.println("aggro from B to C");
						}
					}
				}
				if (s == cichlidC) {
					// call move stuff here
					Point3D loc = new Point3D(s.getWorldTranslation().getCol(3));
					Matrix3D cichlidClocalT = s.getLocalTranslation();
					aggroRangeC.setLocalTranslation(cichlidClocalT);
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
					if (largePotC == true)
					{
						if (cichlidC.getWorldBound().intersects(largePot.getWorldBound()))
						{
							System.out.println("c hit large pot");
						}
					}
					if (largePlantC == true)
					{
						if (cichlidC.getWorldBound().intersects(largePlant.getWorldBound()))
						{
							System.out.println("c hit large plant");
						}
					}
					if (mediumPotC == true)
					{
						if (cichlidC.getWorldBound().intersects(mediumPot.getWorldBound()))
						{
							System.out.println("c hit medium pot");
						}
					}
					if (mediumPlantC == true)
					{
						if (cichlidC.getWorldBound().intersects(mediumPlant.getWorldBound()))
						{
							System.out.println("c hit medium plant");
						}
					}
					if (smallPlantC == true)
					{
						if (cichlidC.getWorldBound().intersects(smallPlant.getWorldBound()))
						{
							System.out.println("c hit small plant");
						}
					}
					if (smallPotC == true)
					{
						if (cichlidC.getWorldBound().intersects(smallPot.getWorldBound()))
						{
							System.out.println("c hit small pot");
						}
					}
					// cichlid collision
					if (cichlidA != null)
					{
						if (cichlidC.getWorldBound().intersects(cichlidA.getWorldBound()))
						{
							System.out.println("c hits a");
							// this is where shit goes down
						}
						if (aggroRangeC.getWorldBound().intersects(aggroRangeA.getWorldBound()))
						{
							System.out.println("aggro from C to A");
						}
					}
					if (cichlidB != null)
					{
						if (cichlidC.getWorldBound().intersects(cichlidA.getWorldBound()))
						{
							System.out.println("c hits b");
							// this is where shit goes down
						}
						if (aggroRangeC.getWorldBound().intersects(aggroRangeB.getWorldBound()))
						{
							System.out.println("aggro from C to B");
						}
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
			int z = s
					.executeUpdate("UPDATE ScenarioFlag set ScenarioNumber = 0 where ID = 1");
			int zz = s
					.executeUpdate("UPDATE ScenarioFlag set ScenarioNumber = 0 where ID = 2");
			int aa = s
					.executeUpdate("UPDATE ScenarioFlag set ScenarioNumber = 0 where ID = 3");
			int bb = s
					.executeUpdate("UPDATE ScenarioFlag set ScenarioNumber = 0 where ID = 4");
			int cc = s
					.executeUpdate("UPDATE ScenarioFlag set ScenarioNumber = 0 where ID = 5");
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
