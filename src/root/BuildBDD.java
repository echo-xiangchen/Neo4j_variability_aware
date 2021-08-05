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
import static info.scce.addlib.cudd.Cudd.*;
import static org.junit.Assert.assertEquals;

public class BuildBDD {
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
	        
	        BDDbuilder bddBuilder = new BDDbuilder();
	        // create a false BDD for checking satisfiability
	        long FF = Cudd_ReadLogicZero(BDDbuilder.ddManager);
	        long TT = Cudd_ReadOne(BDDbuilder.ddManager);
	        
	        // call the bddBuilder
	        expr.accept(bddBuilder);
			
	        
	        // test evaluation
	        if (bddBuilder.getBDDaddress() != FF) {
	        	System.out.println("Evaluate: satisfiable" );
			}else {
				System.out.println("Evaluate: not satisfiable" );
			}
	        
	        /* Release memory */
	        Cudd_Quit(BDDbuilder.ddManager);
	        
	        
//	        int[] assignment = {1, 1};
//	        long terminal = Cudd_Eval(BDDbuilder.ddManager, 
//	        		BDDbuilder.address.get(BDDbuilder.address.size() - 1), assignment);
//	        
//	        long zero = Cudd_ReadLogicZero(BDDbuilder.ddManager);
//	        System.out.println("[[ terminal == zero ]] = " + (terminal == zero));
//	        assertEquals(terminal, zero);
	        
	        
//	        /* Initialize DDManager with default values */
//	        long ddManager = Cudd_Init(0, 0, CUDD_UNIQUE_SLOTS, CUDD_CACHE_SLOTS, 0);
//
//	        /* Get the variables */
//	        long var0 = Cudd_bddIthVar(ddManager, 0);
//	        Cudd_Ref(var0);
//	        long var1 = Cudd_bddIthVar(ddManager, 1);
//	        Cudd_Ref(var1);
//
//	        /* Build the disjunction */
//	        long disjunction = Cudd_bddOr(ddManager, var0, var1);
//	        Cudd_Ref(disjunction);
//	        Cudd_RecursiveDeref(ddManager, var0);
//	        Cudd_RecursiveDeref(ddManager, var1);
//
//	        /* Evaluate disjunction for assignment var0 := 1, var1 := 0 */
//	        int[] assignment = {1, 0};
//	        long terminal = Cudd_Eval(ddManager, disjunction, assignment);
//
//	        /* See if the terminal is what we expect it to be */
//	        long one = Cudd_ReadOne(ddManager);
//	        Cudd_Ref(one);
//	        System.out.println("[[ terminal == one ]] = " + (terminal == one));
//	        assertEquals(terminal, one);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
