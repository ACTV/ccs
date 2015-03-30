package actv.ccs.model.ui;

import java.awt.Color;
import java.sql.*;
import java.util.Vector;

import actv.ccs.listener.CCChangeListener;
import actv.ccs.model.ConvictCichlid;
import actv.ccs.model.TankObject;
import actv.ccs.model.type.FishState;
import actv.ccs.model.graphics.objects.*;

public class SimulationWorld implements IObservable, ISimulationWorld {
	
	private ConvictCichlid cichlid, cichlidA, cichlidB, cichlidC; // test
	private TankObject tank;
	private GeoShape testObj;
	private CichlidCollection cList;
	private Vector<IObserver> observerList;
	private int fishPoolArr [];
	private ResultSet rs, rsI;
	private Connection conn;
	public SimulationWorld()
	{		
		cList = new CichlidCollection();
		observerList = new Vector<IObserver>();
		fishPoolArr = new int [3];
		fishPoolArr[0] = fishPoolArr[1] = fishPoolArr[2] = 0;
		
		cichlid = getFromDB();
		cichlid.addPropertyChangeListener(new CCChangeListener());
			
		float [] verts = new float [] {0,1,0,-1,-1,1,1,-1,1,1,-1,-1,-1,-1,-1};
		
		
		// here's to trying to draw something on the screen.
		testObj = new GeoShape(null, verts, null, null, 10, 10, 10);
		//     public GeoShape(String string, float[] verts, float[] texCoords, String texFilePath, float x, float y, float z)
		
		System.out.println("testobj"  + testObj);
		
		spawnCichlids();
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
			
			float lw = Float.parseFloat(length);
			float ww = Float.parseFloat(width);
			float hw = Float.parseFloat(height);
			float tw = Float.parseFloat(temperature);
			int cW = Integer.parseInt(cCount);
			int pC = Integer.parseInt(pCount);
			
			tank = new TankObject(lw, ww, hw, tw, cW, pC);
		//	System.out.println("tank : gerg " + lw );
		//	System.out.println("tank : gergff " + cCount );
			
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
		        	
		        	float weightW = Float.parseFloat(weight);
		        	float widthW = Float.parseFloat(width);
		        	float heightW = Float.parseFloat(height);
		        	float aggroW = Float.parseFloat(aggro);
		        	
		     //   	System.out.println( name + weight + width + height + gender + aggro);
		        	cichlidA = new ConvictCichlid();
		        	
		        	// set variables

		        	cichlidA.setName(name);
		        	cichlidA.setWeight(weightW);
		        	cichlidA.setLength(widthW);
		        	cichlidA.setHeight(heightW);
		        	cichlidA.setGender(gender);
		        	cichlidA.setAggroLevel(aggroW);
		        	cichlidA.setBaseAggroLevel(5);
		        	cichlidA.setBaseSpeed(5);
		        	cichlidA.setBaseCautionLevel(0);
		        	cichlidA.setX(300); // starting variables
		        	cichlidA.setY(300);
		        	cichlidA.setDirection(0);
		        	cichlidA.setColor(Color.GREEN);
		        	cList.add(cichlidA);
		        	
		        	System.out.println("name: " + cichlidA.getName() + "color"  + cichlidA.getColor());
		        	
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
		        	
		        	float weightW = Float.parseFloat(weight);
		        	float widthW = Float.parseFloat(width);
		        	float heightW = Float.parseFloat(height);
		        	float aggroW = Float.parseFloat(aggro);
		        	
		     //   	System.out.println( name + weight + width + height + gender + aggro);

		        	cichlidB = new ConvictCichlid();
		        	cichlidB.setName(name);
		        	cichlidB.setWeight(weightW);
		        	cichlidB.setLength(widthW);
		        	cichlidB.setHeight(heightW);
		        	cichlidB.setGender(gender);
		        	cichlidB.setAggroLevel(aggroW);
		        	cichlidB.setBaseAggroLevel(5);
		        	cichlidB.setBaseSpeed(5);
		        	cichlidB.setBaseCautionLevel(0);
		        	cichlidB.setX(500); // starting variables
		        	cichlidB.setY(300);
		        	cichlidB.setDirection(100);
		        	cichlidB.setColor(Color.blue);
		        	cList.add(cichlidB);
		        	
		        	System.out.println("name: " + cichlidB.getName() + "color"  + cichlidB.getColor());
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
		        	
		        	cichlidC = new ConvictCichlid();
		        	
		        	float weightW = Float.parseFloat(weight);
		        	float widthW = Float.parseFloat(width);
		        	float heightW = Float.parseFloat(height);
		        	float aggroW = Float.parseFloat(aggro);
		        	
		        	cichlidC.setName(name);
		        	cichlidC.setWeight(weightW);
		        	cichlidC.setLength(widthW);
		        	cichlidC.setHeight(heightW);
		        	cichlidC.setGender(gender);
		        	cichlidC.setAggroLevel(aggroW);
		        	cichlidC.setBaseAggroLevel(5);
		        	cichlidC.setBaseSpeed(5);
		        	cichlidC.setBaseCautionLevel(300);
		        	cichlidC.setX(100); // here is the starting position
		        	cichlidC.setY(100);
		        	cichlidC.setDirection(10);
		        	cichlidC.setColor(Color.red);
		        	cList.add(cichlidC);

		        	System.out.println("name: " + cichlidC.getName() + "color"  + cichlidC.getColor());

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
	private static ConvictCichlid getFromDB()
	{
		ConvictCichlid c = new ConvictCichlid();
		c.setLocation(new float[] {1,1});
		c.setState(FishState.NONE);
		c.setLength(10);
		c.setHeight(5);
		c.setWeight(10);
		c.setName("Shark");
		c.setCichlidID(0);
		return c;
	}
	private static TankObject baseTank()
	{
		TankObject t = new TankObject(20, 20, 20, 26, 0, 0);
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

}