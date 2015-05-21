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
	private BehaviorTree bt;
	private BehaviorTree bt1;
	private BehaviorTree bt2;
	private long startTime;
	private long lastUpdateTime;
	private MyGame mg;
	private ConvictCichlid cc;
	private boolean getNearCichlidFlag;
	private boolean getNearObjectFlag;
	private ConvictCichlid[] ccList = new ConvictCichlid[3];
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

	public AIController(MyGame gg) {
		mg = gg;
	}

	public BehaviorTree getBehaviorTreeA() {
		if (mg.getCichlidA() != null) {
			return bt;
		} else
			return null;

	}

	public BehaviorTree getBehaviorTreeB() {
		if (mg.getCichlidB() != null) {
			return bt1;
		} else
			return null;
	}

	public BehaviorTree getBehaviorTreeC() {

		if (mg.getCichlidC() != null) {
			return bt2;
		} else
			return null;
	}

	public void startAI() {
		startTime = System.nanoTime();
		lastUpdateTime = startTime;
		setupAI();
	}

	public void setupAI() {
		if (mg.getCichlidA() != null) {
			ccList[0] = mg.getCichlidA();
			System.out.println(ccList[0]);
			bt = setupBehaviorTree(ccList[0]);
		}
		if (mg.getCichlidB() != null) {
			ccList[1] = mg.getCichlidB();
			System.out.println(ccList[1]);
			bt1 = setupBehaviorTree(ccList[1]);
		}
		if (mg.getCichlidC() != null) {
			ccList[2] = mg.getCichlidC();
			System.out.println(ccList[2]);
			bt2 = setupBehaviorTree(ccList[2]);
		}

	}

	/*
	 * if fish is near bounds then either stay thereor reverse direction if
	 * aggroRange is in between another aggroRange then and both are male and
	 * one is bigger than the other then they fight
	 * 
	 * 
	 * if fish is near object like a plant, then they will go cover
	 * 
	 * if fish aggro range is nothing and not in wall then it will move around
	 */
	public BehaviorTree setupBehaviorTree(ConvictCichlid cichlid) {
		BehaviorTree btt = new BehaviorTree(BTCompositeType.SEQUENCE);

		// btt.insertAtRoot( new BTSequence(10)); // bounds
		// btt.insertAtRoot(new BTSequence(20)); //
		// btt.insertAtRoot(new BTSequence(30));

		// btt.insert(10, new IsNearWall(this, ccList[0], false)); // bounds
		// condition

		btt.insertAtRoot(new BTSequence(10));

		btt.insert(10, new BTSequence(16));
		btt.insert(10, new BTSequence(17));
		btt.insert(10, new BTSequence(30));

		btt.insert(10, new IsNearXWall(this, cichlid, false));
		btt.insert(10, new IdleNearXWall(cichlid)); 

		btt.insert(16, new IsNearYWall(this, cichlid, false));
		btt.insert(16, new IdleNearYWall(cichlid));

		btt.insert(17, new IsNearZWall(this, cichlid, false));
		btt.insert(17, new IdleNearZWall(cichlid));

		// btt.insert(10, new BTSequence(20));
		// btt.insert(10, new Move(ccList[0])); **PENDING

		// btt.insert(20, new CichlidNearChecker()); // cichlid fight condition
		// btt.insert(20, new CichlidFight()); // then fight
		
		btt.insert(30, new CichlidNearObject(this, cichlid, false, mg));
		btt.insert(30, new CichlidHoverObject(cichlid)); // then hover

		return btt;
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

	public void update() {
		/*
		 * for (int i = 0; i < ccList.length; i++) {
		 * System.out.println("RIPPED OUT FROM THE PLANET"); if (ccList[0] !=
		 * null) { System.out.println(mg.getCichlidA().getName()); }
		 * 
		 * }
		 */

		// the cichlids are getting the location
		if (ccList[0] == mg.getCichlidA() && mg.getCichlidA() != null) {
			ccList[0].getLocation();

			System.out.println("loc 1: " + ccList[0].getLocation());
		}
		if (ccList[1] == mg.getCichlidB() && mg.getCichlidB() != null) {
			ccList[1].getLocation();

			System.out.println("loc 2 " + ccList[1].getLocation());
		}
		if (ccList[2] == mg.getCichlidC() && mg.getCichlidC() != null) {
			ccList[2].getLocation();
			System.out.println("loc 3: " + ccList[2].getLocation());
		}
	}
}
