package expressivo;

import java.util.Objects;

/**
 * Number 是 Expression 的一个不可变实现，表示一个数值常量。
 */
public final class Number implements Expression {
    private final double value;
    //  AF(value)

    /**
     * 创建一个数值表达式
     * @param value a non-negative double number
     * @throws IllegalArgumentException 如果 value 是 NaN 或无穷大
     */
    public Number(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Number that)) return false;
        return Double.compare(this.value, that.value) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
