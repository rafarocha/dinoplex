package simplex;

import static simplex.Math.rd;
import static simplex.Math.sum;
import static simplex.Strategy.Default;

import java.util.Arrays;
import java.util.List;

public class Simplex {
	
	public static void main(String[] args) {
		
		Model model = Problems.P6;
		
		treatNegativeBasisIfAny( model ); /// mudar se negativo apos igualdade, checar!
		mountTableau( model, 0 );
		
		System.out.println("iter 0"); // melhorar o print das iteracoes
		print( model.last() );
		
		calculate( model );
		
		updateSignalFinalResultObjectiveFunctionByMinusOneWhenMinimization( model );
		
		System.out.println("\nafter");
		print( model.last() );
	}

	private static void updateSignalFinalResultObjectiveFunctionByMinusOneWhenMinimization(Model model) {
		if ( model.isFunctionMinimization() ) {
			Double[][] tableau = model.last();
			for (int i = 0; i < tableau[0].length; i++) {
				double value = tableau[0][i];
				if ( value == 0 ) continue;
				tableau[0][i] *= -1;			
			}
		}
	}

	private static void print(Double[][] tableau) {
		for (int i = 0; i < tableau.length; i++) {
			System.out.print( "line " + i + ": ");
			for (int j = 0; j < tableau[i].length; j++) {
				System.out.print( tableau[i][j] + ", " );
			}
			System.out.println();
		}
		System.out.println();
	}

	private static void calculate(Model model) {
		int iter = 1;
		Double[][] tableau = null;
		
		boolean quit = false;
		while (!quit) {
			if ( model.isOptimality() ) {
				if ( !model.hasFinishedStrategy() ) {
					if ( model.isTwoPhasesStrategy() ) {
						System.out.println( "start two phase ..." );
						calculateTwoPhaseStrategy( model );
						print( model.last() );
						continue;
					}
				}
				break;				
			}
			
			tableau = model.queue(); 
			
			int indexPivotColum = findIndexColumnEntering( model );
			int indexPivotRow = findIndexRowPivot( indexPivotColum, model );
			
			String pivot = "pivot tableau[" + indexPivotRow + "][" + indexPivotColum + "]="; 
			System.out.println( pivot + tableau[indexPivotRow][indexPivotColum] );
			
			updateDivideElementsRowByBaseOfThePivot( tableau, indexPivotRow, indexPivotColum );
			updateZeroingElementsAgainstPivot( tableau, indexPivotRow, indexPivotColum );
			
			System.out.println("\niter " + iter++ );
			print( tableau );
			System.out.print("");
		}
	}
	
	private static void calculateTwoPhaseStrategy(Model model) {
		Double[][] tableau = model.last();
		
		removeColumnsArtificialVariablesFromRestrictionsExceptWithSignalLessThan( model, tableau );
		setupInitialValuesFromObjetiveFunction( model, model.last() );
		resetObjectiveFunction( model, model.last() );
	}

	private static void removeColumnsArtificialVariablesFromRestrictionsExceptWithSignalLessThan(Model model, Double[][] tableau) {
		List<Integer> indexesConstraintsNotSatisfy = model.getListIndexColRestrictionsThatNotSatisfySignal( Signal.LessEqual );
		
		int colsResize = tableau[0].length - indexesConstraintsNotSatisfy.size();
		int countVars = model.getVariables().length;
		
		Double[][] newTableau = tableau(tableau.length, colsResize);
		
		for (int i = 0; i < tableau.length; i++) {
			int k = 0;
			for (int j = 0; j < tableau[i].length; j++) {
				if ( j >= countVars && indexesConstraintsNotSatisfy.contains(j) ) {
					continue;					
				}
				newTableau[i][k] = tableau[i][j];
				k = k+1;
			}
		}
		
		model.add( newTableau );
	}

	private static void setupInitialValuesFromObjetiveFunction(Model model, Double[][] tableau) {
		Double[] variables = model.getVariables();
		int length = tableau[0].length;
		for (int i = 0; i < length-1; i++) {
			for (int j = i; j < variables.length; j++) {
				tableau[0][i] = variables[i];
				i = j+1;
			}
			tableau[0][i] = 0d;
		}
		tableau[0][length-1] = model.objectiveFunction.value;
	}

	private static void resetObjectiveFunction(Model model, Double[][] tableau) {
		List<Integer> idxs = model.getListIndexRowRestrictionsThatNotSatisfySignal(Signal.GreaterEqual, Signal.Equal);
		double value = 0, vx = 0, vc = 0;
		
		for (Integer idx : idxs) {
			for (int col = 0; col < tableau[0].length; col++) {
				Double[] variables = model.getVariables();
				if ( col <= variables.length-1 ) {
					vx = tableau[0][col];
					vc = tableau[idx][col];
					value += ( vc == 0 ) ? 0 : (vc - vx);
					tableau[0][col] = 0d;
				} else {
					vx = tableau[0][col];
					vc = tableau[idx][col];
					tableau[0][col] = (vx + vc);					
				}
			}			
		}
		tableau[0][tableau[0].length-1] = (vx + vc) - value;
	}

	private static void updateObjectiveFunctionByMinusOne(double[][] tableau) {
		for (int i = 0; i < tableau[0].length; i++) {
			if ( tableau[0][i] == 0 ) continue;
			tableau[0][i] *= -1; 
		}
	}

	private static void updateZeroingElementsAgainstPivot(Double[][] tableau, int row, int col) {
		for (int i = 0; i < tableau.length; i++) {
			if ( i == row ) continue;
			double px = tableau[i][col];
			for (int j = 0; j < tableau[i].length; j++) {
				double r1 = (tableau[i][j]);		
				double py = (tableau[row][j]);
				
				double r2 = (px * py);
				
//				double cl = (r2 > 0) ? subtract(r1,r2) : sum(r1,(r2*-1));
//				double cl = (r2 > 0) ? r1 - r2 : r1 + (r2 * -1);
				double cl = (r2 > 0) ? r1 - r2 : sum(r1,(r2*-1));
				tableau[i][j] = rd(cl);
			}
			System.out.print("");
		}
		System.out.print("");
	}

	private static void updateDivideElementsRowByBaseOfThePivot(Double[][] tableau, int row, int col) {
		double pivot = tableau[row][col];
		for (int i = 0; i < tableau[row].length; i++) {
			tableau[row][i] = rd(tableau[row][i] / pivot); 
		}
	}

	private static int findIndexRowPivot(int column, Model model) {
		int row = 0;
		Double[][] tableau = model.last();
		int length = ( model.strategy.equals(Default) ) ? tableau.length : tableau.length-1; // because last line is W, when two phases strategy
		
		double lastAccepted = model.function.initialInverseAcceptedRow();
		for (int i = 1; i <= length; i++) {
			double element = tableau[i][column];
			double ratio = tableau[i][tableau[i].length-1] / element;
			if ( model.function.acceptRow(lastAccepted, ratio) ) {
				lastAccepted = ratio;
				row = i;
			}
		}
		return row;
	}

	private static int findIndexColumnEntering(Model model) {
		int column = 0;
		Double[][] tableau = model.last();
		double lastAccepted = model.function.initialInverseAcceptedCol();
		
		for (int j = 0; j < tableau[0].length-1; j++) {
			if ( model.function.acceptCol(lastAccepted, tableau[0][j]) ) { 
				lastAccepted = tableau[0][j];
				column = j;
			}
		}
		
		return column;
	}

	public static void treatNegativeBasisIfAny(Model model) {
		for (Constraint constraint : model.constraints) {
			if ( constraint.value < 0 )
				constraint.multiplyAllByMinusOne();
		}
	}

	private static Double[][] tableau(int countLines, int countCols) {
		Double[][] tableau = new Double[countLines][countCols];

		for (int i = 0; i < tableau.length; i++) {
			Arrays.fill(tableau[i], 0d);
		}
		return tableau;
	}
	
	private static Double[][] mountMatrixIdentity(Model model) {
		// TODO what if matriz 5x4? ... I assemble 5x5
		
		int length = model.getConstraintsCount();
		Double[][] identity = new Double[length][length]; 
		int indexSetOne = 0;
		for (int i = 0; i < length; i++) {
			Arrays.fill(identity[i], 0d);
			identity[i][indexSetOne++] = 1d;
		}
		return identity;
	}
	
	private static void mountTableau(Model model, int x) {
		// slack variables and matrix identity 
		Double[] slackVars = model.getSlackVariables();
		Double[][] matrixIdentity = mountMatrixIdentity( model );
		
		if ( !model.isImpossibleMountMatrixIdentityWithSlackVariables() ) {
			mountTableau( model, matrixIdentity );
			return;
		} else {
			model.change( Strategy.TwoPhases );
		}
		
		// objetive function + constraints
		Double[] of = model.getAllObjectiveFunction();
		Double[][] rt = model.getConstraintsValues();
		
		// mount tableau
		int countColSlack = 1;
		int countCols = of.length + countColSlack + rt.length; // s1 + {a1,a2,..} 
		int countLines = 1 + model.constraints.length; // of + cstr + w ... melhorar para casos mais complexos com mais de duas variaveis de folga e mais de duas variaveis artificiais
		int countVars = model.getVariablesObjectiveFunction().length;
		
		Double[][] tableau = tableau(countLines, countCols);
		
		// fill values constraints
		for (int i = 0; i < rt.length; i++) {
			int lic = rt[i].length-1; // last index of constraint = index value after signal
			int mic = matrixIdentity[i].length; // number of elements, identity matrix
			
			System.arraycopy(rt[i], 0, tableau[i+1], 0, countVars); // objective function
			System.arraycopy(matrixIdentity[i], 0, tableau[i+1], countVars, mic);  // artificial variables, matrix identity
			System.arraycopy(slackVars, i, tableau[i+1], countCols-2, 1);  // s1
			System.arraycopy(rt[i], lic, tableau[i+1], countCols-1, 1); // value after signal
			
			// function W thats ..
			if ( model.constraints[i].signal.equals(Signal.LessEqual) ) continue;
			for (int j = 0; j < countVars + countColSlack; j++) { // +1
				tableau[0][j] += ( tableau[i+1][j] * -1 ); // sum columns a1 + a2 + ..
			}
			tableau[0][countCols-2] += model.constraints[i].getLeftOverValue();
			tableau[0][countCols-1] += ( tableau[i+1][countCols-1] * -1 ); // sum values columns a1 + a2 + .. 
			System.out.println();
		}

		model.add( tableau );
	}
	
	private static void mountTableau(Model model) {
		// slack variables and matrix identity 
		Double[] slackVars = model.getSlackVariables();
		Double[][] matrixIdentity = mountMatrixIdentity( model );
		
		if ( !model.isImpossibleMountMatrixIdentityWithSlackVariables() ) {
			mountTableau( model, matrixIdentity );
			return;
		} else {
			model.change( Strategy.TwoPhases );
		}
		
		// objetive function + constraints
		Double[] of = model.getAllObjectiveFunction();
		Double[][] rt = model.getConstraintsValues();
		
		// mount tableau
		int countColSlack = 1;
		int countCols = of.length + countColSlack + rt.length; // s1 + {a1,a2,..} 
		int countLines = 1 + model.constraints.length + 1; // of + cstr + w ... melhorar para casos mais complexos com mais de duas variaveis de folga e mais de duas variaveis artificiais
		int countVars = model.getVariablesObjectiveFunction().length;
		
		Double[][] tableau = tableau(countLines, countCols);
		
		// fill values constraints
		System.arraycopy(of, 0, tableau[0], 0, countVars); // objective function
		for (int i = 0; i < rt.length; i++) {
			int lic = rt[i].length-1; // last index of constraint = index value after signal
			int mic = matrixIdentity[i].length; // number of elements, identity matrix
			
			System.arraycopy(rt[i], 0, tableau[i+1], 0, countVars); // objective function
			System.arraycopy(slackVars, i, tableau[i+1], countVars, 1);  // s1
			System.arraycopy(matrixIdentity[i], 0, tableau[i+1], countVars+1, mic);  // artificial variables, matrix identity
			System.arraycopy(rt[i], lic, tableau[i+1], countCols-1, 1); // value after signal
			
			// function W thats  
			for (int j = 0; j < countVars + countColSlack; j++) { // +1
				tableau[countLines-1][j] += ( tableau[i+1][j] * -1 ); // add columns a1 + a2 + ..
			}
			tableau[countLines-1][countCols-1] += ( tableau[i+1][countCols-1] * -1 ); // add columns a1 + a2 + ..
		}

		model.add( tableau );
	}

	private static void mountTableau(final Model model, Double[][] matrixIdentity) {
		Double[][] of = new Double[][] { model.getObjectiveFunction() };
		Double[][] rt = model.getConstraintsValues(); 
		Double[][] id = matrixIdentity;
		
		int ofElements = of[0].length;
		int rows = of.length + rt.length;
		int cols = ofElements + rt.length + 1; 
		Double[][] tableau = tableau(rows, cols);

		System.arraycopy(of[0], 0, tableau[0], 0, of[0].length); // objective function
		for (int i = 1; i < tableau.length; i++) { // 
			System.arraycopy(rt[i-1], 0, tableau[i], 0, of[0].length); // constraints
			System.arraycopy(id[i-1], 0, tableau[i], ofElements, id[0].length); // identity
			tableau[i][cols-1] = rt[i-1][rt[i-1].length-1]; // base
		}
		
		model.add( tableau );
	}
	
}