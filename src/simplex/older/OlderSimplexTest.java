package simplex.older;

//// example standardized form
//float[][] standardized = {
//      {1,1,1,0,4},
//      {1,3,0,1,6},
//      {-3,-5,0,0,0}
//};

public class OlderSimplexTest {

	public static void main(String[] args) {

		boolean quit = false;

// Example problem:
// maximize 3x + 5y 
// subject to x +  y = 4 and
//            x + 3y = 6
//		float[][] standardized = { 
//				{ 1, 1, 1, 0, 4 }, 
//				{ 1, 3, 0, 1, 6 }, 
//				{ -3, -5, 0, 0, 0 } 
//		};
		
		// caso fabio = 2,4 
		float[][] standardized = { 
				{ 2, 1, 1, 0, 8 }, 
				{ 2, 3, 0, 1, 12 }, 
				{ -4, -1, 0, 0, 0 } 
		};
		
		// caso youtube = 3,6? com bug
//		float[][] standardized = { 
//				{  2, 4, -5, 0,  0, 0,   0 },// Z funcao objetivo 
//				{  1, 2, 10, 1,  0, 0, 600 },// s1
//				{ -1, 2, -1, 0,  1, 0,  50 },// s2 - com/sem x * -1 
//				{  2, 0, -1, 0,  0, 1, 100 } // s3  
//		};
		
		

// row and column do not include
// right hand side values
// and objective row
		OlderSimplex simplex = new OlderSimplex(2, 4);

		simplex.fillTable(standardized);

// print it out
		System.out.println("---Starting set---");
		simplex.print();

// if table is not optimal re-iterate
		while (!quit) {
			OlderSimplex.ERROR err = simplex.compute();

			if (err == OlderSimplex.ERROR.IS_OPTIMAL) {
				simplex.print();
				quit = true;
			} else if (err == OlderSimplex.ERROR.UNBOUNDED) {
				System.out.println("---Solution is unbounded---");
				quit = true;
			}
		}
	}
}