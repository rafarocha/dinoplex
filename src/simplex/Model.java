package simplex;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Model {
	
	Function function;
	double[][] objectiveFunction;
	double[][] constraints;
	
	public Model(Function function, double[][] of, double[][] ct) {
		this.function = function;
		this.objectiveFunction = of;
		this.constraints = ct;
	}
	
	Expression expression;
	Constraint[] constraintsSSS;

	public Model(Function fun, Expression exp, Constraint ... cts) {
		this.function = fun;
		this.expression = exp;
		this.constraintsSSS = cts;
		// older
		this.objectiveFunction = matrix( exp.getValues() );
		
		//setar constraint
		//this.constraints = matrix ( cts. )
//		double[][] x = new double[cts.length][4];
//		for (Constraint constraint : cts) {
//			constraint.getValues();
//		}
	}
	
	private static double[][] matrix( Double[][] doubles ) {
		return new double[][] { // first element or bug!
			Stream.of( doubles[0] ).mapToDouble(Double::doubleValue).toArray()
		};
	}
	
	public Double[] getObjectiveFunction() {
		return expression.variables;
	}
	
	public double[][] getConstraints() {
		List<Double[]> list = new ArrayList<Double[]>();
		for (Constraint cts : constraintsSSS) {
			list.add( cts.variables );
		}
		return null;
	}
	
}