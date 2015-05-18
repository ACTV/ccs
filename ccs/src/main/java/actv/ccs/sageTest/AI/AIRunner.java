package actv.ccs.sageTest.AI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AIRunner implements Runnable {
	private AIController aic;
	private static final Logger logger = LoggerFactory.getLogger(AIRunner.class);
	
	public AIRunner(AIController aic){
		this.aic = aic;
	}

	public void run() {
		logger.debug("Executing AI!");
		long frameStartTime;
		float elapMilSecs;
		long lastUpdateTime=0;
		
		aic.setupAI();
		while (true){
			frameStartTime = System.nanoTime();
			elapMilSecs = (frameStartTime-lastUpdateTime)/(1000000.0f);
			if (elapMilSecs >= 50.0f){
				aic.update();
			}
		}
	}

}
