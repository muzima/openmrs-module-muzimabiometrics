package org.javarosa.xform.parse;

import java.io.Serializable;

public class ValidationMessage implements Serializable {
    private final String message;
    private final Type type;

    public ValidationMessage(String message, Type type) {
        this.message = message;
        this.type = type;
    }

    public enum Type {
        WARNING, ERROR
    }

    public String getMessage() {
        return message;
    }

    public Type getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ValidationMessage that = (ValidationMessage) o;

        if (getMessage() != null ? !getMessage().equals(that.getMessage()) : that.getMessage() != null) return false;
        if (getType() != that.getType()) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = getMessage() != null ? getMessage().hashCode() : 0;
        result = 31 * result + (getType() != null ? getType().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ValidationMessage{" +
                "message='" + getMessage() + '\'' +
                ", type=" + getType() +
                '}';
    }

}
