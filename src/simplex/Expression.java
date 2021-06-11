package simplex;

public class Expression {
	Double[] variables;
	Double value;
	
	public Expression(Double ... variables) {
		this.variables = variables;
		this.value = 0d;
	}
	
	public Double[] getAll() {
		Double[] x = new Double[this.variables.length + 1];
		for (int i = 0; i < this.variables.length; i++) {
			x[i] = this.variables[i];
		}
		x[this.variables.length] = ( value == null ) ? 0d : value;
		return x;
	}
	
	public Double[] getVariables() {
		return this.variables;
	}
	
	@Override public String toString() {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < variables.length; i++) {
			String s = ( variables[i] >= 0 ) ? "+" : "";
			double v = (variables[i]);
			builder.append( s + v + "x" + (i+1) + " " );
		}
		return builder.toString();
	}
	
	public Double getVariable(int i) {
		return this.variables[i];
	}
	
}