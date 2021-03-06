package actv.ccs.sageTest.AI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import actv.ccs.model.ConvictCichlid;
import actv.ccs.sageTest.MyGame;
import sage.ai.behaviortrees.BTCompositeType;
import sage.ai.behaviortrees.BTSelector;
import sage.ai.behaviortrees.BTSequence;
import sage.ai.behaviortrees.BehaviorTree;

public class AIController {
	private static final Logger logger = LoggerFactory.getLogger(AIController.class);
	BehaviorTree bt = new BehaviorTree(BTCompositeType.SELECTOR);
	BehaviorTree bt1 = new BehaviorTree(BTCompositeType.SELECTOR);
	BehaviorTree bt2 = new BehaviorTree(BTCompositeType.SELECTOR);
	long startTime;
	long lastUpdateTime;
	MyGame mg;
	ConvictCichlid cc;
	
	private boolean getNearXWallFlag, getNearYWallFlag, getNearZWallFlag;
	public boolean isGetNearXWallFlag() {
		return getNearXWallFlag;
	}

	public boolean isGetNearYWallFlag() {
		return getNearYWallFlag;
	}

	public boolean isGetNearZWallFlag() {
		return getNearZWallFlag;
	}
	private boolean getNearCichlidFlag;
	private boolean getNearObjectFlag;
	
	private ConvictCichlid[] ccList = new ConvictCichlid[3];
	
	
	public AIController(MyGame gg)
	{
		mg = gg;
	}
	
	public BehaviorTree getBehaviorTreeA(){
		if (mg.getCichlidA() != null)
		{
		return bt;
		}
		else return null;
		
	}
	public BehaviorTree getBehaviorTreeB(){
		if (mg.getCichlidB() != null)
		{
		return bt1;
		}
		else return null;
	}
	public BehaviorTree getBehaviorTreeC(){
		
		if (mg.getCichlidC() != null)
		{
		return bt2;
		}
		else return null;
	}
	
	
	public void startAI()
	{
		startTime = System.nanoTime();
		lastUpdateTime = startTime;
		setupAI();
	}
	
	public void setupAI()
	{
		if (mg.getCichlidA() != null)
		{
			ccList[0] = mg.getCichlidA();
			System.out.println(ccList[0]);
			setupBehaviorTreeA();
		}
		if (mg.getCichlidB() != null)
		{
			ccList[1] = mg.getCichlidB();
			System.out.println(ccList[1]);
			setupBehaviorTreeB();
		}
		if (mg.getCichlidC() != null)
		{
			ccList[2] = mg.getCichlidC();
			System.out.println(ccList[2]);
			setupBehaviorTreeC();
		}
		
	}

	/*
	 * if fish is near bounds then either stay thereor reverse direction
	if aggroRange is in between another aggroRange then and both are male and one is bigger than the other
	then they fight


	if fish is near object like a plant, then they will go cover

	if fish aggro range is nothing and not in wall then it will move around
	
	Update: Here is where the behavior trees are set. Refer to the 165 notes for a basic rundown in SAGE AI behavior.
	We tried drools but it looks like it wasn't working with loading/exiting the program at the time, so we switched back 
	to SAGE. For now the bounds are "working" and cichlidA will stop if it hits a big plant, but that's only in the update. 
	If it's possible to find a different implementation of AI to work with SAGE, or also use the BOIDs implementation for fry, then 
	it will make this way more complex. Right now, this is a barebones implementation.
	 */
	public void setupBehaviorTreeA()
	{
	//	bt.insertAtRoot( new BTSequence(10)); // bounds
	//	bt.insertAtRoot(new BTSequence(20)); // 
	//	bt.insertAtRoot(new BTSequence(30));
		
		//bt.insert(10, new IsNearWall(this, ccList[0], false)); // bounds condition
		
		bt.insertAtRoot(new BTSequence(10));
		bt.insertAtRoot(new BTSequence(16));
		bt.insertAtRoot(new BTSequence(17));
		bt.insertAtRoot(new BTSequence(30));
		bt.insert(10, new IsNearXWall(this, ccList[0], false)); // bounds condition
		bt.insert(10, new IdleNearXWall(ccList[0])); // action 1
		

		bt.insert(16, new IsNearYWall(this, ccList[0], false)); // bounds condition
		bt.insert(16, new IdleNearYWall(ccList[0]));
		

		bt.insert(17, new IsNearZWall(this, ccList[0], false)); // bounds condition
		bt.insert(17, new IdleNearZWall(ccList[0]));
		
	//	bt.insert(10, new BTSequence(20));
	//	bt.insert(10, new Move(ccList[0])); **PENDING 
		
	//	bt.insert(20, new CichlidNearChecker()); // cichlid fight condition
	//	bt.insert(20, new CichlidFight()); // then fight
		bt.insert(30, new CichlidNearObject(this, ccList[0], false, mg)); // cichlid object condition
		bt.insert(30, new CichlidHoverObject(ccList[0])); // then hover
	}
	public void setupBehaviorTreeB()
	{
	//	bt1.insertAtRoot( new BTSequence(10)); // bounds
	//	bt.insertAtRoot(new BTSequence(20)); // 
	//	bt.insertAtRoot(new BTSequence(30));
		
		//bt.insert(10, new IsNearWall(this, ccList[0], false)); // bounds condition
		
		bt1.insertAtRoot(new BTSequence(15));
		bt1.insertAtRoot(new BTSequence(16));
		bt1.insertAtRoot(new BTSequence(17));
	//	bt1.insertAtRoot(new BTSequence(30));
		bt1.insert(15, new IsNearXWall(this, ccList[1], false)); // bounds condition
		bt1.insert(15, new IdleNearXWall(ccList[1])); // action 1
		

		bt1.insert(16, new IsNearYWall(this, ccList[1], false)); // bounds condition
		bt1.insert(16, new IdleNearYWall(ccList[1]));
		

		bt1.insert(17, new IsNearZWall(this, ccList[1], false)); // bounds condition
		bt1.insert(17, new IdleNearZWall(ccList[1]));
	//	bt.insert(20, new CichlidNearChecker()); // cichlid fight condition
	//	bt.insert(20, new CichlidFight()); // then fight
	//	bt.insert(30, new CichlidNearObject()); // cichlid object condition
	//	bt.insert(30, new CichlidHoverObject()); // then hover
	}
	public void setupBehaviorTreeC()
	{
	//	System.out.println("i'm being called for C");
	//	bt1.insertAtRoot( new BTSequence(10)); // bounds
	//	bt.insertAtRoot(new BTSequence(20)); // 
	//	bt.insertAtRoot(new BTSequence(30));
		
		//bt.insert(10, new IsNearWall(this, ccList[0], false)); // bounds condition
		
		bt2.insertAtRoot(new BTSequence(15));
		bt2.insert(15, new IsNearXWall(this, ccList[2], false)); // bounds condition
		bt2.insert(15, new IdleNearXWall(ccList[2])); // action 1
		
		bt2.insertAtRoot(new BTSequence(16));
		bt2.insert(16, new IsNearYWall(this, ccList[2], false)); // bounds condition
		bt2.insert(16, new IdleNearYWall(ccList[2]));
		
		bt2.insertAtRoot(new BTSequence(17));
		bt2.insert(17, new IsNearZWall(this, ccList[2], false)); // bounds condition
		bt2.insert(17, new IdleNearZWall(ccList[2]));
	//	bt.insert(20, new CichlidNearChecker()); // cichlid fight condition
	//	bt.insert(20, new CichlidFight()); // then fight
	//	bt.insert(30, new CichlidNearObject()); // cichlid object condition
	//	bt.insert(30, new CichlidHoverObject()); // then hover
	}
	

	public boolean getNearXWallFlagCheck() {
		// TODO Auto-generated method stub
		return getNearXWallFlag;
	}
	public boolean getNearYWallFlagCheck() {
		// TODO Auto-generated method stub
		return getNearYWallFlag;
	}
	public boolean getNearZWallFlagCheck() {
		// TODO Auto-generated method stub
		return getNearZWallFlag;
	}
	public boolean getNearCichlidCheck() {
		// TODO Auto-generated method stub
		return getNearCichlidFlag;
	}
	public boolean getNearObjectCheck() {
		// TODO Auto-generated method stub
		return getNearObjectFlag;
	}

	public void setNearXWallFlag(boolean b) {
		getNearXWallFlag = b;
	}
	public void setNearYWallFlag(boolean b) {
		getNearYWallFlag = b;
	}
	public void setNearZWallFlag(boolean b) {
		getNearZWallFlag = b;
	}
	public void update()
	{
		if (ccList[0] == mg.getCichlidA() && mg.getCichlidA() != null)
		{
			ccList[0].getLocation();
			

			System.out.println("loc 1: " + ccList[0].getLocation());
		}
		if (ccList[1] == mg.getCichlidB() && mg.getCichlidB() != null)
		{
			ccList[1].getLocation();

			System.out.println("loc 2 " + ccList[1].getLocation());
		}
		if (ccList[2] == mg.getCichlidC() && mg.getCichlidC() != null)
		{
			ccList[2].getLocation();
			System.out.println("loc 3: " + ccList[2].getLocation());
		}
	}
}

