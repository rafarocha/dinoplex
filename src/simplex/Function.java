package simplex;

import static java.lang.Double.*;

// rules biggest values
// MIN col +negative, row +positive
// MAX col +positive, row +negative
public enum Function {
	MIN, MAX;
	
	public boolean acceptCol(double lastAccepted, double iterated ) {
		switch ( this ) {
			case MIN: return lastAccepted > iterated;
			case MAX: return lastAccepted < iterated;
		}
		return false;
	}

	public boolean acceptRow(double lastAccepted, double iterated ) {
		if ( iterated < 0 ) return false;
		switch ( this ) {
			case MIN: return lastAccepted < iterated;
			case MAX: return lastAccepted > iterated;
		}
		return false;
	}
	
	public boolean isOptimality(double[][] tableau) {
		int count = 0;
		for (int i = 0; i < 1; i++) {
			for (int j = 0; j < tableau[i].length; j++) {
				double value = tableau[i][j];
				switch ( this ) {
					case MIN: if (value < 0) count++; continue;
					case MAX: if (value > 0) count++;
				}
			}
		}
		return (count == 0);
	}
	
	public double initialInverseAcceptedCol() {
		return ( this.equals(MAX) ) ? -1 * MAX_VALUE : MAX_VALUE;
	}

	public double initialInverseAcceptedRow() {
		return ( this.equals(MAX) ) ? MAX_VALUE : -1 * MAX_VALUE;
	}
	
}