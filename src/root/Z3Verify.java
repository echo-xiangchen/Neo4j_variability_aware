package root;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.io.*;
import java.util.*;
import antlr.*;
import expr.*;
import expr.composite.*;
import expr.composite.Expr;
import expr.visitor.*;
import com.microsoft.z3.*;

public class Z3Verify {
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		try {
			// test to see if there is a input file
	        String inputFile = null;
	        if ( args.length > 1 ) inputFile = args[1];
	        InputStream is = System.in;
	        if ( inputFile!=null ) {
	            is = new FileInputStream(inputFile);
	        }
	        
	        @SuppressWarnings("deprecation")
			ANTLRInputStream input = new ANTLRInputStream(is);
	        PCparserLexer lexer = new PCparserLexer(input);
	        CommonTokenStream tokens = new CommonTokenStream(lexer);
	        PCparserParser parser = new PCparserParser(tokens);
	        parser.setBuildParseTree(true);      // tell ANTLR to build a parse tree
	        ParseTree tree = parser.stat(); // parse
	        
	        Antlr2Expr antlr2Expr = new Antlr2Expr();
	        
	        // generate the Expr hierarchy 
	        Expr expr = antlr2Expr.visit(tree.getChild(0));
	        
	        z3verifier z3verifier = new z3verifier();
	        
	        expr.accept(z3verifier);
	        
	        // create solver and add the input expression
			Solver s = z3verifier.getCtx().mkSolver();
			s.add(z3verifier.getBoolExpr());
			
			// check if the formula is a tautology
			Status result = s.check();
			
			if (result == Status.SATISFIABLE) {
				// get the model
	         	System.out.println("Satisfiable");
			}
			else if (result == Status.UNSATISFIABLE) {
				System.out.println("Unsatisfiable");
			}
			else {
				System.out.println("Unknow formula");
			}
	        
//	        z3Printer z3Printer = new z3Printer();
//	        
//	        expr.accept(z3Printer);
//			
//	        
//	        String z3string = "";
//	        
//	        if (!z3Printer.varList.isEmpty()) {
//				for (int i = 0; i < z3Printer.varList.size(); i++) {
//					z3string = z3string.concat("(declare-const " + z3Printer.varList.get(i) + " Bool)\n");
//				}
//			}
//	        
//	        z3string = z3string.concat("(assert " + z3Printer.prefixOutput + ")");
//	        
//	        //System.out.println(z3string);
//	       
//	        //System.out.println(z3Printer.prefixOutput);
//	        
//	        @SuppressWarnings("resource")
//			Context ctx = new Context();
//	        
//	        // convert the string and sent it to z3 context
//			BoolExpr[] boolExpr = ctx.parseSMTLIB2String(z3string, 
//					null, null, null, null);
//			
//			// create solver and add the input expression
//			Solver s = ctx.mkSolver();
//			s.add(boolExpr);
//			
//			// check if the formula is a tautology
//			Status result = s.check();
//			
//			if (result == Status.SATISFIABLE) {
//				// get the model
//            	System.out.println("Satisfiable");
//			}
//			else if (result == Status.UNSATISFIABLE) {
//				System.out.println("Unsatisfiable");
//			}
//			else {
//				System.out.println("Unknow formula");
//			}
	        
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
