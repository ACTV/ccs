package actv.ccs.sageTest.AI;

import actv.ccs.model.ConvictCichlid;
import actv.ccs.sageTest.MyGame;
import sage.ai.behaviortrees.BTCompositeType;
import sage.ai.behaviortrees.BTSequence;
import sage.ai.behaviortrees.BehaviorTree;

public class AIController {
	BehaviorTree bt = new BehaviorTree(BTCompositeType.SELECTOR);
	long startTime;
	long lastUpdateTime;
	MyGame mg;
	ConvictCichlid cc;
	
	boolean getNearWallFlag;
	boolean getNearCichlidFlag;
	boolean getNearObjectFlag;
	
	private ConvictCichlid[] ccList = new ConvictCichlid[3];
	
	
	public AIController(MyGame gg)
	{
		mg = gg;
	}
	
	public void startAI()
	{
	startTime = System.nanoTime();
	lastUpdateTime = startTime;
	setupAI();
	setupBehaviorTree();
	AILoop();
	}
	
	public void setupAI()
	{
		if (mg.getCichlidA() != null)
		{
			ccList[0] = mg.getCichlidA();
			System.out.println(ccList[0]);
		}
		if (mg.getCichlidB() != null)
		{
			ccList[1] = mg.getCichlidB();
			System.out.println(ccList[1]);
		}
		if (mg.getCichlidC() != null)
		{
			ccList[2] = mg.getCichlidC();
			System.out.println(ccList[2]);
		}
		
	}
	 public void AILoop()
	 { while (true)
		 { 
			 long frameStartTime = System.nanoTime();
			 float elapsedMilliSecs = (frameStartTime-lastUpdateTime)/(1000000.0f);
			 if (elapsedMilliSecs >= 50.0f)
			 { lastUpdateTime = frameStartTime;
		//	 npc.updateLocation();
		//	 server.sendNPCinfo();
			 bt.update(elapsedMilliSecs);
		 }
		 Thread.yield();
		 } 
	 }
	/*
	 * if fish is near bounds then either stay thereor reverse direction
	if aggroRange is in between another aggroRange then and both are male and one is bigger than the other
	then they fight


	if fish is near object like a plant, then they will go cover

	if fish aggro range is nothing and not in wall then it will move around
	 */
	public void setupBehaviorTree()
	{
		bt.insertAtRoot(new BTSequence(10)); // bounds
	//	bt.insertAtRoot(new BTSequence(20)); // 
	//	bt.insertAtRoot(new BTSequence(30));
		bt.insert(10, new IsNearWall(this, cc, false)); // bounds condition
		bt.insert(10, new IdleNearWall(cc)); // action 1
	//	bt.insert(20, new CichlidNearChecker()); // cichlid fight condition
	//	bt.insert(20, new CichlidFight()); // then fight
	//	bt.insert(30, new CichlidNearObject()); // cichlid object condition
	//	bt.insert(30, new CichlidHoverObject()); // then hover
	}

	public boolean getNearWallFlagCheck() {
		// TODO Auto-generated method stub
		return getNearWallFlag;
	}
	public boolean getNearCichlidCheck() {
		// TODO Auto-generated method stub
		return getNearCichlidFlag;
	}
	public boolean getNearObjectCheck() {
		// TODO Auto-generated method stub
		return getNearObjectFlag;
	}

	public void setNearWallFlag(boolean b) {
		getNearWallFlag = b;
	}
	
	public void update()
	{
		/*
		for (int i = 0; i < ccList.length; i++)
		{
			System.out.println("RIPPED OUT FROM THE PLANET");
			if (ccList[0] != null)
			{
			System.out.println(mg.getCichlidA().getName());
			}
			
		}
		*/
		
		// the cichlids are getting the location
		if (ccList[0] == mg.getCichlidA() && mg.getCichlidA() != null)
		{
			ccList[0].getLocation();

		//	System.out.println("loc 1: " + ccList[0].getLocation());
		}
		if (ccList[1] == mg.getCichlidB() && mg.getCichlidB() != null)
		{
			ccList[1].getLocation();

		//	System.out.println("loc 2 " + ccList[1].getLocation());
		}
		if (ccList[2] == mg.getCichlidC() && mg.getCichlidC() != null)
		{
			ccList[2].getLocation();
		//	System.out.println("loc 3: " + ccList[2].getLocation());
		}
	}
}

