package root;

import expr.composite.*;
import java.util.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.io.*;
import antlr.*;
import expr.*;
import expr.composite.Expr;
import expr.visitor.*;
import static info.scce.addlib.cudd.Cudd.*;


public class TestBDDruntime {
	public static void main(String[] args) throws FileNotFoundException {
		// arraylist for storing all the PCs
		ArrayList<String> pcList = new ArrayList<String>();
		
		// hashmap for storing <string, Expr> pair
		Map<String, Expr> BDDexprMap = new LinkedHashMap<String, Expr>();
		
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
        
        // create BDDbuilder
        BDDbuilder bddBuilder = new BDDbuilder();
        // create a false BDD for checking satisfiability
        long FF = Cudd_ReadLogicZero(BDDbuilder.ddManager);
        //long TT = Cudd_ReadOne(BDDbuilder.ddManager);
        
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
        BDDexprMap.put(pc, expr);
        
        // BDD ver: looping the string list for checing satisfiability
        for (int i = 1; i < pcList.size(); i++) {
        	
        	//System.out.println("BDD Line: " + i);
        	
        	// only when next string has not been parsed before, parse it
        	if (!BDDexprMap.containsKey(pcList.get(i))) {
        		input = new ANTLRInputStream(pcList.get(i));
		        lexer = new PCparserLexer(input);
		        tokens = new CommonTokenStream(lexer);
		        parser = new PCparserParser(tokens);
		        parser.setBuildParseTree(true);      // tell ANTLR to build a parse tree
		        tree = parser.stat(); // parse
		        
		        Expr nextExpr = antlr2Expr.visit(tree.getChild(0));
		        
		        BDDexprMap.put(pcList.get(i), nextExpr);
			}
        	
        	// create new conjunction = current expr && next expr
        	expr = new Conjunction(expr, BDDexprMap.get(pcList.get(i)));
        	
//        	PrettyPrinter printer = new PrettyPrinter();
//        	expr.accept(printer);
//        	System.out.println(printer.infixOutput);
        	
        	// call bddbuilder to check it's satisfiability
        	expr.accept(bddBuilder);
        	
        	if (bddBuilder.getBDDaddress() == FF) {
				break;
			}
        }
        
        endTime = System.nanoTime();
        // count runtime
        long totalTime1 = (endTime - startTime);
        System.out.println("BDD runtime: " + totalTime1 + " nanoseconds.");
        
//        for (Entry<String, Expr> entry : BDDexprMap.entrySet()) {
//            System.out.println("Key: " + entry.getKey()  + "\nValue: " + '[' + entry.getValue() + ']');
//        }
	}
}

