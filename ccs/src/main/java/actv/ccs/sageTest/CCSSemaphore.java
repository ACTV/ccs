package actv.ccs.sageTest;

import java.util.concurrent.Semaphore;


public class CCSSemaphore{
	private static Semaphore semaphore = new Semaphore(1, true);
	
	private CCSSemaphore(){}
	
	public static Semaphore getSemaphore(){
		return semaphore;
	}
}
