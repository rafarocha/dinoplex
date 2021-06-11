package simplex;

import static simplex.Signal.GreaterEqual; 
import static simplex.Signal.Equal;
import static simplex.Signal.LessEqual;

public interface Problems {
	
	// https://www.youtube.com/watch?v=EzMCem2YhBM
	Model P1 = new Model(
		Function.MIN, 
		new Expression( 2d, 4d, -5d ),
		new Constraint( new El(1d,2d,10d), LessEqual, 600d),
		new Constraint( new El(1d,-2d,1d), GreaterEqual, -50d), // 2d checar
		new Constraint( new El(2d,0d,-1d), LessEqual, 100d)
	);
	
	// exemplo enviado por fabio
	Model P2 = new Model(
		Function.MAX, 
		new Expression( 4d, 1d ),
		new Constraint( new El(2d,1d), LessEqual, 8d),
		new Constraint( new El(2d,3d), LessEqual, 12d) /// checar signal
	);
	
	// https://www.youtube.com/watch?v=w047PccELc4
	Model P3 = new Model(
		Function.MAX, 
		new Expression( 10d, 8d, 1d ),
		new Constraint( new El(3d,3d,2d), LessEqual, 30d),
		new Constraint( new El(6d,3d,0d), LessEqual, 48d)
	);
	
	// https://www.youtube.com/watch?v=FEMo2QlogKI
	Model P4 = new Model( // two phases
		Function.MIN, 
		new Expression( 1700d, 750d, 800d ),
		new Constraint( new El(2d,2d,5d), GreaterEqual, 20d),
		new Constraint( new El(3d,1d,5d), Equal, 10d)
	);
	
	// https://www.youtube.com/watch?v=dnmWcr6Ho7c&list=PLf1lowbdbFIC4ttkw9UWvy1lVDVaGzukh&index=3
	Model P5 = new Model( // big M
		Function.MIN, 
		new Expression( 3d, 2d, 5d ),
		new Constraint( new El(1d,3d, 2d), LessEqual, 15d),
		new Constraint( new El(0d,2d,-1d), GreaterEqual, 5d),
		new Constraint( new El(2d,1d,-5d), Equal, 10d)
	);
	
	// slide: lesson 5 page 53, two phases and big M implementations, same problem
	Model P6 = new Model( 
		Function.MIN, 
		new Expression( 0.4d, 0.5d ),
		new Constraint( new El(0.3d,0.1d), LessEqual, 2.7d),
		new Constraint( new El(0.5d,0.5d), Equal, 6d),
		new Constraint( new El(0.6d,0.4d), GreaterEqual, 6d)
	);
	
}