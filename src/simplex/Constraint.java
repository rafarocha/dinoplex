package simplex;

public class Constraint extends Expression {
	
	Double value;
	Signal signal;
	
	public Constraint(El el, Signal signal, Double value) {
		super(el.variables);
		this.signal = signal;
		this.value = value;
	}
	
	public int getVariablesCount() {
		return super.variables.length;
	}

}