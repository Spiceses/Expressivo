package expressivo;

import java.util.Objects;

/**
 * Variable 是 Expression 的一个不可变实现，表示变量（如 "x"、"y"）。
 *
 * <p> 示例:
 * <pre>
 *     Expression var1 = new Variable("x");  // 表示变量 x
 *     Expression var2 = new Variable("y");  // 表示变量 y
 * </pre>
 */
public final class Variable implements Expression {
    private final String name;

    /**
     * 创建一个变量表达式
     *
     * @param name 变量名称，必须是非空的字母字符串
     */
    public Variable(String name) {
        if (!name.matches("[a-zA-Z]+")) {
            throw new IllegalArgumentException("变量名必须是非空的字母字符串");
        }
        this.name = name;
    }

    /**
     * 返回此变量的字符串表示
     *
     * @return 变量名称
     */
    @Override
    public String toString() {
        return name;
    }

    /**
     * 判断当前变量表达式是否与另一个对象相等
     *
     * @param obj 需要比较的对象
     * @return 如果 obj 也是一个 `Variable`，且其名称与当前对象相等，则返回 true；否则返回 false
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Variable that)) return false;
        return this.name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
