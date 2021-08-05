package expr.visitor;

import expr.composite.*;
import com.microsoft.z3.*;

public class z3verifier implements Visitor {
	
	private static Context ctx = new Context();
	
	private static BoolExpr boolExpr = null;
	
	public Context getCtx() {
		return ctx;
	}
	
	public BoolExpr getBoolExpr() {
		return boolExpr;
	}

	@Override
	public void visitAnd(Conjunction conjunction) {
		z3verifier left = new z3verifier();
		z3verifier right = new z3verifier();
		
		// store left child and right child
		conjunction.left().accept(left);
		BoolExpr leftExpr = boolExpr;
		
		conjunction.right().accept(right);
		BoolExpr rightExpr = boolExpr;
		
		boolExpr = ctx.mkAnd(leftExpr, rightExpr);
	}

	@Override
	public void visitOr(Disjunction disjunction) {
		z3verifier left = new z3verifier();
		z3verifier right = new z3verifier();
		
		// store left child and right child
		disjunction.left().accept(left);
		BoolExpr leftExpr = boolExpr;
		
		disjunction.right().accept(right);
		BoolExpr rightExpr = boolExpr;
		
		boolExpr = ctx.mkAnd(leftExpr, rightExpr);
	}

	@Override
	public void visitNot(Negation negation) {
		z3verifier p = new z3verifier();
		negation.child.accept(p);
		
		BoolExpr not = boolExpr;
		
		boolExpr = ctx.mkNot(not);
		
	}

	@Override
	public void visitBoolFalse(BoolFalse boolFalse) {
		boolExpr = ctx.mkFalse();
		
	}

	@Override
	public void visitBoolTrue(BoolTrue boolTrue) {
		boolExpr = ctx.mkTrue();
	}

	@Override
	public void visitBoolVar(BoolVar boolVar) {
		boolExpr = ctx.mkBoolConst(boolVar.name);
	}

}
