package simplex;

public class Simplex {
	
	public static void main(String[] args) {
		
		// colocar restricoes em uma classe que indique quando houver igualdade
		// retornar matriz inteira
		// retornar linha inteira
		// retornar apenas sinais
		
		Model model = Problems.P1;
		
		double[][] constraints = treatNegativeBasisIfAny( model.constraints );
		double[][] identity = mountArtificialVariables( constraints );
		double[][] tableau = mountTableau( model.objectiveFunction, constraints, identity );
		
		System.out.println("before");
		print( tableau );
		
		resolve( tableau, model.function );
		updateSignalResultObjectiveFunctionByMinusOneWhenMinimization( tableau, model.function );
		System.out.println("\nafter");
		print( tableau );
	}

	private static void updateSignalResultObjectiveFunctionByMinusOneWhenMinimization(double[][] tableau, Function function) {
		for (int i = 0; i < tableau[0].length; i++) {
			double value = tableau[0][i];
			if ( value == 0 ) continue;
			tableau[0][i] *= -1;			
		}
	}

	private static void print(double[][] tableau) {
		for (int i = 0; i < tableau.length; i++) {
			System.out.print( "line " + i + ": ");
			for (int j = 0; j < tableau[i].length; j++) {
				System.out.print( tableau[i][j] + ", " );
			}
			System.out.println();
		}
		System.out.println();
	}

	private static void resolve(double[][] tableau, Function function) {
		boolean quit = false;
		while (!quit) {
			if ( function.isOptimality(tableau) )
				break;
			
			int indexPivotColum = findIndexColumnEntering( tableau, function );
			int indexPivotRow = findIndexRowPivot( indexPivotColum, tableau, function );
			
			updateDivideElementsRowByBaseOfThePivot( tableau, indexPivotRow, indexPivotColum );
			updateZeroingElementsAgainstPivot( tableau, indexPivotRow, indexPivotColum );
		}
	}
	
	private static void updateObjectiveFunctionByMinusOne(double[][] tableau) {
		for (int i = 0; i < tableau[0].length; i++) {
			if ( tableau[0][i] == 0 ) continue;
			tableau[0][i] *= -1; 
		}
	}

	private static void updateZeroingElementsAgainstPivot(double[][] tableau, int row, int col) {
		for (int i = 0; i < tableau.length; i++) {
			if ( i == row ) continue;
			double px = tableau[i][col];
			for (int j = 0; j < tableau[i].length; j++) {
				double r1 = tableau[i][j];				
				double py = tableau[row][j];
				
				double r2 = (px * py);
				double cl = (r2 > 0) ? r1 - r2 : r1 + (r2 * -1);
				tableau[i][j] = cl;
			}
		}
	}

	private static void updateDivideElementsRowByBaseOfThePivot(double[][] tableau, int row, int col) {
		double pivot = tableau[row][col];
		for (int i = 0; i < tableau[row].length; i++) {
			tableau[row][i] /= pivot; 
		}
	}

	private static int findIndexRowPivot(int column, double[][] tableau, Function function) {
		int row = 0;
		double lastAccepted = function.initialInverseAcceptedRow();
		for (int i = 1; i < tableau.length; i++) {
			double element = tableau[i][column];
			double ratio = tableau[i][tableau[i].length-1] / element;
			if ( function.acceptRow(lastAccepted, ratio) ) {
				lastAccepted = ratio;
				row = i;
			}
		}
		return row;
	}

	private static int findIndexColumnEntering(double[][] tableau, Function function) {
		int column = 0;
		double lastAccepted = function.initialInverseAcceptedCol();
		for (int i = 0; i < tableau.length; i++) {
			for (int j = 0; j < tableau[i].length-1; j++) {
				if ( function.acceptCol(lastAccepted, tableau[i][j]) ) { 
					lastAccepted = tableau[i][j];
					column = j;
				}
			} 
		}
		return column;
	}

	public static double[][] treatNegativeBasisIfAny(double[][] constraints) {
		for(int i = 0; i < constraints.length; i++){
			int lastIndex = constraints[i].length-1;
            if ( constraints[i][lastIndex] < 0 ) {
            	do {
            		constraints[i][lastIndex] = constraints[i][lastIndex] * -1; 
            	} while(--lastIndex >= 0);
            }
        }
		return constraints;
	}

	// detectar signal
	// atribuir valores ao s1, s2, s3 ... negativo ou positivo
	// apos atribuir ver se tem como montar matriz identidade
	// se nao puder, vai criar os a1, a2, a3
	private static double[][] mountIdentityMatrizAndArtificialVariables(Constraint ... constraints) {
		for (Constraint cts : constraints) {
			System.out.println( cts.variables );
			System.out.println( cts.signal );
			System.out.println( cts.value );
		}
		return null;
	}
	
	private static double[][] mountArtificialVariables(double[][] rt) {
		// TODO what if matriz 5x4? ... I assemble 5x5
		
		double[][] identity = new double[rt.length][rt.length]; 
		int indexSetOne = 0;
		for (int i = 0; i < rt.length; i++) {
			identity[i][indexSetOne++] = 1;
		}
		return identity;
	}
	
	private static double[][] mountTableau(double[][] of, double[][] rt, double[][] id) {
		int ofElements = of[0].length;
		int rows = of.length + rt.length;
		int cols = ofElements + rt.length; 
		double[][] tableau = new double[rows][cols];
		
		System.arraycopy(of[0], 0, tableau[0], 0, of[0].length); // objective function
		for (int i = 1; i < tableau.length; i++) { // 
			System.arraycopy(rt[i-1], 0, tableau[i], 0, of[0].length); // constraints
			System.arraycopy(id[i-1], 0, tableau[i], ofElements-1, id[0].length); // identity
			tableau[i][cols-1] = rt[i-1][rt[i-1].length-1]; // base
		}
		return tableau;
	}
	
}