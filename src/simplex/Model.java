package simplex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Model {
	
	Function function;
	Expression objectiveFunction;
	Constraint[] constraints;
	Strategy strategy;
	List<Double[][]> tableaus;

	public Model(Function fun, Expression exp, Constraint ... cts) {
		this.function = fun;
		this.objectiveFunction = exp;
		this.constraints = cts;
		this.strategy = Strategy.Default;
		this.tableaus = new ArrayList<Double[][]>();
	}
	
	public Double[] getObjectiveFunction() {
		return objectiveFunction.variables;
	}
	
	public boolean isImpossibleMountMatrixIdentityWithSlackVariables() {
		for (Constraint constraint : constraints) {
			if ( constraint.hasSlackVariableUnacceptableMatrixIdentity() )
				return true;
		}
		return false;
	}
	
	public Double[] getSlackVariables() {
		Double[] slacks = new Double[this.constraints.length];
		for (int i = 0; i < constraints.length; i++) {
			slacks[i] = constraints[i].getSlackVariable();
		}
		return slacks;
	}

	public Double[][] getConstraintsValues() {
		Double[][] cts = new Double[getConstraintsCount()][];
		for (int i = 0; i < getConstraintsCount(); i++) {
			cts[i] = constraints[i].getAll();
		}
		return cts;
	}
	
	public int getConstraintsCount() {
		return this.constraints.length;
	}

	public Model change(Strategy newStrategy) {
		this.strategy = newStrategy; return this;
	}
	
	public Model add(Double[][] tableau) {
		this.tableaus.add( tableau );
		return this;
	}
	
	public boolean isOptimality() {
		return this.function.isOptimality( this );
	}
	
	public Double[][] last() {
		if ( this.tableaus.isEmpty() ) return null;
		return tableaus.get( tableaus.size() - 1 );
	}
	
	public Double[][] queue() {
		Double[][] tableau = cloneLastTableau();
		this.add( tableau );
		return tableau;
	}
	
	private Double[][] cloneLastTableau() {
		return Arrays.stream( this.last() ).map(Double[]::clone).toArray(Double[][]::new);
	}
	
	public boolean isTwoPhasesStrategy() {
		return this.strategy.equals( Strategy.TwoPhases );
	}
	
	public boolean hasFinishedStrategy() {
		return this.strategy.hasFinished();
	}
	
	public boolean isFunctionMinimization() {
		return this.function.equals( Function.MIN );
	}
	
	public Double[] getAllObjectiveFunction() {
		return this.objectiveFunction.getAll();
	}
	
	public Double[] getVariablesObjectiveFunction() {
		return this.objectiveFunction.getVariables();
	}
	
	
	
	public Double[] getVariables() {
		return this.objectiveFunction.getVariables();
	}
	
	public List<Integer> getListIndexColRestrictionsThatNotSatisfySignal(Signal ... signals) {
		int countVars = this.getVariables().length;
		List<Integer> list = new ArrayList<Integer>();
		int idx = countVars;
		
		for (Constraint cstr : this.constraints) {
			boolean contains = Arrays.asList( signals ).contains( cstr.signal );
			if ( !contains ) {
				list.add( idx );
			}
			idx++;
		}
		return list;
	}
	
	public List<Integer> getListIndexRowRestrictionsThatNotSatisfySignal(Signal ... signals) {
		List<Integer> list = new ArrayList<Integer>();
		int idx = 1;
		
		for (Constraint cstr : this.constraints) {
			boolean contains = Arrays.asList( signals ).contains( cstr.signal );
			if ( !contains ) {
				list.add( idx );
			}
			idx++;
		}
		return list;
	}

}