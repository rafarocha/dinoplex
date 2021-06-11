package simplex;

public enum Strategy {
	
	Default, TwoPhases, BigO;
	
	public int stage; // min 0, max 1 -> twoPhases
	
	public Strategy stage(int value) {
		this.stage = value; return this;
	}
	
	public boolean hasFinished() {
		if ( this.equals(Strategy.TwoPhases) )
			return (stage == 1);
		return true; // ?
	}

}