package expr.visitor;

import expr.composite.*;
import static info.scce.addlib.cudd.Cudd.*;

public class BDDbuilder implements Visitor {
	
	/* Initialize DDManager with default values */
	long ddManager = Cudd_Init(0, 0, CUDD_UNIQUE_SLOTS, CUDD_CACHE_SLOTS, 0);

	@Override
	public void visitAnd(Conjunction conjunction) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitOr(Disjunction disjunction) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitNot(Negation negation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitBoolFalse(BoolFalse boolFalse) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitBoolTrue(BoolTrue boolTrue) {
		// TODO Auto-generated method stub
		
	}
}
