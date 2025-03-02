package expressivo;

import java.util.Objects;

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
}
