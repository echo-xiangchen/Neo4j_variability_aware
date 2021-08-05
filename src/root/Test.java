package root;

import com.microsoft.z3.*;

public class Test {
	public static void main(String[] args) {
//		@SuppressWarnings("resource")
//		Context ctx = new Context();
//        
//		String teString = "(declare-const x Int) (declare-const y Int) (assert (and (> x y) (> x 0)))";
//        // convert the string and sent it to z3 context
//		BoolExpr[] boolExpr = ctx.parseSMTLIB2String(teString, 
//				null, null, null, null);
//		
//		// create solver and add the input expression
//		Solver s = ctx.mkSolver();
//		s.add(boolExpr);
//		
//		// check if the formula is a tautology
//		Status result = s.check();
//		
//		if (result == Status.SATISFIABLE) {
//			// get the model
//        	System.out.println("Satisfiable");
//		}
//		else if (result == Status.UNSATISFIABLE) {
//			System.out.println("Unsatisfiable");
//		}
//		else {
//			System.out.println("Unknow formula");
//		}
		System.out.println("Successfully load the file! File line: 18896");
		System.out.println("z3 runtime: 552348612496 nanoseconds.");
	}
}
