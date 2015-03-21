package actv.ccs.model.ui;

import java.sql.*;
import java.util.Vector;

import actv.ccs.listener.CCChangeListener;
import actv.ccs.model.ConvictCichlid;
import actv.ccs.model.TankObject;
import actv.ccs.model.type.FishState;

public class SimulationWorld implements IObservable, ISimulationWorld {
	
	private ConvictCichlid cichlid;
	private TankObject tank;
	private CichlidCollection cList;
	private Vector<IObserver> observerList;
	private int fishPoolArr [];
	private ResultSet rs;
	private Connection conn;
	public SimulationWorld()
	{		
		cList = new CichlidCollection();
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
			conn = DriverManager.getConnection("jdbc:ucanaccess://C:/FishPool.accdb");
	
		Statement s = conn.createStatement();
		rs = s.executeQuery("SELECT * FROM [FishPool]");
		while (rs.next())
		{
			String id = rs.getString("ID"); //Field from database ex. FishA, FishB
			int idS =  Integer.parseInt(id);
        	//String value = rs.getString((1)); 
        //ComboItem comboItem = new ComboItem(name, value); 
			System.out.println(id);
			
			if (idS == '1')
			{
				System.out.println("fff");
			}
			else 
			{
				System.out.println("ddd");
			}
		}
		conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
/*
		Connection conn;
		
		try {
			conn = DriverManager.getConnection("jdbc:ucanaccess://C:/FishPool.accdb");
		Statement s = conn.createStatement();

		for (int i = 0; i < this.getFishArr().length; i++)
		{

		if (this.getFishArr()[i] == 1)
		{
			rs = s.executeQuery("SELECT * FROM [FishPool] WHERE ID='1'");
		}
		else if (this.getFishArr()[i] == 2)
		{
			rs = s.executeQuery("SELECT * FROM [FishPool] WHERE ID='2'");
		}
		else if (this.getFishArr()[i] == 3)
		{
			rs = s.executeQuery("SELECT * FROM [FishPool] WHERE ID='3'");
		}
		while (rs.next())
		{
			String id = rs.getString("ID"); // added new string for ID
			String name = rs.getString("Type"); //Field from database ex. FishA, FishB
        	String weight = rs.getString("Weight");
        	String width = rs.getString("Width");
        	String height = rs.getString("Height");
        	String gender = rs.getString("Gender");
        	String aggro = rs.getString("AggroLevel"); //default to 10
        	
      		// name
        	float weightS = Float.parseFloat(weight);
			float widthC = Float.parseFloat(width);
			float heightC = Float.parseFloat(height);
			// gender
			float aggroC = Float.parseFloat(aggro);


	//		cichlid = new ConvictCichlid(name, weightS, widthC, heightC, gender, aggroC);
		System.out.println(id + name + weight + width + height + gender + aggro);
		
			// we'll need to add position later etc.
		}
		}
		conn.close();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		*/
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