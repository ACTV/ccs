package actv.ccs.model.ui;

import java.awt.Color;
import java.sql.*;
import java.util.Vector;

import actv.ccs.listener.CCChangeListener;
import actv.ccs.listener.RuleEngineRunner;
import actv.ccs.model.ConvictCichlid;
import actv.ccs.model.TankObject;
import actv.ccs.model.type.FishState;

public class SimulationWorld implements IObservable, ISimulationWorld {
	
	private ConvictCichlid cichlid, cichlidA, cichlidB, cichlidC; // test
	private TankObject tank;
	private int timerT;
	private CichlidCollection cList;
	private Vector<IObserver> observerList;
	private int fishPoolArr [];
	private ResultSet rs, rsI;
	private Connection conn;
	private RuleEngineRunner runner;

	public SimulationWorld()
	{		
		cList = new CichlidCollection();
		observerList = new Vector<IObserver>();
		fishPoolArr = new int [3];
		fishPoolArr[0] = fishPoolArr[1] = fishPoolArr[2] = 0;
		getTimer();
		
			
		float [] verts = new float [] {0,1,0,-1,-1,1,1,-1,1,1,-1,-1,-1,-1,-1};
		
		
		spawnCichlids();
	}
	
	public CichlidCollection getCichlidCollection(){
		return cList;
	}
	
	/* 
	 * Starting the rule engine:
	 * 	Initialize the RuleEngineRunner singleton,
	 * 		add objects to it,
	 * 		.start() the runner
	 */
	public void startRunner(){
		if(runner == null){
			runner = RuleEngineRunner.getInstance();
			//TODO: this is commented out to avoid errors in SageTest
			//runner.newMap(cList);
			runner.start();
		}
	}
	
	public void stopRunner(){
		try {
			runner.closeSession();
			runner.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void spawnTank()
	{
		
		
		try {
			conn = DriverManager.getConnection("jdbc:ucanaccess://FishPool.accdb");
	
		Statement s = conn.createStatement();
		rs = s.executeQuery("SELECT * FROM [TankData]");
		while (rs.next())
		{
			String length = rs.getString("Length");
			String width = rs.getString("Width");
			String height = rs.getString("Height");
			String temperature = rs.getString("Temperature");
			String cCount = rs.getString("cichlidCount");
			String pCount = rs.getString("plantCount");
			String tTime = rs.getString("Time");
			
			float lw = Float.parseFloat(length);
			float ww = Float.parseFloat(width);
			float hw = Float.parseFloat(height);
			float tw = Float.parseFloat(temperature);
			int cW = Integer.parseInt(cCount);
			int pC = Integer.parseInt(pCount);
			int tP = Integer.parseInt(tTime);
			
			tank = new TankObject(lw, ww, hw, tw, cW, pC, tP);
		//	System.out.println("tank : gerg " + lw );
			System.out.println("tank : gergff " + tank.getTimer() );
			
		}
		conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
		        	
		        	float weightW = Float.parseFloat(weight);
		        	float widthW = Float.parseFloat(width);
		        	float heightW = Float.parseFloat(height);
		        	float aggroW = Float.parseFloat(aggro);
		        	double xStartW = Double.parseDouble(xLocS);
		        	double yStartY = Double.parseDouble(yLocS);
		        	
		        	
		     //   	System.out.println( name + weight + width + height + gender + aggro);

		        	cichlidA = new ConvictCichlid();
//		        	cichlidA.setStartX(xStartW);
//		        	cichlidA.setStartY(yStartY);
		        	cichlidA.setName(name);
		        	cichlidA.setWeight(weightW);
		        	cichlidA.setLength(widthW);
		        	cichlidA.setHeight(heightW);
		        	cichlidA.setGender(gender);
		        	cichlidA.setAggroLevel(aggroW);
		        	cichlidA.setBaseAggroLevel(5);
		        	cichlidA.setBaseSpeed(5);
		        	cichlidA.setBaseCautionLevel(0);
//		        	cichlidA.setX(1000); // starting variables
//		        	cichlidA.setY(500);
//		        	cichlidA.setDirection(0);
//		        	cichlidA.setColor(Color.GREEN);
		        	cList.add(cichlidA);
		        	
		        	System.out.println("name: " + cichlidA.getName() + "direction start"  + cichlidA.getDirection());
		        	
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
		        	
		        	float weightW = Float.parseFloat(weight);
		        	float widthW = Float.parseFloat(width);
		        	float heightW = Float.parseFloat(height);
		        	float aggroW = Float.parseFloat(aggro);
		        	double xStartW = Double.parseDouble(xLocS);
		        	double yStartY = Double.parseDouble(yLocS);
		        	
		        	
		     //   	System.out.println( name + weight + width + height + gender + aggro);

		        	cichlidB = new ConvictCichlid();
//		        	cichlidB.setStartX(xStartW);
//		        	cichlidB.setStartY(yStartY);
		        	cichlidB.setName(name);
		        	cichlidB.setWeight(weightW);
		        	cichlidB.setLength(widthW);
		        	cichlidB.setHeight(heightW);
		        	cichlidB.setGender(gender);
		        	cichlidB.setAggroLevel(aggroW);
		        	cichlidB.setBaseAggroLevel(5);
		        	cichlidB.setBaseSpeed(5);
		        	cichlidB.setBaseCautionLevel(0);
//		        	cichlidB.setDirection(100);
//		        	cichlidB.setColor(Color.blue);
		        	cList.add(cichlidB);

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
		        	
		        	float weightW = Float.parseFloat(weight);
		        	float widthW = Float.parseFloat(width);
		        	float heightW = Float.parseFloat(height);
		        	float aggroW = Float.parseFloat(aggro);
		        	double xStartW = Double.parseDouble(xLocS);
		        	double yStartY = Double.parseDouble(yLocS);
		        	
		        	
		     //   	System.out.println( name + weight + width + height + gender + aggro);

		        	cichlidC = new ConvictCichlid();
//		        	cichlidC.setStartX(xStartW);
//		        	cichlidC.setStartY(yStartY);
		        	cichlidC.setName(name);
		        	cichlidC.setWeight(weightW);
		        	cichlidC.setLength(widthW);
		        	cichlidC.setHeight(heightW);
		        	cichlidC.setGender(gender);
		        	cichlidC.setAggroLevel(aggroW);
		        	cichlidC.setBaseAggroLevel(5);
		        	cichlidC.setBaseSpeed(5);
		        	cichlidC.setBaseCautionLevel(300);
//		        	cichlidC.setX(100); // here is the starting position
//		        	cichlidC.setY(100);
//		        	cichlidC.setDirection(10);
//		        	cichlidC.setColor(Color.red);
		        	cList.add(cichlidC);

//		        	System.out.println("name: " + cichlidC.getName() + "color"  + cichlidC.getColor());

				}
			}
		}
		conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		spawnTank();
	}

	public void add(ConvictCichlid c)
	{
		cList.add(c);
	}
	public Iterator getIterator()
	{
		return cList.getIterator();
	}
	public void removeObj(ConvictCichlid o)
	{
		cList.removeObject(o);
	}
	public void addObserver(IObserver o)
	{
		observerList.add(o);
	}
	public void notifyObservers()
	{
		SimulationWorldProxy proxy = new SimulationWorldProxy(this);
		for (IObserver o: observerList)
		{
			o.update((IObservable) proxy, null);
		}
	}
	public void printData(FishState state, float[] location, float aggroLevel, float length, float height, float weight, String name, int id){
		System.out.println("FishState is " + state);
		System.out.println("Fish location is " + location);
		System.out.println("Fish aggro is " + aggroLevel);
		System.out.println("Fish length is " + length);
		System.out.println("Fish height is " + height);
		System.out.println("Fish weight is " + weight);
		System.out.println("Fish name is " + name);
		System.out.println("Fish ID is " + id);
	}

	private static TankObject baseTank()
	{
		TankObject t = new TankObject(20, 20, 20, 26, 0, 0, 0);
		return t;
	}
	public int [] getFishArr()
	{
		if (fishPoolArr == null)
		{
			fishPoolArr = new int[3];
			fishPoolArr[0] = fishPoolArr[1] = fishPoolArr[2] = 0;
		}
		return fishPoolArr;
	}
	public void setFishArr(int [] a)
	{
		fishPoolArr = a;
	}
	public int getTimer()
	{
		timerT = 0;
		Connection conn;
		try {
			conn = DriverManager.getConnection("jdbc:ucanaccess://FishPool.accdb");
			Statement s = conn.createStatement();
			rs = s.executeQuery("SELECT * FROM [TankData] WHERE ID='1' ");
			while (rs.next())
			{
			String id = rs.getString("Time"); // added new string for ID
			timerT = Integer.parseInt(id);
		}
        	conn.close();
        	
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		return timerT;
	}
	public void setTimer()
	{
		
	}

}
