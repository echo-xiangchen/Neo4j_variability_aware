package root;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.io.*;
import java.util.*;
import java.util.Map.Entry;

import antlr.*;
import expr.*;
import expr.composite.*;
import expr.composite.Expr;
import expr.visitor.*;
import com.microsoft.z3.*;

public class TestZ3runtime {
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws FileNotFoundException {
		// arraylist for storing all the PCs
		ArrayList<String> pcList = new ArrayList<String>();
		
		// hashmap for storing <string, Expr> pair
		Map<String, Expr> z3exprMap = new LinkedHashMap<String, Expr>();
		
		// create variable for checking the runtime
        long startTime = System.nanoTime();
        long endTime = 0;
        
        // create scanner for reading the file
 		Scanner sc = new Scanner(new File("test.csv"));
 		
 		// store the data into arraylist pc
 		while (sc.hasNext()) {
 			pcList.add(sc.next());
 		}
 		System.out.println("Successfully load the file! File line: " + pcList.size());
 		
 		// set initial string to be pc[0]
		String pc = pcList.get(0);
		
        
        // initialize antlr2expr
        Antlr2Expr antlr2Expr = new Antlr2Expr();
        
        // create z3verifier
    	//z3verifier z3verifier = new z3verifier();
        
        // create solver
        Solver solver;
        // create result
        Status result;
        
        // create antlr parser object
        @SuppressWarnings("deprecation")
		ANTLRInputStream input;
        PCparserLexer lexer;
        CommonTokenStream tokens;
        PCparserParser parser;
        ParseTree tree;
        
        // parse initial string
		input = new ANTLRInputStream(pc);
        lexer = new PCparserLexer(input);
        tokens = new CommonTokenStream(lexer);
        parser = new PCparserParser(tokens);
        parser.setBuildParseTree(true);      // tell ANTLR to build a parse tree
        tree = parser.stat(); // parse
        
        // generate the Expr hierarchy for initial string and store it to the map
        Expr expr = antlr2Expr.visit(tree.getChild(0));
        z3exprMap.put(pc, expr);
        
        // z3 ver: looping the string list for checing satisfiability
        for (int i = 1; i < pcList.size(); i++) {
        	System.out.println("z3 Line: " + i);
        	// only when next string has not been parsed before, parse it
        	if (!z3exprMap.containsKey(pcList.get(i))) {
        		input = new ANTLRInputStream(pcList.get(i));
		        lexer = new PCparserLexer(input);
		        tokens = new CommonTokenStream(lexer);
		        parser = new PCparserParser(tokens);
		        parser.setBuildParseTree(true);      // tell ANTLR to build a parse tree
		        tree = parser.stat(); // parse
		        
		        Expr nextExpr = antlr2Expr.visit(tree.getChild(0));
		        
		        z3exprMap.put(pcList.get(i), nextExpr);
			}
        	
        	// create new conjunction = current expr && next expr
        	expr = new Conjunction(expr, z3exprMap.get(pcList.get(i)));
        	
        	// create z3verifier
        	z3verifier z3verifier = new z3verifier();
        	expr.accept(z3verifier);
        	
//        	PrettyPrinter printer = new PrettyPrinter();
//        	expr.accept(printer);
//        	System.out.println(printer.infixOutput);
        	
	        solver = z3verifier.getCtx().mkSolver();
			solver.add(z3verifier.getBoolExpr());
			result = solver.check();
			
			if (result == Status.UNSATISFIABLE) {
				break;
			}
			
			endTime = System.nanoTime();
	        // count runtime
	        long totalTime1 = (endTime - startTime);
	        System.out.println("z3 runtime: " + totalTime1 + " nanoseconds.");
        }
        
        
//        for (Entry<String, Expr> entry : z3exprMap.entrySet()) {
//        	System.out.println("Key: " + entry.getKey()  + "\nValue: " + '[' + entry.getValue() + ']');
//        }
	}
}
