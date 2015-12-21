package random;

public class ExponentialRandomGenerator extends RandomGenerator {
	double lambda;
	
	public ExponentialRandomGenerator(double lambda) {
		super();
		this.lambda = lambda;
	}
	// overridden methods
	@Override
	public double nextRand() {
		double r = super.nextRand();		
		return -Math.log(r) / lambda;
	}
}
