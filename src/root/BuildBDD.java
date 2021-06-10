package root;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.Pair;
import org.antlr.v4.runtime.tree.*;
import java.io.*;
import java.util.*;
import java.util.Map.*;

import antlr.*;
import expr.*;
import expr.composite.*;
import expr.visitor.*;

public class BuildBDD {
	public static void main(String[] args) {
		try {
			// if there is no argument
			if (args.length < 1) {
				System.out.println("Please provide files contain boolean expression.");
			} else {
				// test to see if there is a input file
		        String inputFile = null;
		        if ( args.length>1 ) inputFile = args[1];
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
		        
		        // list that stores the subtree separately
		        List<Expr> exprList = new ArrayList<Expr>();
		        
		        for (int i = 0; i < tree.getChildCount(); i++) {
		        	exprList.add(antlr2Expr.visit(tree.getChild(i)));
				}
		        
		        BDDbuilder bdd = new BDDbuilder();
		        
		        // call the BDDbuilder
		        for (int i = 0; i < exprList.size(); i++) {
		        	exprList.get(i).accept(bdd);
				}
		        
		        
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
