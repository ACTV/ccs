package actv.ccs.sageTest;

import actv.ccs.listener.RuleEngineRunner;
import actv.ccs.model.CCSMemoryObject;
import actv.ccs.model.ConvictCichlid;
import actv.ccs.model.type.FishState;
import actv.ccs.sageTest.actions.QuitAction;
import graphicslib3D.Matrix3D;
import graphicslib3D.Point3D;
import graphicslib3D.Vector3D;

import java.awt.Color;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

import net.java.games.input.Component;
import net.java.games.input.Component.Identifier.Key;
import net.java.games.input.Event;
import sage.app.BaseGame;
import sage.camera.ICamera;
import sage.display.IDisplaySystem;
import sage.input.IInputManager;
import sage.input.IInputManager.INPUT_ACTION_TYPE;
import sage.input.InputManager;
import sage.input.action.AbstractInputAction;
import sage.input.action.IAction;
import sage.model.loader.OBJLoader;
import sage.model.loader.ogreXML.OgreXMLParser;
import sage.renderer.IRenderer;
import sage.scene.Group;
import sage.scene.HUDString;
import sage.scene.Model3DTriMesh;
import sage.scene.SceneNode;
import sage.scene.SceneNode.CULL_MODE;
import sage.scene.SkyBox;
import sage.scene.TriMesh;
import sage.scene.bounding.BoundingVolume;
import sage.scene.shape.Line;
import sage.scene.shape.Rectangle;
import sage.scene.shape.Sphere;
import sage.scene.state.RenderState;
import sage.scene.state.RenderState.RenderStateType;
import sage.scene.state.TextureState;
import sage.terrain.TerrainBlock;
import sage.texture.Texture;
import sage.texture.Texture.ApplyMode;
import sage.texture.TextureManager;

public class MyGame
  extends BaseGame
{
  IDisplaySystem display;
  IInputManager im;
  private TriMesh largePlant;
  private TriMesh mediumPlant;
  private TriMesh smallPlant;
  private TriMesh largePot;
  private TriMesh mediumPot;
  private TriMesh smallPot;
  private TriMesh cichlidAMesh;
  private TriMesh cichlidBMesh;
  private TriMesh cichlidCMesh;
  private ICamera camera;
  private CameraOrbit cc;
  private SkyBox skybox;
  private Connection conn;
  private ResultSet rs;
  private ResultSet rsI;
  private TerrainBlock floor;
  private Texture skyThing;
  private Rectangle ground;
  private Rectangle leftWall;
  private Rectangle rightWall;
  private Rectangle ceiling;
  private Rectangle backWall;
  private Rectangle frontWall;
  private ConvictCichlid cichlidA;
  private ConvictCichlid cichlidB;
  private ConvictCichlid cichlidC;
  private SceneNode cameraGuy;
  private Line yAxis1;
  private Line zYPAxis;
  private Line zyPtoxEnd3;
  private Line pPart;
  private Line zPart;
  private Line yEndtoZPart;
  private Line xEndtoZPart;
  private Line xxPart;
  private Line finishPart;
  private RuleEngineRunner runner;
  private ArrayList<CCSMemoryObject> objs = new ArrayList();
  private boolean largePotC;
  private boolean mediumPotC;
  private boolean smallPotC;
  private boolean largePlantC;
  private boolean mediumPlantC;
  private boolean smallPlantC;
  private float simulationTime = 100.0F;
  private float time = 0.0F;
  private int cichlidCount = 0;
  private int objCount = 0;
  private HUDString timeString;
  private Sphere aggroRangeA;
  private Sphere aggroRangeB;
  private Sphere aggroRangeC;
  private Group fishWalls;
  private IRenderer renderer;
  private volatile boolean pauseSimulation;
  private FishTank fishTank;
  private boolean startAnimation = true;
  TextureState testState;
  Group model;
  Model3DTriMesh cichlidAObject;
  Model3DTriMesh cichlidBObject;
  Model3DTriMesh cichlidCObject;
  private Iterator<SceneNode> modelIterator;
  
  public void initGame()
  {
    try
    {
      this.conn = DriverManager.getConnection("jdbc:ucanaccess://FishPool.accdb");
      
      Statement s = this.conn.createStatement();
      this.rs = s.executeQuery("SELECT scenarioNumber FROM [ScenarioFlag]");
      while (this.rs.next())
      {
        String scenNum = this.rs.getString("ScenarioNumber");
        

        int scenGrab = Integer.parseInt(scenNum);
        if ((scenGrab == 1) || (scenGrab == 2) || (scenGrab == 3) || (scenGrab == 4) || (scenGrab == 5) || (scenGrab == 6))
        {
          this.fishTank = new FishTankImpl();
        }
        else
        {
          IDisplaySystem display = getDisplaySystem();
          display.setTitle("Empty Window where the Sun don't shine apparently.");
          this.camera = display.getRenderer().getCamera();
          this.camera.setPerspectiveFrustum(45.0D, 1.0D, 0.01D, 1000.0D);
          this.camera.setLocation(new Point3D(1.0D, 1.0D, 20.0D));
          System.out.println("no scenario in place?");
          this.pauseSimulation = true;
          this.startAnimation = true;
          this.cichlidCount = 0;
          this.objCount = 0;
          createPerson();
        }
      }
    }
    catch (Exception epp)
    {
      epp.printStackTrace();
    }
  }
  
  public void startAnimationProcess()
  {
    for (SceneNode s : getGameWorld()) {
      if ((s instanceof Model3DTriMesh))
      {
        if (s == this.cichlidAObject) {
          ((Model3DTriMesh)s).startAnimation("swimmingAction");
        }
        if (s == this.cichlidBObject) {
          ((Model3DTriMesh)s).startAnimation("swimmingAction");
        }
        if (s == this.cichlidCObject) {
          ((Model3DTriMesh)s).startAnimation("swimmingAction");
        }
      }
    }
  }
  
  public void startRunner()
  {
    this.runner = RuleEngineRunner.getInstance();
    this.runner.newMap(this.objs);
    this.runner.start();
  }
  
  private void pauseRunner()
  {
    this.runner.pauseSession();
  }
  
  private void resumeRunner()
  {
    this.runner.resumeSession();
  }
  
  private void stopRunner()
  {
    try
    {
      this.runner.closeSession();
      this.runner.join();
    }
    catch (InterruptedException e)
    {
      throw new RuntimeException("Unable to end the rule session thread!");
    }
  }
  
  public void initObjects()
  {
    this.display = getDisplaySystem();
    this.display.setTitle("sage implementation of the pain");
    
    this.camera = this.display.getRenderer().getCamera();
    this.camera.setPerspectiveFrustum(45.0D, 1.0D, 0.01D, 1000.0D);
    this.camera.setLocation(new Point3D(1.0D, 1.0D, 20.0D));
    


    Point3D origin = new Point3D(0.0D, 0.0D, 0.0D);
    Point3D xEnd1 = new Point3D(200.0D, 200.0D, 0.0D);
    Point3D xEnd3 = new Point3D(200.0D, 200.0D, 200.0D);
    Point3D xEnd2 = new Point3D(200.0D, 0.0D, 200.0D);
    Point3D zyP = new Point3D(0.0D, 200.0D, 200.0D);
    Point3D xEnd = new Point3D(200.0D, 0.0D, 0.0D);
    Point3D yEnd = new Point3D(0.0D, 200.0D, 0.0D);
    Point3D zEnd = new Point3D(0.0D, 0.0D, 200.0D);
    

    Line xAxis = new Line(origin, xEnd, Color.black, 2);
    Line yAxis = new Line(origin, yEnd, Color.black, 2);
    Line zAxis = new Line(origin, zEnd, Color.black, 2);
    

    this.yAxis1 = new Line(xEnd2, xEnd3, Color.black, 2);
    this.zyPtoxEnd3 = new Line(new Point3D(200.0D, 0.0D, 0.0D), new Point3D(200.0D, 200.0D, 0.0D), 
      Color.black, 2);
    this.pPart = new Line(new Point3D(200.0D, 0.0D, 0.0D), new Point3D(200.0D, 0.0D, 200.0D), 
      Color.black, 2);
    this.finishPart = new Line(new Point3D(0.0D, 200.0D, 0.0D), new Point3D(200.0D, 200.0D, 0.0D), 
      Color.black, 2);
    this.yEndtoZPart = new Line(yEnd, new Point3D(0.0D, 200.0D, 200.0D), Color.black, 2);
    this.xEndtoZPart = new Line(new Point3D(0.0D, 200.0D, 200.0D), new Point3D(200.0D, 200.0D, 
      200.0D), Color.black, 2);
    this.xxPart = new Line(new Point3D(200.0D, 200.0D, 0.0D), new Point3D(200.0D, 200.0D, 200.0D), 
      Color.black, 2);
    this.zPart = new Line(zEnd, xEnd2, Color.black, 2);
    this.zYPAxis = new Line(zEnd, zyP, Color.black, 2);
    

    addGameWorldObject(this.yAxis1);
    this.yAxis1.updateWorldBound();
    addGameWorldObject(this.zYPAxis);
    this.zYPAxis.updateWorldBound();
    addGameWorldObject(this.zyPtoxEnd3);
    this.zyPtoxEnd3.updateWorldBound();
    addGameWorldObject(this.pPart);
    this.pPart.updateWorldBound();
    addGameWorldObject(this.zPart);
    this.zPart.updateWorldBound();
    addGameWorldObject(this.yEndtoZPart);
    this.yEndtoZPart.updateWorldBound();
    addGameWorldObject(this.xEndtoZPart);
    this.xEndtoZPart.updateWorldBound();
    addGameWorldObject(this.xxPart);
    this.xxPart.updateWorldBound();
    addGameWorldObject(this.finishPart);
    this.finishPart.updateWorldBound();
    
    addGameWorldObject(xAxis);
    addGameWorldObject(yAxis);
    addGameWorldObject(zAxis);
    
    this.largePlantC = false;
    this.mediumPlantC = false;
    this.smallPlantC = false;
    this.largePotC = false;
    this.mediumPotC = false;
    this.smallPotC = false;
  }
  
  public void createPerson()
  {
    this.cameraGuy = new CameraGuy();
    this.cameraGuy.translate(100.0F, 100.0F, 500.0F);
    this.cameraGuy.scale(-1.0F, -1.0F, -1.0F);
    this.cameraGuy.rotate(180.0F, new Vector3D(0.0D, 1.0D, 0.0D));
    addGameWorldObject(this.cameraGuy);
    this.cameraGuy.updateWorldBound();
  }
  
  public void createHUD()
  {
    this.timeString = new HUDString("Time = " + this.time);
    this.timeString.setLocation(0.0D, 0.05D);
    addGameWorldObject(this.timeString);
  }
  
  public void setPauseSim(boolean b)
  {
    this.pauseSimulation = b;
  }
  
  public boolean getPause()
  {
    return this.pauseSimulation;
  }
  
  public void pauseGame()
  {
    this.pauseSimulation = true;
    pauseRunner();
  }
  
  public void resumeGame()
  {
    this.pauseSimulation = false;
    resumeRunner();
  }
  
  public void setUpTank()
  {
    try
    {
      this.conn = DriverManager.getConnection("jdbc:ucanaccess://FishPool.accdb");
      
      Statement s = this.conn.createStatement();
      this.rs = s.executeQuery("SELECT * FROM [TankData] WHERE ID = 1");
      while (this.rs.next())
      {
        String timeGrab = this.rs.getString("Time");
        
        float timeParse = Float.parseFloat(timeGrab);
        
        this.simulationTime = timeParse;
        System.out.println("Here is the simulationTime! " + this.simulationTime);
      }
    }
    catch (Exception epp)
    {
      epp.printStackTrace();
    }
  }
  
  public void spawnObjects()
  {
    try
    {
      this.conn = 
        DriverManager.getConnection("jdbc:ucanaccess://FishPool.accdb");
      
      Statement s = this.conn.createStatement();
      this.rs = s.executeQuery("SELECT objID FROM [SimulationObjects]");
      while (this.rs.next())
      {
        String id = this.rs.getString("objID");
        
        int idS = Integer.parseInt(id);
        
        System.out.println(idS);
        if (id.equals("1"))
        {
          this.rsI = s.executeQuery("SELECT * FROM [Objects] WHERE Name='Large Plant'");
          while (this.rsI.next())
          {
            String name = this.rsI.getString("Name");
            
            String type = this.rsI.getString("Type");
            String length = this.rsI.getString("Length");
            String width = this.rsI.getString("Width");
            String height = this.rsI.getString("Height");
            String xLocS = this.rsI.getString("StartingXPos");
            String yLocS = this.rsI.getString("StartingYPos");
            String zLocS = this.rsI.getString("StartingZPos");
            
            float lengthW = Float.parseFloat(length);
            float widthW = Float.parseFloat(width);
            float heightW = Float.parseFloat(height);
            double xStartW = Double.parseDouble(xLocS);
            double yStartY = Double.parseDouble(yLocS);
            double zStartZ = Double.parseDouble(zLocS);
            
            OBJLoader loader = new OBJLoader();
            this.largePlant = loader
              .loadModel("plantBlend.obj");
            this.largePlant.setName(name);
            Matrix3D largePlantT = this.largePlant.getLocalTranslation();
            


            largePlantT.translate(xStartW, yStartY, zStartZ);
            this.largePlant.setLocalTranslation(largePlantT);
            Matrix3D largePlantS = this.largePlant.getLocalScale();
            




            largePlantS.scale(lengthW, widthW, heightW);
            






            this.largePlant.setLocalScale(largePlantS);
            
            addGameWorldObject(this.largePlant);
            this.largePlant.updateLocalBound();
            this.largePlant.updateGeometricState(0.0D, true);
            this.largePlant.updateWorldBound();
            this.largePlantC = true;
            this.objCount += 1;
          }
        }
        else if (id.equals("2"))
        {
          this.rsI = s.executeQuery("SELECT * FROM [Objects] WHERE Name='Medium Plant'");
          while (this.rsI.next())
          {
            String name = this.rsI.getString("Name");
            
            String type = this.rsI.getString("Type");
            String length = this.rsI.getString("Length");
            String width = this.rsI.getString("Width");
            String height = this.rsI.getString("Height");
            String xLocS = this.rsI.getString("StartingXPos");
            String yLocS = this.rsI.getString("StartingYPos");
            String zLocS = this.rsI.getString("StartingZPos");
            
            float lengthW = Float.parseFloat(length);
            float widthW = Float.parseFloat(width);
            float heightW = Float.parseFloat(height);
            double xStartW = Double.parseDouble(xLocS);
            double yStartY = Double.parseDouble(yLocS);
            double zStartZ = Double.parseDouble(zLocS);
            
            OBJLoader loader1 = new OBJLoader();
            this.mediumPlant = loader1
              .loadModel("plantBlend.obj");
            this.mediumPlant.setName(name);
            Matrix3D mediumPlantT = this.mediumPlant
              .getLocalTranslation();
            mediumPlantT.translate(xStartW, yStartY, zStartZ);
            this.mediumPlant.setLocalTranslation(mediumPlantT);
            Matrix3D mediumPlantS = this.mediumPlant.getLocalScale();
            




            mediumPlantS.scale(lengthW, widthW, heightW);
            






            this.mediumPlant.setLocalScale(mediumPlantS);
            
            addGameWorldObject(this.mediumPlant);
            this.mediumPlant.updateLocalBound();
            this.mediumPlant.updateGeometricState(0.0D, true);
            this.mediumPlant.updateWorldBound();
            this.mediumPlantC = true;
            this.objCount += 1;
          }
        }
        else if (id.equals("3"))
        {
          this.rsI = s.executeQuery("SELECT * FROM [Objects] WHERE Name='Small Plant'");
          while (this.rsI.next())
          {
            String name = this.rsI.getString("Name");
            
            String type = this.rsI.getString("Type");
            String length = this.rsI.getString("Length");
            String width = this.rsI.getString("Width");
            String height = this.rsI.getString("Height");
            String xLocS = this.rsI.getString("StartingXPos");
            String yLocS = this.rsI.getString("StartingYPos");
            String zLocS = this.rsI.getString("StartingZPos");
            
            float lengthW = Float.parseFloat(length);
            float widthW = Float.parseFloat(width);
            float heightW = Float.parseFloat(height);
            double xStartW = Double.parseDouble(xLocS);
            double yStartY = Double.parseDouble(yLocS);
            double zStartZ = Double.parseDouble(zLocS);
            
            OBJLoader loader2 = new OBJLoader();
            this.smallPlant = loader2
              .loadModel("plantBlend.obj");
            this.smallPlant.setName(name);
            Matrix3D smallPlantT = this.smallPlant.getLocalTranslation();
            


            smallPlantT.translate(xStartW, yStartY, zStartZ);
            this.smallPlant.setLocalTranslation(smallPlantT);
            Matrix3D smallPlantS = this.smallPlant.getLocalScale();
            




            smallPlantS.scale(lengthW, widthW, heightW);
            






            this.smallPlant.setLocalScale(smallPlantS);
            
            addGameWorldObject(this.smallPlant);
            this.smallPlant.updateLocalBound();
            this.smallPlant.updateGeometricState(0.0D, true);
            this.smallPlant.updateWorldBound();
            this.smallPlantC = true;
            this.objCount += 1;
          }
        }
        else if (id.equals("4"))
        {
          this.rsI = s.executeQuery("SELECT * FROM [Objects] WHERE Name='Large Pot'");
          while (this.rsI.next())
          {
            String name = this.rsI.getString("Name");
            
            String type = this.rsI.getString("Type");
            String length = this.rsI.getString("Length");
            String width = this.rsI.getString("Width");
            String height = this.rsI.getString("Height");
            String xLocS = this.rsI.getString("StartingXPos");
            String yLocS = this.rsI.getString("StartingYPos");
            String zLocS = this.rsI.getString("StartingZPos");
            
            float lengthW = Float.parseFloat(length);
            float widthW = Float.parseFloat(width);
            float heightW = Float.parseFloat(height);
            double xStartW = Double.parseDouble(xLocS);
            double yStartY = Double.parseDouble(yLocS);
            double zStartZ = Double.parseDouble(zLocS);
            
            OBJLoader loader3 = new OBJLoader();
            this.largePot = loader3
              .loadModel("potBlend.obj");
            this.largePot.setName(name);
            Matrix3D largePotT = this.largePot.getLocalTranslation();
            


            largePotT.translate(xStartW, yStartY, zStartZ);
            this.largePot.setLocalTranslation(largePotT);
            Matrix3D largePotS = this.largePot.getLocalScale();
            




            largePotS.scale(lengthW, widthW, heightW);
            



            this.largePot.setLocalScale(largePotS);
            
            addGameWorldObject(this.largePot);
            this.largePot.updateLocalBound();
            this.largePot.updateGeometricState(0.0D, true);
            this.largePot.updateWorldBound();
            this.largePotC = true;
            this.objCount += 1;
          }
        }
        else if (id.equals("5"))
        {
          this.rsI = s.executeQuery("SELECT * FROM [Objects] WHERE Name='Medium Pot'");
          while (this.rsI.next())
          {
            String name = this.rsI.getString("Name");
            
            String type = this.rsI.getString("Type");
            String length = this.rsI.getString("Length");
            String width = this.rsI.getString("Width");
            String height = this.rsI.getString("Height");
            String xLocS = this.rsI.getString("StartingXPos");
            String yLocS = this.rsI.getString("StartingYPos");
            String zLocS = this.rsI.getString("StartingZPos");
            
            float lengthW = Float.parseFloat(length);
            float widthW = Float.parseFloat(width);
            float heightW = Float.parseFloat(height);
            double xStartW = Double.parseDouble(xLocS);
            double yStartY = Double.parseDouble(yLocS);
            double zStartZ = Double.parseDouble(zLocS);
            
            OBJLoader loader4 = new OBJLoader();
            this.mediumPot = loader4
              .loadModel("potBlend.obj");
            this.mediumPot.setName(name);
            Matrix3D mediumPotT = this.mediumPot.getLocalTranslation();
            


            mediumPotT.translate(xStartW, yStartY, zStartZ);
            this.mediumPot.setLocalTranslation(mediumPotT);
            Matrix3D mediumPotS = this.mediumPot.getLocalScale();
            




            mediumPotS.scale(lengthW, widthW, heightW);
            



            this.mediumPot.setLocalScale(mediumPotS);
            
            addGameWorldObject(this.mediumPot);
            this.mediumPot.updateLocalBound();
            this.mediumPot.updateGeometricState(0.0D, true);
            this.mediumPot.updateWorldBound();
            this.mediumPotC = true;
            this.objCount += 1;
          }
        }
        else if (id.equals("6"))
        {
          this.rsI = s.executeQuery("SELECT * FROM [Objects] WHERE Name='Small Pot'");
          while (this.rsI.next())
          {
            String name = this.rsI.getString("Name");
            
            String type = this.rsI.getString("Type");
            String length = this.rsI.getString("Length");
            String width = this.rsI.getString("Width");
            String height = this.rsI.getString("Height");
            String xLocS = this.rsI.getString("StartingXPos");
            String yLocS = this.rsI.getString("StartingYPos");
            String zLocS = this.rsI.getString("StartingZPos");
            
            float lengthW = Float.parseFloat(length);
            float widthW = Float.parseFloat(width);
            float heightW = Float.parseFloat(height);
            double xStartW = Double.parseDouble(xLocS);
            double yStartY = Double.parseDouble(yLocS);
            double zStartZ = Double.parseDouble(zLocS);
            
            OBJLoader loader5 = new OBJLoader();
            this.smallPot = loader5
              .loadModel("potBlend.obj");
            this.smallPot.setName(name);
            Matrix3D smallPotT = this.smallPot.getLocalTranslation();
            


            smallPotT.translate(xStartW, yStartY, zStartZ);
            this.smallPot.setLocalTranslation(smallPotT);
            Matrix3D smallPotS = this.smallPot.getLocalScale();
            




            smallPotS.scale(lengthW, widthW, heightW);
            



            this.smallPot.setLocalScale(smallPotS);
            
            addGameWorldObject(this.smallPot);
            this.smallPot.updateLocalBound();
            this.smallPot.updateGeometricState(0.0D, true);
            this.smallPot.updateWorldBound();
            this.smallPotC = true;
            this.objCount += 1;
          }
        }
      }
      this.conn.close();
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
  }
  
  public void spawnCichlids()
  {
    Texture cichlidTexA = TextureManager.loadTexture2D("cichlidMesh.png");
    try
    {
      this.conn = 
        DriverManager.getConnection("jdbc:ucanaccess://FishPool.accdb");
      
      Statement s = this.conn.createStatement();
      this.rs = s.executeQuery("SELECT fishID FROM [SimulationFish]");
      while (this.rs.next())
      {
        String id = this.rs.getString("fishID");
        
        int idS = Integer.parseInt(id);
        
        System.out.println(idS);
        if (id.equals("1"))
        {
          this.rsI = s.executeQuery("SELECT * FROM [FishPool] WHERE Type='Fish A'");
          while (this.rsI.next())
          {
            String name = this.rsI.getString("Type");
            

            String weight = this.rsI.getString("Weight");
            String width = this.rsI.getString("Width");
            String height = this.rsI.getString("Height");
            String gender = this.rsI.getString("Gender");
            String aggro = this.rsI.getString("AggroLevel");
            
            String xLocS = this.rsI.getString("StartingXPos");
            String yLocS = this.rsI.getString("StartingYPos");
            String zLocS = this.rsI.getString("StartingZPos");
            

            float weightW = Float.parseFloat(weight);
            float widthW = Float.parseFloat(width);
            float heightW = Float.parseFloat(height);
            float aggroW = Float.parseFloat(aggro);
            double xStartW = Double.parseDouble(xLocS);
            double yStartY = Double.parseDouble(yLocS);
            double zStartZ = Double.parseDouble(zLocS);
            

            this.cichlidA = new ConvictCichlid(0.0F, widthW, heightW, name, new Point3D(xStartW, yStartY, zStartZ));
            this.cichlidA.setGender(gender);
            this.cichlidA.setAggroLevel(aggroW);
            
            this.cichlidA.setBaseSpeed(3.0F);
            this.cichlidA.setBaseCautionLevel(4.0F);
            this.cichlidA.setDirection(new Vector3D(1.0D, 1.0D, 1.0D));
            this.cichlidA.setCullMode(SceneNode.CULL_MODE.ALWAYS);
            this.cichlidA.setState(FishState.IDLE);
            this.cichlidA.setInfluence(12.0D);
            

            Matrix3D cichlidAT = this.cichlidA.getLocalTranslation();
            


            cichlidAT.translate(xStartW, yStartY, zStartZ);
            this.cichlidA.setLocalTranslation(cichlidAT);
            Matrix3D cichlidAS = this.cichlidA.getLocalScale();
            




            cichlidAS.scale(widthW * weightW * 0.1D, heightW * 
              weightW * 0.1D, 0.0D);
            

            this.cichlidA.setLocalScale(cichlidAS);
            Matrix3D cichlidAR = new Matrix3D();
            

            cichlidAR.rotateX(30.0D);
            this.cichlidA.setLocalRotation(cichlidAR);
            addGameWorldObject(this.cichlidA);
            this.objs.add(this.cichlidA);
            this.cichlidA.updateWorldBound();
            


            this.aggroRangeA = new Sphere();
            Matrix3D aRangeT = this.aggroRangeA.getLocalTranslation();
            aRangeT.translate(xStartW, yStartY, zStartZ);
            this.aggroRangeA.setLocalTranslation(aRangeT);
            Matrix3D aScale = this.aggroRangeA.getLocalScale();
            aScale.scale(30.0D, 30.0D, 30.0D);
            this.aggroRangeA.setLocalScale(aScale);
            addGameWorldObject(this.aggroRangeA);
            this.aggroRangeA.updateWorldBound();
            this.aggroRangeA.setCullMode(SceneNode.CULL_MODE.ALWAYS);
            this.cichlidCount += 1;
            















































            OgreXMLParser loader = new OgreXMLParser();
            try
            {
              this.model = loader.loadModel("src/main/java/actv/ccs/sageTest/testingoutOgre/Plane.mesh.xml", "src/main/java/actv/ccs/sageTest/testingoutOgre/pooplid.material", "src/main/java/actv/ccs/sageTest/testingoutOgre/Plane.skeleton.xml");
              
              this.model.updateGeometricState(0.0D, true);
              this.modelIterator = this.model.iterator();
              this.cichlidAObject = ((Model3DTriMesh)this.modelIterator.next());
              System.out.println("test");
            }
            catch (Exception vv)
            {
              vv.printStackTrace();
            }
            Texture hobTexture = TextureManager.loadTexture2D("src/main/java/actv/ccs/sageTest/testingOutOgre/cichlidMesh.png");
            hobTexture.setApplyMode(Texture.ApplyMode.Replace);
            this.testState = ((TextureState)this.display.getRenderer().createRenderState(RenderState.RenderStateType.Texture));
            this.testState.setTexture(hobTexture, 0);
            this.testState.setEnabled(true);
            


            addGameWorldObject(this.cichlidAObject);
            this.cichlidAObject.translate((float)xStartW, (float)yStartY, (float)zStartZ);
            this.cichlidAObject.scale((float)(widthW * weightW * 0.05D), 
              (float)(heightW * weightW * 0.09D), 0.09F);
            this.cichlidAObject.setName("CichlidA");
          }
        }
        else if (id.equals("2"))
        {
          this.rsI = s.executeQuery("SELECT * FROM [FishPool] WHERE Type='Fish B'");
          while (this.rsI.next())
          {
            String name = this.rsI.getString("Type");
            

            String weight = this.rsI.getString("Weight");
            String width = this.rsI.getString("Width");
            String height = this.rsI.getString("Height");
            String gender = this.rsI.getString("Gender");
            String aggro = this.rsI.getString("AggroLevel");
            
            String xLocS = this.rsI.getString("StartingXPos");
            String yLocS = this.rsI.getString("StartingYPos");
            String zLocS = this.rsI.getString("StartingZPos");
            
            float weightW = Float.parseFloat(weight);
            float widthW = Float.parseFloat(width);
            float heightW = Float.parseFloat(height);
            float aggroW = Float.parseFloat(aggro);
            double xStartW = Double.parseDouble(xLocS);
            double yStartY = Double.parseDouble(yLocS);
            double zStartZ = Double.parseDouble(zLocS);
            
            this.cichlidB = new ConvictCichlid(0.0F, widthW, heightW, name, new Point3D(this.cichlidA.getLocation().getX() + 4.0D, this.cichlidA.getLocation().getY() + 2.0D, this.cichlidA.getLocation().getZ()));
            this.cichlidB.setName(name);
            this.cichlidB.setGender(gender);
            this.cichlidB.setAggroLevel(aggroW);
            
            this.cichlidB.setBaseSpeed(3.0F);
            this.cichlidB.setBaseCautionLevel(4.0F);
            this.cichlidB.setDirection(new Vector3D(1.0D, 1.0D, 1.0D));
            this.cichlidB.setCullMode(SceneNode.CULL_MODE.ALWAYS);
            this.cichlidB.setState(FishState.IDLE);
            this.cichlidB.setInfluence(8.0D);
            Matrix3D cichlidBT = this.cichlidB.getLocalTranslation();
            


            cichlidBT.translate(xStartW, yStartY, zStartZ);
            this.cichlidB.setLocalTranslation(cichlidBT);
            Matrix3D cichlidBS = this.cichlidB.getLocalScale();
            




            cichlidBS.scale(widthW * weightW * 0.1D, heightW * 
              weightW * 0.1D, 0.0D);
            

            this.cichlidB.setLocalScale(cichlidBS);
            Matrix3D cichlidBR = new Matrix3D();
            

            cichlidBR.rotateX(30.0D);
            this.cichlidB.setLocalRotation(cichlidBR);
            addGameWorldObject(this.cichlidB);
            this.objs.add(this.cichlidB);
            this.cichlidB.updateWorldBound();
            

            this.aggroRangeB = new Sphere();
            Matrix3D aRangeT = this.aggroRangeB.getLocalTranslation();
            aRangeT.translate(xStartW, yStartY, zStartZ);
            this.aggroRangeB.setLocalTranslation(aRangeT);
            Matrix3D aScale = this.aggroRangeB.getLocalScale();
            aScale.scale(30.0D, 30.0D, 30.0D);
            this.aggroRangeB.setLocalScale(aScale);
            addGameWorldObject(this.aggroRangeB);
            this.aggroRangeB.updateWorldBound();
            this.aggroRangeB.setCullMode(SceneNode.CULL_MODE.ALWAYS);
            









































            OgreXMLParser loader = new OgreXMLParser();
            try
            {
              this.model = loader.loadModel("src/main/java/actv/ccs/sageTest/testingOutOgre/Plane.mesh.xml", "src/main/java/actv/ccs/sageTest/testingOutOgre/pooplid.material", "src/main/java/actv/ccs/sageTest/testingoutOgre/Plane.skeleton.xml");
              
              this.model.updateGeometricState(0.0D, true);
              this.modelIterator = this.model.iterator();
              this.cichlidBObject = ((Model3DTriMesh)this.modelIterator.next());
              System.out.println("test");
            }
            catch (Exception vv)
            {
              vv.printStackTrace();
            }
            Texture hobTexture = TextureManager.loadTexture2D("src/main/java/actv/ccs/sageTest/testingOutOgre/cichlidMesh.png");
            hobTexture.setApplyMode(Texture.ApplyMode.Replace);
            this.testState = ((TextureState)this.display.getRenderer().createRenderState(RenderState.RenderStateType.Texture));
            this.testState.setTexture(hobTexture, 0);
            this.testState.setEnabled(true);
            


            addGameWorldObject(this.cichlidBObject);
            this.cichlidBObject.translate((float)xStartW, (float)yStartY, (float)zStartZ);
            this.cichlidBObject.scale((float)(widthW * weightW * 0.05D), 
              (float)(heightW * weightW * 0.05D), 0.09F);
            this.cichlidBObject.setName("CichlidB");
            this.cichlidCount += 1;
          }
        }
        else if (id.equals("3"))
        {
          this.rsI = s.executeQuery("SELECT * FROM [FishPool] WHERE Type='Fish C'");
          while (this.rsI.next())
          {
            String name = this.rsI.getString("Type");
            

            String weight = this.rsI.getString("Weight");
            String width = this.rsI.getString("Width");
            String height = this.rsI.getString("Height");
            String gender = this.rsI.getString("Gender");
            String aggro = this.rsI.getString("AggroLevel");
            
            String xLocS = this.rsI.getString("StartingXPos");
            String yLocS = this.rsI.getString("StartingYPos");
            String zLocS = this.rsI.getString("StartingZPos");
            
            float weightW = Float.parseFloat(weight);
            float widthW = Float.parseFloat(width);
            float heightW = Float.parseFloat(height);
            float aggroW = Float.parseFloat(aggro);
            double xStartW = Double.parseDouble(xLocS);
            double yStartY = Double.parseDouble(yLocS);
            double zStartZ = Double.parseDouble(zLocS);
            
            this.cichlidC = new ConvictCichlid(0.0F, widthW, heightW, name, new Point3D(xStartW, yStartY, zStartZ));
            this.cichlidC.setName(name);
            this.cichlidC.setGender(gender);
            this.cichlidC.setAggroLevel(aggroW);
            

            this.cichlidC.setBaseSpeed(3.0F);
            this.cichlidC.setBaseCautionLevel(4.0F);
            this.cichlidC.setDirection(new Vector3D(-0.5D, 0.8D, 0.1D));
            this.cichlidC.setState(FishState.IDLE);
            this.cichlidC.setCullMode(SceneNode.CULL_MODE.ALWAYS);
            this.cichlidC.setInfluence(6.0D);
            
            Matrix3D cichlidCT = this.cichlidC.getLocalTranslation();
            


            cichlidCT.translate(xStartW, yStartY, zStartZ);
            this.cichlidC.setLocalTranslation(cichlidCT);
            Matrix3D cichlidCS = this.cichlidC.getLocalScale();
            




            cichlidCS.scale(widthW * weightW * 0.1D, heightW * 
              weightW * 0.1D, 0.0D);
            

            this.cichlidC.setLocalScale(cichlidCS);
            Matrix3D cichlidCR = new Matrix3D();
            

            cichlidCR.rotateX(30.0D);
            this.cichlidC.setLocalRotation(cichlidCR);
            addGameWorldObject(this.cichlidC);
            this.objs.add(this.cichlidC);
            this.cichlidC.updateWorldBound();
            

            this.aggroRangeC = new Sphere();
            Matrix3D aRangeT = this.aggroRangeC.getLocalTranslation();
            aRangeT.translate(xStartW, yStartY, zStartZ);
            this.aggroRangeC.setLocalTranslation(aRangeT);
            Matrix3D aScale = this.aggroRangeC.getLocalScale();
            aScale.scale(30.0D, 30.0D, 30.0D);
            this.aggroRangeC.setLocalScale(aScale);
            addGameWorldObject(this.aggroRangeC);
            this.aggroRangeC.updateWorldBound();
            this.aggroRangeC.setCullMode(SceneNode.CULL_MODE.ALWAYS);
            































            OgreXMLParser loader = new OgreXMLParser();
            try
            {
              this.model = loader.loadModel("src/main/java/actv/ccs/sageTest/testingOutOgre/Plane.mesh.xml", "src/main/java/actv/ccs/sageTest/testingOutOgre/pooplid.material", "src/main/java/actv/ccs/sageTest/testingoutOgre/Plane.skeleton.xml");
              
              this.model.updateGeometricState(0.0D, true);
              this.modelIterator = this.model.iterator();
              this.cichlidCObject = ((Model3DTriMesh)this.modelIterator.next());
              System.out.println("test");
            }
            catch (Exception vv)
            {
              vv.printStackTrace();
            }
            Texture hobTexture = TextureManager.loadTexture2D("src/main/java/actv/ccs/sageTest/testingOutOgre/cichlidMesh.png");hobTexture.setApplyMode(Texture.ApplyMode.Replace);
            this.testState = ((TextureState)this.display.getRenderer().createRenderState(RenderState.RenderStateType.Texture));
            this.testState.setTexture(hobTexture, 0);
            this.testState.setEnabled(true);
            


            addGameWorldObject(this.cichlidCObject);
            this.cichlidCObject.translate((float)xStartW, (float)yStartY, (float)zStartZ);
            this.cichlidCObject.scale((float)(widthW * weightW * 0.05D), 
              (float)(heightW * weightW * 0.05D), 0.09F);
            this.cichlidCObject.setName("CichlidC");
            
            this.cichlidCount += 1;
          }
        }
      }
      this.conn.close();
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
  }
  
  public void initActions()
  {
    this.im = getInputManager();
    String kbName = this.im.getKeyboardName();
    
    String mName = this.im.getMouseName();
    

    this.cc = new CameraOrbit(this.camera, this.cameraGuy, this.im, mName);
    
    System.out.println("controller: " + mName);
    














    IAction quitGame = new QuitAction(this);
    IAction pauseKey = new pauseAction();
    IAction resumeKey = new resumeAction();
    this.im.associateAction(kbName, 
      Component.Identifier.Key.ESCAPE, quitGame, 
      IInputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
    this.im.associateAction(kbName, Component.Identifier.Key.P, pauseKey, 
      IInputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
    this.im.associateAction(kbName, Component.Identifier.Key.R, resumeKey, 
      IInputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
    


    IAction saveState = new saveAction();
    this.im.associateAction(kbName, Component.Identifier.Key.Q, saveState, 
      IInputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
  }
  
  private class pauseAction
    extends AbstractInputAction
  {
    private pauseAction() {}
    
    public void performAction(float time, Event ev)
    {
      System.out.println("PAUSE PRESSED");
      MyGame.this.pauseGame();
    }
  }
  
  private class resumeAction
    extends AbstractInputAction
  {
    private resumeAction() {}
    
    public void performAction(float time, Event evento)
    {
      System.out.println("PAUSE IS OFF");
      MyGame.this.resumeGame();
    }
  }
  
  private class saveAction
    extends AbstractInputAction
  {
    private saveAction() {}
    
    public void performAction(float time, Event sp)
    {
      System.out.println("saveAction");
      try
      {
        try
        {
          Connection conn = DriverManager.getConnection("jdbc:ucanaccess://FishPool.accdb");
          
          Statement s = conn.createStatement();
          MyGame.this.rs = s.executeQuery("SELECT ID FROM [ScenarioFlag]");
          while (MyGame.this.rs.next()) {
            int i = s.executeUpdate("UPDATE ScenarioFlag set ScenarioNumber = 6 where ID = 6");
          }
          conn.close();
        }
        catch (SQLException Ex)
        {
          Ex.printStackTrace();
        }
        try
        {
          Point3D loc;
          int m;
          try
          {
            Connection connn = DriverManager.getConnection("jdbc:ucanaccess://FishPool.accdb");
            Statement s = connn.createStatement();
            MyGame.this.rs = s.executeQuery("SELECT ID FROM [SimulationFishS]");
            while (MyGame.this.rs.next())
            {
              Point3D ca;
              int n;
              if (MyGame.this.cichlidA != null)
              {
                loc = new Point3D(MyGame.this.cichlidA.getWorldTranslation().getCol(3));
                System.out.println("save flag for cichlidA");
                int a = s.executeUpdate("UPDATE SimulationFishS set fishID = 1 where ID = 1");
                int aa = s.executeUpdate("UPDATE FishPoolSaveState set StartingXPos = " + loc.getX() + " where ID = 1");
                int aaa = s.executeUpdate("UPDATE FishPoolSaveState set StartingYPos = " + loc.getY() + " where ID = 1");
                n = s.executeUpdate("UPDATE FishPoolSaveState set StartingZPos = " + loc.getZ() + " where ID = 1");
              }
              else if (MyGame.this.cichlidA == null)
              {
                int j = s.executeUpdate("UPDATE SimulationFishS set fishID = 0 where ID = 1");
              }
              if (MyGame.this.cichlidB != null)
              {
                loc = new Point3D(MyGame.this.cichlidB.getWorldTranslation().getCol(3));
                System.out.println("save flag for cichlidB");
                int a = s.executeUpdate("UPDATE SimulationFishS set fishID = 2 where ID = 2");
                int aa = s.executeUpdate("UPDATE FishPoolSaveState set StartingXPos = " + loc.getX() + " where ID = 2");
                int aaa = s.executeUpdate("UPDATE FishPoolSaveState set StartingYPos = " + loc.getY() + " where ID = 2");
                n = s.executeUpdate("UPDATE FishPoolSaveState set StartingZPos = " + loc.getZ() + " where ID = 2");
              }
              else if (MyGame.this.cichlidB == null)
              {
                int k = s.executeUpdate("UPDATE SimulationFishS set fishID = 0 where ID = 2");
              }
              if (MyGame.this.cichlidC != null)
              {
                loc = new Point3D(MyGame.this.cichlidC.getWorldTranslation().getCol(3));
                System.out.println("save flag for cichlidC");
                int a = s.executeUpdate("UPDATE SimulationFishS set fishID = 3 where ID = 3");
                int aa = s.executeUpdate("UPDATE FishPoolSaveState set StartingXPos = " + loc.getX() + " where ID = 3");
                int aaa = s.executeUpdate("UPDATE FishPoolSaveState set StartingYPos = " + loc.getY() + " where ID = 3");
                n = s.executeUpdate("UPDATE FishPoolSaveState set StartingZPos = " + loc.getZ() + " where ID = 3");
              }
              else if (MyGame.this.cichlidC == null)
              {
                m = s.executeUpdate("UPDATE SimulationFishS set fishID = 0 where ID = 3");
              }
            }
            connn.close();
          }
          catch (Exception p1)
          {
            p1.printStackTrace();
          }
          try
          {
            try
            {
              Connection conne = DriverManager.getConnection("jdbc:ucanaccess://FishPool.accdb");
              Statement s = conne.createStatement();
              MyGame.this.rs = s.executeQuery("SELECT ID FROM [SimulationObjects]");
              while (MyGame.this.rs.next())
              {
                if (MyGame.this.largePlant != null)
                {
                  System.out.println("saving large plant");
                  m = s.executeUpdate("UPDATE SimulationObjectsS set objID = 1 where ID = 1");
                }
                else if (MyGame.this.largePlant == null)
                {
                  m = s.executeUpdate("UPDATE SimulationObjectsS set objID = 0 where ID = 1");
                }
                if (MyGame.this.largePot != null)
                {
                  System.out.println("saving large pot");
                  m = s.executeUpdate("UPDATE SimulationObjectsS set objID = 4 where ID = 4");
                }
                else if (MyGame.this.largePot == null)
                {
                  m = s.executeUpdate("UPDATE SimulationObjectsS set objID = 0 where ID = 4");
                }
                if (MyGame.this.mediumPlant != null)
                {
                  System.out.println("saving medium plant");
                  m = s.executeUpdate("UPDATE SimulationObjectsS set objID = 2 where ID = 2");
                }
                else if (MyGame.this.mediumPlant == null)
                {
                  m = s.executeUpdate("UPDATE SimulationObjectsS set objID = 0 where ID = 2");
                }
                if (MyGame.this.mediumPot != null)
                {
                  System.out.println("saving medium pot");
                  m = s.executeUpdate("UPDATE SimulationObjectsS set objID = 5 where ID = 5");
                }
                else if (MyGame.this.mediumPot == null)
                {
                  m = s.executeUpdate("UPDATE SimulationObjectsS set objID = 0 where ID = 5");
                }
                if (MyGame.this.smallPlant != null)
                {
                  System.out.println("saving small plant");
                  m = s.executeUpdate("UPDATE SimulationObjectsS set objID = 3 where ID = 3");
                }
                else if (MyGame.this.smallPlant == null)
                {
                  m = s.executeUpdate("UPDATE SimulationObjectsS set objID = 0 where ID = 3");
                }
                if (MyGame.this.smallPot != null)
                {
                  System.out.println("saving small pot");
                  m = s.executeUpdate("UPDATE SimulationObjectsS set objID = 6 where ID = 6");
                }
                else if (MyGame.this.smallPot == null)
                {
                  m = s.executeUpdate("UPDATE SimulationObjectsS set objID = 0 where ID = 6");
                }
              }
              conne.close();
            }
            catch (Exception p1)
            {
              p1.printStackTrace();
            }
            return;
          }
          catch (Exception pp)
          {
            pp.printStackTrace();
          }
        }
        catch (Exception pp)
        {
          pp.printStackTrace();
        }
      }
      catch (SecurityException e5)
      {
        e5.printStackTrace();
      }
    }
  }
  
  public void createFishTankWalls()
  {
    addGameWorldObject(this.fishTank.createFishTankWalls());
  }
  
  public void createFishTank()
  {
    addGameWorldObject(this.fishTank.createTankTerrain());
  }
  
  public void update(float elapsedTimeMS)
  {
    try
    {
      this.conn = DriverManager.getConnection("jdbc:ucanaccess://FishPool.accdb");
      
      Statement sea = this.conn.createStatement();
      this.rs = sea.executeQuery("SELECT scenarioNumber FROM [ScenarioFlag]");
      while (this.rs.next())
      {
        String scenNum = this.rs.getString("ScenarioNumber");
        

        int scenGrab = Integer.parseInt(scenNum);
        if ((scenGrab == 1) || (scenGrab == 2) || (scenGrab == 3) || (scenGrab == 4) || (scenGrab == 5) || (scenGrab == 6))
        {
          if (this.startAnimation)
          {
            startAnimationProcess();
            this.startAnimation = false;
          }
          if (!this.pauseSimulation)
          {
            Point3D camLoc = this.camera.getLocation();
            Matrix3D camT = new Matrix3D();
            camT.translate(camLoc.getX(), camLoc.getY(), camLoc.getZ());
            for (SceneNode s : getGameWorld()) {
              if ((s instanceof Model3DTriMesh))
              {
                if (this.cichlidAObject != null) {
                  if (s == this.cichlidAObject) {
                    ((Model3DTriMesh)s).updateAnimation(elapsedTimeMS);
                  }
                }
                if (this.cichlidBObject != null) {
                  if (s == this.cichlidBObject) {
                    ((Model3DTriMesh)s).updateAnimation(elapsedTimeMS);
                  }
                }
                if (this.cichlidCObject != null) {
                  if (s == this.cichlidCObject)
                  {
                    System.out.println("the world gone bad");
                    ((Model3DTriMesh)s).updateAnimation(elapsedTimeMS);
                  }
                }
                s.updateGeometricState(elapsedTimeMS, true);
              }
            }
            for (SceneNode s : getGameWorld()) {
              if ((s instanceof ConvictCichlid))
              {
                if (s == this.cichlidA)
                {
                  Point3D loc = new Point3D(s.getWorldTranslation().getCol(3));
                  


                  Matrix3D cichlidAlocalT = s.getLocalTranslation();
                  Matrix3D cichlidARot = s.getLocalRotation();
                  this.aggroRangeA.setLocalTranslation(cichlidAlocalT);
                  this.cichlidAObject.setLocalTranslation(cichlidAlocalT);
                  this.cichlidAObject.setLocalRotation(cichlidARot);
                  if ((loc.getX() > 200.0D) || (loc.getX() < 0.0D)) {
                    System.out.println("X BOUNDS");
                  }
                  if ((loc.getY() > 200.0D) || (loc.getY() < 0.0D)) {
                    System.out.println("Y BOUNDS");
                  }
                  if ((loc.getZ() > 200.0D) || (loc.getZ() < 0.0D)) {
                    System.out.println("Z BOUNDS");
                  }
                  if (this.largePotC) {
                    if (this.cichlidA.getWorldBound().intersects(this.largePot.getWorldBound())) {
                      System.out.println("a hit largePot");
                    }
                  }
                  if (this.largePlantC) {
                    if (this.cichlidA.getWorldBound().intersects(this.largePlant.getWorldBound())) {
                      System.out.println("a hit largePl");
                    }
                  }
                  if (this.mediumPotC) {
                    if (this.cichlidA.getWorldBound().intersects(this.mediumPot.getWorldBound())) {
                      System.out.println("a hit med pot");
                    }
                  }
                  if (this.mediumPlantC) {
                    if (this.cichlidA.getWorldBound().intersects(this.mediumPlant.getWorldBound())) {
                      System.out.println("a hit med pl");
                    }
                  }
                  if (this.smallPlantC) {
                    if (this.cichlidA.getWorldBound().intersects(this.smallPlant.getWorldBound())) {
                      System.out.println("a hit small pla");
                    }
                  }
                  if (this.smallPotC) {
                    if (this.cichlidA.getWorldBound().intersects(this.smallPot.getWorldBound())) {
                      System.out.println("a hit small pot");
                    }
                  }
                  if (this.cichlidB != null)
                  {
                    if (this.cichlidA.getWorldBound().intersects(this.cichlidB.getWorldBound())) {
                      System.out.println("a hits b");
                    }
                    if (this.aggroRangeA.getWorldBound().intersects(this.aggroRangeB.getWorldBound())) {
                      System.out.println("aggro from a to B");
                    }
                  }
                  if (this.cichlidC != null)
                  {
                    if (this.cichlidA.getWorldBound().intersects(this.cichlidC.getWorldBound())) {
                      System.out.println("a hits c");
                    }
                    if (this.aggroRangeA.getWorldBound().intersects(this.aggroRangeC.getWorldBound())) {
                      System.out.println("aggro from a to C");
                    }
                  }
                }
                if (s == this.cichlidB)
                {
                  Point3D loc = new Point3D(s.getWorldTranslation().getCol(3));
                  

                  Matrix3D cichlidBlocalT = s.getLocalTranslation();
                  Matrix3D cichlidBRot = s.getLocalRotation();
                  this.cichlidBObject.setLocalTranslation(cichlidBlocalT);
                  this.cichlidBObject.setLocalRotation(cichlidBRot);
                  this.aggroRangeB.setLocalTranslation(cichlidBlocalT);
                  if ((loc.getX() > 200.0D) || (loc.getX() < 0.0D)) {
                    System.out.println("X BOUNDS");
                  }
                  if ((loc.getY() > 200.0D) || (loc.getY() < 0.0D)) {
                    System.out.println("Y BOUNDS");
                  }
                  if ((loc.getZ() > 200.0D) || (loc.getZ() < 0.0D)) {
                    System.out.println("Z BOUNDS");
                  }
                  if (this.largePotC) {
                    if (this.cichlidB.getWorldBound().intersects(this.largePot.getWorldBound())) {
                      System.out.println("b hit largePo");
                    }
                  }
                  if (this.largePlantC) {
                    if (this.cichlidB.getWorldBound().intersects(this.largePlant.getWorldBound())) {
                      System.out.println("b hit largePl");
                    }
                  }
                  if (this.mediumPotC) {
                    if (this.cichlidB.getWorldBound().intersects(this.mediumPot.getWorldBound())) {
                      System.out.println("b hit medP");
                    }
                  }
                  if (this.mediumPlantC) {
                    if (this.cichlidB.getWorldBound().intersects(this.mediumPlant.getWorldBound())) {
                      System.out.println("b hit medPL");
                    }
                  }
                  if (this.smallPlantC) {
                    if (this.cichlidB.getWorldBound().intersects(this.smallPlant.getWorldBound())) {
                      System.out.println("b hit smallPl");
                    }
                  }
                  if (this.smallPotC) {
                    if (this.cichlidB.getWorldBound().intersects(this.smallPot.getWorldBound())) {
                      System.out.println("b hit smallPot");
                    }
                  }
                  if (this.cichlidA != null)
                  {
                    if (this.cichlidB.getWorldBound().intersects(this.cichlidA.getWorldBound())) {
                      System.out.println("b hits a");
                    }
                    if (this.aggroRangeB.getWorldBound().intersects(this.aggroRangeA.getWorldBound())) {
                      System.out.println("aggro from B to A");
                    }
                  }
                  if (this.cichlidC != null)
                  {
                    if (this.cichlidB.getWorldBound().intersects(this.cichlidC.getWorldBound())) {
                      System.out.println("b hits c");
                    }
                    if (this.aggroRangeB.getWorldBound().intersects(this.aggroRangeC.getWorldBound())) {
                      System.out.println("aggro from B to C");
                    }
                  }
                }
                if (s == this.cichlidC)
                {
                  Point3D loc = new Point3D(s.getWorldTranslation().getCol(3));
                  Matrix3D cichlidClocalT = s.getLocalTranslation();
                  Matrix3D cichlidCRot = s.getLocalRotation();
                  this.aggroRangeC.setLocalTranslation(cichlidClocalT);
                  this.cichlidCObject.setLocalTranslation(cichlidClocalT);
                  this.cichlidCObject.setLocalRotation(cichlidCRot);
                  if ((loc.getX() > 200.0D) || (loc.getX() < 0.0D)) {
                    System.out.println("X BOUNDS");
                  }
                  if ((loc.getY() > 200.0D) || (loc.getY() < 0.0D)) {
                    System.out.println("Y BOUNDS");
                  }
                  if ((loc.getZ() > 200.0D) || (loc.getZ() < 0.0D)) {
                    System.out.println("Z BOUNDS");
                  }
                  if (this.largePotC) {
                    if (this.cichlidC.getWorldBound().intersects(this.largePot.getWorldBound())) {
                      System.out.println("c hit large pot");
                    }
                  }
                  if (this.largePlantC) {
                    if (this.cichlidC.getWorldBound().intersects(this.largePlant.getWorldBound())) {
                      System.out.println("c hit large plant");
                    }
                  }
                  if (this.mediumPotC) {
                    if (this.cichlidC.getWorldBound().intersects(this.mediumPot.getWorldBound())) {
                      System.out.println("c hit medium pot");
                    }
                  }
                  if (this.mediumPlantC) {
                    if (this.cichlidC.getWorldBound().intersects(this.mediumPlant.getWorldBound())) {
                      System.out.println("c hit medium plant");
                    }
                  }
                  if (this.smallPlantC) {
                    if (this.cichlidC.getWorldBound().intersects(this.smallPlant.getWorldBound())) {
                      System.out.println("c hit small plant");
                    }
                  }
                  if (this.smallPotC) {
                    if (this.cichlidC.getWorldBound().intersects(this.smallPot.getWorldBound())) {
                      System.out.println("c hit small pot");
                    }
                  }
                  if (this.cichlidA != null)
                  {
                    if (this.cichlidC.getWorldBound().intersects(this.cichlidA.getWorldBound())) {
                      System.out.println("c hits a");
                    }
                    if (this.aggroRangeC.getWorldBound().intersects(this.aggroRangeA.getWorldBound())) {
                      System.out.println("aggro from C to A");
                    }
                  }
                  if (this.cichlidB != null)
                  {
                    if (this.cichlidC.getWorldBound().intersects(this.cichlidA.getWorldBound())) {
                      System.out.println("c hits b");
                    }
                    if (this.aggroRangeC.getWorldBound().intersects(this.aggroRangeB.getWorldBound())) {
                      System.out.println("aggro from C to B");
                    }
                  }
                }
              }
            }
            super.update(this.time);
            this.cc.update(this.time);
          }
          else
          {
            HUDString pauseString = new HUDString("Game is Paused");
            addGameWorldObject(pauseString);
            pauseString.setLocation(10.0D, 10.0D);
            


            super.update(0.0F);
          }
        }
        else
        {
          super.update(elapsedTimeMS);
        }
      }
    }
    catch (Exception epp)
    {
      epp.printStackTrace();
    }
  }
  
  private IDisplaySystem createDisplaySystem()
  {
    IDisplaySystem display = new MyDisplaySystem(1000, 500, 24, 20, false, 
      "sage.renderer.jogl.JOGLRenderer");
    System.out.print("\nWaiting for display creation...");
    int count = 0;
    

















    return display;
  }
  
  protected void initSystem()
  {
    IDisplaySystem display = createDisplaySystem();
    setDisplaySystem(display);
    
    IInputManager inputManager = new InputManager();
    setInputManager(inputManager);
    
    ArrayList<SceneNode> gameWorld = new ArrayList();
    setGameWorld(gameWorld);
  }
  
  protected void shutdown()
  {
    this.display.close();
    try
    {
      Connection conn = 
        DriverManager.getConnection("jdbc:ucanaccess://FishPool.accdb");
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
      
      stopRunner();
    }
    catch (SQLException e1)
    {
      e1.printStackTrace();
    }
  }
}
