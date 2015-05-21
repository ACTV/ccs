package actv.ccs.sageTest;

import java.awt.Color;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.java.games.input.Event;
import actv.ccs.DBConnection;
import actv.ccs.RuleEngineRunner;
import actv.ccs.model.*;
import actv.ccs.model.type.FishState;
import actv.ccs.sageTest.AI.AIController;
import actv.ccs.sageTest.AI.AIRunner;
import actv.ccs.sageTest.actions.*;
import graphicslib3D.*;
import sage.app.BaseGame;
import sage.camera.ICamera;
import sage.display.DisplaySystem;
import sage.display.IDisplaySystem;
import sage.input.IInputManager;
import sage.input.InputManager;
import sage.input.action.AbstractInputAction;
import sage.input.action.IAction;
import sage.model.loader.OBJLoader;
import sage.model.loader.ogreXML.OgreXMLParser;
import sage.renderer.IRenderer;
import sage.scene.*;
import sage.scene.SceneNode.*;
import sage.scene.shape.*;
import sage.scene.state.RenderState.RenderStateType;
import sage.scene.state.RenderState;
import sage.scene.state.TextureState;
import sage.terrain.*;
import sage.texture.*;

/*
 *  Albert to do list 4-21-15
 *  do 165 lool
 *  figure out to close the window for test game
 *  then how to save file ...
 *  which could be done by adding another table for fish scenario and then when closed, saved all data to there. then you can start a new table 
 *  do documentation
 *  user manual
 *  models if victor doesn't do it ... zz
 *  
 *  rules need to be done - with collisions ... make them faster. will look at after i'm done with documentation
 *  
 *  for collisions - large plant is getting hit a lot. - maybe fixed.
 */
public class MyGame extends BaseGame {

	IDisplaySystem display;
	IInputManager im;
	// private Pot largePot, mediumPot, smallPot;
	// private Plant largePlant, mediumPlant, smallPlant;
	private TriMesh largePlant, mediumPlant, smallPlant, largePot, mediumPot,
			smallPot;
	private ICamera camera;
	private CameraOrbit cc;
	private SkyBox skybox;
	private Connection conn;
	private ResultSet rs, rsI;
	private TerrainBlock floor;
	private Texture skyThing;
	private ConvictCichlid cichlidA, cichlidB, cichlidC;
	private ConvictCichlid[] CCList = new ConvictCichlid[3];
	private SceneNode cameraGuy;
	private Line yAxis1, zYPAxis, zyPtoxEnd3, pPart, zPart, yEndtoZPart,
			xEndtoZPart, xxPart, finishPart;
	private RuleEngineRunner runner;
	private ArrayList<SceneNode> objs = new ArrayList<SceneNode>();
	private boolean largePotC, mediumPotC, smallPotC, largePlantC,
			mediumPlantC, smallPlantC;
	private float simulationTime = 100;
	private float time = 0;
	private int cichlidCount;
	private int objCount;
	private HUDString timeString;
	private Sphere aggroRangeA, aggroRangeB, aggroRangeC;
	private Group fishWalls;
	private IRenderer renderer;
	// going to add a pause button here
	private volatile boolean pauseSimulation = false;
	private FishTank fishTank;
	private boolean startAnimation;
	private Thread tObject;
	private boolean initialized = false;
	private Group walls;
	private static final Logger logger = LoggerFactory.getLogger(MyGame.class);

	// testing for ogre model loader
	TextureState testState;
	Group model;
	Model3DTriMesh cichlidAObject, cichlidBObject, cichlidCObject;

	/*
	 * public void initGame() { initObjects(); spawnCichlids(); spawnObjects();
	 * createPerson(); // createScene(); initActions(); fishTank = new
	 * FishTankImpl(); // createFishTank(); createFishTankWalls();
	 * startRunner(); createHUD(); setUpTank(); pauseSimulation = false; // set
	 * to false for beginning startAnimation = true; cichlidCount = 0; objCount
	 * = 0;
	 * 
	 * }
	 */
	
	// testing for AI now
	private AIController aic;
	private AIRunner aiRunner;
	private long lastUpdateTime;
	private ExecutorService AIExecutor = Executors.newSingleThreadExecutor();
	
	public void initGame() {
		createHUD();
		startAnimation = false;
		try {
			this.conn = DriverManager
					.getConnection("jdbc:ucanaccess://FishPool.accdb");

			Statement s = this.conn.createStatement();
			this.rs = s
					.executeQuery("SELECT scenarioNumber FROM [ScenarioFlag]");
			while (this.rs.next()) {
				String scenNum = this.rs.getString("ScenarioNumber");

				int scenGrab = Integer.parseInt(scenNum);
				if (scenGrab >= 1 && scenGrab <= 6) {
					this.fishTank = new FishTankImpl();
					startAnimation = true;
					startAnimationProcess();
				} else {
					IDisplaySystem display = getDisplaySystem();
					display.setTitle("Please wait... loading objects.");
					this.camera = display.getRenderer().getCamera();
					this.camera.setPerspectiveFrustum(45.0D, 1.0D, 0.01D,
							1000.0D);
					this.camera.setLocation(new Point3D(1.0D, 1.0D, 20.0D));
					logger.debug("no scenario in place?");
					this.pauseSimulation = false;
					// this.startAnimation = false;
					this.cichlidCount = 0;
					this.objCount = 0;
					createPerson();
					initActions();
					// createHUD();
					logger.info("Finished initial startup!");

				}
			}
		} catch (Exception epp) {
			epp.printStackTrace();
		}
	}

	public void startAnimationProcess() {
		System.out.println("call me maybe");

		for (SceneNode s : getGameWorld()) {
			if (s instanceof Model3DTriMesh) {
				if (s == cichlidAObject) {
					// System.out.println("i'm calling back!");
					((Model3DTriMesh) s).startAnimation("swimmingAction");
				}
				if (s == cichlidBObject) {
					// System.out.println("i'm fapping back");
					((Model3DTriMesh) s).startAnimation("swimmingAction");
				}
				if (s == cichlidCObject) {
					// System.out.println("the world gone maaad");
					((Model3DTriMesh) s).startAnimation("swimmingAction");
				}
			}
		}

	}

	public void startRunner() {
		runner = RuleEngineRunner.getInstance();
		runner.newMap(objs);
		runner.start();
	}

	private void pauseRunner() {
		runner.pauseSession();
	}

	private void resumeRunner() {
		runner.resumeSession();
	}

	private void stopRunner() {
		try {
			runner.closeSession();
			runner.join();
		} catch (InterruptedException e) {
			throw new RuntimeException("Unable to end the rule session thread!");
		}
	}

	public void initObjects() {
		// this is for initializing objects
		display = getDisplaySystem();
		display.setTitle("Convict Cichlid Simulation");

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
		Line xAxis = new Line(origin, xEnd, Color.black, 2);
		Line yAxis = new Line(origin, yEnd, Color.black, 2);
		Line zAxis = new Line(origin, zEnd, Color.black, 2); // Base

		// Line xAxis1 = new Line (xEnd1, xEnd3, Color.cyan, 2);
		yAxis1 = new Line(xEnd2, xEnd3, Color.black, 2);
		zyPtoxEnd3 = new Line(new Point3D(200, 0, 0), new Point3D(200, 200, 0),
				Color.black, 2);
		pPart = new Line(new Point3D(200, 0, 0), new Point3D(200, 0, 200),
				Color.black, 2);
		finishPart = new Line(new Point3D(0, 200, 0), new Point3D(200, 200, 0),
				Color.black, 2);
		yEndtoZPart = new Line(yEnd, new Point3D(0, 200, 200), Color.black, 2);
		xEndtoZPart = new Line(new Point3D(0, 200, 200), new Point3D(200, 200,
				200), Color.black, 2);
		xxPart = new Line(new Point3D(200, 200, 0), new Point3D(200, 200, 200),
				Color.black, 2);
		zPart = new Line(zEnd, xEnd2, Color.black, 2);
		zYPAxis = new Line(zEnd, zyP, Color.black, 2);
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

		initialized = true;
	}

	public boolean isInitialized() {
		return initialized;
	}

	public void createPerson() {
		cameraGuy = new CameraGuy();
		cameraGuy.translate(100, 100, 500);
		cameraGuy.scale(-1, -1, -1);
		cameraGuy.rotate(180, new Vector3D(0, 1, 0));
		addGameWorldObject(cameraGuy);
		cameraGuy.updateWorldBound();

	}

	public void createHUD() {
		timeString = new HUDString("Time = " + time);
		timeString.setLocation(0, 0.05);
		addGameWorldObject(timeString);

	}

	public void pauseGame() {
		pauseSimulation = true;
		pauseRunner();

	}

	public void resumeGame() {
		pauseSimulation = false;
		resumeRunner();
	}

	public void setUpTank() {

		try {
			conn = DriverManager
					.getConnection("jdbc:ucanaccess://FishPool.accdb");

			Statement s = conn.createStatement();
			rs = s.executeQuery("SELECT * FROM [TankData] WHERE ID = 1");
			while (rs.next()) {
				String timeGrab = rs.getString("Time"); // Field from database
														// ex.
				// FishA, FishB
				float timeParse = Float.parseFloat(timeGrab);

				simulationTime = timeParse;
				logger.info("Here is the simulationTime! " + simulationTime);
			}

		} catch (Exception epp)

		{
			epp.printStackTrace();
		}

	}

	public void spawnObjects() {
		try {

			Texture plantTex = TextureManager.loadTexture2D("./uplant.png");
			Texture potTex = TextureManager.loadTexture2D("./potBa.png");
			conn = DriverManager
					.getConnection("jdbc:ucanaccess://FishPool.accdb");

			Statement s = conn.createStatement();
			rs = s.executeQuery("SELECT objID FROM [SimulationObjects]");
			while (rs.next()) {
				String id = rs.getString("objID"); // Field from database ex.
													// largepot etc.
				int idS = Integer.parseInt(id);

				logger.info("ID: {}", idS);

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
						largePlant = loader.loadModel("uplant.obj");
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
						largePlant.setTexture(plantTex);
						largePlant.updateLocalBound();
						largePlant.updateGeometricState(0, true);
						largePlant.updateWorldBound();
						largePlantC = true;
						objCount++;

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
						mediumPlant = loader1.loadModel("uplant.obj");
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
						mediumPlant.setTexture(plantTex);
						mediumPlant.updateLocalBound();
						mediumPlant.updateGeometricState(0, true);
						mediumPlant.updateWorldBound();
						mediumPlantC = true;
						objCount++;

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
						smallPlant = loader2.loadModel("uplant.obj");
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
						smallPlant.setTexture(plantTex);
						smallPlant.updateLocalBound();
						smallPlant.updateGeometricState(0, true);
						smallPlant.updateWorldBound();
						smallPlantC = true;
						objCount++;

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
						largePot = loader3.loadModel("upot.obj");
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
						largePot.setTexture(potTex);
						largePot.updateLocalBound();
						largePot.updateGeometricState(0, true);
						largePot.updateWorldBound();
						largePotC = true;
						objCount++;
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
						mediumPot = loader4.loadModel("upot.obj");
						mediumPot.setName(name);
						Matrix3D mediumPotT = mediumPot.getLocalTranslation(); // this
																				// is
																				// for
																				// position
						mediumPotT.translate(xStartW, yStartY, zStartZ);
						mediumPot.setLocalTranslation(mediumPotT);
						Matrix3D mediumPotS = mediumPot.getLocalScale(); // this
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
						mediumPot.setTexture(potTex);
						mediumPot.updateLocalBound();
						mediumPot.updateGeometricState(0, true);
						mediumPot.updateWorldBound();
						mediumPotC = true;
						objCount++;
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
						smallPot = loader5.loadModel("upot.obj");
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
						smallPot.setTexture(potTex);
						smallPot.updateLocalBound();
						smallPot.updateGeometricState(0, true);
						smallPot.updateWorldBound();
						this.smallPotC = true;
						objCount++;
					}
				}
			}
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private ConvictCichlid spawnCichlidFromDB(String id) throws SQLException {
		ConvictCichlid cichlid;
		DBConnection conn = DBConnection.getInstance();
		conn.createConnection();
		conn.executeQuery("SELECT * FROM [FishPool] WHERE ID='" + id + "'");

		float widthW = Float.parseFloat(rs.getString("Weight"));
		float heightW = Float.parseFloat(rs.getString("Height"));
		float xStartW = Float.parseFloat(rs.getString("StartingXPos"));
		float yStartY = Float.parseFloat(rs.getString("StartingYPos"));
		float zStartZ = Float.parseFloat(rs.getString("StartingZPos"));
		String name = rs.getString("Type");

		cichlid = new ConvictCichlid(0, widthW, heightW, name, new Point3D(
				xStartW, yStartY, zStartZ));
		cichlid.setGender(rs.getString("Gender"));
		cichlid.setAggroLevel(Float.parseFloat(rs.getString("AggroLevel")));

		// TODO: need to put into DB
		cichlid.setBaseSpeed(3f);
		cichlid.setBaseCautionLevel(4f);
		cichlid.setDirection(new Vector3D(1, 1, 1));
		cichlid.setCullMode(CULL_MODE.ALWAYS);
		cichlid.setState(FishState.IDLE);
		cichlid.setInfluence(12);

		return cichlid;
	}

	public void spawnCichlids() {

		try {
			DBConnection conn = DBConnection.getInstance();
			conn.createConnection();
			ResultSet rs = conn
					.executeQuery("SELECT fishID FROM [SimulationFish]");
			while (rs.next()) {
				String id = rs.getString("fishID"); // Field from database ex.
													// FishA, FishB
				int idS = Integer.parseInt(id);

				logger.debug("ID: {}", idS);

				if (id.equals("1")) {
					rsI = conn
							.executeQuery("SELECT * FROM [FishPool] WHERE Type='Fish A'");
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

						cichlidA = new ConvictCichlid(0, widthW, heightW, name,
								new Point3D(xStartW, yStartY, zStartZ));
						cichlidA.setGender(gender);
						cichlidA.setAggroLevel(aggroW);
						// TODO: temporary
						cichlidA.setBaseSpeed(3f);
						cichlidA.setSpeed(0);
						cichlidA.setBaseCautionLevel(4f);
						cichlidA.setDirection(new Vector3D(1, 1, 1));
				//		cichlidA.setCullMode(CULL_MODE.ALWAYS);
						cichlidA.setState(FishState.IDLE);
						cichlidA.setInfluence(12);
						cichlidA.setState(FishState.SWIM);

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
						aScale.scale(30f, 30f, 30f);
						aggroRangeA.setLocalScale(aScale);
						addGameWorldObject(aggroRangeA);
						aggroRangeA.updateWorldBound();
						aggroRangeA.setCullMode(CULL_MODE.ALWAYS); // cull mode
																	// hides the
																	// object
						cichlidCount++;

						/*
						 * // // here is where i add the cichlidMesh OBJLoader
						 * loader1 = new OBJLoader(); cichlidAMesh = loader1
						 * .loadModel("wacklid.obj");
						 * cichlidAMesh.setName(name); Matrix3D cichlidAMeshT =
						 * cichlidAMesh.getLocalTranslation(); // this // is //
						 * for // position cichlidAMeshT.translate(xStartW,
						 * yStartY, zStartZ);
						 * cichlidAMesh.setLocalTranslation(cichlidAMeshT);
						 * Matrix3D cichlidAMeshS =
						 * cichlidAMesh.getLocalScale(); // this // is // for //
						 * size // of // object cichlidAMeshS.scale(widthW *
						 * weightW * .1, heightW weightW * .1, 0); // the scale
						 * // might be // too big // so we //
						 * largePlant.setLocalScale(largePlantS);
						 * 
						 * // cichlidAMeshS.scale(10f, 10f, 10f);
						 * cichlidAMesh.setLocalScale(cichlidAMeshS);
						 * cichlidAMesh.setTexture(cichlidTexA);
						 * 
						 * 
						 * 
						 * // trying texture
						 * 
						 * TextureState cichlidATexS; Texture cATex =
						 * TextureManager.loadTexture2D("cichlidMesh.png");
						 * cATex
						 * .setApplyMode(sage.texture.Texture.ApplyMode.Replace
						 * ); cichlidATexS = (TextureState)
						 * display.getRenderer()
						 * .createRenderState(RenderState.RenderStateType
						 * .Texture); cichlidATexS.setTexture(cATex, 0);
						 * cichlidATexS.setEnabled(true);
						 * 
						 * cichlidAMesh.setRenderState(cichlidATexS);
						 * addGameWorldObject(cichlidAMesh);
						 * cichlidAMesh.updateLocalBound();
						 * cichlidAMesh.updateGeometricState(0, true);
						 * cichlidAMesh.updateWorldBound();
						 */
						// creating new ogre
						OgreXMLParser loader = new OgreXMLParser();

						try {
							model = loader
									.loadModel(
											"src/main/java/actv/ccs/sageTest/testingoutOgre/Plane.mesh.xml",
											"src/main/java/actv/ccs/sageTest/testingoutOgre/pooplid.material",
											"src/main/java/actv/ccs/sageTest/testingoutOgre/Plane.skeleton.xml");
							// src/main/java/actv/ccs/sageTest/TestOgre
							model.updateGeometricState(0, true);
							java.util.Iterator<SceneNode> modelIterator = model
									.iterator();
							cichlidAObject = (Model3DTriMesh) modelIterator
									.next();
							logger.debug("test");
						} catch (Exception vv) {
							vv.printStackTrace();

						}

						Texture hobTexture = TextureManager
								.loadTexture2D("src/main/java/actv/ccs/sageTest/testingOutOgre/cichlidMesh.png");
						hobTexture
								.setApplyMode(sage.texture.Texture.ApplyMode.Replace);
						testState = (TextureState) display.getRenderer()
								.createRenderState(
										RenderState.RenderStateType.Texture);
						testState.setTexture(hobTexture, 0);
						testState.setEnabled(true);

						addGameWorldObject(cichlidAObject);
						cichlidAObject.translate((float) xStartW,
								(float) yStartY, (float) zStartZ);
						cichlidAObject.rotate(90, new Vector3D(0, 1, 0));
						cichlidAObject.scale((float) (widthW * weightW * .1),
								(float) (heightW * weightW * .09),
								(float) (heightW * 0.09));

					}
				} else if (id.equals("2")) {
					rsI = conn
							.executeQuery("SELECT * FROM [FishPool] WHERE Type='Fish B'");

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

						cichlidB = new ConvictCichlid(0, widthW, heightW, name,
								new Point3D(xStartW, yStartY, zStartZ));
						cichlidB.setName(name);
						cichlidB.setGender(gender);
						cichlidB.setAggroLevel(aggroW);
						// TODO: temporary
						cichlidB.setBaseSpeed(3f);
						cichlidB.setSpeed(0f);
						cichlidB.setBaseCautionLevel(4f);
						cichlidB.setDirection(new Vector3D(1, 1, 1));
					//	cichlidB.setCullMode(CULL_MODE.ALWAYS);
						cichlidB.setState(FishState.IDLE);
						cichlidB.setInfluence(8);
						cichlidB.setState(FishState.SWIM);
						
						
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
						aScale.scale(30f, 30f, 30f);
						aggroRangeB.setLocalScale(aScale);
						addGameWorldObject(aggroRangeB);
						aggroRangeB.updateWorldBound();
						aggroRangeB.setCullMode(CULL_MODE.ALWAYS); // cull mode
																	// hides the
																	// object

						// // here is where i add the cichlidMesh
						/*
						 * OBJLoader loader1 = new OBJLoader(); cichlidBMesh =
						 * loader1 .loadModel("wacklid.obj");
						 * cichlidBMesh.setName(name); Matrix3D cichlidAMeshT =
						 * cichlidBMesh.getLocalTranslation(); // this // is //
						 * for // position cichlidAMeshT.translate(xStartW,
						 * yStartY, zStartZ);
						 * cichlidBMesh.setLocalTranslation(cichlidAMeshT);
						 * Matrix3D cichlidAMeshS =
						 * cichlidBMesh.getLocalScale(); // this // is // for //
						 * size // of // object cichlidAMeshS.scale(widthW *
						 * weightW * .1, heightW weightW * .1, 0); // the scale
						 * // might be // too big // so we //
						 * largePlant.setLocalScale(largePlantS);
						 * 
						 * // cichlidAMeshS.scale(10f, 10f, 10f);
						 * cichlidBMesh.setLocalScale(cichlidAMeshS);
						 * cichlidBMesh.setTexture(cichlidTexA); TextureState
						 * cichlidATexS; Texture cATex =
						 * TextureManager.loadTexture2D("cichlidMesh.png");
						 * cATex
						 * .setApplyMode(sage.texture.Texture.ApplyMode.Replace
						 * ); cichlidATexS = (TextureState)
						 * display.getRenderer()
						 * .createRenderState(RenderState.RenderStateType
						 * .Texture); cichlidATexS.setTexture(cATex, 0);
						 * cichlidATexS.setEnabled(true);
						 * 
						 * cichlidBMesh.setRenderState(cichlidATexS);
						 * addGameWorldObject(cichlidBMesh);
						 * cichlidBMesh.updateLocalBound();
						 * cichlidBMesh.updateGeometricState(0, true);
						 * cichlidBMesh.updateWorldBound();
						 */

						OgreXMLParser loader = new OgreXMLParser();

						try {
							model = loader
									.loadModel(
											"src/main/java/actv/ccs/sageTest/testingoutOgre/Plane.mesh.xml",
											"src/main/java/actv/ccs/sageTest/testingoutOgre/pooplid.material",
											"src/main/java/actv/ccs/sageTest/testingoutOgre/Plane.skeleton.xml");
							// src/main/java/actv/ccs/sageTest/TestOgre
							model.updateGeometricState(0, true);
							java.util.Iterator<SceneNode> modelIterator = model
									.iterator();
							cichlidBObject = (Model3DTriMesh) modelIterator
									.next();
							logger.debug("test");
						} catch (Exception vv) {
							vv.printStackTrace();

						}

						Texture hobTexture = TextureManager
								.loadTexture2D("src/main/java/actv/ccs/sageTest/testingOutOgre/cichlidMesh.png");
						hobTexture
								.setApplyMode(sage.texture.Texture.ApplyMode.Replace);
						testState = (TextureState) display.getRenderer()
								.createRenderState(
										RenderState.RenderStateType.Texture);
						testState.setTexture(hobTexture, 0);
						testState.setEnabled(true);

						addGameWorldObject(cichlidBObject);
						cichlidBObject.translate((float) xStartW,
								(float) yStartY, (float) zStartZ);
						cichlidBObject.rotate(45, new Vector3D(0, 1, 1));
						cichlidBObject.scale((float) (widthW * weightW * .05),
								(float) (heightW * weightW * .05),
								(float) (heightW * 0.09));
						cichlidCount++;
					}
				} else if (id.equals("3")) {
					rsI = conn
							.executeQuery("SELECT * FROM [FishPool] WHERE Type='Fish C'");

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

						cichlidC = new ConvictCichlid(0, widthW, heightW, name,
								new Point3D(xStartW, yStartY, zStartZ));
						cichlidC.setName(name);
						cichlidC.setGender(gender);
						cichlidC.setAggroLevel(aggroW);

						// TODO: temporary
						cichlidC.setBaseSpeed(3f);
						cichlidC.setSpeed(0);
						cichlidC.setBaseCautionLevel(4f);
						cichlidC.setDirection(new Vector3D(-.5, .8, .1));
						cichlidC.setState(FishState.IDLE);
					//	cichlidC.setCullMode(CULL_MODE.ALWAYS);
						cichlidC.setInfluence(6);
						cichlidC.setState(FishState.SWIM);

						
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
						cichlidCS.scale(widthW * weightW * .1, heightW
								* weightW * .1, 0); // the scale might be too
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
						aScale.scale(30f, 30f, 30f);
						aggroRangeC.setLocalScale(aScale);
						addGameWorldObject(aggroRangeC);
						aggroRangeC.updateWorldBound();
						aggroRangeC.setCullMode(CULL_MODE.ALWAYS); // cull mode
																	// hides the
																	// object

						/*
						 * OBJLoader loader1 = new OBJLoader(); cichlidCMesh =
						 * loader1 .loadModel("wacklid.obj");
						 * cichlidCMesh.setName(name); Matrix3D cichlidAMeshT =
						 * cichlidCMesh.getLocalTranslation(); // this // is //
						 * for // position cichlidAMeshT.translate(xStartW,
						 * yStartY, zStartZ);
						 * cichlidCMesh.setLocalTranslation(cichlidAMeshT);
						 * Matrix3D cichlidAMeshS =
						 * cichlidCMesh.getLocalScale(); // this // is // for //
						 * size // of // object cichlidAMeshS.scale(widthW *
						 * weightW * .1, heightW weightW * .1, 0); // the scale
						 * // might be // too big // so we //
						 * largePlant.setLocalScale(largePlantS);
						 * 
						 * // cichlidAMeshS.scale(10f, 10f, 10f);
						 * cichlidCMesh.setLocalScale(cichlidAMeshS);
						 * cichlidCMesh.setTexture(cichlidTexA);
						 * addGameWorldObject(cichlidCMesh);
						 * cichlidCMesh.updateLocalBound();
						 * cichlidCMesh.updateGeometricState(0, true);
						 * cichlidCMesh.updateWorldBound();
						 */
						OgreXMLParser loader = new OgreXMLParser();

						try {
							model = loader
									.loadModel(
											"src/main/java/actv/ccs/sageTest/testingoutOgre/Plane.mesh.xml",
											"src/main/java/actv/ccs/sageTest/testingoutOgre/pooplid.material",
											"src/main/java/actv/ccs/sageTest/testingoutOgre/Plane.skeleton.xml");
							// src/main/java/actv/ccs/sageTest/TestOgre
							model.updateGeometricState(0, true);
							java.util.Iterator<SceneNode> modelIterator = model
									.iterator();
							cichlidCObject = (Model3DTriMesh) modelIterator
									.next();
							logger.debug("test");
						} catch (Exception vv) {
							vv.printStackTrace();

						}

						Texture hobTexture = TextureManager
								.loadTexture2D("src/main/java/actv/ccs/sageTest/testingOutOgre/cichlidMesh.png");
						hobTexture
								.setApplyMode(sage.texture.Texture.ApplyMode.Replace);
						testState = (TextureState) display.getRenderer()
								.createRenderState(
										RenderState.RenderStateType.Texture);
						testState.setTexture(hobTexture, 0);
						testState.setEnabled(true);

						addGameWorldObject(cichlidCObject);
						cichlidCObject.translate((float) xStartW,
								(float) yStartY, (float) zStartZ);
						cichlidCObject.rotate(45, new Vector3D(0, 1, 1));
						cichlidCObject.scale((float) (widthW * weightW * .05),
								(float) (heightW * weightW * .05),
								(float) (heightW * 0.09));

						cichlidCount++;
					}
				}
			}
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		startAnimation = true;
		startAnimationProcess();
	}

	/**
	 * 
	 */
	/**
	 * 
	 */
	/**
	 * 
	 */
	public void initActions() {
		im = getInputManager();
		String kbName = im.getKeyboardName(); // error here. it shouldn't be
												// null
		String mName = im.getMouseName();
		// sFindComponents f = new FindComponents();

		cc = new CameraOrbit(camera, cameraGuy, im, mName);

		logger.debug("controller: " + mName);

		// for this area, need to do a checker if A and B and C are called...
		/*
		 * // test actions IAction moveForwardA = new ForwardAction(cichlidA,
		 * cichlidAObject); IAction moveBackA = new
		 * BackwardAction(cichlidAObject); IAction moveLeftA = new
		 * LeftAction(cichlidAObject); IAction moveRightA = new
		 * RightAction(cichlidAObject); IAction upForwardA = new
		 * UpForwardAction(cichlidAObject); IAction upBackA = new
		 * UpBackAction(cichlidAObject); IAction downForwardA = new
		 * DownForwardAction(cichlidAObject); IAction downBackA = new
		 * DownBackAction(cichlidAObject); IAction rotateTest = new
		 * RotateTestAction(cichlidAObject);
		 */
		// game actions
		IAction quitGame = new QuitAction(this);
		IAction pauseKey = new pauseAction();
		IAction resumeKey = new resumeAction();
		im.associateAction(kbName,
				net.java.games.input.Component.Identifier.Key.ESCAPE, quitGame,
				IInputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
		im.associateAction(kbName,
				net.java.games.input.Component.Identifier.Key.P, pauseKey,
				IInputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
		im.associateAction(kbName,
				net.java.games.input.Component.Identifier.Key.R, resumeKey,
				IInputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);

		// if (pauseSimulation == true) // this is for save simulation
		// {
		IAction saveState = new saveAction();
		im.associateAction(kbName,
				net.java.games.input.Component.Identifier.Key.Q, saveState,
				IInputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);

		// }

		// here is the movement options of the character ..
		
		/*  im.associateAction(kbName,
		  net.java.games.input.Component.Identifier.Key.W, rotateTest, IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		 im.associateAction(kbName,
		 * net.java.games.input.Component.Identifier.Key.S, moveBackA,
		 * IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		 * im.associateAction(kbName,
		 * net.java.games.input.Component.Identifier.Key.A, moveLeftA,
		 * IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		 * im.associateAction(kbName,
		 * net.java.games.input.Component.Identifier.Key.D, moveRightA,
		 * IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		 * 
		 * 
		 * im.associateAction(kbName,
		 * net.java.games.input.Component.Identifier.Key.NUMPAD9, upForwardA,
		 * IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		 * im.associateAction(kbName,
		 * net.java.games.input.Component.Identifier.Key.NUMPAD7, upBackA,
		 * IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		 * im.associateAction(kbName,
		 * net.java.games.input.Component.Identifier.Key.NUMPAD3, downForwardA,
		 * IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		 * im.associateAction(kbName,
		 * net.java.games.input.Component.Identifier.Key.NUMPAD1, downBackA,
		 * IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		 */
	}

	// pause and restart simulation
	private class pauseAction extends AbstractInputAction {
		public void performAction(float time, Event ev) {
			logger.debug("PAUSE " + pauseSimulation);
			pauseSimulation = true;
			aiRunner.pause();
			// pauseRunner();
			// error

		}
	}

	private class resumeAction extends AbstractInputAction {
		public void performAction(float time, Event evento) {
			logger.debug("pause is " + pauseSimulation);
			// resumeRunner();
			// error here
			pauseSimulation = false;
			aiRunner.resume();

		}
	}

	private class saveAction extends AbstractInputAction {
		public void performAction(float time, Event sp) {
			logger.debug("saveAction");
			/*
			 * if this thing is ran then check if cichlid is true like if
			 * (cichlidA != null) then set the flag in simfishs then if the
			 * objects do exist like (largePot != null)
			 * 
			 * the big issue is that you need to is get the time of the thing
			 */
			try {
				Connection conn;
				try {
					conn = DriverManager
							.getConnection("jdbc:ucanaccess://FishPool.accdb");

					Statement s = conn.createStatement();
					rs = s.executeQuery("SELECT ID FROM [ScenarioFlag]");
					while (rs.next()) {
						int a = s
								.executeUpdate("UPDATE ScenarioFlag set ScenarioNumber = 1 where ID = 6");
					}
					conn.close();
				} catch (SQLException Ex) {
					// TODO Auto-generated catch block
					Ex.printStackTrace();
				}
			} catch (SecurityException e5) {
				// TODO Auto-generated catch block
				e5.printStackTrace();
			}
			try {
				Connection connn;
				try {
					connn = DriverManager
							.getConnection("jdbc:ucanaccess://FishPool.accdb");
					Statement s = connn.createStatement();
					rs = s.executeQuery("SELECT ID FROM [SimulationFishS]");
					while (rs.next()) {
						/*
						 * i will have to figure out how to update the cichlid's
						 * x,y,z position
						 */
						if (cichlidA != null) {
							Point3D loc = new Point3D(cichlidA
									.getWorldTranslation().getCol(3));
							logger.debug("save flag for cichlidA");
							int a = s
									.executeUpdate("UPDATE SimulationFishS set fishID = 1 where ID = 1");
							int aa = s
									.executeUpdate("UPDATE FishPoolSaveState set StartingXPos = "
											+ loc.getX() + " where ID = 1");
							int aaa = s
									.executeUpdate("UPDATE FishPoolSaveState set StartingYPos = "
											+ loc.getY() + " where ID = 1");
							int aaaa = s
									.executeUpdate("UPDATE FishPoolSaveState set StartingZPos = "
											+ loc.getZ() + " where ID = 1");

						} else if (cichlidA == null) {
							int a = s
									.executeUpdate("UPDATE SimulationFishS set fishID = 0 where ID = 1");
						}
						if (cichlidB != null) {
							Point3D loc = new Point3D(cichlidB
									.getWorldTranslation().getCol(3));
							logger.debug("save flag for cichlidB");
							int a = s
									.executeUpdate("UPDATE SimulationFishS set fishID = 2 where ID = 2");
							int aa = s
									.executeUpdate("UPDATE FishPoolSaveState set StartingXPos = "
											+ loc.getX() + " where ID = 2");
							int aaa = s
									.executeUpdate("UPDATE FishPoolSaveState set StartingYPos = "
											+ loc.getY() + " where ID = 2");
							int aaaa = s
									.executeUpdate("UPDATE FishPoolSaveState set StartingZPos = "
											+ loc.getZ() + " where ID = 2");
						} else if (cichlidB == null) {
							int a = s
									.executeUpdate("UPDATE SimulationFishS set fishID = 0 where ID = 2");
						}
						if (cichlidC != null) {
							Point3D loc = new Point3D(cichlidC
									.getWorldTranslation().getCol(3));
							logger.debug("save flag for cichlidC");
							int a = s
									.executeUpdate("UPDATE SimulationFishS set fishID = 3 where ID = 3");
							int aa = s
									.executeUpdate("UPDATE FishPoolSaveState set StartingXPos = "
											+ loc.getX() + " where ID = 3");
							int aaa = s
									.executeUpdate("UPDATE FishPoolSaveState set StartingYPos = "
											+ loc.getY() + " where ID = 3");
							int aaaa = s
									.executeUpdate("UPDATE FishPoolSaveState set StartingZPos = "
											+ loc.getZ() + " where ID = 3");
						} else if (cichlidC == null) {
							int a = s
									.executeUpdate("UPDATE SimulationFishS set fishID = 0 where ID = 3");
						}

					}
					connn.close();
				} catch (Exception p1) {
					p1.printStackTrace();
				}

			} catch (Exception pp) {
				pp.printStackTrace();
			}
			try {
				Connection conne;
				try {
					conne = DriverManager
							.getConnection("jdbc:ucanaccess://FishPool.accdb");
					Statement s = conne.createStatement();
					rs = s.executeQuery("SELECT ID FROM [SimulationObjects]");
					while (rs.next()) {
						if (largePlant != null) {
							logger.debug("saving large plant");
							int a = s
									.executeUpdate("UPDATE SimulationObjectsS set objID = 1 where ID = 1");
						} else if (largePlant == null) {
							int a = s
									.executeUpdate("UPDATE SimulationObjectsS set objID = 0 where ID = 1");
						}
						if (largePot != null) {
							logger.debug("saving large pot");
							int b = s
									.executeUpdate("UPDATE SimulationObjectsS set objID = 4 where ID = 4");
						} else if (largePot == null) {
							int a = s
									.executeUpdate("UPDATE SimulationObjectsS set objID = 0 where ID = 4");
						}
						if (mediumPlant != null) {
							logger.debug("saving medium plant");
							int c = s
									.executeUpdate("UPDATE SimulationObjectsS set objID = 2 where ID = 2");
						} else if (mediumPlant == null) {
							int a = s
									.executeUpdate("UPDATE SimulationObjectsS set objID = 0 where ID = 2");
						}
						if (mediumPot != null) {
							logger.debug("saving medium pot");
							int d = s
									.executeUpdate("UPDATE SimulationObjectsS set objID = 5 where ID = 5");
						} else if (mediumPot == null) {
							int a = s
									.executeUpdate("UPDATE SimulationObjectsS set objID = 0 where ID = 5");
						}
						if (smallPlant != null) {
							logger.debug("saving small plant");
							int g = s
									.executeUpdate("UPDATE SimulationObjectsS set objID = 3 where ID = 3");
						} else if (smallPlant == null) {
							int a = s
									.executeUpdate("UPDATE SimulationObjectsS set objID = 0 where ID = 3");
						}
						if (smallPot != null) {
							logger.debug("saving small pot");
							int f = s
									.executeUpdate("UPDATE SimulationObjectsS set objID = 6 where ID = 6");
						} else if (smallPot == null) {
							int a = s
									.executeUpdate("UPDATE SimulationObjectsS set objID = 0 where ID = 6");
						}
					}
					conne.close();
				} catch (Exception p1) {
					p1.printStackTrace();
				}

			} catch (Exception pp) {
				pp.printStackTrace();
			}
		}

	}

	public void createFishTankWalls() {
		walls = fishTank.getFishWalls();
		addGameWorldObject(walls);
	}

	public void createFishTank() { // issue with this.
		addGameWorldObject(fishTank.getTerrain());
	}

	public void pauseUpdate(float elapsedTimeMS) {
		// creating timer thing
		time += elapsedTimeMS;
		if (timeString != null) {
			timeString.setText("Simulation is paused. Press R to resume.");
		}
		float timeCompare = time / 1000;

		Point3D camLoc = camera.getLocation();
		Matrix3D camT = new Matrix3D();
		camT.translate(camLoc.getX(), camLoc.getY(), camLoc.getZ());
		/*
		 * if (startAnimation == true) { // this should work
		 * startAnimationProcess(); startAnimation = false; }
		 */
		// update skybox loc

		// skybox.setLocalTranslation(camT);

		// iterating through models

		for (SceneNode s : getGameWorld()) {
			if (s instanceof Model3DTriMesh) {
				((Model3DTriMesh) s).stopAnimation();
				s.updateGeometricState(elapsedTimeMS, true);
			}
		}

		for (SceneNode s : objs) {
			Matrix3D cichlidAlocalT = s.getLocalTranslation();
			Matrix3D cichlidARot = s.getLocalRotation();

			s.setLocalTranslation(cichlidAlocalT);
			s.setLocalRotation(cichlidARot);
		}

		super.update(time);
		cc.update(time);

		startAnimation = true;
	}

	public void update(float elapsedTimeMS) // this will be where the objects
	{
		

		// creating timer thing
		time += elapsedTimeMS;
		if (timeString != null) {
			timeString.setText("Time: " + (int) Math.floor(time / 1000));
		}
		float timeCompare = time / 1000;

		Point3D camLoc = camera.getLocation();
		Matrix3D camT = new Matrix3D();
		camT.translate(camLoc.getX(), camLoc.getY(), camLoc.getZ());

		if (startAnimation == true) {
			// this should work
			startAnimationProcess();
			startAnimation = false;
		}

		// update skybox loc

		// skybox.setLocalTranslation(camT);

		// iterating through models
		
		try {
			CCSSemaphore.getSemaphore().acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (SceneNode s : getGameWorld()) {
			if (s instanceof Model3DTriMesh) {
				if (cichlidAObject != null) {
					if (s == cichlidAObject) {
						// System.out.println("i'm calling now!");
						((Model3DTriMesh) s).updateAnimation(elapsedTimeMS);
					}
				}
				if (cichlidBObject != null) {
					if (s == cichlidBObject) {
						// System.out.println("i'm fapping forward");
						((Model3DTriMesh) s).updateAnimation(elapsedTimeMS);
					}
				}
				if (cichlidCObject != null) {
					if (s == cichlidCObject) {
						// System.out.println("the world gone bad");
						((Model3DTriMesh) s).updateAnimation(elapsedTimeMS);
					}
				}
				s.updateGeometricState(elapsedTimeMS, true);
			}
		}

		for (SceneNode s : getGameWorld())
		{
			if (s instanceof ConvictCichlid)
			{
				if (((ConvictCichlid) s).getState() == FishState.IDLE)
				{
					for (int i = 0; i < 2; i++)
					{
					}
				}
			}
		}
		super.update(time);
		cc.update(time);
		for (SceneNode s : getGameWorld()) {
			if (s instanceof ConvictCichlid) // here will be where the objects
												// will
												// have be able to move, but i
												// will
												// implement that later.
			{
				if (s == cichlidA) {
					// s.translate(0, 0, .1f);
					// s.updateWorldBound();
					// bound collision
			//		System.out.println( "get the starting location A: " + ((ConvictCichlid) s).getLocation());
					
					if(((ConvictCichlid)s).getState() == FishState.IDLE){
						cichlidA.stop();
						
						for (int x = 0; x < 10000000; x++)
						{
							if ( x == 9000000)
							{
								
							System.out.println("FLY YOU FOOLS");
						//	cichlidA.turn(30, new Vector3D(1, 0, 0));
							cichlidA.setState(FishState.SWIM);
							}
						}
					}else if (cichlidA.getState() == FishState.SWIM){
						cichlidA.move(elapsedTimeMS);
					}
					
					Point3D test = new Point3D(s.getWorldTranslation().getCol(3));
					
					//System.out.println("what: "  + ((ConvictCichlid) s).getLocation().getX());
			//		System.out.println( "get the next location A1: " + test);
			//		System.out.println( "get the next location A2: " + ((ConvictCichlid) s).getLocation());
	
					// so the world translation doesn't work for this?
					
					
					// here is where i will test my newfound collision for
					// spheres

					Matrix3D cichlidAlocalT = s.getLocalTranslation();
					Matrix3D cichlidARot = s.getLocalRotation();
					aggroRangeA.setLocalTranslation(cichlidAlocalT);
					cichlidAObject.setLocalTranslation(cichlidAlocalT);
					cichlidAObject.setLocalRotation(cichlidARot);

					// object collision
					/*
					 * if (largePlantC == true) // ERROR { if
					 * (cichlidA.getWorldBound
					 * ().intersects(largePlant.getWorldBound())) {
					 * System.out.println("a hit largePl"); } }
					 */
//			//		System.out.println("s: "  + loc);
//					if (((ConvictCichlid) s).getLocation().getX() > 200 || ((ConvictCichlid) s).getLocation().getX() < 0)
//				//	if (test.getX() > 200 || test.getX() < 0)
//					{
//					//	System.out.println("x b");
//						System.out.println("Turning Loc AX: " + ((ConvictCichlid) s).getLocation());
//						((ConvictCichlid) s).turn(-30, new Vector3D(1, 0, 0));
//					}
//					if (((ConvictCichlid) s).getLocation().getY() > 200 || ((ConvictCichlid) s).getLocation().getY() < 0)
//				//	if (test.getY() > 200 || test.getZ() < 0)
//					{
//						// rotate
//					//	System.out.println("y b");
//						System.out.println("Turning Loc AY: " + ((ConvictCichlid) s).getLocation());
//						((ConvictCichlid) s).turn(-30, new Vector3D(0, 1, 0));
//					}
//					if (((ConvictCichlid) s).getLocation().getZ() > 200 || ((ConvictCichlid) s).getLocation().getZ() < 0)
//				//	if (test.getZ() > 200 || test.getZ() < 0)
//					{
//						System.out.println("Turning Loc AZ: " + ((ConvictCichlid) s).getLocation());
//						((ConvictCichlid) s).turn(-30, new Vector3D(0, 0, 1));
//					//	System.out.println("z b");
//					}
					
					if (largePotC == true) {
						if (cichlidA.getWorldBound().intersects(
								largePot.getWorldBound())) {
							System.out.println("a hit largePot");
						}
					}
					if (largePlantC == true) // ERROR
					{
						if (cichlidA.getWorldBound().intersects(
								largePlant.getWorldBound())) {
							System.out.println("a hit largePl");
						}
					}
					/*
					 * if (largePlantC == true) { Point3D largePlantloc = new
					 * Point3D(largePlant.getWorldTranslation().getCol(3)); if
					 * ((loc.getX() == largePlantloc.getX()) && (loc.getY() ==
					 * largePlantloc.getY()) && (loc.getZ() ==
					 * largePlantloc.getZ()) ) {
					 * System.out.println("b hit large plant"); } }
					 */
					if (mediumPotC == true) {
						if (cichlidA.getWorldBound().intersects(
								mediumPot.getWorldBound())) {
							System.out.println("a hit med pot");
						}
					}
					if (mediumPlantC == true) {
						if (cichlidA.getWorldBound().intersects(
								mediumPlant.getWorldBound())) {
							System.out.println("a hit med pl");
						}
					}
					if (smallPlantC == true) {
						if (cichlidA.getWorldBound().intersects(
								smallPlant.getWorldBound())) {
							System.out.println("a hit small pla");
						}
					}
					if (smallPotC == true) {
						if (cichlidA.getWorldBound().intersects(
								smallPot.getWorldBound())) {
							System.out.println("a hit small pot");
						}
					}
					// cichlid collision
					if (cichlidB != null) {
						if (cichlidA.getWorldBound().intersects(
								cichlidB.getWorldBound())) {
							System.out.println("a hits b");
							// this is where shit goes down
						}
						if (aggroRangeA.getWorldBound().intersects(
								aggroRangeB.getWorldBound())) {
							System.out.println("aggro from a to B");
						}
					}
					if (cichlidC != null) {
						if (cichlidA.getWorldBound().intersects(
								cichlidC.getWorldBound())) {
							System.out.println("a hits c");
							// this is where shit goes down
						}
						if (aggroRangeA.getWorldBound().intersects(
								aggroRangeC.getWorldBound())) {
							System.out.println("aggro from a to C");
						}
					}

				}
				if (s == cichlidB) {

					// call move stuff here
					if(((ConvictCichlid)s).getState() == FishState.IDLE){
					//	((ConvictCichlid) s).move(0);
						cichlidB.stop();
						logger.debug("{} MOVED 0", ((ConvictCichlid)s).getName());
						
						for (int x = 0; x < 10000000; x++)
						{
							if ( x == 9000000)
							{
								
							System.out.println("FLY YOU FOOLS");
						//	cichlidA.turn(30, new Vector3D(1, 0, 0));
							cichlidB.setState(FishState.SWIM);
							}
						}
					}else if (cichlidB.getState() == FishState.SWIM){
						logger.debug("{} MOVED 1", ((ConvictCichlid)s).getName());
						cichlidB.move(elapsedTimeMS);
					}
					Matrix3D cichlidBlocalT = s.getLocalTranslation();
					Matrix3D cichlidBRot = s.getLocalRotation();
					cichlidBObject.setLocalTranslation(cichlidBlocalT);
					cichlidBObject.setLocalRotation(cichlidBRot);
					aggroRangeB.setLocalTranslation(cichlidBlocalT);

					if (largePotC == true) {
						if (cichlidB.getWorldBound().intersects(
								largePot.getWorldBound())) {
							// System.out.println("b hit largePo");
						}
					}

					if (largePlantC == true) {
						if (cichlidB.getWorldBound().intersects(
								largePlant.getWorldBound())) {
							// System.out.println("b hit largePl");
						}
					}
					/*
					 * if (largePlantC == true) { Point3D largePlantloc = new
					 * Point3D(largePlant.getWorldTranslation().getCol(3)); if
					 * ((loc.getX() == largePlantloc.getX()) && (loc.getY() ==
					 * largePlantloc.getY()) && (loc.getZ() ==
					 * largePlantloc.getZ()) ) {
					 * System.out.println("b hit large plant"); } }
					 */
					if (mediumPotC == true) {
						if (cichlidB.getWorldBound().intersects(
								mediumPot.getWorldBound())) {
							// System.out.println("b hit medP");
						}
					}
					if (mediumPlantC == true) {
						if (cichlidB.getWorldBound().intersects(
								mediumPlant.getWorldBound())) {
							// System.out.println("b hit medPL");
						}
					}
					if (smallPlantC == true) {
						if (cichlidB.getWorldBound().intersects(
								smallPlant.getWorldBound())) {
							// System.out.println("b hit smallPl");
						}
					}
					if (smallPotC == true) {
						if (cichlidB.getWorldBound().intersects(
								smallPot.getWorldBound())) {
							// System.out.println("b hit smallPot");
						}
					}
					// cichlid collision
					if (cichlidA != null) {
						if (cichlidB.getWorldBound().intersects(
								cichlidA.getWorldBound())) {
							// System.out.println("b hits a");
							// this is where shit goes down
						}
						if (aggroRangeB.getWorldBound().intersects(
								aggroRangeA.getWorldBound())) {
							// System.out.println("aggro from B to A");
						}
					}
					if (cichlidC != null) {
						if (cichlidB.getWorldBound().intersects(
								cichlidC.getWorldBound())) {
							// System.out.println("b hits c");
							// this is where shit goes down
						}
						if (aggroRangeB.getWorldBound().intersects(
								aggroRangeC.getWorldBound())) {
							// System.out.println("aggro from B to C");
						}
					}
				}
				if (s == cichlidC) {
					// call move stuff here
					Point3D loc = new Point3D(s.getWorldTranslation().getCol(3));
					
					if(cichlidC.getState() == FishState.IDLE){
					//	((ConvictCichlid) s).move(0);
						cichlidC.stop();
						logger.debug("{} MOVED 0", ((ConvictCichlid)s).getName());
					}else{
						logger.debug("{} MOVED 1", ((ConvictCichlid)s).getName());
						cichlidC.move(elapsedTimeMS);
					}
					Matrix3D cichlidClocalT = s.getLocalTranslation();
					Matrix3D cichlidCRot = s.getLocalRotation();
					aggroRangeC.setLocalTranslation(cichlidClocalT);
					cichlidCObject.setLocalTranslation(cichlidClocalT);
					cichlidCObject.setLocalRotation(cichlidCRot);

					
					if (largePotC == true) {
						if (cichlidC.getWorldBound().intersects(
								largePot.getWorldBound())) {
							// System.out.println("c hit large pot");
						}
					}

					if (largePlantC == true) {
						if (cichlidC.getWorldBound().intersects(
								largePlant.getWorldBound())) {
							// System.out.println("c hit large plant");
						}
					}
					/*
					 * if (largePlantC == true) { Point3D largePlantloc = new
					 * Point3D(largePlant.getWorldTranslation().getCol(3)); if
					 * ((loc.getX() == largePlantloc.getX()) && (loc.getY() ==
					 * largePlantloc.getY()) && (loc.getZ() ==
					 * largePlantloc.getZ()) ) {
					 * System.out.println("C hit large plant"); } }
					 */
					if (mediumPotC == true) {
						if (cichlidC.getWorldBound().intersects(
								mediumPot.getWorldBound())) {
							// System.out.println("c hit medium pot");
						}
					}
					if (mediumPlantC == true) {
						if (cichlidC.getWorldBound().intersects(
								mediumPlant.getWorldBound())) {
							// System.out.println("c hit medium plant");
						}
					}
					if (smallPlantC == true) {
						if (cichlidC.getWorldBound().intersects(
								smallPlant.getWorldBound())) {
							// System.out.println("c hit small plant");
						}
					}
					if (smallPotC == true) {
						if (cichlidC.getWorldBound().intersects(
								smallPot.getWorldBound())) {
							// System.out.println("c hit small pot");
						}
					}
					// cichlid collision
					if (cichlidA != null) {
						if (cichlidC.getWorldBound().intersects(
								cichlidA.getWorldBound())) {
							// System.out.println("c hits a");
							// this is where shit goes down
						}
						if (aggroRangeC.getWorldBound().intersects(
								aggroRangeA.getWorldBound())) {
							// System.out.println("aggro from C to A");
						}
					}
					if (cichlidB != null) {
						if (cichlidC.getWorldBound().intersects(
								cichlidA.getWorldBound())) {
							// System.out.println("c hits b");
							// this is where shit goes down
						}
						if (aggroRangeC.getWorldBound().intersects(
								aggroRangeB.getWorldBound())) {
							// System.out.println("aggro from C to B");
						}
					}
				}

			}

		}
		CCSSemaphore.getSemaphore().release();

	}

	private IDisplaySystem createDisplaySystem() {
		IDisplaySystem display = new MyDisplaySystem(1000, 500, 24, 20, false,
				"sage.renderer.jogl.JOGLRenderer");
		logger.debug("Waiting for display creation...");
		int count = 0;
		// wait until display creation completes or a timeout occurs
		// while (!display.isCreated()) {
		// try {
		// Thread.sleep(10);
		// } catch (InterruptedException e) {
		// throw new RuntimeException("Display creation interrupted");
		// }
		// count++;
		// System.out.print("+");
		// if (count % 80 == 0) {
		// System.out.println();
		// }
		// if (count > 2000) // 20 seconds (approx.)
		// {
		// throw new RuntimeException("Unable to create display");
		// }
		// }
		// System.out.println();
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
			// stopRunner();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		// Stop the ai loop, and shutdown the executor service
		// 	so there will be no zombie threads lingering
		//	after the program closes.
		aiRunner.stop();
		// Block until the aiRunner has quit
		while(!aiRunner.isEnded());
		
		AIExecutor.shutdown();
		
		boolean shutdown = false;
		try {
			shutdown = AIExecutor.awaitTermination(3, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(!shutdown){
				logger.debug("Forcing shutdown of thread!");
				AIExecutor.shutdownNow();
			}
		}
		
		logger.info("BaseGame.shutdown() invoked...");
		if (DisplaySystem.getCurrentDisplaySystem() != null) {
			DisplaySystem.getCurrentDisplaySystem().close();
			logger.info("BaseGame.shutdown() invoked...");

		}
		System.exit(0);
	}

	public void setPauseSim(boolean b) {
		pauseSimulation = b;

	}

	public boolean getPauseSim() {
		return pauseSimulation;
	}

	public void setAnimation(boolean b) {
		startAnimation = b;
	}

	public boolean getAnimationBool() {
		return startAnimation;
	}

	protected void mainLoop() {
		long startTime = System.nanoTime();
		long lastUpdateTime = startTime;
		while (!isGameOver()) {
			long frameStartTime = System.nanoTime();
			float elapsedMilliSecs = (float) (frameStartTime - lastUpdateTime) / 1000000.0F;
			lastUpdateTime = frameStartTime;

			handleInput(elapsedMilliSecs);
			if (pauseSimulation == true) {
				pauseUpdate(elapsedMilliSecs);

			} else if (pauseSimulation == false) {
				update(elapsedMilliSecs);
			}
			render();

			DisplaySystem.getCurrentDisplaySystem().getRenderer().swapBuffers();

			Thread.yield();
		}
	}
	
	public void startAIImplementation()
	{
		aiRunner = new AIRunner(new AIController(this));
		AIExecutor.execute(aiRunner);
	}
	
	public ConvictCichlid getCichlidA()
	{
		return cichlidA;
	}
	public ConvictCichlid getCichlidB()
	{
		return cichlidB;
	}
	public ConvictCichlid getCichlidC()
	{
		return cichlidC;
	}
	
	public TriMesh getLargePlant()
	{
		return largePlant;
	}
	public TriMesh getMediumPlant()
	{
		return mediumPlant;
	}
	public TriMesh getSmallPlant()
	{
		return smallPlant;
	}
	public TriMesh getLargePot()
	{
		return largePot;
	}
	public TriMesh getMediumPot()
	{
		return mediumPot;
	}
	public TriMesh getSmallPot()
	{
		return smallPot;
	}
	public Sphere getAggroRangeA()
	{
		return aggroRangeA;
	}
	public Sphere getAggroRangeB()
	{
		return aggroRangeB;
	}
	public Sphere getAggroRangeC()
	{
		return aggroRangeC;
	}
	
	
}
