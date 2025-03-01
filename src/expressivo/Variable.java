package expressivo;

import java.util.Objects;

public final class Variable implements Expression {
    private final String name;

    public Variable(String name) {
        if (!name.matches("[a-zA-Z]+")) {
            throw new IllegalArgumentException("变量名必须是非空的字母字符串");
        }
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

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
