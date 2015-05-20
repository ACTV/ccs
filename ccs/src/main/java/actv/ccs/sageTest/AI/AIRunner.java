package actv.ccs.sageTest.AI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import actv.ccs.sageTest.CCSSemaphore;

/**
 * Runnable class to start the AI of the fish
 * 
 * @see AIController
 */
public class AIRunner implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(AIRunner.class);
	private volatile boolean isPaused = false;
	private volatile boolean stop = false;
	private volatile boolean ended = false;
	private AIController aic;
	long lastUpdateTime;
	
	public AIRunner(AIController aic){
		this.aic = aic;
	}

	synchronized public void run() {
		long frameStartTime;
		float elapsedMilliSecs;

		logger.debug("Executing AI!");
		
		aic.startAI();
		
		while (!stop) {
			frameStartTime = System.nanoTime();
			elapsedMilliSecs = (frameStartTime - lastUpdateTime) / (1000000.0f);

			if (elapsedMilliSecs >= 50.0f) {
				lastUpdateTime = frameStartTime;

				try {
					// synchronize the AIController object so we can
					// execute a wait() to pause
					if (isPaused) {
						synchronized (aic) {
							aic.wait();
						}
					}

					// Semaphore used to control the order of MyGame.update()
					// and any updates from the AI behavior tree
					CCSSemaphore.getSemaphore().acquire();
					aic.getBehaviorTreeA().update(elapsedMilliSecs);
					aic.getBehaviorTreeB().update(elapsedMilliSecs);
				//	aic.getBehaviorTreeC().update(elapsedMilliSecs);
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				CCSSemaphore.getSemaphore().release();
			}
			Thread.yield();
		}
		logger.debug("Stopping execution in the thread...");
		ended = true;
	}
	
	public boolean isEnded(){
		return ended;
	}
	public void pause(){
		this.isPaused = true;
	}
	
	public void resume(){
		this.isPaused = true;
		synchronized(aic){
			aic.notify();
		}
	}
	
	public void stop(){
		// Will throw an exception if the AIController object is
		// 	in a wait() state (ie, is currently paused) when
		//	this thread is shutdown.
		if(isPaused){
			resume();
		}
		this.stop = true;
	}
}
