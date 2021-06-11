package simplex;

public enum Signal {

	Equal("="), LessEqual("<="), GreaterEqual(">=");
	
	public String symbol;

	private Signal(String symbol) {
		this.symbol = symbol;
	}
	
	public Double getSlackValue() {
		switch ( this ) {
			case GreaterEqual: return -1d;
			default: return 0d; // including LessEqual
		}
	}
	
}