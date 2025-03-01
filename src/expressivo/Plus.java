package expressivo;

import java.util.Objects;

public final class Plus implements Expression {
    private final Expression left, right;

    public Plus(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return "(" + left.toString() + " + " + right.toString() + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Plus that)) return false;
        return this.left.equals(that.left) && this.right.equals(that.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }
}
