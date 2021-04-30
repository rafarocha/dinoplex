package simplex;

public enum Signal {

	Equals("="), LessEqual("<="), GreaterEqual(">=");
	
	public String symbol;

	private Signal(String symbol) {
		this.symbol = symbol;
	}
	
}