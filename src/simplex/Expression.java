package simplex;

public class Expression {
	Double[] variables;
	Double value;
	
	public Expression(Double ... variables) {
		this.variables = variables;
		this.value = variables[variables.length-1];
	}
	
	public Double[][] getValues() {
		return new Double[][] {
			this.variables, 
			new Double[] { this.value }
		};
	}
	
}