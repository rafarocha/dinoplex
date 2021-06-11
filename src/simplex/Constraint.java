package simplex;

import static simplex.Signal.GreaterEqual; 
import static simplex.Signal.Equal;
import static simplex.Signal.LessEqual;

public class Constraint extends Expression {
	
	Signal signal;
	Double slack;
	
	public Constraint(El el, Signal signal, Double value) {
		super( el.variables );
		super.value = value;
		this.signal = signal;
		this.slack = signal.getSlackValue();
	}
	
	public int length() {
		return super.variables.length;
	}
	
	public Double getSlackVariable() {
		return this.signal.getSlackValue();
	}
	
	public boolean hasSlackVariableUnacceptableMatrixIdentity() {
		return !(this.slack == 0d || this.slack == 1d);
	}
	
	// multiply everything by -1 if base is less than zero
	public Constraint multiplyAllByMinusOne() {
		for (int i = 0; i < variables.length; i++) {
			variables[i] = variables[i] * -1;
		}
		this.value = this.value * -1;
		
		switch ( this.signal ) {
			case LessEqual: changeSignal(GreaterEqual); break;
			case GreaterEqual: changeSignal(LessEqual); break;
			default: return this;
		}
		return this;
	}
	
	// when base is negative, see method multiplyAllByMinusOne()
	private void changeSignal(Signal newSignal) {
		this.signal = newSignal;
		this.slack = newSignal.getSlackValue();
	}
	
	@Override public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append( super.toString() );
		builder.append( signal.symbol  + " " + this.value );
		builder.append( " .. slk=" + (slack.intValue()) );
		return builder.toString();
	}
	
	public Double getLeftOverValue() {
		switch ( this.signal ) {
			case GreaterEqual: return 1d;
			default: return 0d; // including LessEqual
		}
	}
	
}