package actv.ccs.fact;

import java.util.Random;

/**
 * <p>
 * The PRNG class is used as a global variable in the
 * drools knowledge session to generate a pseudo random numbers and boolean.
 * </p>
 * 
 * <p> Supported outputs are : double, int, and boolean. </p>
 * 
 */
public class PRNG {
	private Random prng;
	
	public PRNG() {
		prng = new Random(System.currentTimeMillis());
	}

	public double randomDouble(){
		return prng.nextDouble();
	}
	
	public boolean randomBoolean(){
		return prng.nextBoolean();
	}
	
	public int randomInt(int max){
		return prng.nextInt(max);
	}
	
	@Override
	public String toString(){
		return "[ PRNG ]";
	}
}
