package expr.composite;

public abstract class Var extends Expr {
	// normal variable value 
	public Expr value;
	
	// uninitialized declaration and verification
	public Var(String name) {
		this.name = name;
	}
}
