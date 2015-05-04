package actv.ccs.fact;

import java.util.Random;

public class PRNG {
	private Random prng;
	
	public PRNG() {
		prng = new Random(System.currentTimeMillis());
	}

	public Random getPrng() {
		return prng;
	}
}
