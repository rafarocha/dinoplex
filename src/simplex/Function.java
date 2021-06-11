package simplex;

import static java.lang.Double.*;
import static simplex.Strategy.Default;
import static simplex.Math.rd;


// rules biggest values
// MIN col +negative, row +positive
// MAX col +positive, row +negative
public enum Function {
	MIN, MAX;
	
	public boolean acceptCol(Double lastAccepted, Double iterated ) {
		if ( iterated == 0 ) return false; // ignore if zero
		switch ( this ) {
			case MIN: return rd(lastAccepted) > rd(iterated);
			case MAX: return rd(lastAccepted) < rd(iterated);
		}
		return false;
	}
		
	public boolean acceptRow(Double lastAccepted, Double iterated ) {
		if ( iterated <= 0 ) return false; // ignore if zero or negative
		switch ( this ) {
			case MIN: return rd(lastAccepted) > rd(iterated);
			case MAX: return rd(lastAccepted) > rd(iterated);
		}
		return false;
	}
	
	public boolean isOptimality(Model model) {
		int count = 0;
		
		Double[][] tableau = model.last();

		for (int j = 0; j < tableau[0].length; j++) {
			double value = tableau[0][j];
			switch ( this ) {
				case MIN: if (value < 0) count++; continue;
				case MAX: if (value > 0) count++;
			}
		}
		
		return (count == 0);
	}
	
	public double initialInverseAcceptedCol() {
		return ( this.equals(MAX) ) ? -1 * MAX_VALUE : MAX_VALUE;
	}

	public double initialInverseAcceptedRow() {
		return ( this.equals(MAX) ) ? -1 * MAX_VALUE : MAX_VALUE;
	}
	
}