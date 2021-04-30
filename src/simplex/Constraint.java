package simplex;

public class Constraint extends Expression {
	
	Signal signal;
	
	public Constraint(El el, Signal signal, Double value) {
		super( el.variables );
		super.value = value;
		this.signal = signal;
	}
	
	public int length() {
		return super.variables.length;
	}
	
}