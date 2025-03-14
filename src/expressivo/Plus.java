package expressivo;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Plus 是 Expression 的一个不可变实现，表示两个表达式的加法运算。
 *
 * <p> 示例:
 * <pre>
 *     Expression expr = new Plus(new Variable("x"), new Number(3));  // 表示 (x + 3)
 * </pre>
 */
public final class Plus implements Expression {
    private final Expression left, right;

    // Abstraction Function:
    //  AF(left, right) = A mathematical addition expression representing (left + right)
    // Representation Invariant:
    //  left and right must not be null

    /**
     * 创建一个加法表达式
     *
     * @param left 左侧表达式，不能为 null
     * @param right 右侧表达式，不能为 null
     * @throws IllegalArgumentException 如果 left 或 right 为 null
     */
    public Plus(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    /**
     * 返回此加法表达式的字符串表示
     *
     * @return 形如 "(left + right)" 的字符串
     */
    @Override
    public String toString() {
        return "(" + left.toString() + " + " + right.toString() + ")";
    }

    /**
     * 判断当前加法表达式是否与另一个对象相等
     *
     * @param obj 需要比较的对象
     * @return 如果 obj 也是一个 `Plus`，并且其左右表达式与当前对象相等，则返回 true；否则返回 false
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Plus that)) return false;
        return this.left.equals(that.left) && this.right.equals(that.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }

    @Override
    public Expression differentiate(String var) {
        return new Plus(left.differentiate(var), right.differentiate(var));
    }

    @Override
    public Expression simplify(Map<String,Double> environment) {
        Expression leftExpr = left.simplify(environment);
        Expression rightExpr = right.simplify(environment);

        Optional<Double> leftResult = leftExpr.result();
        Optional<Double> rightResult = rightExpr.result();
        // 不含未知变量
        if (leftResult.isPresent() && rightResult.isPresent()) {
            return new Number(leftResult.get() + rightResult.get());
        }

        // 含有未知变量
        return new Plus(leftExpr, rightExpr);
    }

    @Override
    public Optional<Double> result() {
        Optional<Double> leftResult = left.result();
        Optional<Double> rightResult = right.result();
        if (leftResult.isPresent() && rightResult.isPresent()) {
            return Optional.of(leftResult.get() + rightResult.get());
        }
        return Optional.empty();
    }
}
