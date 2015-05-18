package actv.ccs.sageTest;

import java.util.concurrent.Semaphore;

/**
 * <p>A binary semaphore to provide mutual exclusion for 
 * 	any object updates between MyGame and AIController.</p>
 * <p>Note: This is specifically meant for use between these two updates</p>
 */
public class CCSSemaphore{
	private static Semaphore semaphore = new Semaphore(1, true);
	
	private CCSSemaphore(){}
	
	public static Semaphore getSemaphore(){
		return semaphore;
	}
}
