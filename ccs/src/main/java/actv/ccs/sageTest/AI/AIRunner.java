package actv.ccs.sageTest.AI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Runnable class to start the AI of the fish
 * 
 * @see AIController
 */
public class AIRunner implements Runnable {
	private AIController aic;
	private static final Logger logger = LoggerFactory.getLogger(AIRunner.class);
	
	public AIRunner(AIController aic){
		this.aic = aic;
	}

	public void run() {
		logger.debug("Executing AI!");
		aic.startAI();
	}
}
