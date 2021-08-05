package expr.visitor;

import java.util.*;
import expr.composite.*;

public class z3Printer implements Visitor {
	
	public String prefixOutput;
	
	public static ArrayList<String> varList = new ArrayList<String>();
	
	public z3Printer() {
		prefixOutput = "";
	}

	public void visitBinaryExpr (BinaryExpr b, String op) {
		z3Printer leftPrinter = new z3Printer();
		z3Printer rightPrinter = new z3Printer();
		
		b.left().accept(leftPrinter);
		b.right().accept(rightPrinter);
		
		prefixOutput = prefixOutput.concat("(" + op + " "
				+ leftPrinter.prefixOutput + " " + rightPrinter.prefixOutput + ")");
	}
	
	public void visitUnaryExpr(UnaryExpr u, String op) {
		
		z3Printer p = new z3Printer();
		u.child.accept(p);
		prefixOutput = prefixOutput.concat("(" + op + " " + p.prefixOutput + ")");
	}
	
	@Override
	public void visitAnd(Conjunction conjunction) {
		visitBinaryExpr(conjunction, "and");
	}

	@Override
	public void visitOr(Disjunction disjunction) {
		visitBinaryExpr(disjunction, "or");
	}

	@Override
	public void visitNot(Negation negation) {
		visitUnaryExpr(negation, "not");
	}

	@Override
	public void visitBoolFalse(BoolFalse boolFalse) {
		prefixOutput = prefixOutput.concat("false");
	}

	@Override
	public void visitBoolTrue(BoolTrue boolTrue) {
		prefixOutput = prefixOutput.concat("true");
	}

	@Override
	public void visitBoolVar(BoolVar boolVar) {
		if (!varList.contains(boolVar.name)) {
			varList.add(boolVar.name);
		}
		prefixOutput = prefixOutput.concat(boolVar.name);
	}

}
