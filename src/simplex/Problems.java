package simplex;

import static simplex.Signal.*;

public interface Problems {
	
	// https://www.youtube.com/watch?v=EzMCem2YhBM
	Model P1 = new Model(
		Function.MIN, 
		new double[][] {{ 2,  4, -5, 0 }},
		new double[][] {
			{  1,  2, 10, 600 },
			{  1, -2, 1,  -50 }, 
			{  2,  0, -1, 100 }
		}
	);
	
	
	// exemplo enviado por fabio
	Model P2 = new Model(
		Function.MAX, 
		new double[][] {{ 4,  1, 0 }},
		new double[][] {
			{  2, 1,  8 },
			{  2, 3, 12 } 
		}
	);
	
	// https://www.youtube.com/watch?v=w047PccELc4
	Model P3 = new Model(
		Function.MAX, 
		new double[][] {{ 10,  8, 1, 0 }},
		new double[][] {
			{  3, 3, 2, 30 },
			{  6, 3, 0, 48 } 
		}
	);
	
	// https://www.youtube.com/watch?v=FEMo2QlogKI
	Model P4 = new Model( // two phases
		Function.MAX, 
		new double[][] {{ 1700, 750, 800, 0 }},
		new double[][] {
			{  2, 2, 5, 20 },
			{  3, 1, 5, 10 } 
		}
	);
	
	// https://www.youtube.com/watch?v=dnmWcr6Ho7c&list=PLf1lowbdbFIC4ttkw9UWvy1lVDVaGzukh&index=3
	Model P5 = new Model( // big M
		Function.MAX, 
		new double[][] {{ 3, 2, 5, 0 }},
		new double[][] {
			{  1, 3,  2, 15 },
			{  0, 2, -1, 5 }, 
			{  2, 1, -5, 10 } 
		}
	);
	
	// new test
	Model P6 = new Model(
		Function.MAX, 
		new Expression( 3d, 2d, 5d, 0d ),
		new Constraint( new El(1d,3d,2d), GreaterEqual, 15d)
	);
	
}