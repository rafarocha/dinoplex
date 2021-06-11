package simplex;

import java.math.BigDecimal;

public class Math {
	
	private static int THRESHOLD = 3;
	
	public static Double rd(Double value) {
		return BigDecimal
				.valueOf( value )
				.setScale(THRESHOLD, BigDecimal.ROUND_HALF_UP)
				.doubleValue();
	}
	
	public static Double sum(Double a, Double b) {
		return BigDecimal.valueOf(a)
				.add( BigDecimal.valueOf(b) )
				.doubleValue();
	}
	
	public static Double subtract(Double a, Double b) {
		return BigDecimal.valueOf(a)
				.subtract( BigDecimal.valueOf(b) )
				.doubleValue();
	}

}