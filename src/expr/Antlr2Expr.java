package expr;

import java.util.*;
import antlr.*;
import antlr.PCparserParser.*;
import types.*;
import expr.composite.*;

public class Antlr2Expr extends PCparserBaseVisitor<Expr>{
	
	
	/* *****************************************************************************************
	 * TODO Methods for boolExpr rule
	 * *****************************************************************************************
	 */
	
	// Negation
	@Override
	public Expr visitNot(NotContext ctx) {
		return new Negation(visit(ctx.boolExpr()));
	}
	
	// Conjunction
	@Override
	public Expr visitAnd(AndContext ctx) {
		return new Conjunction(visit(ctx.boolExpr(0)), visit(ctx.boolExpr(1)));
	}
	
	// Disjunction
	@Override
	public Expr visitOr(OrContext ctx) {
		return new Disjunction(visit(ctx.boolExpr(0)), visit(ctx.boolExpr(1)));
	}
		
	
	// boolean variable verification
	@Override
	public Expr visitBoolVar(BoolVarContext ctx) {
		return new BoolVar(ctx.ID().getText());
	}
	
	
	// boolean true declaration
	@Override
	public Expr visitBoolTrue(BoolTrueContext ctx) {
		return new BoolTrue(ctx.TRUE().getText());
	}
	
	// boolean false declaration
	@Override
	public Expr visitBoolFalse(BoolFalseContext ctx) {
		return new BoolFalse(ctx.FALSE().getText());
	}
	
	
	// parentheses
	@Override
	public Expr visitParen(ParenContext ctx) {
		return visit(ctx.boolExpr());
	}
}
