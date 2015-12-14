package random;

/**
 * returns a random number from l to r
 * @author ahmed
 *
 */
public class UniformRandomGenerator extends RandomGenerator
{
	/* fields */
	long l, r;
	
	/* constructor */
	public UniformRandomGenerator(long l, long r)
	{
		super();
		this.l = l;
		this.r = r;
	}
	
	@Override
	public double nextRand()
	{
		double R =  super.nextRand();
		return l + (r-l)*R;
	}

}
