package expressivo;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Number 是 Expression 的一个不可变实现，表示一个数值常量。
 *
 * <p> 示例:
 * <pre>
 *     Expression num1 = new Number(3.5);   // 表示数值 3.5
 *     Expression num2 = new Number(0);     // 表示数值 0
 * </pre>
 */
public final class Number implements Expression {
    private final double value;
    //  AF(value)

    /**
     * 创建一个数值表达式
     *
     * @param value 非负的有限 double 数值
     */
    public Number(double value) {
        this.value = value;
    }

    /**
     * 返回此数值表达式的字符串表示
     *
     * @return 该数值的字符串表示
     */
    @Override
    public String toString() {
        return String.valueOf(value);
    }

    /**
     * 判断当前数值表达式是否与另一个对象相等
     *
     * @param obj 需要比较的对象
     * @return 如果 obj 也是一个 `Number`，且其值与当前对象相等，则返回 true；否则返回 false
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Number that)) return false;
        return Double.compare(this.value, that.value) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public Expression differentiate(String var) {
        return new Number(0);
    }

    @Override
    public Expression simplify(Map<String,Double> environment) {
        return this;
    }

    @Override
    public Optional<Double> result() {
        return Optional.of(value);
    }
}
