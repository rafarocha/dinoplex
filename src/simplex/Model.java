package simplex;

import java.util.ArrayList;
import java.util.List;

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