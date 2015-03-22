package actv.ccs.model.ui;

import java.sql.*;
import java.util.Vector;

import actv.ccs.listener.CCChangeListener;
import actv.ccs.model.ConvictCichlid;
import actv.ccs.model.TankObject;
import actv.ccs.model.type.FishState;

public class SimulationWorld implements IObservable, ISimulationWorld {
	
	private ConvictCichlid cichlid, cichlidA, cichlidB, cichlidC; // test
	private TankObject tank;
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
		
		ConvictCichlidController controller = new ConvictCichlidController(cichlid, this);
		TankController tankController = new TankController(tank, this);
		
		
		spawnCichlids();
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

			/*
			 * so the issue is that we will need to remove the data from simulationworld whenever there's a new simulation etc. will delve on this more.
			 */
			
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
		        	// need to add into database later caution, speed
		     
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
		        	
		     //   	System.out.println( name + weight + width + height + gender + aggro);
		        	cichlidB = new ConvictCichlid();
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
		        	
		      //  	System.out.println(name + weight + width + height + gender + aggro);
		        	cichlidC = new ConvictCichlid();
				}
			}
		}
		conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		TankObject t = new TankObject(20, 20, 20, 26, 0, 0, new int [3]);
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